package com.hthayer.todoAngular.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.hthayer.todoAngular.model.Todo;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String>{

	@Query("{ 'userName' : ?0 }")
	List<Todo> findAllByUserName(String name);
	
	@Query("{ 'userName': ?0, '_id': ?1 }")
	Todo findByUserName( String name, String id );
}