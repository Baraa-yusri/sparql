package com.semantics.sparql.Services.Actions.models;

import com.semantics.sparql.Connectors.PublicHolidays.PubHolidaysClient;
import com.semantics.sparql.Models.Action;
import com.semantics.sparql.Services.Actions.ActionHandler;
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

        return  action.getActionName().contains("PublicHolidays.Search");

    }

    @Override
    public void invoke() {
        try {
            boolean confirms = pubHolidaysClient.requestvalidation();
            pubHolidaysClient.searchPubHolidays();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
