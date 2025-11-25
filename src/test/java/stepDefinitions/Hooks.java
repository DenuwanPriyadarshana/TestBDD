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

        // --- ENVIRONMENT FIX FOR CI SERVERS (HEADLESS MODE) ---
        // CRITICAL FIX: Add the --headless argument so Chrome can run without a display server.
        options.addArguments("--headless=new"); // Use 'new' for modern Chrome versions
        // -----------------------------------------------------------

        // --- NEW CRITICAL FIX: ANTI-BOT / USER-AGENT MASKING ---
        // Set a common User-Agent string to mask the automation signature
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        // -----------------------------------------------------------

        // --- FIXES FOR GOOGLE CAPTCHA / BOT DETECTION ---
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--incognito");
        // -----------------------------------------------------------

        // --- STABILITY & VISIBILITY ARGUMENTS ---
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080"); // Set a fixed size for consistent screenshots/elements
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--no-sandbox"); // Important for security in containerized environments
        options.addArguments("--disable-dev-shm-usage"); // Helps prevent memory issues in CI/Docker

        options.addArguments("--ignore-certificate-errors");
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