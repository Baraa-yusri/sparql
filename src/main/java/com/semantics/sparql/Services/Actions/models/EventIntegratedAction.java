package com.semantics.sparql.Services.Actions.models;

import com.semantics.sparql.Connectors.Event.EventClient;
import com.semantics.sparql.Models.Action;
import com.semantics.sparql.Services.Actions.ActionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class EventIntegratedAction implements ActionHandler {

    private Action action;
    private EventClient eventClient;
    public EventIntegratedAction(Action action, EventClient eventClient) {
        this.action = action;
        this.eventClient = eventClient;
    }

    @Override
    public boolean canHandleAction() {

       return  action.getActionName().contains("Event.Search");

    }

    @Override
    public void invoke() {
        try {
            boolean confirms = eventClient.requestvalidation();
            eventClient.searchEvent();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



}
