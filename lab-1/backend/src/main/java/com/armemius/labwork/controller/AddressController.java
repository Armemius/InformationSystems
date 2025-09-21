package com.armemius.labwork.controller;

import com.armemius.labwork.data.dto.objects.AddressDto;
import com.armemius.labwork.service.crud.AddressCrudService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management/address")
@Tag(name = "Address objects API", description = "API for managing address objects")
@CrossOrigin("*")
public class AddressController extends CrudController<AddressDto> {

    @Autowired
    protected AddressController(AddressCrudService crudService) {
        super(crudService);
    }
}
