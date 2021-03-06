package com.qudini.gom.example.resolvers.loading;

import com.qudini.gom.Batched;
import com.qudini.gom.FieldResolver;
import com.qudini.gom.TypeResolver;
import com.qudini.gom.example.entities.Article;
import com.qudini.gom.example.entities.Blog;
import com.qudini.gom.example.services.ArticleService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@TypeResolver("Blog")
public final class BlogResolverByLoading {

    private static final AtomicInteger GET_ARTICLES_CALL_COUNT = new AtomicInteger();

    @Batched
    @FieldResolver("articles")
    public Map<Blog, List<Article>> getArticles(Set<Blog> blogs) {
        GET_ARTICLES_CALL_COUNT.incrementAndGet();
        return ArticleService.findManyByBlogs(blogs);
    }

    public static int getArticlesCallCount() {
        return GET_ARTICLES_CALL_COUNT.get();
    }

    public static void resetCounts() {
        GET_ARTICLES_CALL_COUNT.set(0);
    }

}
