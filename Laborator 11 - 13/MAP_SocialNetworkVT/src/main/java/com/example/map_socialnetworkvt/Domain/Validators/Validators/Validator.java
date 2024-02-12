package com.example.map_socialnetworkvt.Domain.Validators.Validators;

import com.example.map_socialnetworkvt.Exception.ValidationException;

/*
    Validator for Enitities. It is an univers Validator
 */
public interface Validator <T>{
    void vaildate(T entity) throws ValidationException;
}
