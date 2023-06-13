package com.semantics.sparql.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Map;
import java.util.List;

@AllArgsConstructor
@Data
@JsonPropertyOrder({ "@context", "@type", "actionStatus", "result" })

public class Answer implements Serializable {

    @JsonProperty("@context")
    private Map<String,Object> context;
    @JsonProperty("@type")
    private String type;
    @JsonProperty("actionStatus")
    private String actionStatus;
    @JsonProperty("result")
    private List<AnswerResult> answerResults;

}
