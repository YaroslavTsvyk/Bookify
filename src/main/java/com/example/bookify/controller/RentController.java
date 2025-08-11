package com.example.bookify.controller;

import com.example.bookify.dto.RentRequest;
import com.example.bookify.dto.RentResponse;
import com.example.bookify.model.User;
import com.example.bookify.service.RentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rents")
@RequiredArgsConstructor
public class RentController {

    private final RentService rentService;


    @PostMapping
    public ResponseEntity<RentResponse> createRent(@Valid @RequestBody RentRequest request) {
        RentResponse response = rentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<RentResponse>> getAllRentsForUser() {
        return ResponseEntity.ok(rentService.getAllForUser());
    }

    @GetMapping()
    public ResponseEntity<List<RentResponse>> getAllRents() {
        return ResponseEntity.ok(rentService.getAll());
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<RentResponse> returnBook(@PathVariable Long id) {
        RentResponse response = rentService.returnBook(id);
        return ResponseEntity.ok(response);
    }
}
