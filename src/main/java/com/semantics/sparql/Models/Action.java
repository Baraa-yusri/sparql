package com.semantics.sparql.Models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
public class Action {

    enum ActionStatusType {ACTIVEACTIONSTATUS,
        CompletedActionStatus
        ,FailedActionStatus
        ,PotentialActionStatus};
    private Map<String,Object> context;
    private String type;

    private String actionName;
    private String actionStatus;

    //TODO: add an error class and change the type of this attribute.
    private String error;
    private String description;
    private Map<String,Object> object;
    private Answer result;


    @Override
    public String toString() {
        return "{" +
                "'@context':" + context+"" +
                ",'@type':'" + type + '\'' +
                ",'schema:name':'" + actionName + '\'' +
                ",'schema:description':'" + description + '\'' +
                ",'schema:actionStatus':'schema:"+actionStatus + '\'' +
                ",'object':" + object +
                "}";
    }
}
