/*
FIXME unsolved problem
is to find element contains '&zwnj;' or '\u200C' or U+200C zero width non-joiner Unicode code point
xpath like //a[text()='\u200C'] or //a[contains(., '\u200C')] not work
due to this problem, HashMap<String tagName, WebElement tagButton> could not be used
https://www.fileformat.info/info/unicode/char/200c/index.htm
https://stackoverflow.com/questions/247135/using-xpath-to-search-text-containing-nbsp
 */

package com.nahorny.testwork.tags.page;

import com.nahorny.testwork.core.Logger;
import com.nahorny.testwork.core.Warp;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class HomePage {
    private final String POPULAR_TAGS = "//a[contains(@class, 'tag-default')]";

    private final String ARTS = "(//div[@class='article-preview'])";
    private final String PAGES = "//ul[@class='pagination']//a[contains(@class, 'page-link')]";
    private final String LOADING = "//article-list/div[contains(text(), 'Loading articles...')]";
    private String tagExpected;

    private final String[] tagNames = {
            "‌",
            "‌‌",
            "‌‌‌",
            "‌‌‌‌",
            "‌‌‌‌‌‌‌",
            "‌‌‌‌‌‌‌‌",
            "‌‌‌‌‌",
            "‌‌‌‌‌‌‌‌‌‌",
            "‌‌‌‌‌‌‌‌‌‌‌",
            "‌‌‌‌‌‌",
            "HuManIty",
            "Hu‌Man‌Ity",
            "Gandhi",
            "HITLER",
            "SIDA",
            "BlackLivesMatter",
            "Black‌Lives‌Matter",
            "test",
            "dragons",
            "butt"
    };

    private WebElement[] tags;

    private int pagesQuantity;
    private final int CURRENT_PAGE_START_VALUE = 0;
    private int currentPage = CURRENT_PAGE_START_VALUE;

    public HomePage() {
        tags = getTags();
    }

    //contract-------------------------------------------------------------------------------------------------------

    public void checkAllTags() {
        for (int i = 0; i < tagNames.length; i++) {
            checkTag(i);
        }
    }

    public void checkTag(final int ordinal) {
        applyTag(ordinal);
        while (isNextPagePresent()) {
            checkPageAtOnce();
        }
        currentPage = CURRENT_PAGE_START_VALUE;
    }

    //contract-------------------------------------------------------------------------------------------------------

    private void checkPageAtOnce() {
        String taggedArts = "//ul[@class='tag-list']//li[contains(text(), '" + tagExpected + "')]";
        Warp.waitForInvisibilityOfElement(LOADING);
        List<WebElement> arts = Warp.els(ARTS);
        List<WebElement> artsTagged = Warp.els(taggedArts);
        Assert.assertTrue(arts.size() <= artsTagged.size()); //due to '&zwnj;' problem
    }

    private WebElement[] getTags() {
        List<WebElement> els = Warp.els(POPULAR_TAGS);
        WebElement[] res = new WebElement[els.size()];
        Logger.getLogger().log("getTags called. tags quantity: " + els.size());
        return els.toArray(res);
    }

    private String applyTag(final int ordinal) {
        tags[ordinal].click();
        pagesQuantity = pagesQuantity();
        tagExpected = tagNames[ordinal];
        return tagNames[ordinal];
    }

    private int pagesQuantity() {
        return Warp.els(PAGES).size();
    }

    private boolean isNextPagePresent() {
        if (currentPage < pagesQuantity) {
            String nextPage = "(".concat(PAGES).concat(")[").concat(String.valueOf(++currentPage)).concat("]");
            Warp.scrollToBottom();
            Warp.clickSimple(nextPage);
            return true;
        } else {
            return false;
        }
    }
}