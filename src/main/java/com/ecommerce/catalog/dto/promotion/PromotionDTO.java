package com.ecommerce.catalog.dto.promotion;

import com.ecommerce.catalog.dto.tag.TagDTO;
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
public class PromotionDTO implements Serializable {

    private Integer id;
    private Integer storeId;
    private Integer customerGroupId;
    private Integer countryId;
    private List<Integer> pricePromotionsId;
    private List<TagDTO> tags;
    private Boolean enable;
    private Date startAt;
    private Date finishAt;
    private List<UserAction> userActions;

}
