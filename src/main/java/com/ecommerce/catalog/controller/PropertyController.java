package com.ecommerce.catalog.controller;

import com.ecommerce.catalog.dto.property.CreateProperty;
import com.ecommerce.catalog.dto.property.PropertyDTO;
import com.ecommerce.catalog.sql.entity.property.Property;
import com.ecommerce.catalog.sql.service.property.PropertyService;
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
public class PropertyController extends AbstractController {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    public static final String URL_PROPERTIES  = "/properties" ;
    public static final String URL_PROPERTY    = "/properties/{property_id}" ;

    private final PropertyService propertyService;

    // --------------------------------------
    // -        Methods  - API              -
    // --------------------------------------

    /**
     *
     * @return
     */
    @GetMapping(value = URL_PROPERTIES , produces= MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<List<PropertyDTO>> getAllProperties(){
        List<Property> properties = this.propertyService.findAll();
        if( properties == null || properties.isEmpty() ) return new ResponseEntity<>(HttpStatus.NOT_FOUND );
        List<PropertyDTO> propertyDTOList = this.propertyService.toDTO( properties );
        return new ResponseEntity<>( propertyDTOList, HttpStatus.FOUND );
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping( value = URL_PROPERTY , produces= MediaType.APPLICATION_JSON_UTF8_VALUE  )
    public ResponseEntity<PropertyDTO> getProperty(@PathVariable("property_id") Integer id ){
        Property property = this.propertyService.find( id );
        if( property == null )return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        PropertyDTO propertyDTO = this.propertyService.toDTO( property );
        return new ResponseEntity<>( propertyDTO , HttpStatus.FOUND );
    }

    /**
     *
     * @param createProperty
     * @param requestContext
     * @return
     */
    @PostMapping( value = URL_PROPERTIES, produces= MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<PropertyDTO> createProperty( @RequestBody CreateProperty createProperty,
                                                       @Context HttpServletRequest requestContext )
    {
        Property property = this.propertyService.toEntity( createProperty );
        Property newProperty = this.propertyService.save( property );
        PropertyDTO newPropertyDTO = this.propertyService.toDTO( newProperty );
        return new ResponseEntity<>( newPropertyDTO, HttpStatus.CREATED );
    }

    /**
     *
     * @param propertyId
     * @return
     */
    @DeleteMapping( value = URL_PROPERTY )
    public ResponseEntity<String> deleteProperty( @PathVariable("property_id") Integer propertyId ){
        Property property = this.propertyService.find( propertyId );
        if( property == null ) return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        this.propertyService.delete( property );
        return new ResponseEntity<>( HttpStatus.GONE );
    }



}
