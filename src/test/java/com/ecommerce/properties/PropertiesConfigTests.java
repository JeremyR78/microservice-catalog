package com.ecommerce.properties;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

//@TestPropertySource(locations="classpath:application-test.properties")
//@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PropertiesConfigTests {

    //@Autowired
    private PropertiesFiles propertiesTests;

    //@Test
    public void test_1_properties(){

        Assert.assertEquals( "root", this.propertiesTests.getUsername());
    }

}
