const config = require('../config/config');
const logger = require('../utils/logger');
const Helpers = require('../utils/helpers');
const figlet = require('figlet');
const chalk = require('chalk');

class AuthenticationMiddleware {
  constructor() {
    this.authenticatedUsers = new Set();
    this.bannedUsers = new Set();
    this.maintenanceMode = false;
    this.botPassword = process.env.BOT_PASSWORD || 'XMSBRA2024';
    this.rainbowColors = [
      '#FF0000', '#FF7F00', '#FFFF00', '#00FF00', 
      '#0000FF', '#4B0082', '#9400D3'
    ];
    this.loadAuthData();
  }

  /**
   * Load authentication data
   */
  async loadAuthData() {
    try {
      const authData = await Helpers.readJsonFile(config.paths.authFile, {
        authenticated: [],
        banned: [],
        maintenance: false
      });
      
      this.authenticatedUsers = new Set(authData.authenticated || []);
      this.bannedUsers = new Set(authData.banned || []);
      this.maintenanceMode = authData.maintenance || false;
      
      logger.info(`Loaded auth data: ${this.authenticatedUsers.size} authenticated, ${this.bannedUsers.size} banned`);
    } catch (error) {
      logger.error('Failed to load auth data', error);
    }
  }

  /**
   * Save authentication data
   */
  async saveAuthData() {
    try {
      const authData = {
        authenticated: Array.from(this.authenticatedUsers),
        banned: Array.from(this.bannedUsers),
        maintenance: this.maintenanceMode
      };
      
      await Helpers.writeJsonFile(config.paths.authFile, authData);
      logger.info('Auth data saved successfully');
    } catch (error) {
      logger.error('Failed to save auth data', error);
    }
  }

  /**
   * Display welcome banner with figlet
   */
  displayWelcomeBanner() {
    return new Promise((resolve) => {
      figlet('XMSBRA', (err, data) => {
        if (err) {
          console.log(chalk.red('Figlet error'));
          resolve('🤖 *SELAMAT DATANG DI XMSBRA CPANEL*');
          return;
        }

        const coloredFiglet = data
          .split('\n')
          .map((line, i) => chalk.hex(this.rainbowColors[i % this.rainbowColors.length])(line))
          .join('\n');

        const banner = 
          chalk.white('\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n') +
          coloredFiglet + '\n' +
          chalk.bgMagenta.white('🤖 SELAMAT DATANG DI XMSBRA CPANEL 🤖') + '\n' +
          chalk.white('▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n');

        console.log(banner);
        resolve('🤖 *SELAMAT DATANG DI XMSBRA CPANEL*');
      });
    });
  }

  /**
   * Log command execution with fancy display
   */
  logCommand(msg, command) {
    const moment = require('moment-timezone');
    const time = moment().tz('Asia/Jakarta').locale('id').format('HH:mm:ss z');
    
    figlet('XMSBRA', (err, data) => {
      if (err) {
        console.log(chalk.red('Figlet error'));
        return;
      }

      const coloredFiglet = data
        .split('\n')
        .map((line, i) => chalk.hex(this.rainbowColors[i % this.rainbowColors.length])(line))
        .join('\n');

      console.log(
        chalk.white('\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n') +
        coloredFiglet + '\n' +
        chalk.bgMagenta.white(`⫹ 𝐓𝐈𝐌𝐄 ⫺ `) + chalk.magentaBright('┃  ' + time) + '\n' +
        chalk.bgWhite.magenta(`⫹ 𝐂𝐇𝐀𝐓 𝐓𝐘𝐏𝐄 ⫺ `) + (msg.chat.type !== 'private' ? chalk.magentaBright('┃  GROUP CHAT 👥') : chalk.magentaBright('┃  PRIVATE CHAT 🔒')) + '\n' +
        chalk.bgMagenta.white(`⫹ 𝐒𝐄𝐍𝐃𝐄𝐑 ⫺ `) + chalk.magentaBright('┃  ' + (msg.from.first_name || 'Unknown')) + '\n' +
        chalk.bgWhite.magenta(`⫹ 𝐂𝐌𝐃 ⫺ `) + chalk.magentaBright('┃  ' + command) + '\n' +
        chalk.bgMagenta.white(`⫹ 𝐌𝐄𝐒𝐒𝐀𝐆𝐄 ⫺ `) + chalk.magentaBright('┃  ' + msg.text) + '\n' +
        '▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n'
      );
    });
  }

  /**
   * Check if user is authenticated
   */
  isAuthenticated(userId) {
    return this.authenticatedUsers.has(userId.toString());
  }

  /**
   * Check if user is banned
   */
  isBanned(userId) {
    return this.bannedUsers.has(userId.toString());
  }

  /**
   * Authenticate user with password
   */
  async authenticateUser(userId, password) {
    if (password === this.botPassword) {
      this.authenticatedUsers.add(userId.toString());
      await this.saveAuthData();
      logger.info(`User ${userId} authenticated successfully`);
      return true;
    }
    
    logger.warn(`Failed authentication attempt from user ${userId}`);
    return false;
  }

  /**
   * Ban user
   */
  async banUser(userId) {
    this.bannedUsers.add(userId.toString());
    this.authenticatedUsers.delete(userId.toString());
    await this.saveAuthData();
    logger.info(`User ${userId} banned`);
  }

  /**
   * Unban user
   */
  async unbanUser(userId) {
    const removed = this.bannedUsers.delete(userId.toString());
    if (removed) {
      await this.saveAuthData();
      logger.info(`User ${userId} unbanned`);
    }
    return removed;
  }

  /**
   * Set maintenance mode
   */
  async setMaintenanceMode(enabled) {
    this.maintenanceMode = enabled;
    await this.saveAuthData();
    logger.info(`Maintenance mode ${enabled ? 'enabled' : 'disabled'}`);
  }

  /**
   * Authentication middleware
   */
  requireAuth() {
    return async (msg, match) => {
      const userId = msg.from.id;

      // Check if banned
      if (this.isBanned(userId)) {
        return {
          text: '🚫 *AKSES DITOLAK*\n\nAnda telah dibanned dari menggunakan bot ini.',
          options: { parse_mode: 'Markdown' }
        };
      }

      // Check maintenance mode
      if (this.maintenanceMode && !config.bot.adminId.includes(userId)) {
        return {
          text: '🔧 *MODE MAINTENANCE*\n\nBot sedang dalam maintenance. Silakan coba lagi nanti.',
          options: { parse_mode: 'Markdown' }
        };
      }

      // Check if authenticated
      if (!this.isAuthenticated(userId)) {
        const welcomeText = await this.displayWelcomeBanner();
        return {
          text: `${welcomeText}\n\n🔐 *Untuk Proses Verifikasi*\n\nTolong masukkan password:\n\nFormat: \`/auth <password>\``,
          options: { parse_mode: 'Markdown' }
        };
      }

      return null; // Authenticated
    };
  }

  /**
   * Get authentication stats
   */
  getStats() {
    return {
      authenticated: this.authenticatedUsers.size,
      banned: this.bannedUsers.size,
      maintenanceMode: this.maintenanceMode
    };
  }
}

// Create singleton instance
const authenticationMiddleware = new AuthenticationMiddleware();

module.exports = authenticationMiddleware;
