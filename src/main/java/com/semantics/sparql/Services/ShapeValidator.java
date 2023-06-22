package com.semantics.sparql.Services;

import com.semantics.sparql.Models.Answer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.common.exception.ValidationException;
import org.eclipse.rdf4j.common.transaction.IsolationLevels;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.vocabulary.RDF4J;
import org.eclipse.rdf4j.query.algebra.Str;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sail.SailRepositoryConnection;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.shacl.ShaclSail;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Data
@NoArgsConstructor
@Component
public class ShapeValidator {


    public boolean requestvalidation(String dataGraph , String shapeGraph) throws IOException {

        boolean conforms = false;

        ShaclSail shaclSail = new ShaclSail(new MemoryStore());
        SailRepository sailRepository = new SailRepository(shaclSail);

        try (SailRepositoryConnection connection = sailRepository.getConnection()) {

            connection.begin(IsolationLevels.NONE, ShaclSail.TransactionSettings.ValidationApproach.Bulk);

            try (InputStream inputStream = new FileInputStream(shapeGraph)) {
                connection.add(inputStream, "", RDFFormat.TURTLE, RDF4J.SHACL_SHAPE_GRAPH);
            }
            try (InputStream inputStream = new BufferedInputStream(new FileInputStream(dataGraph))) {
                connection.add(inputStream, "", RDFFormat.JSONLD);
            }
            try {
                connection.commit();
                conforms=true;
            } catch (RepositoryException e){
                if(e.getCause() instanceof ValidationException){
                    Model model = ((ValidationException) e.getCause()).validationReportAsModel();
                    Rio.write(model, System.out, RDFFormat.TURTLE);
                }
            }
        }
        sailRepository.shutDown();
        PrintWriter writer = new PrintWriter("src/main/resources/shaclShapes/data.jsonld");
        writer.print("");
        writer.close();

        return conforms;
    }
}
