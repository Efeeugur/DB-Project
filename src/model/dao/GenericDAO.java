package model.dao;

import java.util.List;
import java.util.Optional;

/**
 * Generic Data Access Object interface for CRUD operations.
 * @param <T> Entity type
 */
public interface GenericDAO<T> {
    
    /**
     * Saves a new entity.
     * @param entity The entity to save
     * @return The saved entity with generated ID
     */
    T save(T entity);
    
    /**
     * Finds an entity by its ID.
     * @param id The entity ID
     * @return Optional containing the entity if found
     */
    Optional<T> findById(int id);
    
    /**
     * Retrieves all entities.
     * @return List of all entities
     */
    List<T> findAll();
    
    /**
     * Updates an existing entity.
     * @param entity The entity to update
     * @return The updated entity
     */
    T update(T entity);
    
    /**
     * Deletes an entity by its ID.
     * @param id The entity ID
     * @return true if deleted successfully
     */
    boolean delete(int id);
    
    /**
     * Counts total number of entities.
     * @return Count of entities
     */
    int count();
}
