package com.semantics.sparql;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileUtils;
import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.shacl.util.ModelPrinter;
import org.topbraid.shacl.validation.ValidationUtil;



@SpringBootApplication
public class SparqlApplication {

	public static void main(String[] args) throws IOException {

		SpringApplication.run(SparqlApplication.class, args);
	}


}
