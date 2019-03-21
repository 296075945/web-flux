package com.wy.flux.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FluxController {

    @RequestMapping("/mono")
    public Mono<String> mono() {
        return Mono.justOrEmpty(null);
    }

    @RequestMapping("/flux")
    public Flux<String> flux() {
        return Flux.just("hello", "world");
    }

    @RequestMapping("/flux2")
    public Flux<User2> flux2() {
        return Flux.just(new User2("hello"), new User2("world"));
    }

    @RequestMapping("/flux3")
    public Flux<User2> flux3() {
        return Flux.just(new User2("hello"));
    }
}

class User2 {
    private String name;

    public User2(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}