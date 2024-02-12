package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging;

import com.example.map_socialnetworkvt.Domain.Entity;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.CreateFilter;
import com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging.FilteringMethods.FilterMethod;
import com.example.map_socialnetworkvt.Repository.Repository;

public interface PagingRepository <ID,
    E extends Entity<ID>> extends
        Repository<ID,E> {
    Page<E> findAll(Pageable pageable, FilterMethod<E> filter);
}
