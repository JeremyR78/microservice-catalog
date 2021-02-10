package com.ecommerce.catalog.sql.service.tag;

import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.dto.tag.CreateTag;
import com.ecommerce.catalog.dto.tag.TagDTO;
import com.ecommerce.catalog.exceptions.TagNotFoundException;
import com.ecommerce.catalog.service.AbstractService;
import com.ecommerce.catalog.sql.dao.tag.TagDao;
import com.ecommerce.catalog.sql.entity.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService extends AbstractService<Tag, Integer> {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private final TagDao tagDao;

    private final ConvertDTO<Tag, TagDTO> dtoService;

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @PostConstruct
    public void init(){
        this.dtoService.getModelMapper().typeMap( Tag.class, TagDTO.class )
                .addMappings( mapper -> mapper.skip( TagDTO::setParentsTags ) )
                .addMappings( mapper -> mapper.skip( TagDTO::setChildrenTags ) );
    }

    @Override
    protected TagDao getDao() {
        return this.tagDao;
    }

    public TagDTO getByIdToDTO( Integer tagId ){
        Tag tag = this.find( tagId );
        if( tag == null ) throw  new TagNotFoundException( tagId );
        return this.toDto( tag );
    }

    public List<TagDTO> findAllToDTO(){
        List<Tag> tags = this.tagDao.findAll();
        return this.toDto( tags );
    }

    public List<TagDTO> toDto( List<Tag> tags ) {
        return this.dtoService.toDTO(tags, TagDTO.class);
    }

    public TagDTO toDto( Tag tag ) {
       return this.dtoService.toDTO( tag, TagDTO.class );
    }

    public Tag toEntity( CreateTag createTag ){
        Tag tag = new Tag();
        tag.setLabel( createTag.getLabel() );

        return tag;
    }

}
