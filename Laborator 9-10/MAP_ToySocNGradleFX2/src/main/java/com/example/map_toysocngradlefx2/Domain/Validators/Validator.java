package com.example.map_toysocngradlefx2.Domain.Validators;

import com.example.map_toysocngradlefx2.Exception.ValidationException;

/*
    Validator for Enitities. It is an univers Validator
 */
public interface Validator <T>{
    void vaildate(T entity) throws ValidationException;
}
