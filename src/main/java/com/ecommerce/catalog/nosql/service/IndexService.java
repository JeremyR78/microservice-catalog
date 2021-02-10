package com.ecommerce.catalog.nosql.service;

import com.ecommerce.catalog.sql.entity.common.Language;
import com.ecommerce.catalog.sql.entity.common.Store;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IndexService {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    public static final String INDEX_TEMPLATE = "%s_%s-%s_%s-%s";

    @Getter
    private final List<String> indexes = new ArrayList<>();

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    public Optional<String> findIndexFor( String nameTable, Store store, Language language ){
        String indexToFound = String.format( INDEX_TEMPLATE,
                nameTable,
                store.getId(), store.getLabel(),
                language.getId(), language.getIsoCode()
        );
        boolean isFound = this.getIndexes().contains( indexToFound );
        if( isFound ){
            return Optional.of( indexToFound );
        }
        return Optional.empty();
    }

    /**
     *
     * @param stores
     * @param nameTable
     * @return
     */
    public List<String> generateAllIndex( String nameTable, List<Store> stores ){

        List<String> index = new ArrayList<>();

        if( stores == null ){
            return index;
        }

        // STORE
        stores.forEach( store -> {
            // LANGUAGE
            store.getLanguages().forEach( language -> {

                // GENERATE INDEX
                index.add( String.format( INDEX_TEMPLATE,
                        nameTable,
                        store.getId(), store.getLabel(),
                        language.getId(), language.getIsoCode()
                ));
            });
        });

        return index;
    }

    public void saveIndexes ( List<String> indexes ){
        this.indexes.clear();
        this.indexes.addAll( indexes );
    }

}
