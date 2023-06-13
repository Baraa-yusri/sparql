package com.semantics.sparql.Connectors.Trail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontotext.graphdb.repository.http.GraphDBHTTPRepository;
import com.ontotext.graphdb.repository.http.GraphDBHTTPRepositoryBuilder;
import com.semantics.sparql.Models.Action;
import com.semantics.sparql.Models.Answer;
import com.semantics.sparql.Models.AnswerResult;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.graph.Graph;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.lib.ShLib;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.XSD;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.sparqlbuilder.constraint.Expressions;
import org.eclipse.rdf4j.sparqlbuilder.core.Prefix;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.eclipse.rdf4j.sparqlbuilder.core.query.Queries;
import org.eclipse.rdf4j.sparqlbuilder.core.query.SelectQuery;
import org.eclipse.rdf4j.sparqlbuilder.rdf.Iri;
import org.eclipse.rdf4j.sparqlbuilder.rdf.Rdf;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.eclipse.rdf4j.model.util.Values.iri;

@Component
@Data
@Slf4j
@RequiredArgsConstructor
public class TrailClient {
    private final Action action;



    public boolean requestvalidation() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        String jsonString  = mapper.writeValueAsString(action);


        log.info("The validator of EventClient is being acessed!!");
        log.info("The jsonString:", jsonString);
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/shaclShapes/data.jsonld",true));
        writer.write(jsonString);

        writer.close();
        String shape = "src/main/resources/shaclShapes/shape.ttl";
        String data = "src/main/resources/shaclShapes/data.jsonld";

        Graph shapesGraph = RDFDataMgr.loadGraph(shape);
        Graph dataGraph = RDFDataMgr.loadGraph(data);
        Shapes shapes = Shapes.parse(shapesGraph);
        ValidationReport report = ShaclValidator.get().validate(shapes, dataGraph);
        //printout
        ShLib.printReport(report);
        RDFDataMgr.write(System.out, report.getModel(), Lang.TURTLE);

        boolean conforms = report.conforms();
        System.out.println("conforms:");
        System.out.println(conforms);
        PrintWriter deleter = new PrintWriter(data);
        deleter.print("");
        return report.conforms();
    }
    public void searchTrial(){
        GraphDBHTTPRepository repository = new GraphDBHTTPRepositoryBuilder().
                withServerUrl("http://localhost:7200").withRepositoryId("statements").build();
        RepositoryConnection repositoryConnection = repository.getConnection();
        Prefix xsd = SparqlBuilder.prefix(XSD.NS);
        Prefix rdf = SparqlBuilder.prefix(RDF.NS);
        Prefix schema = SparqlBuilder.prefix("schema", iri("https://schema.org/"));
        Prefix odta = SparqlBuilder.prefix("odta", iri("https://odta.io/voc/"));
        Variable trail = SparqlBuilder.var("trail");
        Variable name = SparqlBuilder.var("name");
        Variable geo = SparqlBuilder.var("geo");
        Variable lat = SparqlBuilder.var("lat");
        Variable lon = SparqlBuilder.var("lon");

        Iri oTrail = odta.iri("Trail");
        Iri sName = schema.iri("name");
        Iri sGeo = schema.iri("geo");
        Iri sLat = schema.iri("latitude");
        Iri sLong = schema.iri("longitude");


        SelectQuery selectQuery = Queries.SELECT().prefix(xsd).
                prefix(rdf).
                prefix(schema).
                prefix(odta)
                .select(name,lat,lon)
                .where((trail.isA(oTrail)).and(trail.has(sName,name).and(trail.has(sGeo, geo))
                                .and(geo.has(sLat,lat)
                                        .andHas(sLong, lon)).optional()).
                        filter(action.getObject().containsKey("latitude")?
                                Expressions.equals(lat, Rdf.literalOf(action.getObject().get("latitude").
                                        toString()).ofType(XSD.DOUBLE)) :Expressions.equals(Expressions.datatype(lat), xsd.iri("double")))
                        .filter(action.getObject().containsKey("longitude")?
                                Expressions.equals(lon, Rdf.literalOf(action.getObject().get("longitude").
                                        toString()).ofType(XSD.DOUBLE)) :Expressions.equals(Expressions.datatype(lon), xsd.iri("double")))
                        .filter(action.getObject().containsKey("name")&&!action.getObject().get("name").toString().isEmpty()?
                                Expressions.equals(name, Rdf.literalOf(action.getObject().get("name").toString()))
                                : Expressions.str(name))
                ).limit(100);

        System.out.println(selectQuery.getQueryString());

        try {
            TupleQuery tupleQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,
                    selectQuery.getQueryString());
            TupleQueryResult tupleQueryResult = tupleQuery.evaluate();
            List<BindingSet> arrayList =  tupleQueryResult.stream().toList();
            List<AnswerResult> answer = new ArrayList<>();
            for (BindingSet bindings : arrayList){
                AnswerResult map = new AnswerResult("Trail", new HashMap<>());
                map.getResponse().put("name", bindings.getValue("name").stringValue());
                map.getResponse().put("latitude", bindings.getValue("lat").stringValue());
                map.getResponse().put("longitude", bindings.getValue("lon").stringValue());
                answer.add(map);
            }

            action.setResult(new Answer(action.getContext(),"SearchAction","Schema:CompletedAction",answer));
            tupleQueryResult.close();
        }finally {
            repositoryConnection.close();
        }
    }
}
