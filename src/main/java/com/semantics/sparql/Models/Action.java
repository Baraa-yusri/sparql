package com.semantics.sparql.Models;


import jdk.jfr.Name;
import lombok.Data;
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

}
