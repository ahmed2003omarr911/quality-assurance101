package com.techspace.tests;

import org.openqa.selenium.WebDriver;
import com.techspace.pages.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

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

//        System.out.println("✓ Browser started and navigated to: " + TestData.BASE_URL);
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
    public void tearDown() {
        if (driver != null) {
            driver.quit();
//            System.out.println("✓ Browser closed");
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
     * Helper method: Perform login with default test user
     */
    protected void performDefaultLogin() throws InterruptedException {
        performLogin(TestData.LOGIN_EMAIL, TestData.LOGIN_PASSWORD);
    }
}
