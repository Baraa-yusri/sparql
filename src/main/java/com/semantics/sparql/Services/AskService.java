package com.semantics.sparql.Services;
import com.semantics.sparql.Services.Actions.ActionHandler;
import com.semantics.sparql.Services.Actions.ActionHandlerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AskService {
    private final ActionHandlerFactory actionHandlerFactory;

    public void ask(){
      log.info("Ask service is being accessed!!HAHAHAHAHAH");

      boolean executedIntegratedAction = executeIntegratedAction();
      log.info(String.valueOf(executedIntegratedAction));


    }

    public boolean executeIntegratedAction(){
        Optional<ActionHandler> actionHandlerOptional = actionHandlerFactory.get();
        actionHandlerOptional.ifPresent(ActionHandler::invoke);
        return actionHandlerOptional.isPresent();
    }
}
