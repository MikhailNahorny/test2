package com.nahorny.testwork.tags.art;

import com.nahorny.testwork.core.Warp;
import org.testng.Assert;

public class Article {
    /**
     * This class describes article contains tags.
     * also articles contains header, text, date , author, and favorite/read more buttons. but we need only tags to test filter
     *
     * strategy was changed, and this class not needed to test tag filter
     * but it's an example shows what way we could reuse this code
     */
    private static String tagExpected;

    String BASE;
    String TAGS_LIST;
    String TAGS;

    public Article(String BASE) {
        // we need to get BASE and count all paths each time dynamically to definite articles.
        // according to the fact, that page contains articles, producing the right path is the responsibility of Page-class.
        this.BASE = BASE;
        calc();
    }

    private void calc(){
        //dynamic calculating
        TAGS_LIST = BASE + "//ul";
        TAGS = TAGS_LIST +  "//li";
    }

    public static void setTagExpected(String tagExpected) {
        //it is obvious that there are many more articles than tags,
        // therefore the tag is not passed to the constructor, but is set by a setter in a static field
        Article.tagExpected = tagExpected;
    }

    public static String getTagExpected() {
        return tagExpected;
    }

    public void checkTag(){
        Warp.scrollToElement(BASE); //scroll to current article to avoid intercepted exception
        Warp.delayShort(700);//wait for scroll execution
        int expectedTagsCount = (int) Warp.els(TAGS).stream().map(e -> e.getAttribute("innerText")).filter(s -> s.equals(tagExpected)).count();//get current art's tags, get test for each, filter tagExpected-text, get count
        System.out.println(TAGS);
        Assert.assertEquals(expectedTagsCount, 1);//expect 1 tag with such text
        //there is issue in previous page connected with '&zwnj;' char. see HomePage
    }
}
