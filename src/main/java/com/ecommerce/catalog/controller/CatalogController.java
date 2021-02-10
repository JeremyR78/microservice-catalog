package com.ecommerce.catalog.controller;

import com.ecommerce.catalog.dto.product.ProductView;
import com.ecommerce.catalog.exceptions.*;
import com.ecommerce.catalog.nosql.data.ProductEs;
import com.ecommerce.catalog.nosql.service.ProductEsService;
import com.ecommerce.catalog.sql.entity.category.Category;
import com.ecommerce.catalog.sql.entity.common.Language;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.entity.customer.CustomerGroup;
import com.ecommerce.catalog.sql.entity.price.Currency;
import com.ecommerce.catalog.sql.entity.product.Product;
import com.ecommerce.catalog.service.CatalogService;
import com.ecommerce.catalog.sql.service.common.LanguageService;
import com.ecommerce.catalog.sql.service.customer.CustomerGroupService;
import com.ecommerce.catalog.sql.service.price.CurrencyService;
import com.ecommerce.catalog.sql.service.product.ProductService;
import com.ecommerce.catalog.sql.service.category.CategoryService;
import com.ecommerce.catalog.sql.service.common.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CatalogController extends AbstractController {


    public static final String URL_PRODUCTS_VIEW   = "/view/products" ;
    public static final String URL_PRODUCT_VIEW    = "/view/products/{product_id}" ;

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private final ProductService productService;
    private final CategoryService categoryService;
    private final StoreService storeService;
    private final CatalogService catalogService;
    private final LanguageService languageService;
    private final CurrencyService currencyService;
    private final ProductEsService productEsService;
    private final CustomerGroupService customerGroupService;

    // --------------------------------------
    // -        Methods  - API              -
    // --------------------------------------

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories()
    {
        List<Category> categories = this.categoryService.findAll();
        return new ResponseEntity( categories, HttpStatus.OK);
    }

    @GetMapping("/categories/{store_id}")
    public ResponseEntity<List<Category>> getCategories( @PathVariable("store_id") Integer storeID )
    {
        Store store = this.storeService.find( storeID );
        List<Category> categories = this.categoryService.findCategories( store );
        return new ResponseEntity( categories, HttpStatus.OK);
    }

    @GetMapping("/categories/{store_id}/{category_id}")
    public ResponseEntity<Category> getCategory( @PathVariable("store_id") Integer storeID,
                                                 @PathVariable("category_id") Integer categoryID )
    {
        Store store = this.storeService.find( storeID );
        Category category = this.categoryService.getCategory( categoryID, store );
        return new ResponseEntity<>( category, HttpStatus.OK);
    }

    @PostMapping("/categories/{store_id}/{category_id}")
    public ResponseEntity<Category> addCategory( @PathVariable("store_id") Integer storeID )
    {
        Store store = this.storeService.find( storeID );
        return new ResponseEntity<>( HttpStatus.OK);
    }


    /**
     *
     * @param storeId
     * @param languageId
     * @param currencyId
     * @return
     */
    @GetMapping(value = URL_PRODUCTS_VIEW, produces= MediaType.APPLICATION_JSON_VALUE )
    public List<ProductView> getAllProducts(
            @RequestParam(value = "store_id", required = false ) Integer storeId,
            @RequestParam(value = "language_id", required = false ) Integer languageId,
            @RequestParam(value = "currency_id", required = false ) Integer currencyId )
    {

        Store store = null;
        Language language = null;
        Currency currency = null;
        List<Product> products = this.productService.findAll();

        if( products == null || products.isEmpty() ){
            return new ArrayList<>(); //new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }

        if( storeId != null ) {
            store = this.storeService.find( storeId );
            if( store == null ){
                throw new StoreNotFoundException( storeId );
            }
        }

        if( languageId != null ){
            language = this.languageService.find( languageId );
            if( language == null ){
                throw new LanguageNotFoundException( languageId );
            }
        }

        if( currencyId != null ){
            currency = this.currencyService.find( currencyId );
            if( currency == null ){
                throw new CurrencyNotFoundException( currencyId );
            }
        }

        List<ProductView> productViews = this.catalogService.toViewDTO( products,
                language, store, currency );

        return  productViews;
    }

    /**
     *
     * @param id
     * @param storeId
     * @param languageId
     * @param currencyId
     * @return
     */
    @GetMapping( value = URL_PRODUCT_VIEW , produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<ProductView> getProduct( @PathVariable(value = "product_id") Integer id,
                   @RequestParam(value = "store_id") Integer storeId,
                   @RequestParam(value = "language_id" ) Integer languageId,
                   @RequestParam(value = "currency_id" ) Integer currencyId,
                   @RequestParam(value = "customer_group_id" ) Integer customerGroupId                               )
    {
        Store store = this.storeService.findWithOptional( storeId ).orElseThrow( () -> new StoreNotFoundException( storeId ));
        Language language = this.languageService.findWithOptional( languageId ).orElseThrow( () -> new LanguageNotFoundException( languageId ));
        Currency currency = this.currencyService.findWithOptional( currencyId ).orElseThrow( () -> new CurrencyNotFoundException( currencyId ));
        CustomerGroup customerGroup = this.customerGroupService.findWithOptional( customerGroupId ).orElseThrow( () -> new CustomerGroupNotFoundException( customerGroupId ));
        ProductEs productEsOptional = this.productEsService.findById( id, store, language ).orElseThrow( () -> new ProductNotFoundException( id ));;
        ProductView productView = this.catalogService.toViewDTO( productEsOptional, currency, customerGroup );
        return  ResponseEntity.ok( productView );
    }



    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

}
