const config = require('../config/config');
const logger = require('../utils/logger');

class SimilarityMatcher {
  constructor() {
    this.commands = [
      'start', 'help', 'cekid', 'panel', 'status',
      'buatpanel', 'listpanel', 'delpanel',
      'addsrv', 'listsrv', 'delsrv', 'srvinfo',
      'addprem', 'delprem', 'listadmin',
      'addowner', 'delowner', 'clearserver', 'stats',
      'addgc', 'delgc', 'listgc', 'joingc',
      'ban', 'unban', 'kick', 'maintenance',
      'security_panel', 'auth'
    ];
    this.similarityThreshold = 0.6;
  }

  /**
   * Calculate similarity between two strings using Levenshtein distance
   * @param {string} str1 - First string
   * @param {string} str2 - Second string
   * @returns {number} Similarity score (0-1)
   */
  calculateSimilarity(str1, str2) {
    const len1 = str1.length;
    const len2 = str2.length;
    
    if (len1 === 0) return len2 === 0 ? 1 : 0;
    if (len2 === 0) return 0;

    const matrix = Array(len2 + 1).fill(null).map(() => Array(len1 + 1).fill(null));

    for (let i = 0; i <= len1; i++) matrix[0][i] = i;
    for (let j = 0; j <= len2; j++) matrix[j][0] = j;

    for (let j = 1; j <= len2; j++) {
      for (let i = 1; i <= len1; i++) {
        const indicator = str1[i - 1] === str2[j - 1] ? 0 : 1;
        matrix[j][i] = Math.min(
          matrix[j][i - 1] + 1,     // deletion
          matrix[j - 1][i] + 1,     // insertion
          matrix[j - 1][i - 1] + indicator // substitution
        );
      }
    }

    const distance = matrix[len2][len1];
    const maxLen = Math.max(len1, len2);
    return (maxLen - distance) / maxLen;
  }

  /**
   * Find best matching command
   * @param {string} inputCommand - Input command
   * @returns {Object|null} Best match info
   */
  findBestMatch(inputCommand) {
    if (!inputCommand || inputCommand.length === 0) return null;

    const cleanCommand = inputCommand.toLowerCase().replace(/^\//, '');
    let bestMatch = '';
    let highestSimilarity = 0;

    // Filter commands with similar length (±2 characters)
    const filteredCommands = this.commands.filter(cmd => 
      Math.abs(cmd.length - cleanCommand.length) <= 2
    );

    for (const cmd of filteredCommands) {
      const similarity = this.calculateSimilarity(cleanCommand, cmd.toLowerCase());
      if (similarity > highestSimilarity) {
        highestSimilarity = similarity;
        bestMatch = cmd;
      }
    }

    const similarityPercentage = Math.round(highestSimilarity * 100);

    if (highestSimilarity >= this.similarityThreshold && 
        cleanCommand !== bestMatch.toLowerCase()) {
      return {
        command: bestMatch,
        similarity: highestSimilarity,
        percentage: similarityPercentage
      };
    }

    return null;
  }

  /**
   * Generate suggestion message
   * @param {string} inputCommand - Input command
   * @param {Object} match - Match info
   * @returns {Object} Suggestion message
   */
  generateSuggestion(inputCommand, match) {
    const response = `*${config.bot.name || 'XMSBRA'}[ COMMAND UNDEFINED ]*\n\n` +
                     `Command yang kamu berikan salah.\n` +
                     `Mungkin ini yang kamu maksud:\n` +
                     `→ \`/${match.command}\`\n` +
                     `→ Kemiripan: ${match.percentage}%`;

    return {
      text: response,
      options: {
        parse_mode: 'Markdown',
        reply_markup: {
          inline_keyboard: [
            [
              {
                text: `✅ ${match.command}`,
                callback_data: `execute_${match.command}`
              }
            ],
            [
              {
                text: '📋 Help',
                callback_data: 'help'
              },
              {
                text: '❌ Tutup',
                callback_data: 'close'
              }
            ]
          ]
        }
      }
    };
  }

  /**
   * Process command similarity check
   * @param {string} inputCommand - Input command
   * @returns {Object|null} Suggestion or null
   */
  processSimilarity(inputCommand) {
    try {
      const match = this.findBestMatch(inputCommand);
      
      if (match) {
        logger.info(`Command similarity: ${inputCommand} -> ${match.command} (${match.percentage}%)`);
        return this.generateSuggestion(inputCommand, match);
      }

      return null;
    } catch (error) {
      logger.error('Error in similarity processing', error);
      return null;
    }
  }

  /**
   * Add new command to similarity check
   * @param {string} command - Command to add
   */
  addCommand(command) {
    if (!this.commands.includes(command)) {
      this.commands.push(command);
      logger.info(`Added command to similarity check: ${command}`);
    }
  }

  /**
   * Remove command from similarity check
   * @param {string} command - Command to remove
   */
  removeCommand(command) {
    const index = this.commands.indexOf(command);
    if (index > -1) {
      this.commands.splice(index, 1);
      logger.info(`Removed command from similarity check: ${command}`);
    }
  }

  /**
   * Get all commands
   * @returns {Array} All commands
   */
  getAllCommands() {
    return [...this.commands];
  }

  /**
   * Set similarity threshold
   * @param {number} threshold - Threshold (0-1)
   */
  setSimilarityThreshold(threshold) {
    if (threshold >= 0 && threshold <= 1) {
      this.similarityThreshold = threshold;
      logger.info(`Similarity threshold set to: ${threshold}`);
    }
  }
}

// Create singleton instance
const similarityMatcher = new SimilarityMatcher();

module.exports = similarityMatcher;
