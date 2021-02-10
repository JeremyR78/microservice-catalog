package com.ecommerce.catalog.sql.service.product;

import com.ecommerce.catalog.dto.price.CreatePriceDTO;
import com.ecommerce.catalog.dto.product.*;
import com.ecommerce.catalog.dto.property.PropertyDTO;
import com.ecommerce.catalog.dto.property.PropertyValueDTO;
import com.ecommerce.catalog.exceptions.*;
;
import com.ecommerce.catalog.model.PriceType;
import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.sql.entity.common.Language;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.entity.price.*;
import com.ecommerce.catalog.service.AbstractService;
import com.ecommerce.catalog.sql.dao.product.ProductDao;
import com.ecommerce.catalog.sql.entity.product.*;
import com.ecommerce.catalog.sql.entity.promotion.Promotion;
import com.ecommerce.catalog.sql.entity.property.Property;
import com.ecommerce.catalog.sql.entity.property.PropertyValue;
import com.ecommerce.catalog.sql.service.common.LanguageService;
import com.ecommerce.catalog.sql.service.common.StoreService;
import com.ecommerce.catalog.sql.service.customer.CustomerGroupService;
import com.ecommerce.catalog.sql.service.price.CurrencyService;
import com.ecommerce.catalog.sql.service.price.PriceService;
import com.ecommerce.catalog.sql.service.promotion.PromotionService;
import com.ecommerce.catalog.sql.service.property.PropertyService;
import com.ecommerce.catalog.sql.service.property.PropertyValueService;
import com.ecommerce.catalog.utils.jpa.PredicateUtils;
import com.ecommerce.catalog.utils.logs.LogUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class ProductService extends AbstractService<Product, Integer> {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private final ProductDao productDao;
    private final ConvertDTO<Product, ProductDTO> dtoService;
    private final ConvertDTO<Product, CreateProduct> dtoCreateService;

    private final StoreService storeService;
    private final CustomerGroupService customerGroupService;
    private final CurrencyService currencyService;
    private final LanguageService languageService;
    private final PropertyService propertyService;
    private final PropertyValueService propertyValueService;
    private final PriceService priceService;
    private final PromotionService promotionService;

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------

    @Override
    protected JpaRepository<Product, Integer> getDao() {
        return this.productDao;
    }

    @PostConstruct
    protected void init(){
        this.promotionService.setProductService( this );
    }

    /**
     *
     * @param store
     * @param language
     * @param currency
     * @return
     */
    public List<Product> findProducts( Store store, Language language, Currency currency )
    {
        List<BooleanExpression> booleanExpressionList = new ArrayList<>();
        QProductTranslation qProductTranslation = QProductTranslation.productTranslation;

        if( language != null ){
            booleanExpressionList.add( qProductTranslation.language.eq( language ) );
        }

        if( store != null ){
            booleanExpressionList.add( qProductTranslation.store.eq( store ) );
        }

        return (List<Product>) this.productDao.findAll( Objects.requireNonNull( PredicateUtils.getPredicate(booleanExpressionList) ) );
    }

    //
    //  -   CONVERT
    //

    @PostConstruct
    public void initDTO()
    {
//        this.dtoViewService.getModelMapper().typeMap( Product.class, ProductView.class )
//                .addMapping( mapper
    }


    public List<ProductDTO> toDTO(List<Product> products ){
        return this.dtoService.toDTO( products, ProductDTO.class );
    }

    public ProductDTO toDTO( Product product ) {
        ProductDTO productDTO = this.dtoService.toDTO( product, ProductDTO.class );

        // PROPERTIES
        List<ProductPropertyDTO> productPropertyDTOList = new ArrayList<>();
        product.getPropertyProductList().forEach( productProperty -> {
            ProductPropertyDTO productPropertyDTO = new ProductPropertyDTO();

            Property property = productProperty.getProperty();
            PropertyDTO propertyDto = this.propertyService.toDTO( property );
            productPropertyDTO.setProperty( propertyDto );

            List<PropertyValue> propertyValues = productProperty.getPropertyValues();
            List<PropertyValueDTO> propertyValueDTOList = new ArrayList<>();
            for( PropertyValue propertyValue : propertyValues ) {
                propertyValueDTOList.add( this.propertyValueService.toDTO(propertyValue) );
            }
            productPropertyDTO.setPropertyValues( propertyValueDTOList );

            productPropertyDTOList.add( productPropertyDTO );
        });
        productDTO.setPropertyProductList( productPropertyDTOList );

        return productDTO;
    }

    public List<Product> toEntity( List<ProductDTO> productDTOList ) {
        return this.dtoService.toEntity( productDTOList, Product.class );
    }

    public Product toEntity( ProductDTO productDTO ) {
        return this.dtoService.toEntity( productDTO, Product.class );
    }

    public List<Product> createToEntity( List<CreateProduct> productDTOList ) {
        return this.dtoCreateService.toEntity( productDTOList, Product.class );
    }

    /**
     *
     * @param newProduct
     * @return
     */
    public Product toEntity( CreateProduct newProduct ) {
        Product product = new Product();
        product.setReference( newProduct.getReference() );

        //
        // PRICES
        //
        List<CreatePriceDTO> newPricesList = newProduct.getPrices();
        List<Price> prices = new ArrayList<>();
        for( CreatePriceDTO newPrice : newPricesList ) {

            Promotion promotion = null;
            // FIND PROMO
            if( Objects.equals( newPrice.getPriceType(), PriceType.PROMO ) ) {
                Integer promotionId = newPrice.getPromotionId();
                if( promotionId == null )throw new PromotionNotFoundException( promotionId );
                promotion = this.promotionService.find( promotionId );
                if( promotion == null )throw new PromotionNotFoundException( promotionId );
            }

            // CREATE PRICE
            Price price = this.priceService.toEntity( newPrice, product, promotion );
            // ADD
            prices.add( price );
        }
        product.setPrices( prices );

        //
        // PROPERTIES
        //
        List<CreateProductProperty> propertiesDto = newProduct.getPropertyProductList();
        List<ProductProperty> properties = new ArrayList<>();
        for( CreateProductProperty propertyDto : propertiesDto ){
            Integer propertyId = propertyDto.getPropertyId();
            List<Integer> propertyValueIdList = propertyDto.getPropertyValueIdList();

            Property property = this.propertyService.findWithOptional( propertyId ).orElseThrow( () -> new PropertyNotFoundException( propertyId ));
            List<PropertyValue> propertyValueList = property.getPropertyValues();

            List<PropertyValue> propertyValues = new ArrayList<>();

            for( Integer propertyValueId : propertyValueIdList ) {
                if (propertyValueList == null || propertyValueList.isEmpty()) {
                    throw new PropertyValueNotFoundException(propertyValueId);
                }
                PropertyValue propertyValue = null;
                for (PropertyValue propertyValueItem : propertyValueList) {
                    if (Objects.equals(propertyValueId, propertyValueItem.getId())) {
                        propertyValue = propertyValueItem;
                    }
                }
                if (propertyValue == null) throw new PropertyValueNotFoundException(propertyValueId);
                propertyValues.add( propertyValue );
            }
            ProductProperty productProperty = new ProductProperty();
            productProperty.setProduct(product);
            productProperty.setProperty(property);
            productProperty.setPropertyValues( propertyValues );
            properties.add( productProperty );
        }
        product.setPropertyProductList( properties );

        return product;
    }



    // --------------------------------------
    // -            Methods - TOOLS         -
    // --------------------------------------



}
