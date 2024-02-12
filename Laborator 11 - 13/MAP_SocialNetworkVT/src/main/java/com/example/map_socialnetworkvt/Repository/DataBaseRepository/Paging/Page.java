package com.example.map_socialnetworkvt.Repository.DataBaseRepository.Paging;

import com.example.map_socialnetworkvt.Domain.Friendships.Friendship;

import java.util.Iterator;
import java.util.stream.Stream;

public interface Page<E>{
    Pageable getPageable();
    Pageable nextPageable();
    Stream<E> getContent();

}
