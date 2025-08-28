package com.example.bookify.controller;

import com.example.bookify.dto.RentRequest;
import com.example.bookify.dto.RentResponse;
import com.example.bookify.model.User;
import com.example.bookify.service.RentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Rents", description = "Endpoints for managing book rents")
public class RentController {

    private final RentService rentService;


    @Operation(
            summary = "Create a rent",
            description = "Creates a new rent for a book by providing its ID. The current authenticated user will be the renter.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Rent successfully created",
                            content = @Content(schema = @Schema(implementation = RentResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data",
                            content = @Content(schema = @Schema(example = "{ \"error\": \"Book with id=5 is not available\" }"))),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    @PostMapping
    public ResponseEntity<RentResponse> createRent(@Valid @RequestBody RentRequest request) {
        log.info("POST /api/rents - Creating rent of Book with bookID: {}", request.getBookId());
        RentResponse response = rentService.create(request);
        log.debug("Rent successfully created with ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(
            summary = "Get rents for current user",
            description = "Retrieves all rents that belong to the authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of user rents",
                            content = @Content(schema = @Schema(implementation = RentResponse.class)))
            }
    )
    @GetMapping("/my")
    public ResponseEntity<List<RentResponse>> getAllRentsForUser() {
        log.info("GET /api/rents/my - Retrieving rents that belong to user");
        List<RentResponse> responses = rentService.getAllForUser();
        log.debug("{} rents retrieved", responses.size());
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "Get all rents",
            description = "Retrieves all rents in the system. Accessible only by admins.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of rents",
                            content = @Content(schema = @Schema(implementation = RentResponse.class)))
            }
    )
    @GetMapping()
    public ResponseEntity<List<RentResponse>> getAllRents() {
        log.info("GET /api/rents - Retrieving all rents");
        List<RentResponse> responses = rentService.getAll();
        log.debug("{} rents retrieved", responses.size());
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "Return a rented book",
            description = "Marks a rented book as returned by rent ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book successfully returned",
                            content = @Content(schema = @Schema(implementation = RentResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Rent not found")
            }
    )
    @PostMapping("/{id}/return")
    public ResponseEntity<RentResponse> returnBook(@PathVariable Long id) {
        log.info("POST /api/rents/{}/return - Returning rented book with rentId: {}", id, id);
        RentResponse response = rentService.returnBook(id);
        log.debug("Book returned, rent ID: {}", response.getId());
        return ResponseEntity.ok(response);
    }
}
