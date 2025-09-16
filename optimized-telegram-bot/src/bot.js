const TelegramBot = require('node-telegram-bot-api');
const config = require('./config/config');
const logger = require('./utils/logger');
const Helpers = require('./utils/helpers');

// Import command classes
const UserCommands = require('./commands/userCommands');
const AdminCommands = require('./commands/adminCommands');
const NewCommands = require('./commands/newCommands');
const GroupCommands = require('./commands/groupCommands');

// Import middleware
const authMiddleware = require('./middleware/auth');
const rateLimitMiddleware = require('./middleware/rateLimit');
const groupAccessMiddleware = require('./middleware/groupAccess');
const authenticationMiddleware = require('./middleware/authentication');

// Import services
const securityService = require('./services/securityService');
const cleanupService = require('./services/cleanupService');

// Import handlers
const CallbackHandler = require('./handlers/callbackHandler');

// Import utilities
const similarityMatcher = require('./utils/similarity');

class XMSBRABot {
    constructor() {
        this.bot = new TelegramBot(config.BOT_TOKEN, { polling: true });
        this.userCommands = new UserCommands(this.bot);
        this.adminCommands = new AdminCommands(this.bot);
        this.newCommands = new NewCommands(this.bot);
        this.groupCommands = new GroupCommands(this.bot);
        this.callbackHandler = new CallbackHandler(this.bot);
        
        this.setupMiddleware();
        this.setupCommands();
        this.setupEventHandlers();
        this.startPeriodicTasks();
        
        logger.info('XMSBRA Bot v2.3 initialized successfully');
    }

    setupMiddleware() {
        // Rate limiting middleware
        this.bot.use((msg, metadata) => {
            return rateLimitMiddleware.checkRateLimit(msg, metadata);
        });

        // Authentication middleware
        this.bot.use((msg, metadata) => {
            return authenticationMiddleware.checkAuthentication(msg, metadata);
        });

        // Group access middleware
        this.bot.use((msg, metadata) => {
            return groupAccessMiddleware.checkGroupAccess(msg, metadata);
        });
    }

    setupCommands() {
        // Public commands
        this.bot.onText(/^\/start$/, this.handleCommand('start', this.userCommands.start.bind(this.userCommands)));
        this.bot.onText(/^\/help$/, this.handleCommand('help', this.userCommands.help.bind(this.userCommands)));
        this.bot.onText(/^\/cekid$/, this.handleCommand('cekid', this.userCommands.cekid.bind(this.userCommands)));
        this.bot.onText(/^\/about$/, this.handleCommand('about', this.groupCommands.about.bind(this.groupCommands)));
        this.bot.onText(/^\/privacy$/, this.handleCommand('privacy', this.groupCommands.privacyPolicy.bind(this.groupCommands)));
        this.bot.onText(/^\/commands$/, this.handleCommand('commands', this.groupCommands.commandsList.bind(this.groupCommands)));
        this.bot.onText(/^\/bugreport$/, this.handleCommand('bugreport', this.groupCommands.bugReport.bind(this.groupCommands)));

        // Authentication
        this.bot.onText(/^\/auth (.+)/, this.handleCommand('auth', this.newCommands.authenticate.bind(this.newCommands)));

        // Premium commands
        this.bot.onText(/^\/buatpanel (.+) (.+) (.+)/, this.handleCommand('buatpanel', this.userCommands.buatpanel.bind(this.userCommands)));
        this.bot.onText(/^\/listpanel$/, this.handleCommand('listpanel', this.userCommands.listpanel.bind(this.userCommands)));
        this.bot.onText(/^\/delpanel (.+)/, this.handleCommand('delpanel', this.userCommands.delpanel.bind(this.userCommands)));
        this.bot.onText(/^\/panel$/, this.handleCommand('panel', this.userCommands.panel.bind(this.userCommands)));
        this.bot.onText(/^\/status$/, this.handleCommand('status', this.userCommands.status.bind(this.userCommands)));

        // Admin commands
        this.bot.onText(/^\/addsrv (.+) (.+) (.+)/, this.handleCommand('addsrv', this.adminCommands.addsrv.bind(this.adminCommands)));
        this.bot.onText(/^\/listsrv$/, this.handleCommand('listsrv', this.adminCommands.listsrv.bind(this.adminCommands)));
        this.bot.onText(/^\/delsrv (.+)/, this.handleCommand('delsrv', this.adminCommands.delsrv.bind(this.adminCommands)));
        this.bot.onText(/^\/srvinfo (.+)/, this.handleCommand('srvinfo', this.adminCommands.srvinfo.bind(this.adminCommands)));
        this.bot.onText(/^\/addprem (.+)/, this.handleCommand('addprem', this.adminCommands.addprem.bind(this.adminCommands)));
        this.bot.onText(/^\/delprem (.+)/, this.handleCommand('delprem', this.adminCommands.delprem.bind(this.adminCommands)));
        this.bot.onText(/^\/listadmin$/, this.handleCommand('listadmin', this.adminCommands.listadmin.bind(this.adminCommands)));

        // Group management commands
        this.bot.onText(/^\/addgc (.+) (.+)/, this.handleCommand('addgc', this.newCommands.addGroupAccess.bind(this.newCommands)));
        this.bot.onText(/^\/delgc (.+)/, this.handleCommand('delgc', this.newCommands.deleteGroupAccess.bind(this.newCommands)));
        this.bot.onText(/^\/listgc$/, this.handleCommand('listgc', this.newCommands.listGroupAccess.bind(this.newCommands)));
        this.bot.onText(/^\/joingc (.+)/, this.handleCommand('joingc', this.newCommands.joinGroup.bind(this.newCommands)));

        // User management
        this.bot.onText(/^\/ban (.+)/, this.handleCommand('ban', this.newCommands.banUser.bind(this.newCommands)));
        this.bot.onText(/^\/unban (.+)/, this.handleCommand('unban', this.newCommands.unbanUser.bind(this.newCommands)));

        // Group features
        this.bot.onText(/^\/hidetag (.*)/, this.handleCommand('hidetag', this.groupCommands.hidetag.bind(this.groupCommands)));
        this.bot.onText(/^\/antilink (.+)/, this.handleCommand('antilink', this.groupCommands.antilink.bind(this.groupCommands)));

        // Owner commands
        this.bot.onText(/^\/addowner (.+)/, this.handleCommand('addowner', this.adminCommands.addowner.bind(this.adminCommands)));
        this.bot.onText(/^\/delowner (.+)/, this.handleCommand('delowner', this.adminCommands.delowner.bind(this.adminCommands)));
        this.bot.onText(/^\/clearserver$/, this.handleCommand('clearserver', this.adminCommands.clearserver.bind(this.adminCommands)));
        this.bot.onText(/^\/stats$/, this.handleCommand('stats', this.adminCommands.stats.bind(this.adminCommands)));
        this.bot.onText(/^\/maintenance (.+)/, this.handleCommand('maintenance', this.newCommands.maintenanceMode.bind(this.newCommands)));
        this.bot.onText(/^\/security_panel (.+)/, this.handleCommand('security_panel', this.newCommands.securityPanel.bind(this.newCommands)));
        this.bot.onText(/^\/cleanup (.+)/, this.handleCommand('cleanup', this.newCommands.cleanupSystem.bind(this.newCommands)));
        this.bot.onText(/^\/testing (.+)/, this.handleCommand('testing', this.groupCommands.testingMode.bind(this.groupCommands)));
    }

    setupEventHandlers() {
        // Handle callback queries
        this.bot.on('callback_query', (callbackQuery) => {
            this.callbackHandler.handleCallback(callbackQuery);
        });

        // Handle all messages for antilink check and similarity
        this.bot.on('message', async (msg) => {
            try {
                // Check antilink first
                await this.groupCommands.checkAntilink(msg);

                // Skip if message starts with / (command)
                if (msg.text && msg.text.startsWith('/')) {
                    return;
                }

                // Log fancy command if it's a command
                if (msg.text && msg.text.startsWith('/')) {
                    authenticationMiddleware.logFancyCommand(msg);
                }

            } catch (error) {
                logger.error('Error in message handler:', error);
            }
        });

        // Handle unknown commands with similarity suggestions
        this.bot.on('message', async (msg) => {
            if (msg.text && msg.text.startsWith('/')) {
                const command = msg.text.split(' ')[0];
                
                // Check if command exists in our handlers
                const knownCommands = [
                    '/start', '/help', '/cekid', '/about', '/privacy', '/commands', '/bugreport',
                    '/auth', '/buatpanel', '/listpanel', '/delpanel', '/panel', '/status',
                    '/addsrv', '/listsrv', '/delsrv', '/srvinfo', '/addprem', '/delprem', '/listadmin',
                    '/addgc', '/delgc', '/listgc', '/joingc', '/ban', '/unban',
                    '/hidetag', '/antilink', '/addowner', '/delowner', '/clearserver', '/stats',
                    '/maintenance', '/security_panel', '/cleanup', '/testing'
                ];

                if (!knownCommands.includes(command)) {
                    await similarityMatcher.processSimilarity(msg, this.bot);
                }
            }
        });

        // Error handling
        this.bot.on('polling_error', (error) => {
            logger.error('Polling error:', error);
        });

        this.bot.on('error', (error) => {
            logger.error('Bot error:', error);
        });
    }

    handleCommand(commandName, handler) {
        return async (msg, match) => {
            const startTime = Date.now();
            
            try {
                // Check if bot is in maintenance mode
                const testingData = Helpers.loadJSON(config.PATHS.TESTING_MODE);
                const isOwner = config.OWNER_IDS.includes(msg.from.id);
                
                // Check testing mode
                if (testingData.enabled && !isOwner) {
                    // Allow all commands in testing mode with disclaimer
                    const testingMessage = `🧪 **MODE TESTING AKTIF**\n\nAnda sedang menggunakan fitur dalam mode testing/demo.\n\n⚠️ **DISCLAIMER:**\n• Data yang dibuat bersifat dummy\n• Tidak untuk penggunaan production\n• Fitur mungkin tidak berfungsi penuh\n\n**Cara menonaktifkan:** Hubungi owner\n\n**Melanjutkan command...** ⏳`;
                    
                    await this.bot.sendMessage(msg.chat.id, testingMessage, {
                        parse_mode: 'Markdown',
                        reply_markup: {
                            inline_keyboard: [[
                                { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                            ]]
                        }
                    });
                }

                // Execute the command
                await handler(msg, match);
                
                const responseTime = Date.now() - startTime;
                logger.info(`Command ${commandName} executed in ${responseTime}ms by ${msg.from.first_name}`);
                
            } catch (error) {
                logger.error(`Error in command ${commandName}:`, error);
                
                await this.bot.sendMessage(msg.chat.id, `❌ Terjadi kesalahan saat menjalankan command ${commandName}!`, {
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }
        };
    }

    startPeriodicTasks() {
        // Daily statistics report
        setInterval(async () => {
            try {
                const stats = await this.generateDailyStats();
                
                for (const ownerId of config.OWNER_IDS) {
                    await this.bot.sendMessage(ownerId, stats, {
                        parse_mode: 'Markdown',
                        reply_markup: {
                            inline_keyboard: [[
                                { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                            ]]
                        }
                    });
                }
            } catch (error) {
                logger.error('Error sending daily stats:', error);
            }
        }, 24 * 60 * 60 * 1000); // 24 hours

        // Memory monitoring
        setInterval(() => {
            const memUsage = process.memoryUsage();
            const heapUsedMB = Math.round(memUsage.heapUsed / 1024 / 1024);
            
            if (heapUsedMB > config.MEMORY_ALERT_THRESHOLD / 1024 / 1024) {
                logger.warn(`High memory usage: ${heapUsedMB}MB`);
                
                // Send alert to owners
                for (const ownerId of config.OWNER_IDS) {
                    this.bot.sendMessage(ownerId, `⚠️ **Peringatan Memory**\n\nPenggunaan memory tinggi: ${heapUsedMB}MB\n\nSilahkan periksa sistem.`, {
                        parse_mode: 'Markdown',
                        reply_markup: {
                            inline_keyboard: [[
                                { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                            ]]
                        }
                    });
                }
            }
        }, 60 * 60 * 1000); // 1 hour

        // Security service alerts forwarding
        securityService.on('securityAlert', async (alert) => {
            for (const ownerId of config.OWNER_IDS) {
                await this.bot.sendMessage(ownerId, alert, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }
        });

        // Cleanup service notifications
        cleanupService.on('cleanupComplete', async (report) => {
            for (const ownerId of config.OWNER_IDS) {
                await this.bot.sendMessage(ownerId, report, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }
        });
    }

    async generateDailyStats() {
        const memUsage = process.memoryUsage();
        const uptime = process.uptime();
        
        // Get rate limit stats
        const rateLimitStats = rateLimitMiddleware.getStats();
        
        // Get security stats
        const securityStats = securityService.getStats();
        
        // Get cleanup stats
        const cleanupStats = cleanupService.getStats();

        return `📊 **LAPORAN HARIAN XMSBRA BOT**\n\n**⏱️ Runtime:**\n• Uptime: ${Helpers.getRuntime()}\n• Memory: ${Helpers.formatBytes(memUsage.heapUsed)}\n\n**👥 Aktivitas Pengguna:**\n• User aktif: ${rateLimitStats.activeUsers}\n• Total request: ${rateLimitStats.totalRequests}\n• Request ditolak: ${rateLimitStats.rejectedRequests}\n\n**🛡️ Keamanan:**\n• Alert keamanan: ${securityStats.recentAlerts}\n• Status monitoring: ${securityStats.isActive ? 'Aktif' : 'Nonaktif'}\n\n**🧹 Cleanup:**\n• Rata-rata user tidak aktif: ${cleanupStats.averageInactive}\n• Cleanup terakhir: ${cleanupStats.lastCleanup}\n\n**📅 Tanggal:** ${new Date().toLocaleDateString('id-ID')}\n**🕐 Waktu:** ${new Date().toLocaleTimeString('id-ID')}`;
    }

    // Graceful shutdown
    async shutdown() {
        logger.info('Shutting down XMSBRA Bot...');
        
        try {
            await securityService.stop();
            await cleanupService.stop();
            await this.bot.stopPolling();
            
            logger.info('XMSBRA Bot shutdown complete');
            process.exit(0);
        } catch (error) {
            logger.error('Error during shutdown:', error);
            process.exit(1);
        }
    }
}

// Handle process signals
process.on('SIGINT', async () => {
    if (global.xmsbraBot) {
        await global.xmsbraBot.shutdown();
    } else {
        process.exit(0);
    }
});

process.on('SIGTERM', async () => {
    if (global.xmsbraBot) {
        await global.xmsbraBot.shutdown();
    } else {
        process.exit(0);
    }
});

module.exports = XMSBRABot;
