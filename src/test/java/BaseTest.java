import com.codeborne.selenide.Selenide;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import utils.WebDriverUtility;

@Slf4j
public class BaseTest {

    protected static final String BASE_URL = "https://s.gsb.co.zm/services";

    @BeforeSuite(alwaysRun = true)
    public void setupConfig() {
        WebDriverUtility.initDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() {
        try {
            Selenide.closeWebDriver();
        } catch (IllegalStateException e) {
            log.error("Browser wasn't opened!", e);
        }
    }
}
