package com.semantics.sparql.Services.Actions;

import org.springframework.stereotype.Component;

@Component
public interface ActionHandler {

    boolean canHandleAction();

    void invoke();
}
