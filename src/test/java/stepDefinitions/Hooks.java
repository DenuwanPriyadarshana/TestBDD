package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

public class Hooks {

    // Make the WebDriver instance static and public so Step Definitions can access it.
    public static WebDriver driver;

    @Before
    public void setup() {
        ChromeOptions options = new ChromeOptions();

        // --- FIXES FOR GOOGLE CAPTCHA / BOT DETECTION ---
        // 1. Hide the "Chrome is being controlled by automation" info bar
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        // 2. Prevent detection by modifying the navigator.webdriver property
        options.addArguments("--disable-blink-features=AutomationControlled");
        // 3. Run in Incognito mode: Ensures a clean, fresh session every time
        options.addArguments("--incognito");
        // -----------------------------------------------------------

        // --- STABILITY & VISIBILITY ARGUMENTS (Headless removed for visibility) ---
        // options.addArguments("--headless"); // REMOVED so you can see the browser
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-popup-blocking");

        // --- Your added arguments (Typo corrected in remote-allow-origins) ---
        options.addArguments("--ignore-certificate-errors");
        // FIX: Corrected typo from 'remort-allow-origins' to 'remote-allow-origins'
        options.addArguments("--remote-allow-origins=*");
        options.setAcceptInsecureCerts(true);

        driver = new ChromeDriver(options);

        // General setup for all scenarios
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @After
    public void teardown() {
        // Cleanup after every scenario runs
        if (driver != null) {
            driver.quit();
        }
    }
}