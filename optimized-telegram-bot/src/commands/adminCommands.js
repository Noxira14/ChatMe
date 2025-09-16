const config = require('../config/config');
const PanelService = require('../services/panelService');
const Helpers = require('../utils/helpers');
const logger = require('../utils/logger');

class AdminCommands {
    constructor(bot) {
        this.bot = bot;
        this.panelService = new PanelService();
    }

    async addsrv(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const serverName = match[1];
            const domain = match[2];
            const apiKey = match[3];

            // Check if user is admin or owner
            if (!this.checkAdminAccess(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya admin yang dapat menambahkan server.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            // Validate input
            if (!domain.startsWith('http')) {
                return this.bot.sendMessage(chatId, '❌ **Format Domain Salah**\n\nDomain harus dimulai dengan http:// atau https://\n\n**Contoh:** https://panel.domain.com', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const result = await this.panelService.addServer(serverName, domain, apiKey, userId);
            
            if (result.success) {
                await this.bot.sendMessage(chatId, `✅ **Server Berhasil Ditambahkan**\n\n**📝 Nama:** ${serverName}\n**🌐 Domain:** ${domain}\n**🔑 API Key:** ${apiKey.substring(0, 10)}...\n**👤 Ditambahkan oleh:** ${msg.from.first_name}\n**📅 Tanggal:** ${new Date().toLocaleDateString('id-ID')}`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [
                            [
                                { text: '📋 List Server', callback_data: 'listsrv' },
                                { text: '🔍 Server Info', callback_data: `srvinfo_${serverName}` }
                            ],
                            [
                                { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                            ]
                        ]
                    }
                });
            } else {
                await this.bot.sendMessage(chatId, `❌ **Gagal Menambahkan Server**\n\n**Error:** ${result.error}`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            logger.info(`Server ${serverName} added by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in addsrv command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat menambahkan server!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async listsrv(msg) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;

            // Check if user is admin or owner
            if (!this.checkAdminAccess(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya admin yang dapat melihat daftar server.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const servers = await this.panelService.getServers();
            
            if (servers.length === 0) {
                return this.bot.sendMessage(chatId, '📋 **Daftar Server**\n\nBelum ada server yang terdaftar.\n\n**Cara menambah server:**\n/addsrv <nama> <domain> <apikey>', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            let serverList = `📋 **DAFTAR SERVER**\n\n`;
            
            servers.forEach((server, index) => {
                serverList += `**${index + 1}. ${server.name}**\n`;
                serverList += `🌐 Domain: ${server.domain}\n`;
                serverList += `🔑 API Key: ${server.apiKey.substring(0, 10)}...\n`;
                serverList += `📅 Ditambahkan: ${new Date(server.createdAt).toLocaleDateString('id-ID')}\n`;
                serverList += `👤 Oleh: ${server.addedBy}\n\n`;
            });

            serverList += `**Total Server:** ${servers.length}`;

            await this.bot.sendMessage(chatId, serverList, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '➕ Tambah Server', callback_data: 'addsrv' },
                            { text: '🗑️ Hapus Server', callback_data: 'delsrv' }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            logger.info(`Server list viewed by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in listsrv command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat mengambil daftar server!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async delsrv(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const serverName = match[1];

            // Check if user is admin or owner
            if (!this.checkAdminAccess(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya admin yang dapat menghapus server.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const result = await this.panelService.deleteServer(serverName, userId);
            
            if (result.success) {
                await this.bot.sendMessage(chatId, `✅ **Server Berhasil Dihapus**\n\n**Nama Server:** ${serverName}\n**Dihapus oleh:** ${msg.from.first_name}\n**Tanggal:** ${new Date().toLocaleDateString('id-ID')}`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [
                            [
                                { text: '📋 List Server', callback_data: 'listsrv' },
                                { text: '➕ Tambah Server', callback_data: 'addsrv' }
                            ],
                            [
                                { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                            ]
                        ]
                    }
                });
            } else {
                await this.bot.sendMessage(chatId, `❌ **Gagal Menghapus Server**\n\n**Error:** ${result.error}`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            logger.info(`Server ${serverName} deleted by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in delsrv command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat menghapus server!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async srvinfo(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const serverName = match[1];

            // Check if user is admin or owner
            if (!this.checkAdminAccess(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya admin yang dapat melihat info server.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const serverInfo = await this.panelService.getServerInfo(serverName);
            
            if (!serverInfo) {
                return this.bot.sendMessage(chatId, `❌ **Server Tidak Ditemukan**\n\nServer dengan nama "${serverName}" tidak ditemukan.`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const infoMessage = `🔍 **INFORMASI SERVER**\n\n**📝 Nama:** ${serverInfo.name}\n**🌐 Domain:** ${serverInfo.domain}\n**🔑 API Key:** ${serverInfo.apiKey.substring(0, 15)}...\n**📅 Ditambahkan:** ${new Date(serverInfo.createdAt).toLocaleDateString('id-ID')}\n**👤 Oleh:** ${serverInfo.addedBy}\n**📊 Status:** ${serverInfo.status || 'Active'}\n**💾 Total Panel:** ${serverInfo.totalPanels || 0}`;

            await this.bot.sendMessage(chatId, infoMessage, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '🌐 Test Connection', callback_data: `test_${serverName}` },
                            { text: '📊 Statistics', callback_data: `stats_${serverName}` }
                        ],
                        [
                            { text: '📋 List Server', callback_data: 'listsrv' },
                            { text: '🗑️ Delete', callback_data: `delete_${serverName}` }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            logger.info(`Server info ${serverName} viewed by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in srvinfo command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat mengambil info server!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async addprem(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const targetUserId = parseInt(match[1]);

            // Check if user is admin or owner
            if (!this.checkAdminAccess(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya admin yang dapat menambah user premium.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            if (isNaN(targetUserId)) {
                return this.bot.sendMessage(chatId, '❌ **Format User ID Salah**\n\nGunakan format angka untuk User ID.\n\n**Contoh:** /addprem 123456789', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const premiumData = Helpers.loadJSON(config.PATHS.PREMIUM_DATA);
            
            if (premiumData.includes(targetUserId)) {
                return this.bot.sendMessage(chatId, `❌ **User Sudah Premium**\n\nUser ID ${targetUserId} sudah memiliki akses premium.`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            premiumData.push(targetUserId);
            Helpers.saveJSON(config.PATHS.PREMIUM_DATA, premiumData);

            await this.bot.sendMessage(chatId, `✅ **User Premium Ditambahkan**\n\n**User ID:** ${targetUserId}\n**Ditambahkan oleh:** ${msg.from.first_name}\n**Tanggal:** ${new Date().toLocaleDateString('id-ID')}\n\nUser sekarang memiliki akses premium.`, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '📋 List Premium', callback_data: 'listprem' },
                            { text: '🗑️ Remove Premium', callback_data: 'delprem' }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            // Notify the user if possible
            try {
                await this.bot.sendMessage(targetUserId, `🎉 **Selamat! Anda Mendapat Akses Premium**\n\nAnda sekarang memiliki akses premium ke XMSBRA Bot.\n\n**Fitur yang tersedia:**\n• Membuat panel cPanel\n• Manajemen panel\n• Akses ke semua fitur premium\n\n**Mulai sekarang:** /panel`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [
                            [
                                { text: '🎛️ Panel Menu', callback_data: 'panel' },
                                { text: '📋 Help', callback_data: 'help' }
                            ],
                            [
                                { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                            ]
                        ]
                    }
                });
            } catch (error) {
                // User might have blocked the bot or doesn't exist
                logger.warn(`Could not notify user ${targetUserId} about premium access`);
            }

            logger.info(`Premium access added for user ${targetUserId} by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in addprem command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat menambah user premium!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async delprem(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const targetUserId = parseInt(match[1]);

            // Check if user is admin or owner
            if (!this.checkAdminAccess(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya admin yang dapat menghapus user premium.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            if (isNaN(targetUserId)) {
                return this.bot.sendMessage(chatId, '❌ **Format User ID Salah**\n\nGunakan format angka untuk User ID.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const premiumData = Helpers.loadJSON(config.PATHS.PREMIUM_DATA);
            const index = premiumData.indexOf(targetUserId);
            
            if (index === -1) {
                return this.bot.sendMessage(chatId, `❌ **User Bukan Premium**\n\nUser ID ${targetUserId} tidak memiliki akses premium.`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            premiumData.splice(index, 1);
            Helpers.saveJSON(config.PATHS.PREMIUM_DATA, premiumData);

            await this.bot.sendMessage(chatId, `✅ **User Premium Dihapus**\n\n**User ID:** ${targetUserId}\n**Dihapus oleh:** ${msg.from.first_name}\n**Tanggal:** ${new Date().toLocaleDateString('id-ID')}\n\nUser tidak lagi memiliki akses premium.`, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '📋 List Premium', callback_data: 'listprem' },
                            { text: '➕ Add Premium', callback_data: 'addprem' }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            logger.info(`Premium access removed for user ${targetUserId} by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in delprem command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat menghapus user premium!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async listadmin(msg) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;

            // Check if user is admin or owner
            if (!this.checkAdminAccess(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya admin yang dapat melihat daftar admin.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const adminData = Helpers.loadJSON(config.PATHS.ADMIN_DATA);
            const premiumData = Helpers.loadJSON(config.PATHS.PREMIUM_DATA);

            let adminList = `👑 **DAFTAR ADMIN & PREMIUM**\n\n`;

            // Owner list
            adminList += `**🔱 Owner:**\n`;
            config.OWNER_IDS.forEach((ownerId, index) => {
                adminList += `${index + 1}. ${ownerId}\n`;
            });
            adminList += `\n`;

            // Admin list
            adminList += `**👑 Admin:**\n`;
            if (adminData.length === 0) {
                adminList += `Belum ada admin.\n`;
            } else {
                adminData.forEach((adminId, index) => {
                    adminList += `${index + 1}. ${adminId}\n`;
                });
            }
            adminList += `\n`;

            // Premium list
            adminList += `**💎 Premium Users:**\n`;
            if (premiumData.length === 0) {
                adminList += `Belum ada user premium.\n`;
            } else {
                premiumData.forEach((premId, index) => {
                    adminList += `${index + 1}. ${premId}\n`;
                });
            }

            adminList += `\n**Total:**\n• Owner: ${config.OWNER_IDS.length}\n• Admin: ${adminData.length}\n• Premium: ${premiumData.length}`;

            await this.bot.sendMessage(chatId, adminList, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '➕ Add Premium', callback_data: 'addprem' },
                            { text: '🗑️ Del Premium', callback_data: 'delprem' }
                        ],
                        [
                            { text: '👑 Add Admin', callback_data: 'addowner' },
                            { text: '🗑️ Del Admin', callback_data: 'delowner' }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            logger.info(`Admin list viewed by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in listadmin command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat mengambil daftar admin!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async addowner(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const targetUserId = parseInt(match[1]);

            // Check if user is owner
            if (!config.OWNER_IDS.includes(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya owner yang dapat menambah admin.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            if (isNaN(targetUserId)) {
                return this.bot.sendMessage(chatId, '❌ **Format User ID Salah**\n\nGunakan format angka untuk User ID.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const adminData = Helpers.loadJSON(config.PATHS.ADMIN_DATA);
            
            if (adminData.includes(targetUserId)) {
                return this.bot.sendMessage(chatId, `❌ **User Sudah Admin**\n\nUser ID ${targetUserId} sudah memiliki akses admin.`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            adminData.push(targetUserId);
            Helpers.saveJSON(config.PATHS.ADMIN_DATA, adminData);

            await this.bot.sendMessage(chatId, `✅ **Admin Berhasil Ditambahkan**\n\n**User ID:** ${targetUserId}\n**Ditambahkan oleh:** ${msg.from.first_name}\n**Tanggal:** ${new Date().toLocaleDateString('id-ID')}\n\nUser sekarang memiliki akses admin.`, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '📋 List Admin', callback_data: 'listadmin' },
                            { text: '🗑️ Remove Admin', callback_data: 'delowner' }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            logger.info(`Admin access added for user ${targetUserId} by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in addowner command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat menambah admin!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async delowner(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const targetUserId = parseInt(match[1]);

            // Check if user is owner
            if (!config.OWNER_IDS.includes(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya owner yang dapat menghapus admin.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            if (isNaN(targetUserId)) {
                return this.bot.sendMessage(chatId, '❌ **Format User ID Salah**\n\nGunakan format angka untuk User ID.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const adminData = Helpers.loadJSON(config.PATHS.ADMIN_DATA);
            const index = adminData.indexOf(targetUserId);
            
            if (index === -1) {
                return this.bot.sendMessage(chatId, `❌ **User Bukan Admin**\n\nUser ID ${targetUserId} tidak memiliki akses admin.`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            adminData.splice(index, 1);
            Helpers.saveJSON(config.PATHS.ADMIN_DATA, adminData);

            await this.bot.sendMessage(chatId, `✅ **Admin Berhasil Dihapus**\n\n**User ID:** ${targetUserId}\n**Dihapus oleh:** ${msg.from.first_name}\n**Tanggal:** ${new Date().toLocaleDateString('id-ID')}\n\nUser tidak lagi memiliki akses admin.`, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '📋 List Admin', callback_data: 'listadmin' },
                            { text: '➕ Add Admin', callback_data: 'addowner' }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            logger.info(`Admin access removed for user ${targetUserId} by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in delowner command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat menghapus admin!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async clearserver(msg) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;

            // Check if user is owner
            if (!config.OWNER_IDS.includes(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya owner yang dapat menghapus semua server.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            // Confirmation message
            await this.bot.sendMessage(chatId, `⚠️ **PERINGATAN!**\n\nAnda akan menghapus SEMUA server yang terdaftar.\n\n**Tindakan ini tidak dapat dibatalkan!**\n\nApakah Anda yakin?`, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '✅ Ya, Hapus Semua', callback_data: 'confirm_clearserver' },
                            { text: '❌ Batal', callback_data: 'cancel_clearserver' }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            logger.info(`Clear server confirmation requested by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in clearserver command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat memproses clear server!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async stats(msg) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;

            // Check if user is owner
            if (!config.OWNER_IDS.includes(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya owner yang dapat melihat statistik lengkap.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const memUsage = process.memoryUsage();
            const adminData = Helpers.loadJSON(config.PATHS.ADMIN_DATA);
            const premiumData = Helpers.loadJSON(config.PATHS.PREMIUM_DATA);
            const servers = await this.panelService.getServers();
            const totalPanels = await this.panelService.getTotalPanels();

            const statsMessage = `📊 **STATISTIK LENGKAP XMSBRA BOT**\n\n**🤖 Bot Info:**\n• Version: 2.3.0\n• Uptime: ${Helpers.getRuntime()}\n• Memory: ${Helpers.formatBytes(memUsage.heapUsed)}\n\n**👥 Users:**\n• Owner: ${config.OWNER_IDS.length}\n• Admin: ${adminData.length}\n• Premium: ${premiumData.length}\n• Total: ${config.OWNER_IDS.length + adminData.length + premiumData.length}\n\n**🌐 Servers:**\n• Total Server: ${servers.length}\n• Total Panel: ${totalPanels}\n• Active: ${servers.filter(s => s.status === 'active').length}\n\n**📊 System:**\n• CPU Usage: ${process.cpuUsage().user / 1000000}ms\n• Memory Total: ${Helpers.formatBytes(memUsage.heapTotal)}\n• Platform: ${process.platform}\n• Node Version: ${process.version}\n\n**📅 Report:**\n• Tanggal: ${new Date().toLocaleDateString('id-ID')}\n• Waktu: ${new Date().toLocaleTimeString('id-ID')}\n• Timezone: Asia/Makassar`;

            await this.bot.sendMessage(chatId, statsMessage, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '🔄 Refresh', callback_data: 'stats' },
                            { text: '📊 Detailed', callback_data: 'detailed_stats' }
                        ],
                        [
                            { text: '📋 Export', callback_data: 'export_stats' },
                            { text: '📈 Analytics', callback_data: 'analytics' }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            logger.info(`Full stats viewed by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in stats command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat mengambil statistik!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    // Helper method to check admin access
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

module.exports = AdminCommands;
