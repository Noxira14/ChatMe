const config = require('../config/config');
const logger = require('../utils/logger');
const Helpers = require('../utils/helpers');
const panelService = require('./panelService');
const axios = require('axios');

class CleanupService {
  constructor() {
    this.cleanupInterval = null;
    this.isRunning = false;
    this.cleanupHistory = [];
    this.inactiveThreshold = 2 * 24 * 60 * 60 * 1000; // 2 days in milliseconds
    this.loadCleanupConfig();
  }

  /**
   * Load cleanup configuration
   */
  async loadCleanupConfig() {
    try {
      const cleanupData = await Helpers.readJsonFile(config.paths.cleanupFile, {
        history: [],
        lastCleanup: null,
        autoCleanup: false
      });
      
      this.cleanupHistory = cleanupData.history || [];
      this.lastCleanup = cleanupData.lastCleanup;
      this.autoCleanup = cleanupData.autoCleanup || false;
      
      logger.info('Cleanup config loaded');
    } catch (error) {
      logger.error('Failed to load cleanup config', error);
    }
  }

  /**
   * Save cleanup configuration
   */
  async saveCleanupConfig() {
    try {
      const cleanupData = {
        history: this.cleanupHistory.slice(-50), // Keep last 50 cleanup records
        lastCleanup: this.lastCleanup,
        autoCleanup: this.autoCleanup
      };
      
      await Helpers.writeJsonFile(config.paths.cleanupFile, cleanupData);
      logger.info('Cleanup config saved');
    } catch (error) {
      logger.error('Failed to save cleanup config', error);
    }
  }

  /**
   * Start automatic cleanup (every 5 hours)
   */
  startAutoCleanup() {
    if (this.isRunning) {
      logger.warn('Auto cleanup already running');
      return;
    }

    this.isRunning = true;
    this.autoCleanup = true;

    // Run cleanup every 5 hours
    this.cleanupInterval = setInterval(async () => {
      await this.performCleanup();
    }, 5 * 60 * 60 * 1000); // 5 hours

    // Run initial cleanup after 1 minute
    setTimeout(async () => {
      await this.performCleanup();
    }, 60000);

    logger.success('Auto cleanup started (every 5 hours)');
  }

  /**
   * Stop automatic cleanup
   */
  stopAutoCleanup() {
    if (!this.isRunning) {
      logger.warn('Auto cleanup not running');
      return;
    }

    this.isRunning = false;
    this.autoCleanup = false;

    if (this.cleanupInterval) {
      clearInterval(this.cleanupInterval);
      this.cleanupInterval = null;
    }

    logger.info('Auto cleanup stopped');
  }

  /**
   * Perform cleanup operation
   */
  async performCleanup() {
    try {
      logger.info('Starting cleanup operation...');
      
      const startTime = Date.now();
      const servers = panelService.getAllServers();
      const inactiveServers = [];
      let totalChecked = 0;
      let totalInactive = 0;

      for (const server of servers) {
        try {
          const serverInactive = await this.checkServerActivity(server);
          totalChecked++;
          
          if (serverInactive.length > 0) {
            inactiveServers.push({
              server: server.name,
              domain: server.domain,
              inactive: serverInactive
            });
            totalInactive += serverInactive.length;
          }
        } catch (error) {
          logger.error(`Error checking server ${server.name}`, error);
        }
      }

      const cleanupResult = {
        id: Helpers.generateUniqueId(),
        timestamp: new Date().toISOString(),
        duration: Date.now() - startTime,
        serversChecked: totalChecked,
        inactiveFound: totalInactive,
        inactiveServers,
        notified: false
      };

      this.cleanupHistory.push(cleanupResult);
      this.lastCleanup = cleanupResult.timestamp;
      await this.saveCleanupConfig();

      if (totalInactive > 0) {
        await this.sendCleanupNotification(cleanupResult);
      }

      logger.success(`Cleanup completed: ${totalInactive} inactive servers found in ${cleanupResult.duration}ms`);
      return cleanupResult;

    } catch (error) {
      logger.error('Error during cleanup operation', error);
      return null;
    }
  }

  /**
   * Check server activity
   */
  async checkServerActivity(server) {
    try {
      const inactiveUsers = [];
      
      // Get all users from server
      const users = await this.getServerUsers(server);
      
      for (const user of users) {
        const lastActivity = await this.getUserLastActivity(server, user);
        const isInactive = this.isUserInactive(lastActivity);
        
        if (isInactive) {
          inactiveUsers.push({
            id: user.id,
            username: user.username,
            email: user.email,
            lastActivity: lastActivity,
            inactiveDays: this.getInactiveDays(lastActivity)
          });
        }
      }

      return inactiveUsers;

    } catch (error) {
      logger.error(`Error checking activity for server ${server.name}`, error);
      return [];
    }
  }

  /**
   * Get server users
   */
  async getServerUsers(server) {
    try {
      const response = await axios.get(`${server.domain}/api/application/users`, {
        headers: {
          'Authorization': `Bearer ${server.apiKey}`,
          'Content-Type': 'application/json'
        },
        timeout: 15000
      });

      return response.data.data?.map(user => ({
        id: user.attributes.id,
        username: user.attributes.username,
        email: user.attributes.email,
        createdAt: user.attributes.created_at,
        updatedAt: user.attributes.updated_at
      })) || [];

    } catch (error) {
      logger.error(`Failed to get users from ${server.name}`, error);
      return [];
    }
  }

  /**
   * Get user last activity
   */
  async getUserLastActivity(server, user) {
    try {
      // Get user's servers
      const serversResponse = await axios.get(`${server.domain}/api/application/users/${user.id}/servers`, {
        headers: {
          'Authorization': `Bearer ${server.apiKey}`,
          'Content-Type': 'application/json'
        },
        timeout: 10000
      });

      const userServers = serversResponse.data.data || [];
      let lastActivity = new Date(user.updatedAt);

      // Check each server's last activity
      for (const userServer of userServers) {
        try {
          const serverResponse = await axios.get(`${server.domain}/api/application/servers/${userServer.attributes.id}`, {
            headers: {
              'Authorization': `Bearer ${server.apiKey}`,
              'Content-Type': 'application/json'
            },
            timeout: 10000
          });

          const serverData = serverResponse.data.attributes;
          const serverLastActivity = new Date(serverData.updated_at);
          
          if (serverLastActivity > lastActivity) {
            lastActivity = serverLastActivity;
          }
        } catch (serverError) {
          // Continue checking other servers
        }
      }

      return lastActivity;

    } catch (error) {
      // Fallback to user updated_at
      return new Date(user.updatedAt);
    }
  }

  /**
   * Check if user is inactive
   */
  isUserInactive(lastActivity) {
    const now = Date.now();
    const activityTime = new Date(lastActivity).getTime();
    return (now - activityTime) > this.inactiveThreshold;
  }

  /**
   * Get inactive days
   */
  getInactiveDays(lastActivity) {
    const now = Date.now();
    const activityTime = new Date(lastActivity).getTime();
    return Math.floor((now - activityTime) / (24 * 60 * 60 * 1000));
  }

  /**
   * Send cleanup notification to owner
   */
  async sendCleanupNotification(cleanupResult) {
    try {
      let message = `🧹 *MODE PEMBERSIHAN OTOMATIS*\n\n`;
      message += `📊 *Hasil Pemindaian:*\n`;
      message += `• Server diperiksa: ${cleanupResult.serversChecked}\n`;
      message += `• Server tidak aktif: ${cleanupResult.inactiveFound}\n`;
      message += `• Waktu pemindaian: ${cleanupResult.duration}ms\n`;
      message += `• Tanggal: ${new Date(cleanupResult.timestamp).toLocaleString('id-ID')}\n\n`;

      if (cleanupResult.inactiveServers.length > 0) {
        message += `📋 *Server Tidak Aktif (>2 hari):*\n\n`;
        
        cleanupResult.inactiveServers.forEach((serverData, index) => {
          message += `${index + 1}. **${serverData.server}**\n`;
          message += `   Domain: \`${serverData.domain}\`\n`;
          message += `   Pengguna tidak aktif: ${serverData.inactive.length}\n\n`;
          
          serverData.inactive.slice(0, 3).forEach(user => {
            message += `   • \`${user.username}\` (${user.inactiveDays} hari)\n`;
          });
          
          if (serverData.inactive.length > 3) {
            message += `   • ... dan ${serverData.inactive.length - 3} lainnya\n`;
          }
          message += '\n';
        });

        message += `⚠️ *Gunakan /cleanup_action untuk mengelola server tidak aktif*`;
      } else {
        message += `✅ *Semua server aktif!*`;
      }

      // This would be sent via the bot
      logger.info('Cleanup notification ready', { message, cleanupResult });
      
      cleanupResult.notified = true;
      await this.saveCleanupConfig();

    } catch (error) {
      logger.error('Failed to send cleanup notification', error);
    }
  }

  /**
   * Get cleanup statistics
   */
  getCleanupStats() {
    const now = new Date();
    const last24h = new Date(now.getTime() - 24 * 60 * 60 * 1000);
    
    const recentCleanups = this.cleanupHistory.filter(cleanup => 
      new Date(cleanup.timestamp) > last24h
    );

    const totalInactive = this.cleanupHistory.reduce((sum, cleanup) => 
      sum + cleanup.inactiveFound, 0
    );

    return {
      autoCleanup: this.autoCleanup,
      isRunning: this.isRunning,
      lastCleanup: this.lastCleanup,
      totalCleanups: this.cleanupHistory.length,
      recentCleanups: recentCleanups.length,
      totalInactiveFound: totalInactive,
      averageInactive: this.cleanupHistory.length > 0 ? 
        Math.round(totalInactive / this.cleanupHistory.length) : 0
    };
  }

  /**
   * Get recent cleanup results
   */
  getRecentCleanups(limit = 5) {
    return this.cleanupHistory
      .slice(-limit)
      .reverse();
  }

  /**
   * Manual cleanup trigger
   */
  async triggerManualCleanup() {
    if (this.isRunning) {
      logger.info('Manual cleanup triggered');
      return await this.performCleanup();
    } else {
      logger.warn('Auto cleanup not running, performing one-time cleanup');
      return await this.performCleanup();
    }
  }

  /**
   * Delete inactive servers
   */
  async deleteInactiveServers(serverName, userIds) {
    try {
      const server = panelService.getServer(serverName);
      if (!server) {
        throw new Error(`Server ${serverName} not found`);
      }

      const deletedUsers = [];
      const errors = [];

      for (const userId of userIds) {
        try {
          // Delete user's servers first
          await this.deleteUserServers(server, userId);
          
          // Delete user
          await axios.delete(`${server.domain}/api/application/users/${userId}`, {
            headers: {
              'Authorization': `Bearer ${server.apiKey}`,
              'Content-Type': 'application/json'
            },
            timeout: 10000
          });

          deletedUsers.push(userId);
          logger.info(`Deleted inactive user ${userId} from ${serverName}`);

        } catch (error) {
          errors.push({ userId, error: error.message });
          logger.error(`Failed to delete user ${userId}`, error);
        }
      }

      const result = {
        server: serverName,
        deleted: deletedUsers.length,
        errors: errors.length,
        details: { deletedUsers, errors }
      };

      // Log cleanup action
      this.cleanupHistory.push({
        id: Helpers.generateUniqueId(),
        timestamp: new Date().toISOString(),
        type: 'manual_delete',
        result
      });

      await this.saveCleanupConfig();
      return result;

    } catch (error) {
      logger.error('Error deleting inactive servers', error);
      throw error;
    }
  }

  /**
   * Delete user's servers
   */
  async deleteUserServers(server, userId) {
    try {
      const serversResponse = await axios.get(`${server.domain}/api/application/users/${userId}/servers`, {
        headers: {
          'Authorization': `Bearer ${server.apiKey}`,
          'Content-Type': 'application/json'
        },
        timeout: 10000
      });

      const userServers = serversResponse.data.data || [];

      for (const userServer of userServers) {
        try {
          await axios.delete(`${server.domain}/api/application/servers/${userServer.attributes.id}`, {
            headers: {
              'Authorization': `Bearer ${server.apiKey}`,
              'Content-Type': 'application/json'
            },
            timeout: 10000
          });
        } catch (serverError) {
          logger.warn(`Failed to delete server ${userServer.attributes.id}`, serverError);
        }
      }

    } catch (error) {
      logger.error(`Failed to get/delete servers for user ${userId}`, error);
    }
  }
}

// Create singleton instance
const cleanupService = new CleanupService();

module.exports = cleanupService;
