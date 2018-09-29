package graphql.gom;

import graphql.schema.DataFetcher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

import static lombok.AccessLevel.PACKAGE;

@RequiredArgsConstructor(access = PACKAGE)
@Getter(PACKAGE)
final class GraphFieldWiring<R> {

    private final String typeName;

    private final String fieldName;

    private final DataFetcher<CompletableFuture<R>> dataFetcher;

}
