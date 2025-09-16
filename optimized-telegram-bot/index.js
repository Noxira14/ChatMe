#!/usr/bin/env node

/**
 * Optimized Telegram CPanel Bot
 * 
 * Features:
 * - Modular architecture
 * - Advanced logging system
 * - Rate limiting
 * - Authentication middleware
 * - Error handling
 * - Performance monitoring
 * - Structured commands
 * 
 * Author: Optimized by AI Assistant
 * Version: 2.0.0
 */

const path = require('path');
const fs = require('fs');

// Add src directory to module path
require('module').globalPaths.push(path.join(__dirname, 'src'));

const OptimizedTelegramBot = require('./src/bot');
const logger = require('./src/utils/logger');
const config = require('./src/config/config');
const Helpers = require('./src/utils/helpers');

class BotManager {
  constructor() {
    this.bot = null;
    this.isShuttingDown = false;
  }

  /**
   * Initialize and start the bot
   */
  async start() {
    try {
      console.log('🚀 Starting Optimized Telegram CPanel Bot...\n');

      // Ensure required directories exist
      await this.ensureDirectories();

      // Initialize bot
      this.bot = new OptimizedTelegramBot();
      
      const initialized = await this.bot.initialize();
      if (!initialized) {
        throw new Error('Failed to initialize bot');
      }

      // Start bot
      await this.bot.start();

      // Setup graceful shutdown
      this.setupGracefulShutdown();

      logger.success('Bot is now running! Press Ctrl+C to stop.');

    } catch (error) {
      logger.error('Failed to start bot', error);
      process.exit(1);
    }
  }

  /**
   * Ensure required directories exist
   */
  async ensureDirectories() {
    const directories = [
      config.paths.data,
      config.paths.logs
    ];

    for (const dir of directories) {
      await Helpers.ensureDir(dir);
    }

    logger.info('Required directories ensured');
  }

  /**
   * Setup graceful shutdown
   */
  setupGracefulShutdown() {
    const shutdown = async (signal) => {
      if (this.isShuttingDown) return;
      this.isShuttingDown = true;

      logger.info(`Received ${signal}, shutting down gracefully...`);

      try {
        if (this.bot) {
          await this.bot.stop();
        }
        
        logger.info('Bot stopped successfully');
        process.exit(0);
      } catch (error) {
        logger.error('Error during shutdown', error);
        process.exit(1);
      }
    };

    process.on('SIGINT', () => shutdown('SIGINT'));
    process.on('SIGTERM', () => shutdown('SIGTERM'));
    process.on('SIGUSR2', () => shutdown('SIGUSR2')); // For nodemon
  }

  /**
   * Display startup banner
   */
  static displayBanner() {
    const banner = `
╔══════════════════════════════════════════════════════════════╗
║                                                              ║
║    🤖 Optimized Telegram CPanel Bot v2.0.0                  ║
║                                                              ║
║    ✨ Features:                                              ║
║    • Modular Architecture                                    ║
║    • Advanced Logging                                        ║
║    • Rate Limiting                                           ║
║    • Authentication System                                   ║
║    • Error Handling                                          ║
║    • Performance Monitoring                                  ║
║                                                              ║
║    📊 Status: Starting...                                    ║
║                                                              ║
╚══════════════════════════════════════════════════════════════╝
    `;
    
    console.log(banner);
  }

  /**
   * Validate configuration
   */
  static validateConfig() {
    const required = [
      'bot.token',
      'bot.adminId'
    ];

    const missing = [];

    for (const key of required) {
      const keys = key.split('.');
      let value = config;
      
      for (const k of keys) {
        value = value?.[k];
      }
      
      if (!value) {
        missing.push(key);
      }
    }

    if (missing.length > 0) {
      logger.error(`Missing required configuration: ${missing.join(', ')}`);
      return false;
    }

    return true;
  }
}

// Main execution
async function main() {
  try {
    // Display banner
    BotManager.displayBanner();

    // Validate configuration
    if (!BotManager.validateConfig()) {
      process.exit(1);
    }

    // Create and start bot manager
    const manager = new BotManager();
    await manager.start();

  } catch (error) {
    console.error('Fatal error:', error);
    process.exit(1);
  }
}

// Handle unhandled errors
process.on('uncaughtException', (error) => {
  console.error('Uncaught Exception:', error);
  process.exit(1);
});

process.on('unhandledRejection', (reason, promise) => {
  console.error('Unhandled Rejection at:', promise, 'reason:', reason);
  process.exit(1);
});

// Start the application
if (require.main === module) {
  main();
}

module.exports = BotManager;
