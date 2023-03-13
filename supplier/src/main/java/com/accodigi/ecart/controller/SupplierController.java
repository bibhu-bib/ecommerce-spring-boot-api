package com.accodigi.ecart.controller;


import com.accodigi.ecart.dto.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(path = "/api/1.0/suppliers")
public class SupplierController {

    @GetMapping("/order")
    public ResponseEntity<?> fulfilment() {
        return ResponseEntity.ok().body("Hello");
    }

    @PostMapping("/order")
    public ResponseEntity<?> fulfilment(@RequestBody Order order) {
        log.info("Received fulfillment notification", order);

        return ResponseEntity.ok(order);
    }


}
