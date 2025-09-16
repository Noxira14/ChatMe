const fs = require('fs').promises;
const path = require('path');
const chalk = require('chalk');
const config = require('../config/config');

class Logger {
  constructor() {
    this.logDir = config.paths.logs;
    this.ensureLogDir();
  }

  async ensureLogDir() {
    try {
      await fs.access(this.logDir);
    } catch {
      await fs.mkdir(this.logDir, { recursive: true });
    }
  }

  /**
   * Get current timestamp
   * @returns {string} Formatted timestamp
   */
  getTimestamp() {
    return new Date().toISOString().replace('T', ' ').substring(0, 19);
  }

  /**
   * Get log filename for current date
   * @returns {string} Log filename
   */
  getLogFilename() {
    const date = new Date().toISOString().split('T')[0];
    return path.join(this.logDir, `bot-${date}.log`);
  }

  /**
   * Write log to file
   * @param {string} level - Log level
   * @param {string} message - Log message
   * @param {Object} meta - Additional metadata
   */
  async writeToFile(level, message, meta = {}) {
    const timestamp = this.getTimestamp();
    const logEntry = {
      timestamp,
      level: level.toUpperCase(),
      message,
      ...meta
    };

    const logLine = JSON.stringify(logEntry) + '\n';
    const filename = this.getLogFilename();

    try {
      await fs.appendFile(filename, logLine);
    } catch (error) {
      console.error('Failed to write to log file:', error);
    }
  }

  /**
   * Log info message
   * @param {string} message - Message to log
   * @param {Object} meta - Additional metadata
   */
  async info(message, meta = {}) {
    console.log(chalk.blue(`[INFO] ${this.getTimestamp()}: ${message}`));
    await this.writeToFile('info', message, meta);
  }

  /**
   * Log warning message
   * @param {string} message - Message to log
   * @param {Object} meta - Additional metadata
   */
  async warn(message, meta = {}) {
    console.log(chalk.yellow(`[WARN] ${this.getTimestamp()}: ${message}`));
    await this.writeToFile('warn', message, meta);
  }

  /**
   * Log error message
   * @param {string} message - Message to log
   * @param {Error} error - Error object
   * @param {Object} meta - Additional metadata
   */
  async error(message, error = null, meta = {}) {
    const errorMeta = error ? {
      error: error.message,
      stack: error.stack,
      ...meta
    } : meta;

    console.log(chalk.red(`[ERROR] ${this.getTimestamp()}: ${message}`));
    if (error) {
      console.log(chalk.red(error.stack));
    }
    
    await this.writeToFile('error', message, errorMeta);
  }

  /**
   * Log debug message
   * @param {string} message - Message to log
   * @param {Object} meta - Additional metadata
   */
  async debug(message, meta = {}) {
    if (config.logging.level === 'debug') {
      console.log(chalk.gray(`[DEBUG] ${this.getTimestamp()}: ${message}`));
      await this.writeToFile('debug', message, meta);
    }
  }

  /**
   * Log success message
   * @param {string} message - Message to log
   * @param {Object} meta - Additional metadata
   */
  async success(message, meta = {}) {
    console.log(chalk.green(`[SUCCESS] ${this.getTimestamp()}: ${message}`));
    await this.writeToFile('success', message, meta);
  }

  /**
   * Log bot command usage
   * @param {Object} msg - Telegram message object
   * @param {string} command - Command used
   */
  async logCommand(msg, command) {
    const meta = {
      userId: msg.from.id,
      username: msg.from.username,
      firstName: msg.from.first_name,
      chatId: msg.chat.id,
      command
    };

    await this.info(`Command executed: ${command}`, meta);
  }

  /**
   * Log API request
   * @param {string} method - HTTP method
   * @param {string} url - Request URL
   * @param {number} statusCode - Response status code
   * @param {number} responseTime - Response time in ms
   */
  async logApiRequest(method, url, statusCode, responseTime) {
    const meta = {
      method,
      url,
      statusCode,
      responseTime: `${responseTime}ms`
    };

    const message = `API ${method} ${url} - ${statusCode} (${responseTime}ms)`;
    
    if (statusCode >= 400) {
      await this.error(message, null, meta);
    } else {
      await this.info(message, meta);
    }
  }

  /**
   * Clean old log files
   * @param {number} daysToKeep - Number of days to keep logs
   */
  async cleanOldLogs(daysToKeep = 7) {
    try {
      const files = await fs.readdir(this.logDir);
      const cutoffDate = new Date();
      cutoffDate.setDate(cutoffDate.getDate() - daysToKeep);

      for (const file of files) {
        if (file.startsWith('bot-') && file.endsWith('.log')) {
          const filePath = path.join(this.logDir, file);
          const stats = await fs.stat(filePath);
          
          if (stats.mtime < cutoffDate) {
            await fs.unlink(filePath);
            await this.info(`Deleted old log file: ${file}`);
          }
        }
      }
    } catch (error) {
      await this.error('Failed to clean old logs', error);
    }
  }
}

// Create singleton instance
const logger = new Logger();

module.exports = logger;
