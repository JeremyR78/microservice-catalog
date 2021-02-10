package com.ecommerce.catalog.sql.service.property;

import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.dto.property.CreatePropertyValue;
import com.ecommerce.catalog.dto.property.CreatePropertyValueTranslation;
import com.ecommerce.catalog.dto.property.PropertyValueDTO;
import com.ecommerce.catalog.dto.property.PropertyValueTranslationDTO;
import com.ecommerce.catalog.exceptions.LanguageNotFoundException;
import com.ecommerce.catalog.model.PropertyType;
import com.ecommerce.catalog.service.AbstractService;
import com.ecommerce.catalog.sql.dao.property.PropertyValueDao;
import com.ecommerce.catalog.sql.entity.common.Language;
import com.ecommerce.catalog.sql.entity.product.ProductTranslation_;
import com.ecommerce.catalog.sql.entity.property.*;
import com.ecommerce.catalog.sql.service.common.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyValueService extends AbstractService<PropertyValue, Integer>  {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private final PropertyValueDao propertyValueDao;
    private final LanguageService languageService;
    private final ConvertDTO<PropertyValue, PropertyValueDTO> dtoService;

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------


    @Override
    protected JpaRepository<PropertyValue, Integer> getDao() {
        return this.propertyValueDao;
    }

    public List<PropertyValueDTO> toDTO( List<PropertyValue> properties ){
        List<PropertyValueDTO> propertyValueDTOList = new ArrayList<>();
        if( properties != null ) {
            properties.forEach(propertyValue -> propertyValueDTOList.add(this.toDTO(propertyValue)));
        }
        return propertyValueDTOList;
    }

    public PropertyValueDTO toDTO( PropertyValue propertyValue ) {
        PropertyValueDTO propertyValueDTO = new PropertyValueDTO();
        propertyValueDTO.setId( propertyValue.getId() );

        if( propertyValue instanceof PropertyValueBoolean ){
            PropertyValueBoolean propertyValueBoolean = (PropertyValueBoolean) propertyValue;
            propertyValueDTO.setType( PropertyType.BOOLEAN );
            propertyValueDTO.setValueBoolean( propertyValueBoolean.getValue() );
        }
        else if ( propertyValue instanceof PropertyValueNumber ){
            PropertyValueNumber propertyValueNumber = (PropertyValueNumber) propertyValue ;
            propertyValueDTO.setType( PropertyType.NUMBER );
            propertyValueDTO.setValueNumber( propertyValueNumber.getValue() );
        }
        else if ( propertyValue instanceof PropertyValueString  ){
            PropertyValueString propertyValueString = (PropertyValueString) propertyValue;
            propertyValueDTO.setType( PropertyType.STRING );
            List<PropertyValueTranslation> propertyValueTranslations = propertyValueString.getTranslationList();
            List<PropertyValueTranslationDTO> propertyValueTranslationsDTO = new ArrayList<>();
            if( propertyValueTranslations != null ) {
                propertyValueTranslations.forEach(propertyValueTranslation -> {
                    PropertyValueTranslationDTO propertyValueTranslationDTO = new PropertyValueTranslationDTO();
                    propertyValueTranslationDTO.setLabel(propertyValueTranslation.getLabel());
                    propertyValueTranslationDTO.setLanguageId(propertyValueTranslation.getLanguage().getId());
                    propertyValueTranslationsDTO.add(propertyValueTranslationDTO);
                });
            }
            propertyValueDTO.setValueTranslations( propertyValueTranslationsDTO );
        }

        return propertyValueDTO;
    }

    public List<PropertyValue> toEntity( List<PropertyValueDTO> propertyValueDTOList ) {
        List<PropertyValue> propertyValueList = new ArrayList<>();
        if( propertyValueDTOList != null ) {
            propertyValueDTOList.forEach( createPropertyValue -> {
                propertyValueList.add( this.toEntity( createPropertyValue ));
            });
        }
        return propertyValueList;
    }

    public PropertyValue toEntity( PropertyValueDTO propertyDTO ) {
        PropertyValue propertyValue = null;
        switch ( propertyDTO.getType()) {
            case NUMBER:
                PropertyValueNumber propertyValueNumber = new PropertyValueNumber();
                propertyValueNumber.setValue(propertyDTO.getValueNumber());
                propertyValue = propertyValueNumber;
                break;
            case STRING:
                PropertyValueString propertyValueString = new PropertyValueString();
                List<PropertyValueTranslationDTO> createTranslations = propertyDTO.getValueTranslations();
                List<PropertyValueTranslation> propertyValueTranslations = new ArrayList<>();
                if ( createTranslations != null) {
                    createTranslations.forEach(createPropertyValueTranslation -> {
                        PropertyValueTranslation propertyValueTranslation = new PropertyValueTranslation();
                        Integer languageId = createPropertyValueTranslation.getLanguageId();
                        Language language = this.languageService.findWithOptional(languageId).orElseThrow(() -> new LanguageNotFoundException(languageId));
                        propertyValueTranslation.setLanguage(language);
                        propertyValueTranslation.setLabel(createPropertyValueTranslation.getLabel());
                        propertyValueTranslations.add(propertyValueTranslation);
                    });
                }
                propertyValueString.setTranslationList(propertyValueTranslations);
                propertyValue = propertyValueString;
                break;
            case BOOLEAN:
                PropertyValueBoolean propertyValueBoolean = new PropertyValueBoolean();
                propertyValueBoolean.setValue(propertyDTO.getValueBoolean());
                propertyValue = propertyValueBoolean;
                break;
        }
        return propertyValue;
    }

    public List<PropertyValue> createToEntity( List<CreatePropertyValue> createPropertyValueDTOList , Property property ) {
        List<PropertyValue> propertyValueList = new ArrayList<>();
        if( createPropertyValueDTOList != null ) {
            createPropertyValueDTOList.forEach( createPropertyValue -> {
                propertyValueList.add( this.toEntity( createPropertyValue, property ));
            });
        }
        return propertyValueList;
    }

    public PropertyValue toEntity( CreatePropertyValue createPropertyValue, Property property ) {
        PropertyValue propertyValue = null;
        switch ( createPropertyValue.getType() ){
            case NUMBER:
                PropertyValueNumber propertyValueNumber = new PropertyValueNumber();
                propertyValueNumber.setValue( createPropertyValue.getValueNumber() );
                propertyValue = propertyValueNumber;
                break;
            case STRING:
                PropertyValueString propertyValueString = new PropertyValueString();
                List<CreatePropertyValueTranslation> createTranslations = createPropertyValue.getValueTranslations();
                List<PropertyValueTranslation> propertyValueTranslations = new ArrayList<>();
                if( createTranslations != null ){
                    createTranslations.forEach( createPropertyValueTranslation -> {
                        PropertyValueTranslation propertyValueTranslation = new PropertyValueTranslation();
                        Integer languageId = createPropertyValueTranslation.getLanguageId();
                        Language language = this.languageService.findWithOptional( languageId ).orElseThrow( () -> new LanguageNotFoundException(languageId));
                        propertyValueTranslation.setLanguage( language );
                        propertyValueTranslation.setLabel( createPropertyValueTranslation.getValue() );
                        propertyValueTranslations.add( propertyValueTranslation );
                    });
                }
                propertyValueString.setTranslationList( propertyValueTranslations );
                propertyValue = propertyValueString;
                break;
            case BOOLEAN:
                PropertyValueBoolean propertyValueBoolean = new PropertyValueBoolean();
                propertyValueBoolean.setValue( createPropertyValue.getValueBoolean() );
                propertyValue = propertyValueBoolean;
                break;
        }
        propertyValue.setProperty( property );
        return propertyValue;
    }

}
