package com.example.map_toysocialnetworkgradlefx.Domain.Validators;

import com.example.map_toysocialnetworkgradlefx.Exception.ValidationException;

/*
    Validator for Enitities. It is an univers Validator
 */
public interface Validator <T>{
    void vaildate(T entity) throws ValidationException;
}
