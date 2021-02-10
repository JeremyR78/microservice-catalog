package com.ecommerce.catalog.controller;

import com.ecommerce.catalog.dto.promotion.CreateOrUpdatePromoCode;
import com.ecommerce.catalog.dto.promotion.PromoCodeDTO;
import com.ecommerce.catalog.exceptions.PromoCodeNotFoundException;
import com.ecommerce.catalog.sql.entity.promotion.PromoCode;
import com.ecommerce.catalog.sql.service.promotion.PromoCodeService;
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
public class PromoCodeController  extends AbstractController {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    public static final String URL_PROMO_CODES   = "/promo_codes" ;
    public static final String URL_PROMO_CODE    = "/promo_codes/{promo_code_id}" ;

    private final PromoCodeService promoCodeService;

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------

    // --------------------------------------
    // -        Methods  - API              -
    // --------------------------------------

    /**
     *
     * @param createOrUpdatePromoCode
     * @param requestContext
     * @return
     */
    @PostMapping(value = URL_PROMO_CODES, produces= MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<PromoCodeDTO> createPromoCode(@RequestBody CreateOrUpdatePromoCode createOrUpdatePromoCode,
                                                        @Context HttpServletRequest requestContext )
    {
        PromoCode promoCode = this.promoCodeService.toEntity(createOrUpdatePromoCode);
        PromoCode newPromoCode = this.promoCodeService.save( promoCode );
        PromoCodeDTO promoCodeDTO = this.promoCodeService.toDto( newPromoCode );
        return new ResponseEntity<>( promoCodeDTO, HttpStatus.CREATED );
    }

    /**
     *
     * @return
     */
    @GetMapping(value = URL_PROMO_CODES, produces= MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<List<PromoCodeDTO>> getAllPromoCodes(){
        List<PromoCode> promoCodes = this.promoCodeService.findAll();
        if( promoCodes == null || promoCodes.isEmpty() ){
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
        List<PromoCodeDTO> promoCodeDTOList = this.promoCodeService.toDto( promoCodes );
        return new ResponseEntity<>( promoCodeDTOList, HttpStatus.OK );
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping(value = URL_PROMO_CODE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE  )
    public ResponseEntity<PromoCodeDTO> getPromoCode( @PathVariable("promo_code_id") Integer id ){
        PromoCode promoCode = this.promoCodeService.findWithOptional( id ).orElseThrow( () -> new PromoCodeNotFoundException( id ) );
        PromoCodeDTO promoCodeDTO = this.promoCodeService.toDto( promoCode );
        return new ResponseEntity<>( promoCodeDTO , promoCodeDTO != null ? HttpStatus.FOUND : HttpStatus.NOT_FOUND );
    }

    @PatchMapping(value = URL_PROMO_CODE, consumes = APPLICATION_JSON_PATCH_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<PromoCodeDTO> patchPromoCode( @PathVariable("promo_code_id") Integer id, @RequestBody JsonPatch patch ){
        try {
            PromoCode promoCodeFound = this.promoCodeService.findWithOptional( id ).orElseThrow( () -> new PromoCodeNotFoundException( id ) );
            PromoCodeDTO promoCodeOldDTO = this.promoCodeService.toDto( promoCodeFound );
            PromoCodeDTO promoDtoPatched = this.promoCodeService.applyPatchTo( patch, promoCodeOldDTO );
            PromoCode promoPatched = this.promoCodeService.toEntity( promoDtoPatched );
            PromoCode newPromoCode = this.promoCodeService.save( promoPatched );
            PromoCodeDTO newPromoCodeDTO = this.promoCodeService.toDto( newPromoCode );
            return new ResponseEntity<>( newPromoCodeDTO, HttpStatus.OK );
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = URL_PROMO_CODE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<PromoCodeDTO> putPromoCode( @PathVariable("promo_code_id") Integer id, @RequestBody CreateOrUpdatePromoCode createOrUpdatePromoCode ){
        PromoCode promo = this.promoCodeService.update( id, createOrUpdatePromoCode );
        PromoCode promoUpdated = this.promoCodeService.save( promo );
        PromoCodeDTO promoCodeDTO = this.promoCodeService.toDto( promoUpdated );
        return new ResponseEntity<>( promoCodeDTO, HttpStatus.OK );
    }

    /**
     *
     * @param propertyId
     * @return
     */
    @DeleteMapping( value = URL_PROMO_CODE, produces= MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<Void> deletePromoCode( @PathVariable("promo_code_id") Integer propertyId ){
        PromoCode promo = this.promoCodeService.find( propertyId );
        if( promo == null ){
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
        this.promoCodeService.delete( promo );
        return new ResponseEntity<>( HttpStatus.OK );
    }

}
