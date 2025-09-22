package com.armemius.labwork.controller;

import com.armemius.labwork.data.dto.objects.OrganizationDto;
import com.armemius.labwork.service.crud.OrganizationCrudService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/management/organization")
@Tag(name = "Organization objects API", description = "API for managing organization objects")
@CrossOrigin("*")
public class OrganizationController extends CrudController<OrganizationDto> {

    private final OrganizationCrudService organizationCrudService;

    @Autowired
    protected OrganizationController(OrganizationCrudService crudService) {
        super(crudService);
        this.organizationCrudService = crudService;
    }


    @Override
    public ResponseEntity<List<OrganizationDto>> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(required = false) String filter
    ) {
        return ResponseEntity.ok(organizationCrudService.getAllWithFilter(
                PageRequest.of(
                        page,
                        size,
                        Objects.equals(sort, "asc") ?
                                Sort.by(sortBy).ascending() :
                                Sort.by(sortBy).descending()
                ),
                filter
        ).toList());
    }
}
