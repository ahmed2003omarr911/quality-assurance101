package com.techspace.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/*
 * Page Object Model for Login Page
 */
public class LoginPage {
    WebDriver driver;

    // ============================================
    // LOCATORS
    // ============================================
    By emailField = By.name("email");
    By passwordField = By.name("password");
    By loginButton = By.xpath("//*[@id=\"root\"]/div/div/div/button");
    By registerLink = By.xpath("//*[@id=\"root\"]/div/div/div/p/a");

    // Error message locators
    By errorMessage = By.xpath("//*[@id=\"root\"]/div/div/div/p[1]");

    // ============================================
    // CONSTRUCTOR
    // ============================================
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // ============================================
    // ACTIONS
    // ============================================

    /*
     * Enter email address
     */
    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    /*
     * Enter password
     */
    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    /*
     * Click login button
     */
    public void clickLoginButton() {
        driver.findElement(loginButton).click();
    }

    /*
     * Navigate to Registration Page
     */
    public void clickRegisterLink() {
        driver.findElement(registerLink).click();
    }

    /*
     * Complete login process with credentials
     */
    public void login(String email, String password) {
        if (email != null && !email.isEmpty()) {
            enterEmail(email);
        }
        if (password != null && !password.isEmpty()) {
            enterPassword(password);
        }
        clickLoginButton();
    }

    // ============================================
    // VERIFICATION METHODS
    // ============================================

    /*
     * Get error message displayed on the page
     */
    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    /*
     * Check if error message is displayed
     */
    public boolean isErrorDisplayed() {
        return driver.findElement(errorMessage).isDisplayed();
    }

    /*
     * Check if user is still on login page (login failed)
     */
    public boolean isOnLoginPage() {
        return driver.findElement(loginButton).isDisplayed();
    }
}
