package com.semantics.sparql.Controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.semantics.sparql.Models.Action;
import com.semantics.sparql.Models.IntentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Component
@Slf4j
public class ActionDataLoader {
    private final Action action;

    public ActionDataLoader( Action action) {
        this.action = action;
    }

    public void loadData ( IntentRequest intentRequest) throws IOException {
        action.setContext(intentRequest.getContext());
        action.setType(intentRequest.getType());
        action.setActionStatus(intentRequest.getActionStatus());
        action.setActionName(intentRequest.getName().replace(" ", "."));
        action.setActionStatus(intentRequest.getActionStatus().substring(intentRequest.getActionStatus().lastIndexOf(":")+1));
        action.setDescription(intentRequest.getDescription());
        action.setObject(intentRequest.getObject().entrySet().stream()
                .collect(toMap(e -> e.getKey().substring(e.getKey().lastIndexOf(":")+1), v->v.getValue())));
        action.setError("");
        writeActionToFile(intentRequest);
    }
    public void writeActionToFile(IntentRequest intentRequest) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString  = mapper.writeValueAsString(intentRequest);
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/shaclShapes/data.jsonld",true));
        writer.write(jsonString);
        writer.close();
    }
}
