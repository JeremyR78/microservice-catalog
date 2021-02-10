package com.ecommerce.catalog.sql.dao.tag;

import com.ecommerce.catalog.sql.entity.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TagDao extends JpaRepository<Tag, Integer>, JpaSpecificationExecutor<Tag> {
}
