package com.semantics.sparql.Services.Actions.models;

import com.semantics.sparql.Connectors.PublicHolidays.PubHolidaysClient;
import com.semantics.sparql.Models.Action;
import com.semantics.sparql.Models.Answer;
import com.semantics.sparql.Services.Actions.ActionHandler;
import com.semantics.sparql.Services.ShapeValidator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Slf4j
public class PHoildayIntegratedAction implements ActionHandler {
    private Action action;
    private PubHolidaysClient pubHolidaysClient;
    private ShapeValidator shapeValidator;

    public PHoildayIntegratedAction(Action action, PubHolidaysClient pubHolidaysClient,ShapeValidator shapeValidator) {
        this.action = action;
        this.pubHolidaysClient = pubHolidaysClient;
        this.shapeValidator=shapeValidator;
    }

    @Override
    public boolean canHandleAction() {

        return  action.getActionName().contains("Holidays");

    }

    @Override
    @SneakyThrows
    public void invoke() {
        boolean conforms;

        switch (action.getActionName()) {
            case "Holidays.Search" ->{
                if (new ShapeValidator().requestvalidation("src/main/resources/shaclShapes/data.jsonld",
                        "src/main/resources/shaclShapes/SearchHolidayShape.ttl")){
                                            pubHolidaysClient.search();
                }else {
                    action.setActionStatus("FailedAction");
                    action.setResult(new Answer(action.getContext(), action.getType(), action.getActionStatus(),"shape violation"));
                }
            }
            case "Holidays.Insert"-> {
                 conforms =shapeValidator.requestvalidation("src/main/resources/shaclShapes/data.jsonld",
                        "src/main/resources/shaclShapes/insertHolidayShape.ttl");
                if (conforms) {
                    pubHolidaysClient.insert();
                }else {
                    action.setActionStatus("FailedAction");
                    action.setResult(new Answer(action.getContext(), action.getType(), action.getActionStatus(),"shape violation"));
                }
            }
            case "Holidays.Delete"->{
                if (new ShapeValidator().requestvalidation("src/main/resources/shaclShapes/data.jsonld",
                        "src/main/resources/shaclShapes/DeleteHolidayShape.ttl")) {
                    pubHolidaysClient.delete();
                }else {
                    action.setActionStatus("FailedAction");
                    action.setResult(new Answer(action.getContext(), action.getType(), action.getActionStatus(),"shape violation"));
                }
            }
            case "Holidays.Update"->
                    pubHolidaysClient.update();
        }

    }
}

