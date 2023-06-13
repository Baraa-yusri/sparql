package com.semantics.sparql.Services.Actions;

import java.util.List;
import java.util.Optional;

import com.semantics.sparql.Services.Actions.ActionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ActionHandlerFactory{

    private List<ActionHandler> actionHandlerList;

    public ActionHandlerFactory(  List<ActionHandler> actionHandlerList) {
        this.actionHandlerList = actionHandlerList;
    }

    public Optional<ActionHandler> get(){
         Optional<ActionHandler> actionHandlerOptional = actionHandlerList.stream().filter(ActionHandler::canHandleAction).findFirst();

         if (actionHandlerOptional.isPresent()){
             return actionHandlerOptional;
         }else {
             System.out.println("No action was found!!.");
             return Optional.empty();
         }
    }
}
