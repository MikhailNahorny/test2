package testwork;

import com.nahorny.testwork.core.BaseTest;
import com.nahorny.testwork.tags.page.HomePage;
import org.testng.annotations.Test;

public class TagPageTest extends BaseTest {

    @Test(description = "checking tag filter", priority = 10)
    public void checkAllPopularTags() {
        HomePage hp = new HomePage();
        hp.checkAllTags(); //check filter by all popular tags
//        hp.checkTag(17); //check filter by one tag (argument ordinal)
    }
}
