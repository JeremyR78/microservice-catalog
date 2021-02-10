package com.ecommerce.catalog.sql.service.promotion;

import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.dto.promotion.CreateOrUpdatePromoCode;
import com.ecommerce.catalog.dto.promotion.PromoCodeDTO;
import com.ecommerce.catalog.exceptions.*;
import com.ecommerce.catalog.service.AbstractService;
import com.ecommerce.catalog.sql.dao.promotion.PromoCodeDao;
import com.ecommerce.catalog.sql.entity.common.Country;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.entity.customer.CustomerGroup;
import com.ecommerce.catalog.sql.entity.price.Currency;
import com.ecommerce.catalog.sql.entity.promotion.PromoCode;
import com.ecommerce.catalog.sql.service.common.CountryService;
import com.ecommerce.catalog.sql.service.common.StoreService;
import com.ecommerce.catalog.sql.service.customer.CustomerGroupService;
import com.ecommerce.catalog.sql.service.price.CurrencyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromoCodeService extends AbstractService<PromoCode, Integer> {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private final PromoCodeDao promoCodeDao;
    private final ConvertDTO<PromoCode, PromoCodeDTO> dtoService;
    private final ConvertDTO<PromoCode, CreateOrUpdatePromoCode> dtoServiceCreate;

    private final StoreService storeService;
    private final CountryService countryService;
    private final CustomerGroupService customerGroupService;
    private final CurrencyService currencyService;

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------


    @Override
    protected JpaRepository<PromoCode, Integer> getDao() {
        return this.promoCodeDao;
    }

    public List<PromoCodeDTO> toDto(List<PromoCode> tags ) {
        return this.dtoService.toDTO(tags, PromoCodeDTO.class);
    }

    public PromoCodeDTO toDto( PromoCode promoCode ) {
        return this.dtoService.toDTO( promoCode, PromoCodeDTO.class );
    }


    public PromoCode toEntity( PromoCodeDTO promoCodeDTO ){
        return this.dtoService.toEntity( promoCodeDTO, PromoCode.class );
    }

    public PromoCode toEntity( CreateOrUpdatePromoCode createPromoCode ){
        PromoCode promoCode = this.dtoServiceCreate.toEntity( createPromoCode, PromoCode.class );
        return promoCode;
    }


    public PromoCode update( Integer promoCodeId, CreateOrUpdatePromoCode createPromoCode ){
        PromoCode promoCode = this.findWithOptional( promoCodeId ).orElseThrow( () -> new PromoCodeNotFoundException( promoCodeId ) );
        return this.update( promoCode, createPromoCode );
    }

    public PromoCode update( PromoCode promoCode, CreateOrUpdatePromoCode createPromoCode ){
        promoCode.setCode( createPromoCode.getCode() );
        promoCode.setEnable( createPromoCode.getEnable() );
        promoCode.setDiscount( createPromoCode.getDiscount() );
        promoCode.setStartAt( createPromoCode.getStartAt() );
        promoCode.setFinishAt( createPromoCode.getFinishAt() );

        // STORE
        Integer storeId = createPromoCode.getStoreId();
        Store store = this.storeService.findWithOptional( storeId ).orElseThrow( () -> new StoreNotFoundException( storeId, promoCode.toString() ));
        promoCode.setStore( store );

        // COUNTRY
        Integer countryId = createPromoCode.getCountryId();
        Country country = this.countryService.findWithOptional( countryId ).orElseThrow( () -> new CountryNotFoundException( countryId, promoCode.toString() ));
        promoCode.setCountry( country );

        // CUSTOMER GROUP
        Integer customerGroupId = createPromoCode.getCustomerGroupId();
        CustomerGroup customerGroup = this.customerGroupService.findWithOptional( customerGroupId ).orElseThrow( () -> new CustomerGroupNotFoundException( customerGroupId, promoCode.toString() ));
        promoCode.setCustomerGroup( customerGroup );

        // CURRENCY
        Integer currencyId = createPromoCode.getCurrencyId();
        Currency currency = this.currencyService.findWithOptional( currencyId ).orElseThrow( () -> new CurrencyNotFoundException( currencyId, promoCode.toString() ));
        promoCode.setCurrency( currency );

        return promoCode;
    }

    public PromoCodeDTO applyPatchTo( JsonPatch patch, PromoCodeDTO target ) throws JsonPatchException, JsonProcessingException {
        return this.dtoService.applyPatchTo( patch, target, PromoCodeDTO.class );
    }

}
