<div align="center">
  <img src="https://i.imgur.com/pYX4rOZ.png" alt="Toplist Vote Bot Logo" width="200">
  <h1>🤖 Toplist Vote Bot</h1>
  <p>
    <strong>Ein automatisierter Bot für Metin2 Private Server Toplisten, geschrieben in Java.</strong>
  </p>

  <p>
    <a href="#-features">Features</a> •
    <a href="#-technologien">Technologien</a> •
    <a href="#-systemarchitektur">Systemarchitektur</a> •
    <a href="#-installation">Installation</a> •
    <a href="#%EF%B8%8F-konfiguration">Konfiguration</a> •
    <a href="#-verwendung">Verwendung</a> •
    <a href="#-mitwirken">Mitwirken</a> •
    <a href="#-lizenz">Lizenz</a>
  </p>

  <img src="https://img.shields.io/github/stars/yourusername/toplist-vote-bot?style=social" alt="GitHub Sterne">
  <img src="https://img.shields.io/github/forks/yourusername/toplist-vote-bot?style=social" alt="GitHub Forks">
  <img src="https://img.shields.io/github/issues/yourusername/toplist-vote-bot" alt="GitHub Issues">
  <img src="https://img.shields.io/github/license/yourusername/toplist-vote-bot" alt="Lizenz">
</div>

---

## 🚀 Features

- ✅ Automatisches Voting für Metin2 Private Server
- 🌐 Proxy-Unterstützung mit Rotation für anonymes Voting
- 🔍 Manuelle Captcha-Lösung mit benutzerfreundlicher Führung
- 💪 Robuste Fehlerbehandlung und Wiederholungsmechanismen
- 📝 Detailliertes Logging für bessere Nachverfolgung
- ⏰ Konfigurierbare Wartezeiten zwischen Votes

## 🛠 Technologien

- 🍵 **Programmiersprache**: Java 11
- 🔧 **Build-Tool**: Maven
- 🚗 **Hauptbibliotheken**:
  - Selenium WebDriver für Browser-Automatisierung
  - WebDriverManager für automatische ChromeDriver-Verwaltung
  - SLF4J & Logback für strukturiertes Logging
  - JSON für Proxy-Konfiguration
  - Commons IO für Bildverarbeitung

## 🏗 Systemarchitektur

```
ToplistVoteBot/
├── 📁 src/
│   └── 📁 main/
│       └── 📁 java/
│           └── 📁 com/
│               └── 📁 toplistbot/
│                   ├── 🏠 Main.java
│                   ├── 📁 core/
│                   │   └── 🤖 VoteBot.java
│                   ├── 📁 captcha/
│                   │   └── 🔍 CaptchaSolver.java
│                   └── 📁 config/
│                       ├── ⚙️ BotConfig.java
│                       └── 🌐 ProxyConfig.java
├── 📁 proxy/
│   └── 🌐 proxy.json
└── 📄 pom.xml
```

## 💻 Installation

1. 🔍 Stelle sicher, dass Java 11 oder höher installiert ist
2. 📥 Clone das Repository:
   ```
   git clone https://github.com/ShayRed/Metin2-Toplist-VoteBot.git
   ```
3. 📂 Navigiere zum Projektverzeichnis:
   ```
   cd toplist-vote-bot
   ```
4. 🔨 Baue das Projekt mit Maven:
   ```
   mvn clean install
   ```

## ⚙️ Konfiguration

1. 🌐 **Proxy-Konfiguration** (optional):
   - Bearbeite die `proxy/proxy.json` Datei
   - Format:
     ```json
     {
       "proxies": [
         {
           "host": "proxy.example.com",
           "port": 1080,
           "type": "SOCKS5",
           "username": "user",
           "password": "pass"
         }
       ]
     }
     ```

2. ⚙️ **Bot-Konfiguration**:
   - Öffne `src/main/java/com/toplistbot/config/BotConfig.java`
   - Passe die Einstellungen nach Bedarf an (z.B. Wartezeiten, Ziel-URLs)

## 🎯 Verwendung

1. 🖥️ Führe den Bot aus:
   ```
   java -jar target/toplist-vote-bot-1.0-SNAPSHOT.jar
   ```

2. 🤖 Der Bot wird automatisch für die konfigurierten Server voten. 
3. 🔍 Wenn ein Captcha erscheint, folge den Anweisungen in der Konsole, um es manuell zu lösen.

## 🤝 Mitwirken

Beiträge sind willkommen! Wenn du Verbesserungsvorschläge oder neue Features hast:

1. 🍴 Forke das Projekt
2. 🌿 Erstelle einen Feature-Branch (`git checkout -b feature/AmazingFeature`)
3. 💾 Committe deine Änderungen (`git commit -m 'Add some AmazingFeature'`)
4. 🔀 Pushe zum Branch (`git push origin feature/AmazingFeature`)
5. 🔃 Öffne einen Pull Request

## 📄 Lizenz

Dieses Projekt steht unter der MIT-Lizenz. Siehe `LICENSE` für weitere Informationen.

---

<div align="center">
  <p>Erstellt mit ❤️ von <a href="https://github.com/ShayRed">ShayRed</a></p>
</div>
