package com.ra.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unknown")
public class HomeController {
    @GetMapping("/user/list")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    public String user(){
        return "success";
    }
}
