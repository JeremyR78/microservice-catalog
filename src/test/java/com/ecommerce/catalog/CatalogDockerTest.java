package com.ecommerce.catalog;

import com.ecommerce.GeneratorTest;
import com.ecommerce.catalog.dto.category.CategoryDTO;
import com.ecommerce.catalog.dto.category.CreateCategory;
import com.ecommerce.catalog.dto.category.CreateCategoryTranslation;
import com.ecommerce.catalog.dto.common.*;
import com.ecommerce.catalog.dto.customer.CustomerGroupDTO;
import com.ecommerce.catalog.dto.price.CreatePriceDTO;
import com.ecommerce.catalog.dto.price.CurrencyDTO;
import com.ecommerce.catalog.dto.price.PriceDTO;
import com.ecommerce.catalog.dto.product.*;
import com.ecommerce.catalog.dto.promotion.CreateOrUpdatePromotion;
import com.ecommerce.catalog.dto.tag.CreateTag;
import com.ecommerce.catalog.dto.promotion.PromotionDTO;
import com.ecommerce.catalog.dto.tag.CreateTagLink;
import com.ecommerce.catalog.dto.tag.TagDTO;
import com.ecommerce.catalog.dto.property.*;
import com.ecommerce.catalog.dto.tag.TagLinkDTO;
import com.ecommerce.catalog.dto.tax.CreateOrUpdateTax;
import com.ecommerce.catalog.dto.tax.TaxDTO;
import com.ecommerce.catalog.model.PriceType;
import com.ecommerce.catalog.model.PropertyType;
import com.ecommerce.catalog.model.TaxType;
import com.ecommerce.catalog.nosql.service.IndexService;
import com.ecommerce.catalog.sql.entity.common.Language;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.entity.customer.CustomerGroup;
import com.ecommerce.catalog.sql.entity.price.Currency;
import com.ecommerce.catalog.sql.entity.product.Product;
import com.ecommerce.catalog.sql.service.common.LanguageService;
import com.ecommerce.catalog.sql.service.common.StoreService;
import com.ecommerce.catalog.sql.service.customer.CustomerGroupService;
import com.ecommerce.catalog.sql.service.price.CurrencyService;
import com.ecommerce.catalog.sql.service.product.ProductService;
import com.ecommerce.catalog.sql.service.promotion.PromotionService;
import com.ecommerce.catalog.sql.service.property.PropertyService;
import com.ecommerce.catalog.sql.service.property.PropertyUnitService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.json.Json;
import javax.json.JsonPatch;

import org.elasticsearch.client.Node;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.ecommerce.catalog.controller.CatalogController.URL_PRODUCTS_VIEW;
import static com.ecommerce.catalog.controller.CatalogController.URL_PRODUCT_VIEW;
import static com.ecommerce.catalog.controller.CategoryController.URL_CATEGORIES;
import static com.ecommerce.catalog.controller.CategoryController.URL_CATEGORY;
import static com.ecommerce.catalog.controller.CountryController.URL_COUNTRIES;
import static com.ecommerce.catalog.controller.CountryController.URL_COUNTRY;
import static com.ecommerce.catalog.controller.CurrencyController.URL_CURRENCIES;
import static com.ecommerce.catalog.controller.CurrencyController.URL_CURRENCY;
import static com.ecommerce.catalog.controller.CustomerGroupController.URL_CUSTOMER_GROUP;
import static com.ecommerce.catalog.controller.CustomerGroupController.URL_CUSTOMER_GROUPS;
import static com.ecommerce.catalog.controller.LanguageController.URL_LANGUAGE;
import static com.ecommerce.catalog.controller.LanguageController.URL_LANGUAGES;
import static com.ecommerce.catalog.controller.PriceController.URL_PRICE;
import static com.ecommerce.catalog.controller.PriceController.URL_PRICES;
import static com.ecommerce.catalog.controller.ProductController.URL_PRODUCT;
import static com.ecommerce.catalog.controller.ProductController.URL_PRODUCTS;
import static com.ecommerce.catalog.controller.PromotionController.URL_PROMOTION;
import static com.ecommerce.catalog.controller.PromotionController.URL_PROMOTIONS;
import static com.ecommerce.catalog.controller.PropertyController.URL_PROPERTIES;
import static com.ecommerce.catalog.controller.PropertyController.URL_PROPERTY;
import static com.ecommerce.catalog.controller.PropertyUnitController.URL_PROPERTIES_UNITS;
import static com.ecommerce.catalog.controller.PropertyUnitController.URL_PROPERTY_UNIT;
import static com.ecommerce.catalog.controller.StoreController.URL_STORE;
import static com.ecommerce.catalog.controller.StoreController.URL_STORES;
import static com.ecommerce.catalog.controller.TagController.*;
import static com.ecommerce.catalog.controller.TaxController.URL_TAX;
import static com.ecommerce.catalog.controller.TaxController.URL_TAXES;
import static com.ecommerce.catalog.model.MediaTypeCustom.APPLICATION_JSON_PATCH_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations="classpath:application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles(profiles = "elasticsearch" )
@DisplayName("Integration from REST API")
public class CatalogDockerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private CustomerGroupService customerGroupService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private PropertyUnitService propertyUnitService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestHighLevelClient clientEs;

    @Container
    private static final MySQLContainer<?> SQL_CONTAINER = new MySQLContainer<>("mysql:8.0.20")
                .withDatabaseName("TEST-catalog-db")
                .withUsername("test")
                .withPassword("test")
            .withNetwork( Network.SHARED );

    @Container
    private static final ElasticsearchContainer ELASTICSEARCH_CONTAINER = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.10.0")
            .withNetwork( Network.SHARED );

    @DynamicPropertySource
    static void databaseProperties( DynamicPropertyRegistry registry ) {
        // SQL
        registry.add("spring.datasource.url", SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", SQL_CONTAINER::getPassword);
        // ELASTICSEARCH
        registry.add("elasticsearch.cluster-nodes", ELASTICSEARCH_CONTAINER::getHttpHostAddress );
    }

    private final static GeneratorTest GENERATOR        = new GeneratorTest();

    private final static String PATH_POST_NEW_LANGUAGE      = URL_LANGUAGES;
    private final static String PATH_GET_LANGUAGE           = URL_LANGUAGE.replaceAll("\\{.+}", "%s");

    private final static String PATH_POST_NEW_COUNTRY      = URL_COUNTRIES;
    private final static String PATH_GET_COUNTRY           = URL_COUNTRY.replaceAll("\\{.+}", "%s");

    private final static String PATH_POST_NEW_TAX           = URL_TAXES;
    private final static String PATH_GET_TAX                = URL_TAX.replaceAll("\\{.+}", "%s");

    private final static String PATH_POST_NEW_STORE         = URL_STORES;
    private final static String PATH_GET_STORE              = URL_STORE.replaceAll("\\{.+}", "%s");

    private final static String PATH_POST_NEW_CURRENCY      = URL_CURRENCIES;
    private final static String PATH_GET_CURRENCY           = URL_CURRENCY.replaceAll("\\{.+}", "%s");

    private final static String PATH_POST_NEW_PROMOTION     = URL_PROMOTIONS;
    private final static String PATH_GET_PROMOTION          = URL_PROMOTION.replaceAll("\\{.+}", "%s");

    private final static String PATH_POST_NEW_CUSTOMER_GROUP     = URL_CUSTOMER_GROUPS;
    private final static String PATH_GET_CUSTOMER_GROUP          = URL_CUSTOMER_GROUP.replaceAll("\\{.+}", "%s");

    private final static String PATH_POST_NEW_PROPERTIES     = URL_PROPERTIES;
    private final static String PATH_GET_PROPERTIES          = URL_PROPERTY.replaceAll("\\{.+}", "%s");

    private final static String PATH_POST_NEW_PROPERTIES_UNITS     = URL_PROPERTIES_UNITS;
    private final static String PATH_GET_PROPERTIES_UNITS          = URL_PROPERTY_UNIT.replaceAll("\\{.+}", "%s");

    private final static String PATH_POST_NEW_PRODUCT       = URL_PRODUCTS;
    private final static String PATH_GET_PRODUCT            = URL_PRODUCT.replaceAll("\\{.+}", "%s");

    private final static String PATH_POST_NEW_PRICE         = URL_PRICES;
    private final static String PATH_GET_PRICE              = URL_PRICE.replaceAll("\\{.+}", "%s");

    private final static String PATH_POST_NEW_CATEGORIES    = URL_CATEGORIES;
    private final static String PATH_GET_CATEGORY           = URL_CATEGORY.replaceAll("\\{.+}", "%s");

    private final static String PATH_POST_NEW_TAGS      = URL_TAGS;
    private final static String PATH_GET_TAG            = URL_TAG.replaceAll("\\{.+}", "%s");

    private final static String PATH_POST_NEW_TAGS_LINK      = URL_TAGS_LINK;
    private final static String PATH_GET_TAG_LINK            = URL_TAG_LINK.replaceAll("\\{.+}", "%s");

    private final static String PATH_PRODUCTS_VIEW          = URL_PRODUCTS_VIEW;
    private final static String PATH_PRODUCT_VIEW           = URL_PRODUCT_VIEW.replaceAll("\\{.+}", "%s");;

    //
    // Variables shared for tests
    //
    private static Integer LANGUAGE_ID;
    private static Integer COUNTRY_ID;
    private static Integer CURRENCY_ID;

    // STORE
    private static Integer STORE_TOTO_ID;
    private static Integer STORE_E_COMMERCE_ID;

    private static Integer CUSTOMER_GROUP_ID;
    private static Integer PROPERTY_UNIT_ID;
    private static Integer PROPERTY_ID;
    private static Integer PROPERTY_VALUE_1_ID;
    private static Integer PRODUCT_ID;

    private static Integer PROMOTION_ID;

    private static Integer TAX_ID;

    private static Integer PRICE_HT_ID;
    private static Integer PRICE_TTC_ID;
    private static Integer PRICE_PROMO_ID;

    private static Integer PRODUCT_PROPERTY_ID;
    private static Integer CATEGORY_ROOT_ID;

    private static Integer TAG_PROMO_ID;
    private static Integer TAG_PROMO_FLASH_ID;
    private static Integer TAG_PROMO_BLACK_FRIDAY_ID;
    private static Integer TAG_NEW_ID;
    private static Integer TAG_MUSIC_ID;
    private static Integer TAG_STORE_TOTO_ID;

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------

    @DisplayName("First test for check the docker and the context")
    @Test
    @Order(1)
    public void dockerIsStart() {
        Assert.assertNotNull( "Le service n'est pas disponible !", this.languageService );
        Assert.assertNotNull( "Le service n'est pas disponible !", this.storeService );
        Assert.assertNotNull( "Le service n'est pas disponible !", this.propertyService );
        Assert.assertNotNull( "Le service n'est pas disponible !", this.currencyService );
        Assert.assertNotNull( "Le service n'est pas disponible !", this.promotionService );
        Assert.assertNotNull( "Le service n'est pas disponible !", this.customerGroupService );
    }

    @DisplayName("Data base is starting ")
    @Test
    @Order(2)
    public void dataBaseIsStart() {
        Assert.assertTrue( SQL_CONTAINER.isRunning() );
    }

    @DisplayName("Elasticsearch is starting")
    @Test
    @Order(3)
    public void elasticsearchIsStart() throws IOException {
        Assert.assertTrue( ELASTICSEARCH_CONTAINER.isRunning() );
        String host = ELASTICSEARCH_CONTAINER.getHttpHostAddress();
        List<Node> nodes = this.clientEs.getLowLevelClient().getNodes();
        nodes.forEach( node -> {
            Assertions.assertEquals( host , node.getHost().toHostString() );
        });
        boolean response = this.clientEs.ping( RequestOptions.DEFAULT );
        Assertions.assertTrue( response );
    }

    @DisplayName("Create a new language from the REST API")
    @Test
    @Order(10)
    public void create_language_by_api_rest() throws Exception {

        Language language = GENERATOR.getLanguageFrench();
        LanguageDTO languageDTO = this.languageService.toDTO( language );
        String LanguageJson = this.objectMapper.writeValueAsString( languageDTO );
        String resultJson;

        // PUSH NEW Language
        RequestBuilder requestPost = MockMvcRequestBuilders
                .post(PATH_POST_NEW_LANGUAGE)
                .accept( MediaType.APPLICATION_JSON )
                .content( LanguageJson )
                .contentType( MediaType.APPLICATION_JSON );

        MvcResult result = mockMvc.perform( requestPost )
                .andExpect( status().isCreated() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                .andReturn();

        // The JSON response
        resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson );
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        LanguageDTO languageResult = this.objectMapper.readValue( resultJson, LanguageDTO.class );

        Assert.assertNotNull( languageResult );
        Assert.assertNotNull( languageResult.getId() );

        // ID Language
        Integer idLanguageCreated = languageResult.getId();

        String newPathLanguageId = String.format( PATH_GET_LANGUAGE, idLanguageCreated );
        // FIND Language
        RequestBuilder requestGet = MockMvcRequestBuilders
                .get( newPathLanguageId )
                .accept( MediaType.APPLICATION_JSON );

        MvcResult resultGet = mockMvc.perform( requestGet )
                .andExpect( status().isFound() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                .andReturn();

        // The JSON response
        String resultGetJson = resultGet.getResponse().getContentAsString();

        Assert.assertNotNull( resultGetJson );
        Assert.assertFalse( resultGetJson.isEmpty() );

        // The OBJECT response
        LanguageDTO languageGetResult = this.objectMapper.readValue( resultGetJson, LanguageDTO.class );

        Assert.assertNotNull( languageGetResult );

        // TEST INSERT
        Assert.assertEquals( idLanguageCreated , languageGetResult.getId() );
        Assert.assertEquals( language.getActive() , languageGetResult.getActive() );
        Assert.assertEquals( language.getIsoCode() , languageGetResult.getIsoCode() );

        List<LanguageTranslationDTO> translations = languageGetResult.getTranslations();

        Assert.assertNotNull( "No translation found for this language !", translations );
        Assert.assertFalse("No translation found for this language !", translations.isEmpty() );

        for( LanguageTranslationDTO ltd : translations ) {
            Assert.assertEquals( language.getLanguageTranslationList().get(0).getLabel(), ltd.getLabel());
            Assert.assertEquals( languageGetResult.getId(), ltd.getLanguageReferenceID() );
        }

        LANGUAGE_ID = idLanguageCreated;
    }

    @DisplayName("Create a new country from the REST API")
    @Test
    @Order(15)
    public void create_country_by_api_rest() throws Exception {

        CreateCountry createCountry = new CreateCountry();
        createCountry.setEnable( true );

        CreateCountryTranslation createCountryTranslation = new CreateCountryTranslation();
        createCountryTranslation.setLabel( "France" );
        createCountryTranslation.setLanguageId( LANGUAGE_ID );

        createCountry.setCountryTranslations( Arrays.asList( createCountryTranslation ) );

        String createCountryJson = this.objectMapper.writeValueAsString( createCountry );
        String resultJson;

        // POST NEW
        RequestBuilder requestPost = MockMvcRequestBuilders
                .post(PATH_POST_NEW_COUNTRY)
                .accept( MediaType.APPLICATION_JSON )
                .content( createCountryJson )
                .contentType( MediaType.APPLICATION_JSON );

        MvcResult result = mockMvc.perform( requestPost )
                .andExpect( status().isCreated() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                .andReturn();

        // The JSON response
        resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson );
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        CountryDTO createResult = this.objectMapper.readValue( resultJson, CountryDTO.class );

        Assert.assertNotNull( createResult );
        Assert.assertNotNull( createResult.getId() );

        // ID Language
        Integer idCountryCreated = createResult.getId();

        String newPathCountryId = String.format( PATH_GET_COUNTRY, idCountryCreated );
        // FIND Language
        RequestBuilder requestGet = MockMvcRequestBuilders
                .get( newPathCountryId )
                .accept( MediaType.APPLICATION_JSON );

        MvcResult resultGet = mockMvc.perform( requestGet )
                .andExpect( status().isFound() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                .andReturn();

        // The JSON response
        String resultGetJson = resultGet.getResponse().getContentAsString();

        Assert.assertNotNull( resultGetJson );
        Assert.assertFalse( resultGetJson.isEmpty() );

        // The OBJECT response
        CountryDTO countryGetResult = this.objectMapper.readValue( resultGetJson, CountryDTO.class );

        Assert.assertNotNull( countryGetResult );

        // TEST INSERT
        Assert.assertEquals( idCountryCreated , countryGetResult.getId() );
        Assert.assertEquals( createCountry.getEnable(), countryGetResult.getEnable() );

        List<CountryTranslationDTO> translations = countryGetResult.getCountryTranslationList();

        Assert.assertNotNull( "No translation found for this language !", translations );
        Assert.assertFalse("No translation found for this language !", translations.isEmpty() );

        for( CountryTranslationDTO ltd : translations ) {
            Assert.assertEquals( createCountry.getCountryTranslations().get(0).getLabel(), ltd.getLabel());
            Assert.assertEquals( countryGetResult.getId(), ltd.getCountryId() );
        }

        COUNTRY_ID = idCountryCreated;
    }

    @DisplayName("Create a new tax from the REST API")
    @Test
    @Order(17)
    public void create_tax_by_api_rest() throws Exception {
        CreateOrUpdateTax createOrUpdateTax = new CreateOrUpdateTax();
        createOrUpdateTax.setCountryId(  COUNTRY_ID );
        createOrUpdateTax.setTaxType(  TaxType.TVA  );
        createOrUpdateTax.setPercent(  BigDecimal.valueOf( 20 ) );

        String createTaxJson = this.objectMapper.writeValueAsString(createOrUpdateTax);
        String resultJson;

        // POST NEW
        RequestBuilder requestPost = MockMvcRequestBuilders
                .post( PATH_POST_NEW_TAX )
                .accept( MediaType.APPLICATION_JSON )
                .content( createTaxJson )
                .contentType( MediaType.APPLICATION_JSON );

        MvcResult result = mockMvc.perform( requestPost )
                .andExpect( status().isCreated() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                .andReturn();

        // The JSON response
        resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson );
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        TaxDTO createResult = this.objectMapper.readValue( resultJson, TaxDTO.class );

        Assert.assertNotNull( createResult );

        TAX_ID = createResult.getId();

        Assert.assertEquals( createOrUpdateTax.getPercent(), createResult.getPercent() );
        Assert.assertEquals( createOrUpdateTax.getTaxType(), createResult.getTaxType() );
    }

    @DisplayName("Patch a tax from the REST API")
    @Test
    @Order(18)
    public void patch_tax_by_api_rest() throws Exception {

        BigDecimal newValue = BigDecimal.valueOf( 19.6 );

        JsonPatch jsonPatch = Json.createPatchBuilder()
                .replace("/percent", newValue.toString() )
                .build();

        String patchTax = jsonPatch.toString();

        String newPathId = String.format( PATH_GET_TAX, TAX_ID );

        // POST NEW
        RequestBuilder requestPost = MockMvcRequestBuilders
                .patch( newPathId )
                .accept( MediaType.APPLICATION_JSON )
                .content( patchTax )
                .contentType( APPLICATION_JSON_PATCH_JSON_VALUE );

        MvcResult result = mockMvc.perform( requestPost )
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                .andReturn();

        // The JSON response
        String resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson );
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        TaxDTO createResult = this.objectMapper.readValue( resultJson, TaxDTO.class );

        Assert.assertNotNull( createResult );

        Assert.assertEquals( newValue, createResult.getPercent() );
        //Assert.assertEquals( createTax.getTaxType().get(), createResult.getTaxType() );

    }

    @DisplayName("Put a tax from the REST API")
    @Test
    @Order(19)
    public void put_tax_by_api_rest() throws Exception {

        CreateOrUpdateTax createOrUpdateTax = new CreateOrUpdateTax();
        createOrUpdateTax.setCountryId(  COUNTRY_ID );
        createOrUpdateTax.setTaxType(  TaxType.TVA  );
        createOrUpdateTax.setPercent(  BigDecimal.valueOf( 43 ) );

        String createTaxJson = this.objectMapper.writeValueAsString(createOrUpdateTax);

        String newPathId = String.format( PATH_GET_TAX, TAX_ID );

        // PUT NEW
        RequestBuilder requestPost = MockMvcRequestBuilders
                .put( newPathId )
                .accept( MediaType.APPLICATION_JSON_UTF8_VALUE )
                .content( createTaxJson )
                .contentType( MediaType.APPLICATION_JSON_UTF8_VALUE );

        MvcResult result = mockMvc.perform( requestPost )
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                .andReturn();

        // The JSON response
        String resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson );
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        TaxDTO createResult = this.objectMapper.readValue( resultJson, TaxDTO.class );

        Assert.assertEquals( TAX_ID, createResult.getId() );
        Assert.assertEquals( createOrUpdateTax.getPercent(), createResult.getPercent() );
        Assert.assertEquals( createOrUpdateTax.getTaxType(), createResult.getTaxType() );

    }

    @DisplayName("Create news tags from the REST API")
    @Test
    @Order(20)
    public void create_tags_by_api_rest() throws Exception {
        CreateTag createTagPromo = new CreateTag();
        createTagPromo.setLabel( "PROMOTION" );

        CreateTag createTagFlash = new CreateTag();
        createTagFlash.setLabel( "FLASH" );

        CreateTag createTagBlackFriday = new CreateTag();
        createTagBlackFriday.setLabel( "BLACK_FRIDAY" );

        CreateTag createTagNew = new CreateTag();
        createTagNew.setLabel("NEW");

        CreateTag createTagMusic = new CreateTag();
        createTagMusic.setLabel("MUSIC");

        CreateTag createTagPro = new CreateTag();
        createTagPro.setLabel("PROFESSIONAL_CUSTOMER");

        CreateTag createTagStoreToto = new CreateTag();
        createTagStoreToto.setLabel("STORE_TOTO");


        Map<CreateTag, Integer> mapTags = new HashMap<>();
        // PROMO
        mapTags.put( createTagPromo, null );
        mapTags.put( createTagFlash, null );
        mapTags.put( createTagBlackFriday, null );

        mapTags.put( createTagNew, null );
        mapTags.put( createTagMusic, null );
        mapTags.put( createTagStoreToto, null );

        // POST - CREATE
        for( Map.Entry<CreateTag, Integer> entry : mapTags.entrySet() ) {
            String createJson = this.objectMapper.writeValueAsString( entry.getKey() );

            // POST
            RequestBuilder requestPost = MockMvcRequestBuilders
                    .post(PATH_POST_NEW_TAGS)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(createJson)
                    .contentType(MediaType.APPLICATION_JSON);

            MvcResult result = mockMvc.perform(requestPost)
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andReturn();

            // The JSON response
            String resultJson = result.getResponse().getContentAsString();

            Assert.assertNotNull( resultJson );
            Assert.assertFalse( resultJson.isEmpty() );

            // The OBJECT response
            TagDTO tagResult = this.objectMapper.readValue( resultJson, TagDTO.class );

            Assert.assertNotNull( tagResult );
            Assert.assertNotNull( tagResult.getId() );

            // ID
            mapTags.put( entry.getKey(), tagResult.getId() );
        }

        // CHECK
        mapTags.forEach((key, value) -> {
            Assert.assertNotNull(value);
            if (Objects.equals(key, createTagPromo))
                TAG_PROMO_ID = value;
            else if (Objects.equals(key, createTagFlash))
                TAG_PROMO_FLASH_ID = value;
            else if (Objects.equals(key, createTagBlackFriday))
                TAG_PROMO_BLACK_FRIDAY_ID = value;

            else if (Objects.equals(key, createTagNew))
                TAG_NEW_ID = value;
            else if (Objects.equals(key, createTagMusic))
                TAG_MUSIC_ID = value;
            else if (Objects.equals(key, createTagStoreToto))
                TAG_STORE_TOTO_ID = value;
        });

        // CREATE LINK
        CreateTagLink createTagLinkPromotionFlash = new CreateTagLink();
        createTagLinkPromotionFlash.setTagParentId( TAG_PROMO_ID );
        createTagLinkPromotionFlash.setTagChildId( TAG_PROMO_FLASH_ID );
        createTagLinkPromotionFlash.setLinkStrength( 100 );

        CreateTagLink createTagLinkPromotionBlackFriday = new CreateTagLink();
        createTagLinkPromotionBlackFriday.setTagParentId( TAG_PROMO_ID );
        createTagLinkPromotionBlackFriday.setTagChildId( TAG_PROMO_BLACK_FRIDAY_ID );
        createTagLinkPromotionBlackFriday.setLinkStrength( 100 );

        List<CreateTagLink> tagsLinkList = new ArrayList<>();
        tagsLinkList.add( createTagLinkPromotionFlash );
        tagsLinkList.add( createTagLinkPromotionBlackFriday );

        // POST - LINK
        for( CreateTagLink createTagLink : tagsLinkList ) {
            String createJson = this.objectMapper.writeValueAsString( createTagLink );

            // POST
            RequestBuilder requestPost = MockMvcRequestBuilders
                    .post(PATH_POST_NEW_TAGS_LINK)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(createJson)
                    .contentType(MediaType.APPLICATION_JSON);

            MvcResult result = mockMvc.perform(requestPost)
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andReturn();

            // The JSON response
            String resultJson = result.getResponse().getContentAsString();

            Assert.assertNotNull( resultJson );
            Assert.assertFalse( resultJson.isEmpty() );

            // The OBJECT response
            TagLinkDTO tagResult = this.objectMapper.readValue( resultJson, TagLinkDTO.class );
            Assert.assertNotNull( tagResult );

        }

    }

    @DisplayName("Create a new store from the REST API")
    @Test
    @Order(25)
    public void create_store_by_api_rest() throws Exception {

        Store storeToto = GENERATOR.getStoreToto();
        StoreDTO storeDTO = this.storeService.toDTO( storeToto );
        String storeJson = this.objectMapper.writeValueAsString( storeDTO );

        // PUSH NEW store
        RequestBuilder requestPost = MockMvcRequestBuilders
                .post( PATH_POST_NEW_STORE )
                .accept( MediaType.APPLICATION_JSON )
                .content( storeJson )
                .contentType( MediaType.APPLICATION_JSON );

        MvcResult result = mockMvc.perform( requestPost )
                .andExpect( status().isCreated() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andReturn();

        // The JSON response
        String resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson );
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        StoreDTO storeResult = this.objectMapper.readValue( resultJson, StoreDTO.class );

        Assert.assertNotNull( storeResult );
        Assert.assertNotNull( storeResult.getId() );

        // ID Language
        Integer idStoreCreated = storeResult.getId();

        String newPathStoreId = String.format( PATH_GET_STORE, idStoreCreated );
        // FIND Language
        RequestBuilder requestGet = MockMvcRequestBuilders
                .get( newPathStoreId )
                .accept( MediaType.APPLICATION_JSON );

        MvcResult resultGet = mockMvc.perform( requestGet )
                .andExpect( status().isFound() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andReturn();

        // The JSON response
        String resultGetJson = resultGet.getResponse().getContentAsString();

        Assert.assertNotNull( resultGetJson );
        Assert.assertFalse( resultGetJson.isEmpty() );

        // The OBJECT response
        StoreDTO storeGetResult = this.objectMapper.readValue( resultGetJson, StoreDTO.class );

        Assert.assertNotNull( storeGetResult );

        // TEST INSERT
        Assert.assertEquals( idStoreCreated , storeGetResult.getId() );
        Assert.assertEquals( storeToto.getCode() , storeGetResult.getCode() );
        Assert.assertEquals( storeToto.getLabel() , storeGetResult.getLabel() );
        Assert.assertEquals( storeToto.getWebSite() , storeGetResult.getWebSite() );

        STORE_TOTO_ID = idStoreCreated;
    }

    @DisplayName("Create a second store from the REST API")
    @Test
    @Order(26)
    public void create_second_store_by_api_rest() throws Exception {
        Store storeToto = GENERATOR.getStoreToto();
        StoreDTO storeDTO = this.storeService.toDTO( storeToto );
        String storeJson = this.objectMapper.writeValueAsString( storeDTO );

        // PUSH NEW store
        RequestBuilder requestPost = MockMvcRequestBuilders
                .post( PATH_POST_NEW_STORE )
                .accept( MediaType.APPLICATION_JSON )
                .content( storeJson )
                .contentType( MediaType.APPLICATION_JSON );

        MvcResult result = mockMvc.perform( requestPost )
                .andExpect( status().isCreated() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andReturn();

        // The JSON response
        String resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson );
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        StoreDTO storeResult = this.objectMapper.readValue( resultJson, StoreDTO.class );

        Assert.assertNotNull( storeResult );
        Assert.assertNotNull( storeResult.getId() );

        // ID Language
        STORE_E_COMMERCE_ID = storeResult.getId();
    }

    @DisplayName("Create a new currency from the REST API")
    @Test
    @Order(30)
    public void create_currency_by_api_rest() throws Exception {

        Currency currencyEuro = GENERATOR.getCurrencyEuro();
        CurrencyDTO currencyDTO = this.currencyService.toDTO( currencyEuro );
        String currencyJson = this.objectMapper.writeValueAsString( currencyDTO );

        // PUSH NEW store
        RequestBuilder requestPost = MockMvcRequestBuilders
                .post( PATH_POST_NEW_CURRENCY )
                .accept( MediaType.APPLICATION_JSON )
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .content( currencyJson )
                .contentType( MediaType.APPLICATION_JSON );

        MvcResult result = mockMvc.perform( requestPost )
                .andExpect( status().isCreated() )
                //.andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andReturn();

        // The JSON response
        String resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson );
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        CurrencyDTO currencyResult = this.objectMapper.readValue( resultJson, CurrencyDTO.class );

        Assert.assertNotNull( currencyResult );
        Assert.assertNotNull( currencyResult.getId() );

        // ID Language
        Integer idCurrencyCreated = currencyResult.getId();

        String newPathStoreId = String.format( PATH_GET_CURRENCY, idCurrencyCreated );
        // FIND Language
        RequestBuilder requestGet = MockMvcRequestBuilders
                .get( newPathStoreId )
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .accept( MediaType.APPLICATION_JSON );

        MvcResult resultGet = mockMvc.perform( requestGet )
                .andExpect( status().isFound() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                //.andExpect( content().encoding( StandardCharsets.UTF_8.name() ))
                .andReturn();

        // The JSON response
        String resultGetJson = resultGet.getResponse().getContentAsString();

        Assert.assertNotNull( resultGetJson );
        Assert.assertFalse( resultGetJson.isEmpty() );

        // The OBJECT response
        CurrencyDTO currencyGetResult = this.objectMapper.readValue( resultGetJson, CurrencyDTO.class );

        Assert.assertNotNull( currencyGetResult );

        // TEST INSERT
        Assert.assertEquals( idCurrencyCreated , currencyGetResult.getId() );
        Assert.assertEquals( currencyEuro.getSymbol() , currencyGetResult.getSymbol() );
        Assert.assertEquals( currencyEuro.getLabel() , currencyGetResult.getLabel() );

        CURRENCY_ID = idCurrencyCreated;
    }

    @DisplayName("Create a new customer group from the REST API")
    @Test
    @Order(40)
    public void create_customer_group_by_api_rest() throws Exception {

        CustomerGroup customerGroup = GENERATOR.getCustomerGroupPublic();
        CustomerGroupDTO customerGroupDTO = this.customerGroupService.toDTO( customerGroup );
        String customerGroupJson = this.objectMapper.writeValueAsString( customerGroupDTO );

        // PUSH NEW store
        RequestBuilder requestPost = MockMvcRequestBuilders
                .post( PATH_POST_NEW_CUSTOMER_GROUP )
                .accept( MediaType.APPLICATION_JSON )
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .content(customerGroupJson)
                .contentType( MediaType.APPLICATION_JSON );

        MvcResult result = mockMvc.perform( requestPost )
                .andExpect( status().isCreated() )
                //.andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andReturn();

        // The JSON response
        String resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson );
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        CustomerGroupDTO customerGroupResult = this.objectMapper.readValue( resultJson, CustomerGroupDTO.class );

        Assert.assertNotNull( customerGroupResult );
        Assert.assertNotNull( customerGroupResult.getId() );

        // ID Language
        Integer idCurrencyCreated = customerGroupResult.getId();

        String newPathStoreId = String.format( PATH_GET_CUSTOMER_GROUP, idCurrencyCreated );
        // FIND Language
        RequestBuilder requestGet = MockMvcRequestBuilders
                .get( newPathStoreId )
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .accept( MediaType.APPLICATION_JSON );

        MvcResult resultGet = mockMvc.perform( requestGet )
                .andExpect( status().isFound() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                //.andExpect( content().encoding( StandardCharsets.UTF_8.name() ))
                .andReturn();

        // The JSON response
        String resultGetJson = resultGet.getResponse().getContentAsString();

        Assert.assertNotNull( resultGetJson );
        Assert.assertFalse( resultGetJson.isEmpty() );

        // The OBJECT response
        CustomerGroupDTO customerGroupGetResult = this.objectMapper.readValue( resultGetJson, CustomerGroupDTO.class );

        Assert.assertNotNull( customerGroupGetResult );

        // TEST INSERT
        Assert.assertEquals( idCurrencyCreated , customerGroupGetResult.getId() );
        Assert.assertEquals( customerGroupDTO.getLabel() , customerGroupGetResult.getLabel());
        Assert.assertEquals( customerGroupDTO.getType() , customerGroupGetResult.getType() );

        CUSTOMER_GROUP_ID = idCurrencyCreated;
    }

    @DisplayName("Create a property unit from the REST API")
    @Test
    @Order(50)
    public void create_property_unit_by_api_rest() throws Exception {

        CreatePropertyUnit createPropertyUnit = new CreatePropertyUnit();
        createPropertyUnit.setPropertyType( PropertyType.NUMBER);

        // TRANSLATION
        CreatePropertyUnitTranslation createPropertyUnitTranslation = new CreatePropertyUnitTranslation();
        createPropertyUnitTranslation.setLabel( "métre");
        createPropertyUnitTranslation.setLabelLite("m");
        createPropertyUnitTranslation.setLanguageID( LANGUAGE_ID );
        List<CreatePropertyUnitTranslation> createPropertyUnitTranslations = new ArrayList<>();
        createPropertyUnitTranslations.add( createPropertyUnitTranslation );
        createPropertyUnit.setPropertyUnitTranslationList( createPropertyUnitTranslations );

        String propertyJson = this.objectMapper.writeValueAsString( createPropertyUnit );

        // PUSH NEW store
        RequestBuilder requestPost = MockMvcRequestBuilders
                .post( PATH_POST_NEW_PROPERTIES_UNITS )
                .accept( MediaType.APPLICATION_JSON )
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .content(propertyJson)
                .contentType( MediaType.APPLICATION_JSON );

        MvcResult result = mockMvc.perform( requestPost )
                .andExpect( status().isCreated() )
                //.andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andReturn();

        // The JSON response
        String resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson );
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        PropertyUnitDTO propertyResult = this.objectMapper.readValue( resultJson, PropertyUnitDTO.class );

        Assert.assertNotNull( propertyResult );
        Assert.assertNotNull( propertyResult.getId() );

        // ID Language
        Integer idPropertyUnitCreated = propertyResult.getId();

        String newPathPropertyId = String.format( PATH_GET_PROPERTIES_UNITS, idPropertyUnitCreated );
        // FIND Language
        RequestBuilder requestGet = MockMvcRequestBuilders
                .get( newPathPropertyId )
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .accept( MediaType.APPLICATION_JSON );

        MvcResult resultGet = mockMvc.perform( requestGet )
                .andExpect( status().isFound() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                //.andExpect( content().encoding( StandardCharsets.UTF_8.name() ))
                .andReturn();

        // The JSON response
        String resultGetJson = resultGet.getResponse().getContentAsString();

        Assert.assertNotNull( resultGetJson );
        Assert.assertFalse( resultGetJson.isEmpty() );

        // The OBJECT response
        PropertyUnitDTO propertyGetResult = this.objectMapper.readValue( resultGetJson, PropertyUnitDTO.class );

        Assert.assertNotNull( propertyGetResult );

        // TEST INSERT
        Assert.assertEquals( idPropertyUnitCreated , propertyGetResult.getId() );
        Assert.assertEquals( createPropertyUnit.getPropertyType() , propertyGetResult.getPropertyType());
        Assert.assertNotNull(  propertyGetResult.getPropertyUnitTranslationList() );
        Assert.assertFalse( propertyGetResult.getPropertyUnitTranslationList().isEmpty() );

        PROPERTY_UNIT_ID = idPropertyUnitCreated;
    }

    @DisplayName("Create a new property from the REST API")
    @Test
    @Order(60)
    public void create_properties_by_api_rest() throws Exception {
        CreateProperty createProperty = new CreateProperty();
        createProperty.setEnable( true );
        createProperty.setPropertyUnitId( PROPERTY_UNIT_ID );
        createProperty.setTranslations( Arrays.asList( CreatePropertyTranslation.builder()
                .label( "Hauteur")
                .languageId( LANGUAGE_ID ).build() ) );

        CreatePropertyValue propertyValueItem1 = new CreatePropertyValue();
        propertyValueItem1.setType( PropertyType.NUMBER );
        propertyValueItem1.setValueNumber( 12.5 );

        CreatePropertyValue propertyValueItem2 = new CreatePropertyValue();
        propertyValueItem2.setType( PropertyType.NUMBER );
        propertyValueItem2.setValueNumber( 4.0 );

        CreatePropertyValue propertyValueItem3 = new CreatePropertyValue();
        propertyValueItem3.setType( PropertyType.NUMBER );
        propertyValueItem3.setValueNumber( 0.5 );

        createProperty.setPropertyValues( Arrays.asList( propertyValueItem1, propertyValueItem2, propertyValueItem3));

        String propertyJson = this.objectMapper.writeValueAsString( createProperty );

        // PUSH
        RequestBuilder requestPost = MockMvcRequestBuilders
                .post( PATH_POST_NEW_PROPERTIES )
                .accept( MediaType.APPLICATION_JSON )
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .content(propertyJson)
                .contentType( MediaType.APPLICATION_JSON );

        MvcResult result = mockMvc.perform( requestPost )
                .andExpect( status().isCreated() )
                //.andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andReturn();

        // The JSON response
        String resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson );
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        PropertyDTO propertyResult = this.objectMapper.readValue( resultJson, PropertyDTO.class );

        Assert.assertNotNull( propertyResult );
        Assert.assertNotNull( propertyResult.getId() );

        // ID Language
        Integer idPropertyCreated = propertyResult.getId();

        String newPathPropertyId = String.format( PATH_GET_PROPERTIES , idPropertyCreated );
        // FIND Language
        RequestBuilder requestGet = MockMvcRequestBuilders
                .get( newPathPropertyId )
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .accept( MediaType.APPLICATION_JSON );

        MvcResult resultGet = mockMvc.perform( requestGet )
                .andExpect( status().isFound() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                .andReturn();

        // The JSON response
        String resultGetJson = resultGet.getResponse().getContentAsString();

        Assert.assertNotNull( resultGetJson );
        Assert.assertFalse( resultGetJson.isEmpty() );

        // The OBJECT response
        PropertyDTO propertyGetResult = this.objectMapper.readValue( resultGetJson, PropertyDTO.class );

        Assert.assertNotNull( propertyGetResult );

        // TEST - PROPERTY
        Assert.assertEquals( idPropertyCreated , propertyGetResult.getId() );
        Assert.assertEquals( createProperty.getPropertyUnitId() , propertyGetResult.getPropertyUnit().getId());
        Assert.assertNotNull(  propertyGetResult.getTranslations() );
        Assert.assertFalse(  propertyGetResult.getTranslations().isEmpty() );

        PROPERTY_ID = idPropertyCreated;

        // TEST - PROPERTY  VALUES
        Assert.assertNotNull(  propertyGetResult.getPropertyValues() );
        Assert.assertFalse(  propertyGetResult.getPropertyValues().isEmpty() );

        List<PropertyValueDTO> propertyValues = propertyGetResult.getPropertyValues();
        boolean propertyValue1Validated = false;
        boolean propertyValue2Validated = false;
        boolean propertyValue3Validated = false;
        for( PropertyValueDTO propertyValue : propertyValues ){

            // Property value 1
            if( Objects.equals( propertyValueItem1.getType(), propertyValue.getType() )
                    && Objects.equals( propertyValueItem1.getValueNumber(), propertyValue.getValueNumber() )  ){
                propertyValue1Validated = true;
                PROPERTY_VALUE_1_ID = propertyValue.getId();
            }
            // Property value 2
            else if( Objects.equals( propertyValueItem2.getType(), propertyValue.getType() )
                    && Objects.equals( propertyValueItem2.getValueNumber(), propertyValue.getValueNumber() )  ){
                propertyValue2Validated = true;
            }
            // Property value 3
            else if( Objects.equals( propertyValueItem3.getType(), propertyValue.getType() )
                    && Objects.equals( propertyValueItem3.getValueNumber(), propertyValue.getValueNumber() )  ){
                propertyValue3Validated = true;
            }
        }

        Assert.assertTrue( propertyValue1Validated );
        Assert.assertTrue( propertyValue2Validated );
        Assert.assertTrue( propertyValue3Validated );

    }

    @DisplayName("Create a new property from the REST API")
    @Test
    @Order(61)
    public void create_properties_values_by_api_rest() throws Exception {

    }

    @DisplayName("Update a property from the REST API")
    @Test
    @Order(65)
    public void update_properties_by_api_rest() throws Exception {

    }

    @DisplayName("Create a new product from the REST API")
    @Test
    @Order(100)
    public void create_product_by_api_rest () throws Exception {

        // CHECK DATA
        Assert.assertNotNull( "Ce test est dépendant des tests précedents ! ", CURRENCY_ID);
        Assert.assertNotNull( "Ce test est dépendant des tests précedents ! ", STORE_TOTO_ID);
        Assert.assertNotNull( "Ce test est dépendant des tests précedents ! ", CUSTOMER_GROUP_ID);
        Assert.assertNotNull( "Ce test est dépendant des tests précedents ! ", PROPERTY_ID);

        Product product = GENERATOR.getProductTest1();
        double priceInit = 123.56;

        // PRICE
        CreatePriceDTO createPrice = new CreatePriceDTO();
        createPrice.setPrice( BigDecimal.valueOf( priceInit ) );
        createPrice.setCurrencyId( CURRENCY_ID);
        createPrice.setPriceType( PriceType.HT );
        createPrice.setCurrentPrice( true );
        createPrice.setStoreId(STORE_TOTO_ID);
        createPrice.setCustomerGroupId( CUSTOMER_GROUP_ID);

        // PROPERTY
        double hauteur = 1.2;
        CreateProductProperty createProductProperty = new CreateProductProperty();
        createProductProperty.setPropertyId( PROPERTY_ID );
        createProductProperty.setPropertyValueIdList( Arrays.asList( PROPERTY_VALUE_1_ID ) );

        // PRODUCT
        CreateProduct createProduct = new CreateProduct();
        createProduct.setReference( product.getReference() );

        List<CreatePriceDTO> createProductPriceList = Arrays.asList( createPrice );
        createProduct.setPrices( createProductPriceList );

        List<CreateProductProperty> createProductPropertyList = Arrays.asList( createProductProperty );
        createProduct.setPropertyProductList( createProductPropertyList );

        // PRODUCT - TAGS
        createProduct.setTagIdList( Arrays.asList( TAG_MUSIC_ID ) );

        String createProductJson = this.objectMapper.writeValueAsString( createProduct );

        // PUSH NEW store
        RequestBuilder requestPost = MockMvcRequestBuilders
                .post( PATH_POST_NEW_PRODUCT )
                .accept( MediaType.APPLICATION_JSON )
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .content( createProductJson )
                .contentType( MediaType.APPLICATION_JSON );

        MvcResult result = mockMvc.perform( requestPost )
                .andExpect( status().isCreated() )
                //.andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andReturn();

        // The JSON response
        String resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson );
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        ProductDTO productResult = this.objectMapper.readValue( resultJson, ProductDTO.class );

        Assert.assertNotNull( productResult );
        Assert.assertNotNull( productResult.getId() );

        // ID Language
        Integer idProductCreated = productResult.getId();

        String newPathProductId = String.format( PATH_GET_PRODUCT, idProductCreated );
        // FIND Language
        RequestBuilder requestGet = MockMvcRequestBuilders
                .get( newPathProductId )
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .accept( MediaType.APPLICATION_JSON );

        MvcResult resultGet = mockMvc.perform( requestGet )
                .andExpect( status().isFound() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                //.andExpect( content().encoding( StandardCharsets.UTF_8.name() ))
                .andReturn();

        // The JSON response
        String resultGetJson = resultGet.getResponse().getContentAsString();

        Assert.assertNotNull( resultGetJson );
        Assert.assertFalse( resultGetJson.isEmpty() );

        // The OBJECT response
        ProductDTO productGetResult = this.objectMapper.readValue( resultGetJson, ProductDTO.class );

        Assert.assertNotNull( productGetResult );

        // TEST INSERT
        Assert.assertEquals( idProductCreated , productGetResult.getId() );
        Assert.assertEquals( createProduct.getReference() , productGetResult.getReference() );
        Assert.assertNotNull(  productGetResult.getPrices() );
        Assert.assertFalse(  productGetResult.getPrices().isEmpty() );

        // TAGS
        Assert.assertNotNull(  productGetResult.getTagList() );

        PRODUCT_ID = productGetResult.getId();

        // TEST PRICES
        List<PriceDTO> pricesDto = productGetResult.getPrices();
        for( PriceDTO priceDto :  pricesDto ){
            Assert.assertEquals( createPrice.getStoreId() , priceDto.getStoreId() );
            Assert.assertEquals( createPrice.getCustomerGroupId() , priceDto.getCustomerGroupId() );
            Assert.assertNotNull( priceDto.getId() );
            Assert.assertEquals( createPrice.getCurrencyId() , priceDto.getCurrency().getId() );
            Assert.assertEquals( createPrice.getPrice() , priceDto.getPrice() );
            PRICE_HT_ID = priceDto.getId();
        }

        // TEST PROPERTIES
        List<ProductPropertyDTO> propertiesDto = productGetResult.getPropertyProductList();
        Assert.assertNotNull( propertiesDto );
        Assert.assertFalse( propertiesDto.isEmpty() );
        for( ProductPropertyDTO productProperty : propertiesDto ){

            PropertyDTO property = productProperty.getProperty();
            List<PropertyValueDTO> propertyValues = productProperty.getPropertyValues();

            Assert.assertNotNull( property );
            Assert.assertNotNull( propertyValues );
            Assert.assertFalse( propertyValues.isEmpty() );
        }

    }

    @DisplayName("Get the product Elasticsearch from the REST API")
    //@Test
    @Order(101)
    public void get_product_view_by_api_rest() throws Exception {

        // ID Language
        Integer idProductFound = PRODUCT_ID;

        String newPathProductId = String.format( PATH_PRODUCT_VIEW, idProductFound );
        // FIND
        RequestBuilder requestGet = MockMvcRequestBuilders
                .get( newPathProductId )
                .param("store_id", String.valueOf(STORE_TOTO_ID))
                .param( "language_id", String.valueOf(LANGUAGE_ID))
                .param( "currency_id", String.valueOf(CURRENCY_ID))
                .param( "customer_group_id", String.valueOf(CUSTOMER_GROUP_ID))
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .accept( MediaType.APPLICATION_JSON );

        MvcResult resultGet = mockMvc.perform( requestGet )
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                //.andExpect( content().encoding( StandardCharsets.UTF_8.name() ))
                .andReturn();


        // The JSON response
        String resultGetJson = resultGet.getResponse().getContentAsString();

        Assert.assertNotNull( resultGetJson );
        Assert.assertFalse( resultGetJson.isEmpty() );

        // The OBJECT response
        ProductView productGetResult = this.objectMapper.readValue( resultGetJson, ProductView.class );
        Assert.assertNotNull( productGetResult );

        Assert.assertEquals( idProductFound , productGetResult.getId() );

    }

    @DisplayName("Create a new promotion from the REST API")
    @Test
    @Order(150)
    public void create_promotion_by_api_rest() throws Exception {

        // PROMO
        CreateOrUpdatePromotion createOrUpdatePromotion = new CreateOrUpdatePromotion();
        createOrUpdatePromotion.setStoreId( STORE_TOTO_ID );
        createOrUpdatePromotion.setEnable(  true  );
        createOrUpdatePromotion.setStartAt(  new Date()  );
        createOrUpdatePromotion.setFinishAt( new Date()  );
        createOrUpdatePromotion.setCustomerGroupId(  CUSTOMER_GROUP_ID );
        createOrUpdatePromotion.setTagsId( Arrays.asList( TAG_PROMO_FLASH_ID ) );
        createOrUpdatePromotion.setCountryId( COUNTRY_ID );
        createOrUpdatePromotion.setUserAction( null );


        // PRICE - PROMO
        CreatePriceDTO createPricePromo = new CreatePriceDTO();
        createPricePromo.setPromotionId( null );
        createPricePromo.setPriceInitialId( PRICE_HT_ID );
        createPricePromo.setStoreId(STORE_TOTO_ID);
        createPricePromo.setCustomerGroupId( CUSTOMER_GROUP_ID );
        createPricePromo.setCurrencyId( CURRENCY_ID );
        createPricePromo.setCurrentPrice( true );
        createPricePromo.setPriceType( PriceType.PROMO );
        createPricePromo.setProductId( PRODUCT_ID );
        createPricePromo.setPrice( BigDecimal.valueOf( 50.00 ) );

        createOrUpdatePromotion.setPricePromotion(  Arrays.asList( createPricePromo ) );

        String promotionJson =  this.objectMapper.writeValueAsString(createOrUpdatePromotion);

        // PUSH NEW store
        RequestBuilder requestPost = MockMvcRequestBuilders
                .post(PATH_POST_NEW_PROMOTION)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(  promotionJson )
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestPost)
                .andExpect( status().isCreated() )
                //.andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andDo( MockMvcResultHandlers.print() )
                .andReturn();

        // The JSON response
        String resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson);
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        PromotionDTO promotionResult = this.objectMapper.readValue( resultJson, PromotionDTO.class );

        Assert.assertNotNull( promotionResult );
        Assert.assertNotNull( promotionResult.getId() );

        // ID
        Integer idPromotionCreated = promotionResult.getId();
        PROMOTION_ID = idPromotionCreated;

        String newPathPromotionId = String.format( PATH_GET_PROMOTION, idPromotionCreated );
        // FIND Promotion
        RequestBuilder requestGet = MockMvcRequestBuilders
                .get( newPathPromotionId )
                .characterEncoding(StandardCharsets.UTF_8.name())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult resultGet = mockMvc.perform(requestGet)
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                //.andExpect( content().encoding( StandardCharsets.UTF_8.name() ))
                .andReturn();

        // The JSON response
        String resultGetJson = resultGet.getResponse().getContentAsString();

        Assert.assertNotNull( resultGetJson );
        Assert.assertFalse( resultGetJson.isEmpty() );

        // The OBJECT response
        PromotionDTO promotionGetResult = this.objectMapper.readValue(resultGetJson, PromotionDTO.class);

        Assert.assertNotNull( promotionGetResult );

        // TEST PROMO
        Assert.assertEquals( idPromotionCreated, promotionGetResult.getId() );
        Assert.assertEquals( createOrUpdatePromotion.getStoreId(), promotionGetResult.getStoreId() );
        Assert.assertEquals( createOrUpdatePromotion.getCustomerGroupId(), promotionGetResult.getCustomerGroupId() );
        Assert.assertEquals( createOrUpdatePromotion.getEnable(), promotionGetResult.getEnable() );
        Assert.assertEquals( createOrUpdatePromotion.getStartAt(), promotionGetResult.getStartAt() );
        Assert.assertEquals( createOrUpdatePromotion.getFinishAt(), promotionGetResult.getFinishAt() );
        Assert.assertNotNull( promotionGetResult.getPricePromotionsId() );
        Assert.assertFalse( promotionGetResult.getPricePromotionsId().isEmpty() );

        Assert.assertEquals( createOrUpdatePromotion.getTagsId().size() , promotionGetResult.getTags().size() );

        // PROMOTION - TAGS
        List<Integer> tagDtoIdList = new ArrayList<>();
        promotionGetResult.getTags().forEach( tagDTO -> tagDtoIdList.add( tagDTO.getId() ) );

        createOrUpdatePromotion.getTagsId().forEach(tagId -> {
            Assert.assertTrue( tagDtoIdList.contains( tagId )); }
        );

        // TEST PRICE PROMO
        promotionGetResult.getPricePromotionsId().forEach( Assert::assertNotNull );

        // CHECK PRICE PROMO
        Integer priceId = promotionGetResult.getPricePromotionsId().get( 0 );
        String newPathPriceId = String.format( PATH_GET_PRICE, priceId );
        // FIND PRICE
        RequestBuilder requestGetPrice = MockMvcRequestBuilders
                .get( newPathPriceId )
                .characterEncoding(StandardCharsets.UTF_8.name())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult resultGetPrice = mockMvc.perform( requestGetPrice )
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        // The JSON response
        String resultGetPriceJson = resultGetPrice.getResponse().getContentAsString();

        Assert.assertNotNull( resultGetPriceJson );
        Assert.assertFalse( resultGetPriceJson.isEmpty() );

        PriceDTO priceGetResult = this.objectMapper.readValue( resultGetPriceJson, PriceDTO.class );
        Assert.assertNotNull( priceGetResult );
        Assert.assertEquals( priceId, priceGetResult.getId() );
        Assert.assertEquals(0, createPricePromo.getPrice().compareTo(priceGetResult.getPrice()));
        Assert.assertEquals( createPricePromo.getProductId(), priceGetResult.getProductId() );
        Assert.assertEquals( createPricePromo.getPriceType(), priceGetResult.getPriceType() );
        Assert.assertEquals( createPricePromo.getCustomerGroupId(), priceGetResult.getCustomerGroupId() );
        Assert.assertEquals( idPromotionCreated, priceGetResult.getPromotionId() );
        Assert.assertEquals( createPricePromo.getPriceInitialId(), priceGetResult.getPriceInitialId() );

        PRICE_PROMO_ID = priceGetResult.getId();

    }

    @DisplayName("Put a promotion from the REST API")
    @Test
    @Order(152)
    public void put_promotion_by_api_rest() throws Exception {

        // FIND OLD PROMOTION
        String newPathPromotionId = String.format( PATH_GET_PROMOTION, PROMOTION_ID );
        // FIND Promotion
        RequestBuilder requestGet = MockMvcRequestBuilders
                .get( newPathPromotionId )
                .characterEncoding(StandardCharsets.UTF_8.name())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult resultGet = mockMvc.perform(requestGet)
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                //.andExpect( content().encoding( StandardCharsets.UTF_8.name() ))
                .andReturn();

        // The JSON response
        String resultGetJson = resultGet.getResponse().getContentAsString();
        Assert.assertNotNull( resultGetJson );
        Assert.assertFalse( resultGetJson.isEmpty() );

        // The OBJECT response
        PromotionDTO promotionGetResult = this.objectMapper.readValue( resultGetJson, PromotionDTO.class );
        Assert.assertNotNull( promotionGetResult );

        //
        //  GET PRICES
        //
        List<String> pricesIdString = new ArrayList<>();
        promotionGetResult.getPricePromotionsId().forEach( priceID -> pricesIdString.add( priceID.toString() ));

        RequestBuilder requestGetPrice = MockMvcRequestBuilders
                .get( PATH_POST_NEW_PRICE )
                .param( "ids", pricesIdString.toArray(String[]::new) )
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .accept(MediaType.APPLICATION_JSON);

        MvcResult resultGetPrices = mockMvc.perform( requestGetPrice )
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                //.andExpect( content().encoding( StandardCharsets.UTF_8.name() ))
                .andReturn();

        // The JSON response
        String resultGetPricesJson = resultGetPrices.getResponse().getContentAsString();
        Assert.assertNotNull( resultGetPricesJson );
        Assert.assertFalse( resultGetPricesJson.isEmpty() );

        List<PriceDTO> pricesGetResult = this.objectMapper.readValue( resultGetPricesJson, new TypeReference<List<PriceDTO>>(){});
        Assert.assertNotNull( pricesGetResult );
        Assert.assertFalse( pricesGetResult.isEmpty() );

        List<CreatePriceDTO> pricesIds = new ArrayList<>();
        pricesGetResult.forEach( priceDTO -> {
            CreatePriceDTO createPriceDTO = new CreatePriceDTO( );
            createPriceDTO.setProductId( priceDTO.getProductId() );
            createPriceDTO.setPriceHtId( priceDTO.getPriceHtId() );
            createPriceDTO.setPrice( priceDTO.getPrice() );
            createPriceDTO.setPriceInitialId( priceDTO.getPriceInitialId() );
            createPriceDTO.setPriceType( priceDTO.getPriceType() );
            createPriceDTO.setCurrentPrice( priceDTO.getCurrentPrice() );
            createPriceDTO.setStoreId( priceDTO.getStoreId() );
            createPriceDTO.setEnable( priceDTO.getEnable() );
            createPriceDTO.setCurrencyId( priceDTO.getCurrency().getId() );
            createPriceDTO.setTaxId( priceDTO.getTaxId() );
            createPriceDTO.setCustomerGroupId( priceDTO.getCustomerGroupId() );
            pricesIds.add( createPriceDTO );
        } );

        //
        //
        //

        // UPDATE
        CreateOrUpdatePromotion updatePromotion = new CreateOrUpdatePromotion();
        updatePromotion.setStoreId(  STORE_E_COMMERCE_ID  );
        updatePromotion.setFinishAt( new Date() );
        updatePromotion.setStartAt( new Date() );
        updatePromotion.setCountryId( promotionGetResult.getCountryId() );
        updatePromotion.setEnable( false );
        updatePromotion.setTagsId( null );
        updatePromotion.setUserAction( null );
        updatePromotion.setCustomerGroupId( promotionGetResult.getCustomerGroupId() );
        updatePromotion.setPricePromotion( pricesIds );

        List<Integer> tagsId = new ArrayList<>();
        promotionGetResult.getTags().forEach( tagDTO -> tagsId.add( tagDTO.getId() ));
        updatePromotion.setTagsId( tagsId );

        Assert.assertNotEquals( "Les données ne doivent pas être identiques pour que le test soit validé !",
                promotionGetResult.getStoreId(), updatePromotion.getStoreId() );

        String promotionJson =  this.objectMapper.writeValueAsString( updatePromotion );

        // PUT NEW store
        RequestBuilder requestPost = MockMvcRequestBuilders
                .put( newPathPromotionId )
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content( promotionJson )
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestPost)
                .andExpect( status().isOk() )
                //.andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andDo( MockMvcResultHandlers.print() )
                .andReturn();

        // The JSON response
        String resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson);
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        PromotionDTO promotionResult = this.objectMapper.readValue( resultJson, PromotionDTO.class );

        Assert.assertNotNull( promotionResult );
        Assert.assertNotNull( promotionResult.getId() );

        // ID
        Integer idPromotionCreated = promotionResult.getId();

        Assert.assertEquals( idPromotionCreated, promotionResult.getId() );
        Assert.assertEquals( updatePromotion.getStoreId(), promotionResult.getStoreId() );
        Assert.assertEquals( updatePromotion.getCustomerGroupId(), promotionResult.getCustomerGroupId() );
        Assert.assertEquals( updatePromotion.getEnable(), promotionResult.getEnable() );
        Assert.assertEquals( updatePromotion.getStartAt(), promotionResult.getStartAt() );
        Assert.assertEquals( updatePromotion.getFinishAt(), promotionResult.getFinishAt() );
        Assert.assertNotNull( promotionResult.getPricePromotionsId() );
        Assert.assertFalse( promotionResult.getPricePromotionsId().isEmpty() );

        Assert.assertEquals( promotionGetResult.getTags().size() , promotionResult.getTags().size() );

    }

    @DisplayName("Patch a promotion from the REST API")
    @Test
    @Order(153)
    public void patch_promotion_by_api_rest() throws Exception {

        JsonPatch jsonPatch = Json.createPatchBuilder()
                .replace("/enable", true )
                .build();

        String patch = jsonPatch.toString();
        String newPathId = String.format( PATH_GET_PROMOTION , PROMOTION_ID );

        // PATCH
        RequestBuilder requestPost = MockMvcRequestBuilders
                .patch( newPathId )
                .accept( MediaType.APPLICATION_JSON_UTF8_VALUE )
                .content( patch )
                .contentType( APPLICATION_JSON_PATCH_JSON_VALUE );

        MvcResult result = mockMvc.perform( requestPost )
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                .andReturn();

        // The JSON response
        String resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson );
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        PromotionDTO promotionGetResult = this.objectMapper.readValue( resultJson, PromotionDTO.class );
        Assert.assertNotNull( promotionGetResult );

        Assert.assertEquals(true, promotionGetResult.getEnable() );

    }

    @DisplayName("Create news prices from the REST API")
    @Test
    @Order(160)
    public void create_price_by_api_rest() throws Exception {

        // PRICE
        CreatePriceDTO createPrice = new CreatePriceDTO();
        createPrice.setPrice( BigDecimal.valueOf( 45.56 ) );
        createPrice.setProductId( PRODUCT_ID );
        createPrice.setCurrencyId( CURRENCY_ID);
        createPrice.setPriceType( PriceType.TTC );
        createPrice.setCurrentPrice( true );
        createPrice.setStoreId(STORE_TOTO_ID);
        createPrice.setCustomerGroupId( CUSTOMER_GROUP_ID);
        createPrice.setPriceHtId( PRICE_HT_ID );
        createPrice.setTaxId( TAX_ID );

        String createProductJson = this.objectMapper.writeValueAsString( createPrice );

        // PUSH NEW store
        RequestBuilder requestPost = MockMvcRequestBuilders
                .post( PATH_POST_NEW_PRICE )
                .accept( MediaType.APPLICATION_JSON )
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .content( createProductJson )
                .contentType( MediaType.APPLICATION_JSON );

        MvcResult result = mockMvc.perform( requestPost )
                .andExpect( status().isCreated() )
                //.andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andReturn();

        // The JSON response
        String resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson );
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        PriceDTO priceDTO = this.objectMapper.readValue( resultJson, PriceDTO.class );

        Assert.assertNotNull( priceDTO );
        Assert.assertNotNull( priceDTO.getId() );

        // ID
        Integer idPriceCreated = priceDTO.getId();

        String newPathPriceId = String.format( PATH_GET_PRICE, idPriceCreated );
        // FIND Language
        RequestBuilder requestGet = MockMvcRequestBuilders
                .get( newPathPriceId )
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .accept( MediaType.APPLICATION_JSON );

        MvcResult resultGet = mockMvc.perform( requestGet )
                .andExpect( status().isFound() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                //.andExpect( content().encoding( StandardCharsets.UTF_8.name() ))
                .andReturn();

        // The JSON response
        String resultGetJson = resultGet.getResponse().getContentAsString();

        Assert.assertNotNull( resultGetJson );
        Assert.assertFalse( resultGetJson.isEmpty() );

        // The OBJECT response
        PriceDTO priceGetResult = this.objectMapper.readValue( resultGetJson, PriceDTO.class );

        Assert.assertNotNull( priceGetResult );

        Assert.assertEquals( idPriceCreated, priceGetResult.getId() );
        Assert.assertEquals( createPrice.getPriceHtId(), priceGetResult.getPriceHtId() );
        Assert.assertEquals( createPrice.getTaxId(), priceGetResult.getTaxId() );
        Assert.assertEquals( createPrice.getPriceInitialId(), priceGetResult.getPriceInitialId() );
        Assert.assertEquals( createPrice.getStoreId(), priceGetResult.getStoreId() );
        Assert.assertEquals( createPrice.getPrice(), priceGetResult.getPrice() );
        Assert.assertEquals( createPrice.getPriceType(), priceGetResult.getPriceType() );
        Assert.assertEquals( createPrice.getPriceHtId(), priceGetResult.getPriceHtId() );
    }

    @DisplayName("Create news categories from the REST API")
    @Test
    @Order(200)
    public void create_categories_by_api_rest() throws Exception {
        CreateCategory createCategory = new CreateCategory();
        createCategory.setRank( 1 );
        createCategory.setStoreId(STORE_TOTO_ID);
        createCategory.setParentId( null );
        createCategory.setChildrenId( null );
        createCategory.setVisible( true );

        createCategory.setTagsId( Arrays.asList( TAG_MUSIC_ID ));

        CreateCategoryTranslation createCategoryTranslation = new CreateCategoryTranslation();
        createCategoryTranslation.setLabel( "Sonorisation" );
        createCategoryTranslation.setLanguageId( LANGUAGE_ID );
        createCategory.setTranslations( Arrays.asList( createCategoryTranslation ) );

        String createJson = this.objectMapper.writeValueAsString( createCategory );

        // POST
        RequestBuilder requestPost = MockMvcRequestBuilders
                .post( PATH_POST_NEW_CATEGORIES )
                .accept( MediaType.APPLICATION_JSON )
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .content(createJson)
                .contentType( MediaType.APPLICATION_JSON );

        MvcResult result = mockMvc.perform( requestPost )
                .andExpect( status().isCreated() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                .andReturn();

        // The JSON response
        String resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson );
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        CategoryDTO categoryResult = this.objectMapper.readValue( resultJson, CategoryDTO.class );

        Assert.assertNotNull( categoryResult );
        Assert.assertNotNull( categoryResult.getId() );

        // ID
        Integer idCreated = categoryResult.getId();

        String newPathId = String.format( PATH_GET_CATEGORY , idCreated );
        // FIND
        RequestBuilder requestGet = MockMvcRequestBuilders
                .get( newPathId )
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .accept( MediaType.APPLICATION_JSON );

        MvcResult resultGet = mockMvc.perform( requestGet )
                .andExpect( status().isFound() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                .andReturn();

        // The JSON response
        String resultGetJson = resultGet.getResponse().getContentAsString();

        Assert.assertNotNull( resultGetJson );
        Assert.assertFalse( resultGetJson.isEmpty() );

        // The OBJECT response
        CategoryDTO categoryGetResult = this.objectMapper.readValue( resultGetJson, CategoryDTO.class );

        Assert.assertNotNull( categoryGetResult );
        Assert.assertEquals( idCreated ,categoryGetResult.getId() );
        Assert.assertEquals( createCategory.getVisible() ,categoryGetResult.getVisible() );
        Assert.assertEquals( createCategory.getRank() ,categoryGetResult.getRank() );
        Assert.assertEquals( createCategory.getStoreId() ,categoryGetResult.getStoreId() );
        Assert.assertEquals( createCategory.getParentId() ,categoryGetResult.getParentId() );

        Assert.assertNotNull( createCategory.getTranslations() );
        Assert.assertFalse( createCategory.getTranslations().isEmpty() );

        createCategory.getTranslations().forEach( translationItem -> {
            Assert.assertEquals( createCategoryTranslation.getLabel() ,translationItem.getLabel() );
            Assert.assertEquals( createCategoryTranslation.getLanguageId() ,translationItem.getLanguageId() );
        });

        // TAGS
        List<Integer> tagDtoIdList = new ArrayList<>();
        categoryGetResult.getTags().forEach( tagDTO -> tagDtoIdList.add( tagDTO.getId() ) );

        createCategory.getTagsId().forEach( tagId -> {
            Assert.assertTrue( tagDtoIdList.contains( tagId )); }
        );

        CATEGORY_ROOT_ID = idCreated;
    }

    @DisplayName("Create news categories (child) from the REST API")
    @Test
    @Order(205)
    public void create_all_categories_by_api_rest() throws Exception {
        CreateCategory createCategory = new CreateCategory();
        createCategory.setRank( 1 );
        createCategory.setStoreId(STORE_TOTO_ID);
        createCategory.setParentId( CATEGORY_ROOT_ID );
        createCategory.setChildrenId( null );
        createCategory.setVisible( true );

        CreateCategoryTranslation createCategoryTranslation = new CreateCategoryTranslation();
        createCategoryTranslation.setLabel( "Hi-Fi" );
        createCategoryTranslation.setLanguageId( LANGUAGE_ID );
        createCategory.setTranslations( Arrays.asList( createCategoryTranslation ) );

        String createJson = this.objectMapper.writeValueAsString( createCategory );

        // POST
        RequestBuilder requestPost = MockMvcRequestBuilders
                .post( PATH_POST_NEW_CATEGORIES )
                .accept( MediaType.APPLICATION_JSON )
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .content(createJson)
                .contentType( MediaType.APPLICATION_JSON );

        MvcResult result = mockMvc.perform( requestPost )
                .andExpect( status().isCreated() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                .andReturn();

        // The JSON response
        String resultJson = result.getResponse().getContentAsString();

        Assert.assertNotNull( resultJson );
        Assert.assertFalse( resultJson.isEmpty() );

        // The OBJECT response
        CategoryDTO categoryResult = this.objectMapper.readValue( resultJson, CategoryDTO.class );

        Assert.assertNotNull( categoryResult );
        Assert.assertNotNull( categoryResult.getId() );

        // ID
        Integer idCreated = categoryResult.getId();

        String newPathId = String.format( PATH_GET_CATEGORY , idCreated );
        // FIND
        RequestBuilder requestGet = MockMvcRequestBuilders
                .get( newPathId )
                .characterEncoding( StandardCharsets.UTF_8.name() )
                .accept( MediaType.APPLICATION_JSON );

        MvcResult resultGet = mockMvc.perform( requestGet )
                .andExpect( status().isFound() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                .andReturn();

        // The JSON response
        String resultGetJson = resultGet.getResponse().getContentAsString();

        Assert.assertNotNull( resultGetJson );
        Assert.assertFalse( resultGetJson.isEmpty() );

        // The OBJECT response
        CategoryDTO categoryGetResult = this.objectMapper.readValue( resultGetJson, CategoryDTO.class );

        Assert.assertNotNull( categoryGetResult );
        Assert.assertEquals( idCreated ,categoryGetResult.getId() );

        Assert.assertNotNull( categoryGetResult );
        Assert.assertEquals( idCreated ,categoryGetResult.getId() );
        Assert.assertEquals( createCategory.getVisible() ,categoryGetResult.getVisible() );
        Assert.assertEquals( createCategory.getRank() ,categoryGetResult.getRank() );
        Assert.assertEquals( createCategory.getStoreId() ,categoryGetResult.getStoreId() );
        Assert.assertEquals( createCategory.getParentId() ,categoryGetResult.getParentId() );

        Assert.assertNotNull( createCategory.getTranslations() );
        Assert.assertFalse( createCategory.getTranslations().isEmpty() );

        createCategory.getTranslations().forEach( translationItem -> {
            Assert.assertEquals( createCategoryTranslation.getLabel() ,translationItem.getLabel() );
            Assert.assertEquals( createCategoryTranslation.getLanguageId() ,translationItem.getLanguageId() );
        });

    }

    @DisplayName("Create a new promoCode from the REST API")
    @Test
    @Order(220)
    public void create_promo_code_by_api_rest() throws Exception {

    }

    @DisplayName("Patch a promoCode from the REST API")
    @Test
    @Order(224)
    public void patch_promo_code_by_api_rest() throws Exception {

    }

    @DisplayName("Put a promoCode from the REST API")
    @Test
    @Order(225)
    public void put_promo_code_by_api_rest() throws Exception {

    }


    @DisplayName("Validate view REST API - ( Elasticsearch ) ")
    //@Test
    @Order(400)
    public void validate_view_rest_api() throws Exception {

    }


}
