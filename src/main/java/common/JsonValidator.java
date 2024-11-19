package common;

import DTO.events.FullEventData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Slf4j
@UtilityClass
public class JsonValidator {

    public void validateJsonSchema(String json) {
        ObjectMapper mapper = new ObjectMapper();

        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        InputStream schemaStream = JsonValidator.class.getResourceAsStream("/schema.json");
        if (schemaStream == null) {
            throw new IllegalArgumentException("Schema file not found");
        }

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);
        JsonSchema schema = factory.getSchema(schemaStream);

        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(jsonStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Set<ValidationMessage> errors = schema.validate(jsonNode);

        if (errors.isEmpty()) {
            log.info("JSON is valid!");
        } else {
            log.info("Validation errors:");
            errors.forEach(error -> log.error(error.getMessage()));
        }
    }

    public FullEventData deserializeEvents(String eventsResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(eventsResponse, FullEventData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
