# XMSBRA Telegram Bot v2.3 - Complete Documentation

## 📋 Table of Contents
1. [Overview](#overview)
2. [New Features](#new-features)
3. [Installation & Setup](#installation--setup)
4. [Command Reference](#command-reference)
5. [Group Management](#group-management)
6. [Security Features](#security-features)
7. [API Reference](#api-reference)
8. [Troubleshooting](#troubleshooting)

## 🎯 Overview

XMSBRA Telegram Bot v2.3 is an enterprise-grade cPanel management bot with advanced group management features, comprehensive security layers, and intuitive user experience.

### Key Highlights
- **Enterprise Security:** Multi-layer authentication and access control
- **Group Management:** Advanced hidetag and antilink features
- **Testing Environment:** Safe demo mode for feature testing
- **User Experience:** Consistent UI with developer support access
- **Documentation:** Complete privacy policy and help system

### System Requirements
- **Node.js:** >= 16.0.0
- **Memory:** >= 512MB RAM
- **Storage:** >= 100MB free space
- **Network:** Stable internet connection
- **Telegram:** Bot token from @BotFather

## 🚀 New Features

### 1. Enhanced Text Formatting

**Description:** Upgraded from single asterisk to double asterisk markdown formatting for better readability and consistency.

**Changes:**
- `*text*` → `**text**`
- Applied across all bot messages
- Improved visual hierarchy
- Better mobile compatibility

**Example:**
```
Before: *Welcome to XMSBRA Bot*
After:  **Welcome to XMSBRA Bot**
```

### 2. Privacy Policy System

**Command:** `/privacy`  
**Access Level:** Public  
**Description:** Comprehensive privacy policy explaining data collection, usage, and protection measures.

**Features:**
- Data collection transparency
- Usage explanation
- Security measures overview
- Data sharing policy
- User rights information
- Contact details for privacy concerns

**Implementation:**
```javascript
// Privacy policy accessible via callback
case 'privacy':
    await this.handlePrivacy(chatId, messageId, userId);
    break;
```

### 3. About Section

**Command:** `/about`  
**Access Level:** Public  
**Description:** Detailed information about the bot, its features, and technical specifications.

**Information Included:**
- Bot version and developer
- Feature overview
- System statistics (uptime, memory usage)
- Platform information
- Support contact

**Technical Details:**
- Real-time system metrics
- Memory usage monitoring
- Platform detection
- Version tracking

### 4. Commands List

**Command:** `/commands`  
**Access Level:** Public (role-based visibility)  
**Description:** Formatted list of all available commands categorized by access level.

**Categories:**
- **Public Commands:** Available to all users
- **Premium Commands:** Requires premium access
- **Admin Commands:** Admin and owner only
- **Owner Commands:** Owner exclusive

**Dynamic Display:**
- Shows only commands user has access to
- Role-based filtering
- Usage examples included
- Parameter explanations

### 5. Hidetag Feature

**Command:** `/hidetag <message>`  
**Access Level:** Admin only  
**Description:** Tag all group members with a custom message.

**Features:**
- Admin-only restriction
- Custom message support
- Group-specific functionality
- Telegram API compliance

**Usage Examples:**
```
/hidetag Meeting in 10 minutes!
/hidetag Important announcement for everyone
/hidetag Please check the new rules
```

**Technical Implementation:**
```javascript
async hidetag(msg, match) {
    const message = match[1];
    const chatId = msg.chat.id;
    
    // Get group administrators
    const admins = await this.bot.getChatAdministrators(chatId);
    
    // Create mention string
    let mentions = admins.map(admin => 
        `[${admin.user.first_name}](tg://user?id=${admin.user.id})`
    ).join(' ');
    
    // Send message with mentions
    await this.bot.sendMessage(chatId, `${message}\n\n${mentions}`, {
        parse_mode: 'Markdown'
    });
}
```

### 6. Antilink Feature

**Command:** `/antilink <on/off>`  
**Access Level:** Owner only  
**Description:** Automatically delete messages containing links in groups.

**Features:**
- Owner-only control
- Regex-based link detection
- Auto-delete with warnings
- Per-group settings
- Admin bypass

**Configuration:**
```javascript
// Antilink patterns
const linkPatterns = [
    /https?:\/\/[^\s]+/gi,
    /www\.[^\s]+/gi,
    /[^\s]+\.(com|org|net|io|co)[^\s]*/gi
];
```

**Usage:**
```
/antilink on   - Enable antilink protection
/antilink off  - Disable antilink protection
```

### 7. Testing Mode

**Command:** `/testing <on/off/status>`  
**Access Level:** Owner only  
**Description:** Global demo mode allowing all users to test bot features safely.

**Features:**
- Owner-only control
- Global access override
- Disclaimer warnings
- Safe testing environment
- Production protection

**States:**
- **ON:** All users can access all features with disclaimers
- **OFF:** Normal access control applies
- **STATUS:** Show current testing mode state

**Implementation:**
```javascript
// Check testing mode in middleware
const testingData = Helpers.loadJSON(config.PATHS.TESTING_MODE);
if (testingData.enabled) {
    // Allow access with disclaimer
    return true;
}
```

### 8. Bug Report System

**Command:** `/bugreport`  
**Access Level:** Public  
**Description:** Structured system for reporting bugs and requesting features.

**Information Provided:**
- Developer contact details
- Required information for reports
- Response time expectations
- Feature request process
- User ID for tracking

**Report Template:**
- Bug description
- Steps to reproduce
- Screenshots (if applicable)
- User ID (auto-included)
- Timestamp

### 9. Developer Button Integration

**Feature:** Developer button on all bot responses  
**Access Level:** Universal  
**Description:** Consistent access to developer support across all bot interactions.

**Implementation:**
```javascript
// Standard developer button
reply_markup: {
    inline_keyboard: [
        // ... other buttons
        [
            { text: '👨‍💻 Developer', url: 'https://t.me/ibradecode' }
        ]
    ]
}
```

## 🛠️ Installation & Setup

### Prerequisites
```bash
# Install Node.js (>= 16.0.0)
curl -fsSL https://deb.nodesource.com/setup_16.x | sudo -E bash -
sudo apt-get install -y nodejs

# Verify installation
node --version
npm --version
```

### Installation Steps

1. **Clone Repository:**
```bash
git clone https://github.com/noxira14/chatme.git
cd chatme/optimized-telegram-bot
```

2. **Install Dependencies:**
```bash
npm install
```

3. **Configuration:**
```bash
# Copy example config
cp src/config/config.example.js src/config/config.js

# Edit configuration
nano src/config/config.js
```

4. **Required Configuration:**
```javascript
module.exports = {
    BOT_TOKEN: 'your_bot_token_here',
    OWNER_IDS: [your_telegram_id],
    AUTH_PASSWORD: 'your_auth_password',
    // ... other settings
};
```

5. **Initialize Data Files:**
```bash
# Create data directories
mkdir -p data
touch data/auth_data.json
touch data/admin_data.json
touch data/premium_data.json
touch data/group_access.json
touch data/antilink.json
touch data/testing_mode.json

# Initialize with empty arrays/objects
echo '[]' > data/auth_data.json
echo '[]' > data/admin_data.json
echo '[]' > data/premium_data.json
echo '{}' > data/group_access.json
echo '{}' > data/antilink.json
echo '{"enabled": false}' > data/testing_mode.json
```

6. **Start Bot:**
```bash
# Development mode
npm run dev

# Production mode
npm start

# With PM2 (recommended)
pm2 start src/bot.js --name "xmsbra-bot"
```

## 📚 Command Reference

### Public Commands

| Command | Description | Usage |
|---------|-------------|-------|
| `/start` | Start the bot | `/start` |
| `/help` | Show help menu | `/help` |
| `/cekid` | Check your Telegram ID | `/cekid` |
| `/about` | Bot information | `/about` |
| `/privacy` | Privacy policy | `/privacy` |
| `/commands` | List all commands | `/commands` |
| `/bugreport` | Report bugs | `/bugreport` |
| `/auth` | Authenticate | `/auth <password>` |

### Premium Commands

| Command | Description | Usage |
|---------|-------------|-------|
| `/buatpanel` | Create cPanel | `/buatpanel <email> <username> <name>` |
| `/listpanel` | List your panels | `/listpanel` |
| `/delpanel` | Delete panel | `/delpanel <username>` |
| `/panel` | Panel management menu | `/panel` |
| `/status` | Bot status | `/status` |

### Admin Commands

| Command | Description | Usage |
|---------|-------------|-------|
| `/addsrv` | Add server | `/addsrv <name> <domain> <apikey>` |
| `/listsrv` | List servers | `/listsrv` |
| `/delsrv` | Delete server | `/delsrv <name>` |
| `/srvinfo` | Server info | `/srvinfo <name>` |
| `/addprem` | Add premium user | `/addprem <user_id>` |
| `/delprem` | Remove premium user | `/delprem <user_id>` |
| `/addgc` | Add group access | `/addgc <group_id> <type>` |
| `/delgc` | Remove group access | `/delgc <group_id>` |
| `/listgc` | List group access | `/listgc` |
| `/joingc` | Join group | `/joingc <group_id>` |
| `/ban` | Ban user | `/ban <user_id>` |
| `/unban` | Unban user | `/unban <user_id>` |
| `/hidetag` | Tag group members | `/hidetag <message>` |

### Owner Commands

| Command | Description | Usage |
|---------|-------------|-------|
| `/addowner` | Add admin | `/addowner <user_id>` |
| `/delowner` | Remove admin | `/delowner <user_id>` |
| `/clearserver` | Clear all servers | `/clearserver` |
| `/stats` | Bot statistics | `/stats` |
| `/maintenance` | Maintenance mode | `/maintenance <on/off/status>` |
| `/security_panel` | Security panel | `/security_panel <action>` |
| `/cleanup` | Cleanup system | `/cleanup <action>` |
| `/antilink` | Antilink control | `/antilink <on/off>` |
| `/testing` | Testing mode | `/testing <on/off/status>` |

## 👥 Group Management

### Hidetag Feature

**Purpose:** Tag all group members with important announcements.

**How it works:**
1. Admin uses `/hidetag <message>`
2. Bot retrieves group administrators
3. Creates mention string for all admins
4. Sends message with mentions

**Limitations:**
- Only tags administrators (Telegram API limitation)
- Requires admin permissions in group
- Admin-only command

**Best Practices:**
- Use for important announcements only
- Keep messages concise and clear
- Avoid spam usage

### Antilink Protection

**Purpose:** Automatically remove messages containing links to prevent spam.

**Configuration:**
```javascript
// Enable antilink
/antilink on

// Disable antilink
/antilink off
```

**Detection Patterns:**
- HTTP/HTTPS URLs
- WWW domains
- Common TLDs (.com, .org, .net, etc.)
- Shortened URLs

**Bypass Rules:**
- Group administrators are exempt
- Owner can always post links
- Bot ignores its own messages

**Customization:**
```javascript
// Add custom patterns in groupCommands.js
const customPatterns = [
    /telegram\.me\/[^\s]+/gi,
    /t\.me\/[^\s]+/gi
];
```

## 🔒 Security Features

### Multi-Layer Authentication

1. **Password Authentication:** Initial access control
2. **Role-Based Access:** Different permission levels
3. **Group Access Control:** Per-group permissions
4. **Rate Limiting:** Prevent abuse
5. **Ban System:** Block malicious users

### Access Levels

| Level | Permissions | Features |
|-------|-------------|----------|
| **Public** | Basic commands | Help, info, auth |
| **Premium** | Panel management | Create/manage panels |
| **Admin** | User management | Add/remove users, servers |
| **Owner** | Full control | All features, system control |

### Security Monitoring

**Real-time Monitoring:**
- Failed authentication attempts
- Suspicious activity patterns
- Rate limit violations
- Unauthorized access attempts

**Logging System:**
```javascript
// Security events are logged
logger.warn(`Failed authentication attempt by ${userId}`);
logger.info(`Admin action performed by ${userId}`);
logger.error(`Security violation detected: ${details}`);
```

### Data Protection

**Encryption:**
- Sensitive data encrypted at rest
- API keys securely stored
- Password hashing implemented

**Access Control:**
- File system permissions
- Database access restrictions
- Network security measures

## 🔧 API Reference

### Bot Class Methods

```javascript
class XMSBRABot {
    constructor(token) {
        this.bot = new TelegramBot(token, { polling: true });
        this.setupCommands();
        this.setupMiddleware();
    }

    // Command setup
    setupCommands() {
        // Initialize command handlers
    }

    // Middleware setup
    setupMiddleware() {
        // Authentication, rate limiting, etc.
    }

    // Start bot
    start() {
        console.log('Bot started successfully');
    }
}
```

### Helper Functions

```javascript
// Helpers utility class
class Helpers {
    static loadJSON(path) {
        // Load JSON file safely
    }

    static saveJSON(path, data) {
        // Save JSON file with backup
    }

    static formatBytes(bytes) {
        // Format bytes to human readable
    }

    static getRuntime() {
        // Get bot uptime
    }
}
```

### Configuration Structure

```javascript
module.exports = {
    // Bot configuration
    BOT_TOKEN: process.env.BOT_TOKEN,
    OWNER_IDS: [123456789],
    AUTH_PASSWORD: 'secure_password',

    // Paths
    PATHS: {
        AUTH_DATA: './data/auth_data.json',
        ADMIN_DATA: './data/admin_data.json',
        PREMIUM_DATA: './data/premium_data.json',
        GROUP_ACCESS: './data/group_access.json',
        ANTILINK_DATA: './data/antilink.json',
        TESTING_MODE: './data/testing_mode.json'
    },

    // Rate limiting
    RATE_LIMIT: {
        WINDOW_MS: 60000,
        MAX_REQUESTS: 10
    }
};
```

## 🐛 Troubleshooting

### Common Issues

**1. Bot Not Responding**
```bash
# Check bot status
pm2 status xmsbra-bot

# View logs
pm2 logs xmsbra-bot

# Restart bot
pm2 restart xmsbra-bot
```

**2. Authentication Failures**
- Verify BOT_TOKEN in config
- Check OWNER_IDS array
- Ensure AUTH_PASSWORD is set
- Validate data file permissions

**3. Command Not Working**
- Check user permissions
- Verify command syntax
- Review error logs
- Test in private chat first

**4. Group Features Not Working**
- Ensure bot is admin in group
- Check group permissions
- Verify antilink settings
- Test hidetag with admin account

### Debug Mode

**Enable Debug Logging:**
```javascript
// In config.js
DEBUG: true,
LOG_LEVEL: 'debug'
```

**View Debug Logs:**
```bash
# Real-time logs
tail -f logs/bot.log

# Error logs only
grep "ERROR" logs/bot.log

# Search specific user
grep "user_id:123456" logs/bot.log
```

### Performance Issues

**Memory Usage:**
```bash
# Check memory usage
free -h

# Monitor bot memory
ps aux | grep node

# Optimize if needed
pm2 restart xmsbra-bot --max-memory-restart 500M
```

**Response Time:**
- Check network connectivity
- Verify server resources
- Review database performance
- Optimize code if needed

### Data Recovery

**Backup Data Files:**
```bash
# Create backup
cp -r data/ backup/data_$(date +%Y%m%d_%H%M%S)/

# Restore from backup
cp -r backup/data_20250917_120000/* data/
```

**Reset Data:**
```bash
# Reset specific data
echo '[]' > data/auth_data.json

# Reset all data
rm data/*.json
npm run init-data
```

## 📞 Support & Contact

### Developer Contact
- **Telegram:** [@ibradecode](https://t.me/ibradecode)
- **GitHub:** [noxira14/chatme](https://github.com/noxira14/chatme)
- **Email:** Available on request

### Bug Reports
1. Use `/bugreport` command in bot
2. Include detailed description
3. Provide steps to reproduce
4. Attach screenshots if applicable
5. Include your User ID

### Feature Requests
1. Contact developer directly
2. Explain use case clearly
3. Provide implementation suggestions
4. Consider development complexity

### Response Times
- **Critical Bugs:** < 24 hours
- **Normal Issues:** 1-3 days
- **Feature Requests:** 1-7 days
- **General Support:** 24-48 hours

---

**Documentation Version:** 2.3.0  
**Last Updated:** September 17, 2025  
**Developer:** @ibradecode  
**License:** MIT
