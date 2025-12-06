package com.techspace.tests;

/*
 * Centralized test data for all test cases
 * Update values here to change test data across all tests
 */
public class TestData {

    // ============================================
    // WEBSITE URL
    // ============================================
    public static final String BASE_URL = "https://techspace-pi.vercel.app";

    // ============================================
    // USER CREDENTIALS
    // ============================================
    // Existing user
    public static final String LOGIN_EMAIL = "jim@gmail.com";
    public static final String LOGIN_PASSWORD = "123";

    // Another existing user
    public static final String USER2_EMAIL = "abdo@gmail.com";
    public static final String USER2_PASSWORD = "123";

    // New user for registration test
    public static final String NEW_USER_FIRST_NAME = "Maro";
    public static final String NEW_USER_LAST_NAME = "Adel";
    public static final String NEW_USER_EMAIL = "maro@gmail.com";
    public static final String NEW_USER_PASSWORD = "123";

    // ============================================
    // PRODUCT DETAILS
    // ============================================
    public static final String PRODUCT_NAME = "ASUS Laptop";

    // ============================================
    // DELIVERY INFORMATION
    // ============================================
    public static final String DELIVERY_ADDRESS = "El-Sayeda Zainab";

    // ============================================
    // EXPECTED MESSAGES
    // ============================================
    public static final String ORDER_SUCCESS_MESSAGE = "Order Successful!";
    public static final String ORDERS_PAGE_HEADING = "My Orders";

    // ============================================
    // ERROR MESSAGES  - REGISTRATION
    // ============================================
    public static final String ERROR_USER_EXISTS = "User already exists!";
    public static final String ERROR_CHECK_DATA = "Check Submitted Data!";

    // ============================================
    // ERROR MESSAGES - LOGIN
    // ============================================
    public static final String ERROR_INCORRECT_CREDENTIALS = "Incorrect Email or Password!";
    public static final String ERROR_EMPTY_CREDENTIALS = "Check Submitted Data!";
}
