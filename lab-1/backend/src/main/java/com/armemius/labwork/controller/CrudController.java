package com.armemius.labwork.controller;

import com.armemius.labwork.annotations.CurrentUser;
import com.armemius.labwork.annotations.NotifyClients;
import com.armemius.labwork.data.domain.AppUser;
import com.armemius.labwork.data.dto.ErrorResponseDto;
import com.armemius.labwork.data.dto.SimpleSuccessDto;
import com.armemius.labwork.service.abstractions.CrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

abstract public class CrudController<T> {

    protected final CrudService<?, T> crudService;

    protected CrudController(CrudService<?, T> crudService) {
        this.crudService = crudService;

    }

    @GetMapping
    @Operation(
            summary = "Get list of objects",
            description = "Get list of all objects in DB with pagination and optional sorting"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of objects was successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation or other errors",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    public ResponseEntity<List<T>> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(required = false) String filter
    ) {
        return ResponseEntity.ok(crudService.getAll(
                PageRequest.of(
                        page,
                        size,
                        Objects.equals(sort, "asc") ?
                                Sort.by(sortBy).ascending() :
                                Sort.by(sortBy).descending()
                )
        ).toList());
    }

    @GetMapping("/total")
    @Operation(
            summary = "Get total objects count",
            description = "Get count of all objects present in persistence, maybe with filtering"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Total count was successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation or other errors",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    public ResponseEntity<SimpleSuccessDto<Long>> getTotalCount(
            @RequestParam(required = false) String filter
    ) {
        return ResponseEntity.ok(new SimpleSuccessDto<>(
                crudService.getTotal()
        ));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get one object",
            description = "Get one object by its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Object was found and successfully returned"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Object with such ID was not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation or other errors",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    public ResponseEntity<T> get(@Valid @NotNull @PathVariable Long id) {
        return ResponseEntity.ok(crudService.get(id));
    }

    @PostMapping
    @Operation(
            summary = "Create new object",
            description = "Create new object and insert into DB"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Object was successfully created and inserted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Some dependent objects does not exist (for example if you've provided non-existent ID)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation or other errors",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @NotifyClients("Some object was created")
    public ResponseEntity<T> create(
            @Valid @RequestBody T location,
            @CurrentUser @Parameter(hidden = true) AppUser user
    ) {
        return ResponseEntity.ok(crudService.create(location, user));
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Update existing object",
            description = "Updates existing object in DB by its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Object was successfully updated"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Some dependent objects or object itself does not exist (for example if you've provided non-existent ID)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation or other errors",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @NotifyClients("Some object was updated")
    public ResponseEntity<T> update(
            @Valid @RequestBody T location,
            @Valid @NotNull @PathVariable Long id
    ) {
        return ResponseEntity.ok(crudService.update(id, location));
    }

    @Operation(
            summary = "Deletes existing object",
            description = "Deletes object from DB if it exists"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Object was successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Object with given ID does not exist",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation or other errors",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    @NotifyClients("Some object was deleted")
    public ResponseEntity<SimpleSuccessDto<String>> delete(
            @Valid @NotNull @PathVariable Long id
    ) {
        crudService.delete(id);
        return ResponseEntity.ok(new SimpleSuccessDto<>("Success"));
    }

}
