package com.fadavidos.graphql;

import com.netflix.dgs.codegen.generated.client.BooksByReleasedGraphQLQuery;
import com.netflix.dgs.codegen.generated.client.BooksGraphQLQuery;
import com.netflix.dgs.codegen.generated.client.BooksProjectionRoot;
import com.netflix.dgs.codegen.generated.types.Author;
import com.netflix.dgs.codegen.generated.types.ReleaseHistoryInput;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FakeBookDataResolverTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    Faker faker;

    @Test
    void testAllBooks(){
        var graphqlQuery = new BooksGraphQLQuery.Builder().build();
        var projectionRoot = new BooksProjectionRoot<>()
                .title()
                    .author().name().originCountry()
                .getRoot().released().year();
        var graphqlQueryRequest = new GraphQLQueryRequest(graphqlQuery, projectionRoot).serialize();

        List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(graphqlQueryRequest, "data.books[*].title");
        assertNotNull(titles);
        assertFalse(titles.isEmpty());

        List<Author> authors = dgsQueryExecutor.executeAndExtractJsonPath(graphqlQueryRequest, "data.books[*].author");
        assertNotNull(authors);
        assertEquals(titles.size(), authors.size());

        List<Integer> releaseYears = dgsQueryExecutor.executeAndExtractJsonPath(graphqlQueryRequest, "data.books[*].released.year");

        assertNotNull(releaseYears);
        assertEquals(titles.size(), releaseYears.size());
    }

    @Test
    void testBooksWithInput() {
        int expectedYear = faker.number().numberBetween(2019, 2021);
        boolean expectedPrintedEdition = faker.bool().bool();

        ReleaseHistoryInput releaseHistoryInput = ReleaseHistoryInput.newBuilder()
                .year(expectedYear)
                .printedEdition(expectedPrintedEdition)
                .build();

        var graphqlQuery = BooksByReleasedGraphQLQuery.newRequest()
                .releasedInput(releaseHistoryInput)
                .build();

        var projectionRoot = new BooksProjectionRoot<>()
                .released().year().printedEdition();

        var graphqlQueryRequest = new GraphQLQueryRequest(graphqlQuery, projectionRoot).serialize();

        List<Integer> releaseYears = dgsQueryExecutor.executeAndExtractJsonPath(
                graphqlQueryRequest,
                "data.booksByReleased[*].released.year"
        );

        Set<Integer> uniqueReleaseYears = new HashSet<>(releaseYears);

        assertNotNull(uniqueReleaseYears);
        assertTrue(uniqueReleaseYears.size() <= 1);

        if(!uniqueReleaseYears.isEmpty()) {
            assertTrue(uniqueReleaseYears.contains(expectedYear));
        }

        List<Boolean> releaseePrintedEditions = dgsQueryExecutor.executeAndExtractJsonPath(
                graphqlQueryRequest,
                "data.booksByReleased[*].released.printedEdition"
        );
        Set<Boolean> uniqueReleasePrintedEditions = new HashSet<>(releaseePrintedEditions);

        assertNotNull(uniqueReleasePrintedEditions);
        assertTrue(uniqueReleasePrintedEditions.size() <= 1);

        if(!uniqueReleasePrintedEditions.isEmpty()) {
            assertTrue(uniqueReleasePrintedEditions.contains(expectedPrintedEdition));
        }
    }




}
