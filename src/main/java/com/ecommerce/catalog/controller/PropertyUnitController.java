package com.ecommerce.catalog.controller;

import com.ecommerce.catalog.dto.property.CreateProperty;
import com.ecommerce.catalog.dto.property.CreatePropertyUnit;
import com.ecommerce.catalog.dto.property.PropertyDTO;
import com.ecommerce.catalog.dto.property.PropertyUnitDTO;
import com.ecommerce.catalog.sql.entity.property.Property;
import com.ecommerce.catalog.sql.entity.property.PropertyUnit;
import com.ecommerce.catalog.sql.service.property.PropertyUnitService;
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
public class PropertyUnitController extends AbstractController {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    public static final String URL_PROPERTIES_UNITS     = "/properties_units" ;
    public static final String URL_PROPERTY_UNIT        = "/properties_units/{property_unit_id}" ;

    private final PropertyUnitService propertyUnitService;

    // --------------------------------------
    // -        Methods  - API              -
    // --------------------------------------

    //
    //  -   UNIT
    //

    /**
     *
     * @return
     */
    @GetMapping( value = URL_PROPERTIES_UNITS , produces= MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<List<PropertyUnitDTO>> getAllProperties(){
        List<PropertyUnit> properties = this.propertyUnitService.findAll();
        if( properties == null || properties.isEmpty() ) return new ResponseEntity<>(HttpStatus.NOT_FOUND );
        List<PropertyUnitDTO> propertyDTOList = this.propertyUnitService.toDTO( properties );
        return new ResponseEntity<>( propertyDTOList, HttpStatus.FOUND );
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping( value = URL_PROPERTY_UNIT , produces= MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<PropertyUnitDTO> getPropertyUnit(@PathVariable("property_unit_id") Integer id ){
        PropertyUnit property = this.propertyUnitService.find( id );
        if( property == null )return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        PropertyUnitDTO propertyDTO = this.propertyUnitService.toDTO( property );
        return new ResponseEntity<>( propertyDTO , HttpStatus.FOUND );
    }

    /**
     *
     * @param createPropertyUnit
     * @param requestContext
     * @return
     */
    @PostMapping( value = URL_PROPERTIES_UNITS, produces= MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<PropertyUnitDTO> createPropertyUnit( @RequestBody CreatePropertyUnit createPropertyUnit,
                                                       @Context HttpServletRequest requestContext )
    {
        PropertyUnit property = this.propertyUnitService.toEntity( createPropertyUnit );
        PropertyUnit newPropertyUnit = this.propertyUnitService.save( property );
        PropertyUnitDTO newPropertyUnitDTO = this.propertyUnitService.toDTO( newPropertyUnit );
        return new ResponseEntity<>( newPropertyUnitDTO, HttpStatus.CREATED );
    }

    /**
     *
     * @param propertyId
     * @return
     */
    @DeleteMapping( value = URL_PROPERTY_UNIT )
    public ResponseEntity<String> deletePropertyUnit( @PathVariable("property_unit_id") Integer propertyId ){
        PropertyUnit property = this.propertyUnitService.find( propertyId );
        if( property == null ) return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        this.propertyUnitService.delete( property );
        return new ResponseEntity<>( HttpStatus.GONE );
    }



}
