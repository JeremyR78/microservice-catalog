package com.ecommerce.catalog.controller;


import com.ecommerce.catalog.dto.category.CategoryDTO;
import com.ecommerce.catalog.dto.category.CreateCategory;
import com.ecommerce.catalog.sql.entity.category.Category;
import com.ecommerce.catalog.sql.service.category.CategoryService;
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
public class CategoryController extends AbstractController {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    public static final String URL_CATEGORIES   = "/categories" ;
    public static final String URL_CATEGORY     = "/categories/{category_id}" ;

    private final CategoryService categoryService;


    // --------------------------------------
    // -        Methods  - API              -
    // --------------------------------------

    @GetMapping(value = URL_CATEGORIES , produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<List<CategoryDTO>> getCountries()
    {
        List<CategoryDTO> categoryList = this.categoryService.findAllToDTO();
        return new ResponseEntity<>( categoryList, HttpStatus.OK );
    }

    @GetMapping(value = URL_CATEGORY , produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<CategoryDTO> getCategory( @PathVariable("category_id") Integer categoryId )
    {
        CategoryDTO currency = this.categoryService.getByIdToDTO( categoryId );
        return new ResponseEntity<>( currency, currency == null ? HttpStatus.NOT_FOUND : HttpStatus.FOUND );
    }


    @PostMapping(value = URL_CATEGORIES, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<CategoryDTO> createCategory (@RequestBody CreateCategory createCountry,
                                                     @Context HttpServletRequest requestContext ) {
        Category category = this.categoryService.toEntity( createCountry );
        category = this.categoryService.save( category );
        CategoryDTO categoryDTOResult = this.categoryService.toDto( category );
        return new ResponseEntity<>( categoryDTOResult, HttpStatus.CREATED );
    }

    @DeleteMapping( value = URL_CATEGORY )
    public ResponseEntity<String> deleteCountry( @PathVariable("category_id") Integer categoryId ){
        Category category = this.categoryService.find( categoryId );
        if( category == null ) return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        this.categoryService.delete( category );
        return new ResponseEntity<>( HttpStatus.GONE );
    }

}
