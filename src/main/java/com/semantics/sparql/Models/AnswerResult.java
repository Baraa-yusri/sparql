package com.semantics.sparql.Models;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@JsonPropertyOrder()

public class AnswerResult implements Serializable {

    @JsonProperty("@type")
    private String type;

    @JsonProperty("object")
    private Map<String,String> response;


}

