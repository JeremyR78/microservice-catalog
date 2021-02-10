package com.ecommerce.catalog.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConvertDTO<E, DTO> {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------

    /**
     *
     * @param entities
     * @return
     */
    public List<DTO> toDTO( List<E> entities,  Class<DTO> classDto ){
        List<DTO> listDTO = new ArrayList<>();
        for( E entity : entities )
        {
            listDTO.add( this.toDTO( entity , classDto) );
        }
        return listDTO;
    }


    /**
     *
     * @param entity
     * @return
     */
    public DTO toDTO( E entity, Class<DTO> classDto )
    {
        return modelMapper.map( entity, classDto );
    }

    /**
     *
     * @param dtoList
     * @return
     */
    public List<E> toEntity( List<DTO> dtoList, Class<E> classEntity){
        List<E> entities = new ArrayList<>();
        for( DTO dto : dtoList )
        {
            entities.add( this.toEntity( dto, classEntity ) );
        }
        return entities;
    }

    /**
     *
     * @param dto
     * @return
     */
    public E toEntity( DTO dto , Class<E> classEntity)
    {
        return this.modelMapper.map( dto, classEntity );
    }


    /**
     *
     * @return
     */
    public ModelMapper getModelMapper(){
        return this.modelMapper;
    }


    /**
     *
     * @param patch
     * @param target
     * @param type
     * @return
     * @throws JsonPatchException
     * @throws JsonProcessingException
     */
    public DTO applyPatchTo( JsonPatch patch, DTO target, Class<DTO> type )
            throws JsonPatchException, JsonProcessingException
    {
        JsonNode patched = patch.apply( this.objectMapper.convertValue( target, JsonNode.class ));
        return this.objectMapper.treeToValue( patched, type );
    }

}
