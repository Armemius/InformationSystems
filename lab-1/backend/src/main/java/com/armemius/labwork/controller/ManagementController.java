package com.armemius.labwork.controller;

import com.armemius.labwork.data.dto.ErrorResponseDto;
import com.armemius.labwork.data.dto.objects.OrganizationDto;
import com.armemius.labwork.service.ManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/management")
@RequiredArgsConstructor
@Tag(name = "Management API", description = "API for managing organizations")
@CrossOrigin("*")
public class ManagementController {

    private final ManagementService managementService;

    @GetMapping("/search/by-name")
    @Operation(
            summary = "Find organizations by name prefix",
            description = "Returns all organizations whose name starts with the given prefix"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of matching organizations"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    public ResponseEntity<List<OrganizationDto>> findByNameStartsWith(
            @RequestParam String prefix
    ) {
        return ResponseEntity.ok(managementService.findByNameStartsWith(prefix));
    }

    @GetMapping("/search/rating/less-than")
    @Operation(
            summary = "Find organizations with rating less than value",
            description = "Returns all organizations whose rating is lower than the given threshold"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of matching organizations"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    public ResponseEntity<List<OrganizationDto>> findByRatingLessThan(
            @RequestParam Float maxRating
    ) {
        return ResponseEntity.ok(managementService.findByRatingLessThan(maxRating));
    }

    @GetMapping("/search/rating/greater-than")
    @Operation(
            summary = "Find organizations with rating greater than value",
            description = "Returns all organizations whose rating is greater than the given threshold"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of matching organizations"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    public ResponseEntity<List<OrganizationDto>> findByRatingGreaterThan(
            @RequestParam Float minRating
    ) {
        return ResponseEntity.ok(managementService.findByRatingGreaterThan(minRating));
    }

    @PostMapping("/merge")
    @Operation(
            summary = "Merge two organizations into a new one",
            description = "Creates a new organization by merging two existing ones and assigning it a new name and address"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "New organization successfully created"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "One of the specified organizations or address does not exist",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation or other errors",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    public ResponseEntity<OrganizationDto> mergeOrganizations(
            @RequestParam Long firstOrgId,
            @RequestParam Long secondOrgId,
            @RequestParam String newName,
            @RequestParam Long addressId
    ) {
        return ResponseEntity.ok(
                managementService.mergeOrganizations(firstOrgId, secondOrgId, newName, addressId)
        );
    }

    @PostMapping("/absorb")
    @Operation(
            summary = "Absorb one organization into another",
            description = "Transfers all employees and turnover from the absorbed organization into the absorber, then deletes the absorbed organization"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Organization successfully absorbed"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "One of the specified organizations does not exist",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation or other errors",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    public ResponseEntity<OrganizationDto> absorbOrganization(
            @RequestParam Long absorberId,
            @RequestParam Long absorbedId
    ) {
        return ResponseEntity.ok(
                managementService.absorbOrganization(absorberId, absorbedId)
        );
    }
}
