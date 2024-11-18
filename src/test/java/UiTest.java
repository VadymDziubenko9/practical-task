import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import page.object.HomePage;
import utils.WebDriverUtility;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class UiTest extends BaseTest {

    private final HomePage homePage = new HomePage();

    @Test
    public void validateUpcomingEvents() {
        assertThat(homePage.isUpcomingOpened()).as("Upcoming is opened").isTrue();
        assertThat(WebDriverUtility.getCurrentUrl())
                .as("Incorrect page is opened")
                .isEqualTo("https://s.gsb.co.zm/sportsbook/upcoming");

        var upcomingEvents = homePage.getUpcomingEvents();

        assertThat(upcomingEvents).isNotEmpty();

        System.out.println("Events with odds over 1.5 and under 3.34");
        upcomingEvents.forEach(System.out::println);
    }

}
