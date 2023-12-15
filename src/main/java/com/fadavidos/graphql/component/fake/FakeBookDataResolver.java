package com.fadavidos.graphql.component.fake;

import com.fadavidos.graphql.datasource.fake.FakeBookDataSource;
import com.netflix.dgs.codegen.generated.DgsConstants;
import com.netflix.dgs.codegen.generated.types.Book;
import com.netflix.dgs.codegen.generated.types.ReleaseHistory;
import com.netflix.dgs.codegen.generated.types.ReleaseHistoryInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@DgsComponent
public class FakeBookDataResolver {

    @DgsData(parentType = DgsConstants.QUERY_TYPE, field = DgsConstants.QUERY.Books)
    public List<Book> booksWrittenBy(@InputArgument(name ="author") Optional<String> authorName) {
        if(authorName.isEmpty() || StringUtils.isBlank(authorName.get())) {
            return FakeBookDataSource.BOOK_LIST;
        }
        return FakeBookDataSource.BOOK_LIST.stream()
                .filter( book -> StringUtils.containsIgnoreCase(
                        book.getAuthor().getName(), authorName.get()
                )).collect(Collectors.toList());
    }

    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.BooksByReleased
    )
    public List<Book> getBooksByReleased(DataFetchingEnvironment dataFetchingEnvironment) {
        var releasedMap = (Map<String, Object>) dataFetchingEnvironment.getArgument("releasedInput");
        var releasedInput = ReleaseHistoryInput.newBuilder()
                .printedEdition((boolean) releasedMap.get(DgsConstants.RELEASEHISTORYINPUT.PrintedEdition))
                .year((int) releasedMap.get(DgsConstants.RELEASEHISTORYINPUT.Year))
                .build();
        return FakeBookDataSource.BOOK_LIST.stream()
                .filter( book -> matchReleaseHistory(releasedInput, book.getReleased()))
                .collect(Collectors.toList());
    }

    private boolean matchReleaseHistory(ReleaseHistoryInput input, ReleaseHistory releaseHistory) {
        return releaseHistory.getPrintedEdition().equals(input.getPrintedEdition()) &&
                releaseHistory.getYear() == input.getYear();
    }
}
