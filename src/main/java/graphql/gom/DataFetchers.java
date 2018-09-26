package graphql.gom;

import example.db.Article;
import example.db.Blog;
import example.db.Comment;
import graphql.schema.DataFetcher;
import example.resolvers.QueryResolver;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class DataFetchers {

    public static DataFetcher<CompletableFuture<List<Blog>>> queryToBlogsDataFetcher = environment -> {
        System.out.println("########## queryToBlogsDataFetcher");
        return QueryResolver
                .INSTANCE
                .getBlogs()
                .collectList()
                .toFuture();
    };

    public static DataFetcher<CompletableFuture<Blog>> articleToBlogDataFetcher = environment -> {
        Context context = environment.getContext();
        Article article = environment.getSource();
        System.out.println("########## articleToBlogDataFetcher for " + article);
        return context
                .articleToBlogBatchLoader()
                .load(article);
    };

    public static DataFetcher<CompletableFuture<Article>> commentToArticleDataFetcher = environment -> {
        Context context = environment.getContext();
        Comment comment = environment.getSource();
        System.out.println("########## commentToArticleDataFetcher for " + comment);
        return context
                .commentToArticleBatchLoader()
                .load(comment);
    };

    public static DataFetcher<CompletableFuture<List<Article>>> blogToArticlesDataFetcher = environment -> {
        Context context = environment.getContext();
        Blog blog = environment.getSource();
        System.out.println("########## blogToArticlesDataFetcher for " + blog);
        return context
                .blogToArticlesBatchLoader()
                .load(new DataLoaderKey<>(environment));
    };

    public static DataFetcher<CompletableFuture<List<Comment>>> articleToCommentsDataFetcher = environment -> {
        Context context = environment.getContext();
        Article article = environment.getSource();
        System.out.println("########## articleToCommentsDataFetcher for " + article);
        return context
                .articleToCommentsBatchLoader()
                .load(article);
    };

}