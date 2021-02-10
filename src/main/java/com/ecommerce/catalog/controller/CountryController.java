package com.ecommerce.catalog.controller;

import com.ecommerce.catalog.dto.common.CountryDTO;
import com.ecommerce.catalog.dto.common.CreateCountry;
import com.ecommerce.catalog.dto.price.CurrencyDTO;
import com.ecommerce.catalog.sql.entity.common.Country;
import com.ecommerce.catalog.sql.entity.price.Currency;
import com.ecommerce.catalog.sql.service.common.CountryService;
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
public class CountryController extends AbstractController {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    public static final String URL_COUNTRIES   = "/countries" ;
    public static final String URL_COUNTRY     = "/countries/{country_id}" ;

    private final CountryService countryService;

    // --------------------------------------
    // -        Methods  - API              -
    // --------------------------------------

    @GetMapping(value = URL_COUNTRIES , produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<List<CountryDTO>> getCountries()
    {
        List<CountryDTO> countryList = this.countryService.findAllToDTO();
        return new ResponseEntity<>( countryList, HttpStatus.OK );
    }

    @GetMapping(value = URL_COUNTRY , produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<CountryDTO> getCountry( @PathVariable("country_id") Integer currencyId )
    {
        CountryDTO currency = this.countryService.getByIdToDTO( currencyId );
        return new ResponseEntity<>( currency, currency == null ? HttpStatus.NOT_FOUND : HttpStatus.FOUND );
    }


    @PostMapping(value = URL_COUNTRIES, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<CountryDTO> createCountry (@RequestBody CreateCountry createCountry,
                                                     @Context HttpServletRequest requestContext ) {
        Country country = this.countryService.toEntity( createCountry );
        country = this.countryService.save( country );
        CountryDTO countryDTOResult = this.countryService.toDto( country );
        return new ResponseEntity<>( countryDTOResult, HttpStatus.CREATED );
    }

    @DeleteMapping( value = URL_COUNTRY )
    public ResponseEntity<String> deleteCountry( @PathVariable("country_id") Integer countryId ){
        Country country = this.countryService.find( countryId );
        if( country == null ) return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        this.countryService.delete( country );
        return new ResponseEntity<>( HttpStatus.GONE );
    }

}
