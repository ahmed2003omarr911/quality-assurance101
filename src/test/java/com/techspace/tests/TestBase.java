package com.techspace.tests;

import org.openqa.selenium.*;
import com.techspace.pages.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

/*
 * Base Test Class - Contains common setup and teardown for all tests
 * All test classes should extend this class
 */
public class TestBase {

    // WebDriver instance - available to all test classes
    protected WebDriver driver;

    // WebDriverWait instance - available to all test classes
    protected WebDriverWait wait;

    // Standard timeout durations
    protected static final int DEFAULT_TIMEOUT = 10; // seconds
    protected static final int LONG_TIMEOUT = 15; // seconds
    protected static final int SHORT_TIMEOUT = 5; // seconds

    // Page Objects - available to all test classes
    protected HomePage homePage;
    protected LoginPage loginPage;
    protected RegisterPage registerPage;
    protected CartPage cartPage;
    protected CheckoutPage checkoutPage;
    protected OrderSuccessPage orderSuccessPage;
    protected OrdersPage ordersPage;

    /*
     * Runs before each test method
     * Initializes the browser and page objects
     */
    @BeforeMethod
    public void setUp() {
        // Initialize Chrome browser
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Set implicit wait as fallback
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // Initialize explicit wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));

        // Navigate to the website
        driver.get(TestData.BASE_URL);

        // Initialize all page objects
        initializePages();
    }

    /*
     * Initialize all page objects
     */
    private void initializePages() {
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        registerPage = new RegisterPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        orderSuccessPage = new OrderSuccessPage(driver);
        ordersPage = new OrdersPage(driver);
    }

    /*
     * Runs after each test method
     * Closes the browser
     */
    @AfterMethod
    public void tearDown(ITestResult result) throws InterruptedException {
        // Check if test failed
        if (result.getStatus() == ITestResult.FAILURE) {
            // Take screenshot
            takeScreenshot(result.getName());
        }

        // Close browser
        if (driver != null) {
            driver.quit();
        }
    }

    /*
     * Helper Method: Take screenshot with timestamp
     * Saves screenshot to screenshots/ folder with test name and timestamp
     */
    private void takeScreenshot(String testName) {
        try {
            // Create screenshots directory if it doesn't exist
            File screenshotsDir = new File("screenshots");
            if (!screenshotsDir.exists()) {
                screenshotsDir.mkdirs();
            }

            // Generate timestamp for unique filename
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String fileName = testName + "_FAILED_" + timestamp + ".png";
            String filePath = "screenshots/" + fileName;

            // Take screenshot
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destinationFile = new File(filePath);

            // Save screenshot
            FileHandler.copy(sourceFile, destinationFile);

            System.out.println("Screenshot saved: " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }
    }

    // ============================================
    // EXPLICIT WAIT HELPER METHODS
    // ============================================

    /*
     * Wait for element to be clickable
     */
    protected WebElement waitForElementToBeClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /*
     * Wait for element to be clickable with custom timeout
     */
    protected WebElement waitForElementToBeClickable(By locator, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /*
     * Wait for element to be visible
     */
    protected WebElement waitForElementToBeVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /*
     * Wait for element to be visible with custom timeout
     */
    protected WebElement waitForElementToBeVisible(By locator, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /*
     * Wait for element to be present in DOM
     */
    protected WebElement waitForElementToBePresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /*
     * Wait for URL to contain specific text
     */
    protected boolean waitForUrlContains(String urlFragment) {
        return wait.until(ExpectedConditions.urlContains(urlFragment));
    }

    /*
     * Wait for URL to contain specific text with custom timeout
     */
    protected boolean waitForUrlContains(String urlFragment, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.urlContains(urlFragment));
    }

    /*
     * Wait for text to be present in element
     */
    protected boolean waitForTextToBePresentInElement(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /*
     * Wait for element to be invisible
     */
    protected boolean waitForElementToBeInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /*
     * Wait for page to load completely (document.readyState = complete)
     */
    protected void waitForPageToLoad() {
        wait.until(driver -> {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            return js.executeScript("return document.readyState").equals("complete");
        });
    }

    /*
     * Wait for AJAX/jQuery requests to complete (if site uses jQuery)
     */
    protected void waitForAjaxToComplete() {
        wait.until(driver -> {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            return (Boolean) js.executeScript("return jQuery.active == 0");
        });
    }

    /*
     * Helper method: Perform login with given credentials
     * This is a reusable method for tests that need authentication
     */
    protected void performLogin(String email, String password) {
        homePage.clickLoginButton();
        waitForPageToLoad();
        loginPage.login(email, password);
        waitForPageToLoad();
        System.out.println("✓ Logged in as: " + email);
    }

    /*
     * Helper method: Get authentication token from localStorage
     * Returns empty string if token doesn't exist
     */
    protected String getLocalStorageToken() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String tokenKey = "token";
        Object token = js.executeScript("return localStorage.getItem('" + tokenKey + "');");
        if (token != null && !token.toString().equals("null")) {
            return token.toString();
        }
        return "";
    }

    /*
     * Helper method: Clearing Cart for the next test case
     * This is a reusable method for cleaning the cart before tearing down
     */
    protected void clearCart() {
        // Navigate to the website
        driver.get(TestData.BASE_URL);
        waitForPageToLoad();

        // Navigate to cart page
        homePage.clickCartIcon();
        waitForPageToLoad();

        // clear the cart
        cartPage.clickClearCartButton();
    }
}
