const config = require('../config/config');
const logger = require('../utils/logger');
const Helpers = require('../utils/helpers');

class GroupAccessMiddleware {
  constructor() {
    this.groupAccess = new Map(); // groupId -> { type, permissions }
    this.loadGroupAccess();
  }

  /**
   * Load group access from file
   */
  async loadGroupAccess() {
    try {
      const groupData = await Helpers.readJsonFile(config.paths.groupAccessFile, {});
      this.groupAccess = new Map(Object.entries(groupData));
      logger.info(`Loaded ${this.groupAccess.size} group access configurations`);
    } catch (error) {
      logger.error('Failed to load group access', error);
    }
  }

  /**
   * Save group access to file
   */
  async saveGroupAccess() {
    try {
      const groupData = Object.fromEntries(this.groupAccess);
      await Helpers.writeJsonFile(config.paths.groupAccessFile, groupData);
      logger.info('Group access saved successfully');
    } catch (error) {
      logger.error('Failed to save group access', error);
    }
  }

  /**
   * Add group access
   * @param {string} groupId - Group ID
   * @param {string} type - Access type (reseller, admin, owner, developer)
   * @param {Object} permissions - Group permissions
   */
  async addGroupAccess(groupId, type, permissions = {}) {
    const accessConfig = {
      type,
      permissions: {
        createPanel: type === 'reseller' || type === 'admin' || type === 'owner' || type === 'developer',
        manageServer: type === 'admin' || type === 'owner' || type === 'developer',
        viewStats: type === 'admin' || type === 'owner' || type === 'developer',
        fullAccess: type === 'owner' || type === 'developer',
        ...permissions
      },
      addedAt: new Date().toISOString(),
      addedBy: 'system'
    };

    this.groupAccess.set(groupId.toString(), accessConfig);
    await this.saveGroupAccess();
    logger.info(`Added group access: ${groupId} as ${type}`);
    return accessConfig;
  }

  /**
   * Remove group access
   * @param {string} groupId - Group ID
   */
  async removeGroupAccess(groupId) {
    const removed = this.groupAccess.delete(groupId.toString());
    if (removed) {
      await this.saveGroupAccess();
      logger.info(`Removed group access: ${groupId}`);
    }
    return removed;
  }

  /**
   * Check if user has group access
   * @param {string} userId - User ID
   * @param {string} chatId - Chat ID
   * @param {string} permission - Required permission
   * @returns {boolean} Has access
   */
  hasGroupAccess(userId, chatId, permission = 'createPanel') {
    const groupConfig = this.groupAccess.get(chatId.toString());
    
    if (!groupConfig) {
      return false;
    }

    return groupConfig.permissions[permission] || false;
  }

  /**
   * Get group access info
   * @param {string} groupId - Group ID
   * @returns {Object|null} Group access info
   */
  getGroupAccess(groupId) {
    return this.groupAccess.get(groupId.toString()) || null;
  }

  /**
   * Get all group accesses
   * @returns {Array} All group accesses
   */
  getAllGroupAccesses() {
    return Array.from(this.groupAccess.entries()).map(([groupId, config]) => ({
      groupId,
      ...config
    }));
  }

  /**
   * Middleware for group access check
   * @param {string} permission - Required permission
   * @returns {Function} Middleware function
   */
  requireGroupAccess(permission = 'createPanel') {
    return async (msg, match) => {
      const chatId = msg.chat.id;
      const userId = msg.from.id;

      // Check if it's a group chat
      if (msg.chat.type === 'private') {
        return { error: '❌ Perintah ini hanya dapat digunakan di grup!' };
      }

      // Check group access
      if (!this.hasGroupAccess(userId, chatId, permission)) {
        const groupConfig = this.getGroupAccess(chatId);
        if (!groupConfig) {
          return { error: '❌ Grup ini tidak memiliki akses untuk menggunakan bot!' };
        }
        
        return { 
          error: `❌ Grup ini tidak memiliki izin untuk: ${permission}\n` +
                 `Tipe akses grup: ${groupConfig.type}`
        };
      }

      return null; // Access granted
    };
  }
}

// Create singleton instance
const groupAccessMiddleware = new GroupAccessMiddleware();

module.exports = groupAccessMiddleware;
