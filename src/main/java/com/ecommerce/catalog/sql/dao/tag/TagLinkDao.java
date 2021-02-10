package com.ecommerce.catalog.sql.dao.tag;

import com.ecommerce.catalog.sql.entity.tag.TagLink;
import com.ecommerce.catalog.sql.entity.tag.TagLinkID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TagLinkDao extends JpaRepository<TagLink, TagLinkID>,
        JpaSpecificationExecutor<TagLink>  {

}
