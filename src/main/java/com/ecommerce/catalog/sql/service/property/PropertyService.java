package com.ecommerce.catalog.sql.service.property;

import com.ecommerce.catalog.dto.property.*;
import com.ecommerce.catalog.exceptions.LanguageNotFoundException;
import com.ecommerce.catalog.exceptions.PropertyUnitNotFoundException;
import com.ecommerce.catalog.sql.dao.property.PropertyDao;
import com.ecommerce.catalog.sql.dao.property.PropertyUnitDao;
import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.sql.entity.common.Language;
import com.ecommerce.catalog.sql.entity.property.*;
import com.ecommerce.catalog.service.AbstractService;
import com.ecommerce.catalog.sql.service.common.LanguageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService extends AbstractService<Property, Integer> {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private final PropertyDao propertyDao;
    private final PropertyUnitDao propertyUnitDao;
    private final LanguageService languageService;
    private final PropertyUnitService propertyUnitService;
    private final PropertyValueService propertyValueService;
//    private final ConvertDTO<Property, PropertyDTO> dtoService;
//    private final ConvertDTO<Property, CreateProperty> dtoCreatePropertyService;

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------

    @Override
    protected JpaRepository<Property, Integer> getDao() {
        return this.propertyDao;
    }

    @PostConstruct
    public void init(){
//         this.dtoCreatePropertyService.getModelMapper().addMappings(new PropertyMap<Property, CreateProperty>() {
//            @Override
//            protected void configure() {
//                // IGNORE PROPERTY VALUE ( ABSTRACT CLASS )
//                skip(destination.getPropertyValues());
//                //skip(source.getPropertyValues());
//            }
//        });
//        this.dtoService.getModelMapper().addMappings(new PropertyMap<Property, CreateProperty>() {
//            @Override
//            protected void configure() {
//                // IGNORE PROPERTY VALUE ( ABSTRACT CLASS )
//                skip(destination.getPropertyValues());
//                //skip(source.getPropertyValues());
//            }
//        });
    }

    //
    //  -   CONVERT
    //

    public List<PropertyDTO> toDTO(List<Property> properties ){
        List<PropertyDTO> propertiesDto = new ArrayList<>();
        for( Property property : properties ){
            propertiesDto.add( this.toDTO( property ) );
        }
        return propertiesDto;
    }

    public PropertyDTO toDTO( Property property ) {
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setId( property.getId() );
        propertyDTO.setEnable( property.isEnable() );

        // PROPERTY UNIT
        propertyDTO.setPropertyUnit( this.propertyUnitService.toDTO( property.getPropertyUnit() ));
        // PROPERTY VALUE
        propertyDTO.setPropertyValues( this.propertyValueService.toDTO( property.getPropertyValues() ));
        // TRANSLATION
        List<PropertyTranslation> translations = property.getTranslations();
        List<PropertyTranslationDTO> translationDTOS = new ArrayList<>();
        if( translations != null ) {
            for (PropertyTranslation propertyTranslation : translations) {
                PropertyTranslationDTO propertyTranslationDTO = new PropertyTranslationDTO();
                propertyTranslationDTO.setLabel(propertyTranslation.getLabel());
                propertyTranslationDTO.setLanguageId(propertyTranslation.getLanguage().getId());
                translationDTOS.add(propertyTranslationDTO);
            }
        }
        propertyDTO.setTranslations( translationDTOS );
        return propertyDTO;
    }

    public List<Property> toEntity( List<PropertyDTO> propertyDTOList ) {
        List<Property> properties = new ArrayList<>();
        for( PropertyDTO propertyDTO : propertyDTOList ){
            properties.add( this.toEntity( propertyDTO ) );
        }
        return properties;
    }

    public Property toEntity( PropertyDTO propertyDTO ) {
        Property property = new Property();
        property.setId( propertyDTO.getId() );
        property.setEnable( propertyDTO.isEnable() );

        // TRANSLATION
        List<PropertyTranslationDTO> propertyValueTranslationDTOList = propertyDTO.getTranslations();
        List<PropertyTranslation> propertyValueTranslations = new ArrayList<>();
        if( propertyValueTranslationDTOList != null ){
            for( PropertyTranslationDTO propertyTranslationDTO : propertyValueTranslationDTOList ){
                PropertyTranslation propertyTranslation = new PropertyTranslation();
                propertyTranslation.setProperty( property );
                propertyTranslation.setLabel( propertyTranslationDTO.getLabel() );
                Integer languageId = propertyTranslationDTO.getLanguageId();
                Language language = this.languageService.findWithOptional( languageId ).orElseThrow(() -> new LanguageNotFoundException(languageId  ));
                propertyTranslation.setLanguage( language );
                propertyValueTranslations.add( propertyTranslation );
            }
        }
        property.setTranslations( propertyValueTranslations );

        // PROPERTY UNIT
        property.setPropertyUnit( this.propertyUnitService.toEntity( propertyDTO.getPropertyUnit() ));

        // PROPERTY VALUE
        property.setPropertyValues( this.propertyValueService.toEntity( propertyDTO.getPropertyValues() ));

        return property;
    }

    public List<Property> newPropertyToEntity( List<CreateProperty> propertyDTOList ) {
        List<Property> properties = new ArrayList<>();
        for( CreateProperty createProperty : propertyDTOList ){
            properties.add( this.toEntity( createProperty ) );
        }
        return properties;
    }

    public Property toEntity( CreateProperty createProperty ) {
        Property property = new Property();
        property.setEnable( createProperty.isEnable() );

        // PROPERTY UNIT
        property.setPropertyUnit( this.propertyUnitService.findWithOptional( createProperty.getPropertyUnitId() ).orElseThrow( () -> new PropertyUnitNotFoundException( createProperty.getPropertyUnitId() )));

        // PROPERTY VALUE
        property.setPropertyValues( this.propertyValueService.createToEntity( createProperty.getPropertyValues(), property ));

        // TRANSLATIONS
        List<CreatePropertyTranslation> propertyValueTranslationDTOList = createProperty.getTranslations();
        List<PropertyTranslation> propertyValueTranslations = new ArrayList<>();
        if( propertyValueTranslationDTOList != null ){
            for( CreatePropertyTranslation propertyTranslationDTO : propertyValueTranslationDTOList ){
                PropertyTranslation propertyTranslation = new PropertyTranslation();
                propertyTranslation.setProperty( property );
                propertyTranslation.setLabel( propertyTranslationDTO.getLabel() );
                Integer languageId = propertyTranslationDTO.getLanguageId();
                Language language = this.languageService.findWithOptional( languageId ).orElseThrow(() -> new LanguageNotFoundException(languageId  ));
                propertyTranslation.setLanguage( language );
                propertyValueTranslations.add( propertyTranslation );
            }
        }
        property.setTranslations( propertyValueTranslations );

        return property;
    }

}
;