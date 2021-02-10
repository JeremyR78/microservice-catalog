package com.ecommerce.catalog.controller;


import com.ecommerce.catalog.dto.promotion.CreateOrUpdatePromotion;
import com.ecommerce.catalog.dto.promotion.PromotionDTO;
import com.ecommerce.catalog.exceptions.PromotionNotFoundException;
import com.ecommerce.catalog.sql.entity.promotion.Promotion;
import com.ecommerce.catalog.sql.service.promotion.PromotionService;
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

@RestController
@RequiredArgsConstructor
public class PromotionController extends AbstractController {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    public static final String URL_PROMOTIONS   = "/promotions" ;
    public static final String URL_PROMOTION    = "/promotions/{promotion_id}" ;

    private final PromotionService  promotionService;

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------


    @PatchMapping(value = URL_PROMOTION, produces= MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<PromotionDTO> updatePromotion( @PathVariable("promotion_id") Integer id,
                                                         @RequestBody JsonPatch patch ){
        try {
            Promotion promotionFound = this.promotionService.findWithOptional( id ).orElseThrow( () -> new PromotionNotFoundException( id ) );
            PromotionDTO promotionOldDTO = this.promotionService.toDTO( promotionFound );
            PromotionDTO promotionDtoPatched = this.promotionService.applyPatchTo( patch, promotionOldDTO );
            Promotion promotionPatched = this.promotionService.toEntity( promotionDtoPatched );
            Promotion newPromotion = this.promotionService.save( promotionPatched );
            PromotionDTO newPromotionDTO = this.promotionService.toDTO( newPromotion );
            return new ResponseEntity<>( newPromotionDTO, HttpStatus.OK );
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = URL_PROMOTION, produces= MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<PromotionDTO> updatePromotion( @PathVariable("promotion_id") Integer id,
                                                         @RequestBody CreateOrUpdatePromotion updatePromotion )
    {
        Promotion promotion = this.promotionService.update( id, updatePromotion );
        Promotion newPromotion = this.promotionService.save( promotion );
        PromotionDTO promotionDTO = this.promotionService.toDTO( newPromotion );
        return new ResponseEntity<>( promotionDTO, HttpStatus.OK );
    }


    /**
     *
     * @param createOrUpdatePromotion
     * @param requestContext
     * @return
     */
    @PostMapping(value = URL_PROMOTIONS, produces= MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<PromotionDTO> createPromotion(@RequestBody CreateOrUpdatePromotion createOrUpdatePromotion,
                                                       @Context HttpServletRequest requestContext )
    {
        Promotion promotion = this.promotionService.toEntity(createOrUpdatePromotion);
        Promotion newPromotion = this.promotionService.save( promotion );
        PromotionDTO promotionDTO = this.promotionService.toDTO( newPromotion );
        return new ResponseEntity<>( promotionDTO, HttpStatus.CREATED );
    }

    /**
     *
     * @return
     */
    @GetMapping(value = URL_PROMOTIONS , produces= MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<List<PromotionDTO>> getAllPromotions(){
        List<Promotion> promotions = this.promotionService.findAll();
        if( promotions == null || promotions.isEmpty() ){
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
        List<PromotionDTO> promotionDTOList = this.promotionService.toDTO( promotions );
        return new ResponseEntity<>( promotionDTOList, HttpStatus.OK );
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping(value = URL_PROMOTION , produces= MediaType.APPLICATION_JSON_UTF8_VALUE  )
    public ResponseEntity<PromotionDTO> getPromotion( @PathVariable("promotion_id") Integer id ){
        Promotion promotion = this.promotionService.find( id );
        if( promotion == null ){
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
        PromotionDTO promotionDTO = this.promotionService.toDTO( promotion );
        return new ResponseEntity<>( promotionDTO , promotionDTO != null ? HttpStatus.FOUND : HttpStatus.NOT_FOUND );
    }

    /**
     *
     * @param propertyId
     * @return
     */
    @DeleteMapping( value = URL_PROMOTION , produces= MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<Void> deletePromotion( @PathVariable("promotion_id") Integer propertyId ){
        Promotion promotion = this.promotionService.find( propertyId );
        if( promotion == null ){
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
        this.promotionService.delete( promotion );
        return new ResponseEntity<>( HttpStatus.OK );
    }

}
