package com.nahorny.testwork.core;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

class DriverConfiguration {
    /**
     * this class produces DesiredCapabilities
     */
    static DesiredCapabilities getCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();
        if(Properties.get("with.head").equals("true")){
            options.addArguments("start-maximized");
        } else {
            options.addArguments("--headless");
        }
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        Logger.getLogger().log("capabilities set");
        return capabilities;
    }
}

