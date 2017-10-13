package fr.jmallofre.crud;

import fr.jmallofre.exception.ResourceException;

public interface CrudService {
    <T> T create(T t) throws ResourceException;
    <T> T find(Class<T> type, Object id) throws ResourceException;
    <T> T update(T t) throws ResourceException;
    void delete(Class type, Object id) throws ResourceException;
}
