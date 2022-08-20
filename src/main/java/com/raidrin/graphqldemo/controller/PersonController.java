package com.raidrin.graphqldemo.controller;

import com.raidrin.graphqldemo.entity.Person;
import com.raidrin.graphqldemo.entity.PersonRepository;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class PersonController {
    @Autowired
    private PersonRepository repository;

    @Value("classpath:person.graphql")
    private Resource schemaResource;

    private GraphQL graphQL;

    @PostConstruct
    public void loadSchema() throws IOException {
        // Get the file from resource
        File schemaFile = schemaResource.getFile();

        // Pass the file to the registry
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(schemaFile);

        RuntimeWiring runtimeWiring = buildWiring();

        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private RuntimeWiring buildWiring() {
        DataFetcher<List<Person>> findAllPersonFetcher = data -> (List<Person>) repository.findAll();

        DataFetcher<Person> findPersonFetcher = data -> repository.findByName(data.getArgument("name"));

        return RuntimeWiring.newRuntimeWiring().type("Query", typeWriting ->
                typeWriting.dataFetcher("person", findAllPersonFetcher)
                        .dataFetcher("findPerson", findPersonFetcher)
        ).build();
    }

    @PostMapping("/graphql")
    public ResponseEntity<Object> graphql(@RequestBody String query) {
        ExecutionResult result = graphQL.execute(query);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/addPerson")
    public String addPerson(@RequestBody List<Person> personList) {
        repository.saveAll(personList);
        return "record inserted " + personList.size();
    }
}
