# ChatGuard
![ChatGuard-Banner.png](assets/ChatGuard-Banner.png)

## Features
**ChatGuard** is a Minecraft plugin designed for servers running version b1.7.3.

- Cancels messages containing blocked terms or matching RegEx patterns.
- Prevents players from joining with usernames containing blocked terms or matching RegEx patterns.
- Logs offenders (via Discord webhook, server console, or local file).
- Prevents chat message and command spam.
- Prompts captcha verification on suspected bot-like behavior.
- Issues temporary mutes (requires [Essentials v2.5.8](#requirements) as of ChatGuard `v4.1.1`).
- Enforces escalating penalties via a six-strike tier system.
- Plays local sound cues for offending players upon detection.

The plugin is entirely configurable.

---
## Contributing Code & Reporting Issues
Consider helping ChatGuard become even more versatile and robust.

Visit the [CONTRIBUTING](https://github.com/AleksandarHaralanov/ChatGuard/blob/master/.github/CONTRIBUTING.md) guide for details on how to get started and where to focus your efforts.

For any issues with the plugin, or suggestions, please report them [here](https://github.com/AleksandarHaralanov/ChatGuard/issues).

---
## Download
Latest releases of **ChatGuard** can be found here on [GitHub](https://github.com/AleksandarHaralanov/ChatGuard/releases).<br/>
Alternatively, you can also download through [Modrinth](https://modrinth.com/plugin/chatguard/versions).

The plugin is fully open-source and transparent.<br/>
If you'd like additional peace of mind, you're welcome to scan the `.jar` file using [VirusTotal](https://www.virustotal.com/gui/home/upload).

---
## Requirements
Your server must be running one of the following APIs: CB1060-CB1092, [Project Poseidon](https://github.com/retromcorg/Project-Poseidon) or [UberBukkit](https://github.com/Moresteck/Project-Poseidon-Uberbukkit).

It also needs to be running **Essentials v2.5.8 or newer** (as of ChatGuard `v4.1.1`).<br/>You can download it from [here](https://github.com/AleksandarHaralanov/ChatGuard/raw/refs/heads/master/libs/Essentials.jar).

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
Generates `config.yml` and `strikes.yml` located at `plugins/ChatGuard`.

> [!CAUTION]
> 🔖`v4.1.1`: If your server is not running **Essentials v2.5.8 or newer**, make sure to download and install it. Without it, the entire plugin will break, and in-game messages will fail to send properly.
>
> You can find the download [here](#requirements) in the requirements heading.

### Config
This is the default `config.yml` configuration file:
```yaml
miscellaneous:        # QoL Configurations
  sound-cues: true    # Offending player hears a local sound cue upon detection

spam-prevention:      # Spam Prevention Configuration
  enabled:            # Toggles spam prevention for chat messages and commands
    message: true
    command: true
  warn-player: true   # Warns offending player upon detection
  cooldown-ms:        # Cooldown durations in milliseconds for strike tiers
    message:
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
  log:                # Logs captcha triggers to:
    console: true     # Server console
    local-file: true  # Local file
    discord-webhook:  # Discord webhook by an embed
      enabled: false  # Toggles Discord webhook
      url: ""         # Discord webhook URL
  whitelist: []       # Allowed captcha bypass terms for sanitizing

filter:               # Filter Configuration
  enabled: true       # Toggles filtering of chat messages and player usernames
  warn-player: true   # Warns offending player upon detection
  log:                # Logs captcha triggers to:
    console: true     # Server console
    local-file: true  # Local file
    discord-webhook:  # Discord webhook by an embed
      enabled: false  # Toggles Discord webhook
      url: ""         # Discord webhook URL
  mute:               # Mute Configuration
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

### Strikes
The default `strikes.yml` configuration file is initially empty. When a player joins for the first time after ChatGuard is installed on the server, they are added to the configuration with 0 strikes. From there, the plugin manages their strikes, incrementing them up to a maximum of 5 as necessary. Read note below on how that works.

> [!NOTE]
> 🔖`v4.1.1`: Strike tiers will increment only when the filter is enabled, and a disallowed term or matching regex pattern is detected in a message. Otherwise, all strike tiers will default to 0 unless manually modified in the configuration file or through the included command.
