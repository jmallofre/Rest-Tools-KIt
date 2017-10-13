package fr.jmallofre.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestResponse<T> implements Serializable {
    private int httpErrorCode;
    private T data;
    private String errorMessage;
    public static final String MEDIA_TYPE = MediaType.APPLICATION_JSON + ";charset=utf-8";


    public RestResponse(){
        httpErrorCode = 0;
        data = null;
        errorMessage = null;
    }

    /**
     * Constructor by parameter
     * @param httpErrorCode
     * @param data
     * @param errorMessage
     */
    public RestResponse(int httpErrorCode, T data, String errorMessage) {
        this.httpErrorCode = httpErrorCode;
        this.data = data;
        System.out.println(this.data);
        this.errorMessage = errorMessage;
    }

    public Response throw200Ok() {
        Response.Status status = Response.Status.OK;
        setHttpErrorCode(status.getStatusCode());
        return Response.status(status).entity(this).type(MEDIA_TYPE).build();
    }

    public Response throw201Created(){
        Response.Status status = Response.Status.CREATED;
        setHttpErrorCode(status.getStatusCode());
        return Response.status(status).entity(this).type(MEDIA_TYPE).build();
    }

    public Response throw204NoContent(){
        Response.Status status = Response.Status.NO_CONTENT;
        setHttpErrorCode(status.getStatusCode());
        return Response.status(status).entity(this).type(MEDIA_TYPE).build();
    }

    public Response throw400BadRequest() {
        Response.Status status = Response.Status.BAD_REQUEST;
        setHttpErrorCode(status.getStatusCode());
        return Response.status(status).entity(this).type(MEDIA_TYPE).build();
    }

    public Response throw403Forbidden() {
        Response.Status status = Response.Status.FORBIDDEN;
        setHttpErrorCode(status.getStatusCode());
        return Response.status(status).entity(this).type(MEDIA_TYPE).build();
    }

    public Response throw404NotFound(){
        Response.Status status = Response.Status.NOT_FOUND;
        setHttpErrorCode(status.getStatusCode());
        return Response.status(status).entity(this).type(MEDIA_TYPE).build();
    }

    public Response throw405MethodNotAllowed() {
        Response.Status status = Response.Status.METHOD_NOT_ALLOWED;
        setHttpErrorCode(status.getStatusCode());
        return Response.status(status).entity(this).type(MEDIA_TYPE).build();
    }

    public Response throw409Conflict() {
        Response.Status status = Response.Status.CONFLICT;
        setHttpErrorCode(status.getStatusCode());
        return Response.status(status).entity(this).type(MEDIA_TYPE).build();
    }


    public Response throw500InternalServerError() {
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        setHttpErrorCode(status.getStatusCode());
        return Response.status(status).entity(this).type(MEDIA_TYPE).build();
    }

    public int getHttpErrorCode() {
        return httpErrorCode;
    }

    /**
     *
     * @param httpErrorCode
     */
    private void setHttpErrorCode(int httpErrorCode) {
        this.httpErrorCode = httpErrorCode;
    }

    public boolean isSuccess() {
        return httpErrorCode == 200;
    }

    public T getData() {
        return data;
    }

    /**
     *
     * @param data
     */
    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     *
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}