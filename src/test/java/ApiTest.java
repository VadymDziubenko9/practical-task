import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterMethod;
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

    @Test(groups = "spainLeague")
    public void getSpainLeagueEventsOdds() {
        var eventsResponse = apiTestSteps.validateJsonSchema(SPAIN_ID);

        var fullEventData = apiTestSteps.deserializeEvents(eventsResponse);

        Assertions.assertThat(fullEventData.isSuccessful())
                .as("Check if the response is successful")
                .isTrue();

        eventsResult = apiTestSteps.getEventsWithAllOdds(fullEventData);
    }

    @AfterMethod(groups = "spainLeague")
    public void printEventsSummary() {
        if (!eventsResult.isEmpty()) {
            log.info("Spain events with all odds");
            for (String event : eventsResult) {
                System.out.println(event);
            }
        }
    }
}
