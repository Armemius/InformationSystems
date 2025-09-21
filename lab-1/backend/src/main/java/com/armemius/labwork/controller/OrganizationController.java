package com.armemius.labwork.controller;

import com.armemius.labwork.data.dto.objects.OrganizationDto;
import com.armemius.labwork.service.crud.OrganizationCrudService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management/organization")
@Tag(name = "Organization objects API", description = "API for managing organization objects")
@CrossOrigin("*")
public class OrganizationController extends CrudController<OrganizationDto> {

    @Autowired
    protected OrganizationController(OrganizationCrudService crudService) {
        super(crudService);
    }
}
