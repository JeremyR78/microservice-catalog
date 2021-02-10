package com.ecommerce.catalog;

import com.ecommerce.GeneratorTest;
import com.ecommerce.catalog.nosql.data.ProductEs;
import com.ecommerce.catalog.nosql.mapper.MapperProductService;
import com.ecommerce.catalog.nosql.service.IndexService;
import com.ecommerce.catalog.sql.entity.common.Language;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.entity.product.Product;
import com.ecommerce.catalog.sql.service.category.CategoryService;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Tests mapper service")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MapperProductServiceTests {

    @InjectMocks
    private MapperProductService mapperProductService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private IndexService indexService;

    private GeneratorTest generatorTest;

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @BeforeEach
    public void init(){
        this.generatorTest = new GeneratorTest();
    }

    @DisplayName("Mapping product")
    @Test
    @Order(10)
    public void mapping_product(){

        String tableWithIndex = "product_1_1";

        // MOCK
        when( this.categoryService.findCategoryForProduct( any(Product.class), any(Store.class) )).thenReturn( new HashMap<>() );
        when( this.indexService.findIndexFor( any(String.class), any(Store.class), any(Language.class) ) ).thenReturn( Optional.of( tableWithIndex ) );

        // TEST

        String table = "product";
        Product product = this.generatorTest.getProductTest1();

        Assert.assertNotNull( product );
        Assert.assertNotNull( this.mapperProductService );

        Map<String, ProductEs> productEsMap = this.mapperProductService.mappingProduct( product, table );

        Assert.assertNotNull( productEsMap );
        Assert.assertFalse( productEsMap.isEmpty() );

        productEsMap.forEach( (key,value) -> {
            Assert.assertNotNull( key );
            Assert.assertNotNull( value );

            Assert.assertEquals( tableWithIndex, key );

        });

    }

}
