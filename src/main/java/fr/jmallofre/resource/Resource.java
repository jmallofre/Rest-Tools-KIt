package fr.jmallofre.resource;

import fr.jmallofre.crud.CrudServiceBean;
import fr.jmallofre.exception.ResourceException;
import fr.jmallofre.response.RestResponse;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class Resource {
    final Logger LOG = Logger.getLogger(Resource.class.getName());

    public static CrudServiceBean crudService;
    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_JSON_UTF8 = "application/json;charset=urf-8";
    public static final String APPLICATION_URLENCODED = "application/x-www-form-urlencoded";
    public Class<?> entity;


    public Resource() {
        crudService = new CrudServiceBean(CrudServiceBean.PU_DB);
    }

    /**
     * Constructor by parameter
     * @param crudService
     */
    public Resource(CrudServiceBean crudService) {
        this.crudService = crudService;
    }

    /**
     * Set CrudService
     * @param crudServiceBean
     */
    public void setCrudService(CrudServiceBean crudServiceBean)
    {
        this.crudService = crudServiceBean;
    }


    /**
     * Generic Get
     * @param type
     * @param id
     * @param <T>
     * @return A response
     * @throws ResourceException
     */
    public <T> Response get(Class<T> type, long id) throws ResourceException {
        Response toReturn;
        RestResponse<T> response = new RestResponse<T>();
        crudService.newTransaction();
        if(crudService.exist(type,id)){
            response.setData(crudService.find(type, id));
            toReturn = response.throw200Ok();
        }
        else{
            response.setErrorMessage("Not Found");
            toReturn = response.throw404NotFound();
        }
        crudService.closeTransaction();
        LOG.info(toReturn.toString());
        return toReturn;
    }


    /**
     * Generic Fetch
     * @param type
     * @param <T>
     * @return A response
     */
    public <T> Response fetch(Class<T> type) {
        return new RestResponse<List<T>>(200, crudService.findAll(type),null).throw200Ok();
    }

    /**
     * Generic Create
     * @param t
     * @param <T>
     * @return A response
     */
    public <T> Response create(T t) {
        // Validator
        /*
        Validate the entity before the creation. Else, show a message that explain
        the error.
         */
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<T>> violations = validator.validate(t);
        for (ConstraintViolation<T> violation : violations) {
            LOG.warning(violation.getMessage());
        }

        System.out.println("violations : "+violations.equals(null));
        System.out.println("violations size : "+violations.size());


        Response toReturn;
        RestResponse<T> response = new RestResponse<T>();
        if(t != null) {
            try {
                crudService.newTransaction();
                response.setData(crudService.create(t));
                crudService.commit();
                toReturn = response.throw201Created();
            } catch (Exception e) {
                e.printStackTrace();
                response.setErrorMessage("Conflict");
                toReturn = response.throw409Conflict();
            }
        }else{
            response.setErrorMessage("Forbidden");
            toReturn = response.throw403Forbidden();
        }
        crudService.closeTransaction();
        LOG.info(toReturn.toString());
        return toReturn;
    }

    /**
     * Generic Update
     * @param t
     * @param <T>
     * @return A response
     */
    public <T> Response update(T t, long id) throws ResourceException {
        // Validator
        /*
        Validate the entity before the creation. Else, show a message that explain
        the error.
         */
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        for (ConstraintViolation<T> violation : violations) {
            LOG.warning(violation.getMessage());
        }

        Response toReturn;
        RestResponse<T> response = new RestResponse<T>();
        if(t != null) {


            crudService.newTransaction();
            if(crudService.exist(t.getClass(),id)){
                response.setData(crudService.update(t));
                crudService.commit();
                toReturn = response.throw200Ok();
            }
            else{
                response.setErrorMessage("Not Found");
                toReturn = response.throw404NotFound();
            }
        }else{
            response.setErrorMessage("Forbidden");
            toReturn = response.throw403Forbidden();
        }
        crudService.closeTransaction();
        LOG.info(toReturn.toString());
        return toReturn;
    }

    /**
     * Generic Delete
     * @param type
     * @param id
     * @param <T>
     * @return A response
     * @throws ResourceException
     */
    public <T> Response delete(Class<T> type, long id) throws ResourceException {
        Response toReturn;
        RestResponse<T> response = new RestResponse<T>();
        if(crudService.exist(type,id)){
            crudService.newTransaction();
            crudService.delete(type, id);
            crudService.commit();

            toReturn = response.throw204NoContent();
        }
        else{
            response.setErrorMessage("Not Found");
            toReturn = response.throw404NotFound();
        }
        crudService.closeTransaction();
        LOG.info(toReturn.toString());
        return toReturn;
    }
}
