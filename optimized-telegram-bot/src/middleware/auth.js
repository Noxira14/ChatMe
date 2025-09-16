const config = require('../config/config');
const logger = require('../utils/logger');
const Helpers = require('../utils/helpers');

class AuthMiddleware {
  constructor() {
    this.adminUsers = new Set();
    this.premiumUsers = new Set();
    this.loadUsers();
  }

  /**
   * Load users from files
   */
  async loadUsers() {
    try {
      // Load admin users
      const adminData = await Helpers.readJsonFile(config.paths.adminFile, []);
      this.adminUsers = new Set(adminData);

      // Load premium users
      const premiumData = await Helpers.readJsonFile(config.paths.premiumFile, []);
      this.premiumUsers = new Set(premiumData);

      logger.info(`Loaded ${this.adminUsers.size} admin users and ${this.premiumUsers.size} premium users`);
    } catch (error) {
      logger.error('Failed to load users', error);
    }
  }

  /**
   * Save users to files
   */
  async saveUsers() {
    try {
      await Helpers.writeJsonFile(config.paths.adminFile, Array.from(this.adminUsers));
      await Helpers.writeJsonFile(config.paths.premiumFile, Array.from(this.premiumUsers));
      logger.info('Users saved successfully');
    } catch (error) {
      logger.error('Failed to save users', error);
    }
  }

  /**
   * Check if user is owner
   * @param {string|number} userId - User ID
   * @returns {boolean} Is owner
   */
  isOwner(userId) {
    return userId.toString() === config.bot.adminId;
  }

  /**
   * Check if user is admin
   * @param {string|number} userId - User ID
   * @returns {boolean} Is admin
   */
  isAdmin(userId) {
    return this.isOwner(userId) || this.adminUsers.has(userId.toString());
  }

  /**
   * Check if user is premium
   * @param {string|number} userId - User ID
   * @returns {boolean} Is premium
   */
  isPremium(userId) {
    return this.isAdmin(userId) || this.premiumUsers.has(userId.toString());
  }

  /**
   * Add admin user
   * @param {string|number} userId - User ID
   */
  async addAdmin(userId) {
    this.adminUsers.add(userId.toString());
    await this.saveUsers();
    logger.info(`Added admin user: ${userId}`);
  }

  /**
   * Remove admin user
   * @param {string|number} userId - User ID
   */
  async removeAdmin(userId) {
    this.adminUsers.delete(userId.toString());
    await this.saveUsers();
    logger.info(`Removed admin user: ${userId}`);
  }

  /**
   * Add premium user
   * @param {string|number} userId - User ID
   */
  async addPremium(userId) {
    this.premiumUsers.add(userId.toString());
    await this.saveUsers();
    logger.info(`Added premium user: ${userId}`);
  }

  /**
   * Remove premium user
   * @param {string|number} userId - User ID
   */
  async removePremium(userId) {
    this.premiumUsers.delete(userId.toString());
    await this.saveUsers();
    logger.info(`Removed premium user: ${userId}`);
  }

  /**
   * Get all admin users
   * @returns {Array} Admin users
   */
  getAdminUsers() {
    return Array.from(this.adminUsers);
  }

  /**
   * Get all premium users
   * @returns {Array} Premium users
   */
  getPremiumUsers() {
    return Array.from(this.premiumUsers);
  }

  /**
   * Middleware for owner only commands
   * @param {Function} handler - Command handler
   * @returns {Function} Wrapped handler
   */
  ownerOnly(handler) {
    return async (msg, match) => {
      if (!this.isOwner(msg.from.id)) {
        logger.warn(`Unauthorized owner command attempt by user ${msg.from.id}`);
        return { error: config.messages.unauthorized };
      }
      return await handler(msg, match);
    };
  }

  /**
   * Middleware for admin only commands
   * @param {Function} handler - Command handler
   * @returns {Function} Wrapped handler
   */
  adminOnly(handler) {
    return async (msg, match) => {
      if (!this.isAdmin(msg.from.id)) {
        logger.warn(`Unauthorized admin command attempt by user ${msg.from.id}`);
        return { error: config.messages.unauthorized };
      }
      return await handler(msg, match);
    };
  }

  /**
   * Middleware for premium only commands
   * @param {Function} handler - Command handler
   * @returns {Function} Wrapped handler
   */
  premiumOnly(handler) {
    return async (msg, match) => {
      if (!this.isPremium(msg.from.id)) {
        logger.warn(`Unauthorized premium command attempt by user ${msg.from.id}`);
        return { error: config.messages.unauthorized };
      }
      return await handler(msg, match);
    };
  }

  /**
   * Get user info with permissions
   * @param {string|number} userId - User ID
   * @returns {Object} User info
   */
  getUserInfo(userId) {
    return {
      userId: userId.toString(),
      isOwner: this.isOwner(userId),
      isAdmin: this.isAdmin(userId),
      isPremium: this.isPremium(userId)
    };
  }
}

// Create singleton instance
const authMiddleware = new AuthMiddleware();

module.exports = authMiddleware;
