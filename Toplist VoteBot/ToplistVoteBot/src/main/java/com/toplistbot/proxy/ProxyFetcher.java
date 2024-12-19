package com.toplistbot.proxy;

import com.toplistbot.config.ProxyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.atomic.AtomicBoolean;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class ProxyFetcher {
    private static final Logger logger = LoggerFactory.getLogger(ProxyFetcher.class);
    
    public static void loadFromLocalJson(ProxyConfig proxyConfig, String protocolFilter) {
        try {
            logger.info("Starte Laden der Proxies aus proxies.json...");
            String jsonContent = new String(Files.readAllBytes(Paths.get("proxies.json")));
            JSONArray proxies = new JSONArray(jsonContent);
            
            logger.info("Gefunden: {} Proxies in der Datei", proxies.length());
            int totalProxies = 0;
            int successfulProxies = 0;
            
            for (int i = 0; i < proxies.length(); i++) {
                JSONObject proxyJson = proxies.getJSONObject(i);
                String ip = proxyJson.getString("ip");
                int port = proxyJson.getInt("port");
                String proxyProtocol = proxyJson.getString("protocol");
                String country = proxyJson.getString("country");
                
                String username = proxyJson.optString("username", null);
                String password = proxyJson.optString("password", null);
                
                logger.info("Teste Proxy {}/{}: {}:{} ({} - {})", 
                    i+1, proxies.length(), ip, port, proxyProtocol, country);
                
                if (proxyProtocol.toLowerCase().contains("socks5")) {
                    totalProxies++;
                    
                    // Teste den Proxy mit Timeout
                    boolean success = testProxyWithTimeout(ip, port, username, password);
                    
                    if (success) {
                        if (username != null && password != null) {
                            proxyConfig.addProxy(ip, port, username, password);
                        } else {
                            proxyConfig.addProxy(ip, port);
                        }
                        successfulProxies++;
                        logger.info("✓ Proxy erfolgreich getestet: {}:{} ({} - {})", 
                            ip, port, proxyProtocol, country);
                    } else {
                        logger.info("✗ Proxy nicht erreichbar: {}:{}", ip, port);
                    }
                }
            }
            
            logger.info("Proxy-Statistik:");
            logger.info("Gefundene Proxies: {}", totalProxies);
            logger.info("Erfolgreich getestete Proxies: {}", successfulProxies);
            if (totalProxies > 0) {
                logger.info("Erfolgsrate: {}%", String.format("%.1f", (successfulProxies * 100.0 / totalProxies)));
            }
            
        } catch (Exception e) {
            logger.error("Fehler beim Laden der Proxies: " + e.getMessage(), e);
        }
    }

    private static boolean testProxyWithTimeout(String host, int port, String username, String password) {
        AtomicBoolean success = new AtomicBoolean(false);
        Thread testThread = new Thread(() -> {
            success.set(testProxy(host, port, username, password));
        });
        testThread.start();
        
        try {
            testThread.join(15000);
            if (testThread.isAlive()) {
                testThread.interrupt();
                logger.debug("Proxy-Test Timeout für {}:{}", host, port);
                return false;
            }
            return success.get();
        } catch (InterruptedException e) {
            return false;
        }
    }

    private static boolean testProxy(String host, int port, String username, String password) {
        try {
            logger.debug("Starte Test für Proxy {}:{}", host, port);
            
            if (username != null && password != null) {
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password.toCharArray());
                    }
                });
            }
            
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port));
            
            // Test 1: Google
            URL testUrl = new URL("http://www.google.com");
            HttpURLConnection conn = (HttpURLConnection) testUrl.openConnection(proxy);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("HEAD");
            
            if (conn.getResponseCode() != 200) {
                logger.debug("Google-Test fehlgeschlagen für {}:{}", host, port);
                return false;
            }
            
            // Test 2: Metin2 Server
            URL targetUrl = new URL("https://metin2pserver.net");
            conn = (HttpURLConnection) targetUrl.openConnection(proxy);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod("HEAD");
            
            if (conn.getResponseCode() != 200) {
                logger.debug("Metin2-Test fehlgeschlagen für {}:{}", host, port);
                return false;
            }
            
            logger.debug("Proxy {}:{} erfolgreich getestet", host, port);
            return true;
            
        } catch (Exception e) {
            logger.debug("Fehler beim Testen von {}:{}: {}", host, port, e.getMessage());
            return false;
        }
    }
} 