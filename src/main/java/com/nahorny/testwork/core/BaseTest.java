package com.nahorny.testwork.core;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {
    /**
     * this class describe before/after method/class
     * all test classes have to extend it
     */
    protected WebDriver driver;

    @BeforeClass(alwaysRun = true)
    protected void beforeClass() {
        driver = Driver.getDriver();
    }

    @BeforeMethod(alwaysRun = true)
    protected void beforeMethod() {
        Navigation.gotoPage(Pages.HOMEPAGE);
    }

    @AfterMethod(alwaysRun = true)
    protected void afterMethod() {
    }

    @AfterClass(alwaysRun = true)
    protected void afterClass() {
        driver.quit();
    }


}
