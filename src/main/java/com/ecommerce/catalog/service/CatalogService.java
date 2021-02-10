package com.ecommerce.catalog.service;

import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.dto.price.PriceView;
import com.ecommerce.catalog.dto.product.ProductPropertyView;
import com.ecommerce.catalog.dto.product.ProductView;
import com.ecommerce.catalog.dto.property.PropertyView;
import com.ecommerce.catalog.model.CustomerGroupType;
import com.ecommerce.catalog.nosql.data.PriceEs;
import com.ecommerce.catalog.nosql.data.ProductEs;
import com.ecommerce.catalog.sql.entity.common.Language;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.entity.customer.CustomerGroup;
import com.ecommerce.catalog.sql.entity.price.Currency;
import com.ecommerce.catalog.sql.entity.price.Price;
import com.ecommerce.catalog.sql.entity.product.ProductProperty;
import com.ecommerce.catalog.sql.entity.product.ProductTranslation;
import com.ecommerce.catalog.sql.entity.property.*;
import com.ecommerce.catalog.model.PriceType;
import com.ecommerce.catalog.sql.service.category.CategoryService;
import com.ecommerce.catalog.sql.service.common.StoreService;
import com.ecommerce.catalog.sql.service.price.CurrencyService;
import com.ecommerce.catalog.sql.entity.product.Product;
import com.ecommerce.catalog.sql.service.common.LanguageService;
import com.ecommerce.catalog.sql.service.product.ProductService;
import com.ecommerce.catalog.sql.service.property.PropertyService;
import com.ecommerce.catalog.exceptions.CurrencyNotFoundException;
import com.ecommerce.catalog.exceptions.LanguageNotFoundException;
import com.ecommerce.catalog.exceptions.StoreNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.ecommerce.catalog.utils.logs.LogNameSpace.*;

@Service
@RequiredArgsConstructor
public class CatalogService {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private transient final Logger logger = LoggerFactory.getLogger(getClass());

    private final ProductService productService;
    private final StoreService storeService;
    private final LanguageService languageService;
    private final PropertyService propertyService;
    private final CategoryService categoryService;
    private final CurrencyService currencyService;

    private final ConvertDTO<Product, ProductView> dtoViewService;

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------

    /**
     *
     * @param storeId
     * @param languageId
     * @param currencyId
     * @return
     */
    public List<Product> findProducts( Integer storeId, Integer languageId, Integer currencyId )
    {
        Store store = null;
        Language language = null;
        Currency currency = null;

        if( storeId != null ) {
            store = this.storeService.find( storeId );
            if( store == null ){
                throw new StoreNotFoundException( storeId );
            }
        }

        if( languageId != null ){
            language = this.languageService.find( languageId );
            if( language == null ){
                throw new LanguageNotFoundException( languageId );
            }
        }

        if( currencyId != null ){
            currency = this.currencyService.find( currencyId );
            if( currency == null ){
                throw new CurrencyNotFoundException( currencyId );
            }
        }

        return this.productService.findProducts( store, language, currency );
    }


    /**
     *
     * @param products
     * @param language
     * @param store
     * @param currency
     * @return
     */
    public List<ProductView> toViewDTO(List<Product> products , Language language, Store store, Currency currency  ){

        List<ProductView> productViewList = new ArrayList<>();
        for( Product product : products ){
            productViewList.add( this.toViewDTO( product, language, store, currency ) );
        }

        return productViewList;
    }

    /**
     *
     * @param product
     * @param language
     * @param store
     * @param currency
     * @return
     */
    public ProductView toViewDTO( Product product, Language language, Store store, Currency currency ) {

        this.getLogger().info("conversion du produit ");
        Date currentDate = new Date();
        ProductView productView = this.dtoViewService.toDTO( product, ProductView.class );

        // PRODUCT TRANSLATION
        List<ProductTranslation> translations = product.getProductTranslations();
        for( ProductTranslation translation : translations ) {
            if( Objects.equals( translation.getLanguage(), language)
                    && Objects.equals(  translation.getStore(), store ) )
            {
                productView.setPresentation( translation.getPresentation() );
                productView.setDescription( translation.getDescription() );
                productView.setModel( translation.getModel() );
            }
        }

        // TAG TRANSLATION
        List<Property> properties = product.getTagList();
        List<PropertyView> propertyViews = this.convertProperties( properties, language );
        productView.setTagList( propertyViews );

        // PROPERTY TRANSLATION
        List<ProductProperty> productProperties = product.getPropertyProductList();
        List<ProductPropertyView> productPropertyViews = this.convertProductProperties( productProperties, language );
        productView.setProductPropertyViews( productPropertyViews );

        // PRICE
        List<Price> priceList = product.getPrices();
        productView.setSymbolPrice( currency.getSymbol() );

        for( Price price : priceList )
        {
            if( ! Objects.equals( price.getStore(), store ))
            {
                continue;
            }

            for( PriceType type : PriceType.values() ) {

                CustomerGroupType customerGroupType = price.getCustomerGroup() != null ? price.getCustomerGroup().getType() : null;

                switch ( customerGroupType ) {
                    case PUBLIC:
                    case PREMIUM_1:
                    case PREMIUM_2:
                    case PREMIUM_3:
                        switch (type) {
                            case TTC:
                                productView.setPricePublicTTC( price != null ? price.getPrice() : null );
                                break;

                            case HT:
                                productView.setPricePublicHT( price != null ? price.getPrice() : null );
                                break;

                            case PROMO:
                                // MEILLEUR PROMO calculé après
                                break;
                        }
                        break;
                    default:
                        this.getLogger().error("{}{} Impossible de trouver le CUSTOMER GROUP : {}", fLog(CATALOG),
                                fLog(TO_DTO), customerGroupType
                                );
                        break;
                }
            }

            // BEST PROMOTION
//            Promotion promo = price.getBestPromotion( currentDate );
//            productView.setPricePublicWithPromo( promo.getPriceWithPromotion().getPrice() );
//            productView.setStartPromoAt( promo.getStartAt() );
//            productView.setFinishPromoAt( promo.getFinishAt() );
//            productView.setPricePublicPromotionPercent( promo.getPercentage() );
        }


        return productView;
    }

    public ProductView toViewDTO( ProductEs productEs, Currency currency, CustomerGroup customerGroup ){
        ProductView productView = new ProductView();

        productView.setId( productEs.getId() );
        productView.setModel( productEs.getModel() );
        productView.setReference( productEs.getReference() );
        productView.setPresentation( productEs.getPresentation() );
        productView.setDescription( productEs.getDescription() );

        // PRICE
        productEs.getPrices().stream()
                .filter( priceEs -> Objects.equals( currency.getId(), priceEs.getCurrencyId() ) )
                .filter( priceEs -> Objects.equals( customerGroup.getId(), priceEs.getCustomerGroupId() ))
                .filter( PriceEs::getCurrentPrice )
                .forEach( priceEs -> {
                    productView.setSymbolPrice( priceEs.getCurrencySymbol() );
                    productView.setPricePublicHT( priceEs.getPrice() );
        } );



        return productView;
    }

    /**
     *
     * @param productList
     * @param currency
     * @return
     */
    private List<PriceView> convertPrice( List<Product> productList, Currency currency )
    {
        List<PriceView> priceViewList = new ArrayList<>();
        for( Product product : productList )
        {
            priceViewList.add( this.convertPrice( product, currency ));
        }
        return priceViewList;
    }

    /**
     *
     * @param product
     * @param currency
     * @return
     */
    private PriceView convertPrice( Product product , Currency currency )
    {
        PriceView priceView = new PriceView();

        List<Price> prices = product.getPrices();
        for( Price price : prices )
        {
            if( Objects.equals( price.getCurrency(), currency ))
            {
                priceView.setPrice( price.getPrice() );
                priceView.setSymbol( currency.getSymbol() );
            }
        }

        return priceView;
    }

    /**
     *
     * @param productProperties
     * @param language
     */
    private List<ProductPropertyView> convertProductProperties( List<ProductProperty> productProperties, Language language )
    {
        List<ProductPropertyView> productPropertyViews = new ArrayList<>();
        for( ProductProperty productProperty : productProperties ) {
            productPropertyViews.add( this.convertProductProperty( productProperty, language ));
        }
        return productPropertyViews;
    }

    /**
     *
     * @param productProperty
     * @param language
     * @return
     */
    private ProductPropertyView convertProductProperty( ProductProperty productProperty, Language language )
    {
        ProductPropertyView productPropertyView = new ProductPropertyView();

        // PROPERTY
        Property property = productProperty.getProperty();
        PropertyView propertyView = this.convertProperty( property, language );
        productPropertyView.setPropertyView( propertyView );

        // VALUE
//        PropertyValue propertyValue = productProperty.getValue();
//        if( propertyValue != null ){
//            List<PropertyValueTranslation> propertyValueTranslations = propertyValue.getPropertyValueTranslationList();
//            for( PropertyValueTranslation translation : propertyValueTranslations )
//            {
//                if( Objects.equals( translation.getLanguage(), language ) )
//                {
//                    productPropertyView.setValue( translation.getLabel() );
//                }
//            }
//        }

        // RANK

        return productPropertyView;
    }

    /**
     * Convert List of Property to PropertyView
     *
     * @param properties
     * @param language
     * @return
     */
    private List<PropertyView> convertProperties( List<Property> properties ,Language language )
    {
        List<PropertyView> propertyViews = new ArrayList<>();
        for( Property property : properties ) {
            propertyViews.add( this.convertProperty( property, language ) );
        }
        return propertyViews;
    }

    /**
     * convert Property to PropertyView
     *
     * @param property
     * @param language
     * @return
     */
    private PropertyView convertProperty( Property property ,Language language )
    {
        PropertyView propertyView = new PropertyView();

        // TRANSLATION
        List<PropertyTranslation> propertyTranslationList = property.getTranslations();
        for( PropertyTranslation propertyTranslation : propertyTranslationList )
        {
            if( Objects.equals( propertyTranslation.getLanguage(), language ) )
            {
                propertyView.setLabel( propertyTranslation.getLabel() );
            }
        }

        // UNIT TRANSLATION
        PropertyUnit propertyUnit = property.getPropertyUnit();
        if( propertyUnit != null  ) {
            List<PropertyUnitTranslation> unitTranslations = propertyUnit.getPropertyUnitTranslationList();
            for( PropertyUnitTranslation unitTranslation : unitTranslations )
            {
                if( Objects.equals( unitTranslation.getLanguage(), language ) )
                {
                    propertyView.setUnitLabel( unitTranslation.getLabel() );
                    propertyView.setUnitLabelLite( unitTranslation.getLabelLite() );
                }
            }
        }
        return propertyView;
    }


    protected Logger getLogger()
    {
        return this.logger;
    }
}
