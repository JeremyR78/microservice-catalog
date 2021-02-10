package com.ecommerce.catalog.controller;

import com.ecommerce.catalog.dto.price.CreatePriceDTO;
import com.ecommerce.catalog.dto.price.PriceDTO;
import com.ecommerce.catalog.exceptions.ProductNotFoundException;
import com.ecommerce.catalog.exceptions.PromotionNotFoundException;
import com.ecommerce.catalog.model.PriceType;
import com.ecommerce.catalog.sql.entity.price.Price;
import com.ecommerce.catalog.sql.entity.product.Product;
import com.ecommerce.catalog.sql.entity.promotion.Promotion;
import com.ecommerce.catalog.sql.service.price.PriceService;
import com.ecommerce.catalog.sql.service.product.ProductService;
import com.ecommerce.catalog.sql.service.promotion.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class PriceController extends AbstractController  {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    public static final String URL_PRICES   = "/prices" ;
    public static final String URL_PRICE    = "/prices/{price_id}" ;

    private final PriceService priceService;
    private final ProductService productService;
    private final PromotionService promotionService;

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------

    @PostMapping( value = URL_PRICES, produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<PriceDTO> createPrice( @RequestBody CreatePriceDTO createPrice,
                                                        @Context HttpServletRequest requestContext )
    {
        // FIND PRODUCT
        Integer productId = createPrice.getProductId();
        if( productId == null ) throw new ProductNotFoundException( productId );
        Product product = this.productService.find( productId );
        if( product == null ) throw new ProductNotFoundException( productId );

        // FIND PROMO
        Promotion promotion = null;
        if( Objects.equals( createPrice.getPriceType(), PriceType.PROMO ) ) {
            Integer promotionId = createPrice.getPromotionId();
            if( promotionId == null )throw new PromotionNotFoundException( promotionId );
            promotion = this.promotionService.find( promotionId );
            if( promotion == null )throw new PromotionNotFoundException( promotionId );
        }

        // CREATE PRICE
        Price price = this.priceService.toEntity( createPrice, product, promotion );
        Price newPrice = this.priceService.save( price );
        PriceDTO priceDTO = this.priceService.toDTO( newPrice );
        return new ResponseEntity<>( priceDTO, HttpStatus.CREATED );
    }

    /**
     *
     * @return
     */
    @GetMapping(value = URL_PRICES , produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<List<PriceDTO>> getAllPrices(
            @RequestParam( value = "ids", required = false ) List<Integer> pricesId )
    {
        List<Price> prices;
        if( pricesId != null && ! pricesId.isEmpty() ) {  prices = this.priceService.getPrices( pricesId ); }
        else { prices = this.priceService.findAll(); }
        if( prices == null || prices.isEmpty() ){ return new ResponseEntity<>( HttpStatus.NOT_FOUND ); }
        List<PriceDTO> priceDTOList = this.priceService.toDTO( prices );
        return new ResponseEntity<>( priceDTOList, HttpStatus.FOUND );
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping(value = URL_PRICE , produces= MediaType.APPLICATION_JSON_UTF8_VALUE  )
    public ResponseEntity<PriceDTO> getPrice( @PathVariable("price_id") Integer id ){
        Price price = this.priceService.getOnePrice( id );
        if( price == null ) return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        PriceDTO priceDTO = this.priceService.toDTO( price );
        return new ResponseEntity<>( priceDTO , priceDTO != null ? HttpStatus.FOUND : HttpStatus.NOT_FOUND );
    }

    /**
     *
     * @param propertyId
     * @return
     */
    @DeleteMapping( value = URL_PRICE , produces= MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<String> deletePrice( @PathVariable("price_id") Integer propertyId ){
        Price price = this.priceService.find( propertyId );
        if( price == null ){
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
        this.priceService.delete( price );
        return new ResponseEntity<>( HttpStatus.GONE );
    }


}
