import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import page.object.HomePage;
import utils.WebDriverUtility;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class UiTest extends BaseTest {
    private static final double OOD_MIN = 1.5;
    private static final double ODD_MAX = 3.34;
    private final HomePage homePage = new HomePage();
    private Map<String, String> sortedEventsMap;

    @Test(groups = "upcomingEvents")
    public void validateUpcomingEvents() {
        homePage.openUpcomingPage();

        assertThat(WebDriverUtility.getCurrentUrl())
                .as("Incorrect page is opened")
                .isEqualTo("https://s.gsb.co.zm/sportsbook/upcoming");

        Map<String, String> upcomingEvents = homePage.getUpcomingEvents();

        assertThat(upcomingEvents).as("Map is empty").isNotEmpty();

        sortedEventsMap = homePage.sortEvents(upcomingEvents, OOD_MIN, ODD_MAX);
    }


    @AfterMethod(groups = "upcomingEvents")
    public void printResult() {
        if (!sortedEventsMap.isEmpty()) {
            for (Map.Entry<String, String> entry : sortedEventsMap.entrySet()) {
                log.info("Events with odds between {} and {} {} {}\n", OOD_MIN, ODD_MAX, entry.getKey(), entry.getValue());
            }
        }
    }
}
