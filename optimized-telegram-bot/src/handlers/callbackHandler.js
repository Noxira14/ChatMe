const config = require('../config/config');
const Helpers = require('../utils/helpers');
const logger = require('../utils/logger');

class CallbackHandler {
    constructor(bot) {
        this.bot = bot;
    }

    async handleCallback(callbackQuery) {
        try {
            const chatId = callbackQuery.message.chat.id;
            const messageId = callbackQuery.message.message_id;
            const userId = callbackQuery.from.id;
            const data = callbackQuery.data;

            // Answer callback query to remove loading state
            await this.bot.answerCallbackQuery(callbackQuery.id);

            // Route callback data to appropriate handler
            switch (data) {
                case 'help':
                    await this.handleHelp(chatId, messageId, userId);
                    break;
                case 'start':
                    await this.handleStart(chatId, messageId, userId);
                    break;
                case 'panel':
                    await this.handlePanel(chatId, messageId, userId);
                    break;
                case 'about':
                    await this.handleAbout(chatId, messageId, userId);
                    break;
                case 'privacy':
                    await this.handlePrivacy(chatId, messageId, userId);
                    break;
                case 'commands':
                    await this.handleCommands(chatId, messageId, userId);
                    break;
                case 'bugreport':
                    await this.handleBugReport(chatId, messageId, userId);
                    break;
                case 'status':
                    await this.handleStatus(chatId, messageId, userId);
                    break;
                case 'listpanel':
                    await this.handleListPanel(chatId, messageId, userId);
                    break;
                case 'listsrv':
                    await this.handleListServer(chatId, messageId, userId);
                    break;
                case 'listadmin':
                    await this.handleListAdmin(chatId, messageId, userId);
                    break;
                case 'listgc':
                    await this.handleListGroupAccess(chatId, messageId, userId);
                    break;
                case 'stats':
                    await this.handleStats(chatId, messageId, userId);
                    break;
                case 'maintenance_on':
                    await this.handleMaintenanceToggle(chatId, messageId, userId, 'on');
                    break;
                case 'maintenance_off':
                    await this.handleMaintenanceToggle(chatId, messageId, userId, 'off');
                    break;
                case 'maintenance_status':
                    await this.handleMaintenanceStatus(chatId, messageId, userId);
                    break;
                case 'security_status':
                    await this.handleSecurityStatus(chatId, messageId, userId);
                    break;
                case 'cleanup_status':
                    await this.handleCleanupStatus(chatId, messageId, userId);
                    break;
                case 'confirm_clearserver':
                    await this.handleClearServerConfirm(chatId, messageId, userId);
                    break;
                case 'cancel_clearserver':
                    await this.handleClearServerCancel(chatId, messageId, userId);
                    break;
                default:
                    // Handle dynamic callbacks
                    if (data.startsWith('srvinfo_')) {
                        const serverName = data.replace('srvinfo_', '');
                        await this.handleServerInfo(chatId, messageId, userId, serverName);
                    } else if (data.startsWith('gc_settings_')) {
                        const groupId = data.replace('gc_settings_', '');
                        await this.handleGroupSettings(chatId, messageId, userId, groupId);
                    } else {
                        await this.handleUnknownCallback(chatId, messageId, data);
                    }
                    break;
            }

            logger.info(`Callback handled: ${data} by ${callbackQuery.from.first_name}`);

        } catch (error) {
            logger.error('Error in callback handler:', error);
            
            try {
                await this.bot.answerCallbackQuery(callbackQuery.id, {
                    text: '❌ Terjadi kesalahan!',
                    show_alert: true
                });
            } catch (answerError) {
                logger.error('Error answering callback query:', answerError);
            }
        }
    }

    async handleHelp(chatId, messageId, userId) {
        // Check user role
        const isOwner = config.OWNER_IDS.includes(userId);
        const adminData = Helpers.loadJSON(config.PATHS.ADMIN_DATA);
        const isAdmin = adminData.includes(userId);
        const premiumData = Helpers.loadJSON(config.PATHS.PREMIUM_DATA);
        const isPremium = premiumData.includes(userId);

        let helpMessage = `📋 **BANTUAN XMSBRA BOT v2.3**\n\n`;

        // Public commands
        helpMessage += `**🔓 Perintah Umum:**\n`;
        helpMessage += `/start - Memulai bot\n`;
        helpMessage += `/help - Menampilkan bantuan\n`;
        helpMessage += `/cekid - Cek ID Telegram Anda\n`;
        helpMessage += `/about - Informasi tentang bot\n`;
        helpMessage += `/privacy - Kebijakan privasi\n`;
        helpMessage += `/commands - Daftar lengkap perintah\n`;
        helpMessage += `/bugreport - Laporkan bug atau saran\n\n`;

        // Premium commands
        if (isPremium || isAdmin || isOwner) {
            helpMessage += `**💎 Perintah Premium:**\n`;
            helpMessage += `/buatpanel <email> <username> <nama> - Buat panel baru\n`;
            helpMessage += `/listpanel - Lihat daftar panel Anda\n`;
            helpMessage += `/delpanel <username> - Hapus panel\n`;
            helpMessage += `/panel - Menu manajemen panel\n`;
            helpMessage += `/status - Status bot dan server\n\n`;
        }

        // Admin commands
        if (isAdmin || isOwner) {
            helpMessage += `**👑 Perintah Admin:**\n`;
            helpMessage += `/addsrv <nama> <domain> <apikey> - Tambah server\n`;
            helpMessage += `/listsrv - Daftar semua server\n`;
            helpMessage += `/delsrv <nama> - Hapus server\n`;
            helpMessage += `/addprem <user_id> - Tambah user premium\n`;
            helpMessage += `/delprem <user_id> - Hapus user premium\n`;
            helpMessage += `/addgc <group_id> <tipe> - Tambah akses grup\n`;
            helpMessage += `/delgc <group_id> - Hapus akses grup\n`;
            helpMessage += `/listgc - Daftar akses grup\n`;
            helpMessage += `/ban <user_id> - Ban user\n`;
            helpMessage += `/unban <user_id> - Unban user\n\n`;

            helpMessage += `**🏷️ Perintah Grup:**\n`;
            helpMessage += `/hidetag <pesan> - Tag semua member grup\n`;
            helpMessage += `/antilink <on/off> - Aktifkan anti-link\n\n`;
        }

        // Owner commands
        if (isOwner) {
            helpMessage += `**🔱 Perintah Owner:**\n`;
            helpMessage += `/addowner <user_id> - Tambah admin/owner\n`;
            helpMessage += `/delowner <user_id> - Hapus admin/owner\n`;
            helpMessage += `/maintenance <on/off/status> - Mode maintenance\n`;
            helpMessage += `/security_panel <aksi> - Panel keamanan\n`;
            helpMessage += `/cleanup <aksi> - Sistem cleanup\n`;
            helpMessage += `/testing <on/off/status> - Mode testing\n`;
            helpMessage += `/stats - Statistik bot lengkap\n`;
            helpMessage += `/clearserver - Hapus semua server\n\n`;
        }

        helpMessage += `**📞 Butuh Bantuan Lebih?**\nHubungi developer untuk support teknis dan pertanyaan lainnya.`;

        await this.bot.editMessageText(helpMessage, {
            chat_id: chatId,
            message_id: messageId,
            parse_mode: 'Markdown',
            reply_markup: {
                inline_keyboard: [
                    [
                        { text: '📊 Commands', callback_data: 'commands' },
                        { text: 'ℹ️ About', callback_data: 'about' }
                    ],
                    [
                        { text: '🐛 Bug Report', callback_data: 'bugreport' },
                        { text: '🔒 Privacy', callback_data: 'privacy' }
                    ],
                    [
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]
                ]
            }
        });
    }

    async handleStart(chatId, messageId, userId) {
        const firstName = 'User'; // We don't have access to first name in callback
        
        const welcomeMessage = `🤖 **SELAMAT DATANG DI XMSBRA BOT v2.3**\n\nHalo! Selamat datang di bot manajemen cPanel terdepan.\n\n**✨ Fitur Utama:**\n• 🔐 Sistem keamanan berlapis\n• 👥 Manajemen grup canggih\n• 🛡️ Monitoring real-time\n• 🧹 Pembersihan otomatis\n• 📊 Analytics mendalam\n• 🏷️ Hidetag untuk grup\n• 🚫 Anti-link protection\n\n**🚀 Mulai Sekarang:**\n• Ketik /help untuk bantuan\n• Ketik /commands untuk daftar perintah\n• Ketik /panel untuk manajemen cPanel\n\n**📞 Butuh bantuan?**\nHubungi developer untuk support teknis.`;

        await this.bot.editMessageText(welcomeMessage, {
            chat_id: chatId,
            message_id: messageId,
            parse_mode: 'Markdown',
            reply_markup: {
                inline_keyboard: [
                    [
                        { text: '📋 Help', callback_data: 'help' },
                        { text: '🎛️ Panel', callback_data: 'panel' }
                    ],
                    [
                        { text: '📊 Commands', callback_data: 'commands' },
                        { text: 'ℹ️ About', callback_data: 'about' }
                    ],
                    [
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]
                ]
            }
        });
    }

    async handlePanel(chatId, messageId, userId) {
        // Check if user has premium access
        const hasAccess = await this.checkUserAccess(userId, 'viewPanels');
        if (!hasAccess) {
            return this.bot.editMessageText('❌ **Akses Ditolak**\n\nAnda tidak memiliki akses premium.', {
                chat_id: chatId,
                message_id: messageId,
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }

        const panelMessage = `🎛️ **MENU MANAJEMEN PANEL**\n\nSelamat datang di menu manajemen panel cPanel.\n\n**Pilih aksi yang ingin dilakukan:**`;

        await this.bot.editMessageText(panelMessage, {
            chat_id: chatId,
            message_id: messageId,
            parse_mode: 'Markdown',
            reply_markup: {
                inline_keyboard: [
                    [
                        { text: '📋 List Panel', callback_data: 'listpanel' },
                        { text: '📊 Status', callback_data: 'status' }
                    ],
                    [
                        { text: '📋 Help', callback_data: 'help' },
                        { text: '🏠 Main Menu', callback_data: 'start' }
                    ],
                    [
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]
                ]
            }
        });
    }

    async handleAbout(chatId, messageId, userId) {
        const aboutMessage = `ℹ️ **TENTANG XMSBRA BOT**\n\n**🤖 Informasi Bot:**\n• Nama: XMSBRA Telegram Bot\n• Versi: 2.3.0\n• Developer: @ibradecode\n• Bahasa: Node.js\n• Database: JSON Files\n\n**🚀 Fitur Utama:**\n• Manajemen cPanel otomatis\n• Sistem keamanan berlapis\n• Monitoring real-time\n• Group management\n• Anti-link protection\n• Hidetag functionality\n• Testing mode\n• Analytics & reporting\n\n**📊 Statistik:**\n• Uptime: ${Helpers.getRuntime()}\n• Memory: ${Helpers.formatBytes(process.memoryUsage().heapUsed)}\n• Platform: ${process.platform}\n• Node: ${process.version}\n\n**📞 Support:**\nUntuk bantuan teknis, bug report, atau saran pengembangan, silahkan hubungi developer.`;

        await this.bot.editMessageText(aboutMessage, {
            chat_id: chatId,
            message_id: messageId,
            parse_mode: 'Markdown',
            reply_markup: {
                inline_keyboard: [
                    [
                        { text: '📋 Commands', callback_data: 'commands' },
                        { text: '🔒 Privacy', callback_data: 'privacy' }
                    ],
                    [
                        { text: '🐛 Bug Report', callback_data: 'bugreport' },
                        { text: '🏠 Main Menu', callback_data: 'start' }
                    ],
                    [
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]
                ]
            }
        });
    }

    async handlePrivacy(chatId, messageId, userId) {
        const privacyMessage = `🔒 **KEBIJAKAN PRIVASI XMSBRA BOT**\n\n**📋 Data yang Dikumpulkan:**\n• User ID Telegram\n• Nama pengguna\n• Username (jika ada)\n• Waktu aktivitas\n• Command yang digunakan\n• Data panel yang dibuat\n\n**🛡️ Penggunaan Data:**\n• Autentikasi pengguna\n• Manajemen akses\n• Logging aktivitas\n• Statistik penggunaan\n• Troubleshooting\n\n**🔐 Keamanan Data:**\n• Data disimpan lokal\n• Enkripsi password\n• Rate limiting\n• Access control\n• Regular cleanup\n\n**📤 Berbagi Data:**\n• Data TIDAK dibagikan ke pihak ketiga\n• Data hanya untuk operasional bot\n• Akses terbatas untuk admin\n\n**🗑️ Penghapusan Data:**\n• Data inactive user dihapus otomatis\n• User dapat request penghapusan\n• Backup data dihapus berkala\n\n**📞 Kontak:**\nUntuk pertanyaan privasi, hubungi developer.`;

        await this.bot.editMessageText(privacyMessage, {
            chat_id: chatId,
            message_id: messageId,
            parse_mode: 'Markdown',
            reply_markup: {
                inline_keyboard: [
                    [
                        { text: 'ℹ️ About', callback_data: 'about' },
                        { text: '📋 Commands', callback_data: 'commands' }
                    ],
                    [
                        { text: '🐛 Bug Report', callback_data: 'bugreport' },
                        { text: '🏠 Main Menu', callback_data: 'start' }
                    ],
                    [
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]
                ]
            }
        });
    }

    async handleCommands(chatId, messageId, userId) {
        // Check user role
        const isOwner = config.OWNER_IDS.includes(userId);
        const adminData = Helpers.loadJSON(config.PATHS.ADMIN_DATA);
        const isAdmin = adminData.includes(userId);
        const premiumData = Helpers.loadJSON(config.PATHS.PREMIUM_DATA);
        const isPremium = premiumData.includes(userId);

        let commandsMessage = `📊 **DAFTAR LENGKAP PERINTAH**\n\n`;

        // Public commands
        commandsMessage += `**🔓 Perintah Umum:**\n`;
        commandsMessage += `• /start - Memulai bot\n`;
        commandsMessage += `• /help - Bantuan lengkap\n`;
        commandsMessage += `• /cekid - Cek ID Telegram\n`;
        commandsMessage += `• /about - Info bot\n`;
        commandsMessage += `• /privacy - Kebijakan privasi\n`;
        commandsMessage += `• /commands - Daftar perintah\n`;
        commandsMessage += `• /bugreport - Lapor bug\n`;
        commandsMessage += `• /auth <password> - Autentikasi\n\n`;

        // Premium commands
        if (isPremium || isAdmin || isOwner) {
            commandsMessage += `**💎 Perintah Premium:**\n`;
            commandsMessage += `• /buatpanel - Buat panel cPanel\n`;
            commandsMessage += `• /listpanel - Daftar panel\n`;
            commandsMessage += `• /delpanel - Hapus panel\n`;
            commandsMessage += `• /panel - Menu panel\n`;
            commandsMessage += `• /status - Status sistem\n\n`;
        }

        // Admin commands
        if (isAdmin || isOwner) {
            commandsMessage += `**👑 Perintah Admin:**\n`;
            commandsMessage += `• /addsrv - Tambah server\n`;
            commandsMessage += `• /listsrv - Daftar server\n`;
            commandsMessage += `• /delsrv - Hapus server\n`;
            commandsMessage += `• /srvinfo - Info server\n`;
            commandsMessage += `• /addprem - Tambah premium\n`;
            commandsMessage += `• /delprem - Hapus premium\n`;
            commandsMessage += `• /listadmin - Daftar admin\n`;
            commandsMessage += `• /addgc - Tambah grup\n`;
            commandsMessage += `• /delgc - Hapus grup\n`;
            commandsMessage += `• /listgc - Daftar grup\n`;
            commandsMessage += `• /joingc - Join grup\n`;
            commandsMessage += `• /ban - Ban user\n`;
            commandsMessage += `• /unban - Unban user\n`;
            commandsMessage += `• /hidetag - Tag member\n`;
            commandsMessage += `• /antilink - Anti-link\n\n`;
        }

        // Owner commands
        if (isOwner) {
            commandsMessage += `**🔱 Perintah Owner:**\n`;
            commandsMessage += `• /addowner - Tambah admin\n`;
            commandsMessage += `• /delowner - Hapus admin\n`;
            commandsMessage += `• /clearserver - Hapus server\n`;
            commandsMessage += `• /stats - Statistik\n`;
            commandsMessage += `• /maintenance - Mode maintenance\n`;
            commandsMessage += `• /security_panel - Panel keamanan\n`;
            commandsMessage += `• /cleanup - Sistem cleanup\n`;
            commandsMessage += `• /testing - Mode testing\n\n`;
        }

        commandsMessage += `**📝 Catatan:**\n• Gunakan /help untuk detail perintah\n• Parameter dalam <> wajib diisi\n• Hubungi developer untuk bantuan`;

        await this.bot.editMessageText(commandsMessage, {
            chat_id: chatId,
            message_id: messageId,
            parse_mode: 'Markdown',
            reply_markup: {
                inline_keyboard: [
                    [
                        { text: '📋 Help', callback_data: 'help' },
                        { text: 'ℹ️ About', callback_data: 'about' }
                    ],
                    [
                        { text: '🔒 Privacy', callback_data: 'privacy' },
                        { text: '🏠 Main Menu', callback_data: 'start' }
                    ],
                    [
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]
                ]
            }
        });
    }

    async handleBugReport(chatId, messageId, userId) {
        const bugReportMessage = `🐛 **LAPORAN BUG & SARAN**\n\n**📞 Hubungi Developer:**\nUntuk melaporkan bug, memberikan saran, atau meminta fitur baru, silahkan hubungi developer melalui Telegram.\n\n**📝 Informasi yang Diperlukan:**\n• Deskripsi masalah/saran\n• Langkah untuk reproduce bug\n• Screenshot (jika ada)\n• User ID Anda: ${userId}\n• Waktu kejadian\n\n**🚀 Fitur yang Bisa Diminta:**\n• Fitur manajemen baru\n• Integrasi dengan service lain\n• Peningkatan UI/UX\n• Optimasi performa\n• Fitur keamanan tambahan\n\n**⚡ Response Time:**\n• Bug critical: < 24 jam\n• Bug normal: 1-3 hari\n• Saran fitur: 1-7 hari\n\n**🙏 Terima kasih atas kontribusi Anda!**`;

        await this.bot.editMessageText(bugReportMessage, {
            chat_id: chatId,
            message_id: messageId,
            parse_mode: 'Markdown',
            reply_markup: {
                inline_keyboard: [
                    [
                        { text: '📋 Help', callback_data: 'help' },
                        { text: 'ℹ️ About', callback_data: 'about' }
                    ],
                    [
                        { text: '📊 Commands', callback_data: 'commands' },
                        { text: '🏠 Main Menu', callback_data: 'start' }
                    ],
                    [
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]
                ]
            }
        });
    }

    async handleStatus(chatId, messageId, userId) {
        const memUsage = process.memoryUsage();
        
        const statusMessage = `📊 **STATUS XMSBRA BOT**\n\n**🤖 Bot Info:**\n• Version: 2.3.0\n• Status: Online ✅\n• Uptime: ${Helpers.getRuntime()}\n\n**💾 Memory Usage:**\n• Used: ${Helpers.formatBytes(memUsage.heapUsed)}\n• Total: ${Helpers.formatBytes(memUsage.heapTotal)}\n\n**📅 System Info:**\n• Tanggal: ${new Date().toLocaleDateString('id-ID')}\n• Waktu: ${new Date().toLocaleTimeString('id-ID')}\n• Timezone: Asia/Makassar\n\n**🔧 Services:**\n• Panel Service: Active ✅\n• Security Monitor: Active ✅\n• Cleanup Service: Active ✅`;

        await this.bot.editMessageText(statusMessage, {
            chat_id: chatId,
            message_id: messageId,
            parse_mode: 'Markdown',
            reply_markup: {
                inline_keyboard: [
                    [
                        { text: '🔄 Refresh', callback_data: 'status' },
                        { text: '📊 Stats', callback_data: 'stats' }
                    ],
                    [
                        { text: '🏠 Main Menu', callback_data: 'start' },
                        { text: '📋 Help', callback_data: 'help' }
                    ],
                    [
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]
                ]
            }
        });
    }

    async handleUnknownCallback(chatId, messageId, data) {
        await this.bot.editMessageText(`❌ **Callback Tidak Dikenal**\n\nCallback "${data}" tidak dikenali sistem.\n\nSilahkan gunakan menu yang tersedia.`, {
            chat_id: chatId,
            message_id: messageId,
            parse_mode: 'Markdown',
            reply_markup: {
                inline_keyboard: [
                    [
                        { text: '🏠 Main Menu', callback_data: 'start' },
                        { text: '📋 Help', callback_data: 'help' }
                    ],
                    [
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]
                ]
            }
        });
    }

    // Helper method to check user access
    async checkUserAccess(userId, permission) {
        // Check if user is owner
        if (config.OWNER_IDS.includes(userId)) {
            return true;
        }

        // Check if user is admin
        const adminData = Helpers.loadJSON(config.PATHS.ADMIN_DATA);
        if (adminData.includes(userId)) {
            return true;
        }

        // Check if user is premium
        const premiumData = Helpers.loadJSON(config.PATHS.PREMIUM_DATA);
        if (premiumData.includes(userId)) {
            return true;
        }

        // Check group access
        const groupAccessData = Helpers.loadJSON(config.PATHS.GROUP_ACCESS);
        for (const groupId in groupAccessData) {
            const groupData = groupAccessData[groupId];
            if (groupData.members && groupData.members.includes(userId)) {
                return true;
            }
        }

        // Check testing mode
        const testingData = Helpers.loadJSON(config.PATHS.TESTING_MODE);
        if (testingData.enabled) {
            return true; // Allow access in testing mode
        }

        return false;
    }

    // Additional callback handlers can be added here
    async handleListPanel(chatId, messageId, userId) {
        // Implementation for list panel callback
        await this.bot.editMessageText('📋 **Daftar Panel**\n\nFitur ini akan menampilkan daftar panel Anda.\n\nGunakan command: /listpanel', {
            chat_id: chatId,
            message_id: messageId,
            parse_mode: 'Markdown',
            reply_markup: {
                inline_keyboard: [
                    [
                        { text: '🎛️ Panel Menu', callback_data: 'panel' },
                        { text: '🏠 Main Menu', callback_data: 'start' }
                    ],
                    [
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]
                ]
            }
        });
    }

    async handleListServer(chatId, messageId, userId) {
        // Check admin access
        if (!this.checkAdminAccess(userId)) {
            return this.bot.editMessageText('❌ **Akses Ditolak**\n\nHanya admin yang dapat melihat daftar server.', {
                chat_id: chatId,
                message_id: messageId,
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }

        await this.bot.editMessageText('📋 **Daftar Server**\n\nFitur ini akan menampilkan daftar server.\n\nGunakan command: /listsrv', {
            chat_id: chatId,
            message_id: messageId,
            parse_mode: 'Markdown',
            reply_markup: {
                inline_keyboard: [
                    [
                        { text: '📊 Stats', callback_data: 'stats' },
                        { text: '🏠 Main Menu', callback_data: 'start' }
                    ],
                    [
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]
                ]
            }
        });
    }

    async handleListAdmin(chatId, messageId, userId) {
        // Check admin access
        if (!this.checkAdminAccess(userId)) {
            return this.bot.editMessageText('❌ **Akses Ditolak**\n\nHanya admin yang dapat melihat daftar admin.', {
                chat_id: chatId,
                message_id: messageId,
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }

        await this.bot.editMessageText('👑 **Daftar Admin**\n\nFitur ini akan menampilkan daftar admin dan premium.\n\nGunakan command: /listadmin', {
            chat_id: chatId,
            message_id: messageId,
            parse_mode: 'Markdown',
            reply_markup: {
                inline_keyboard: [
                    [
                        { text: '📊 Stats', callback_data: 'stats' },
                        { text: '🏠 Main Menu', callback_data: 'start' }
                    ],
                    [
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]
                ]
            }
        });
    }

    async handleListGroupAccess(chatId, messageId, userId) {
        // Check admin access
        if (!this.checkAdminAccess(userId)) {
            return this.bot.editMessageText('❌ **Akses Ditolak**\n\nHanya admin yang dapat melihat daftar akses grup.', {
                chat_id: chatId,
                message_id: messageId,
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }

        await this.bot.editMessageText('📋 **Daftar Akses Grup**\n\nFitur ini akan menampilkan daftar akses grup.\n\nGunakan command: /listgc', {
            chat_id: chatId,
            message_id: messageId,
            parse_mode: 'Markdown',
            reply_markup: {
                inline_keyboard: [
                    [
                        { text: '📊 Stats', callback_data: 'stats' },
                        { text: '🏠 Main Menu', callback_data: 'start' }
                    ],
                    [
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]
                ]
            }
        });
    }

    async handleStats(chatId, messageId, userId) {
        // Check owner access
        if (!config.OWNER_IDS.includes(userId)) {
            return this.bot.editMessageText('❌ **Akses Ditolak**\n\nHanya owner yang dapat melihat statistik lengkap.', {
                chat_id: chatId,
                message_id: messageId,
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }

        await this.bot.editMessageText('📊 **Statistik Bot**\n\nFitur ini akan menampilkan statistik lengkap.\n\nGunakan command: /stats', {
            chat_id: chatId,
            message_id: messageId,
            parse_mode: 'Markdown',
            reply_markup: {
                inline_keyboard: [
                    [
                        { text: '🔄 Refresh', callback_data: 'stats' },
                        { text: '📊 Status', callback_data: 'status' }
                    ],
                    [
                        { text: '🏠 Main Menu', callback_data: 'start' },
                        { text: '📋 Help', callback_data: 'help' }
                    ],
                    [
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]
                ]
            }
        });
    }

    checkAdminAccess(userId) {
        // Check if user is owner
        if (config.OWNER_IDS.includes(userId)) {
            return true;
        }

        // Check if user is admin
        const adminData = Helpers.loadJSON(config.PATHS.ADMIN_DATA);
        return adminData.includes(userId);
    }
}

module.exports = CallbackHandler;
