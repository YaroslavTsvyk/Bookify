package com.example.bookify.controller;

import com.example.bookify.dto.RentRequest;
import com.example.bookify.dto.RentResponse;
import com.example.bookify.model.User;
import com.example.bookify.service.RentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rents")
@RequiredArgsConstructor
@Slf4j
public class RentController {

    private final RentService rentService;


    @PostMapping
    public ResponseEntity<RentResponse> createRent(@Valid @RequestBody RentRequest request) {
        log.info("POST /api/rents - Creating rent of Book with bookID: {}", request.getBookId());
        RentResponse response = rentService.create(request);
        log.debug("Rent successfully created with ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<RentResponse>> getAllRentsForUser() {
        log.info("GET /api/rents/my - Retrieving rents that belong to user");
        List<RentResponse> responses = rentService.getAllForUser();
        log.debug("{} rents retrieved", responses.size());
        return ResponseEntity.ok(responses);
    }

    @GetMapping()
    public ResponseEntity<List<RentResponse>> getAllRents() {
        log.info("GET /api/rents - Retrieving all rents");
        List<RentResponse> responses = rentService.getAll();
        log.debug("{} rents retrieved", responses.size());
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<RentResponse> returnBook(@PathVariable Long id) {
        log.info("POST /api/rents/{}/return - Returning rented book with rentId: {}", id, id);
        RentResponse response = rentService.returnBook(id);
        log.debug("Book returned, rent ID: {}", response.getId());
        return ResponseEntity.ok(response);
    }
}
