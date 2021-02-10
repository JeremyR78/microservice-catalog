package com.ecommerce.catalog.controller;


import com.ecommerce.catalog.dto.price.CurrencyDTO;
import com.ecommerce.catalog.sql.entity.price.Currency;
import com.ecommerce.catalog.sql.service.price.CurrencyService;
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
public class CurrencyController extends AbstractController  {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    public static final String URL_CURRENCIES   = "/currencies" ;
    public static final String URL_CURRENCY     = "/currencies/{currency_id}" ;

    private final CurrencyService currencyService;

    // --------------------------------------
    // -        Methods  - API              -
    // --------------------------------------

    @GetMapping(value = URL_CURRENCIES , produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<List<CurrencyDTO>> getCurrencies()
    {
        List<CurrencyDTO> currencies = this.currencyService.findAllToDTO();
        return new ResponseEntity<>( currencies, HttpStatus.OK );
    }

    @GetMapping(value = URL_CURRENCY , produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<CurrencyDTO> getCurrency( @PathVariable("currency_id") Integer currencyId )
    {
        CurrencyDTO currency = this.currencyService.getByIdToDTO( currencyId );
        return new ResponseEntity<>( currency, currency == null ? HttpStatus.NOT_FOUND : HttpStatus.FOUND );
    }


    @PostMapping(value = URL_CURRENCIES, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<CurrencyDTO> createCurrency ( @RequestBody CurrencyDTO currencyDTO,
                                                  @Context HttpServletRequest requestContext ) {
        Currency currency = this.currencyService.toEntity( currencyDTO );
        currency = this.currencyService.save( currency );
        CurrencyDTO currencyDTOResult = this.currencyService.toDTO( currency );
        return new ResponseEntity<>( currencyDTOResult, HttpStatus.CREATED );
    }

    @DeleteMapping( value = URL_CURRENCY )
    public ResponseEntity<String> deleteCurrency( @PathVariable("currency_id") Integer currencyId ){
        Currency currency = this.currencyService.find( currencyId );
        if( currency == null ) return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        this.currencyService.delete( currency );
        return new ResponseEntity<>( HttpStatus.GONE );
    }

}
