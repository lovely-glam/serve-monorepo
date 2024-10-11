package com.lovelyglam.nailserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("pings")
public class PingController {
    @GetMapping
    public ResponseEntity<String> pingHealthCheck() {
        return ResponseEntity.ok("Nail Service System Nominal");
    } 
}