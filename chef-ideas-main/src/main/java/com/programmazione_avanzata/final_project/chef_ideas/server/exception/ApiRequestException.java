package com.programmazione_avanzata.final_project.chef_ideas.server.exception;

public class ApiRequestException extends RuntimeException{
    public ApiRequestException(String message){
        super(message);
    }
}
