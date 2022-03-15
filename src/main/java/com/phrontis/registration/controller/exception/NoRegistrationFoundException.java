package com.phrontis.registration.controller.exception;

public class NoRegistrationFoundException extends Exception{
    public NoRegistrationFoundException(String message){
        super(message);
    }

    public NoRegistrationFoundException(){
        super("No Registration Found Exception");
    }
}
