package com.armemius.labwork.controller;

import com.armemius.labwork.data.dto.objects.CoordinatesDto;
import com.armemius.labwork.service.crud.CoordinatesCrudService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management/coordinates")
@Tag(name = "Coordinates objects API", description = "API for managing coordinates objects")
@CrossOrigin("*")
public class CoordinatesController extends CrudController<CoordinatesDto> {

    @Autowired
    protected CoordinatesController(CoordinatesCrudService crudService) {
        super(crudService);
    }
}
