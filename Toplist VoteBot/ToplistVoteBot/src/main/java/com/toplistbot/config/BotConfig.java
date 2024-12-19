package com.toplistbot.config;

public class BotConfig {
    private static boolean useProxy = false;  // Standard: keine Proxies
    private static String captchaApiKey = null;  // Captcha API Key

    public static boolean isUseProxy() {
        return useProxy;
    }

    public static void setUseProxy(boolean useProxy) {
        BotConfig.useProxy = useProxy;
    }

    public static String getCaptchaApiKey() {
        return captchaApiKey;
    }

    public static void setCaptchaApiKey(String apiKey) {
        captchaApiKey = apiKey;
    }
} 