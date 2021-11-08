package com.csms.server.exception;

public class ObjectDoesNotExistException extends Exception {

    public ObjectDoesNotExistException(String objectName, long id){
        super(String.format("Object %s with id = %d doesn't exist", objectName, (id)));
    }
}
