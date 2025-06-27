package com.zhukm.sync.controller;

import com.zhukm.sync.service.CacheService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private final CacheService cacheService;

    public TestController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/map/put/{key}/{value}")
    public String putCache(@PathVariable("key") String key, @PathVariable("value") Object value) {
        cacheService.put(key, value);
        return "SUCCESS";
    }

    @GetMapping("/map/get/{key}")
    public Object getCache(@PathVariable("key") String key) {
        return cacheService.get(key);
    }

    @GetMapping("/map/remove/{key}")
    public String removeCache(@PathVariable("key") String key) {
        cacheService.remove(key);
        return "SUCCESS";
    }
}
