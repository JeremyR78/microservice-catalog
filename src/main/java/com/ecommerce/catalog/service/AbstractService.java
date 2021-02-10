package com.ecommerce.catalog.service;

import com.ecommerce.catalog.observer.core.Observable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public abstract class AbstractService<T, ID> extends Observable {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private transient final Logger logger = LoggerFactory.getLogger(getClass());

    // --------------------------------------
    // -        Methods - DAO               -
    // --------------------------------------

    protected abstract JpaRepository<T, ID> getDao();

    @Transactional
    public T save( T entity ) {
        return getDao().save( entity );
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public T find(ID id) {
        return (T) getDao().getOne( id );
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<T> findWithOptional(ID id) {
        return Optional.ofNullable( (T) getDao().getOne( id ) );
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<T> findAll() {
        return getDao().findAll();
    }

    @Transactional
    public void delete( T entity ){
        this.getDao().delete( entity );
    }


    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    protected Logger getLogger()
    {
        return this.logger;
    }


    /**
     * Apply the patch to the DTO object
     *
     * @param patch
     * @param target
     * @return
     * @throws JsonPatchException
     * @throws JsonProcessingException
     */
    @Transactional
    public T applyPatchTo( JsonPatch patch, T target ) throws JsonPatchException, JsonProcessingException {
        return null;
    }

    @Transactional
    public T saveWithNotify( T oldEntity, T newEntity ){
        T newEntitySaved = this.save( newEntity );
        this.getObservers().firePropertyChange( getClass().getName(), oldEntity , newEntity );
        return newEntitySaved;
    }

    @Transactional
    public void deleteWithNotify( T oldEntity ){
        this.getObservers().firePropertyChange( getClass().getName(), oldEntity , null );
        this.delete( oldEntity );
    }

}
