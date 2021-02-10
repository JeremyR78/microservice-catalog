package com.ecommerce.product;

import com.ecommerce.GeneratorTest;

import com.ecommerce.catalog.sql.entity.category.Category;
import com.ecommerce.catalog.sql.entity.common.Language;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.service.category.CategoryService;
import com.ecommerce.catalog.sql.service.common.LanguageService;
import com.ecommerce.catalog.sql.service.common.StoreService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
@Rollback
public class CategoryTest {

    // --------------------------------------
    // -        Services                    -
    // --------------------------------------

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private StoreService storeService;

    private Language currentLanguage;
    private Store store1;
    private Store store2;
    private Category categoryParent;
    private Category categoryChild1;
    private Category categoryChild2;

    private GeneratorTest g;

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @Before
    public void setUp() {
        g = new GeneratorTest();

        this.store1 = this.storeService.save( g.getStoreToto() );
        this.store2 = this.storeService.save( g.getStoreTutu() );
        this.currentLanguage = this.languageService.save( g.getLanguageFrench() );

        this.categoryParent = this.categoryService.save( g.getCategoryParent() );
        this.categoryChild1 = this.categoryService.save( g.getCategoryChild1() );
        this.categoryChild2 = this.categoryService.save( g.getCategoryChild2() );
    }


    @Test
    public void test1_create_find_category()
    {
        //
        //  -    TEST
        //

//        List<Category> categories = this.categoryService.findAll();
//
//        Assert.assertNotNull( categories );
//        Assert.assertFalse( categories.isEmpty() );
//
//        Category categoryResult = categories.get(0);
//
//        Assert.assertTrue( categoryResult.getVisible() );
//        Assert.assertEquals( categoryParent.getRank(), categoryResult.getRank() );
    }

    @Test
    public void test2_find_by_store()
    {
        List<Category> categories = this.categoryService.findCategories( store2 );

        Assert.assertNotNull( categories );
        Assert.assertFalse( categories.isEmpty() );

        Assert.assertEquals( 1 , categories.size() );

        Assert.assertEquals( this.categoryChild2.getId(), categories.get(0).getId());
    }

    @Test
    public void test2_find_root()
    {
        Category category = this.categoryService.getRootCategory( store1 );

        Assert.assertNotNull( category );

        Assert.assertEquals( this.categoryParent.getId(), category.getId());
    }

}
