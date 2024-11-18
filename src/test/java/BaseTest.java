import com.codeborne.selenide.Selenide;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import page.object.HomePage;
import utils.WebDriverUtility;

@Slf4j
public class BaseTest {

    @BeforeSuite
    public void setupConfig() {
        WebDriverUtility.initDriver();
        new HomePage().openUpcomingTab();
    }

    @AfterSuite
    public void tearDown() {
        try {
            Selenide.closeWebDriver();
        } catch (IllegalStateException e) {
            log.error("Browser wasn't opened!", e);
        }
    }
}
