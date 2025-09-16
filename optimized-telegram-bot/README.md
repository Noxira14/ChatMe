# 🤖 XMSBRA Telegram Bot v2.3

[![Version](https://img.shields.io/badge/version-2.3.0-blue.svg)](https://github.com/noxira14/chatme)
[![Node.js](https://img.shields.io/badge/node-%3E%3D16.0.0-green.svg)](https://nodejs.org/)
[![License](https://img.shields.io/badge/license-MIT-yellow.svg)](LICENSE)
[![Telegram](https://img.shields.io/badge/telegram-@ibradecode-blue.svg)](https://t.me/ibradecode)

Enterprise-grade Telegram bot for cPanel management with advanced group features, comprehensive security, and intuitive user experience.

## ✨ What's New in v2.3

### 🎯 Major Features
- **🏷️ Hidetag System** - Tag all group members with custom messages
- **🚫 Antilink Protection** - Auto-delete links in groups with smart detection
- **🧪 Testing Mode** - Safe demo environment for feature testing
- **📋 Enhanced Documentation** - Complete privacy policy and help system
- **🎨 Improved UI** - Consistent formatting and developer support access

### 🔧 Technical Improvements
- **Text Formatting** - Upgraded to double asterisk markdown (`**text**`)
- **Developer Integration** - Support buttons on all responses
- **Security Enhancements** - Advanced access control and monitoring
- **User Experience** - Streamlined navigation and error handling

## 🚀 Quick Start

### Prerequisites
- Node.js >= 16.0.0
- Telegram Bot Token from [@BotFather](https://t.me/BotFather)
- Basic knowledge of Telegram bots

### Installation

```bash
# Clone repository
git clone https://github.com/noxira14/chatme.git
cd chatme/optimized-telegram-bot

# Install dependencies
npm install

# Configure bot
cp src/config/config.example.js src/config/config.js
nano src/config/config.js

# Initialize data files
npm run init-data

# Start bot
npm start
```

### Configuration

```javascript
// src/config/config.js
module.exports = {
    BOT_TOKEN: 'your_bot_token_here',
    OWNER_IDS: [your_telegram_id],
    AUTH_PASSWORD: 'your_secure_password',
    // ... other settings
};
```

## 📋 Features Overview

### 🔐 Security & Authentication
- **Multi-layer Authentication** - Password + role-based access
- **Rate Limiting** - Prevent spam and abuse
- **Ban System** - Block malicious users
- **Real-time Monitoring** - Security event tracking
- **Data Encryption** - Secure storage of sensitive information

### 👥 Group Management
- **Hidetag Feature** - `/hidetag <message>` - Tag all group members
- **Antilink Protection** - `/antilink on/off` - Auto-delete links
- **Admin Controls** - Group-specific permissions and settings
- **Member Management** - Add/remove group access

### 🎛️ cPanel Management
- **Panel Creation** - Automated cPanel account creation
- **Server Management** - Multi-server support with load balancing
- **User Management** - Premium and admin user controls
- **Resource Monitoring** - Real-time server statistics

### 🧪 Testing & Development
- **Testing Mode** - Safe demo environment for all features
- **Debug Logging** - Comprehensive error tracking
- **Performance Monitoring** - Memory and response time tracking
- **Developer Tools** - Built-in debugging and maintenance tools

## 📚 Command Reference

### 🔓 Public Commands
| Command | Description |
|---------|-------------|
| `/start` | Start the bot and show welcome message |
| `/help` | Display comprehensive help menu |
| `/about` | Bot information and technical details |
| `/privacy` | Privacy policy and data handling |
| `/commands` | Complete list of available commands |
| `/bugreport` | Report bugs or request features |
| `/cekid` | Check your Telegram ID |
| `/auth <password>` | Authenticate with the bot |

### 💎 Premium Commands
| Command | Description |
|---------|-------------|
| `/buatpanel <email> <username> <name>` | Create new cPanel account |
| `/listpanel` | List all your cPanel accounts |
| `/delpanel <username>` | Delete cPanel account |
| `/panel` | Access panel management menu |
| `/status` | View bot and server status |

### 👑 Admin Commands
| Command | Description |
|---------|-------------|
| `/addsrv <name> <domain> <apikey>` | Add new server |
| `/listsrv` | List all configured servers |
| `/delsrv <name>` | Remove server |
| `/srvinfo <name>` | Get detailed server information |
| `/addprem <user_id>` | Grant premium access |
| `/delprem <user_id>` | Remove premium access |
| `/addgc <group_id> <type>` | Add group access |
| `/delgc <group_id>` | Remove group access |
| `/listgc` | List group access permissions |
| `/ban <user_id>` | Ban user from bot |
| `/unban <user_id>` | Unban user |
| `/hidetag <message>` | Tag all group members |

### 🔱 Owner Commands
| Command | Description |
|---------|-------------|
| `/addowner <user_id>` | Add admin privileges |
| `/delowner <user_id>` | Remove admin privileges |
| `/clearserver` | Remove all servers (with confirmation) |
| `/stats` | Comprehensive bot statistics |
| `/maintenance <on/off/status>` | Toggle maintenance mode |
| `/security_panel <action>` | Access security management |
| `/cleanup <action>` | System cleanup and optimization |
| `/antilink <on/off>` | Control antilink protection |
| `/testing <on/off/status>` | Toggle testing mode |

## 🏗️ Architecture

### Project Structure
```
src/
├── commands/
│   ├── userCommands.js      # User-level commands
│   ├── adminCommands.js     # Admin-level commands
│   ├── groupCommands.js     # Group management commands
│   └── newCommands.js       # Additional commands
├── middleware/
│   ├── authMiddleware.js    # Authentication middleware
│   ├── rateLimitMiddleware.js # Rate limiting
│   ├── loggingMiddleware.js # Request logging
│   └── errorMiddleware.js   # Error handling
├── services/
│   ├── panelService.js      # cPanel management
│   ├── securityService.js   # Security monitoring
│   └── cleanupService.js    # Data cleanup
├── utils/
│   ├── helpers.js           # Utility functions
│   ├── logger.js            # Logging system
│   └── validator.js         # Input validation
├── handlers/
│   └── callbackHandler.js   # Callback query handling
├── config/
│   └── config.js            # Configuration settings
└── bot.js                   # Main bot class
```

### Data Flow
```
User Input → Middleware → Authentication → Command Handler → Service Layer → Response
     ↓
Rate Limiting → Logging → Validation → Business Logic → Data Storage → User Feedback
```

## 🔧 Configuration

### Environment Variables
```bash
# Required
BOT_TOKEN=your_telegram_bot_token
OWNER_ID=your_telegram_user_id
AUTH_PASSWORD=secure_authentication_password

# Optional
DEBUG=true
LOG_LEVEL=info
RATE_LIMIT_WINDOW=60000
RATE_LIMIT_MAX=10
```

### Data Files
```
data/
├── auth_data.json          # Authenticated users
├── admin_data.json         # Admin users
├── premium_data.json       # Premium users
├── group_access.json       # Group permissions
├── server_data.json        # Server configurations
├── panel_data.json         # cPanel accounts
├── banned_users.json       # Banned users
├── antilink.json           # Antilink settings
├── testing_mode.json       # Testing mode config
└── maintenance_mode.json   # Maintenance settings
```

## 🛡️ Security Features

### Authentication Layers
1. **Password Authentication** - Initial access control
2. **Role-Based Permissions** - Granular access control
3. **Group Access Control** - Per-group permissions
4. **Rate Limiting** - Abuse prevention
5. **Ban System** - Malicious user blocking

### Data Protection
- **Encryption at Rest** - Sensitive data encrypted
- **Secure API Storage** - Protected API keys
- **Access Logging** - All actions logged
- **Regular Cleanup** - Automatic data maintenance
- **Backup System** - Automated data backups

### Monitoring & Alerts
- **Real-time Monitoring** - Security event tracking
- **Failed Login Alerts** - Suspicious activity detection
- **Performance Monitoring** - System health tracking
- **Error Reporting** - Automatic error notifications

## 🧪 Testing Mode

Testing mode allows safe demonstration of all bot features without affecting production data.

### Features
- **Global Access** - All users can test features
- **Disclaimer Warnings** - Clear testing indicators
- **Safe Environment** - No production impact
- **Owner Control** - Easy enable/disable

### Usage
```bash
# Enable testing mode
/testing on

# Check status
/testing status

# Disable testing mode
/testing off
```

## 📊 Monitoring & Analytics

### Built-in Metrics
- **User Activity** - Command usage statistics
- **Performance Metrics** - Response times and memory usage
- **Error Tracking** - Comprehensive error logging
- **Security Events** - Authentication and access logs

### Health Checks
- **System Status** - Real-time health monitoring
- **Service Availability** - Component status tracking
- **Resource Usage** - Memory and CPU monitoring
- **Database Health** - Data integrity checks

## 🐛 Troubleshooting

### Common Issues

**Bot Not Responding**
```bash
# Check bot process
pm2 status xmsbra-bot

# View logs
pm2 logs xmsbra-bot --lines 50

# Restart bot
pm2 restart xmsbra-bot
```

**Authentication Problems**
- Verify `BOT_TOKEN` in configuration
- Check `OWNER_IDS` array contains your Telegram ID
- Ensure `AUTH_PASSWORD` is correctly set
- Validate data file permissions

**Group Features Not Working**
- Ensure bot has admin permissions in group
- Check group access configuration
- Verify antilink settings
- Test commands with admin account

### Debug Mode
```javascript
// Enable debug logging in config.js
DEBUG: true,
LOG_LEVEL: 'debug'
```

### Performance Optimization
```bash
# Monitor memory usage
pm2 monit

# Restart with memory limit
pm2 restart xmsbra-bot --max-memory-restart 500M

# Clear logs
pm2 flush xmsbra-bot
```

## 📈 Performance

### Benchmarks
- **Response Time** - < 100ms average
- **Memory Usage** - ~50MB baseline
- **Concurrent Users** - 1000+ supported
- **Uptime** - 99.9% availability target

### Optimization Features
- **Rate Limiting** - Prevents overload
- **Memory Management** - Automatic cleanup
- **Caching** - Improved response times
- **Load Balancing** - Multi-server support

## 🤝 Contributing

### Development Setup
```bash
# Fork and clone repository
git clone https://github.com/yourusername/chatme.git
cd chatme/optimized-telegram-bot

# Install dependencies
npm install

# Create feature branch
git checkout -b feature/your-feature-name

# Make changes and test
npm test

# Submit pull request
```

### Code Standards
- **ESLint** - Code linting and formatting
- **JSDoc** - Comprehensive documentation
- **Testing** - Unit and integration tests
- **Security** - Security-first development

### Feature Requests
1. Open GitHub issue with feature request template
2. Describe use case and implementation details
3. Discuss with maintainers
4. Submit pull request with implementation

## 📞 Support

### Getting Help
- **Documentation** - Check [DOCUMENTATION_v2.3.md](DOCUMENTATION_v2.3.md)
- **Bug Reports** - Use `/bugreport` command in bot
- **Feature Requests** - Contact developer directly
- **General Support** - Telegram [@ibradecode](https://t.me/ibradecode)

### Response Times
- **Critical Issues** - < 24 hours
- **Bug Reports** - 1-3 days
- **Feature Requests** - 1-7 days
- **General Questions** - 24-48 hours

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Telegram Bot API** - Powerful bot platform
- **Node.js Community** - Excellent ecosystem
- **Contributors** - Community support and feedback
- **Users** - Testing and feature suggestions

## 📊 Statistics

- **Version** - 2.3.0
- **Release Date** - September 17, 2025
- **Total Commands** - 35+
- **Security Layers** - 5
- **Supported Languages** - Indonesian, English
- **Minimum Node.js** - 16.0.0

---

<div align="center">

**Made with ❤️ by [@ibradecode](https://t.me/ibradecode)**

[🚀 Get Started](#quick-start) • [📚 Documentation](DOCUMENTATION_v2.3.md) • [🐛 Report Bug](https://t.me/ibradecode) • [💡 Request Feature](https://t.me/ibradecode)

</div>
