package com.semantics.sparql.Services.Actions.models;

import com.semantics.sparql.Connectors.Trail.TrailClient;
import com.semantics.sparql.Models.Action;
import com.semantics.sparql.Services.Actions.ActionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
        return action.getActionName().contains("Trail.Search");
    }

    @Override
    public void invoke() {
        try {
            boolean confirms = trailClient.requestvalidation();
            trailClient.searchTrial();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
