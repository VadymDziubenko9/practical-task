import DTO.events.Bets;
import DTO.events.EventData;
import DTO.events.FullEventData;
import DTO.events.Odds;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.Specification;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class ApiTest {

    private final static String BASE_URL = "https://s.gsb.co.zm/services";
    private final static String SPAIN_ID = "336";

    @BeforeClass
    public void installSpec() {
        Specification.
                installSpecification(Specification.requestSpec(BASE_URL), Specification.responseSpecUniqueStatus(200));
    }

    @Test
    public void validateSpainOdds() {
        var fullEventData = getDivisionEventsByLeagueId(SPAIN_ID);
        Assertions.assertThat(fullEventData.isSuccessfull())
                .as("Check if the response is successful")
                .isTrue();

        var eventsList = getEventsList(fullEventData);
        assertThat(eventsList).hasNoNullFieldsOrProperties();
        for (String event : eventsList) {
            var oddsList = getOddsList(fullEventData, event);
            for (String odd : oddsList) {
                assertThat(Double.parseDouble(odd))
                        .as("Found odd is over 1.5 and less than 3.34")
                        .isGreaterThan(1.5)
                        .isLessThan(3.34);
            }
            printEvents(event, oddsList.toString());
        }
    }

    public FullEventData getDivisionEventsByLeagueId(String leagueId) {
        Map<String, String> queryParams = new LinkedHashMap<>();
        queryParams.put("betTypeIds", "-1");
        queryParams.put("take", "100");
        queryParams.put("statusId", "0");
        queryParams.put("eventTypeId", "0");
        queryParams.put("leagueIds", String.format("%s", leagueId));
        queryParams.put("skip", "0");
        queryParams.put("sportTypeIds", "31");

        String query = queryParams
                .entrySet()
                .stream()
                .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("&"));

        return given()
                .when()
                .get("/evapi/event/GetEvents?" + query)
                .then().log().all()
                .extract().as(FullEventData.class);
    }

    @Step
    public List<String> getEventsList(FullEventData fullEventData) {
        return fullEventData.getData().stream()
                .filter(eventData -> eventData.getBts().stream()
                        .flatMap(bets -> bets.getOdds().stream())
                        .anyMatch(odds -> Double.parseDouble(odds.getP()) > 1.5 && Double.parseDouble(odds.getP()) < 3.34))
                .map(EventData::getA)
                .toList();
    }

    @Step
    public List<String> getOddsList(FullEventData fullEventData, String eventName) {
        log.info("Gets odds list for {} ", eventName);
        return fullEventData.getData().stream()
                .filter(eventData -> eventName.equals(eventData.getA()))
                .flatMap(eventData -> eventData.getBts().stream())
                .map(Bets::getOdds)
                .flatMap(List::stream).toList()
                .stream()
                .filter(odds -> Double.parseDouble(odds.getP()) > 1.5 && Double.parseDouble(odds.getP()) < 3.34)
                .map(Odds::getP)
                .toList();
    }

    @Step
    public void printEvents(String event, String oddsList) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(event, oddsList);

        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            System.out.println(entry);
        }
    }
}
