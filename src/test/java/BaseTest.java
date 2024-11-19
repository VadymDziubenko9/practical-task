import com.codeborne.selenide.Selenide;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import utils.WebDriverUtility;

@Slf4j
public class BaseTest {

    protected final static String BASE_URL = "https://s.gsb.co.zm/services";
    protected final static double ODD1 = 1.5;
    protected final static double ODD2 = 3.34;

    @BeforeSuite
    public void setupConfig() {
        WebDriverUtility.initDriver();
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
