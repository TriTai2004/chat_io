package com.chatio.socket.exception;

public class ResourceNotFoundException extends RuntimeException{

    static final long serialVersionUID = -7034897190745766939L;
    
    public ResourceNotFoundException(String message) {
        super(message);
    }

}