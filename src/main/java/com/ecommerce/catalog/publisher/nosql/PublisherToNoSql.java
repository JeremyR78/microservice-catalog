package com.ecommerce.catalog.publisher.nosql;

import com.ecommerce.catalog.publisher.core.AsyncService;
import com.ecommerce.catalog.publisher.core.TimeOutCommand;
import com.ecommerce.catalog.utils.logs.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.ecommerce.catalog.publisher.nosql.PublisherProductsConstants.NUMBER_MAX_POOL_THREAD;
import static com.ecommerce.catalog.utils.logs.LogNameSpace.*;

@Service
public class PublisherToNoSql extends AsyncService {

    // --------------------------------------
    // -        Attributes - STATIC         -
    // --------------------------------------

    public static final String SERVICE 						= PUBLISHER;
    public static final String SUB_SERVICE_EXECUTOR         = PRODUCT;
    public static final String INFORMATION                  = SQL_TO_NOSQL;
    private final Logger logger = LoggerFactory.getLogger(PublisherToNoSql.class);

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------


    // --------------------------------------
    // -        Attributes  - SERVICE       -
    // --------------------------------------


    // --------------------------------------
    // -        Constructors                -
    // --------------------------------------

    public PublisherToNoSql() {
        // Number of thread in same time
        super(  NUMBER_MAX_POOL_THREAD,
                // The list of timers to respect
                new ArrayList<>(),
                // Le TimeOut d'éxécution des commandes
                new TimeOutCommand( 5, TimeUnit.MINUTES));
        // Affiche le nom du service dans les logs des threads
        this.setLoggerServiceName( SERVICE );
    }

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    /**
     * Ajoute un produit à indexer sur la base NoSql
     *
     * @param productToIndexCommand
     */
    public void addProductToIndex( ProductIndexCommand productToIndexCommand  ){

        if( productToIndexCommand == null ){
            this.logger.warn( LogUtil.format( SERVICE, SUB_SERVICE_EXECUTOR, ADD, "The ProductIndexCommand is null !", ""));
            return;
        }

        if( productToIndexCommand.getProductId() == null  ) {
            this.logger.warn( LogUtil.format( SERVICE, SUB_SERVICE_EXECUTOR,ADD, "The product ID is null !", ""));
            return;
        }

        if( productToIndexCommand.getAction() == null  ) {
            this.logger.warn( LogUtil.format( SERVICE, SUB_SERVICE_EXECUTOR,ADD, "The product Action is null !", ""));
            return;
        }

        try {
            // Ajout d'une commande à éxecuter
            this.addCommand( productToIndexCommand );
            // Start if is not started
            this.executorAsynchronously();
        }
        catch ( IllegalStateException ex )
        {
            this.logger.warn(LogUtil.format(SERVICE, SUB_SERVICE_EXECUTOR, ADD, "The FIFO is full !!",
                    String.format("La commande de Feedback AMAZON n'a pas été ajoutée : %s ! " +
                                    "Il faudra attendre le prochain passage de la crontab." +
                                    " Nombre de commande dans la FIFO : %s ! FIFO : %s",
                            productToIndexCommand, this.getSizeFifo(), this.getAllCommands() ),
                    ex ));
            return;
        }

        this.logger.info("{}{}{} Ajout du {} à la liste de feedback AMAZON.",
                fLog(SERVICE) , fLog(SUB_SERVICE_EXECUTOR),  fLog(ADD), productToIndexCommand );
    }


}
