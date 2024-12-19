package com.toplistbot.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.time.Duration;

import com.toplistbot.config.ProxyConfig;
import com.toplistbot.config.BotConfig;
import com.toplistbot.captcha.CaptchaSolver;

public class VoteBot {
    private static final Logger logger = LoggerFactory.getLogger(VoteBot.class);
    private WebDriver driver;
    private ProxyConfig proxyConfig;
    private int currentProxyIndex = 0;

    public VoteBot(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
        setupDriver();
    }

    private void setupDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        if (BotConfig.isUseProxy() && proxyConfig != null && !proxyConfig.getProxyList().isEmpty()) {
            ProxyConfig.Proxy proxy = proxyConfig.getProxyList().get(currentProxyIndex);
            
            org.openqa.selenium.Proxy seleniumProxy = new org.openqa.selenium.Proxy();
            String proxyString = proxy.getHost() + ":" + proxy.getPort();
            
            seleniumProxy.setSocksProxy(proxyString);
            
            if (proxy.hasAuth()) {
                seleniumProxy.setSocksUsername(proxy.getUsername());
                seleniumProxy.setSocksPassword(proxy.getPassword());
            }
            
            options.setCapability("proxy", seleniumProxy);
            
            logger.info("Verwende {} SOCKS5 Proxy: {}", 
                proxy.hasAuth() ? "authentifizierten" : "", proxyString);
        } else {
            logger.info("Verwende direkte Verbindung (keine Proxies)");
        }
        
        // Anti-Detection Einstellungen
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--incognito");
        options.addArguments("--window-size=1920,1080");
        
        // User-Agent randomisieren
        String[] userAgents = {
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:120.0) Gecko/20100101 Firefox/120.0"
        };
        options.addArguments("--user-agent=" + userAgents[(int)(Math.random() * userAgents.length)]);

        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    public void voteForServers(List<Integer> serverIndices) {
        try {
            for (int index : serverIndices) {
                logger.info("Starte Voting für Server #" + index);
                voteForServer(index);
                Thread.sleep(5000); // Warte zwischen Votes
            }
        } catch (Exception e) {
            logger.error("Fehler beim Multi-Server Voting: " + e.getMessage());
        } finally {
            close();
        }
    }

    private void voteForServer(int serverIndex) {
        try {
            logger.info("Starte Voting-Prozess für Server #" + serverIndex);
            
            // Zufällige Wartezeit zwischen 2-5 Sekunden
            Thread.sleep(2000 + (long)(Math.random() * 3000));
            
            // Direkte Server-URL verwenden
            driver.get("https://metin2pserver.net/pserver.de/2503");
            Thread.sleep(3000);
            
            // Vote-Button klicken
            WebElement voteButton = waitForClickable(By.cssSelector(".icon-vote"), 10);
            
            // Scroll zum Button
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", voteButton);
            Thread.sleep(1000);
            
            // Klicken
            try {
                voteButton.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", voteButton);
            }
            Thread.sleep(2000);
            
            // Vote bestätigen
            WebElement submitButton = waitForClickable(By.name("vote_submit"), 10);
            submitButton.click();
            Thread.sleep(2000);
            
            // Captcha behandeln
            if (isCaptchaPresent()) {
                if (BotConfig.getCaptchaApiKey() != null) {
                    logger.info("Captcha erkannt - Versuche automatische Lösung");
                    CaptchaSolver solver = new CaptchaSolver(driver);
                    boolean solved = solver.solveCaptcha();
                    
                    // Warte kurz nach der Captcha-Lösung
                    Thread.sleep(2000);
                    
                    // Prüfe ob Captcha noch da ist (Fallback auf manuelle Lösung)
                    if (!solved || isCaptchaPresent()) {
                        logger.info("Automatische Lösung fehlgeschlagen - Warte auf manuelle Lösung");
                        System.out.println("Bitte Captcha manuell lösen und Enter drücken...");
                        new Scanner(System.in).nextLine();
                    }
                } else {
                    logger.info("Captcha erkannt - Warte auf manuelle Lösung");
                    System.out.println("Bitte Captcha manuell lösen und Enter drücken...");
                    new Scanner(System.in).nextLine();
                }
            }
            
            logger.info("Voting abgeschlossen für Server: Elveron");
            
            // Warte zwischen 5-10 Sekunden vor dem nächsten Vote
            Thread.sleep(5000 + (long)(Math.random() * 5000));
            
        } catch (Exception e) {
            logger.error("Fehler beim Voting-Prozess: " + e.getMessage());
        }
    }

    private boolean isCaptchaPresent() {
        try {
            return driver.findElement(By.cssSelector(".card-im")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }

    private boolean testProxy(String host, int port) {
        try {
            URL url = new URL("https://metin2pserver.net");
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
            conn.setConnectTimeout(5000);
            conn.connect();
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    // Neue Hilfsmethode zum Warten auf klickbare Elemente
    private WebElement waitForClickable(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
} 