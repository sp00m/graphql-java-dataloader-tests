package db;

import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public final class Db {

    private static final Map<Integer, Blog> blogs = Stream
            .of(
                    new Blog(1),
                    new Blog(2)
            )
            .collect(toMap(blog -> blog.id, identity()));

    private static final Map<Integer, Article> articles = Stream
            .of(
                    new Article(11, blogs.get(1)),
                    new Article(12, blogs.get(1)),
                    new Article(21, blogs.get(2)),
                    new Article(22, blogs.get(2))
            )
            .collect(toMap(article -> article.id, identity()));

    private static final Map<Integer, Comment> comments = Stream
            .of(
                    new Comment(111, articles.get(11)),
                    new Comment(112, articles.get(11)),
                    new Comment(121, articles.get(12)),
                    new Comment(122, articles.get(12)),
                    new Comment(211, articles.get(21)),
                    new Comment(212, articles.get(21)),
                    new Comment(221, articles.get(22)),
                    new Comment(222, articles.get(22))
            )
            .collect(toMap(comment -> comment.id, identity()));

    public static Flux<Blog> findAllBlogsByIds(Collection<Integer> ids) {
        return Flux
                .fromIterable(ids)
                .map(blogs::get);
    }

    public static Flux<Blog> findAllBlogs() {
        return Flux.fromIterable(blogs.values());
    }

    public static Flux<Article> findAllArticlesByIds(Collection<Integer> ids) {
        return Flux
                .fromIterable(ids)
                .map(articles::get);
    }

    public static Flux<Article> findAllArticlesByBlogIds(Collection<Integer> blogIds) {
        return Flux
                .fromIterable(articles.values())
                .filter(article -> blogIds.contains(article.blog.id));
    }

    public static Flux<Comment> findAllCommentsByArticleIds(Collection<Integer> articleIds) {
        return Flux
                .fromIterable(comments.values())
                .filter(comment -> articleIds.contains(comment.article.id));
    }

}