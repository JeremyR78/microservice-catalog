package com.ecommerce.catalog.nosql.mapper;


import com.ecommerce.catalog.nosql.data.CategoryEs;
import com.ecommerce.catalog.nosql.data.ProductEs;
import com.ecommerce.catalog.nosql.data.PropertyEs;
import com.ecommerce.catalog.nosql.service.IndexService;
import com.ecommerce.catalog.sql.entity.category.Category;
import com.ecommerce.catalog.sql.entity.category.CategoryTranslation;
import com.ecommerce.catalog.sql.entity.common.Language;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.entity.product.Product;
import com.ecommerce.catalog.sql.entity.product.ProductProperty;
import com.ecommerce.catalog.sql.entity.product.ProductTranslation;
import com.ecommerce.catalog.sql.entity.property.Property;
import com.ecommerce.catalog.sql.entity.property.PropertyTranslation;
import com.ecommerce.catalog.sql.entity.property.PropertyUnit;
import com.ecommerce.catalog.sql.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class MapperProductService {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private final CategoryService categoryService;
    private final IndexService indexService;

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    /**
     *
     * @param product
     * @return
     */
    public Map<String, ProductEs> mappingProduct( Product product, String table ){

        Map<String, ProductEs> mapProduct = new HashMap<>();

        if( product == null || table == null ){
            return mapProduct;
        }

        List<ProductTranslation> productTranslationList = product.getProductTranslations();
        if( productTranslationList != null ) {
            for (ProductTranslation productTranslation : productTranslationList) {

                Store store = productTranslation.getStore();
                Language language = productTranslation.getLanguage();

                ProductEs productEs = new ProductEs();
                productEs.setId(product.getId());
                productEs.setReference(product.getReference());
                productEs.setModel(productTranslation.getModel());
                productEs.setDescription(productTranslation.getDescription());
                productEs.setPresentation(productTranslation.getPresentation());

                // Categories
                Map<Integer, Category> mapCategories = this.categoryService.findCategoryForProduct(product, store);
                List<CategoryEs> categoriesEs = new ArrayList<>();
                for (Map.Entry<Integer, Category> entry : mapCategories.entrySet()) {
                    Category category = entry.getValue();
                    CategoryEs categoryEs = this.mappingCategory(category, language);
                    categoriesEs.add(categoryEs);
                }
                productEs.setCategoryEs(categoriesEs);


                // Properties
                List<ProductProperty> productProperties = product.getPropertyProductList();
                List<Property> properties = new ArrayList<>();
                if (productProperties != null) {
                    productProperties.forEach(productProperty -> {
                        properties.add(productProperty.getProperty());
                    });
                }

                List<PropertyEs> propertyEsList = new ArrayList<>();
                properties.forEach(property -> propertyEsList.add(this.mappingProperty(property, language)));
                productEs.setProperties(propertyEsList);

                Optional<String> indexOptional = this.indexService.findIndexFor(table, store, language);
                if (indexOptional.isEmpty()) continue;
                mapProduct.put(indexOptional.get(), productEs);
            }
        }
        return mapProduct;
    }

    public CategoryEs mappingCategory( Category category, Language language ){
        CategoryEs categoryEs = new CategoryEs();
        categoryEs.setId( category.getId() );

        // LABEL
        List<CategoryTranslation> categoryTranslationList = category.getCategoryTranslations();
        categoryTranslationList.forEach( categoryTranslation -> {
            if( Objects.equals( categoryTranslation.getLanguage(), language ) ){
                categoryEs.setLabel( categoryTranslation.getLabel() );
            }
        });

        categoryEs.setRank( category.getRank() );
        categoryEs.setVisible( category.getVisible() );

        // PARENT CATEGORY
        Integer parentId = category.getParent() != null ? category.getParent().getId() : null;
        categoryEs.setParentId( parentId );

        // CHILDREN CATEGORIES
        List<Category> categories = category.getChildren();
        List<Integer> childrenId = new ArrayList<>();
        if( categories != null && ! categories.isEmpty() ){
            categories.forEach( categoryChildItem -> childrenId.add( categoryChildItem.getId() ));
        }
        categoryEs.setChildrenId( childrenId );
        return categoryEs;
    }


    public PropertyEs mappingProperty( Property property, Language language ){
        PropertyEs propertyEs = new PropertyEs();

        propertyEs.setId( property.getId() );

        // LABEL
        List<PropertyTranslation> propertyTranslations = property.getTranslations();
        if( propertyTranslations != null ) {
            propertyTranslations.stream()
                    .filter(propertyTranslationItemFilter -> Objects.equals(propertyTranslationItemFilter.getLanguage(), language))
                    .findFirst()
                    .ifPresent(translation -> propertyEs.setLabel(translation.getLabel()));
        }

        // PROPERTY UNIT
        PropertyUnit propertyUnit = property.getPropertyUnit();
        if( propertyUnit != null ){

            propertyEs.setPropertyUnitId( propertyUnit.getId() );
            propertyEs.setPropertyType( propertyUnit.getPropertyType() );

            // LABEL
            propertyUnit.getPropertyUnitTranslationList().stream()
                    .filter(propertyUnitTranslationItem -> Objects.equals( propertyUnitTranslationItem.getLanguage(), language) )
                    .findFirst()
                    .ifPresent( unitTranslation -> propertyEs.setPropertyUnitLabel( unitTranslation.getLabel() ) );

        }

        return propertyEs;
    }
}
