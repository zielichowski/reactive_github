package zielichowski.reactive.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Slf4j
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Repository {

    private Repository(String fullName, String description, String cloneUrl, int stars, LocalDateTime createdAt) {
        this.fullName = fullName;
        this.description = description;
        this.cloneUrl = cloneUrl;
        this.stars = stars;
        this.createdAt = createdAt;
    }

    private Repository() {
    }

    private String fullName;

    private String description;

    private String cloneUrl;

    private int stars;

    private LocalDateTime createdAt;

    public static Repository of(String s) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (s != null) {
            try {
                JsonNode jsonNode = objectMapper.readTree(s);

                return new Repository(
                        jsonNode.get("full_name").asText(),
                        jsonNode.get("description").asText(),
                        jsonNode.get("clone_url").asText(),
                        jsonNode.get("stargazers_count").asInt(),
                        LocalDateTime.parse(jsonNode.get("created_at").asText(), DateTimeFormatter.ISO_DATE_TIME));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new Repository();
    }

}
