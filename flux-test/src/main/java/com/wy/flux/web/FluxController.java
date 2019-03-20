package com.wy.flux.web;

import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FluxController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello world";
    }
    
    @RequestMapping("/hello/{id}")
    public String path(@PathVariable(name="id") int id) {
        return "hello : "+id;
    }
    
    @RequestMapping("/json")
    public Map<String, Object> json(@RequestBody Map<String, Object> map) {
        return map;
    }
}
