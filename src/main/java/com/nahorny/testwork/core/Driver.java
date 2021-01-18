package com.nahorny.testwork.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class Driver {
    /**
     * this class produces an instance of WebDriver, set DesiredCapabilities, and implicitlyWait.
     * required for DriverConfiguration class
     * implemented singleton pattern with double checked lock
     */
    private static volatile WebDriver driver;

    private Driver() {
        if (driver != null) throw new RuntimeException();
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            synchronized (Driver.class) {
                if (driver == null) driver = driver();
            }
        }
        return driver;
    }

    private static WebDriver driver() {
        String binPath = Paths.get("bin/chromedriver.exe").toAbsolutePath().toString();
        System.setProperty("webdriver.chrome.driver", binPath);
        WebDriver driver = new ChromeDriver(DriverConfiguration.getCapabilities());
        driver.manage().timeouts().implicitlyWait(Long.parseLong(Properties.get("timeout")), TimeUnit.SECONDS);
        Logger.getLogger().log("driver start");
        return driver;
    }
}


