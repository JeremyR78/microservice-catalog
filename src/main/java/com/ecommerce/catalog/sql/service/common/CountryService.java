package com.ecommerce.catalog.sql.service.common;

import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.dto.common.CountryDTO;
import com.ecommerce.catalog.dto.common.CountryTranslationDTO;
import com.ecommerce.catalog.dto.common.CreateCountry;
import com.ecommerce.catalog.service.AbstractService;
import com.ecommerce.catalog.sql.dao.common.CountryDao;
import com.ecommerce.catalog.sql.entity.common.Country;
import com.ecommerce.catalog.sql.entity.common.CountryTranslation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CountryService extends AbstractService<Country, Integer> {

    // --------------------------------------
    // -        Services                    -
    // --------------------------------------

    private final CountryDao countryDao;
    private final ConvertDTO<Country, CountryDTO> dtoService;

    private final CountryTranslationService countryTranslationService;

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @Override
    protected CountryDao getDao() {
        return this.countryDao;
    }

    public CountryDTO getByIdToDTO( Integer countryId ){
        Country country = this.find( countryId );
        return this.toDto( country );
    }

    public  List<CountryDTO> findAllToDTO(){
        List<Country> countries = this.countryDao.findAll();
        return toDto( countries );
    }


    public List<CountryDTO> toDto( List<Country> countries ){
        List<CountryDTO> countryDTOList = new ArrayList<>();
        countries.forEach( country -> {
            countryDTOList.add( this.toDto( country ));
        });
        return countryDTOList;
    }


    public CountryDTO toDto( Country country ){
        CountryDTO countryDTO = this.dtoService.toDTO( country, CountryDTO.class );

        List<CountryTranslation> countryTranslationList = country.getCountryTranslations();
        List<CountryTranslationDTO> countryTranslationDTOList = new ArrayList<>();

        countryTranslationList.forEach( countryTranslation -> {
            countryTranslationDTOList.add( this.countryTranslationService.toDto( countryTranslation ) );
        } );
        countryDTO.setCountryTranslationList( countryTranslationDTOList );
        return countryDTO;
    }

    public Country toEntity( CreateCountry createCountry ){
        Country country = new Country();

        country.setEnable( createCountry.getEnable() );

        List<CountryTranslation> countryTranslations = new ArrayList<>();
        createCountry.getCountryTranslations().forEach( countryTranslation -> {
            countryTranslations.add( this.countryTranslationService.toEntity( countryTranslation, country ));
        });
        country.setCountryTranslations( countryTranslations );
        return country;
    }

}
