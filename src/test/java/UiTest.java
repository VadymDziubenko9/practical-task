import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import page.object.HomePage;
import utils.WebDriverUtility;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class UiTest extends BaseTest {
    private final HomePage homePage = new HomePage();
    private final HashMap<String, String> resultEvents = new HashMap<>();

    @Test
    public void validateUpcomingEvents() {
        homePage.openUpcomingTab();

        validateIfHomePageOpened(WebDriverUtility.getCurrentUrl(), "https://s.gsb.co.zm/sportsbook/upcoming");

        Map<String, String> upcomingEvents = homePage.getUpcomingEvents();

        assertThat(upcomingEvents).as("Map is empty").isNotEmpty();

        homePage.validateEvents(resultEvents, upcomingEvents, ODD1, ODD2);
    }

    public void validateIfHomePageOpened(String actualUrl, String expectedUrl) {
        assertThat(actualUrl).as("Incorrect page is opened").isEqualTo(expectedUrl);
        assertThat(homePage.isUpcomingOpened()).as("Upcoming is opened").isTrue();
    }

    @AfterClass
    public void printResult() {
        if (!resultEvents.isEmpty()) {
            for (Map.Entry<String, String> entry : resultEvents.entrySet()) {
                log.info("Events with odds between {} and {} {} {}\n", ODD1, ODD2, entry.getKey(), entry.getValue());
            }
        }
    }
}
