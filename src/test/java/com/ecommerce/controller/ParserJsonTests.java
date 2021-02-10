package com.ecommerce.controller;

import com.ecommerce.catalog.dto.promotion.CreateOrUpdatePromotion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
;


import java.util.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Tests JSON Parser")
public class ParserJsonTests {

    // --------------------------------------
    // -        Services                    -
    // --------------------------------------

    @Autowired
    private ObjectMapper objectMapper;

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @DisplayName("Tests the parser JSON with Otional value")
    @Test
    public void test_serialization_deserialization_optional() throws JsonProcessingException {

        Assert.assertNotNull("Le mapper n'est pas instancié !" ,this.objectMapper );

        // PROMO
        CreateOrUpdatePromotion createOrUpdatePromotion = new CreateOrUpdatePromotion();
        createOrUpdatePromotion.setStoreId(  1  );
        createOrUpdatePromotion.setEnable(  true  );
        createOrUpdatePromotion.setStartAt(  new Date()  );
        createOrUpdatePromotion.setFinishAt(  new Date()  );
        createOrUpdatePromotion.setCustomerGroupId(  2 );
        createOrUpdatePromotion.setTagsId(  Arrays.asList( 5 ) );

        String serialized = this.objectMapper.writeValueAsString(createOrUpdatePromotion);
        Set<Object> registeredModuleIds = this.objectMapper.getRegisteredModuleIds();

        Assert.assertNotNull( "Aucun module de présent pour lire les Optionals ?",registeredModuleIds );
        Assert.assertFalse( "Aucun module de présent pour lire les Optionals ?", registeredModuleIds.isEmpty() );

        Assert.assertNotNull( serialized );
        Assert.assertFalse( serialized.isEmpty() );

        CreateOrUpdatePromotion objectResult = this.objectMapper.readValue( serialized, CreateOrUpdatePromotion.class );

        Assert.assertNotNull( objectResult );

        Assert.assertEquals( createOrUpdatePromotion.getStoreId(), objectResult.getStoreId());
        Assert.assertEquals( createOrUpdatePromotion.getEnable(), objectResult.getEnable());
        Assert.assertEquals( createOrUpdatePromotion.getTagsId(), objectResult.getTagsId());
        Assert.assertEquals( createOrUpdatePromotion.getPricePromotion(), objectResult.getPricePromotion());
        Assert.assertEquals( createOrUpdatePromotion.getStartAt(), objectResult.getStartAt());
        Assert.assertEquals( createOrUpdatePromotion.getFinishAt(), objectResult.getFinishAt());


    }

}
