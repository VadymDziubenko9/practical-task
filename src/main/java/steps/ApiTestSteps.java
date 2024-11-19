package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.events.EventData;
import dto.events.FullEventData;
import io.qameta.allure.Step;
import io.restassured.module.jsv.JsonSchemaValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

@Slf4j
public class ApiTestSteps {

    @Step
    public String getDivisionEventsResponse(String leagueId) {
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
                .assertThat()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema.json"))
                .extract().asString();
    }

    @Step
    public List<String> getEventsList(FullEventData fullEventData, double odd1, double odd2) {
        return fullEventData.getData().stream()
                .filter(eventData -> eventData.getBts().stream()
                        .flatMap(bets -> bets.getOdds().stream())
                        .anyMatch(odds -> Double.parseDouble(odds.getP()) > odd1 && Double.parseDouble(odds.getP()) < odd2))
                .map(EventData::getA)
                .toList();
    }

    @Step
    public FullEventData deserializeEvents(String eventsResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(eventsResponse, FullEventData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
