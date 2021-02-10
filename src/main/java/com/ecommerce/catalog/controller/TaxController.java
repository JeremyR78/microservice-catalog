package com.ecommerce.catalog.controller;

import com.ecommerce.catalog.dto.tax.CreateOrUpdateTax;
import com.ecommerce.catalog.dto.tax.TaxDTO;
import com.ecommerce.catalog.exceptions.TaxNotFoundException;
import com.ecommerce.catalog.sql.entity.tax.Tax;
import com.ecommerce.catalog.sql.service.tax.TaxService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.List;

import static com.ecommerce.catalog.model.MediaTypeCustom.APPLICATION_JSON_PATCH_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class TaxController extends AbstractController {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    public static final String URL_TAXES    = "/taxes" ;
    public static final String URL_TAX      = "/taxes/{tax_id}" ;

    private final TaxService tavService;


    // --------------------------------------
    // -        Methods  - API              -
    // --------------------------------------

    /**
     *
     * @param createOrUpdateTax
     * @param requestContext
     * @return
     */
    @PostMapping(value = URL_TAXES, produces= MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<TaxDTO> createTax( @RequestBody CreateOrUpdateTax createOrUpdateTax,
                                                        @Context HttpServletRequest requestContext )
    {
        Tax tax = this.tavService.toEntity(createOrUpdateTax);
        Tax newTax = this.tavService.save( tax );
        TaxDTO taxDTO = this.tavService.toDTO( newTax );
        return new ResponseEntity<>( taxDTO, HttpStatus.CREATED );
    }

    /**
     *
     * @return
     */
    @GetMapping(value = URL_TAXES, produces= MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<List<TaxDTO>> getAllTaxes(){
        List<Tax> taxes = this.tavService.findAll();
        if( taxes == null || taxes.isEmpty() ){
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
        List<TaxDTO> taxDTOList = this.tavService.toDTO( taxes );
        return new ResponseEntity<>( taxDTOList, HttpStatus.OK );
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping(value = URL_TAX, produces = MediaType.APPLICATION_JSON_UTF8_VALUE  )
    public ResponseEntity<TaxDTO> getTax( @PathVariable("tax_id") Integer id ){
        Tax tax = this.tavService.findWithOptional( id ).orElseThrow( () -> new TaxNotFoundException( id ) );
        TaxDTO taxDTO = this.tavService.toDTO( tax );
        return new ResponseEntity<>( taxDTO , taxDTO != null ? HttpStatus.FOUND : HttpStatus.NOT_FOUND );
    }

    @PatchMapping(value = URL_TAX, consumes = APPLICATION_JSON_PATCH_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<TaxDTO> patchTax( @PathVariable("tax_id") Integer id, @RequestBody JsonPatch patch ){
        try {
            Tax taxFound = this.tavService.findWithOptional( id ).orElseThrow( () -> new TaxNotFoundException( id ) );
            TaxDTO taxOldDTO = this.tavService.toDTO( taxFound );
            TaxDTO taxDtoPatched = this.tavService.applyPatchTo( patch, taxOldDTO );
            Tax taxPatched = this.tavService.toEntity( taxDtoPatched );
            Tax newTax = this.tavService.save( taxPatched );
            TaxDTO newTaxDTO = this.tavService.toDTO( newTax );
            return new ResponseEntity<>( newTaxDTO, HttpStatus.OK );
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = URL_TAX, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<TaxDTO> putTax( @PathVariable("tax_id") Integer id, @RequestBody CreateOrUpdateTax createOrUpdateTax ){
        Tax tax = this.tavService.update( id, createOrUpdateTax );
        Tax taxUpdated = this.tavService.save( tax );
        TaxDTO taxDTO = this.tavService.toDTO( taxUpdated );
        return new ResponseEntity<>( taxDTO, HttpStatus.OK );
    }

    /**
     *
     * @param propertyId
     * @return
     */
    @DeleteMapping( value = URL_TAX, produces= MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<Void> deleteTax( @PathVariable("tax_id") Integer propertyId ){
        Tax tax = this.tavService.find( propertyId );
        if( tax == null ){
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
        this.tavService.delete( tax );
        return new ResponseEntity<>( HttpStatus.GONE );
    }
    
}
