package com.ecommerce.catalog.sql.service.property;

import com.ecommerce.catalog.exceptions.LanguageNotFoundException;
import com.ecommerce.catalog.sql.dao.property.PropertyUnitDao;
import com.ecommerce.catalog.dto.property.PropertyUnitDTO;
import com.ecommerce.catalog.service.AbstractService;
import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.dto.property.CreatePropertyUnit;
import com.ecommerce.catalog.sql.entity.common.Language;
import com.ecommerce.catalog.sql.entity.property.PropertyUnit;
import com.ecommerce.catalog.sql.entity.property.PropertyUnitTranslation;
import com.ecommerce.catalog.sql.service.common.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyUnitService extends AbstractService<PropertyUnit, Integer> {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private final PropertyUnitDao propertyUnitDao;
    private final ConvertDTO<PropertyUnit, PropertyUnitDTO> dtoService;
    private final ConvertDTO<PropertyUnit, CreatePropertyUnit> dtoCreatePropertyService;

    private final LanguageService languageService;

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------

    @Override
    protected JpaRepository<PropertyUnit, Integer> getDao() {
        return this.propertyUnitDao;
    }


    //
    //  -   CONVERT
    //

    public List<PropertyUnitDTO> toDTO(List<PropertyUnit> propertiesUnit ){
        return this.dtoService.toDTO( propertiesUnit, PropertyUnitDTO.class );
    }

    public PropertyUnitDTO toDTO( PropertyUnit propertyUnit ) {
        return this.dtoService.toDTO( propertyUnit, PropertyUnitDTO.class );
    }

    public List<PropertyUnit> toEntity( List<PropertyUnitDTO> propertyDTOList ) {
        return this.dtoService.toEntity( propertyDTOList, PropertyUnit.class );
    }

    public PropertyUnit toEntity( PropertyUnitDTO propertyDTO ) {
        return this.dtoService.toEntity( propertyDTO, PropertyUnit.class );
    }


    public List<PropertyUnit> createToEntity( List<CreatePropertyUnit> propertyDTOList ) {
        return this.dtoCreatePropertyService.toEntity( propertyDTOList, PropertyUnit.class );
    }

    public PropertyUnit toEntity( CreatePropertyUnit propertyDTO ) {
        PropertyUnit propertyUnit = this.dtoCreatePropertyService.toEntity( propertyDTO, PropertyUnit.class );

        propertyUnit.getPropertyUnitTranslationList().forEach( translation -> {
            translation.setPropertyUnit( propertyUnit );
            Integer languageId = translation.getLanguage() != null ? translation.getLanguage().getId() : null;
            if( languageId == null ) throw new LanguageNotFoundException( languageId );
            Language language = this.languageService.find( languageId );
            if( language == null ) throw new LanguageNotFoundException( languageId );
            translation.setLanguage( language );
        });

        return propertyUnit;
    }

}
