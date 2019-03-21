package com.wy.flux;

import reactor.core.publisher.Flux;

public class FluxTest {

    public static void main(String[] args) {
//        Flux.just("1", "2").subscribe(System.out::println);
//        Flux.just("1", "2").subscribe((s) -> {
//            System.out.println(s);
//        });
//        Flux.just("1", "2").subscribe(FluxTest::print);
//        FluxTest test = new FluxTest();
//        Flux.just("1", "2").subscribe(test::print2);

        U u1 = new U();
        u1.name = "hello";
        U u2 = new U();
        u2.name = "world";
        T t = new T() {

            @Override
            public String hello(U u) {
//                System.out.println(u.name);
                return u.name;
            }
        };

        Flux.just(u1, u2).subscribe(t::hello);
        
        Flux<U> flux = Flux.just(u1, u2);
        System.out.println(flux.blockFirst().name);
        

    }

    static void print(String s) {
        System.out.println(s);
    }

    void print2(String s) {
        System.out.println(s);
    }

}

interface T {
    String hello(U u);
}

class U {
    String name;
}