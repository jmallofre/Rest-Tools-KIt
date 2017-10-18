package fr.jmallofre.crud;

import java.util.HashMap;
import java.util.Map;

public class QueryParameter {
    private Map parameters = new HashMap();

    /**
     *
     * @param name
     * @param value
     */
    private QueryParameter(String name,Object value){
        this.parameters.put(name, value);
    }

    /**
     *
     * @param name
     * @param value
     * @return QueryParameter
     */
    public static QueryParameter with(String name,Object value){
        return new QueryParameter(name, value);
    }

    /**
     *
     * @param name
     * @param value
     * @return QueryParameter
     */
    public QueryParameter and(String name,Object value){
        this.parameters.put(name, value);
        return this;
    }

    /**
     *
     * @return Map
     */
    public Map parameters(){
        return this.parameters;
    }
}
