package fr.jmallofre.crud;

import fr.jmallofre.exception.ResourceException;
import lombok.Getter;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CrudServiceBean {
    EntityManagerFactory entityManagerFactory;

    @Getter
    EntityManager em;
    EntityTransaction transaction;

    public static final String PU_HEROKU = "heroku_postgres";
    public static final String PU_DEFAULT = "postgres";
    public static String PU_DB = "";
    //public static final String PU_DB = "heroku_postgres";
    //public static final String PU_DOCKER = "docker";

    public CrudServiceBean(){
        setPersistenceUnit(PU_DB);
    }

    /**
     * Constructer by parameter
     * @param persistence_unit
     */
    public CrudServiceBean(String persistence_unit){
        setPersistenceUnit(persistence_unit);
    }

    /**
     * Set persistence unit
     * @param persistence_unit
     */
    public void setPersistenceUnit(String persistence_unit){
        entityManagerFactory = Persistence.createEntityManagerFactory(persistence_unit);
        em = entityManagerFactory.createEntityManager();

        transaction = em.getTransaction();
    }

    /**
     * Create your own query and set params if necessary
     * @param string
     * @return Query
     */
    public Query createQuery(String string){
        em.createNamedQuery(string);
        return null;
    }

    public void newTransaction(){
        if(!transaction.isActive())
            transaction.begin();
    }

    public void closeTransaction(){
        em.close();
        entityManagerFactory.close();
    }

    public void clearCache(){
        em.clear();
    }

    public void commit(){
        transaction.commit();
    }

    /**
     * CRUD Create
     * @param t
     * @param <T>
     * @return Object
     * @throws ResourceException
     */
    public  <T> T create(T t) throws ResourceException {
        this.em.persist(t);
        this.em.flush();
        this.em.refresh(t);
        return t;
    }

    /**
     * CRUD Get
     * @param type
     * @param id
     * @param <T>
     * @return Object
     * @throws ResourceException
     */
    public  <T> T find(Class<T> type,Object id) throws ResourceException {
        T data = (T) this.em.find(type, id);
        return data;
    }

    /**
     * CRUD Delete
     * @param type
     * @param id
     * @throws ResourceException
     */
    public void delete(Class type,Object id) throws ResourceException{
        Object ref = this.em.getReference(type, id);
        this.em.remove(ref);
    }

    /**
     * CRUD Update
     * @param t
     * @param <T>
     * @return Object
     * @throws ResourceException
     */
    public  <T> T update(T t) throws ResourceException{
        T data = (T)this.em.merge(t);
        return data;
    }

    /**
     * Generic Find with Named Query
     * @param type
     * @param namedQueryName
     * @param <T>
     * @return list
     */
    public <T> List findWithNamedQuery(Class<T> type, String namedQueryName){
        return this.em.createNamedQuery(namedQueryName).getResultList();
    }

    /**
     * Generic Find with Names Query and Parameters
     * @param type
     * @param namedQueryName
     * @param parameters
     * @param <T>
     * @return list
     */
    public <T> List findWithNamedQuery(Class<T> type, String namedQueryName, Map parameters){
        return findWithNamedQuery(type, namedQueryName, parameters, 0);
    }

    /**
     * Generic Find with Named Query with QueryName
     * @param type
     * @param queryName
     * @param resultLimit
     * @param <T>
     * @return list
     */
    public <T> List findWithNamedQuery(Class<T> type, String queryName, int resultLimit) {
        return this.em.createNamedQuery(queryName).
                setMaxResults(resultLimit).
                getResultList();
    }

    /**
     * Generic Find with Native Query
     * @param sql
     * @param type
     * @return list
     */
    public List findByNativeQuery(String sql, Class type) {
        return this.em.createNativeQuery(sql, type).getResultList();
    }


    /**
     * Generic Find with Names Query and parameters and limits
     * @param type
     * @param namedQueryName
     * @param parameters
     * @param resultLimit
     * @param <T>
     * @return list
     */
    public <T> List findWithNamedQuery(Class<T> type, String namedQueryName, Map parameters, int resultLimit){
        Set rawParameters = parameters.keySet();
        TypedQuery<T> query = this.em.createNamedQuery(namedQueryName,type);
        if(resultLimit > 0)
            query.setMaxResults(resultLimit);
        for (Object entry : rawParameters) {
            query.setParameter(entry.toString(), parameters.get(entry.toString()));
        }
        return query.getResultList();
    }

    /**
     * Generic Get All
     * @param clazz
     * @param <T>
     * @return list
     */
    public <T> List findAll(Class<T> clazz) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> rootEntry = cq.from(clazz);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }

    /**
     * Existence test to an object in database
     * @param type
     * @param id
     * @return boolean
     * @throws ResourceException
     */
    public boolean exist(Class type,Object id) throws ResourceException {
        return find(type,id) != null;
    }

}
