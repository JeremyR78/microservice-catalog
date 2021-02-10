package com.ecommerce.catalog.observer.nosql;

import com.ecommerce.catalog.model.ActionType;
import com.ecommerce.catalog.nosql.service.ProductEsService;
import com.ecommerce.catalog.publisher.nosql.ProductIndexCommand;
import com.ecommerce.catalog.publisher.nosql.PublisherToNoSql;
import com.ecommerce.catalog.sql.entity.product.Product;
import com.ecommerce.catalog.sql.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static com.ecommerce.catalog.utils.logs.LogNameSpace.*;

@Service
@RequiredArgsConstructor
public class ObserverSqlService implements PropertyChangeListener {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private transient final Logger logger = LoggerFactory.getLogger(getClass());

    private final ProductService productService;
    private final PublisherToNoSql publisherToNoSql;
    private final ProductEsService productEsService;


    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @PostConstruct
    public void subscribeToServiceSql(){

        this.productService.addPropertyChangeListener( this );

    }


    @Override
    public void propertyChange( PropertyChangeEvent propertyChangeEvent ) {
        ProductIndexCommand productIndexCommand = null;

        String name = propertyChangeEvent.getPropertyName();
        Object newValue = propertyChangeEvent.getNewValue();
        Object oldValue = propertyChangeEvent.getOldValue();
        Object propagationId = propertyChangeEvent.getPropagationId();

        this.getLogger().info("{}{}{} Property change <{}> of <{}> to <{}> -> id <{}> ",
                fLog(OBSERVER),
                fLog(SQL),
                fLog(UPDATE),
                name,
                oldValue,
                newValue,
                propagationId );

        if( newValue instanceof Product ){
            if( oldValue == null ) {
                productIndexCommand = new ProductIndexCommand(((Product) newValue).getId(), ActionType.CREATED, this.productService, this.productEsService );
            } else {
                productIndexCommand = new ProductIndexCommand(((Product) newValue).getId(), ActionType.UPDATED, this.productService, this.productEsService);
            }
        }
        else if ( oldValue instanceof Product && newValue == null ){
            productIndexCommand = new ProductIndexCommand(((Product) oldValue).getId(), ActionType.DELETED, this.productService, this.productEsService );
        }
        else {
            this.getLogger().error("{}{}{} Impossible to update <{}> -> NEW : <{}> - OLD : <{}>",
                    fLog(OBSERVER),
                    fLog(SQL),
                    fLog(UPDATE),
                    name,
                    oldValue,
                    newValue );
        }

        if( productIndexCommand != null ){
            this.publisherToNoSql.addProductToIndex( productIndexCommand );
        }
    }


    protected Logger getLogger()
    {
        return this.logger;
    }
}
