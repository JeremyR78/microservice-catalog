package com.ecommerce.controller;

import com.ecommerce.catalog.configurations.JacksonConfiguration;
import com.ecommerce.catalog.dto.promotion.CreateOrUpdatePromotion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;

import java.util.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Tests JSON Parser")
@Profile("test-unit")
public class ParserJsonTests {

    // --------------------------------------
    // -        Services                    -
    // --------------------------------------

    private final ObjectMapper OBJECT_MAPPER = new JacksonConfiguration().objectMapper();

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @DisplayName("Tests the parser JSON with Optional value")
    @Test
    public void test_serialization_deserialization_optional() throws JsonProcessingException {

        Assert.assertNotNull("Le mapper n'est pas instancié !" ,this.OBJECT_MAPPER);

        // PROMO
        CreateOrUpdatePromotion createOrUpdatePromotion = new CreateOrUpdatePromotion();
        createOrUpdatePromotion.setStoreId(  1  );
        createOrUpdatePromotion.setEnable(  true  );
        createOrUpdatePromotion.setStartAt(  new Date()  );
        createOrUpdatePromotion.setFinishAt(  new Date()  );
        createOrUpdatePromotion.setCustomerGroupId(  2 );
        createOrUpdatePromotion.setTagsId(  Arrays.asList( 5 ) );

        String serialized = this.OBJECT_MAPPER.writeValueAsString(createOrUpdatePromotion);
        Set<Object> registeredModuleIds = this.OBJECT_MAPPER.getRegisteredModuleIds();

        Assert.assertNotNull( "Aucun module de présent pour lire les Optionals ?",registeredModuleIds );
        Assert.assertFalse( "Aucun module de présent pour lire les Optionals ?", registeredModuleIds.isEmpty() );

        Assert.assertNotNull( serialized );
        Assert.assertFalse( serialized.isEmpty() );

        CreateOrUpdatePromotion objectResult = this.OBJECT_MAPPER.readValue( serialized, CreateOrUpdatePromotion.class );

        Assert.assertNotNull( objectResult );

        Assert.assertEquals( createOrUpdatePromotion.getStoreId(), objectResult.getStoreId());
        Assert.assertEquals( createOrUpdatePromotion.getEnable(), objectResult.getEnable());
        Assert.assertEquals( createOrUpdatePromotion.getTagsId(), objectResult.getTagsId());
        Assert.assertEquals( createOrUpdatePromotion.getPricePromotion(), objectResult.getPricePromotion());
        Assert.assertEquals( createOrUpdatePromotion.getStartAt(), objectResult.getStartAt());
        Assert.assertEquals( createOrUpdatePromotion.getFinishAt(), objectResult.getFinishAt());
    }

}
