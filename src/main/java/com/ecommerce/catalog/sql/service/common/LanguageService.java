package com.ecommerce.catalog.sql.service.common;

import com.ecommerce.catalog.dto.common.LanguageTranslationDTO;
import com.ecommerce.catalog.exceptions.LanguageNotFoundException;
import com.ecommerce.catalog.sql.dao.common.LanguageDao;
import com.ecommerce.catalog.sql.dao.common.LanguageTranslationDao;
import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.dto.common.LanguageDTO;
import com.ecommerce.catalog.sql.entity.common.Language;
import com.ecommerce.catalog.sql.entity.common.LanguageTranslation;
import com.ecommerce.catalog.service.AbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LanguageService extends AbstractService<Language, Integer> {

    // --------------------------------------
    // -        Services                    -
    // --------------------------------------

    private final LanguageDao languageDao;
    private final LanguageTranslationDao languageTranslationDao;
    private final ConvertDTO<Language, LanguageDTO> dtoService;

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @Override
    protected LanguageDao getDao() {
        return this.languageDao;
    }

    @Override
    public Language save( Language language )
    {
        if( language.getLanguageTranslationList() != null ){
            for( LanguageTranslation languageTranslation : language.getLanguageTranslationList() ){
                this.save( languageTranslation );
            }
        }
        return super.save( language );
    }

    public LanguageTranslation save( LanguageTranslation languageTranslation ){
        return this.languageTranslationDao.save( languageTranslation );
    }

    public LanguageTranslation findLanguageTranslation( Integer languageTranslationId ){
        return this.languageTranslationDao.getOne( languageTranslationId );
    }

    public List<LanguageTranslation> findAllLanguageTranslation(){
        return this.languageTranslationDao.findAll();
    }

    public List<LanguageDTO> toDTO(List<Language> languages ){
        return this.dtoService.toDTO( languages, LanguageDTO.class );
    }

    public LanguageDTO toDTO( Language language ) {
        return this.dtoService.toDTO( language, LanguageDTO.class );
    }

    public List<Language> toEntity( List<LanguageDTO> languageDTOList ) {
        return this.dtoService.toEntity( languageDTOList, Language.class );
    }

    public Language toEntity( LanguageDTO languageDTO ) {

        Language language = this.dtoService.toEntity( languageDTO, Language.class );
        List<LanguageTranslationDTO> languageDTOTranslations = languageDTO.getTranslations();
        List<LanguageTranslation> languageTranslations = new ArrayList<>();
        for( LanguageTranslationDTO translationDTO : languageDTOTranslations ){

            LanguageTranslation languageTranslation = new LanguageTranslation();
            languageTranslation.setLabel( translationDTO.getLabel() );

            Integer languageId = translationDTO.getLanguageReferenceID();
            if( languageId == null ) {
                languageTranslation.setLanguage( language );
                languageTranslation.setLanguageReference( language );
            }
            else {
                Language languageRef = this.find( languageId );
                if( languageRef == null ) throw new LanguageNotFoundException( languageId );
                languageTranslation.setLanguage( languageRef );
                languageTranslation.setLanguageReference( languageRef );
            }

            languageTranslations.add( languageTranslation );
        }
        language.setLanguageTranslationList( languageTranslations );
        return language;
    }


}
