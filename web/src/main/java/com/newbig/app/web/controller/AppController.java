package com.newbig.app.web.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(value = "App管理", description = "App管理")
public class AppController {

    @GetMapping(value = "/")
    @ApiOperation(value = "HelloWorld", notes = "", httpMethod = "GET")
    public String HelloWorld() {
        return "Hello World";
    }
}
