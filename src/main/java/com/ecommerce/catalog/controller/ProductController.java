package com.ecommerce.catalog.controller;

import com.ecommerce.catalog.dto.product.CreateProduct;
import com.ecommerce.catalog.dto.product.ProductDTO;
import com.ecommerce.catalog.sql.entity.product.Product;
import com.ecommerce.catalog.service.CatalogService;
import com.ecommerce.catalog.sql.service.product.ProductService;
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
public class ProductController extends AbstractController {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    public static final String URL_PRODUCTS   = "/products" ;
    public static final String URL_PRODUCT    = "/products/{id}" ;

    private final CatalogService catalogService;
    private final ProductService productService;

    // --------------------------------------
    // -        Methods  - API              -
    // --------------------------------------

    @GetMapping(value = URL_PRODUCTS , produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<List<ProductDTO>> getAllProducts(
            @RequestParam(value = "store_id", required = false ) Integer storeId,
            @RequestParam(value = "language_id", required = false ) Integer languageId,
            @RequestParam(value = "currency_id", required = false ) Integer currencyId )
    {
        List<Product> products = this.catalogService.findProducts( storeId, languageId, currencyId );

        if( products == null || products.isEmpty() ) new ResponseEntity<>( HttpStatus.NOT_FOUND );

        List<ProductDTO> productDTOList = this.productService.toDTO( products );
        return new ResponseEntity<>( productDTOList, HttpStatus.FOUND );
    }

    @GetMapping( value = URL_PRODUCT , produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<ProductDTO> getProduct( @PathVariable("id") Integer id ) {
        Product product = this.productService.find( id );
        if( product == null ) return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        ProductDTO productDTO = this.productService.toDTO( product );
        return new ResponseEntity<>( productDTO, HttpStatus.FOUND );
    }

    @PostMapping(value = URL_PRODUCTS, produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<ProductDTO> createProduct( @RequestBody CreateProduct createProduct,
                                                      @Context HttpServletRequest requestContext )
    {
        Product product = this.productService.toEntity( createProduct );
        if( product == null ) return new ResponseEntity<>( HttpStatus.NOT_ACCEPTABLE );
        product = this.productService.saveWithNotify( null, product );
        ProductDTO productDTO = this.productService.toDTO( product );
        return new ResponseEntity<>( productDTO , HttpStatus.CREATED );
    }

}

