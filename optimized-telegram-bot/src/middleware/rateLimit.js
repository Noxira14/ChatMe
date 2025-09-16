const config = require('../config/config');
const logger = require('../utils/logger');

class RateLimiter {
  constructor() {
    this.requests = new Map(); // userId -> { count, resetTime }
    this.cleanupInterval = setInterval(() => this.cleanup(), 60000); // Cleanup every minute
  }

  /**
   * Check if user is rate limited
   * @param {string|number} userId - User ID
   * @returns {Object} Rate limit status
   */
  checkRateLimit(userId) {
    const now = Date.now();
    const userKey = userId.toString();
    const windowMs = 60000; // 1 minute window
    const maxRequests = config.security.maxRequestsPerMinute;

    if (!this.requests.has(userKey)) {
      this.requests.set(userKey, {
        count: 1,
        resetTime: now + windowMs
      });
      return { allowed: true, remaining: maxRequests - 1 };
    }

    const userRequests = this.requests.get(userKey);

    // Reset if window has passed
    if (now > userRequests.resetTime) {
      this.requests.set(userKey, {
        count: 1,
        resetTime: now + windowMs
      });
      return { allowed: true, remaining: maxRequests - 1 };
    }

    // Check if limit exceeded
    if (userRequests.count >= maxRequests) {
      const resetIn = Math.ceil((userRequests.resetTime - now) / 1000);
      return {
        allowed: false,
        remaining: 0,
        resetIn,
        message: `⚠️ Terlalu banyak permintaan! Coba lagi dalam ${resetIn} detik.`
      };
    }

    // Increment count
    userRequests.count++;
    this.requests.set(userKey, userRequests);

    return {
      allowed: true,
      remaining: maxRequests - userRequests.count
    };
  }

  /**
   * Rate limit middleware
   * @param {Function} handler - Command handler
   * @returns {Function} Wrapped handler
   */
  rateLimit(handler) {
    return async (msg, match) => {
      const rateLimitResult = this.checkRateLimit(msg.from.id);

      if (!rateLimitResult.allowed) {
        logger.warn(`Rate limit exceeded for user ${msg.from.id}`);
        return { error: rateLimitResult.message };
      }

      // Log if user is approaching limit
      if (rateLimitResult.remaining <= 5) {
        logger.warn(`User ${msg.from.id} approaching rate limit: ${rateLimitResult.remaining} requests remaining`);
      }

      return await handler(msg, match);
    };
  }

  /**
   * Cleanup expired entries
   */
  cleanup() {
    const now = Date.now();
    let cleaned = 0;

    for (const [userId, data] of this.requests.entries()) {
      if (now > data.resetTime) {
        this.requests.delete(userId);
        cleaned++;
      }
    }

    if (cleaned > 0) {
      logger.debug(`Cleaned up ${cleaned} expired rate limit entries`);
    }
  }

  /**
   * Get rate limit stats
   * @returns {Object} Stats
   */
  getStats() {
    return {
      activeUsers: this.requests.size,
      totalRequests: Array.from(this.requests.values()).reduce((sum, data) => sum + data.count, 0)
    };
  }

  /**
   * Reset rate limit for user
   * @param {string|number} userId - User ID
   */
  resetUserLimit(userId) {
    this.requests.delete(userId.toString());
    logger.info(`Reset rate limit for user ${userId}`);
  }

  /**
   * Clear all rate limits
   */
  clearAll() {
    const count = this.requests.size;
    this.requests.clear();
    logger.info(`Cleared all rate limits (${count} entries)`);
  }

  /**
   * Destroy rate limiter
   */
  destroy() {
    if (this.cleanupInterval) {
      clearInterval(this.cleanupInterval);
      this.cleanupInterval = null;
    }
    this.requests.clear();
  }
}

// Create singleton instance
const rateLimiter = new RateLimiter();

module.exports = rateLimiter;
