package com.walker.ds_catalog_java_spring_expert_devsuperior.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
