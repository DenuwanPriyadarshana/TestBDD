package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Assert;
import java.time.Duration;
import java.util.List;

public class GoogleSearchSteps {

    @Given("I am on the Google homepage")
    public void i_am_on_the_google_homepage() {
        // Using the /ncr (No Country Redirect) URL to help bypass some regional consent screens
        Hooks.driver.get("https://www.google.com/ncr");
    }

    @When("I accept any Google consent or cookie prompts")
    public void i_accept_any_google_consent_or_cookie_prompts() {
        // Common XPath for the 'Accept all' or similar consent button
        By consentButtonLocator = By.xpath("//button[div[text()='Accept all']]");

        WebDriverWait wait = new WebDriverWait(Hooks.driver, Duration.ofSeconds(5));

        try {
            WebElement consentButton = wait.until(ExpectedConditions.elementToBeClickable(consentButtonLocator));

            consentButton.click();
            System.out.println("Google consent screen accepted.");

        } catch (Exception e) {
            System.out.println("No Google consent screen found or already dismissed. Continuing test.");
        }
    }

    @When("I search for {string}")
    public void i_search_for(String searchTerm) {
        // Locate the search box using the 'name' attribute which is 'q' on Google
        WebElement searchBox = Hooks.driver.findElement(By.name("q"));
        searchBox.sendKeys(searchTerm);
        searchBox.submit();
    }

    @Then("I should see search results related to {string}")
    public void i_should_see_search_results_related_to(String expectedTerm) {

        // Define the minimum number of matching result links required for the test to pass
        final int MIN_REQUIRED_RESULTS = 5;

        // --- FIX FOR TimeoutException ---
        // 1. Wait for the search input field to contain the expected term.
        // This is a more reliable indicator that the results page has loaded.
        By searchInputLocator = By.name("q");

        WebDriverWait wait = new WebDriverWait(Hooks.driver, Duration.ofSeconds(15)); // Increased wait time to 15s
        wait.until(ExpectedConditions.attributeToBe(searchInputLocator, "value", expectedTerm));

        // 2. Assert that the search input box on the results page contains the expected term.
        WebElement searchInput = Hooks.driver.findElement(searchInputLocator);
        String actualSearchQuery = searchInput.getAttribute("value");

        Assert.assertTrue(
                "Expected search term '" + expectedTerm + "' not found in search input field. Found: " + actualSearchQuery,
                actualSearchQuery.equalsIgnoreCase(expectedTerm)
        );

        // --- Assertion: Verify multiple search result titles contain the term ---

        // Common XPath for search result link titles (h3 tags within the results div with id 'rso')
        By resultLinkLocator = By.xpath("//div[@id='rso']//h3");
        List<WebElement> resultTitles = Hooks.driver.findElements(resultLinkLocator);

        int matchCount = 0;
        // Iterate through the results and count how many titles contain the expected search term
        for (WebElement title : resultTitles) {
            String titleText = title.getText();
            if (!titleText.isEmpty() && titleText.toLowerCase().contains(expectedTerm.toLowerCase())) {
                matchCount++;
            }
        }

        // 3. Assert that the minimum number of relevant results was found.
        Assert.assertTrue(
                "Expected at least " + MIN_REQUIRED_RESULTS + " result links containing '" + expectedTerm +
                        "', but only found " + matchCount + ". Check browser console for network or detection errors.",
                matchCount >= MIN_REQUIRED_RESULTS
        );

        System.out.println("Search successful. Verified " + matchCount + " result links contain the term '" + expectedTerm + "'.");
    }
}