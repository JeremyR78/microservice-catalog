package com.ecommerce.catalog.sql.service.category;

import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.dto.category.CategoryTranslationDTO;
import com.ecommerce.catalog.service.AbstractService;
import com.ecommerce.catalog.sql.dao.category.CategoryTranslationDao;
import com.ecommerce.catalog.sql.entity.category.CategoryTranslation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryTranslationService extends AbstractService<CategoryTranslation, Integer>  {

    // --------------------------------------
    // -        Services                    -
    // --------------------------------------

    private final CategoryTranslationDao categoryTranslationDao;
    private final ConvertDTO<CategoryTranslation, CategoryTranslationDTO> dtoService;

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @Override
    protected CategoryTranslationDao getDao() {
        return categoryTranslationDao;
    }


    public CategoryTranslationDTO toDto( CategoryTranslation categoryTranslation ){
        return this.dtoService.toDTO( categoryTranslation, CategoryTranslationDTO.class );
    }

    public List<CategoryTranslationDTO> toDto( List<CategoryTranslation> categoryTranslations ){
        if( categoryTranslations == null || categoryTranslations.isEmpty() ) return new ArrayList<>();
        return this.dtoService.toDTO( categoryTranslations, CategoryTranslationDTO.class );
    }

}
