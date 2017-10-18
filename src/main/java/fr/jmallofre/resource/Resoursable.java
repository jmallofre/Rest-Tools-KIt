package fr.jmallofre.resource;

import fr.jmallofre.exception.ResourceException;

import javax.ws.rs.core.Response;

public interface Resoursable<T>{
    Response get(long id) throws ResourceException;
    Response fetch();
    Response insert(T t);
    Response merge(T t, long id) throws ResourceException;
    Response delete(long id) throws ResourceException;
}