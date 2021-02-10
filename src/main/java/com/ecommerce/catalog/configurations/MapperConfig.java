package com.ecommerce.catalog.configurations;

import com.ecommerce.catalog.dto.common.LanguageDTO;
import com.ecommerce.catalog.sql.entity.common.Language;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper geModelMapper(){
        return this.initConfig( new ModelMapper() );
    }


    public ModelMapper initConfig( ModelMapper modelMapper )
    {
        // CONFIG LANGUAGE
        modelMapper.typeMap(Language.class, LanguageDTO.class)
                .addMapping(Language::getLanguageTranslationList, LanguageDTO::setTranslations );

        return modelMapper;
    }

}
