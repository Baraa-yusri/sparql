package com.semantics.sparql.Controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semantics.sparql.Models.Action;
import com.semantics.sparql.Models.Answer;
import com.semantics.sparql.Models.IntentRequest;
import com.semantics.sparql.Services.AskService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("sparqltool/")
@Slf4j
public class ApplicationController {
    private Action action;
    private ActionDataLoader actionDataLoader;
    private AskService askService;

    ApplicationController(Action action, ActionDataLoader actionDataLoader, AskService askService){
        this.actionDataLoader = actionDataLoader;
        this.action = action;
        this.askService = askService;
    }
    @PostMapping(
            value = "/query",
            consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Answer> post(
            @RequestBody IntentRequest intentRequest) throws IOException {
        log.info("received intent request: {}", intentRequest);
            actionDataLoader.loadData(intentRequest);
        askService.ask();
        return ResponseEntity.ok(action.getResult());
    }
}
