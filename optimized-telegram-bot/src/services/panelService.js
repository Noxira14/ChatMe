const axios = require('axios');
const config = require('../config/config');
const logger = require('../utils/logger');
const Helpers = require('../utils/helpers');

class PanelService {
  constructor() {
    this.servers = new Map();
    this.loadServers();
  }

  /**
   * Load servers from file
   */
  async loadServers() {
    try {
      const serverData = await Helpers.readJsonFile(config.paths.cpanelDataFile, {});
      this.servers = new Map(Object.entries(serverData));
      logger.info(`Loaded ${this.servers.size} servers`);
    } catch (error) {
      logger.error('Failed to load servers', error);
    }
  }

  /**
   * Save servers to file
   */
  async saveServers() {
    try {
      const serverData = Object.fromEntries(this.servers);
      await Helpers.writeJsonFile(config.paths.cpanelDataFile, serverData);
      logger.info('Servers saved successfully');
    } catch (error) {
      logger.error('Failed to save servers', error);
    }
  }

  /**
   * Add new server
   * @param {string} name - Server name
   * @param {Object} serverConfig - Server configuration
   */
  async addServer(name, serverConfig) {
    const server = {
      name,
      domain: serverConfig.domain,
      plta: serverConfig.plta,
      pltc: serverConfig.pltc,
      location: serverConfig.location || '1',
      eggs: serverConfig.eggs || '15',
      createdAt: new Date().toISOString(),
      status: 'active'
    };

    this.servers.set(name, server);
    await this.saveServers();
    logger.info(`Added server: ${name}`);
    return server;
  }

  /**
   * Remove server
   * @param {string} name - Server name
   */
  async removeServer(name) {
    if (this.servers.has(name)) {
      this.servers.delete(name);
      await this.saveServers();
      logger.info(`Removed server: ${name}`);
      return true;
    }
    return false;
  }

  /**
   * Update server
   * @param {string} name - Server name
   * @param {Object} updates - Updates to apply
   */
  async updateServer(name, updates) {
    if (this.servers.has(name)) {
      const server = this.servers.get(name);
      const updatedServer = { ...server, ...updates, updatedAt: new Date().toISOString() };
      this.servers.set(name, updatedServer);
      await this.saveServers();
      logger.info(`Updated server: ${name}`);
      return updatedServer;
    }
    return null;
  }

  /**
   * Get server by name
   * @param {string} name - Server name
   * @returns {Object|null} Server data
   */
  getServer(name) {
    return this.servers.get(name) || null;
  }

  /**
   * Get all servers
   * @returns {Array} All servers
   */
  getAllServers() {
    return Array.from(this.servers.values());
  }

  /**
   * Create panel user
   * @param {Object} serverConfig - Server configuration
   * @param {Object} userData - User data
   */
  async createPanel(serverConfig, userData) {
    try {
      const startTime = Date.now();
      
      // Generate secure password if not provided
      if (!userData.password) {
        userData.password = Helpers.generateSecurePassword();
      }

      // Create user account
      const userResponse = await this.createUser(serverConfig, userData);
      if (!userResponse.success) {
        throw new Error(userResponse.error);
      }

      // Create server instance
      const serverResponse = await this.createServer(serverConfig, {
        ...userData,
        userId: userResponse.data.id
      });

      if (!serverResponse.success) {
        // Cleanup user if server creation failed
        await this.deleteUser(serverConfig, userResponse.data.id);
        throw new Error(serverResponse.error);
      }

      const responseTime = Date.now() - startTime;
      logger.success(`Panel created successfully in ${responseTime}ms`, {
        username: userData.username,
        email: userData.email,
        serverId: serverResponse.data.id
      });

      return {
        success: true,
        data: {
          user: userResponse.data,
          server: serverResponse.data,
          loginUrl: `${serverConfig.domain}/auth/login`,
          responseTime
        }
      };

    } catch (error) {
      logger.error('Failed to create panel', error);
      return {
        success: false,
        error: error.message
      };
    }
  }

  /**
   * Create user account
   * @param {Object} serverConfig - Server configuration
   * @param {Object} userData - User data
   */
  async createUser(serverConfig, userData) {
    try {
      const response = await axios.post(`${serverConfig.domain}/api/application/users`, {
        username: userData.username,
        email: userData.email,
        first_name: userData.firstName || userData.username,
        last_name: userData.lastName || 'User',
        password: userData.password
      }, {
        headers: {
          'Authorization': `Bearer ${serverConfig.plta}`,
          'Content-Type': 'application/json',
          'Accept': 'Application/vnd.pterodactyl.v1+json'
        },
        timeout: 30000
      });

      logger.logApiRequest('POST', `${serverConfig.domain}/api/application/users`, 
                          response.status, Date.now());

      return {
        success: true,
        data: response.data.attributes
      };

    } catch (error) {
      const status = error.response?.status || 0;
      logger.logApiRequest('POST', `${serverConfig.domain}/api/application/users`, 
                          status, Date.now());
      
      return {
        success: false,
        error: this.parseApiError(error)
      };
    }
  }

  /**
   * Create server instance
   * @param {Object} serverConfig - Server configuration
   * @param {Object} serverData - Server data
   */
  async createServer(serverConfig, serverData) {
    try {
      const response = await axios.post(`${serverConfig.domain}/api/application/servers`, {
        name: serverData.serverName || `Server-${serverData.username}`,
        user: serverData.userId,
        egg: parseInt(serverConfig.eggs),
        docker_image: 'ghcr.io/pterodactyl/yolks:nodejs_18',
        startup: 'npm start',
        environment: {
          STARTUP: 'npm start',
          P_SERVER_LOCATION: serverConfig.location,
          P_SERVER_UUID: Helpers.generateUniqueId()
        },
        limits: {
          memory: serverData.memory || 1024,
          swap: serverData.swap || 0,
          disk: serverData.disk || 1024,
          io: serverData.io || 500,
          cpu: serverData.cpu || 100
        },
        feature_limits: {
          databases: serverData.databases || 1,
          backups: serverData.backups || 1,
          allocations: serverData.allocations || 1
        },
        allocation: {
          default: serverData.port || 25565
        }
      }, {
        headers: {
          'Authorization': `Bearer ${serverConfig.plta}`,
          'Content-Type': 'application/json',
          'Accept': 'Application/vnd.pterodactyl.v1+json'
        },
        timeout: 30000
      });

      logger.logApiRequest('POST', `${serverConfig.domain}/api/application/servers`, 
                          response.status, Date.now());

      return {
        success: true,
        data: response.data.attributes
      };

    } catch (error) {
      const status = error.response?.status || 0;
      logger.logApiRequest('POST', `${serverConfig.domain}/api/application/servers`, 
                          status, Date.now());
      
      return {
        success: false,
        error: this.parseApiError(error)
      };
    }
  }

  /**
   * Delete user
   * @param {Object} serverConfig - Server configuration
   * @param {number} userId - User ID
   */
  async deleteUser(serverConfig, userId) {
    try {
      await axios.delete(`${serverConfig.domain}/api/application/users/${userId}`, {
        headers: {
          'Authorization': `Bearer ${serverConfig.plta}`,
          'Accept': 'Application/vnd.pterodactyl.v1+json'
        },
        timeout: 30000
      });

      logger.info(`Deleted user: ${userId}`);
      return { success: true };

    } catch (error) {
      logger.error(`Failed to delete user ${userId}`, error);
      return {
        success: false,
        error: this.parseApiError(error)
      };
    }
  }

  /**
   * Get server status
   * @param {Object} serverConfig - Server configuration
   * @param {string} serverId - Server ID
   */
  async getServerStatus(serverConfig, serverId) {
    try {
      const response = await axios.get(`${serverConfig.domain}/api/client/servers/${serverId}/resources`, {
        headers: {
          'Authorization': `Bearer ${serverConfig.pltc}`,
          'Accept': 'Application/vnd.pterodactyl.v1+json'
        },
        timeout: 15000
      });

      return {
        success: true,
        data: response.data.attributes
      };

    } catch (error) {
      return {
        success: false,
        error: this.parseApiError(error)
      };
    }
  }

  /**
   * Clear server (delete all users and servers)
   * @param {Object} serverConfig - Server configuration
   */
  async clearServer(serverConfig) {
    try {
      let deletedUsers = 0;
      let deletedServers = 0;

      // Get all users
      const usersResponse = await axios.get(`${serverConfig.domain}/api/application/users`, {
        headers: {
          'Authorization': `Bearer ${serverConfig.plta}`,
          'Accept': 'Application/vnd.pterodactyl.v1+json'
        }
      });

      // Delete all users except admin
      for (const user of usersResponse.data.data) {
        if (user.attributes.root_admin) continue; // Skip admin users
        
        await this.deleteUser(serverConfig, user.attributes.id);
        deletedUsers++;
        await Helpers.sleep(100); // Small delay to prevent rate limiting
      }

      logger.success(`Cleared server: ${deletedUsers} users deleted`);
      return {
        success: true,
        deletedUsers,
        deletedServers
      };

    } catch (error) {
      logger.error('Failed to clear server', error);
      return {
        success: false,
        error: this.parseApiError(error)
      };
    }
  }

  /**
   * Parse API error response
   * @param {Error} error - Axios error
   * @returns {string} Parsed error message
   */
  parseApiError(error) {
    if (error.response?.data?.errors) {
      const errors = error.response.data.errors;
      return errors.map(err => err.detail || err.message).join(', ');
    }
    
    if (error.response?.data?.message) {
      return error.response.data.message;
    }
    
    if (error.code === 'ECONNREFUSED') {
      return 'Tidak dapat terhubung ke server';
    }
    
    if (error.code === 'ETIMEDOUT') {
      return 'Koneksi timeout';
    }
    
    return error.message || 'Terjadi kesalahan tidak dikenal';
  }
}

// Create singleton instance
const panelService = new PanelService();

module.exports = panelService;
