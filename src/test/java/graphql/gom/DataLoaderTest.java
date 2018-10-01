package graphql.gom;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static graphql.gom.Gom.newGom;
import static graphql.gom.utils.QueryRunner.callData;
import static graphql.gom.utils.QueryRunner.callErrors;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@NoArgsConstructor(access = PUBLIC)
public final class DataLoaderTest {

    @RequiredArgsConstructor(access = PRIVATE)
    @Getter
    public static final class MyType {

        private final String name;

    }

    @NoArgsConstructor(access = PRIVATE)
    @Resolver("Query")
    public static final class FooBarQueryResolver {

        public List<MyType> myTypes() {
            return asList(
                    new MyType("foo"),
                    new MyType("bar")
            );
        }

    }

    @NoArgsConstructor(access = PRIVATE)
    @Resolver("Query")
    public static final class BarFooQueryResolver {

        public List<MyType> myTypes() {
            return asList(
                    new MyType("bar"),
                    new MyType("foo")
            );
        }

    }

    @NoArgsConstructor(access = PRIVATE)
    @Resolver("MyType")
    public static final class WithoutSourcesNorArguments {

        @Batched
        public Map<MyType, String> name() {
            return new HashMap<>();
        }

    }

    @Test
    public void withoutSourcesNorArguments() {
        Gom gom = newGom()
                .resolvers(asList(new FooBarQueryResolver(), new WithoutSourcesNorArguments()))
                .build();
        assertFalse(callErrors(gom).isEmpty());
    }

    @NoArgsConstructor(access = PRIVATE)
    @Resolver("MyType")
    public static final class WithSources {

        @Batched
        public Map<MyType, String> name(Set<MyType> myTypes) {
            return myTypes
                    .stream()
                    .collect(toMap(identity(), myType -> myType.getName() + "bar"));
        }

    }

    @Test
    public void withSources() {
        Gom gom = newGom()
                .resolvers(asList(new FooBarQueryResolver(), new WithSources()))
                .build();
        List<Map<String, Object>> myTypes = (List<Map<String, Object>>) callData(gom).get("myTypes");
        assertEquals("foobar", myTypes.get(0).get("name"));
        assertEquals("barbar", myTypes.get(1).get("name"));
    }

    @NoArgsConstructor(access = PRIVATE)
    @Resolver("MyType")
    public static final class WithArguments {

        @Batched
        public Map<MyType, String> name(Arguments arguments) {
            return new HashMap<>();
        }

    }

    @Test
    public void withArguments() {
        Gom gom = newGom()
                .resolvers(asList(new FooBarQueryResolver(), new WithArguments()))
                .build();
        assertFalse(callErrors(gom).isEmpty());
    }

    @NoArgsConstructor(access = PRIVATE)
    @Resolver("MyType")
    public static final class WithSourcesAndArguments {

        @Batched
        public Map<MyType, String> name(Set<MyType> myTypes, Arguments arguments) {
            return myTypes
                    .stream()
                    .collect(toMap(identity(), myType -> myType.getName() + arguments.get("suffix")));
        }

    }

    @Test
    public void withSourcesAndArguments() {
        Gom gom = newGom()
                .resolvers(asList(new FooBarQueryResolver(), new WithSourcesAndArguments()))
                .build();
        List<Map<String, Object>> myTypes = (List<Map<String, Object>>) callData(gom).get("myTypes");
        assertEquals("foobar", myTypes.get(0).get("name"));
        assertEquals("barbar", myTypes.get(1).get("name"));
    }

    @Test
    public void sourceOrder() {
        Gom gom = newGom()
                .resolvers(asList(new BarFooQueryResolver(), new WithSources()))
                .build();
        List<Map<String, Object>> myTypes = (List<Map<String, Object>>) callData(gom).get("myTypes");
        assertEquals("barbar", myTypes.get(0).get("name"));
        assertEquals("foobar", myTypes.get(1).get("name"));
    }

}
