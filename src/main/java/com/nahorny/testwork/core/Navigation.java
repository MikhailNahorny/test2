package com.nahorny.testwork.core;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import java.util.HashMap;

import static com.nahorny.testwork.core.Pages.*;

public class Navigation {
    /**
     * this class used to get the page to be tested
     * as a rule, URL consists of constant host and path
     * so let's put paths into map<page, path> where the page an enum constant, and the path is a String
     */
    private static WebDriver driver = Driver.getDriver();

    @Step
    public static void gotoPage(String link) {
        driver.get(link);
    }

    @Step
    public static void gotoPage(final Pages page) {
        HashMap<Pages, String> pages = new HashMap<>();
        pages.put(HOMEPAGE, "");
//        pages.put(ANOTHER_HOMEPAGE, "/another/page/path");

        driver.get(Properties.get("url") + (pages.get(page)));
    }
}
