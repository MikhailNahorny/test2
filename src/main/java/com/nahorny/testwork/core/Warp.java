package com.nahorny.testwork.core;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;


public class Warp {
    /**
     * this class contains useful static methods for convenient handling different element/actions
     * be attentive: some method contains assertions, other ones return value.
     */
    private static Logger logger = Logger.getLogger();
    private static WebDriver driver;

    /**
     * doBefore() always to be called the first line in methods where the driver field is used set the correct value
     * to avoid org.openqa.selenium.NoSuchSessionException: Session ID is null. Using WebDriver after calling quit()?
     * moved to a separate method because when you switch selenide/selenium, the method of obtaining driver will change
     */
    private static void doBefore() {
//        driver = WebDriverRunner.getWebDriver(); //for Selenide
        driver = Driver.getDriver(); //for Selenium
    }

    public static void setDriver(final WebDriver driver) {
        Warp.driver = driver;
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void refreshPage() {
        doBefore();
        String url = driver.getCurrentUrl();
        driver.get(url);
    }

    public static void delay(int sec) {
        logger.log(sec + " seconds delay start");
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException ignored) {
        }
    }

    public static void delayShort(int millis) {
        logger.log(millis + " millis delay start");
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    public static void waitForPageLoading() {
        doBefore();
        Wait<WebDriver> wait = new WebDriverWait(driver, Long.parseLong(Properties.get("timeout")));
        wait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
    }

    public static void waitForInvisibilityOfElement(String path){
        doBefore();
        Wait<WebDriver> wait = new WebDriverWait(driver, Long.parseLong(Properties.get("timeout")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(path)));
    }

    //клики по элементам, скроллинг-----------------------------------------------------------------------------------

    public static void clickUntouchable(WebElement el, int xOffset, int yOffset) {
        //для псевдоэлементов /before/after
        doBefore();
        Actions action = new Actions(driver);
        action.moveToElement(el).moveByOffset(xOffset, yOffset).click().build().perform();
    }

    public static String[] openNewTab(String path) {
        /*
        передаем в метод путь на элемент, который откроет новую вкладку по клику
        метод откроет новую вкладку, переключит на нее фокус
        вернет массив [старая, новая] вкладки
        */
        return openNewTab(driver.findElement(By.xpath(path)));
    }

    public static String[] openNewTab(By locator) {
        return openNewTab(driver.findElement(locator));
    }

    public static String[] openNewTab(WebElement e) {
        doBefore();
        // можно проверять сразу, собирается ли что-то открываться в новой вкладке или нет, но в таком случае мотод съест только ссылку, а у нас может быть что-то вложенное в ссылку млм обернутое вокруг
        //Assert.assertTrue(e.getAttribute("target").contains("blank"));
        String startTab = driver.getWindowHandle();//сохраним первоначальную вкладку. она имеет право быть не_единственной / не_первой
        String newTab = null;//обявим переменную под вкладку, которая будет открыта по клику на элемент
        Set<String> oldTabs = driver.getWindowHandles();//возьмем сет всех вкладок открытых до
        e.click();//кликнем
        Set<String> newTabs = driver.getWindowHandles();//возьмем сет всех вкладок открытых после
        if (newTabs.size() != oldTabs.size() + 1) {
            Assert.fail("MORE THAN ONE TAB WAS OPENED");//ожидаем, что вкладок стало на одну больше
        }
        for (String tab : newTabs) {
            newTab = tab;//нашли вкладку которой раньше не было
            if (!oldTabs.contains(newTab)) {
                break;
            }
        }
        driver.switchTo().window(newTab);//переключились на новую вкладку
        return new String[]{startTab, newTab};
    }

    public static void clickUntouchable(WebElement el, int xOffset, int yOffset, Boolean test) {
        //для псевдоэлементов /before/after пристрелка
        doBefore();
        Actions action = new Actions(driver);
        action.moveToElement(el).moveByOffset(xOffset, yOffset).contextClick().build().perform();
    }

    public static void clickSimple(String path) {
        doBefore();
        (driver.findElement(By.xpath(path))).click();
    }

    public static void clickSimpleIfPresent(String path) {
        doBefore();
        if (isElementPresent(By.xpath(path))) (driver.findElement(By.xpath(path))).click();
    }

    public static void scrollToElement(WebElement e) {
        doBefore();
        Actions action = new Actions(driver);
        action.moveToElement(e).build().perform();
    }

    public static void scrollToElement(By locator) {
        doBefore();
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement(locator)).build().perform();
    }

    public static void scrollToElement(String path) {
        doBefore();
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement(By.xpath(path))).build().perform();
    }

    public static void scrollToElement3(WebElement element) {
        doBefore();
        int y = element.getLocation().getY();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0," + y + ")");
    }

    public static void scrollToElement2(By locator) {
        doBefore();
        WebElement element = driver.findElement(locator);
        int y = element.getLocation().getY();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0," + y + ")");
    }

    public static void scrollToBottom() {
        doBefore();
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    //проверки ссылок-----------------------------------------------------------------------------------
    public static void checkLinkThisTab(WebElement e, String href, boolean debug) {
        //для отладки
        if (debug) {
            System.out.println("href - " + (e.getAttribute("href").equals(href) ? true : e.getAttribute("href")));
            System.out.println("isDisplayed - " + e.isDisplayed());
            System.out.println("isEnabled - " + e.isEnabled());
            System.out.println("!target:blank - " + !e.getAttribute("target").contains("blank"));
        }
        checkLinkThisTab(e, href);
    }

    public static void checkLinkThisTab(WebElement e, String href) {
        Assertions.mouseIsPointer(e);
        Assert.assertTrue(e.getAttribute("href").equals(href) && e.isDisplayed() && e.isEnabled() && !e.getAttribute("target").contains("blank"));
    }

    public static void checkLinkNewTab(WebElement e, String href) {
        Assertions.mouseIsPointer(e);
        Assert.assertTrue(e.getAttribute("href").equals(href) && e.isDisplayed() && e.isEnabled() && e.getAttribute("target").contains("blank"));
    }

    public static void checkLinkNewTab(WebElement e, String href, boolean debug) {
        if (debug) {
            System.out.println("href - " + (e.getAttribute("href").equals(href) ? true : e.getAttribute("href")));
            System.out.println("isDisplayed - " + e.isDisplayed());
            System.out.println("isEnabled - " + e.isEnabled());
            System.out.println("!target:blank - " + e.getAttribute("target").contains("blank"));
        }
        checkLinkNewTab(e, href);
    }

    public static void checkLinkThisTab(String path, String href) {
        WebElement e = driver.findElement(By.xpath(path));
        Assertions.mouseIsPointer(e);
        Assert.assertTrue(e.isDisplayed() && e.isEnabled());
        Assert.assertEquals(e.getAttribute("href"), href);
    }

    public static boolean checkLinkPart(WebElement e, String partOfHref) {
        Assertions.mouseIsPointer(e);
        return e.getAttribute("href").contains(partOfHref) && e.isDisplayed() && e.isEnabled();
    }

    //проверки ховер-эффектов, мышь-указатель-----------------------------------------------------------------------------------

//    public static void mouseIsPointer(By locator) {
//        Assert.assertTrue(driver.findElement(locator).hover().getCssValue("cursor").equalsIgnoreCase("pointer"));
//    }
//
//    public static void mouseIsPointer(WebElement e) {
//        Assert.assertTrue(e.hover().getCssValue("cursor").equalsIgnoreCase("pointer"));
//    }
//
//    public static void mouseIsPointer(String path) {
//        Assert.assertTrue(driver.findElement(By.xpath(path)).hover().getCssValue("cursor").equalsIgnoreCase("pointer"));
//    }

    //получение каких-либо данных-----------------------------------------------------------------------------------

    public static int randomInt(int max) {
        return (int) (Math.random() * max);//random [0, max)
    }

    public static String tabTitle() {
        doBefore();
        return driver.getTitle();
    }

    public static int tabsCount() {
        doBefore();
        Set<String> tabs = driver.getWindowHandles();
        return tabs.size();
    }

    public static String currentURL() {
        doBefore();
        return driver.getCurrentUrl();
    }

    public static String curWindow() {
        doBefore();
        return driver.getWindowHandle();
    }

    public static String[] titlesCollection(List<WebElement> c) {
        String[] res = new String[c.size()];
        for (int i = 0; i < c.size(); i++) {
            res[i] = c.get(i).getAttribute("innerText");
        }
        return res;
    }


    public static String justOpenedTabGetUrlAndClose(WebElement e) {
        doBefore();
        String startTab = driver.getWindowHandle();//сохраним первоначальную вкладку. она имеет право быть не_единственной / не_первой
        String newTab = null;//обявим переменную под вкладку, которая будет открыта по клику на элемент
        Set<String> oldTabs = driver.getWindowHandles();//возьмем сет всех вкладок открытых до
        e.click();//кликнем
        Set<String> newTabs = driver.getWindowHandles();//возьмем сет всех вкладок открытых после
        if (newTabs.size() != oldTabs.size() + 1) {
            Assert.fail("MORE THAN ONE TAB WAS OPENED");//ожидаем, что вкладок стало на одну больше
        }
        for (String tab : newTabs) {
            newTab = tab;//нашли вкладку которой раньше не было
            if (!oldTabs.contains(newTab)) {
                break;
            }
        }
        driver.switchTo().window(newTab);//переключились на новую вкладку
        String result = driver.getCurrentUrl();//взяли УРЛ
        driver.close();
        driver.switchTo().window(startTab);
        return result;
    }//target="blank", если ходить по вкладкам неудобно

    public static By xp(String path) {
        doBefore();
        return By.xpath(path);
    }

    public static WebElement el(String path) {
        doBefore();
        return driver.findElement(By.xpath(path));
    }

    public static List<WebElement> els(String path) {
        doBefore();
        return driver.findElements(By.xpath(path));
    }

    //проверка элемента на присутствие/кликабельность/видимость-----------------------------------------------------------------------------------

    public static boolean elementIsIntractable(WebElement e) {
        doBefore();
        return e.isDisplayed() && e.isEnabled();
    }

    public static boolean isElementPresent(By locator) {
        //https://stackoverflow.com/questions/12270092/best-way-to-check-that-element-is-not-present-using-selenium-webdriver-with-java
        doBefore();
        try {
            driver.findElement(locator);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public static int isElementPresent(String path) {
        //https://stackoverflow.com/questions/12270092/best-way-to-check-that-element-is-not-present-using-selenium-webdriver-with-java
        doBefore();
        List<WebElement> ec = driver.findElements(By.xpath(path));
        return ec.size();
    }

    public static void ElementVisible(String path) {
        doBefore();
        WebElement e = driver.findElement(By.xpath(path));
        e.isDisplayed();
    }

    //файлы---------------------------------------------------------------------------------------------------------------

    public static boolean isFileDownloaded(String downloadPath, String fileName) {
        //https://stackoverflow.com/questions/30726126/detecting-a-file-downloaded-in-selenium-java
        //com.andersenlab.autotests.core.configs.DriverConfiguration.defaultRemote()
        File dir = new File(Warp.pathDownload());
        File[] dirContents = dir.listFiles();
        for (File dirContent : dirContents) {
            if (dirContent.getName().equals(fileName)) {
                dirContent.delete();
                return true;
            }
        }
        return false;
    }

    public static String pathDownload() {
        //https://github.com/SeleniumHQ/selenium/issues/5292
        final String path = Paths.get(Properties.get("tmp.dir")).toAbsolutePath().toString();
        return path;
    }

    //разное-----------------------------------------------------------------------------------

    public static void slide(WebElement drag, WebElement drop) {
        doBefore();
        Actions slide = new Actions(driver);
        slide.dragAndDrop(drag, drop).build().perform();
    }

    //непроверенные/неотлаженные методы-----------------------------------------------------------------------------------

    public static String allCss(By locator) {
        //https://stackoverflow.com/questions/32537339/getting-the-values-of-all-the-css-properties-of-a-selected-element-in-selenium
        doBefore();
        WebElement we = driver.findElement(locator);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        String script = "var s = '';" +
                "var o = getComputedStyle(arguments[0]);" +
                "for(var i = 0; i < o.length; i++){" +
                "s+=o[i] + ':' + o.getPropertyValue(o[i])+';';}" +
                "return s;";

        return "" + executor.executeScript(script, we);
    }

    public static void scrollToElementByOffset(WebElement e, int y) {
        doBefore();//при попытке проскроллиться ниже элемента дает исключение
        Actions action = new Actions(driver);
        action.moveToElement(e).moveByOffset(0, y).build().perform();
    }

}
