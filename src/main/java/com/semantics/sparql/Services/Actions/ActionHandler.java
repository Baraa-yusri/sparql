package com.semantics.sparql.Services.Actions;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface ActionHandler {

    boolean canHandleAction();

    void invoke();
}
