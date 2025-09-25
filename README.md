# 🛡️ ChatGuard
![ChatGuard-Banner.png](assets/ChatGuard-Banner.png)

## ✨ Features
### Content Filtering
Configurable terms and regex patterns
- 🛑 Blocks chat messages containing inappropriate content
- 🪧 Censors signs containing inappropriate content
- 👤 Prevents players from joining with inappropriate usernames

### Anti-Spam Protection
- ⏱️ Configurable rate-limits for chat messages and commands for each strike tier
- 🤖 Triggers CAPTCHA verification for repeated message spam

### Feedback & Notifications
- 🔊 Plays audio cues to notify offending players when violations occur
- 💬️ Sends warning messages to players explaining why content was blocked

### Moderation System
- ⚠️ Six-tier strike system for tracking repeat offenders
- 🔇 Issues temporary mutes with escalating durations based on current strike tier
- ⏱️ Configurable mute durations for each strike tier
- 🛠️ Staff commands to view and set player strikes manually

### Logging & Monitoring
- 📱 Discord webhook integration with customizable embeds
- 💻 Console logging with detailed information
- 📄 Local file logging for record-keeping

---
## 🤝 Contributions, Suggestions, and Issues
Consider helping ChatGuard become even more versatile and robust.

It is **highly recommended** to visit the [CONTRIBUTING](https://github.com/AleksandarHaralanov/ChatGuard/blob/master/.github/CONTRIBUTING.md) guide for details on how to get started and where to focus your efforts.

For any issues with the plugin, or suggestions, please submit them [here](https://github.com/AleksandarHaralanov/ChatGuard/issues).

---
## ⬇️ Download
Latest stable release of **ChatGuard** can be found here on [GitHub](https://github.com/AleksandarHaralanov/ChatGuard/releases/latest).<br/>

The plugin is fully open-source and transparent.<br/>
If you'd like additional peace of mind, you're welcome to scan the `.jar` file using [VirusTotal](https://www.virustotal.com/gui/home/upload).

---
## 📋 Requirements
Your server must be running one of the following software: [CB1060](https://github.com/AleksandarHaralanov/ChatGuard/raw/refs/heads/master/libs/craftbukkit-1060.jar), [Project Poseidon](https://github.com/retromcorg/Project-Poseidon) or [UberBukkit](https://github.com/Moresteck/Project-Poseidon-Uberbukkit).

---
## 🚀 Usage
By default, only OPs have permission.

Use PermissionsEx or similar plugins to grant groups the permission, enabling the commands.

### Commands:
- `/cg` - View ChatGuard commands.
- `/cg about` - About ChatGuard.
- `/cg captcha <code>` - Captcha verification.
- `/cg reload` - `chatguard.config` - Reload ChatGuard configuration.
- `/cg strike <username>` - `chatguard.config` - View strike of player.
- `/cg strike <username> [0-5]` - `chatguard.config` - Set strike of player.

### Permissions:
#### Single permissions:
- `chatguard.bypass` - Allows player to bypass the ChatGuard protection.
- `chatguard.config` - Allows player to reload and modify the ChatGuard configuration.
- `chatguard.captcha` - Allows player to be notified when someone is prompted a captcha verification.
#### Wildcard permissions:
- `chatguard.*` - Wildcard permission that grants all permissions.

---
## ⚙️ Configurations
ChatGuard generates two configuration files using the default settings in the **config** directory.

Additionally, it creates two data files, `captchas.yml` and `strikes.yml`, in the **data** directory.

#### Main Config `config.yml`:
```yaml
miscellaneous:        # Misc Configurations
  audio-cues: true    # Offending player hears a local audio cue upon detection

spam-prevention:      # Spam Prevention Configuration
  enabled:            # Toggles spam prevention for chat messages and commands
    chat: true
    command: true
  warn-player: true   # Warns offending player upon detection
  cooldown-ms:        # Cooldown durations in milliseconds for strike tiers
    chat: [1000, 2000, 3000, 4000, 5000, 6000]
    command: [1000, 2000, 3000, 4000, 5000, 6000]

captcha:              # Captcha Configuration
  enabled: true       # Toggles captcha verification
  threshold: 5        # Triggers captcha after X identical messages, canceling on the last attempt
  code:               # Captcha characters and length
    characters: "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789"
    length: 5
  log-console: true   # Log captcha trigger to server console
  whitelist: []       # Allowed captcha bypass terms for sanitizing

filter:               # Filter Configuration
  enabled:            # Toggles filtration for chat messages, player usernames, and signs
    chat: true
    sign: true
    name: true
  warn-player: true   # Warns offending player upon detection
  log:                # Log filter trigger to:
    console: true     # Server console
    local-file: true  # Local file
  auto-mute:          # Auto Mute Configuration
    enabled: true     # Toggles auto mute (Need to have a compatible plugin installed, such as Essentials or ZCore)
    duration: ["30m", "1h", "2h", "4h", "8h", "24h"]  # Mute durations for strike tiers
  rules:              # Filter Rule Configurations
    regex: []         # Regular expression patterns
    terms:            # Term Configurations
      whitelist: []   # Allowed chat message and player username bypass terms for sanitizing
      blacklist: []   # Disallowed chat message and player username bypass terms
```
<br/>

#### Discord Embed Config `discord.yml`:
```yaml
webhook-url: ""          # Discord Webhook URL

embed-log:               # Embed Configurations
  type:                  # Logs to embed
    chat: false
    sign: false
    name: false
    captcha: false
  optional:
    censor: true         # Censors sensitive data, such as IP addresses and the filter trigger in the embed
    data:
      ip-address: true   # Includes player IP address in the embed
      timestamp: true    # Includes timestamp in the embed

customize:               # Embed customization options
  player-avatar: "https://minotar.net/avatar/%player%.png"   # Place %player% where the player's username would usually go
  type:                  # Various embed type log customizations
    chat:
      color: "#FF5555"
      webhook:
        name: "ChatGuard - Chat"
        icon: "https://raw.githubusercontent.com/AleksandarHaralanov/ChatGuard/refs/heads/master/assets/ChatGuard-Logo.png"
    sign:
      color: "#FFAA00"
      webhook:
        name: "ChatGuard - Sign"
        icon: "https://raw.githubusercontent.com/AleksandarHaralanov/ChatGuard/refs/heads/master/assets/ChatGuard-Logo-Gold.png"
    name:
      color: "#FFFF55"
      webhook:
        name: "ChatGuard - Name"
        icon: "https://raw.githubusercontent.com/AleksandarHaralanov/ChatGuard/refs/heads/master/assets/ChatGuard-Logo-Yellow.png"
    captcha:
      color: "#AA00AA"
      webhook:
        name: "ChatGuard - Captcha"
        icon: "https://raw.githubusercontent.com/AleksandarHaralanov/ChatGuard/refs/heads/master/assets/ChatGuard-Logo-Dark-Purple.png"
```

> [!WARNING]
> Install a compatible plugin to use the auto mute feature.
> - Compatible plugins are [Essentials v2.5.8](https://github.com/AleksandarHaralanov/ChatGuard/raw/refs/heads/master/libs/Essentials.jar) and [ZCore]([https://github.com/zcoreplugin/zcore) (as of ChatGuard `v6.0.0`).
>
> If `auto-mute.enabled` in `config/config.yml` is left on `true`, it will act as disabled when no compatible plugins are detected.

> [!NOTE]  
> Strike tiers increment only when the filter is enabled and a disallowed term or matching regex pattern is detected.
>
> Otherwise, all strike tiers default to `0` unless manually modified in `data/strikes.yml` or via the staff command.

## 📊 Project Statistics
<img src="https://repobeats.axiom.co/api/embed/4726e8b7a0fb27e54816c2f0bb26bf89048cfbd4.svg" alt="Statistics" />
