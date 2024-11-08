 package org.example;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class AmazonGridTest {

    public static void main(String[] args) {
        try {
            // Run the test on Chrome
            System.out.println("Running test on Chrome...");
            runTest("chrome");

            // Run the test on Edge
            System.out.println("Running test on Edge...");
            runTest("MicrosoftEdge");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void runTest(String browser) throws MalformedURLException {
        // Define the Selenium Grid hub URL
        String hubURL = "http://localhost:4444"; // Update if your hub is hosted on a different machine

        // Define desired capabilities for the specific browser
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(browser);

        // Create a RemoteWebDriver instance connected to the Grid hub
        WebDriver driver = new RemoteWebDriver(new URL(hubURL), capabilities);
        driver.manage().deleteAllCookies(); // Clear cookies before starting the test

        try {
            driver.get("https://www.amazon.com/");
            System.out.println("Navigated to Amazon on " + browser);

            // Wait for the CAPTCHA to be solved manually
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("twotabsearchtextbox")));
            System.out.println("CAPTCHA solved. Continuing with the test on " + browser);

            // Fluent wait for the search box
            FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(30))
                    .pollingEvery(Duration.ofSeconds(5))
                    .ignoring(NoSuchElementException.class);
            WebElement searchbox = driver.findElement(By.id("twotabsearchtextbox"));
            searchbox.sendKeys("laptop");
            System.out.println("Typed 'laptop' into the search box on " + browser);

            searchbox.submit();
            System.out.println("Submitted the search form.");

            // Wait for search results to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.s-main-slot")));
            System.out.println("Search results are loaded on " + browser);

            // Get the list of products and select a random one
            List<WebElement> products = driver.findElements(By.cssSelector("div.s-main-slot div.s-result-item"));
            Random random = new Random();
            int randomIndex = random.nextInt(products.size());
            WebElement randomProduct = products.get(randomIndex);
            randomProduct.findElement(By.cssSelector("h2 a")).click();
            System.out.println("Clicked on a random product on " + browser);

            // Wait for the product details page to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("wishListMainButton")));

            // Click the 'Add to List' button
            WebElement addToListButton = driver.findElement(By.id("wishListMainButton"));
            addToListButton.click();
            System.out.println("Added the product to the list on " + browser);

            // Sign in
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ap_email")));
            WebElement signin = driver.findElement(By.id("ap_email"));
            signin.sendKeys("nyiringangokevin4@gmail.com");  // Replace with your Amazon email
            signin.submit();
            System.out.println("Email entered on " + browser);

            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ap_password")));
            WebElement password = driver.findElement(By.id("ap_password"));
            password.sendKeys("Nyiringango2000?");  // Replace with your Amazon password
            password.submit();
            System.out.println("Password entered on " + browser);

            // Change the delivery location
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("glow-ingress-block")));
            driver.findElement(By.id("glow-ingress-block")).click();
            System.out.println("Clicked on change location on " + browser);

            // Select country
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("GLUXCountryListDropdown")));
            driver.findElement(By.id("GLUXCountryListDropdown")).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("GLUXCountryList_87")));
            driver.findElement(By.id("GLUXCountryList_87")).click();
            System.out.println("Selected country on " + browser);

            // Confirm location selection
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[name='glowDoneButton']")));
            driver.findElement(By.cssSelector("button[name='glowDoneButton']")).click();
            System.out.println("Clicked Done on " + browser);

            // Select quantity as 3
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("quantity")));
            Select quantityDropdown = new Select(driver.findElement(By.id("quantity")));
            quantityDropdown.selectByValue("3");
            System.out.println("Selected quantity as 3 on " + browser);

            // Add to cart
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add-to-cart-button")));
            driver.findElement(By.id("add-to-cart-button")).click();
            System.out.println("Added to cart on " + browser);

        } catch (Exception e) {
            System.out.println("An error occurred on " + browser + ": " + e.getMessage());
        } finally {
            // Close the browser
            driver.quit();
            System.out.println("Browser closed on " + browser);
        }
    }
}
