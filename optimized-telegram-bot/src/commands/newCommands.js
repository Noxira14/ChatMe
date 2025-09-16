const config = require('../config/config');
const Helpers = require('../utils/helpers');
const logger = require('../utils/logger');
const figlet = require('figlet');
const chalk = require('chalk');

class NewCommands {
    constructor(bot) {
        this.bot = bot;
    }

    async authenticate(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const firstName = msg.from.first_name;
            const password = match[1];

            // Check if password is correct
            if (password !== config.AUTH_PASSWORD) {
                // Log failed attempt
                logger.warn(`Failed authentication attempt by ${firstName} (${userId}) with password: ${password}`);
                
                return this.bot.sendMessage(chatId, `❌ **Password Salah!**\n\nPassword yang Anda masukkan tidak valid.\n\n**Coba lagi:**\n/auth <password>\n\n**Hubungi admin jika lupa password.**`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            // Check if user is already authenticated
            const authData = Helpers.loadJSON(config.PATHS.AUTH_DATA);
            if (authData[userId]) {
                return this.bot.sendMessage(chatId, `✅ **Sudah Terautentikasi**\n\nAnda sudah berhasil login sebelumnya.\n\n**Akses Anda:**\n• Status: Aktif ✅\n• Login: ${new Date(authData[userId].loginTime).toLocaleString('id-ID')}\n\n**Mulai menggunakan bot:** /start`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [
                            [
                                { text: '🚀 Start Bot', callback_data: 'start' },
                                { text: '📋 Help', callback_data: 'help' }
                            ],
                            [
                                { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                            ]
                        ]
                    }
                });
            }

            // Generate figlet banner
            const banner = figlet.textSync('XMSBRA', {
                font: 'Small',
                horizontalLayout: 'default',
                verticalLayout: 'default'
            });

            // Save authentication data
            authData[userId] = {
                firstName: firstName,
                username: msg.from.username || 'N/A',
                loginTime: new Date().toISOString(),
                lastActivity: new Date().toISOString(),
                authenticated: true
            };
            Helpers.saveJSON(config.PATHS.AUTH_DATA, authData);

            // Log successful authentication with colors
            console.log(chalk.green.bold('\n' + '='.repeat(50)));
            console.log(chalk.cyan(banner));
            console.log(chalk.green.bold('='.repeat(50)));
            console.log(chalk.yellow(`🎉 NEW USER AUTHENTICATED!`));
            console.log(chalk.white(`👤 Name: ${firstName}`));
            console.log(chalk.white(`🆔 ID: ${userId}`));
            console.log(chalk.white(`📱 Username: @${msg.from.username || 'N/A'}`));
            console.log(chalk.white(`🕐 Time: ${new Date().toLocaleString('id-ID')}`));
            console.log(chalk.green.bold('='.repeat(50) + '\n'));

            logger.info(`User authenticated: ${firstName} (${userId})`);

            // Send success message
            await this.bot.sendMessage(chatId, `🎉 **AUTENTIKASI BERHASIL!**\n\n**Selamat datang, ${firstName}!**\n\nAnda telah berhasil login ke XMSBRA Bot v2.3\n\n**✨ Akses Anda:**\n• Status: Aktif ✅\n• Login: ${new Date().toLocaleString('id-ID')}\n• Timezone: Asia/Makassar\n\n**🚀 Langkah Selanjutnya:**\n• Ketik /start untuk memulai\n• Ketik /help untuk bantuan\n• Ketik /panel untuk manajemen cPanel\n\n**Selamat menggunakan bot!** 🎊`, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '🚀 Start Bot', callback_data: 'start' },
                            { text: '🎛️ Panel', callback_data: 'panel' }
                        ],
                        [
                            { text: '📋 Help', callback_data: 'help' },
                            { text: 'ℹ️ About', callback_data: 'about' }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

        } catch (error) {
            logger.error('Error in authenticate command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat autentikasi!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async addGroupAccess(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const groupId = match[1];
            const accessType = match[2].toLowerCase();

            // Check if user is admin or owner
            if (!this.checkAdminAccess(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya admin yang dapat menambah akses grup.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            // Validate access type
            const validTypes = ['basic', 'premium', 'admin'];
            if (!validTypes.includes(accessType)) {
                return this.bot.sendMessage(chatId, `❌ **Tipe Akses Tidak Valid**\n\nTipe akses yang valid: ${validTypes.join(', ')}\n\n**Contoh:** /addgc -1001234567890 premium`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const groupAccessData = Helpers.loadJSON(config.PATHS.GROUP_ACCESS);
            
            groupAccessData[groupId] = {
                accessType: accessType,
                addedBy: userId,
                addedAt: new Date().toISOString(),
                members: [],
                settings: {
                    allowPanelCreation: accessType !== 'basic',
                    allowServerManagement: accessType === 'admin',
                    maxPanels: accessType === 'basic' ? 1 : accessType === 'premium' ? 10 : -1
                }
            };

            Helpers.saveJSON(config.PATHS.GROUP_ACCESS, groupAccessData);

            await this.bot.sendMessage(chatId, `✅ **Akses Grup Berhasil Ditambahkan**\n\n**Group ID:** ${groupId}\n**Tipe Akses:** ${accessType.toUpperCase()}\n**Ditambahkan oleh:** ${msg.from.first_name}\n**Tanggal:** ${new Date().toLocaleDateString('id-ID')}\n\n**Fitur yang tersedia:**\n${this.getAccessFeatures(accessType)}`, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '📋 List Grup', callback_data: 'listgc' },
                            { text: '⚙️ Settings', callback_data: `gc_settings_${groupId}` }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            logger.info(`Group access added: ${groupId} (${accessType}) by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in addGroupAccess command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat menambah akses grup!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async deleteGroupAccess(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const groupId = match[1];

            // Check if user is admin or owner
            if (!this.checkAdminAccess(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya admin yang dapat menghapus akses grup.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const groupAccessData = Helpers.loadJSON(config.PATHS.GROUP_ACCESS);
            
            if (!groupAccessData[groupId]) {
                return this.bot.sendMessage(chatId, `❌ **Grup Tidak Ditemukan**\n\nGrup dengan ID ${groupId} tidak memiliki akses.`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const groupData = groupAccessData[groupId];
            delete groupAccessData[groupId];
            Helpers.saveJSON(config.PATHS.GROUP_ACCESS, groupAccessData);

            await this.bot.sendMessage(chatId, `✅ **Akses Grup Berhasil Dihapus**\n\n**Group ID:** ${groupId}\n**Tipe Akses:** ${groupData.accessType.toUpperCase()}\n**Dihapus oleh:** ${msg.from.first_name}\n**Tanggal:** ${new Date().toLocaleDateString('id-ID')}`, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '📋 List Grup', callback_data: 'listgc' },
                            { text: '➕ Add Grup', callback_data: 'addgc' }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            logger.info(`Group access deleted: ${groupId} by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in deleteGroupAccess command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat menghapus akses grup!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async listGroupAccess(msg) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;

            // Check if user is admin or owner
            if (!this.checkAdminAccess(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya admin yang dapat melihat daftar akses grup.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const groupAccessData = Helpers.loadJSON(config.PATHS.GROUP_ACCESS);
            const groups = Object.keys(groupAccessData);

            if (groups.length === 0) {
                return this.bot.sendMessage(chatId, '📋 **Daftar Akses Grup**\n\nBelum ada grup yang memiliki akses.\n\n**Cara menambah akses:**\n/addgc <group_id> <tipe>', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            let groupList = `📋 **DAFTAR AKSES GRUP**\n\n`;
            
            groups.forEach((groupId, index) => {
                const groupData = groupAccessData[groupId];
                groupList += `**${index + 1}. Group ${groupId}**\n`;
                groupList += `🏷️ Tipe: ${groupData.accessType.toUpperCase()}\n`;
                groupList += `👥 Member: ${groupData.members ? groupData.members.length : 0}\n`;
                groupList += `📅 Ditambahkan: ${new Date(groupData.addedAt).toLocaleDateString('id-ID')}\n\n`;
            });

            groupList += `**Total Grup:** ${groups.length}`;

            await this.bot.sendMessage(chatId, groupList, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '➕ Add Grup', callback_data: 'addgc' },
                            { text: '🗑️ Delete Grup', callback_data: 'delgc' }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            logger.info(`Group access list viewed by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in listGroupAccess command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat mengambil daftar akses grup!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async joinGroup(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const groupId = match[1];

            const groupAccessData = Helpers.loadJSON(config.PATHS.GROUP_ACCESS);
            
            if (!groupAccessData[groupId]) {
                return this.bot.sendMessage(chatId, `❌ **Grup Tidak Ditemukan**\n\nGrup dengan ID ${groupId} tidak memiliki akses atau tidak terdaftar.`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const groupData = groupAccessData[groupId];
            
            if (!groupData.members) {
                groupData.members = [];
            }

            if (groupData.members.includes(userId)) {
                return this.bot.sendMessage(chatId, `✅ **Sudah Bergabung**\n\nAnda sudah menjadi member grup ini.\n\n**Akses Anda:** ${groupData.accessType.toUpperCase()}`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            groupData.members.push(userId);
            groupAccessData[groupId] = groupData;
            Helpers.saveJSON(config.PATHS.GROUP_ACCESS, groupAccessData);

            await this.bot.sendMessage(chatId, `✅ **Berhasil Bergabung**\n\n**Group ID:** ${groupId}\n**Akses Anda:** ${groupData.accessType.toUpperCase()}\n**Bergabung:** ${new Date().toLocaleDateString('id-ID')}\n\n**Fitur yang tersedia:**\n${this.getAccessFeatures(groupData.accessType)}`, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '🎛️ Panel', callback_data: 'panel' },
                            { text: '📋 Help', callback_data: 'help' }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            logger.info(`User ${msg.from.first_name} joined group ${groupId}`);

        } catch (error) {
            logger.error('Error in joinGroup command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat bergabung ke grup!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async banUser(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const targetUserId = parseInt(match[1]);

            // Check if user is admin or owner
            if (!this.checkAdminAccess(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya admin yang dapat mem-ban user.', {
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

            // Check if target is owner or admin
            if (config.OWNER_IDS.includes(targetUserId)) {
                return this.bot.sendMessage(chatId, '❌ **Tidak Dapat Mem-ban Owner**\n\nOwner tidak dapat di-ban.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const bannedData = Helpers.loadJSON(config.PATHS.BANNED_USERS);
            
            if (bannedData.includes(targetUserId)) {
                return this.bot.sendMessage(chatId, `❌ **User Sudah Di-ban**\n\nUser ID ${targetUserId} sudah dalam daftar banned.`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            bannedData.push(targetUserId);
            Helpers.saveJSON(config.PATHS.BANNED_USERS, bannedData);

            // Remove from auth data
            const authData = Helpers.loadJSON(config.PATHS.AUTH_DATA);
            if (authData[targetUserId]) {
                delete authData[targetUserId];
                Helpers.saveJSON(config.PATHS.AUTH_DATA, authData);
            }

            await this.bot.sendMessage(chatId, `✅ **User Berhasil Di-ban**\n\n**User ID:** ${targetUserId}\n**Di-ban oleh:** ${msg.from.first_name}\n**Tanggal:** ${new Date().toLocaleDateString('id-ID')}\n\nUser tidak dapat menggunakan bot.`, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '📋 List Banned', callback_data: 'listbanned' },
                            { text: '🔓 Unban', callback_data: 'unban' }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            logger.info(`User ${targetUserId} banned by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in banUser command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat mem-ban user!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async unbanUser(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const targetUserId = parseInt(match[1]);

            // Check if user is admin or owner
            if (!this.checkAdminAccess(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya admin yang dapat meng-unban user.', {
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

            const bannedData = Helpers.loadJSON(config.PATHS.BANNED_USERS);
            const index = bannedData.indexOf(targetUserId);
            
            if (index === -1) {
                return this.bot.sendMessage(chatId, `❌ **User Tidak Di-ban**\n\nUser ID ${targetUserId} tidak dalam daftar banned.`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            bannedData.splice(index, 1);
            Helpers.saveJSON(config.PATHS.BANNED_USERS, bannedData);

            await this.bot.sendMessage(chatId, `✅ **User Berhasil Di-unban**\n\n**User ID:** ${targetUserId}\n**Di-unban oleh:** ${msg.from.first_name}\n**Tanggal:** ${new Date().toLocaleDateString('id-ID')}\n\nUser dapat menggunakan bot kembali.`, {
                parse_mode: 'Markdown',
                reply_markup: {
                    inline_keyboard: [
                        [
                            { text: '📋 List Banned', callback_data: 'listbanned' },
                            { text: '🚫 Ban User', callback_data: 'ban' }
                        ],
                        [
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]
                    ]
                }
            });

            logger.info(`User ${targetUserId} unbanned by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in unbanUser command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat meng-unban user!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async maintenanceMode(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const action = match[1].toLowerCase();

            // Check if user is owner
            if (!config.OWNER_IDS.includes(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya owner yang dapat mengatur maintenance mode.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            const maintenanceData = Helpers.loadJSON(config.PATHS.MAINTENANCE_MODE);

            if (action === 'on') {
                maintenanceData.enabled = true;
                maintenanceData.enabledBy = userId;
                maintenanceData.enabledAt = new Date().toISOString();
                maintenanceData.reason = 'Maintenance mode activated by owner';

                Helpers.saveJSON(config.PATHS.MAINTENANCE_MODE, maintenanceData);

                await this.bot.sendMessage(chatId, `🔧 **Maintenance Mode Diaktifkan**\n\n**Status:** Aktif ✅\n**Diaktifkan oleh:** ${msg.from.first_name}\n**Waktu:** ${new Date().toLocaleString('id-ID')}\n\n**Efek:**\n• Bot tidak dapat digunakan user biasa\n• Hanya owner yang dapat mengakses\n• Semua command dibatasi\n\n**Menonaktifkan:** /maintenance off`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [
                            [
                                { text: '🔧 Turn Off', callback_data: 'maintenance_off' },
                                { text: '📊 Status', callback_data: 'maintenance_status' }
                            ],
                            [
                                { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                            ]
                        ]
                    }
                });

            } else if (action === 'off') {
                maintenanceData.enabled = false;
                maintenanceData.disabledBy = userId;
                maintenanceData.disabledAt = new Date().toISOString();

                Helpers.saveJSON(config.PATHS.MAINTENANCE_MODE, maintenanceData);

                await this.bot.sendMessage(chatId, `✅ **Maintenance Mode Dinonaktifkan**\n\n**Status:** Nonaktif ❌\n**Dinonaktifkan oleh:** ${msg.from.first_name}\n**Waktu:** ${new Date().toLocaleString('id-ID')}\n\nBot sekarang dapat digunakan normal.`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [
                            [
                                { text: '🔧 Turn On', callback_data: 'maintenance_on' },
                                { text: '📊 Status', callback_data: 'maintenance_status' }
                            ],
                            [
                                { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                            ]
                        ]
                    }
                });

            } else if (action === 'status') {
                const status = maintenanceData.enabled ? 'Aktif ✅' : 'Nonaktif ❌';
                const lastAction = maintenanceData.enabled ? 
                    `Diaktifkan: ${new Date(maintenanceData.enabledAt).toLocaleString('id-ID')}` :
                    `Dinonaktifkan: ${new Date(maintenanceData.disabledAt || Date.now()).toLocaleString('id-ID')}`;

                await this.bot.sendMessage(chatId, `🔧 **Status Maintenance Mode**\n\n**Status:** ${status}\n**${lastAction}**\n**Reason:** ${maintenanceData.reason || 'N/A'}\n\n**Info:**\n• Mode maintenance membatasi akses user\n• Hanya owner yang dapat menggunakan bot\n• Berguna untuk update atau perbaikan`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [
                            [
                                { text: maintenanceData.enabled ? '❌ Turn Off' : '✅ Turn On', 
                                  callback_data: maintenanceData.enabled ? 'maintenance_off' : 'maintenance_on' }
                            ],
                            [
                                { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                            ]
                        ]
                    }
                });
            }

            logger.info(`Maintenance mode ${action} by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in maintenanceMode command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat mengatur maintenance mode!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async securityPanel(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const action = match[1].toLowerCase();

            // Check if user is owner
            if (!config.OWNER_IDS.includes(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya owner yang dapat mengakses security panel.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            if (action === 'status') {
                const securityService = require('../services/securityService');
                const stats = securityService.getStats();

                await this.bot.sendMessage(chatId, `🛡️ **SECURITY PANEL STATUS**\n\n**Service Status:** ${stats.isActive ? 'Active ✅' : 'Inactive ❌'}\n**Monitoring:** ${stats.monitoring ? 'ON' : 'OFF'}\n**Recent Alerts:** ${stats.recentAlerts}\n**Last Check:** ${stats.lastCheck}\n**Uptime:** ${stats.uptime}\n\n**Security Layers:**\n• Rate Limiting ✅\n• Authentication ✅\n• Group Access Control ✅\n• Ban System ✅\n• Real-time Monitoring ✅`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [
                            [
                                { text: '🔄 Refresh', callback_data: 'security_status' },
                                { text: '📊 Detailed', callback_data: 'security_detailed' }
                            ],
                            [
                                { text: '⚙️ Settings', callback_data: 'security_settings' },
                                { text: '📋 Logs', callback_data: 'security_logs' }
                            ],
                            [
                                { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                            ]
                        ]
                    }
                });
            }

            logger.info(`Security panel ${action} accessed by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in securityPanel command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat mengakses security panel!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    async cleanupSystem(msg, match) {
        try {
            const chatId = msg.chat.id;
            const userId = msg.from.id;
            const action = match[1].toLowerCase();

            // Check if user is owner
            if (!config.OWNER_IDS.includes(userId)) {
                return this.bot.sendMessage(chatId, '❌ **Akses Ditolak**\n\nHanya owner yang dapat mengakses cleanup system.', {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [[
                            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                        ]]
                    }
                });
            }

            if (action === 'status') {
                const cleanupService = require('../services/cleanupService');
                const stats = cleanupService.getStats();

                await this.bot.sendMessage(chatId, `🧹 **CLEANUP SYSTEM STATUS**\n\n**Service Status:** ${stats.isActive ? 'Active ✅' : 'Inactive ❌'}\n**Auto Cleanup:** ${stats.autoCleanup ? 'ON' : 'OFF'}\n**Last Cleanup:** ${stats.lastCleanup}\n**Cleaned Items:** ${stats.cleanedItems}\n**Average Inactive:** ${stats.averageInactive}\n\n**Cleanup Schedule:**\n• Interval: Every 5 hours\n• Target: Inactive users (7+ days)\n• Action: Remove auth data\n• Notification: Owner alert`, {
                    parse_mode: 'Markdown',
                    reply_markup: {
                        inline_keyboard: [
                            [
                                { text: '🧹 Run Now', callback_data: 'cleanup_run' },
                                { text: '📊 Stats', callback_data: 'cleanup_stats' }
                            ],
                            [
                                { text: '⚙️ Settings', callback_data: 'cleanup_settings' },
                                { text: '📋 History', callback_data: 'cleanup_history' }
                            ],
                            [
                                { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                            ]
                        ]
                    }
                });
            }

            logger.info(`Cleanup system ${action} accessed by ${msg.from.first_name}`);

        } catch (error) {
            logger.error('Error in cleanupSystem command:', error);
            this.bot.sendMessage(msg.chat.id, '❌ Terjadi kesalahan saat mengakses cleanup system!', {
                reply_markup: {
                    inline_keyboard: [[
                        { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
                    ]]
                }
            });
        }
    }

    // Helper methods
    checkAdminAccess(userId) {
        // Check if user is owner
        if (config.OWNER_IDS.includes(userId)) {
            return true;
        }

        // Check if user is admin
        const adminData = Helpers.loadJSON(config.PATHS.ADMIN_DATA);
        return adminData.includes(userId);
    }

    getAccessFeatures(accessType) {
        switch (accessType) {
            case 'basic':
                return '• Akses command dasar\n• Maksimal 1 panel\n• Support terbatas';
            case 'premium':
                return '• Semua fitur basic\n• Maksimal 10 panel\n• Priority support\n• Advanced features';
            case 'admin':
                return '• Semua fitur premium\n• Unlimited panel\n• Server management\n• User management\n• Full admin access';
            default:
                return '• Fitur tidak diketahui';
        }
    }
}

module.exports = NewCommands;
