package com.nahorny.testwork.core;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import static org.testng.Assert.assertTrue;

public class Assertions {
    /**
     * this class encapsulates assertions
     */
    private static WebDriver driver = Driver.getDriver();

    @Step("Assert: element should have href value")
    public static void elementHaveHref(By locator, String hrefValue) {
        WebElement e = driver.findElement(locator);
        Assert.assertEquals(e.getAttribute("href"), hrefValue);
    }

    @Step("Assert: element should have text")
    public static void elementShouldHaveText(By locator, String text) {
        WebElement e = driver.findElement(locator);
        Assert.assertEquals(e.getAttribute("innerText"), text);
    }

    @Step("Assert: viewport size")
    public static void assertViewportSize(String expected) {
        String js = "return ('('+Math.max(document.documentElement.clientWidth, window.innerWidth || 0) +', '" +
                "+ Math.max(document.documentElement.clientHeight, window.innerHeight || 0 ) +')');";
        Assert.assertEquals(((JavascriptExecutor)driver).executeScript(js), expected);
    }

    @Step("Assert: browser size")
    public static void assertBrowserSize(String expected) {
        Assert.assertEquals(driver.manage().window().getSize().toString(), expected);
    }

    @Step("Assert: element is visible")
    public static void elementVisible(By locator) {
        Assert.assertTrue(driver.findElement(locator).isDisplayed());
    }

    @Step("Assert: url is contains")
    public static void assertCurrentUrlContains(String urlContains) {
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains(urlContains), currentUrl + " does not contains " + urlContains);
    }

    @Step("Assert: element is not visible")
    public static void elementNotVisible(By locator) {
        Assert.assertFalse(driver.findElement(locator).isDisplayed());
    }

    @Step("Assert: mouse become a pointer after hovering")
    public static void mouseIsPointer(By locator) {
        WebElement e = driver.findElement(locator);
        Actions actions = new Actions(driver);
        Action action = actions.moveToElement(e).build();
        action.perform();
        Assert.assertTrue(e.getCssValue("cursor").equalsIgnoreCase("pointer"));
    }

    @Step("Assert: mouse becomes a pointer after hovering")
    public static void mouseIsPointer(WebElement e) {
        Actions actions = new Actions(driver);
        Action action = actions.moveToElement(e).build();
        action.perform();
        Assert.assertTrue(e.getCssValue("cursor").equalsIgnoreCase("pointer"));
    }

    @Step("Assert: mouse not becomes a pointer after hovering")
    public static void mouseIsNotPointer(WebElement e) {
        Actions actions = new Actions(driver);
        Action action = actions.moveToElement(e).build();
        action.perform();
        Assert.assertFalse(e.getCssValue("cursor").equalsIgnoreCase("pointer"));
    }
}
