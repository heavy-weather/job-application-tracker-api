package com.anthonyguidotti.job_application_tracker.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.networknt.schema.SpecVersion.VersionFlag.V202012;

@Component
public class JsonSchemaValidator implements Validator {
    private final ConcurrentMap<String, JsonSchema> jsonSchemaMap;
    private final JsonSchemaFactory jsonSchemaFactory;
    private final ObjectMapper objectMapper;

    public JsonSchemaValidator(ObjectMapper objectMapper) {
        this.jsonSchemaMap = new ConcurrentHashMap<>(5, 0.8f, 5);
        this.jsonSchemaFactory = JsonSchemaFactory.getInstance(V202012);
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAnnotationPresent(JsonSchemaValidate.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        jsonSchemaMap.computeIfAbsent(target.getClass().getSimpleName(), (key) -> getJsonSchema(target));
        JsonSchema jsonSchema = jsonSchemaMap.get(target.getClass().getSimpleName());
        JsonNode jsonObject = objectMapper.convertValue(target, JsonNode.class);
        // TODO: Finish this implementation; figure out message codes
        Set<ValidationMessage> validationMessages = jsonSchema.validate(jsonObject);
        if (!validationMessages.isEmpty()) {
            for (ValidationMessage validationMessage : validationMessages) {
//                errors.reject(validationMessage.getProperty(), validationMessage.getMessage());
            }
        }
    }

    private JsonSchema getJsonSchema(Object target) {
        JsonSchemaValidate annotation = AnnotationUtils.findAnnotation(target.getClass(), JsonSchemaValidate.class);
        String schemaFileLocation = (String) AnnotationUtils.getValue(annotation, "schemaFileLocation");

        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(schemaFileLocation)) {
            if (is == null) {
                throw new IOException("Could not load file: " + schemaFileLocation);
            }

            String jsonSchema = new String(is.readAllBytes());
            return jsonSchemaFactory.getSchema(jsonSchema);
        } catch (IOException e) {
            throw new RuntimeException("Could not load JSON Schema from provided path: " + schemaFileLocation +
                    ", class to validate: " + target.getClass().getCanonicalName(), e);
        }

    }
}
