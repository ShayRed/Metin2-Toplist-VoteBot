package com.toplistbot;

import com.toplistbot.core.VoteBot;
import com.toplistbot.config.ProxyConfig;
import com.toplistbot.config.BotConfig;
import com.toplistbot.proxy.ProxyFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starte Toplist VoteBot");

        // Proxy-Nutzung konfigurieren (false = direkte Verbindung)
        BotConfig.setUseProxy(false);
        
        // Automatische Captcha-Lösung aktivieren
        BotConfig.setCaptchaApiKey("AUTO");  // Kein API-Key nötig, wir nutzen Bildanalyse
        
        ProxyConfig proxyConfig = null;
        if (BotConfig.isUseProxy()) {
            proxyConfig = new ProxyConfig();
            ProxyFetcher.loadFromLocalJson(proxyConfig, "socks5");
            
            if (proxyConfig.getProxyList().isEmpty()) {
                logger.error("Keine funktionierenden SOCKS5 Proxies gefunden!");
                return;
            }
        }

        VoteBot bot = new VoteBot(proxyConfig);
        
        try {
            while (true) {
                List<Integer> serverIndices = Arrays.asList(1);
                bot.voteForServers(serverIndices);
                
                // Warte 24 Stunden + zufällige Zeit (0-30 Minuten)
                long waitTime = 24 * 60 * 60 * 1000 + (long)(Math.random() * 30 * 60 * 1000);
                logger.info("Warte {} Stunden bis zum nächsten Vote", String.format("%.2f", waitTime / 3600000.0));
                Thread.sleep(waitTime);
                
                // Neuen Bot mit frischem Proxy erstellen
                bot.close();
                bot = new VoteBot(proxyConfig);
            }
        } catch (Exception e) {
            logger.error("Fehler beim Ausführen des Bots: " + e.getMessage());
        } finally {
            bot.close();
        }
    }
} 