# ChatGuard
![ChatGuard-Banner.png](assets/ChatGuard-Banner.png)

## Features
**ChatGuard** is a Minecraft plugin designed for servers running version b1.7.3.

- Cancels messages containing blocked terms or matching RegEx patterns.
- Censors signs containing blocked terms or matching RegEx patterns.
- Prevents players from joining with usernames containing blocked terms or matching RegEx patterns.
- Logs offenders (via Discord webhook, server console, or local file).
- Implements chat and command rate limiter to decrease spam.
- Triggers captcha verification on repeated message spam.
- Issues temporary mutes (requires [Essentials v2.5.8](#Requirements & Optional) as of ChatGuard `v5.0.0`).
- Enforces escalating penalties via a six-strike tier system.
- Plays local audio cues for offending players upon detection.

The plugin is entirely configurable.

---
## Contributions, Suggestions, and Issues
Consider helping ChatGuard become even more versatile and robust.

It is **highly recommended** to visit the [CONTRIBUTING](https://github.com/AleksandarHaralanov/ChatGuard/blob/master/.github/CONTRIBUTING.md) guide for details on how to get started and where to focus your efforts.

For any issues with the plugin, or suggestions, please submit them [here](https://github.com/AleksandarHaralanov/ChatGuard/issues).

---
## Download
Latest releases of **ChatGuard** can be found here on [GitHub](https://github.com/AleksandarHaralanov/ChatGuard/releases).<br/>
Alternatively, you can also download through [Modrinth](https://modrinth.com/plugin/chatguard/versions).

The plugin is fully open-source and transparent.<br/>
If you'd like additional peace of mind, you're welcome to scan the `.jar` file using [VirusTotal](https://www.virustotal.com/gui/home/upload).

---
## Requirements & Optional
Your server must be running one of the following APIs: CB1060-CB1092, [Project Poseidon](https://github.com/retromcorg/Project-Poseidon) or [UberBukkit](https://github.com/Moresteck/Project-Poseidon-Uberbukkit).

You can download **Essentials v2.5.8** from [here](https://github.com/AleksandarHaralanov/ChatGuard/raw/refs/heads/master/libs/Essentials.jar).

---
## Usage
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
## Configurations
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
    chat:
      s0: 1000
      s1: 2000
      s2: 3000
      s3: 4000
      s4: 5000
      s5: 6000
    command:
      s0: 5000
      s1: 7500
      s2: 10000
      s3: 12500
      s4: 15000
      s5: 17500

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
  essentials-mute:    # Essentials Mute Configuration
    enabled: true     # Toggles automatic mutes upon filter detection
    duration:         # Mute durations for strike tiers
      s0: "30m"
      s1: "1h"
      s2: "2h"
      s3: "4h"
      s4: "8h"
      s5: "24h"
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

> [!CAUTION]  
> If your server is not running **Essentials v2.5.8**, you must do one of the following:
> - **Disable Essentials mute support** by setting `filter.essentials-mute.enabled` to `false` in `config/config.yml`.
> - **Install Essentials** to use the temporary mute feature.
>   - You can download **Essentials v2.5.8** from [here](#Requirements & Optional).
> 
> Without one of the two, ChatGuard could break, and in-game messages might fail to send.

> [!NOTE]  
> Strike tiers increment only when the filter is enabled and a disallowed term or matching regex pattern is detected.
>
> Otherwise, all strike tiers default to `0` unless manually modified in `data/strikes.yml` or via the staff command.
