package com.hthayer.todoAngular.controllers;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hthayer.todoAngular.exceptions.NotFoundException;
import com.hthayer.todoAngular.model.Todo;
import com.hthayer.todoAngular.repository.TodoRepository;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

	private final TodoRepository todoRepository;

	@Autowired
	public AdminController(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Todo> getAllTodo( @RequestParam(value = "username", required = false) String userName ) {
		if( userName == null ) {
			return todoRepository.findAll();
		}else{
			return todoRepository.findAllByUserName(userName.toLowerCase());
		}
	}
	
	@RequestMapping(value = "user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Todo createTodo( @RequestParam(value = "username", required = true) String userName,
						    		@RequestBody final Todo todo) {
		todo.setUserName( userName.toLowerCase() );
		return todoRepository.save(todo);
	}

	@RequestMapping(value = "user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Todo getTodo( @RequestParam(value = "username", required = true) String userName,
						 		@RequestParam(value = "id", required = true) String requestId ) throws NotFoundException {
		final Todo todo = todoRepository.findByUserName(userName, requestId);
		if (todo == null) {
			throw new NotFoundException("Todo with id:" + requestId + " was not found");
		}
		return todo;
	}

	@RequestMapping(value = "user", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Todo updateTodo( @RequestParam(value = "username", required = true) String userName,
			 					   @RequestParam(value = "id", required = true) String requestId, 
			 					   @RequestBody Todo todo ) throws NotFoundException {
		
		final Todo oldTodo = todoRepository.findByUserName(userName, requestId);
		if (oldTodo == null) {
			throw new NotFoundException("Todo with id:" + requestId + " was not found");
		}
		BeanUtils.copyProperties(todo, oldTodo);
		oldTodo.setUserName( oldTodo.getUserName().toLowerCase());  //Make sure username is lower case
		return todoRepository.save(oldTodo);
	}

	@RequestMapping(value ="user", method = RequestMethod.DELETE)
	public void delete( @RequestParam(value = "username", required = true) String userName,
							  @RequestParam(value = "id", required = true) String requestId ) throws NotFoundException {
		Todo deleteTodo = todoRepository.findByUserName(userName.toLowerCase(), requestId);
		if( deleteTodo == null ) {
			throw new NotFoundException("Todo with userName: " + userName.toLowerCase() + " and Id: " + requestId + " was not found");
		}
		todoRepository.delete(deleteTodo.getId());
	}

}
