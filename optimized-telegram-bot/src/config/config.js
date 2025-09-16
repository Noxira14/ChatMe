const path = require('path');
require('dotenv').config();

const config = {
  // Bot configuration
  bot: {
    token: process.env.BOT_TOKEN || '',
    adminId: process.env.ADMIN_ID ? [parseInt(process.env.ADMIN_ID)] : [],
    name: 'XMSBRA CPANEL BOT',
    version: '2.0.0',
    profilePicture: process.env.PROFILE_PICTURE || 'https://files.catbox.moe/7l5fvh.jpg',
    adminUrl: process.env.ADMIN_URL || 'https://t.me/yourusername',
    polling: {
      interval: 1000,
      params: {
        timeout: 10
      }
    }
  },

  // Server configuration
  server: {
    domain: process.env.DOMAIN || 'https://your-panel-domain.com',
    plta: process.env.PLTA || 'your_application_api_key_here',
    pltc: process.env.PLTC || 'your_client_api_key_here',
    location: parseInt(process.env.LOCATION) || 1,
    eggs: parseInt(process.env.EGGS) || 15,
    timeout: 15000
  },

  // Security configuration
  security: {
    maxRequestsPerMinute: parseInt(process.env.MAX_REQUESTS_PER_MINUTE) || 30,
    sessionTimeout: parseInt(process.env.SESSION_TIMEOUT) || 3600000,
    passwordLength: parseInt(process.env.PASSWORD_LENGTH) || 12,
    botPassword: process.env.BOT_PASSWORD || 'XMSBRA2024',
    similarityThreshold: 0.6
  },

  // Logging configuration
  logging: {
    level: process.env.LOG_LEVEL || 'info',
    maxFileSize: '10MB',
    maxFiles: 5,
    datePattern: 'YYYY-MM-DD'
  },

  // File paths
  paths: {
    root: path.resolve(__dirname, '../..'),
    data: path.resolve(__dirname, '../../data'),
    logs: path.resolve(__dirname, '../../logs'),
    
    // Data files
    adminFile: path.resolve(__dirname, '../../data/adminID.json'),
    premiumFile: path.resolve(__dirname, '../../data/premiumUsers.json'),
    serverFile: path.resolve(__dirname, '../../data/data-cpanel.json'),
    historyFile: path.resolve(__dirname, '../../data/server-history.json'),
    
    // New data files
    authFile: path.resolve(__dirname, '../../data/authentication.json'),
    groupAccessFile: path.resolve(__dirname, '../../data/groupAccess.json'),
    securityFile: path.resolve(__dirname, '../../data/security.json'),
    cleanupFile: path.resolve(__dirname, '../../data/cleanup.json'),
    bannedUsersFile: path.resolve(__dirname, '../../data/bannedUsers.json')
  },

  // Message templates
  messages: {
    welcome: `🤖 *SELAMAT DATANG DI XMSBRA CPANEL*

🔐 *Untuk Proses Verifikasi*
Tolong masukkan password:

Format: \`/auth <password>\`

📋 Ketik /help untuk melihat perintah yang tersedia.`,

    unauthorized: '❌ *AKSES DITOLAK*\n\nAnda tidak memiliki izin untuk menggunakan perintah ini.',
    
    processing: '⏳ *Memproses permintaan...*\n\nMohon tunggu sebentar.',
    
    error: '❌ *Terjadi kesalahan!*\n\nSilakan coba lagi nanti atau hubungi admin.',
    
    maintenance: '🔧 *MODE MAINTENANCE*\n\nBot sedang dalam maintenance. Silakan coba lagi nanti.',
    
    banned: '🚫 *AKSES DITOLAK*\n\nAnda telah dibanned dari menggunakan bot ini.',
    
    authRequired: '🔐 *AUTENTIKASI DIPERLUKAN*\n\nSilakan login terlebih dahulu dengan `/auth <password>`',
    
    commandNotFound: '*COMMAND UNDEFINED*\n\nCommand yang Anda masukkan tidak ditemukan.\n\nKetik /help untuk melihat daftar perintah.',
    
    groupOnly: '❌ *Perintah ini hanya dapat digunakan di grup!*',
    
    privateOnly: '❌ *Perintah ini hanya dapat digunakan di chat pribadi!*'
  },

  // Feature flags
  features: {
    similarity: true,
    authentication: true,
    groupAccess: true,
    securityMonitoring: true,
    autoCleanup: true,
    rateLimiting: true,
    logging: true,
    maintenance: true
  },

  // Cleanup configuration
  cleanup: {
    inactiveThreshold: 2 * 24 * 60 * 60 * 1000, // 2 days
    cleanupInterval: 5 * 60 * 60 * 1000, // 5 hours
    maxHistoryRecords: 50
  },

  // Security monitoring
  monitoring: {
    checkInterval: 30000, // 30 seconds
    maxAlerts: 100,
    trustedUserTypes: ['username', 'email', 'ip']
  },

  // Rate limiting
  rateLimit: {
    windowMs: 60 * 1000, // 1 minute
    maxRequests: 30,
    cleanupInterval: 5 * 60 * 1000 // 5 minutes
  },

  // Command categories
  commands: {
    public: ['start', 'help', 'cekid', 'auth'],
    premium: ['buatpanel', 'listpanel', 'delpanel', 'panel'],
    admin: [
      'addsrv', 'listsrv', 'delsrv', 'srvinfo',
      'addprem', 'delprem', 'listadmin',
      'addgc', 'delgc', 'listgc', 'joingc',
      'ban', 'unban', 'security_panel'
    ],
    owner: [
      'addowner', 'delowner', 'clearserver', 'stats',
      'maintenance', 'cleanup'
    ]
  },

  // API endpoints
  api: {
    pterodactyl: {
      users: '/api/application/users',
      servers: '/api/application/servers',
      locations: '/api/application/locations',
      nests: '/api/application/nests',
      eggs: '/api/application/nests/{nest}/eggs'
    }
  },

  // Default values
  defaults: {
    serverName: 'default',
    userRole: 'regular',
    panelTheme: 'default',
    language: 'id'
  }
};

module.exports = config;

// New paths for v2.3 features
config.PATHS.ANTILINK_DATA = path.join(__dirname, '../../data/antilink.json');
config.PATHS.TESTING_MODE = path.join(__dirname, '../../data/testing-mode.json');

// Initialize new data files if they don't exist
const fs = require('fs');

// Initialize antilink data
if (!fs.existsSync(config.PATHS.ANTILINK_DATA)) {
    fs.writeFileSync(config.PATHS.ANTILINK_DATA, JSON.stringify({}, null, 2));
}

// Initialize testing mode data
if (!fs.existsSync(config.PATHS.TESTING_MODE)) {
    fs.writeFileSync(config.PATHS.TESTING_MODE, JSON.stringify({
        enabled: false,
        enabledBy: null,
        enabledAt: null,
        disabledBy: null,
        disabledAt: null
    }, null, 2));
}

