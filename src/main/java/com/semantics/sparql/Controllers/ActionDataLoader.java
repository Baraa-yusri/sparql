package com.semantics.sparql.Controllers;


import com.semantics.sparql.Models.Action;
import com.semantics.sparql.Models.IntentRequest;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.query.algebra.Str;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class ActionDataLoader {
    private Action action;
    private IntentRequest intentRequest;

    public ActionDataLoader( Action action, IntentRequest intentRequest) {
        this.action = action;
        this.intentRequest = intentRequest;
    }

    public void loadData ( IntentRequest intentRequest){
        action.setContext(intentRequest.getContext());
        action.setType(intentRequest.getType());
        action.setActionStatus(intentRequest.getActionStatus());
        action.setActionName(intentRequest.getName().replace(" ", "."));
        action.setActionStatus(intentRequest.getActionStatus().substring(intentRequest.getActionStatus().lastIndexOf(":")+1));
        action.setDescription(intentRequest.getDescription());
        action.setObject(intentRequest.getObject());
        action.setError("No report yet");
    }

}
