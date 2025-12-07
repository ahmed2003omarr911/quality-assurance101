package com.techspace.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/*
 * Page Object Model for Login Page
 */
public class LoginPage {
    WebDriver driver;
    WebDriverWait wait;

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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ============================================
    // ACTIONS
    // ============================================

    /*
     * Enter email address
     */
    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(email);
    }

    /*
     * Enter password
     */
    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(password);
    }

    /*
     * Click login button
     */
    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    /*
     * Navigate to Registration Page
     */
    public void clickRegisterLink() {
        wait.until(ExpectedConditions.elementToBeClickable(registerLink)).click();
        wait.until(ExpectedConditions.urlContains("/register"));
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
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }

    /*
     * Check if user is still on login page (login failed)
     */
    public boolean isOnLoginPage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton)).isDisplayed();
    }
}
