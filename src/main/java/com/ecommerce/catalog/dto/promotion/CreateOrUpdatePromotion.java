package com.ecommerce.catalog.dto.promotion;

import com.ecommerce.catalog.dto.price.CreatePriceDTO;
import com.ecommerce.catalog.sql.entity.user.UserAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrUpdatePromotion implements Serializable  {

    private Integer storeId;
    private List<Integer> tagsId ;
    private Integer customerGroupId;
    private Integer countryId;

    private List<CreatePriceDTO> pricePromotion;

    private Boolean enable;
    private Date startAt;
    private Date finishAt;

    private UserAction userAction;

}
