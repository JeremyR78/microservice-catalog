package com.ecommerce.product;

import com.ecommerce.catalog.sql.entity.property.Property;
import com.ecommerce.GeneratorTest;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PropertyTest {

    // --------------------------------------
    // -        Services                    -
    // --------------------------------------


    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test1_new_property()
    {
        //  -   INIT

        GeneratorTest g = new GeneratorTest();

        Property height = g.getPropertyHeight();
        Property width = g.getPropertyWidth();

        //
        //  -    TEST
        //

        //this.

        //Assert.assertEquals(  , property );

    }

}
