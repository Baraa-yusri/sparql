package com.semantics.sparql.Connectors.Trail;

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
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.springframework.stereotype.Component;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.eclipse.rdf4j.model.util.Values.iri;

@Component
@Data
@Slf4j
@RequiredArgsConstructor
public class TrailClient {
    private final Action action;

    private final AnswerBuilder answerBuilder;

    @SneakyThrows
    public void search(){

        String query = String.format(Files.readString(Path.of("src/main/resources/templates/Trail/trailsearch.txt"))
                ,action.getObject().containsKey("name")?"FILTER (?name=\""+
                        action.getObject().get("name")+"\"@de).":"filter(LANG(?name)=\"de\")",
                action.getObject().containsKey("lat")?"FILTER (?lat=\""+
                        action.getObject().get("lat")+"\"^^xsd:double).":"FILTER (DATATYPE(?lat)=xsd:double )\n"
                ,action.getObject().containsKey("lon")?"FILTER (?lon=\""+
                        action.getObject().get("lon")+"\"^^xsd:double).":"FILTER (DATATYPE(?lon)=xsd:double)");
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
        String eventIRI = "https://"+"statements/"+ RandomStringUtils.randomAlphanumeric(16);
        String eventScheduleIRI = "https://"+"statements/"+RandomStringUtils.randomAlphanumeric(16);
        String query =String.format( Files.readString(Path.of("src/main/resources/templates/Trail/trailinsert.txt")),
                eventIRI, eventIRI,action.getObject().get("name"),eventScheduleIRI,eventScheduleIRI,action.getObject().get("lat"),
                action.getObject().get("lon"), eventIRI,eventScheduleIRI);
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
        String query =String.format( Files.readString(Path.of("src/main/resources/templates/Trail/traildelete.txt")),
                action.getObject().get("name"),action.getObject().get("lat"),action.getObject().get("lon"));
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
        boolean latExp = object.containsKey("lat");
        boolean lonExp = object.containsKey("lon");

        String query =String.format( Files.readString(Path.of("src/main/resources/templates/Trail/trailupdate.txt")),
                data.containsKey("name")?"?trail schema:name ?oldName.":"",data.containsKey("lat")?"?geo schema:latitude ?oldlat.":"",
                data.containsKey("lon")?"?geo schema:longitude ?oldlon.":"",
                data.containsKey("name")?"?trail schema:name \""+data.get("name")+"\"@de.":"",data.containsKey("lat")?"?geo schema:latitude \""+
                        data.get("lat")+"\"^^xsd:double.":"",
                data.containsKey("lon")?"?geo schema:longitude \""+data.get("lon")+"\"^^xsd:double.":"",
                nameExp?"FILTER (?oldName = \""+object.get("name")+"\"@de)":"",
                latExp?"FILTER (?oldlat = \""+object.get("lat")+"\"^^xsd:double)":"",
                lonExp?" FILTER (?oldlon = \""+object.get("lon")+"\"^^xsd:double)":"");
        log.info(query);
        GraphDBHTTPRepository repository = new GraphDBHTTPRepositoryBuilder().
                withServerUrl("http://localhost:7200").withRepositoryId("statements").build();
        RepositoryConnection repositoryConnection = repository.getConnection();
        Update update = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL,query);
        update.execute();

        repositoryConnection.close();
    }


}
