package fr.jmallofre;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.jmallofre.crud.CrudServiceBean;

import static org.mockito.Mockito.mock;

public class ResourceTest {
    protected CrudServiceBean crudServiceBean;

    public ResourceTest(){
        crudServiceBean = mock(CrudServiceBean.class);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
