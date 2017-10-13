package fr.jmallofre.controller;

import fr.jmallofre.exception.ResourceException;

import javax.ws.rs.core.Response;

public interface ResourceController<T> {
    Response get(Long id) throws ResourceException;
    Response fetch();
    Response create(T t);
    Response update(T t,Long id) throws ResourceException;
    Response delete(Long id) throws ResourceException;
}