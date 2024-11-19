import dto.events.FullEventData;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import steps.ApiTestSteps;
import utils.Specification;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ApiTest extends BaseTest {
    private static final String SPAIN_ID = "336";
    private final ApiTestSteps apiTestSteps = new ApiTestSteps();
    private List<String> eventsResult = new ArrayList<>();

    @BeforeClass
    public void installSpec() {
        Specification.
                installSpecification(Specification.requestSpec(BASE_URL), Specification.responseSpecUniqueStatus(200));
    }

    @Test
    public void validateSpainOdds() {
        var eventsResponse = apiTestSteps.getDivisionEventsResponse(SPAIN_ID);

        var fullEventData = apiTestSteps.deserializeEvents(eventsResponse);

        validateResponseStatus(fullEventData);

        eventsResult = apiTestSteps.getEventsList(fullEventData, ODD1, ODD2);
    }

    public void validateResponseStatus(FullEventData fullEventData) {
        Assertions.assertThat(fullEventData.isSuccessful())
                .as("Check if the response is successful")
                .isTrue();
    }

    @AfterClass
    public void printEventsSummary() {
        if (!eventsResult.isEmpty()) {
            log.info("Events with odds between {} and {}", ODD1, ODD2);
            for (String event : eventsResult) {
                System.out.println(event);
            }
        }
    }
}
