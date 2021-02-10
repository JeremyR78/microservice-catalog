package com.ecommerce.catalog.exceptions;

import com.ecommerce.catalog.sql.entity.tag.TagLinkID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TagLinkNotFoundException extends RuntimeException  {
    public TagLinkNotFoundException( TagLinkID id ) {
        super( String.format("Tag <%s> not found !", id));
    }
}
