package com.aymen.security.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read') OR hasAuthority('user:read') ")

    public String get() {
        return "GET:: book controller";
    }
    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")

    public String post() {
        return "POST:: book controller";
    }
    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")

    public String put() {
        return "PUT:: book controller";
    }
    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')")

    public String delete() {
        return "DELETE:: book controller";
    }
}
