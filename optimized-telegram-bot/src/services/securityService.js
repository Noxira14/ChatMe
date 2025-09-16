const config = require('../config/config');
const logger = require('../utils/logger');
const Helpers = require('../utils/helpers');
const axios = require('axios');

class SecurityService {
  constructor() {
    this.monitoringActive = false;
    this.trustedUsers = new Set();
    this.securityAlerts = [];
    this.monitoringInterval = null;
    this.loadSecurityConfig();
  }

  /**
   * Load security configuration
   */
  async loadSecurityConfig() {
    try {
      const securityData = await Helpers.readJsonFile(config.paths.securityFile, {
        trustedUsers: [],
        alerts: [],
        monitoring: false
      });
      
      this.trustedUsers = new Set(securityData.trustedUsers || []);
      this.securityAlerts = securityData.alerts || [];
      this.monitoringActive = securityData.monitoring || false;
      
      logger.info(`Security config loaded: ${this.trustedUsers.size} trusted users`);
    } catch (error) {
      logger.error('Failed to load security config', error);
    }
  }

  /**
   * Save security configuration
   */
  async saveSecurityConfig() {
    try {
      const securityData = {
        trustedUsers: Array.from(this.trustedUsers),
        alerts: this.securityAlerts.slice(-100), // Keep last 100 alerts
        monitoring: this.monitoringActive
      };
      
      await Helpers.writeJsonFile(config.paths.securityFile, securityData);
      logger.info('Security config saved');
    } catch (error) {
      logger.error('Failed to save security config', error);
    }
  }

  /**
   * Start security monitoring
   */
  async startMonitoring() {
    if (this.monitoringActive) {
      logger.warn('Security monitoring already active');
      return;
    }

    this.monitoringActive = true;
    await this.saveSecurityConfig();

    // Monitor panel access every 30 seconds
    this.monitoringInterval = setInterval(async () => {
      await this.checkPanelSecurity();
    }, 30000);

    logger.success('Security monitoring started');
  }

  /**
   * Stop security monitoring
   */
  async stopMonitoring() {
    if (!this.monitoringActive) {
      logger.warn('Security monitoring not active');
      return;
    }

    this.monitoringActive = false;
    if (this.monitoringInterval) {
      clearInterval(this.monitoringInterval);
      this.monitoringInterval = null;
    }

    await this.saveSecurityConfig();
    logger.info('Security monitoring stopped');
  }

  /**
   * Add trusted user
   */
  async addTrustedUser(identifier, type = 'username') {
    this.trustedUsers.add(`${type}:${identifier}`);
    await this.saveSecurityConfig();
    logger.info(`Added trusted user: ${type}:${identifier}`);
  }

  /**
   * Remove trusted user
   */
  async removeTrustedUser(identifier, type = 'username') {
    const removed = this.trustedUsers.delete(`${type}:${identifier}`);
    if (removed) {
      await this.saveSecurityConfig();
      logger.info(`Removed trusted user: ${type}:${identifier}`);
    }
    return removed;
  }

  /**
   * Check if user is trusted
   */
  isTrustedUser(username, email, ip) {
    return (
      this.trustedUsers.has(`username:${username}`) ||
      this.trustedUsers.has(`email:${email}`) ||
      this.trustedUsers.has(`ip:${ip}`)
    );
  }

  /**
   * Check panel security
   */
  async checkPanelSecurity() {
    try {
      const servers = await this.getAllServers();
      
      for (const server of servers) {
        await this.monitorServerActivity(server);
      }
    } catch (error) {
      logger.error('Error checking panel security', error);
    }
  }

  /**
   * Monitor server activity
   */
  async monitorServerActivity(server) {
    try {
      // Check recent logins
      const recentLogins = await this.getRecentLogins(server);
      
      for (const login of recentLogins) {
        if (!this.isTrustedUser(login.username, login.email, login.ip)) {
          await this.createSecurityAlert('suspicious_login', {
            server: server.name,
            username: login.username,
            email: login.email,
            ip: login.ip,
            timestamp: login.timestamp
          });
        }
      }

      // Check file access
      const fileAccess = await this.getFileAccessLogs(server);
      
      for (const access of fileAccess) {
        if (this.isSuspiciousFileAccess(access) && 
            !this.isTrustedUser(access.username, access.email, access.ip)) {
          await this.createSecurityAlert('suspicious_file_access', {
            server: server.name,
            username: access.username,
            email: access.email,
            ip: access.ip,
            file: access.file,
            action: access.action,
            timestamp: access.timestamp
          });
        }
      }

    } catch (error) {
      logger.error(`Error monitoring server ${server.name}`, error);
    }
  }

  /**
   * Get recent logins from server
   */
  async getRecentLogins(server) {
    try {
      const response = await axios.get(`${server.domain}/api/application/users`, {
        headers: {
          'Authorization': `Bearer ${server.apiKey}`,
          'Content-Type': 'application/json'
        },
        timeout: 10000
      });

      // This is a mock implementation
      // In real scenario, you would parse actual login logs
      return response.data.data?.map(user => ({
        username: user.attributes.username,
        email: user.attributes.email,
        ip: user.attributes.last_login_ip || 'unknown',
        timestamp: user.attributes.updated_at
      })) || [];

    } catch (error) {
      logger.error(`Failed to get recent logins from ${server.name}`, error);
      return [];
    }
  }

  /**
   * Get file access logs
   */
  async getFileAccessLogs(server) {
    try {
      // This is a mock implementation
      // In real scenario, you would integrate with panel's audit logs
      return [];
    } catch (error) {
      logger.error(`Failed to get file access logs from ${server.name}`, error);
      return [];
    }
  }

  /**
   * Check if file access is suspicious
   */
  isSuspiciousFileAccess(access) {
    const suspiciousPatterns = [
      /\.env$/,
      /config\.php$/,
      /database\.php$/,
      /\.sql$/,
      /backup/i,
      /admin/i,
      /password/i
    ];

    return suspiciousPatterns.some(pattern => pattern.test(access.file));
  }

  /**
   * Create security alert
   */
  async createSecurityAlert(type, data) {
    const alert = {
      id: Helpers.generateUniqueId(),
      type,
      data,
      timestamp: new Date().toISOString(),
      notified: false
    };

    this.securityAlerts.push(alert);
    await this.saveSecurityConfig();

    // Send notification
    await this.sendSecurityNotification(alert);

    logger.warn(`Security alert created: ${type}`, data);
  }

  /**
   * Send security notification
   */
  async sendSecurityNotification(alert) {
    try {
      let message = '';
      
      if (alert.type === 'suspicious_login') {
        message = `🚨 *PERINGATAN KEAMANAN*\n\n` +
                  `⚠️ *Wah Ada Yang Ngintip Panel Nih*\n\n` +
                  `👤 *Username:* \`${alert.data.username}\`\n` +
                  `📧 *Email:* \`${alert.data.email}\`\n` +
                  `🌐 *IP:* \`${alert.data.ip}\`\n` +
                  `🖥️ *Server:* \`${alert.data.server}\`\n` +
                  `⏰ *Waktu:* ${new Date(alert.data.timestamp).toLocaleString('id-ID')}`;
      } else if (alert.type === 'suspicious_file_access') {
        message = `🚨 *PERINGATAN KEAMANAN*\n\n` +
                  `⚠️ *Wah Ada Yang Ngambil Script Orang Nih*\n\n` +
                  `👤 *Username:* \`${alert.data.username}\`\n` +
                  `📧 *Email:* \`${alert.data.email}\`\n` +
                  `🌐 *IP:* \`${alert.data.ip}\`\n` +
                  `📁 *File:* \`${alert.data.file}\`\n` +
                  `🔧 *Action:* \`${alert.data.action}\`\n` +
                  `🖥️ *Server:* \`${alert.data.server}\`\n` +
                  `⏰ *Waktu:* ${new Date(alert.data.timestamp).toLocaleString('id-ID')}`;
      }

      // This would be implemented in the bot class
      // For now, we'll just log it
      logger.security('Security notification ready', { message, alert });

      alert.notified = true;
      await this.saveSecurityConfig();

    } catch (error) {
      logger.error('Failed to send security notification', error);
    }
  }

  /**
   * Get all servers for monitoring
   */
  async getAllServers() {
    try {
      const serverData = await Helpers.readJsonFile(config.paths.serverFile, []);
      return serverData;
    } catch (error) {
      logger.error('Failed to get servers for security monitoring', error);
      return [];
    }
  }

  /**
   * Get security statistics
   */
  getSecurityStats() {
    const now = new Date();
    const last24h = new Date(now.getTime() - 24 * 60 * 60 * 1000);
    
    const recentAlerts = this.securityAlerts.filter(alert => 
      new Date(alert.timestamp) > last24h
    );

    const alertsByType = recentAlerts.reduce((acc, alert) => {
      acc[alert.type] = (acc[alert.type] || 0) + 1;
      return acc;
    }, {});

    return {
      monitoring: this.monitoringActive,
      trustedUsers: this.trustedUsers.size,
      totalAlerts: this.securityAlerts.length,
      recentAlerts: recentAlerts.length,
      alertsByType
    };
  }

  /**
   * Get recent alerts
   */
  getRecentAlerts(limit = 10) {
    return this.securityAlerts
      .slice(-limit)
      .reverse();
  }

  /**
   * Clear old alerts
   */
  async clearOldAlerts(days = 30) {
    const cutoff = new Date();
    cutoff.setDate(cutoff.getDate() - days);

    const initialCount = this.securityAlerts.length;
    this.securityAlerts = this.securityAlerts.filter(alert => 
      new Date(alert.timestamp) > cutoff
    );

    const removed = initialCount - this.securityAlerts.length;
    if (removed > 0) {
      await this.saveSecurityConfig();
      logger.info(`Cleared ${removed} old security alerts`);
    }

    return removed;
  }
}

// Create singleton instance
const securityService = new SecurityService();

module.exports = securityService;
