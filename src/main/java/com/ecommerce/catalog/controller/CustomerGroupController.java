package com.ecommerce.catalog.controller;

import com.ecommerce.catalog.dto.customer.CustomerGroupDTO;
import com.ecommerce.catalog.sql.entity.customer.CustomerGroup;
import com.ecommerce.catalog.sql.service.customer.CustomerGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerGroupController extends AbstractController  {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    public static final String URL_CUSTOMER_GROUPS   = "/customer_groups" ;
    public static final String URL_CUSTOMER_GROUP    = "/customer_groups/{customer_group_id}" ;

    private final CustomerGroupService customerGroupService;

    // --------------------------------------
    // -        Methods  - API              -
    // --------------------------------------

    @GetMapping( value = URL_CUSTOMER_GROUPS , produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<List<CustomerGroupDTO>> getCurrencies()
    {
        List<CustomerGroupDTO> customerGroups = this.customerGroupService.findAllToDTO();
        return new ResponseEntity<>( customerGroups, HttpStatus.OK );
    }

    @GetMapping(value="/customer_groups/{customer_group_id}" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<CustomerGroupDTO> getCustomerGroup( @PathVariable("customer_group_id") Integer customerGroupId )
    {
        CustomerGroupDTO customerGroup = this.customerGroupService.getByIdToDTO( customerGroupId );
        return new ResponseEntity<>( customerGroup, customerGroup == null ? HttpStatus.NOT_FOUND : HttpStatus.FOUND );
    }


    @PostMapping(value="/customer_groups", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<CustomerGroupDTO> createCustomerGroup ( @RequestBody CustomerGroupDTO customerGroupDTO,
                                                        @Context HttpServletRequest requestContext ) {
        CustomerGroup customerGroup = this.customerGroupService.toEntity( customerGroupDTO );
        customerGroup = this.customerGroupService.save( customerGroup );
        CustomerGroupDTO customerGroupDTOResult = this.customerGroupService.toDTO( customerGroup );
        return new ResponseEntity<>( customerGroupDTOResult, HttpStatus.CREATED );
    }

    @DeleteMapping( value = "/customer_groups/{customer_group_id}")
    public ResponseEntity deleteCustomerGroup( @PathVariable("customer_group_id") Integer customerGroupId ){
        CustomerGroup customerGroup = this.customerGroupService.find( customerGroupId );
        if( customerGroup == null ){
            return new ResponseEntity( HttpStatus.NOT_FOUND );
        }
        this.customerGroupService.delete( customerGroup );
        return new ResponseEntity( HttpStatus.GONE );
    }

}
