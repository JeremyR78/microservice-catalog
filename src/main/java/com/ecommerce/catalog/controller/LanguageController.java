package com.ecommerce.catalog.controller;

import com.ecommerce.catalog.dto.common.LanguageDTO;
import com.ecommerce.catalog.sql.entity.common.Language;
import com.ecommerce.catalog.sql.service.common.LanguageService;
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
public class LanguageController extends AbstractController {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    public static final String URL_LANGUAGES   = "/languages" ;
    public static final String URL_LANGUAGE    = "/languages/{language_id}" ;

    private final LanguageService languageService;

    // --------------------------------------
    // -        Methods  - API              -
    // --------------------------------------

    @GetMapping(value = URL_LANGUAGES , produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<LanguageDTO>> getLanguages()
    {
        List<Language> languages = this.languageService.findAll();
        if( languages == null || languages.isEmpty() ) return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        List<LanguageDTO> languageDTOList = this.languageService.toDTO( languages );
        return new ResponseEntity<>( languageDTOList, HttpStatus.OK );
    }

    @GetMapping(value = URL_LANGUAGE , produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LanguageDTO> getLanguage( @PathVariable("language_id") Integer languageId )
    {
        Language language = this.languageService.find( languageId );
        if( language == null ){
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
        LanguageDTO languageDTO = this.languageService.toDTO( language );
        return new ResponseEntity<>( languageDTO, HttpStatus.FOUND );
    }

    @PostMapping(value = URL_LANGUAGES, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<LanguageDTO> createLanguage( @RequestBody LanguageDTO languageDTO,
                                                       @Context HttpServletRequest requestContext  )
    {
        Language language = this.languageService.toEntity( languageDTO );
        language = this.languageService.save( language );
        LanguageDTO languageDTOResult = this.languageService.toDTO( language );
        return new ResponseEntity<>( languageDTOResult, HttpStatus.CREATED );
    }

    @DeleteMapping( value = URL_LANGUAGE )
    public ResponseEntity<String> deleteLanguage(@PathVariable("language_id") Integer languageId ){
        Language language = this.languageService.find( languageId );
        if( language == null ) return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        this.languageService.delete( language );
        return new ResponseEntity<>( HttpStatus.GONE );
    }

}
