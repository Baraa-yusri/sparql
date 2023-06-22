package com.semantics.sparql.Models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component

public class IntentRequest {
    @JsonProperty("@type")
    private String type;

    @JsonProperty("@context")
    private Map<String,Object> context;

    @JsonProperty("schema:name")
    private String name;
    @JsonProperty("schema:description")
    private String description;
    @JsonProperty("schema:actionStatus")
    private String actionStatus;
    @JsonProperty("schema:object")
    private Map<String,Object> object;
}

