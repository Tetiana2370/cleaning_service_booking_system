package com.csms.server.api.response;

import java.util.ArrayList;
import java.util.List;

public class ResponseBody<T> {

    public static final String OBJECT_DOES_NOT_EXIST_ERROR_MESSAGE = "Object not found";
    public T body;
    public List<String> errors = new ArrayList<>();
    public boolean succeed = true;

    public ResponseBody(T body){
        this.body = body;
        addErrorIfObjectIsNull();
    }

    public ResponseBody(T body, String error){
        this.body = body;
        if(error != null && !error.isBlank()){
            this.errors.add(error);
            this.succeed = false;
        }
        addErrorIfObjectIsNull();
    }

    public ResponseBody(T body, List<String> errors){
        this.body = body;
        if(errors != null){
            this.errors = errors;
            if(errors.size() > 0){
                this.succeed = false;
            }
        }
        addErrorIfObjectIsNull();
    }

    public ResponseBody(Exception exception){
        this.errors.add(exception.getMessage());
        this.succeed = false;
    }

    private void addErrorIfObjectIsNull(){
        if(this.body == null){
            this.errors.add(OBJECT_DOES_NOT_EXIST_ERROR_MESSAGE);
            this.succeed = false;
        }
    }

}
