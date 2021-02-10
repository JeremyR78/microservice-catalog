package com.ecommerce.product;

import com.ecommerce.GeneratorTest;
import com.ecommerce.catalog.model.PriceType;
import com.ecommerce.catalog.sql.dao.category.CategoryDao;
import com.ecommerce.catalog.sql.entity.price.Currency;
import com.ecommerce.catalog.sql.entity.price.Price;
import com.ecommerce.catalog.sql.entity.product.Product;
import com.ecommerce.catalog.sql.service.product.ProductService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductTest {

    // --------------------------------------
    // -        Services                    -
    // --------------------------------------

    @InjectMocks
    public ProductService productService;

    @Mock
    private CategoryDao categoryDao;

    private GeneratorTest g;

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @Before
    public void setUp() {
        this.g = new GeneratorTest();
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void test1_Price()
    {
        //  -   INIT

        Currency currency = this.g.getCurrencyEuro();
        Product productPrice = this.g.getProductTest1();

        //
        //  -    TEST
        //

        Price price = productPrice.getPriceForCurrency( currency, PriceType.TTC );

        Assert.assertNotNull( price );
        Assert.assertEquals( Integer.valueOf(3), price.getId() );

        List<Price> prices = productPrice.getAllPriceForCurrency( currency, PriceType.TTC );

        Assert.assertNotNull( price );
        Assert.assertEquals( 3, prices.size() );

    }



}
