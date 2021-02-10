package com.ecommerce.product;

import com.ecommerce.GeneratorTest;
import com.ecommerce.catalog.sql.entity.promotion.Promotion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PromotionTest {


    // --------------------------------------
    // -        Services                    -
    // --------------------------------------

    private GeneratorTest g;

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @Before
    public void setUp() {
        this.g = new GeneratorTest();
        MockitoAnnotations.initMocks(this);
    }

    /**
     *  Promotion valide
     *
     * Pomotion activée
     * Date actuelle dans le période de la promotion
     * Prix activé
     * Prix toujours à jour
     *
     */
    @Test
    public void test1_promotion_valid(){

        //  -   INIT

        Promotion promotion = this.g.getPromotionEnable();

        Date currentDate = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            currentDate = simpleDateFormat.parse("05/07/2020");
        }catch ( ParseException ex ){
            Assert.assertTrue( false );
        }

        //
        //  -    TEST
        //

        boolean promotionEnable = promotion.isEnablePromotion( currentDate );

        Assert.assertTrue( promotionEnable );
    }

    /**
     *
     * Promotion terminée
     *
     * La date actuelle n'est pas dans la période de la promotion
     *
     */
    @Test
    public void test1_promotion_fail_date(){

        //  -   INIT

        Promotion promotion = this.g.getPromotionEnable();

        Date currentDate = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            currentDate = simpleDateFormat.parse("12/07/2020");
        }catch ( ParseException ex ){
            Assert.assertTrue( false );
        }

        //
        //  -    TEST
        //

        boolean promotionEnable = promotion.isEnablePromotion( currentDate );

        Assert.assertFalse( promotionEnable );
    }


    /**
     * La promotion a été désactivée
     *
     */
    @Test
    public void test1_promotion_promotion_disable(){

        //  -   INIT

        Promotion promotion = this.g.getPromotionEnable();
        promotion.setEnable( false );

        Date currentDate = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            currentDate = simpleDateFormat.parse("06/07/2020");
        }catch ( ParseException ex ){
            Assert.assertTrue( false );
        }

        //
        //  -    TEST
        //

        boolean promotionEnable = promotion.isEnablePromotion( currentDate );

        Assert.assertFalse( promotionEnable );
    }

    /**
     * Le prix n'est plus le dernier
     *
     */
    @Test
    public void test1_promotion_price_disable(){

        //  -   INIT

        Promotion promotion = this.g.getPromotionEnable();

        //promotion.getPriceWithPromotion().setCurrentPrice( false );

        Date currentDate = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            currentDate = simpleDateFormat.parse("06/07/2020");
        }catch ( ParseException ex ){
            Assert.assertTrue( false );
        }

        //
        //  -    TEST
        //

        boolean promotionEnable = promotion.isEnablePromotion( currentDate );

        Assert.assertFalse( promotionEnable );
    }


    @Test
    public void test2_get_percentage(){

        //  -   INIT

//        Promotion promotion = this.g.getPromotionEnable();
//        promotion.getPriceWithPromotion().setCurrentPrice( true );

        //
        //  -    TEST
        //
//
//        Double percentage = promotion.getPercentage();
//        Assert.assertEquals( Double.valueOf( -15 ), percentage );

    }




}
