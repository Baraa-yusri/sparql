package com.semantics.sparql.Services.Actions.models;

import com.semantics.sparql.Connectors.Event.EventClient;
import com.semantics.sparql.Models.Action;
import com.semantics.sparql.Services.Actions.ActionHandler;
import com.semantics.sparql.Services.ShapeValidator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class EventIntegratedAction implements ActionHandler {

    private Action action;
    private EventClient eventClient;
    private ShapeValidator shapeValidator;

    public EventIntegratedAction(Action action, EventClient eventClient,ShapeValidator shapeValidator) {
        this.action = action;
        this.eventClient = eventClient;
        this.shapeValidator = shapeValidator;
    }

    @Override
    public boolean canHandleAction() {
       return  action.getActionName().contains("Event");
    }

    @Override
    @SneakyThrows
    public void invoke()  {
            boolean conforms;

            switch (action.getActionName()){
                    case "Event.Search" ->{
                        conforms=shapeValidator.requestvalidation("src/main/resources/shaclShapes/data.jsonld",
                                "src/main/resources/shaclShapes/SearchEventAction.ttl");
                        if (conforms) {
                            eventClient.search();
                        }
                    }
                    case "Event.Insert"->
                        eventClient.insert();
                    case "Event.Delete"->
                        eventClient.delete();
                    case "Event.Update"->
                        eventClient.update();
                }
    }
}
