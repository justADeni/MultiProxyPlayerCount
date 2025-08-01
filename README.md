# 🌐 MultiProxyPlayerCount

A simple and lightweight Velocity plugin to show player lists across multiple proxies via Redis. Supports MiniMessage formatting and Java 21 virtual threads for fast async response.

---

## 🔧 Features

- View a global player list from all connected proxies
- Optionally group players by proxy
- Configurable MiniMessage-based formatting
- Hot-reloadable config
- Redis-based shared database
- Non-blocking, virtual thread-based execution 🚀

---

## 💬 Commands

| Command               | Description                          | Permission                          |
|-----------------------|------------------------------------|-----------------------------------|
| `/proxylist`          | Shows all online players            | `multiproxyplayercount.list`      |
| `/proxylist detailed` | Shows players grouped by proxy name| `multiproxyplayercount.detailed`  |
| `/proxylist reload`   | Reloads the config file             | `multiproxyplayercount.reload`    |

---

## ⚙️ Configuration

**File:** `plugins/MultiProxyPlayerCount/config.toml`

```toml
# Details for connecting to Redis DB
# Changes will be applied only after server restart
[connection]
host = "url-here"
port = 0000
user = "username"
password = "password123"

# List command formatting using MiniMessage
# Changes will be applied after config reload
[format]
# Name of the proxy in separated command
# Supports MiniMessage
name = "<gold>ChangeThisProxyNameInConfig</gold>"
simple = "<gold>Online Players: </gold> <aqua><online_players></aqua>"
detailed = "<proxy_name> <gold>: </gold> <aqua><online_players></aqua>"
```

---

## 📦 Installation
1. Download the plugin .jar
2. Drop it into your plugins/ directory for each proxy
3. Configure the Redis connection in config.toml
4. Restart all proxy instances
5. ✅ Done!
