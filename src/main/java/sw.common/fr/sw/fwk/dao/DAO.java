package fr.sw.fwk.dao;

import java.util.Collection;
import java.util.List;

/**
 * FFUCK operations
 * @param <E>
 */
public interface DAO<E> {

    E find(String name);

    List<E> findAll();

    E update(E entity);

    E create(E entity);

    void kill(E entity);

}
