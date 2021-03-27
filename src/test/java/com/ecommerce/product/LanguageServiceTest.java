package com.ecommerce.product;

import com.ecommerce.GeneratorTest;
import com.ecommerce.catalog.sql.entity.common.Language;
import com.ecommerce.catalog.sql.entity.common.LanguageTranslation;
import com.ecommerce.catalog.sql.service.common.LanguageService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@RunWith(SpringRunner.class)
//@Transactional
//@SpringBootTest
public class LanguageServiceTest {

    // --------------------------------------
    // -        Services                    -
    // --------------------------------------

    @Autowired
    private LanguageService languageService;


    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    //@Before
    public void setUp() {

    }


    //@Test
    public void test1_create_and_find() {

        //  -   INIT

        GeneratorTest g = new GeneratorTest();
        Language frenchLanguage = g.getLanguageFrench();
        LanguageTranslation frenchTranslation = frenchLanguage.getLanguageTranslationList().get(0);
        this.languageService.save( frenchLanguage );

        //
        //  -    TEST
        //

        List<Language> languages = this.languageService.findAll();
        List<LanguageTranslation> languageTranslationList = this.languageService.findAllLanguageTranslation();

        Assert.assertNotNull( languages );
        Assert.assertNotNull( languageTranslationList );

        Assert.assertFalse( languages.isEmpty() );
        Assert.assertFalse( languageTranslationList.isEmpty() );

        Language language = languages.get(0);
        Assert.assertEquals( frenchLanguage.getIsoCode(), language.getIsoCode() );

        LanguageTranslation languageTranslation = language.getLanguageTranslationList().get(0);
        Assert.assertEquals( frenchTranslation.getLabel(), languageTranslation.getLabel() );
    }

}
