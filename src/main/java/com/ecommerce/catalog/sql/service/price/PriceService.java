package com.ecommerce.catalog.sql.service.price;

import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.dto.price.CreatePriceDTO;
import com.ecommerce.catalog.dto.price.PriceDTO;
import com.ecommerce.catalog.exceptions.*;
import com.ecommerce.catalog.model.PriceType;
import com.ecommerce.catalog.service.AbstractService;
import com.ecommerce.catalog.sql.dao.price.PriceDao;
import com.ecommerce.catalog.sql.dao.price.PricePromoDao;
import com.ecommerce.catalog.sql.dao.price.PriceTtcDao;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.entity.customer.CustomerGroup;
import com.ecommerce.catalog.sql.entity.price.*;
import com.ecommerce.catalog.sql.entity.product.Product;
import com.ecommerce.catalog.sql.entity.promotion.Promotion;
import com.ecommerce.catalog.sql.entity.tax.Tax;
import com.ecommerce.catalog.sql.service.common.StoreService;
import com.ecommerce.catalog.sql.service.customer.CustomerGroupService;
import com.ecommerce.catalog.sql.service.tax.TaxService;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceService extends AbstractService<Price, Integer> {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private final PriceDao priceDao;
    private final PricePromoDao pricePromoDao;
    private final PriceTtcDao priceTtcDao;
    private final ConvertDTO<Price, PriceDTO> dtoService;

    private final StoreService storeService;
    private final CustomerGroupService customerGroupService;
    private final CurrencyService currencyService;
    private final TaxService taxService;

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------

    @Override
    protected JpaRepository<Price, Integer> getDao() {
        return this.priceDao;
    }


    public List<Price> getPrices( List<Integer> pricesId ){
        QPrice price = QPrice.price1;
        BooleanExpression isInList = price.id.in( pricesId );
        return new ArrayList<>((Collection<? extends Price>) this.priceDao.findAll( isInList ));
    }

    public Price getOnePrice( Integer id ){
        Price price;
        Optional<PriceTTC> optionalPriceTTC;
        Optional<PricePromo> optionalPricePromo = this.pricePromoDao.findById( id );
        if( ! optionalPricePromo.isPresent() ) {
            optionalPriceTTC = this.priceTtcDao.findById( id );
            if( ! optionalPriceTTC.isPresent()  ) {
                price = this.priceDao.getOne( id );
            }
            else {
                price = optionalPriceTTC.get();
            }
        } else {
            price = optionalPricePromo.get();
        }

        return price;
    }

    public Price toEntity( CreatePriceDTO newPrice, Product product, Promotion promotion ){
        // PRICE
        Price price;
        if ( newPrice.getPriceType() == PriceType.TTC ){
            PriceTTC priceTTC = new PriceTTC();

            Integer idReference = newPrice.getPriceHtId();
            if( idReference == null ) throw new PriceNotFoundException( idReference );
            Price priceHt = this.find( idReference );
            if( priceHt == null ) throw new PriceNotFoundException( idReference );
            priceTTC.setPriceHt( priceHt );

            Integer taxId = newPrice.getTaxId();
            if( taxId == null ) throw new TaxNotFoundException( taxId );
            Tax tax = this.taxService.find( taxId );
            if( tax == null ) throw new TaxNotFoundException( taxId );
            priceTTC.setTax( tax );

            price = priceTTC;
        }
        else if ( newPrice.getPriceType() == PriceType.PROMO ){
            PricePromo pricePromo = new PricePromo();

            // PRICE - INITIAL - ( BEFORE PROMO )
            Integer priceIdReference = newPrice.getPriceInitialId();
            if( priceIdReference == null ) throw  new PriceNotFoundException( priceIdReference );
            Price priceRef = this.find( priceIdReference );
            if( priceRef == null ) throw  new PriceNotFoundException( priceIdReference );
            pricePromo.setPriceInitial( priceRef );

            pricePromo.setPromotion( promotion );

            price = pricePromo;
        }
        else
        {
            price = new Price();
        }

        price.setPrice( newPrice.getPrice() );
        price.setProduct( product );

        // STORE
        Integer storeId = newPrice.getStoreId();
        if( storeId == null ) throw new StoreNotFoundException( storeId );
        Store store = this.storeService.find( storeId );
        if ( store == null ) throw new StoreNotFoundException( storeId );
        price.setStore(store);

        // CUSTOMER GROUP
        Integer customerGroupId = newPrice.getCustomerGroupId();
        if ( customerGroupId == null ) throw new CustomerGroupNotFoundException( customerGroupId, newPrice.toString() );
        CustomerGroup customerGroup = this.customerGroupService.find(customerGroupId);
        if ( customerGroup == null ) throw new CustomerGroupNotFoundException( customerGroupId, newPrice.toString() );
        price.setCustomerGroup(customerGroup);

        // CURRENCY
        Integer currencyId = newPrice.getCurrencyId();
        if( currencyId == null ) throw new CurrencyNotFoundException( currencyId );
        Currency currency = this.currencyService.find( currencyId );
        if( currency == null ) throw new CurrencyNotFoundException( currencyId );
        price.setCurrency( currency );
        price.setCurrentPrice( true );
        price.setUserAction( null );
        return price;
    }

    public List<PriceDTO> toDTO( List<Price> prices ){
        List<PriceDTO> priceDTOList = new ArrayList<>();
        prices.forEach( price -> {
            priceDTOList.add( this.toDTO( price ));
        });
        return priceDTOList;
    }

    public PriceDTO toDTO( Price price ) {
        PriceDTO priceDTO = this.dtoService.toDTO( price, PriceDTO.class );

        if( price instanceof PricePromo ){
            PricePromo pricePromo = (PricePromo) price;
            priceDTO.setPromotionId( pricePromo.getPromotion() != null ? pricePromo.getPromotion().getId() : null );
            priceDTO.setPriceInitialId( pricePromo.getPriceInitial() != null ? pricePromo.getPriceInitial().getId() : null );
        }
        else if ( price instanceof PriceTTC ){
            PriceTTC priceTTC = (PriceTTC) price;
            priceDTO.setPriceHtId( priceTTC.getPriceHt() != null ? priceTTC.getPriceHt().getId() : null );
            priceDTO.setTaxId( priceTTC.getTax() != null ? priceTTC.getTax().getId() : null );
        }
        return priceDTO;
    }

}
