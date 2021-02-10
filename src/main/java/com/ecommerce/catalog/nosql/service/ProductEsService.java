package com.ecommerce.catalog.nosql.service;

import com.ecommerce.catalog.exceptions.IndexNotFoundException;
import com.ecommerce.catalog.nosql.data.ProductEs;
import com.ecommerce.catalog.nosql.mapper.MapperProductService;
import com.ecommerce.catalog.nosql.model.EsIndexChange;
import com.ecommerce.catalog.nosql.model.ReferenceElasticsearch;
import com.ecommerce.catalog.nosql.repository.ProductEsRepository;
import com.ecommerce.catalog.sql.entity.common.Language;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.entity.product.Product;
import com.ecommerce.catalog.sql.service.common.StoreService;
import com.ecommerce.catalog.sql.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ecommerce.catalog.utils.logs.LogNameSpace.*;


@Service
@RequiredArgsConstructor
public class ProductEsService {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------
    private transient final Logger logger = LoggerFactory.getLogger(getClass());

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    private final ProductEsRepository productEsRepository;
    private final MapperProductService mapperProductService;
    private final IndexService indexService;
    private final StoreService storeService;
    private final ProductService productService;

    public final static String TABLE_PRODUCT = "product";

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @PostConstruct
    public void initIndex(){
        List<Store> stores = this.storeService.findAll();
        List<String> indexProducts = this.indexService.generateAllIndex( ReferenceElasticsearch.ES_PREFIX_INDEX_PRODUCT, stores );
        this.indexService.getIndexes().addAll( indexProducts );
        indexProducts.forEach( this.elasticsearchRestTemplate::createIndex );
        this.logger.info("{}{} Create index : {}",
                fLog(ELASTICSEARCH), fLog(INDEX), indexProducts );
    }

    @Transactional
    public void createOrUpdateProduct( Integer productId ){
        Optional<Product> productOptional = this.productService.findWithOptional( productId );

        // FIND
        if (productOptional.isEmpty()) {
            this.logger.error("{}{}{} Product <{}> not found !",
                    fLog(COMMAND), fLog(PRODUCT), fLog(NOT_FOUND), productId );
            return;
        }

        this.createOrUpdateProduct( productOptional .get() );
    }


    /**
     *
     * @param product
     */
    public void createOrUpdateProduct( Product product ){
        if( product == null || product.getId() == null ){
            this.logger.warn("{}{} The product is NULL ",
                    fLog(ELASTICSEARCH), fLog(CREATE_OR_UPDATE));
            return;
        }
        this.logger.info("{}{} Create or update the product {} ",
                fLog(ELASTICSEARCH), fLog(CREATE_OR_UPDATE), product );
        Map<String, ProductEs> mapProduct = this.mappingProduct( product );
        mapProduct.forEach((key, value) -> {
            EsIndexChange.setIndexName( key );
            this.save( value );
            EsIndexChange.setIndexName( null );
        });
        this.logger.info("{}{}{} Create or update the product {} -> to index / productEs : {}",
                fLog(ELASTICSEARCH), fLog(CREATE_OR_UPDATE), fLog(FINISH), product, mapProduct );
    }

    /**
     *
     * @param productId
     */
    public void deleteProductById( Integer productId ){
        this.logger.info("{}{} Delete the product {} ",
                fLog(ELASTICSEARCH), fLog(DELETE), productId );

        this.indexService.getIndexes().forEach( index -> {
            EsIndexChange.setIndexName( index );
            this.deleteById( productId );
            EsIndexChange.setIndexName( null );
        });
    }

    public Optional<ProductEs> findById( Integer productId, Store store, Language language ){
        String index = this.indexService.findIndexFor( TABLE_PRODUCT , store, language ).orElseThrow( () -> new IndexNotFoundException( TABLE_PRODUCT ));
        EsIndexChange.setIndexName( index );
        Optional<ProductEs> productEs = this.findBy( productId );
        EsIndexChange.setIndexName( null );
        return productEs;
    }

    /**
     *
     * @param product
     * @return
     */
    public Map<String, ProductEs> mappingProduct( Product product ){
        return this.mapperProductService.mappingProduct( product, TABLE_PRODUCT );
    }

    public Optional<ProductEs> findBy( Integer id ){
        return productEsRepository.findById( id );
    }

    public ProductEs save( ProductEs productEs ){
        return this.productEsRepository.save( productEs );
    }

    public void delete( ProductEs productEs ){
        this.productEsRepository.delete( productEs );
    }

    public void deleteById( Integer id ){
        this.productEsRepository.deleteById( id );
    }

    public List<ProductEs> findAll(){
        return (List<ProductEs>) this.productEsRepository.findAll();
    }

    public List<ProductEs> findAll( Sort sort ){
        return (List<ProductEs>) this.productEsRepository.findAll( sort );
    }

    public Page<ProductEs> findAll( Pageable pageable ){
        return this.productEsRepository.findAll( pageable );
    }




}
