package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.ElementClickInterceptedException;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class amazon {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Kevin\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        try {
            // Open Amazon Germany's website
            driver.get("https://www.amazon.de/");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(200));  // Timeout increased for waits

            // Login process
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-link-accountList")));
            signInButton.click();

            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ap_email")));
            emailField.sendKeys("nyiringangokevin4@gmail.com");

            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("continue")));
            continueButton.click();

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ap_password")));
            passwordField.sendKeys("Nyiringango2000?");

            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("signInSubmit")));
            loginButton.click();

            // Wait for the page to load after login
            wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-link-accountList")));

            // Go back to the main page to search for skateboards
            driver.get("https://www.amazon.de/");

            // Explicit wait for the search box to be visible and interactable
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("twotabsearchtextbox")));

            // Enter "skateboard" in the search box and submit
            searchBox.sendKeys("skateboard");
            searchBox.submit();

            // Wait for the search results to load
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".s-main-slot .s-result-item")));

            // Find all skateboard listings on the search results page
            List<WebElement> skateboards = driver.findElements(By.cssSelector(".s-main-slot .s-result-item"));

            if (!skateboards.isEmpty()) {
                // Choose a random skateboard from the results
                Random rand = new Random();
                int randomIndex = rand.nextInt(skateboards.size());
                WebElement randomSkateboard = skateboards.get(randomIndex);

                // Scroll to the skateboard to ensure it is in view
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", randomSkateboard);

                // Retry the click if it is intercepted
                try {
                    randomSkateboard.findElement(By.cssSelector("a")).click();
                } catch (ElementClickInterceptedException e) {
                    // If click is intercepted, wait and try again
                    WebElement overlay = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-hamburger-menu")));
                    overlay.click();
                    randomSkateboard.findElement(By.cssSelector("a")).click();
                }

                // Wait for the skateboard page to load
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-to-cart-button")));

                // Add skateboard to cart
                WebElement addToCartButton = driver.findElement(By.id("add-to-cart-button"));

                // Ensure the button is clickable
                wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
                addToCartButton.click();

                // Wait for a few seconds to confirm the skateboard is added to the cart
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-cart")));
                System.out.println("Skateboard added to cart successfully!");
            } else {
                System.out.println("No skateboards found in search results.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit(); // Close the browser
        }
    }
}
