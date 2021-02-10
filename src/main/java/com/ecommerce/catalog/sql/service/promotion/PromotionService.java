package com.ecommerce.catalog.sql.service.promotion;

import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.dto.price.CreatePriceDTO;
import com.ecommerce.catalog.dto.promotion.CreateOrUpdatePromotion;
import com.ecommerce.catalog.dto.promotion.PromotionDTO;
import com.ecommerce.catalog.dto.tag.TagDTO;
import com.ecommerce.catalog.exceptions.*;
import com.ecommerce.catalog.service.AbstractService;
import com.ecommerce.catalog.sql.dao.promotion.PromotionDao;
import com.ecommerce.catalog.sql.entity.common.Country;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.entity.customer.CustomerGroup;
import com.ecommerce.catalog.sql.entity.price.Price;
import com.ecommerce.catalog.sql.entity.price.PricePromo;
import com.ecommerce.catalog.sql.entity.product.Product;
import com.ecommerce.catalog.sql.entity.promotion.Promotion;
import com.ecommerce.catalog.sql.entity.tag.Tag;
import com.ecommerce.catalog.sql.service.common.CountryService;
import com.ecommerce.catalog.sql.service.common.StoreService;
import com.ecommerce.catalog.sql.service.customer.CustomerGroupService;
import com.ecommerce.catalog.sql.service.price.PriceService;
import com.ecommerce.catalog.sql.service.product.ProductService;
import com.ecommerce.catalog.sql.service.tag.TagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService extends AbstractService<Promotion, Integer> {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private final PromotionDao promotionDao;
    private final ConvertDTO<Promotion, PromotionDTO> dtoService;
    private final ConvertDTO<Promotion, CreateOrUpdatePromotion> dtoCreatePropertyService;

    @Getter @Setter
    private ProductService productService;
    private final PriceService priceService;
    private final TagService tagService;
    private final StoreService storeService;
    private final CountryService countryService;
    private final CustomerGroupService customerGroupService;

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------

    @PostConstruct
    protected void initConfigDTO() {
        // CONFIG MAPPER DTO
        this.dtoCreatePropertyService.getModelMapper().getConfiguration().setAmbiguityIgnored(true);
    }


    @Override
    protected JpaRepository<Promotion, Integer> getDao() {
        return this.promotionDao;
    }


    /**
     * Recupére la liste des promotions ACTIVE pour un produit
     *
     * @param date
     * @return
     */
    public List<Promotion> getPromotionByProduct( Date date ){
        return this.promotionDao.getPromotionsByProduct( date );
    }


    public Promotion toEntity( PromotionDTO promotionDTO ) {
        Promotion promotion = this.dtoService.toEntity( promotionDTO, Promotion.class );

        // PRICES
        List<PricePromo> pricePromoList = new ArrayList<>();
        List<Integer> pricesPromotionsList = promotionDTO.getPricePromotionsId();
        if( pricesPromotionsList != null ) {
            pricesPromotionsList.forEach( priceId -> {
                // FIND
                Price price = this.priceService.findWithOptional( priceId ).orElseThrow( () -> new PricePromoNotCreatedException(priceId) );
                // ADD
                pricePromoList.add((PricePromo) price);
            });
        }
        promotion.setPricePromotions( pricePromoList );

        // TAGS
        List<Tag> tags = new ArrayList<>();
        List<TagDTO> tagDTOList = promotionDTO.getTags();
        if( tagDTOList != null ) {
            List<Integer> tagIdList = new ArrayList<>();
            tagDTOList.forEach( tagDTO -> tagIdList.add( tagDTO.getId() ) );
                tagIdList.forEach(
                        tagId -> {
                            if (tagId == null) throw new TagNotFoundException(tagId);
                            Tag tag = this.tagService.findWithOptional(tagId).orElseThrow(() -> new TagNotFoundException(tagId));
                            tags.add(tag);
                        });
        }
        promotion.setTags( tags );


        return promotion;
    }

    public List<Promotion> toEntity( List<CreateOrUpdatePromotion> createOrUpdatePromotionList) {
        return this.dtoCreatePropertyService.toEntity(createOrUpdatePromotionList, Promotion.class );
    }

    public Promotion toEntity( CreateOrUpdatePromotion createdPromotion ) {
        Promotion promotion = new Promotion();
        return this.update( promotion, createdPromotion );
    }

    public Promotion update(Integer promotionId, CreateOrUpdatePromotion updatePromotion ){
        if( promotionId == null ) throw new PromotionNotFoundException( promotionId );
        Promotion promotion = this.findWithOptional( promotionId ).orElseThrow( () -> new PromotionNotFoundException( promotionId ));
        return this.update( promotion, updatePromotion );
    }

    public Promotion update(Promotion promotion, CreateOrUpdatePromotion createdPromotion ){

        // STORE
        Integer storeId = createdPromotion.getStoreId();
        if( storeId == null ) throw new StoreNotFoundException( storeId );
        Store store = this.storeService.findWithOptional( storeId ).orElseThrow( ()-> new StoreNotFoundException( storeId )) ;
        promotion.setStore( store );

        // COUNTRY
        Integer countryId = createdPromotion.getCountryId();
        if( countryId == null ) throw new CountryNotFoundException( countryId );
        Country country = this.countryService.findWithOptional( countryId ).orElseThrow( ()-> new CountryNotFoundException( countryId ) );
        promotion.setCountry( country );

        // ENABLE
        promotion.setEnable(createdPromotion.getEnable());

        // CUSTOMER GROUP
        Integer customerGroupId = createdPromotion.getCustomerGroupId();
        if( customerGroupId == null ) throw new CustomerGroupNotFoundException( customerGroupId );
        CustomerGroup customerGroup = this.customerGroupService.findWithOptional( customerGroupId ).orElseThrow( () -> new CustomerGroupNotFoundException( customerGroupId ) );
        promotion.setCustomerGroup( customerGroup );


        // START DATE
        promotion.setStartAt( createdPromotion.getStartAt() );

        // FINISH DATE
        promotion.setFinishAt( createdPromotion.getFinishAt() );

        // USER ACTION
        // TODO

        // PRICES
        List<PricePromo> pricePromoList = new ArrayList<>();
        List<CreatePriceDTO> pricesPromotionsList = createdPromotion.getPricePromotion();
        if( pricesPromotionsList != null ) {
            pricesPromotionsList.forEach(pricePromoDto -> {
                // FIND PRODUCT
                Integer productId = pricePromoDto.getProductId();
                if (productId == null) throw new ProductNotFoundException(productId);
                Product product = this.productService.findWithOptional(productId).orElseThrow(() -> new ProductNotFoundException(productId));
                // CREATE PRICE PROMO
                Price price = this.priceService.toEntity(pricePromoDto, product, promotion);
                if (!(price instanceof PricePromo)) throw new PricePromoNotCreatedException(product);
                // ADD
                pricePromoList.add((PricePromo) price);
            });
        }
        promotion.setPricePromotions( pricePromoList );


        // TAGS
        List<Tag> tags = new ArrayList<>();
        List<Integer> tagIdList = createdPromotion.getTagsId();
        if( tagIdList != null ) {
            tagIdList.forEach(
                    tagId -> {
                        if (tagId == null) throw new TagNotFoundException(tagId);
                        Tag tag = this.tagService.findWithOptional(tagId).orElseThrow(() -> new TagNotFoundException(tagId));
                        tags.add(tag);
                    });
        }
        promotion.setTags( tags );

        return promotion;
    }

    public List<PromotionDTO> toDTO(List<Promotion> promotions ){
        List<PromotionDTO> promotionDTOList = new ArrayList<>();
        if( promotions == null ){
            return promotionDTOList;
        }
        promotions.forEach( promotion -> {
            promotionDTOList.add( this.toDTO( promotion ));
        });
        return promotionDTOList;
    }

    public PromotionDTO toDTO( Promotion promotion ) {
        PromotionDTO promotionDTO = this.dtoService.toDTO( promotion, PromotionDTO.class );
        // PRICES
        List<Integer> priceIdList = new ArrayList<>();
        if ( promotion.getPricePromotions() != null ) {
            promotion.getPricePromotions().forEach(price -> priceIdList.add(price.getId()));
        }
        promotionDTO.setPricePromotionsId( priceIdList );

        // TAGS
        List<TagDTO> tagDTOList = new ArrayList<>();
        if( promotion.getTags() != null ) {
            promotion.getTags().forEach(tag -> tagDTOList.add(this.tagService.toDto(tag)));
        }
        promotionDTO.setTags( tagDTOList );

        return promotionDTO;
    }

    public PromotionDTO applyPatchTo( JsonPatch patch, PromotionDTO target ) throws JsonPatchException, JsonProcessingException {
        return this.dtoService.applyPatchTo( patch, target, PromotionDTO.class );
    }





}
