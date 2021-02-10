package com.ecommerce.catalog.sql.service.common;

import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.dto.common.CountryDTO;
import com.ecommerce.catalog.dto.common.CountryTranslationDTO;
import com.ecommerce.catalog.dto.common.CreateCountryTranslation;
import com.ecommerce.catalog.exceptions.LanguageNotFoundException;
import com.ecommerce.catalog.service.AbstractService;
import com.ecommerce.catalog.sql.dao.common.CountryDao;
import com.ecommerce.catalog.sql.dao.common.CountryTranslationDao;
import com.ecommerce.catalog.sql.entity.common.Country;
import com.ecommerce.catalog.sql.entity.common.CountryTranslation;
import com.ecommerce.catalog.sql.entity.common.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CountryTranslationService extends AbstractService<CountryTranslation, Integer>  {

    // --------------------------------------
    // -        Services                    -
    // --------------------------------------

    private final CountryTranslationDao countryTranslationDao;
    private final ConvertDTO<CountryTranslation, CountryTranslationDTO> dtoService;

    private final LanguageService languageService;

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @Override
    protected CountryTranslationDao getDao() {
        return this.countryTranslationDao;
    }

    public CountryTranslationDTO toDto( CountryTranslation countryTranslation ){
        return this.dtoService.toDTO( countryTranslation, CountryTranslationDTO.class );
    }

    public CountryTranslation toEntity( CreateCountryTranslation createCountryTranslation, Country country ){
        CountryTranslation countryTranslation = new CountryTranslation();
        countryTranslation.setLabel( createCountryTranslation.getLabel() );

        Integer languageID = createCountryTranslation.getLanguageId();
        if( languageID == null ) throw new LanguageNotFoundException( languageID );
        Language language = this.languageService.find( languageID );
        if( language == null ) throw new LanguageNotFoundException( languageID );

        countryTranslation.setLanguage( language );
        countryTranslation.setCountry( country );
        return countryTranslation;
    }
}
