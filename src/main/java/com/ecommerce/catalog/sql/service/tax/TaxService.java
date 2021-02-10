package com.ecommerce.catalog.sql.service.tax;

import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.dto.tax.CreateOrUpdateTax;
import com.ecommerce.catalog.dto.tax.TaxDTO;
import com.ecommerce.catalog.exceptions.CountryNotFoundException;
import com.ecommerce.catalog.exceptions.TaxNotFoundException;
import com.ecommerce.catalog.service.AbstractService;
import com.ecommerce.catalog.sql.dao.tax.TaxDao;
import com.ecommerce.catalog.sql.entity.common.Country;

import com.ecommerce.catalog.sql.entity.tax.Tax;
import com.ecommerce.catalog.sql.service.common.CountryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxService extends AbstractService<Tax, Integer>  {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private final TaxDao taxDao;
    private final ConvertDTO<Tax, TaxDTO> dtoService;
    private final CountryService countryService;
    private final ObjectMapper objectMapper;


    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------

    @Override
    protected JpaRepository<Tax, Integer> getDao() {
        return this.taxDao;
    }

    //
    //  -   CONVERT
    //

    public List<TaxDTO> toDTO( List<Tax> taxes ){
        return this.dtoService.toDTO( taxes, TaxDTO.class );
    }

    public TaxDTO toDTO( Tax tax ) {
        return this.dtoService.toDTO( tax, TaxDTO.class );
    }

    public Tax toEntity( TaxDTO taxDTO ){
        return this.dtoService.toEntity( taxDTO, Tax.class );
    }

    public List<Tax> toEntity(List<CreateOrUpdateTax> createOrUpdateTaxes) {
        List<Tax> taxes = new ArrayList<>();
         createOrUpdateTaxes.forEach(
                 createOrUpdateTax -> {
                    taxes.add( this.toEntity(createOrUpdateTax) );
                }
        );
        return taxes;
    }

    public Tax toEntity( CreateOrUpdateTax createOrUpdateTax) {
        Tax tax = new Tax();
        return this.update( tax, createOrUpdateTax);
    }


    public Tax update( Integer taxIdToUpdate , CreateOrUpdateTax updateTax ){
        Tax taxFound = this.findWithOptional( taxIdToUpdate ).orElseThrow( () -> new TaxNotFoundException( taxIdToUpdate ) );
        return this.update( taxFound, updateTax );
    }

    public Tax update( Tax tax, CreateOrUpdateTax createOrUpdateTax){

        // COUNTRY
        Integer countryId = createOrUpdateTax.getCountryId();
        if( countryId != null ) {
            Country country = this.countryService.findWithOptional(countryId).orElseThrow(() -> new CountryNotFoundException(countryId));
            tax.setCountry(country);
        }

        // TAX TYPE
        tax.setTaxType( createOrUpdateTax.getTaxType() );

        // PERCENT
        tax.setPercent( createOrUpdateTax.getPercent() );

        return tax;
    }

    public TaxDTO applyPatchTo( JsonPatch patch, TaxDTO target ) throws JsonPatchException, JsonProcessingException {
        return this.dtoService.applyPatchTo( patch, target, TaxDTO.class );
    }

}
