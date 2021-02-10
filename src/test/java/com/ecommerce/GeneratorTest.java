package com.ecommerce;

import com.ecommerce.catalog.model.CustomerGroupType;
import com.ecommerce.catalog.model.DimensionType;
import com.ecommerce.catalog.sql.entity.category.Category;
import com.ecommerce.catalog.sql.entity.common.Language;
import com.ecommerce.catalog.sql.entity.common.LanguageTranslation;
import com.ecommerce.catalog.sql.entity.customer.CustomerGroup;
import com.ecommerce.catalog.sql.entity.price.*;
import com.ecommerce.catalog.sql.entity.product.Dimension;
import com.ecommerce.catalog.sql.entity.product.Product;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.entity.product.ProductTranslation;
import com.ecommerce.catalog.sql.entity.promotion.Promotion;
import com.ecommerce.catalog.sql.entity.tag.Tag;
import com.ecommerce.catalog.sql.entity.property.Property;
import com.ecommerce.catalog.sql.entity.property.PropertyTranslation;
import com.ecommerce.catalog.sql.entity.property.PropertyUnit;
import com.ecommerce.catalog.sql.entity.property.PropertyUnitTranslation;
import com.ecommerce.catalog.sql.entity.user.UserAction;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Setter
public class GeneratorTest {


    private Store storeToto;
    private Store storeTutu;
    private Product product;
    private Tag tagSold;
    private PriceTTC price1TCC;
    private PriceTTC price2TCC;
    private PriceTTC price3TCC;
    private PricePromo price4PROMO;
    private Promotion promotion1;
    //private ProductPrice productPriceToto;
    private Currency currencyEuro;
    private Language languageFrench;
    private Language languageEnglish;
    private Property propertyWidth;
    private Property propertyHeight;
    private Category categoryParent;
    private Category categoryChild1;
    private Category categoryChild2;
    private CustomerGroup customerGroupPublic;


    public static final String CODE_STORE = "www.toto.com";
    public static final String CODE_STORE_TUTU = "www.tutu.com";

    //
    //  -   STORE
    //

    public Store getStoreToto()
    {
        if( this.storeToto == null )
        {
            this.storeToto = new Store();
            this.storeToto.setCode( CODE_STORE );
            this.storeToto.setLabel( CODE_STORE );
        }
        return this.storeToto;
    }

    public Store getStoreTutu()
    {
        if( this.storeTutu == null )
        {
            this.storeTutu = new Store();
            this.storeTutu.setCode( CODE_STORE_TUTU );
            this.storeTutu.setLabel( CODE_STORE_TUTU );
        }
        return this.storeTutu;
    }

    //
    //  - LANGUAGE
    //

    public Language getLanguageFrench()
    {
        if( this.languageFrench == null )
        {
            this.languageFrench = new Language();

            LanguageTranslation languageTranslation = new LanguageTranslation();
            languageTranslation.setLabel( "Française" );
            languageTranslation.setLanguage( this.languageFrench );
            languageTranslation.setLanguageReference( this.languageFrench );

            this.languageFrench.setActive( true );
            this.languageFrench.setIsoCode( "FR" );
            this.languageFrench.setLanguageTranslationList(
                    Arrays.asList( languageTranslation ) );

        }
        return this.languageFrench;
    }

    public Language getLanguageEnglish()
    {
        if( this.languageEnglish == null )
        {
            this.languageEnglish = new Language();
            LanguageTranslation languageTranslation = new LanguageTranslation();
            languageTranslation.setLabel( "English" );
            languageTranslation.setLanguage( this.languageEnglish );
            this.languageEnglish.setActive( true );
            this.languageEnglish.setIsoCode( "EN" );
            this.languageEnglish.setLanguageTranslationList(
                    Arrays.asList( languageTranslation ) );
        }
        return this.languageEnglish;
    }

    //
    //  -   CATEGORY
    //

    public Category getCategoryParent()
    {
        if( this.categoryParent == null )
        {
            this.categoryParent = new Category();
            this.categoryParent.setRank( 1 );
            this.categoryParent.setParent( null );
            this.categoryParent.setVisible( true );
            this.categoryParent.setStore( this.getStoreToto() );
            this.categoryParent.setImageData( null );
        }
        return this.categoryParent;
    }

    public Category getCategoryChild1()
    {
        if( this.categoryChild1 == null )
        {
            this.categoryChild1 = new Category();
            this.categoryChild1.setRank( 1 );
            this.categoryChild1.setParent( this.getCategoryParent() );
            this.categoryChild1.setVisible( true );
            this.categoryChild1.setStore( this.getStoreToto() );
            this.categoryChild1.setImageData( null );
        }
        return this.categoryChild1;
    }

    public Category getCategoryChild2()
    {
        if( this.categoryChild2 == null )
        {
            this.categoryChild2 = new Category();
            this.categoryChild2.setRank( 2 );
            this.categoryChild2.setParent( this.getCategoryParent() );
            this.categoryChild2.setVisible( true );
            this.categoryChild2.setStore( this.getStoreTutu() );
            this.categoryChild2.setImageData( null );
        }
        return this.categoryChild2;
    }

    //
    //  -   PROPERTY
    //

    public Property getPropertyHeight()
    {
        if( this.propertyHeight == null )
        {
            // LABEL
            PropertyTranslation propertyTranslation = new PropertyTranslation();
            propertyTranslation.setLabel( "hauteur" );
            propertyTranslation.setLanguage( this.getLanguageFrench() );

            PropertyUnitTranslation unitTranslation = new PropertyUnitTranslation();
            unitTranslation.setLabel("metre");
            unitTranslation.setLabelLite("m");
            //unitTranslation.setLanguage( );

            // UNIT
            PropertyUnit propertyUnit = new PropertyUnit();
            propertyUnit.setId( 1 );
            propertyUnit.setPropertyUnitTranslationList( Arrays.asList( unitTranslation ) );

            // PROPERTY
            Property property = new Property();
            property.setId(1);
            property.setEnable( true );
            property.setPropertyUnit( propertyUnit );
            property.setTranslations( Arrays.asList( propertyTranslation ));

            this.propertyHeight = property;
        }
        return this.propertyHeight;
    }

    public Property getPropertyWidth()
    {
        if( this.propertyWidth == null )
        {
            // LABEL
            PropertyTranslation propertyTranslation = new PropertyTranslation();
            propertyTranslation.setLabel( "largeur" );
            propertyTranslation.setLanguage( this.getLanguageFrench() );

            // UNIT
            PropertyUnit propertyUnit = new PropertyUnit();
            propertyUnit.setId( 2 );

            // PROPERTY
            Property property = new Property();
            property.setId(1);
            property.setEnable( true );
            property.setPropertyUnit( propertyUnit );
            property.setTranslations( Arrays.asList( propertyTranslation ));

            this.propertyWidth = property;
        }
        return this.propertyWidth;
    }

    //
    //  -   CURRENCY
    //

    public Currency getCurrencyEuro()
    {
        if( this.currencyEuro == null ){
            Currency currency = new Currency();
            currency.setId( 1 );
            currency.setLabel( "euro" );
            currency.setSymbol( "€" );
            this.currencyEuro = currency;
        }
        return this.currencyEuro;
    }

    //
    //  -   TAG
    //

    public Tag getTagSold()
    {
        if( this.tagSold == null ){
            Tag tag = new Tag();
            tag.setId( 1 );
            tag.setLabel( "SOLDE");
            this.tagSold = tag;
        }
        return this.tagSold;
    }

    //
    //  -   PRICE
    //

    public PriceTTC getPrice1TTC()  {
        if( this.price1TCC == null ){

            Date date = null;
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                date = simpleDateFormat.parse("01/07/2020");
            }catch ( ParseException ex ){}

            PriceTTC price = new PriceTTC();
            price.setId( 1 );
            price.setPrice( BigDecimal.valueOf( 570 ) );
            price.setCurrency( this.getCurrencyEuro() );
            price.setCurrentPrice( false );
            //price.setPriceType( PriceType.TTC );

            UserAction userAction = new UserAction();
            userAction.setDate( date );

            price.setUserAction( userAction );
            this.price1TCC = price;
        }
        return this.price1TCC;
    }

    public PriceTTC getPrice2TTC()  {
        if (this.price2TCC == null) {

            Date date = null;
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                date = simpleDateFormat.parse( "28/08/2020");
            }catch ( ParseException ex ){}

            PriceTTC price = new PriceTTC();
            price.setId( 2 );
            price.setPrice( BigDecimal.valueOf( 530 ) );
            price.setCurrency( this.getCurrencyEuro() );
            price.setCurrentPrice( false );
            //price.setPriceType( PriceType.TTC );

            UserAction userAction = new UserAction();
            userAction.setDate( date );

            price.setUserAction( userAction );

            this.price2TCC = price;
        }
        return  this.price2TCC;
    }

    public PriceTTC getPrice3TTC() {
        if (this.price3TCC == null) {

            Date date = null;
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                date = simpleDateFormat.parse( "15/09/2020");
            }catch ( ParseException ex ){}

            PriceTTC price = new PriceTTC();
            price.setId( 3 );
            price.setPrice( BigDecimal.valueOf( 490 ) );
            price.setCurrency( this.getCurrencyEuro() );
            price.setCurrentPrice( true );
            //price.setPriceType( PriceType.TTC );

            UserAction userAction = new UserAction();
            userAction.setDate( date );

            price.setUserAction( userAction );

            this.price3TCC = price;
        }
        return  this.price3TCC;
    }

    public PricePromo getPrice4PROMO()  {
        if( this.price4PROMO == null ){

            Date date = null;
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                date = simpleDateFormat.parse("01/07/2020");
            } catch ( ParseException ex ){}

            PricePromo price = new PricePromo();
            price.setId( 4 );
            price.setPrice( BigDecimal.valueOf( 416.5 ) );
            price.setCurrency( this.getCurrencyEuro() );
            price.setCurrentPrice( true );
            //price.setPriceType( PriceType.PROMO );
            price.setPriceInitial( this.getPrice3TTC() );

            UserAction userAction = new UserAction();
            userAction.setDate( date );

            price.setUserAction( userAction );
            this.price4PROMO = price;
        }
        return this.price4PROMO;
    }

//    public ProductPrice getProductPriceToto(){
//        if( this.productPriceToto == null ){
//            ProductPrice productPrice = new ProductPrice();
//            productPrice.setId( 1 );
//            productPrice.setStore( this.getStoreToto() );
//
//            List<Price> prices = new ArrayList<>();
//            prices.add( this.getPrice1TTC() );
//            prices.add( this.getPrice2TTC() );
//            prices.add( this.getPrice3TTC() );
//            prices.add( this.getPrice4PROMO() );
//            productPrice.setPriceList( prices );
//
//            this.productPriceToto = productPrice;
//        }
//        return this.productPriceToto;
//    }

    //
    //  -   CUSTOMER GROUP
    //

    public CustomerGroup getCustomerGroupPublic()
    {
        if( this.customerGroupPublic == null ) {
            CustomerGroup customerGroup = new CustomerGroup();
            customerGroup.setId(1);
            customerGroup.setType(CustomerGroupType.PUBLIC);
            customerGroup.setLabel("PUBLIC");

            this.customerGroupPublic = customerGroup;
        }
        return this.customerGroupPublic;
    }

    //
    //  -   PRODUCT
    //

    public Product getProductTest1(){
        if( this.product == null ){
            Product product = new Product();
            product.setId( 1 );
            product.setReference( "REF-425-ZF" );

            ProductTranslation productTranslationStoreA = new ProductTranslation();
            productTranslationStoreA.setLanguage( this.getLanguageFrench() );
            productTranslationStoreA.setStore( this.getStoreToto() );
            productTranslationStoreA.setModel( "Model-25B" );
            productTranslationStoreA.setPresentation( "Super produit" );
            productTranslationStoreA.setDescription( "Une grande description ............... bla bla ............ ");

            ProductTranslation productTranslationStoreB = new ProductTranslation();
            productTranslationStoreB.setLanguage( this.getLanguageEnglish() );
            productTranslationStoreB.setStore( this.getStoreToto() );
            productTranslationStoreB.setModel( "Model-25B" );
            productTranslationStoreB.setPresentation( "Super product" );
            productTranslationStoreB.setDescription( "A big description ............... bla bla ............ ");

            product.setProductTranslations( Arrays.asList( productTranslationStoreA, productTranslationStoreB ));


            // PRICE
            Price price = new Price();
            price.setProduct( product );
            price.setStore( this.getStoreToto() );
            price.setId( 1 );
            price.setCustomerGroup( this.getCustomerGroupPublic() );
            List<Price> prices = new ArrayList<>();
            prices.add( price );
            product.setPrices( prices );

            Dimension dimension = new Dimension();
            dimension.setId( 1 );
            dimension.setDimensionType( DimensionType.NET );
            List<Property> propertyList = new ArrayList<>();
            dimension.setPropertiesDimension( propertyList );

            List<Dimension> dimensions = new ArrayList<>();
            dimensions.add( dimension );
            product.setDimensions( dimensions );

            this.product = product;
        }
        return this.product;
    }

    //
    //  -   PROMOTION
    //

    public Promotion getPromotionEnable(){
        if( this.promotion1 == null ){

            Date createdAt = null;
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                createdAt = simpleDateFormat.parse("01/07/2020");
            }catch ( ParseException ex ){}

            Date finishedAt = null;
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                finishedAt = simpleDateFormat.parse("11/07/2020");
            }catch ( ParseException ex ){}

            Promotion promotion = new Promotion();
            promotion.setId( 1 );
            promotion.setEnable( true );
            promotion.setStore( this.getStoreToto() );
            promotion.setTags( Arrays.asList( this.getTagSold() ));
            promotion.setStartAt( createdAt );
            promotion.setFinishAt( finishedAt );
            //promotion.setProductPrice( this.getProductPriceToto() );
            //promotion.setPriceWithPromotion( this.getPrice4PROMO() );

            this.promotion1 = promotion;
        }
        return this.promotion1;
    }


}
