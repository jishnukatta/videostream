package com.videostreaming.apigateway;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {


    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ExampleController.class);

    @GetMapping("/test")
    public String test() {
        log.info("Received /test request in API Gateway");
        return "Gateway test response";
    }
    
    
    @GetMapping("/finalize")
    public ResponseEntity<?> finalizeLogin(@RequestParam String token) {
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "token", token
        ));
    }


}
