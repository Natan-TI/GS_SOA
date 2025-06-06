package com.powercast.soap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/soap")
public class SoapController {
    private final CalculatorClient calc;

    public SoapController(CalculatorClient calc) {
        this.calc = calc;
    }

    @GetMapping("/add/{a}/{b}")
    public ResponseEntity<Map<String,Integer>> add(
            @PathVariable int a,
            @PathVariable int b) throws Exception {

        int result = calc.add(a, b);
        return ResponseEntity.ok(Map.of("result", result));
    }
}