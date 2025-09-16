const crypto = require('crypto');
const fs = require('fs').promises;
const path = require('path');

class Helpers {
  /**
   * Generate secure random password
   * @param {number} length - Password length
   * @returns {string} Generated password
   */
  static generateSecurePassword(length = 12) {
    const charset = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#%^&*!';
    let password = '';
    
    // Ensure at least one character from each type
    const types = [
      'ABCDEFGHIJKLMNOPQRSTUVWXYZ',
      'abcdefghijklmnopqrstuvwxyz',
      '0123456789',
      '@#%^&*!'
    ];
    
    // Add one character from each type
    types.forEach(type => {
      password += type[Math.floor(Math.random() * type.length)];
    });
    
    // Fill the rest randomly
    for (let i = password.length; i < length; i++) {
      password += charset[Math.floor(Math.random() * charset.length)];
    }
    
    // Shuffle the password
    return password.split('').sort(() => Math.random() - 0.5).join('');
  }

  /**
   * Generate random string with specified length
   * @param {number} length - String length
   * @returns {string} Random string
   */
  static generateRandomString(length = 8) {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    return Array.from({ length }, () => chars[Math.floor(Math.random() * chars.length)]).join('');
  }

  /**
   * Get formatted runtime
   * @returns {string} Formatted runtime string
   */
  static getRuntime() {
    const uptime = process.uptime();
    const days = Math.floor(uptime / 86400);
    const hours = Math.floor((uptime % 86400) / 3600);
    const minutes = Math.floor((uptime % 3600) / 60);
    const seconds = Math.floor(uptime % 60);
    
    let runtime = '';
    if (days > 0) runtime += `${days} Hari `;
    if (hours > 0) runtime += `${hours} Jam `;
    if (minutes > 0) runtime += `${minutes} Menit `;
    runtime += `${seconds} Detik`;
    
    return runtime;
  }

  /**
   * Format bytes to human readable format
   * @param {number} bytes - Bytes to format
   * @returns {string} Formatted string
   */
  static formatBytes(bytes) {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }

  /**
   * Validate email format
   * @param {string} email - Email to validate
   * @returns {boolean} Is valid email
   */
  static isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  /**
   * Validate URL format
   * @param {string} url - URL to validate
   * @returns {boolean} Is valid URL
   */
  static isValidUrl(url) {
    try {
      new URL(url);
      return true;
    } catch {
      return false;
    }
  }

  /**
   * Sanitize filename
   * @param {string} filename - Filename to sanitize
   * @returns {string} Sanitized filename
   */
  static sanitizeFilename(filename) {
    return filename.replace(/[^a-z0-9.-]/gi, '_').toLowerCase();
  }

  /**
   * Sleep for specified milliseconds
   * @param {number} ms - Milliseconds to sleep
   * @returns {Promise} Promise that resolves after sleep
   */
  static sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  /**
   * Ensure directory exists
   * @param {string} dirPath - Directory path
   */
  static async ensureDir(dirPath) {
    try {
      await fs.access(dirPath);
    } catch {
      await fs.mkdir(dirPath, { recursive: true });
    }
  }

  /**
   * Read JSON file safely
   * @param {string} filePath - File path
   * @param {*} defaultValue - Default value if file doesn't exist
   * @returns {*} Parsed JSON or default value
   */
  static async readJsonFile(filePath, defaultValue = {}) {
    try {
      const data = await fs.readFile(filePath, 'utf8');
      return JSON.parse(data);
    } catch {
      return defaultValue;
    }
  }

  /**
   * Write JSON file safely
   * @param {string} filePath - File path
   * @param {*} data - Data to write
   */
  static async writeJsonFile(filePath, data) {
    await this.ensureDir(path.dirname(filePath));
    await fs.writeFile(filePath, JSON.stringify(data, null, 2), 'utf8');
  }

  /**
   * Escape markdown special characters
   * @param {string} text - Text to escape
   * @returns {string} Escaped text
   */
  static escapeMarkdown(text) {
    return text.replace(/[_*[\]()~`>#+=|{}.!-]/g, '\\$&');
  }

  /**
   * Truncate text to specified length
   * @param {string} text - Text to truncate
   * @param {number} maxLength - Maximum length
   * @returns {string} Truncated text
   */
  static truncateText(text, maxLength = 100) {
    if (text.length <= maxLength) return text;
    return text.substring(0, maxLength - 3) + '...';
  }

  /**
   * Generate unique ID
   * @returns {string} Unique ID
   */
  static generateUniqueId() {
    return crypto.randomBytes(16).toString('hex');
  }

  /**
   * Hash string using SHA256
   * @param {string} text - Text to hash
   * @returns {string} Hashed text
   */
  static hashString(text) {
    return crypto.createHash('sha256').update(text).digest('hex');
  }
}

module.exports = Helpers;
