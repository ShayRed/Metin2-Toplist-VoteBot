package com.toplistbot.captcha;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.awt.Color;
import java.util.Map;
import java.util.HashMap;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;
import java.util.Set;
import java.util.HashSet;
import java.util.Base64;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import org.json.JSONObject;
import java.util.Scanner;

public class CaptchaSolver {
    private static final Logger logger = LoggerFactory.getLogger(CaptchaSolver.class);
    private WebDriver driver;

    public CaptchaSolver(WebDriver driver) {
        this.driver = driver;
    }

    public boolean solveCaptcha() {
        try {
            logger.info("Warte auf manuelle Captcha-Lösung...");
            System.out.println("\n=== CAPTCHA LÖSUNG ERFORDERLICH ===");
            System.out.println("1. Finde das einzigartige Bild (meist gelb/gold)");
            System.out.println("2. Klicke darauf");
            System.out.println("3. Drücke ENTER um fortzufahren");
            System.out.println("=====================================\n");

            // Warte auf Benutzereingabe
            new Scanner(System.in).nextLine();

            // Prüfe ob Captcha verschwunden ist
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".card-im")));
                logger.info("Captcha erfolgreich gelöst!");
                return true;
            } catch (Exception e) {
                logger.warn("Captcha wurde nicht korrekt gelöst, bitte erneut versuchen.");
                return solveCaptcha(); // Rekursiver Aufruf für erneuten Versuch
            }

        } catch (Exception e) {
            logger.error("Fehler beim Lösen des Captchas: " + e.getMessage(), e);
            return false;
        }
    }
} 