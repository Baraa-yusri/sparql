package com.semantics.sparql.Services.Actions.models;

import com.semantics.sparql.Connectors.Trail.TrailClient;
import com.semantics.sparql.Models.Action;
import com.semantics.sparql.Models.Answer;
import com.semantics.sparql.Models.AnswerResult;
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
                    trailClient.search();}else {
                    action.setActionStatus("FailedAction");
                    action.setResult(new Answer(action.getContext(), action.getType(), action.getActionStatus(),"shape violation"));
                }
            }
            case "Trail.Insert"->{
                if (new ShapeValidator().requestvalidation("src/main/resources/shaclShapes/data.jsonld",
                        "src/main/resources/shaclShapes/InsertTrailShape.ttl")) {
                    trailClient.insert();}else {
                    action.setActionStatus("FailedAction");
                    action.setResult(new Answer(action.getContext(), action.getType(), action.getActionStatus(),"shape violation"));
                }
            }
            case "Trail.Delete"->{
                if (new ShapeValidator().requestvalidation("src/main/resources/shaclShapes/data.jsonld",
                        "src/main/resources/shaclShapes/DeleteTrailShape.ttl")) {
                    trailClient.delete();}else {
                    action.setActionStatus("FailedAction");
                    action.setResult(new Answer(action.getContext(), action.getType(), action.getActionStatus(),"shape violation"));
                }
            }
            case "Trail.Update"->
                    trailClient.update();
        }

    }

}
