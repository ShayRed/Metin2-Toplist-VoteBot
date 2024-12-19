package com.toplistbot.config;

import java.util.ArrayList;
import java.util.List;

public class ProxyConfig {
    private List<Proxy> proxyList;

    public ProxyConfig() {
        this.proxyList = new ArrayList<>();
    }

    public void addProxy(String host, int port) {
        proxyList.add(new Proxy(host, port));
    }

    public void addProxy(String host, int port, String username, String password) {
        proxyList.add(new Proxy(host, port, username, password));
    }

    public List<Proxy> getProxyList() {
        return proxyList;
    }

    public static class Proxy {
        private String host;
        private int port;
        private String username;
        private String password;

        public Proxy(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public Proxy(String host, int port, String username, String password) {
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public boolean hasAuth() {
            return username != null && password != null;
        }
    }
} 