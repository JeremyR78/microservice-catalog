package com.ecommerce.catalog.sql.service.tag;

import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.dto.tag.CreateTag;
import com.ecommerce.catalog.dto.tag.CreateTagLink;
import com.ecommerce.catalog.dto.tag.TagDTO;
import com.ecommerce.catalog.dto.tag.TagLinkDTO;
import com.ecommerce.catalog.exceptions.TagLinkNotFoundException;
import com.ecommerce.catalog.exceptions.TagNotFoundException;
import com.ecommerce.catalog.service.AbstractService;
import com.ecommerce.catalog.sql.dao.tag.TagLinkDao;
import com.ecommerce.catalog.sql.entity.tag.Tag;
import com.ecommerce.catalog.sql.entity.tag.TagLink;
import com.ecommerce.catalog.sql.entity.tag.TagLinkID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagLinkService extends AbstractService<TagLink, TagLinkID> {


    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private final TagLinkDao tagLinkDao;
    private final ConvertDTO<TagLink, TagLinkDTO> dtoService;

    private final TagService tagService;

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @Override
    protected TagLinkDao getDao() {
        return this.tagLinkDao;
    }


    public TagLinkDTO getByIdToDTO( Integer tagParentId, Integer tagChildId ){
        TagLinkID id = new TagLinkID( tagParentId, tagChildId );
        TagLink tagLink = this.find( id );
        if( tagLink == null ) throw  new TagLinkNotFoundException( id );
        return this.toDto( tagLink );
    }

    public List<TagLinkDTO> findAllToDTO(){
        List<TagLink> tags = this.tagLinkDao.findAll();
        return this.toDto( tags );
    }

    public List<TagLinkDTO> toDto(List<TagLink> tags ) {
        return this.dtoService.toDTO(tags, TagLinkDTO.class);
    }

    public TagLinkDTO toDto( TagLink tag ) {
        return this.dtoService.toDTO(tag, TagLinkDTO.class);
    }

    public TagLink toEntity( CreateTagLink createTagLink ){
        TagLink tag = new TagLink();

        Integer tagParentId = createTagLink.getTagParentId();
        if( tagParentId == null ) throw new TagNotFoundException( tagParentId );
        Tag tagParent = this.tagService.find( tagParentId );
        if( tagParent == null ) throw new TagNotFoundException( tagParentId );
        tag.setTagParent( tagParent );

        Integer tagChildId = createTagLink.getTagChildId();
        if( tagChildId == null ) throw new TagNotFoundException( tagChildId );
        Tag tagChild = this.tagService.find( tagChildId );
        if( tagChild == null ) throw new TagNotFoundException( tagChildId );
        tag.setTagChild( tagChild );

        return tag;
    }

}
