package com.armemius.labwork.controller;


import com.armemius.labwork.data.dto.objects.LocationDto;
import com.armemius.labwork.service.crud.LocationCrudService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/management/location")
@Tag(name = "Location objects API", description = "API for managing location objects")
@CrossOrigin("*")
public class LocationController extends CrudController<LocationDto> {

    @Autowired
    protected LocationController(LocationCrudService crudService) {
        super(crudService);
    }

}
