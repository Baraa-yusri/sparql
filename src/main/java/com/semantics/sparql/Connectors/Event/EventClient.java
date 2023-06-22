package com.semantics.sparql.Connectors.Event;


import com.ontotext.graphdb.repository.http.GraphDBHTTPRepository;
import com.ontotext.graphdb.repository.http.GraphDBHTTPRepositoryBuilder;
import com.semantics.sparql.Models.Action;

import com.semantics.sparql.Services.AnswerBuilder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.query.algebra.DeleteData;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.springframework.stereotype.Component;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Component
@Data
@Slf4j
@RequiredArgsConstructor
public class EventClient {
        private final Action action;
        private final AnswerBuilder answerBuilder;

    @SneakyThrows
    public void search(){

        String query = String.format(Files.readString(Path.of("src/main/resources/templates/Event/searchevent.txt"))
                ,action.getObject().containsKey("name")?"FILTER (?name=\""+
                    action.getObject().get("name")+"\"@de).":"filter(LANG(?name)=\"de\")",
                    action.getObject().containsKey("startDate")?"FILTER (?startDate=\""+
                            action.getObject().get("startDate")+"\"^^xsd:date).":"FILTER (DATATYPE(?startDate)=xsd:date )\n"
                    ,action.getObject().containsKey("endDate")?"FILTER (?endDate=\""+
                            action.getObject().get("endDate")+"\"^^xsd:date).":"FILTER (DATATYPE(?endDate)=xsd:date)");
        log.info(query);
        GraphDBHTTPRepository repository = new GraphDBHTTPRepositoryBuilder().
                withServerUrl("http://localhost:7200").withRepositoryId("statements").build();
        RepositoryConnection repositoryConnection = repository.getConnection();
        TupleQuery tupleQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
                    query);
        answerBuilder.buildAnswer(tupleQuery);
        repositoryConnection.close();
    }
    @SneakyThrows
    public void insert(){


        String eventIRI = "https://"+"statements/"+RandomStringUtils.randomAlphanumeric(16);
        String eventScheduleIRI = "https://"+"statements/"+RandomStringUtils.randomAlphanumeric(16);
        String query =String.format( Files.readString(Path.of("src/main/resources/templates/Event/insertevent.txt")),
                eventIRI, eventIRI,action.getObject().get("name"),eventScheduleIRI,eventScheduleIRI,action.getObject().get("startDate"),
                action.getObject().get("endDate"), eventIRI,eventScheduleIRI);
        log.info(query);
        GraphDBHTTPRepository repository = new GraphDBHTTPRepositoryBuilder().
                withServerUrl("http://localhost:7200").withRepositoryId("statements").build();
        RepositoryConnection repositoryConnection = repository.getConnection();
       Update update = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL,query);
        update.execute();
        repositoryConnection.close();
    }

    @SneakyThrows
    public void delete(){

        String query =String.format( Files.readString(Path.of("src/main/resources/templates/Event/deletevent.txt")),
              action.getObject().get("name"),action.getObject().get("startDate"),action.getObject().get("endDate")
        );
        log.info(query);
        GraphDBHTTPRepository repository = new GraphDBHTTPRepositoryBuilder().
                withServerUrl("http://localhost:7200").withRepositoryId("statements").build();
        RepositoryConnection repositoryConnection = repository.getConnection();
        Update update = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL,query);
        update.execute();

        repositoryConnection.close();


    }


    @SneakyThrows
    public void update(){
        Map<String,Object> object = action.getObject();
        Map<String,String> data = (Map<String, String>) object.get("data");
        boolean nameExp = object.containsKey("name");
        boolean sdExp = object.containsKey("startDate");
        boolean edExp = object.containsKey("endDate");

        String query =String.format( Files.readString(Path.of("src/main/resources/templates/Event/updatevent.txt")),
                data.containsKey("name")?"?event schema:name ?oldName.":"",data.containsKey("startDate")?"?es schema:startDate ?oldsd.":"",
                data.containsKey("endDate")?"?es schema:endDate ?olded.":"",
                data.containsKey("name")?"?event schema:name \""+data.get("name")+"\"@de.":"",data.containsKey("startDate")?"?es schema:startDate \""+
                        data.get("startDate")+"\"^^xsd:date.":"",
                data.containsKey("endDate")?"?es schema:endDate \""+data.get("endDate")+"\"^^xsd:date.":"",
                nameExp?"FILTER (?oldName = \""+object.get("name")+"\"@de)":"",
                sdExp?"FILTER (?oldsd = \""+object.get("startDate")+"\"^^xsd:date)":"",
                edExp?" FILTER (?olded = \""+object.get("endDate")+"\"^^xsd:date)":""
        );
        log.info(query);
        GraphDBHTTPRepository repository = new GraphDBHTTPRepositoryBuilder().
                withServerUrl("http://localhost:7200").withRepositoryId("statements").build();
        RepositoryConnection repositoryConnection = repository.getConnection();
        Update update = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL,query);
        update.execute();

        repositoryConnection.close();
    }


}
