package com.techspace.tests;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.techspace.pages.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Base Test Class - Contains common setup and teardown for all tests
 * All test classes should extend this class
 */
public class TestBase {

    // WebDriver instance - available to all test classes
    protected WebDriver driver;

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
    public void tearDown(ITestResult result) {
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

            System.out.println("📸 Screenshot saved: " + filePath);
        } catch (IOException e) {
            System.out.println("❌ Failed to save screenshot: " + e.getMessage());
        }
    }

    /*
     * Helper method: Perform login with given credentials
     * This is a reusable method for tests that need authentication
     */
    protected void performLogin(String email, String password) throws InterruptedException {
        homePage.clickLoginButton();
        loginPage.login(email, password);
        Thread.sleep(3000); // Wait for login to complete
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
}
