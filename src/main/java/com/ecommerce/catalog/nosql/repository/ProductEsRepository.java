package com.ecommerce.catalog.nosql.repository;

import com.ecommerce.catalog.nosql.data.ProductEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductEsRepository extends ElasticsearchRepository<ProductEs, Integer> {

    Page<ProductEs> findByReference (String reference , Pageable pageable );

}
