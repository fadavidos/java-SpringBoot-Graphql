package com.fadavidos.graphql.component.fake;

import com.fadavidos.graphql.datasource.fake.FakeHelloDataSource;
import com.netflix.dgs.codegen.generated.types.Hello;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@DgsComponent
public class FakeHelloDataResolver {

    @DgsQuery
    public List<Hello> allHellos(){
        return FakeHelloDataSource.HELLO_LIST;
    }

    @DgsQuery
    public Hello oneHello(){
        System.out.println(String.format("FakeHelloDataSource.HELLO_LIST.size(): %s", FakeHelloDataSource.HELLO_LIST.size()));
        System.out.println(String.format("ThreadLocalRandom.current().nextInt(0,20): %s", ThreadLocalRandom.current().nextInt(0, 20)));
        return FakeHelloDataSource.HELLO_LIST.get(
                Math.abs(ThreadLocalRandom.current().nextInt(0, FakeHelloDataSource.HELLO_LIST.size()))
        );
    }
}
