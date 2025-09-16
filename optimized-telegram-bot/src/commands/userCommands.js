const config = require('../config/config');
const PanelService = require('../services/panelService');
const Helpers = require('../utils/helpers');
const logger = require('../utils/logger');

class UserCommands {
    constructor(bot) {
        this.bot = bot;
        this.panelService = new PanelService();
    }

    async start(msg) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const firstName = msg.from.first_name;

            // Check if user is authenticated
            const authData = Helpers.loadJSON(config.PATHS.AUTH_DATA);
            if (!authData[userId]) {
                return this.bot.sendMessage(chatId, `🔐 **Selamat datang ${firstName}!**\n\nUntuk menggunakan bot ini, Anda perlu melakukan autentikasi terlebih dahulu.\n\n**Cara autentikasi:**\n/auth <password>\n\n**Contoh:**\n/auth XMSBRA2024`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const welcomeMessage = `🤖 **SELAMAT DATANG DI XMSBRA BOT v2.3**\n\nHalo ${firstName}! Selamat datang di bot manajemen cPanel terdepan.\n\n**✨ Fitur Utama:**\n• 🔐 Sistem keamanan berlapis\n• 👥 Manajemen grup canggih\n• 🛡️ Monitoring real-time\n• 🧹 Pembersihan otomatis\n• 📊 Analytics mendalam\n• 🏷️ Hidetag untuk grup\n• 🚫 Anti-link protection\n\n**🚀 Mulai Sekarang:**\n• Ketik /help untuk bantuan\n• Ketik /commands untuk daftar perintah\n• Ketik /panel untuk manajemen cPanel\n\n**📞 Butuh bantuan?**\nHubungi developer untuk support teknis.`;

            await this.bot.sendMessage(chatId, welcomeMessage, {
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

            logger.info(`Start command executed by ${firstName} (${userId})`);

        } catch (error) {
            logger.error('Error in start command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat memproses command start!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async help(msg) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;

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

            await this.bot.sendMessage(chatId, helpMessage, {
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

            logger.info(`Help command executed by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in help command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat menampilkan bantuan!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async cekid(msg) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const firstName = msg.from.first_name;
            const username = msg.from.username;

            const idMessage = `🆔 **INFORMASI ID ANDA**\n\n**👤 Nama:** ${firstName}\n**🆔 User ID:** ${userId}\n**📱 Username:** ${username ? '@' + username : 'Tidak ada'}\n**💬 Chat ID:** ${chatId}\n**📅 Tanggal:** ${new Date().toLocaleDateString('id-ID')}\n**🕐 Waktu:** ${new Date().toLocaleTimeString('id-ID')}`;

            await this.bot.sendMessage(chatId, idMessage, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });

            logger.info(`Cekid command executed by ${firstName} (${userId})`);

        } catch (error) {
            logger.error('Error in cekid command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat mengecek ID!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async buatpanel(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const email = match[1];
            const username = match[2];
            const name = match[3];

            // Check if user has premium access
            const hasAccess = await this.checkUserAccess(userId, 'createPanel');
            if (!hasAccess) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nAnda tidak memiliki akses untuk membuat panel.\n\n**Cara mendapatkan akses:**\n• Hubungi admin untuk upgrade premium\n• Atau hubungi developer', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            // Validate input
            if (!this.validateEmail(email)) {
                return this.bot.sendMessage(chatId, '❌ **Format Email Salah**\n\nGunakan format email yang valid.\n\n**Contoh:** user@domain.com', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            if (username.length < 3) {
                return this.bot.sendMessage(chatId, '❌ **Username Terlalu Pendek**\n\nUsername minimal 3 karakter.', {
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            // Show processing message
            const processingMsg = await this.bot.sendMessage(chatId, '⏳ **Sedang Membuat Panel...**\n\nMohon tunggu sebentar.', {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });

            try {
                const result = await this.panelService.createPanel(email, username, name, userId);
                
                if (result.success) {
                    await this.bot.editMessageText(`✅ **Panel Berhasil Dibuat!**\n\n**📧 Email:** ${email}\n**👤 Username:** ${username}\n**📝 Nama:** ${name}\n**🔑 Password:** ${result.password}\n**🌐 Login:** ${result.loginUrl}\n\n**⚠️ Simpan informasi ini dengan baik!**`, {
                        chat_id: chatId,
                        message_id: processingMsg.message_id,
                        parse_mode: 'Markdown',
                        reply_markup: {
                            inline_keyboard: [
                                [
                                    { text: '🌐 Login Panel', url: result.loginUrl },
                                    { text: '📋 List Panel', callback_data: 'listpanel' }
                                ],
                                [
                                    { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                                ]
                            ]
                        }
                    });
                } else {
                    await this.bot.editMessageText(`❌ **Gagal Membuat Panel**\n\n**Error:** ${result.error}\n\nSilahkan coba lagi atau hubungi developer.`, {
                        chat_id: chatId,
                        message_id: processingMsg.message_id,
                        parse_mode: 'Markdown',
                        reply_markup: {
                            inline_keyboard: [[
                                { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                            ]]
                        }
                    });
                }

            } catch (error) {
                await this.bot.editMessageText(`❌ **Terjadi Kesalahan**\n\nGagal membuat panel. Silahkan coba lagi nanti.`, {
                    chat_id: chatId,
                    message_id: processingMsg.message_id,
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
                throw error;
            }

            logger.info(`Panel created by ${msg.from.first_name} for ${username}`);

        } catch (error) {
            logger.error('Error in buatpanel command:', error);
        }
    }

    async listpanel(msg) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;

            // Check if user has premium access
            const hasAccess = await this.checkUserAccess(userId, 'viewPanels');
            if (!hasAccess) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nAnda tidak memiliki akses untuk melihat panel.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const panels = await this.panelService.getUserPanels(userId);
            
            if (panels.length === 0) {
                return this.bot.sendMessage(chatId, '📋 **Daftar Panel Anda**\n\nAnda belum memiliki panel.\n\n**Cara membuat panel:**\n/buatpanel <email> <username> <nama>', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            let panelList = `📋 **DAFTAR PANEL ANDA**\n\n`;
            
            panels.forEach((panel, index) => {
                panelList += `**${index + 1}. ${panel.username}**\n`;
                panelList += `📧 Email: ${panel.email}\n`;
                panelList += `📝 Nama: ${panel.name}\n`;
                panelList += `🌐 Server: ${panel.server}\n`;
                panelList += `📅 Dibuat: ${new Date(panel.createdAt).toLocaleDateString('id-ID')}\n\n`;
            });

            panelList += `**Total Panel:** ${panels.length}`;

            await this.bot.sendMessage(chatId, panelList, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '➕ Buat Panel', callback_data: 'buatpanel' },
                            { text: '🗑️ Hapus Panel', callback_data: 'delpanel' }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            logger.info(`Panel list viewed by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in listpanel command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat mengambil daftar panel!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async delpanel(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const username = match[1];

            // Check if user has premium access
            const hasAccess = await this.checkUserAccess(userId, 'deletePanel');
            if (!hasAccess) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nAnda tidak memiliki akses untuk menghapus panel.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const result = await this.panelService.deletePanel(username, userId);
            
            if (result.success) {
                await this.bot.sendMessage(chatId, `✅ **Panel Berhasil Dihapus**\n\n**Username:** ${username}\n\nPanel telah dihapus dari sistem.`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [
                            [
                                { text: '📋 List Panel', callback_data: 'listpanel' },
                                { text: '➕ Buat Panel', callback_data: 'buatpanel' }
                            ],
                            [
                                { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                            ]
                        ]
                    }
                });
            } else {
                await this.bot.sendMessage(chatId, `❌ **Gagal Menghapus Panel**\n\n**Error:** ${result.error}`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            logger.info(`Panel ${username} deleted by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in delpanel command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat menghapus panel!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async panel(msg) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;

            // Check if user has premium access
            const hasAccess = await this.checkUserAccess(userId, 'viewPanels');
            if (!hasAccess) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nAnda tidak memiliki akses premium.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const panelMessage = `🎛️ **MENU MANAJEMEN PANEL**\n\nSelamat datang di menu manajemen panel cPanel.\n\n**Pilih aksi yang ingin dilakukan:**`;

            await this.bot.sendMessage(chatId, panelMessage, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '➕ Buat Panel', callback_data: 'buatpanel' },
                            { text: '📋 List Panel', callback_data: 'listpanel' }
                        ],
                        [
                            { text: '🗑️ Hapus Panel', callback_data: 'delpanel' },
                            { text: '📊 Status', callback_data: 'status' }
                        ],
                        [
                            { text: '📋 Help', callback_data: 'help' },
                            { text: '⚙️ Settings', callback_data: 'settings' }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            logger.info(`Panel menu accessed by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in panel command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat membuka menu panel!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async status(msg) {
        try {
            const chatId = msg.chat.id;
            const memUsage = process.memoryUsage();
            const uptime = process.uptime();

            const statusMessage = `📊 **STATUS XMSBRA BOT**\n\n**🤖 Bot Info:**\n• Version: 2.3.0\n• Status: Online ✅\n• Uptime: ${Helpers.getRuntime()}\n\n**💾 Memory Usage:**\n• Used: ${Helpers.formatBytes(memUsage.heapUsed)}\n• Total: ${Helpers.formatBytes(memUsage.heapTotal)}\n\n**📅 System Info:**\n• Tanggal: ${new Date().toLocaleDateString('id-ID')}\n• Waktu: ${new Date().toLocaleTimeString('id-ID')}\n• Timezone: Asia/Makassar\n\n**🔧 Services:**\n• Panel Service: Active ✅\n• Security Monitor: Active ✅\n• Cleanup Service: Active ✅`;

            await this.bot.sendMessage(chatId, statusMessage, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '🔄 Refresh', callback_data: 'status' },
                            { text: '📊 Stats', callback_data: 'stats' }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            logger.info(`Status checked by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in status command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat mengecek status!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    // Helper methods
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

    validateEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }
}

module.exports = UserCommands;
