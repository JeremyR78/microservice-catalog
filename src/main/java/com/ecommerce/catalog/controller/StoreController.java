package com.ecommerce.catalog.controller;

import com.ecommerce.catalog.dto.common.StoreDTO;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.service.common.StoreService;
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
public class StoreController extends AbstractController {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    public static final String URL_STORES       = "/stores" ;
    public static final String URL_STORE        = "/stores/{store_id}" ;

    private final StoreService storeService;

    // --------------------------------------
    // -        Methods  - API              -
    // --------------------------------------

    @GetMapping( value = URL_STORES , produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StoreDTO>> getStores()
    {
        List<StoreDTO> stores = this.storeService.findAllToDTO();
        return new ResponseEntity<>( stores, HttpStatus.OK );
    }

    @GetMapping( value = URL_STORE , produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StoreDTO> getStore( @PathVariable("store_id") Integer storeId )
    {
        StoreDTO store = this.storeService.getByIdToDTO( storeId );
        return new ResponseEntity<>( store, store == null ? HttpStatus.NOT_FOUND : HttpStatus.FOUND );
    }


    @PostMapping( value = URL_STORES, produces= MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<StoreDTO> createStore ( @RequestBody StoreDTO storeDTO,
                                                @Context HttpServletRequest requestContext ) {
        Store store = this.storeService.toEntity( storeDTO );
        store = this.storeService.save( store );
        StoreDTO storeDTOResult = this.storeService.toDTO( store );
        return new ResponseEntity<>( storeDTOResult, HttpStatus.CREATED );
    }

    @DeleteMapping( value = URL_STORE )
    public ResponseEntity<String> deleteStore( @PathVariable("store_id") Integer storeId ){
        Store store = this.storeService.find( storeId );
        if( store == null ){
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
        this.storeService.delete( store );
        return new ResponseEntity<>( HttpStatus.GONE );
    }

}
