package com.ecommerce.catalog.controller;

import com.ecommerce.catalog.dto.tag.CreateTag;
import com.ecommerce.catalog.dto.tag.CreateTagLink;
import com.ecommerce.catalog.dto.tag.TagDTO;
import com.ecommerce.catalog.dto.tag.TagLinkDTO;
import com.ecommerce.catalog.sql.entity.tag.Tag;
import com.ecommerce.catalog.sql.entity.tag.TagLink;
import com.ecommerce.catalog.sql.entity.tag.TagLinkID;
import com.ecommerce.catalog.sql.service.tag.TagLinkService;
import com.ecommerce.catalog.sql.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController extends AbstractController  {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    public static final String URL_TAGS         = "/tags" ;
    public static final String URL_TAG          = "/tags/{tag_id}" ;

    public static final String URL_TAGS_LINK    = "/tags_link" ;
    public static final String URL_TAG_LINK     = "/tags_link/tag_link" ;

    private final TagService        tagService;
    private final TagLinkService tagLinkService;

    // --------------------------------------
    // -        Methods  - API - TAG        -
    // --------------------------------------

    @GetMapping(value = URL_TAGS, produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<List<TagDTO>> getCountries()
    {
        List<TagDTO> tagList = this.tagService.findAllToDTO();
        return new ResponseEntity<>( tagList, HttpStatus.OK );
    }

    @GetMapping(value = URL_TAG, produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<TagDTO> getTag( @PathVariable("tag_id") Integer tagId )
    {
        TagDTO currency = this.tagService.getByIdToDTO( tagId );
        return new ResponseEntity<>( currency, currency == null ? HttpStatus.NOT_FOUND : HttpStatus.FOUND );
    }


    @PostMapping(value = URL_TAGS, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<TagDTO> createTag (@RequestBody CreateTag createTag,
                                                       @Context HttpServletRequest requestContext ) {
        Tag tag = this.tagService.toEntity( createTag );
        tag = this.tagService.save( tag );
        TagDTO tagDTOResult = this.tagService.toDto( tag );
        return new ResponseEntity<>( tagDTOResult, HttpStatus.CREATED );
    }

    @DeleteMapping( value = URL_TAG )
    public ResponseEntity<String> deleteCountry( @PathVariable("tag_id") Integer tagId ){
        Tag tag = this.tagService.find( tagId );
        if( tag == null ) return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        this.tagService.delete( tag );
        return new ResponseEntity<>( HttpStatus.GONE );
    }

    // --------------------------------------
    // -     Methods  - API  - TAG LINK     -
    // --------------------------------------

    @GetMapping(value = URL_TAGS_LINK, produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<List<TagLinkDTO>> getTagsLinks()
    {
        List<TagLinkDTO> tagList = this.tagLinkService.findAllToDTO();
        return new ResponseEntity<>( tagList, HttpStatus.OK );
    }

    @GetMapping(value = URL_TAG_LINK, produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<TagLinkDTO> getTagLink( @RequestParam("tag_parent_id") Integer tagParentId,
                                                  @RequestParam("tag_child_id") Integer tagChildId )
    {
        TagLinkDTO currency = this.tagLinkService.getByIdToDTO( tagParentId, tagChildId );
        return new ResponseEntity<>( currency, currency == null ? HttpStatus.NOT_FOUND : HttpStatus.FOUND );
    }


    @PostMapping(value = URL_TAGS_LINK, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<TagLinkDTO> createTagLink (@RequestBody CreateTagLink createTagLink,
                                             @Context HttpServletRequest requestContext ) {
        TagLink tagLink = this.tagLinkService.toEntity( createTagLink );
        tagLink = this.tagLinkService.save( tagLink );
        TagLinkDTO tagDTOResult = this.tagLinkService.toDto( tagLink );
        return new ResponseEntity<>( tagDTOResult, HttpStatus.CREATED );
    }

    @DeleteMapping( value = URL_TAGS_LINK )
    public ResponseEntity<String> deleteTagLink( @RequestParam("tag_parent_id") Integer tagParentId,
                                                 @RequestParam("tag_child_id") Integer tagChildId ){
        TagLink tagLink = this.tagLinkService.find( new TagLinkID( tagParentId, tagChildId ) );
        if( tagLink == null ) return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        this.tagLinkService.delete( tagLink );
        return new ResponseEntity<>( HttpStatus.GONE );
    }


}
