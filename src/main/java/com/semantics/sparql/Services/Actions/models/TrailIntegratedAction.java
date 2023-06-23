package com.semantics.sparql.Services.Actions.models;

import com.semantics.sparql.Connectors.Trail.TrailClient;
import com.semantics.sparql.Models.Action;
import com.semantics.sparql.Services.Actions.ActionHandler;
import com.semantics.sparql.Services.ShapeValidator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrailIntegratedAction implements ActionHandler {

    private Action action;
    TrailClient trailClient;

    public TrailIntegratedAction( Action action,  TrailClient trailClient) {
        this.action = action;
        this.trailClient = trailClient;
    }

    @Override
    public boolean canHandleAction() {
        return action.getActionName().contains("Trail");
    }

    @SneakyThrows
    @Override
    public void invoke() {


        switch (action.getActionName()) {
            case "Trail.Search" -> {
                if (new ShapeValidator().requestvalidation("src/main/resources/shaclShapes/data.jsonld",
                        "src/main/resources/shaclShapes/SearchTrailShape.ttl")) {
                    trailClient.search();}
            }
            case "Trail.Insert"->{
                if (new ShapeValidator().requestvalidation("src/main/resources/shaclShapes/data.jsonld",
                        "src/main/resources/shaclShapes/InsertTrailShape.ttl")) {
                    trailClient.insert();}
            }
            case "Trail.Delete"->
                    trailClient.delete();
            case "Trail.Update"->
                    trailClient.update();
        }
    }

}
