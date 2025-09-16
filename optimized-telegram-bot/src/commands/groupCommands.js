const config = require('../config/config');
const Helpers = require('../utils/helpers');
const logger = require('../utils/logger');

class GroupCommands {
    constructor(bot) {
        this.bot = bot;
    }

    // Hidetag - Tag semua pengguna dalam group
    async hidetag(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const message = match[1] || 'Halo semua!';

            // Validasi apakah ini group
            if (msg.chat.type !== 'group' && msg.chat.type !== 'supergroup') {
                return this.bot.sendMessage(chatId, '❌ Command ini hanya bisa digunakan di group!', {
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            // Cek apakah user adalah admin atau owner
            const isOwner = config.OWNER_IDS.includes(userId);
            const chatMember = await this.bot.getChatMember(chatId, userId);
            const isAdmin = ['administrator', 'creator'].includes(chatMember.status);

            if (!isOwner && !isAdmin) {
                return this.bot.sendMessage(chatId, '❌ Hanya admin yang bisa menggunakan command ini!', {
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            // Ambil semua member group
            const chatMembers = await this.bot.getChatAdministrators(chatId);
            let mentions = '';
            
            // Buat mention untuk semua admin (karena kita tidak bisa get semua member)
            for (const member of chatMembers) {
                if (member.user.username) {
                    mentions += `@${member.user.username} `;
                } else {
                    mentions += `[${member.user.first_name}](tg://user?id=${member.user.id}) `;
                }
            }

            const finalMessage = `${message}\n\n${mentions}`;

            await this.bot.sendMessage(chatId, finalMessage, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });

            logger.info(`Hidetag executed by ${msg.from.first_name} in ${msg.chat.title}`);

        } catch (error) {
            logger.error('Error in hidetag command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat menjalankan command hidetag!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    // Antilink - Anti link untuk group
    async antilink(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const action = match[1]?.toLowerCase();

            // Validasi apakah ini group
            if (msg.chat.type !== 'group' && msg.chat.type !== 'supergroup') {
                return this.bot.sendMessage(chatId, '❌ Command ini hanya bisa digunakan di group!', {
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            // Cek apakah user adalah owner
            const isOwner = config.OWNER_IDS.includes(userId);
            if (!isOwner) {
                return this.bot.sendMessage(chatId, '❌ Hanya owner yang bisa menggunakan command ini!', {
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            if (!action || !['on', 'off'].includes(action)) {
                return this.bot.sendMessage(chatId, '❌ Gunakan: /antilink on atau /antilink off', {
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            // Load antilink settings
            const antilinkData = Helpers.loadJSON(config.PATHS.ANTILINK_DATA);
            
            if (action === 'on') {
                antilinkData[chatId] = {
                    enabled: true,
                    enabledBy: userId,
                    enabledAt: new Date().toISOString()
                };
                
                await this.bot.sendMessage(chatId, '✅ **Antilink diaktifkan!**\n\nSemua link yang dikirim akan otomatis dihapus.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            } else {
                delete antilinkData[chatId];
                
                await this.bot.sendMessage(chatId, '✅ **Antilink dinonaktifkan!**\n\nLink sekarang bisa dikirim di group ini.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            Helpers.saveJSON(config.PATHS.ANTILINK_DATA, antilinkData);
            logger.info(`Antilink ${action} by ${msg.from.first_name} in ${msg.chat.title}`);

        } catch (error) {
            logger.error('Error in antilink command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat mengatur antilink!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    // Check dan hapus link jika antilink aktif
    async checkAntilink(msg) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;

            // Skip jika bukan group
            if (msg.chat.type !== 'group' && msg.chat.type !== 'supergroup') {
                return;
            }

            // Skip jika user adalah owner atau admin
            const isOwner = config.OWNER_IDS.includes(userId);
            if (isOwner) return;

            try {
                const chatMember = await this.bot.getChatMember(chatId, userId);
                const isAdmin = ['administrator', 'creator'].includes(chatMember.status);
                if (isAdmin) return;
            } catch (error) {
                // Ignore error jika tidak bisa get chat member
            }

            // Load antilink settings
            const antilinkData = Helpers.loadJSON(config.PATHS.ANTILINK_DATA);
            
            if (!antilinkData[chatId] || !antilinkData[chatId].enabled) {
                return;
            }

            // Regex untuk detect link
            const linkRegex = /(https?:\/\/[^\s]+|www\.[^\s]+|t\.me\/[^\s]+|@[^\s]+|[^\s]+\.(com|net|org|io|co|id|me|ly|gg|xyz|tk|ml|ga|cf)[^\s]*)/gi;
            
            if (msg.text && linkRegex.test(msg.text)) {
                // Hapus pesan yang mengandung link
                await this.bot.deleteMessage(chatId, msg.message_id);
                
                // Kirim peringatan
                const warningMsg = await this.bot.sendMessage(chatId, `❌ **Link terdeteksi!**\n\n@${msg.from.username || msg.from.first_name}, pesan Anda dihapus karena mengandung link.\n\n**Antilink aktif di group ini.**`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });

                // Hapus pesan peringatan setelah 10 detik
                setTimeout(() => {
                    this.bot.deleteMessage(chatId, warningMsg.message_id).catch(() => {});
                }, 10000);

                logger.info(`Link deleted from ${msg.from.first_name} in ${msg.chat.title}`);
            }

        } catch (error) {
            logger.error('Error in checkAntilink:', error);
        }
    }

    // Mode Testing
    async testingMode(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const action = match[1]?.toLowerCase();

            // Cek apakah user adalah owner
            const isOwner = config.OWNER_IDS.includes(userId);
            if (!isOwner) {
                return this.bot.sendMessage(chatId, '❌ Hanya owner yang bisa menggunakan command ini!', {
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            if (!action || !['on', 'off', 'status'].includes(action)) {
                return this.bot.sendMessage(chatId, '❌ Gunakan: /testing on/off/status', {
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const testingData = Helpers.loadJSON(config.PATHS.TESTING_MODE);
            
            if (action === 'on') {
                testingData.enabled = true;
                testingData.enabledBy = userId;
                testingData.enabledAt = new Date().toISOString();
                
                await this.bot.sendMessage(chatId, '🧪 **MODE TESTING DIAKTIFKAN!**\n\n⚠️ **DISCLAIMER:**\n• Ini adalah mode testing/demo\n• Semua fitur dapat diakses untuk testing\n• Data yang dibuat bersifat dummy\n• Tidak untuk penggunaan production\n\n**Cara menonaktifkan:** /testing off', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            } else if (action === 'off') {
                testingData.enabled = false;
                testingData.disabledBy = userId;
                testingData.disabledAt = new Date().toISOString();
                
                await this.bot.sendMessage(chatId, '✅ **Mode testing dinonaktifkan!**\n\nBot kembali ke mode normal.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            } else if (action === 'status') {
                const status = testingData.enabled ? 'AKTIF' : 'NONAKTIF';
                const statusIcon = testingData.enabled ? '🧪' : '✅';
                
                await this.bot.sendMessage(chatId, `${statusIcon} **Status Mode Testing:** ${status}\n\n${testingData.enabled ? '⚠️ Bot sedang dalam mode testing' : '✅ Bot dalam mode normal'}`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            Helpers.saveJSON(config.PATHS.TESTING_MODE, testingData);
            logger.info(`Testing mode ${action} by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in testing mode command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat mengatur mode testing!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    // Bug Report
    async bugReport(msg) {
        try {
            const chatId = msg.chat.id;
            
            const reportMessage = `🐛 **Menemukan Bug atau Ada Saran?**\n\n**Silahkan laporkan ke developer:**\n\n👨‍💻 **Developer:** @ibradecode\n📱 **Username:** noxira14\n\n**Jenis laporan yang bisa dikirim:**\n• 🐛 Bug report\n• 💡 Saran fitur baru\n• 🔧 Revisi fitur existing\n• ❓ Pertanyaan teknis\n• 📝 Feedback umum\n\n**Terima kasih atas kontribusi Anda!**`;

            await this.bot.sendMessage(chatId, reportMessage, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [{ text: '👨‍💻 Contact Developer', url: 'https://t.me/ibradecode' }],
                        [{ text: '📋 Help', callback_data: 'help' }]
                    ]
                }
            });

            logger.info(`Bug report info sent to ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in bug report command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    // Privacy Policy
    async privacyPolicy(msg) {
        try {
            const chatId = msg.chat.id;
            
            const privacyMessage = `🔒 **PRIVACY POLICY - XMSBRA BOT**\n\n**1. Informasi yang Dikumpulkan:**\n• User ID Telegram\n• Username (jika ada)\n• Nama pengguna\n• Riwayat command yang digunakan\n• Data panel yang dibuat\n\n**2. Penggunaan Data:**\n• Menyediakan layanan bot\n• Monitoring keamanan\n• Analisis penggunaan\n• Perbaikan fitur\n\n**3. Penyimpanan Data:**\n• Data disimpan secara lokal\n• Tidak dibagikan ke pihak ketiga\n• Backup otomatis untuk keamanan\n\n**4. Keamanan:**\n• Enkripsi data sensitif\n• Akses terbatas untuk admin\n• Monitoring aktivitas mencurigakan\n\n**5. Hak Pengguna:**\n• Menghapus data pribadi\n• Mengakses data yang tersimpan\n• Melaporkan masalah privasi\n\n**6. Kontak:**\n• Developer: @ibradecode\n• Username: noxira14\n\n**Terakhir diperbarui: September 2025**`;

            await this.bot.sendMessage(chatId, privacyMessage, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });

            logger.info(`Privacy policy sent to ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in privacy policy command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    // About
    async about(msg) {
        try {
            const chatId = msg.chat.id;
            
            const aboutMessage = `ℹ️ **TENTANG XMSBRA BOT v2.3**\n\n**🤖 Deskripsi:**\nXMSBRA Bot adalah bot Telegram canggih untuk manajemen cPanel dengan fitur enterprise-level yang lengkap.\n\n**✨ Fitur Utama:**\n• 🔐 Sistem autentikasi berlapis\n• 👥 Kontrol akses grup\n• 🛡️ Monitoring keamanan real-time\n• 🧹 Pembersihan otomatis\n• 📊 Analytics mendalam\n• 🏷️ Hidetag untuk grup\n• 🚫 Anti-link protection\n• 🧪 Mode testing\n\n**👨‍💻 Developer:**\n• Telegram: @ibradecode\n• Username: noxira14\n• GitHub: noxira14\n\n**📅 Version Info:**\n• Version: 2.3.0\n• Release: September 2025\n• Architecture: Modular Enterprise\n\n**🎯 Tujuan:**\nMemberikan solusi manajemen cPanel yang aman, efisien, dan user-friendly melalui Telegram.\n\n**📞 Support:**\nUntuk bantuan, bug report, atau saran fitur, silahkan hubungi developer.`;

            await this.bot.sendMessage(chatId, aboutMessage, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [{ text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }],
                        [{ text: '📋 Commands', callback_data: 'commands' }]
                    ]
                }
            });

            logger.info(`About info sent to ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in about command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    // Commands List
    async commandsList(msg) {
        try {
            const chatId = msg.chat.id;
            
            const commandsMessage = `📋 **DAFTAR COMMANDS - XMSBRA BOT**\n\n**🔓 Public Commands:**\n/start - Memulai bot dan menampilkan welcome\n/help - Menampilkan menu bantuan\n/auth <password> - Autentikasi dengan bot\n/about - Informasi tentang bot\n/privacy - Kebijakan privasi\n/commands - Daftar semua commands\n/bugreport - Laporkan bug atau saran\n\n**💎 Premium Commands:**\n/buatpanel <email> <username> <name> - Membuat panel baru\n/listpanel - Melihat daftar panel Anda\n/delpanel <username> - Menghapus panel\n/panel - Menu manajemen panel\n/status - Status bot dan server\n\n**👑 Admin Commands:**\n/addsrv <name> <domain> <apikey> - Tambah server\n/listsrv - Daftar semua server\n/delsrv <name> - Hapus server\n/addprem <user_id> - Tambah user premium\n/delprem <user_id> - Hapus user premium\n/addgc <group_id> <type> - Tambah akses grup\n/delgc <group_id> - Hapus akses grup\n/listgc - Daftar akses grup\n/joingc <invite_link> - Join grup\n/ban <user_id> - Ban user\n/unban <user_id> - Unban user\n\n**🏷️ Group Commands:**\n/hidetag <pesan> - Tag semua member grup\n/antilink <on/off> - Aktifkan/nonaktifkan anti-link\n\n**🔱 Owner Commands:**\n/addowner <user_id> - Tambah admin/owner\n/delowner <user_id> - Hapus admin/owner\n/maintenance <on/off/status> - Mode maintenance\n/security_panel <start/stop/status/alerts> - Panel keamanan\n/cleanup <start/stop/run/status/history> - Sistem cleanup\n/testing <on/off/status> - Mode testing\n/stats - Statistik bot lengkap\n/clearserver - Hapus semua server`;

            await this.bot.sendMessage(chatId, commandsMessage, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });

            logger.info(`Commands list sent to ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in commands list:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }
}

module.exports = GroupCommands;
