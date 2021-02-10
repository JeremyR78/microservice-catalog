package com.ecommerce.catalog.sql.service.category;

import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.dto.category.CategoryDTO;
import com.ecommerce.catalog.dto.category.CategoryTranslationDTO;
import com.ecommerce.catalog.dto.category.CreateCategory;
import com.ecommerce.catalog.exceptions.CategoryNotFoundException;
import com.ecommerce.catalog.exceptions.StoreNotFoundException;
import com.ecommerce.catalog.exceptions.TagNotFoundException;
import com.ecommerce.catalog.sql.entity.category.Category;
import com.ecommerce.catalog.sql.entity.category.QCategory;
import com.ecommerce.catalog.sql.entity.category.QCategoryProductLink;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.dao.category.CategoryDao;
import com.ecommerce.catalog.service.AbstractService;
import com.ecommerce.catalog.sql.entity.product.Product;
import com.ecommerce.catalog.sql.entity.product.QProduct;
import com.ecommerce.catalog.sql.entity.tag.Tag;
import com.ecommerce.catalog.sql.service.common.StoreService;
import com.ecommerce.catalog.sql.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService extends AbstractService<Category, Integer> {

    // --------------------------------------
    // -        Services                    -
    // --------------------------------------

    private final CategoryDao categoryDao;
    private final StoreService storeService;
    private final TagService tagService;
    private final ConvertDTO<Category, CategoryDTO> dtoService;
    private final CategoryTranslationService categoryTranslationService;

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @Override
    protected CategoryDao getDao() {
        return categoryDao;
    }

    public void save( List<Category> categories )
    {
        for( Category category : categories )
        {
            this.save( category );
        }
    }

    public List<Category> findCategories( Store store ){
        QCategory qCategory = QCategory.category;
        return (List<Category>) this.categoryDao.findAll( qCategory.store.eq( store ) );
    }

    /**
     * Get the parent category by store
     *
     * @param store
     * @return
     */
    public Category getRootCategory( Store store ){
        QCategory qCategory = QCategory.category;
        List<Category> categories = (List<Category>) this.categoryDao.findAll( qCategory.store.eq( store ).and( qCategory.parent.isNull() ) );
        if( categories.isEmpty() ){
            return null;
        }
        return categories.get(0);
    }

    public Category getCategory( Integer categoryID, Store store )
    {
        Category category;
        try {
            category = this.getDao().getOne(categoryID);
        }catch ( EntityNotFoundException ex ){
            return null;
        }
        if( category.getStore() != null && Objects.equals( category.getStore().getId(), store.getId() )){
            return category;
        }
        return null;
    }


    public CategoryDTO getByIdToDTO( Integer categoryId ){
        Category category = this.find( categoryId );
        if( category == null ) throw  new CategoryNotFoundException( categoryId );
        return this.toDto( category );

    }

    public List<CategoryDTO> findAllToDTO(){
        List<Category> categories = this.categoryDao.findAll();
        return this.toDto( categories );
    }


    public List<CategoryDTO> toDto( List<Category> category ){
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        category.forEach( categoryItem -> {
            categoryDTOS.add( this.toDto( categoryItem ) );
        });
        return categoryDTOS;
    }


    public CategoryDTO toDto( Category category ){
        CategoryDTO categoryDto  = this.dtoService.toDTO( category, CategoryDTO.class );

        // CHILD
        List<Integer> childrenID = new ArrayList<>();
        category.getChildren().forEach( categoryItem -> {
            childrenID.add( categoryItem.getId() );
        } );
        categoryDto.setChildrenId( childrenID );

        // TRANSLATION
        List<CategoryTranslationDTO> translationDTOList = new ArrayList<>(this.categoryTranslationService.toDto(category.getCategoryTranslations()));
        categoryDto.setCategoryTranslationDTOList( translationDTOList );

        return categoryDto;
    }

    public Category toEntity( CreateCategory createCategory ){
        Category category = new Category();

        category.setVisible( createCategory.getVisible() );
        category.setRank( createCategory.getRank() );

        // STORE
        Integer storeId = createCategory.getStoreId();
        if( storeId == null ) throw new StoreNotFoundException( storeId );
        Store store = this.storeService.find( storeId );
        category.setStore( store );


        // PARENT CATEGORY
        Integer parentId = createCategory.getParentId();
        if( parentId != null ) {
            Category categoryParent = this.find( parentId );
            if( categoryParent == null ) throw  new CategoryNotFoundException( parentId );
            category.setParent( categoryParent );
        }

        // CHILDREN CATEGORIES
        List<Integer> children = createCategory.getChildrenId();
        List<Category> categoryChildren = new ArrayList<>();
        if( children != null && ! children.isEmpty() ){
            children.forEach( categoryIdItem -> {
                Category categoryChild = this.find( categoryIdItem );
                if( categoryChild == null ) throw  new CategoryNotFoundException( categoryIdItem );
                categoryChildren.add( categoryChild );
            });
        }
        category.setChildren( categoryChildren );

        // TAGS
        List<Tag> tagsList = new ArrayList<>();
        List<Integer> tagsIds = createCategory.getTagsId();
        if( tagsIds != null ) {
            tagsIds.forEach( tagId -> {
                if ( tagId == null ) throw new TagNotFoundException( tagId );
                Tag tag = this.tagService.find( tagId );
                if ( tag == null ) throw new TagNotFoundException( tagId );
                tagsList.add( tag );
            });
        }
        category.setTags( tagsList );

        return category;
    }


    /**
     *
     * @param product
     * @param store
     * @return
     */
    public Map<Integer, Category > findCategoryForProduct( Product product, Store store ){

        Map<Integer, Category> categoryMap = new HashMap<>();
        QCategory category = QCategory.category;
        QCategoryProductLink categoryProductLink = QCategoryProductLink.categoryProductLink;

        List<Category> categories = (List<Category>) this.categoryDao.findAll(
                category.store.eq( store ).and(
                        categoryProductLink.category.eq( category ).and( categoryProductLink.product.eq( product ) )
                ),
                category.rank.desc() );

        categories.forEach( categoryItem -> categoryMap.put( categoryItem.getDepth(), categoryItem ) );
        return categoryMap;
    }

}
