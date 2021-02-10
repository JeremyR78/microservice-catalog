package com.ecommerce.catalog.utils.logs;

import static com.ecommerce.catalog.utils.logs.LogUtil.END_TAG_SERVICE;
import static com.ecommerce.catalog.utils.logs.LogUtil.START_TAG_SERVICE;

public class LogNameSpace {

    // COMMON
    public static final String SAVE_ENTITY       = "SAVE_ENTITY";

    // CATALOG
    public static final String CATALOG          = "CATALOG";
    public static final String TAG              = "TAG";

    // PRODUCT
    public static final String PRODUCT          = "PRODUCT";
    public static final String PRODUCT_SERVICE  = "PRODUCT_SERVICE";

    // PRODUCT_DOCUMENT
    public static final String PRODUCT_DOCUMENT  = "PRODUCT_DOCUMENT";

    // NEWSLETTER
    public static final String NEWSLETTER       = "NEWSLETTER";
    public static final String GENERATE_HTML    = "GENERATE_HTML";

    // ELASTICSEARCH
    public static final String ELASTICSEARCH    = "ELASTICSEARCH";

    // PROPERTIES
    public static final String PROPERTY         = "PROPERTY";

    // VALIDATION
    public static final String VALIDATION       = "VALIDATION";

    // ORDER
    public static final String ORDER                    = "ORDER";
    public static final String EXTERNAL_SCORING         = "EXTERNAL_SCORING";

    // PAYMENT
    public static final String PAYMENT          = "PAYMENT";

    // MAIL
    public static final String MAIL             =   "MAIL";

    // IMAGE
    public static final String IMAGE            = "IMAGE";
    public static final String COOKIE           = "COOKIE";

    // UTILS
    public static final String TOOLS            = "TOOLS";
    public static final String LANGUAGE         = "LANGUAGE";

    // OBSERVER
    public static final String OBSERVER         = "OBSERVER";


    // PUBLISHER
    public static final String PUBLISHER         = "PUBLISHER";

    // TYPE
    public static final String PASSWORD         = "PASSWORD";
    public static final String CONTROLLER       = "CONTROLLER";
    public static final String COMMAND          = "COMMAND";
    public static final String THEMATIC         = "THEMATIC";
    public static final String RESULT           = "RESULT";
    public static final String ASYNC            = "ASYNC";
    public static final String SQL              = "SQL";
    public static final String SQL_TO_NOSQL     = "SQL_TO_NOSQL";
    public static final String INDEX         = "INDEX";

    // ACTION
    public static final String DELETE           = "DELETE";
    public static final String UPLOAD           = "UPLOAD";
    public static final String DOWNLOAD         = "DOWNLOAD";
    public static final String CREATE           = "CREATE";
    public static final String UPDATE           = "UPDATE";
    public static final String CREATE_OR_UPDATE = "CREATE_OR_UPDATE";
    public static final String DUPLICATE        = "DUPLICATE";
    public static final String CALCUL           = "CALCUL";
    public static final String RECALCUL         = "RECALCUL";
    public static final String SEND             = "SEND";
    public static final String IMPORT           = "IMPORT";
    public static final String ASSOCIATE        = "ASSOCIATE";
    public static final String SEARCH           = "SEARCH";
    public static final String DISPLAY          = "DISPLAY";
    public static final String TO_DTO           = "TO_DTO";
    public static final String TIMER            = "TIMER";
    public static final String ADD              = "ADD";
    public static final String START            = "START";
    public static final String STOP             = "STOP";
    public static final String NOT_FOUND        = "NOT_FOUND";
    public static final String FINISH           = "FINISH";

    /**
     * Ajout le format d'identification des catégories des logs
     *
     * @param name
     * @return
     */
    public static String fLog(String name ){
        return String.format("%s%s%s",
                START_TAG_SERVICE, name, END_TAG_SERVICE );
    }

    private LogNameSpace(){}
}
