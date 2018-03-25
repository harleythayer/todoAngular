package com.hthayer.todoAngular.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.hthayer.todoAngular.model.Todo;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String>{

}