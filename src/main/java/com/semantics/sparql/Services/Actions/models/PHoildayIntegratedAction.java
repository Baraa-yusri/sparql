package com.semantics.sparql.Services.Actions.models;

import com.semantics.sparql.Connectors.PublicHolidays.PubHolidaysClient;
import com.semantics.sparql.Models.Action;
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
    public PHoildayIntegratedAction(Action action, PubHolidaysClient pubHolidaysClient) {
        this.action = action;
        this.pubHolidaysClient = pubHolidaysClient;
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
                }
            }
            case "Holidays.Insert"->
                    pubHolidaysClient.insert();
            case "Holidays.Delete"->
                    pubHolidaysClient.delete();
            case "Holidays.Update"->
                    pubHolidaysClient.update();
        }

    }
}

