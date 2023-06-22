package com.semantics.sparql.Services;

import com.semantics.sparql.Models.Action;
import com.semantics.sparql.Models.Answer;
import com.semantics.sparql.Models.AnswerResult;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Component
public class AnswerBuilder {

    private final Action action;

    public AnswerBuilder( Action action) {
        this.action = action;
    }

    public void buildAnswer(TupleQuery tupleQuery){
        TupleQueryResult tupleQueryResult = tupleQuery.evaluate();
        List<BindingSet> arrayList =  tupleQueryResult.stream().toList();
        List<AnswerResult> answer = new ArrayList<>();
        for (BindingSet bindings : arrayList){
            AnswerResult map = new AnswerResult(action.getType(), new HashMap<>());
            List<String> parametersName = bindings.getBindingNames().stream().toList();
            map.getResponse().put(parametersName.get(0),bindings.getValue(parametersName.get(0)).stringValue());
            map.getResponse().put(parametersName.get(1),bindings.getValue(parametersName.get(1)).stringValue());
            map.getResponse().put(parametersName.get(2),bindings.getValue(parametersName.get(2)).stringValue());
            answer.add(map);}
            action.setResult(new Answer(action.getContext(),
                    action.getActionName().substring(action.getActionName().lastIndexOf(".")+1)+"Action", "Schema:CompletedAction",answer));
        tupleQueryResult.close();
    }

}
