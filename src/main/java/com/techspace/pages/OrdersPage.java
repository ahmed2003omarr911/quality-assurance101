package com.techspace.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/*
 * Page Object Model for Orders Page
 */
public class OrdersPage {
    WebDriver driver;

    // ============================================
    // LOCATORS
    // ============================================
    By pageHeading = By.cssSelector("#root > div > p");
    By orderLocator = By.className("css-1jhqtcx");

    // ============================================
    // CONSTRUCTOR
    // ============================================
    public OrdersPage(WebDriver driver) {
        this.driver = driver;
    }

    // ============================================
    // ACTIONS
    // ============================================

    /*
     * Get page heading text
     */
    public String getPageHeading() {
        return driver.findElement(pageHeading).getText();
    }

    public int calculateNumberOfOrders() {
        int numberOfOrders = driver.findElements(orderLocator).size();
        if (numberOfOrders > 0) {
            System.out.println("Number of orders: " + numberOfOrders);
        } else {
            System.out.println("No orders found on the page!");
        }
        return numberOfOrders;
    }
}
