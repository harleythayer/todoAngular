package com.hthayer.todoAngular.controllers;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hthayer.todoAngular.exceptions.NotFoundException;
import com.hthayer.todoAngular.model.Todo;
import com.hthayer.todoAngular.repository.TodoRepository;

@RestController
@RequestMapping(value = "/todos")
public class TodoController {

	private final TodoRepository todoRepository;

	@Autowired
	public TodoController(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}

	@RequestMapping(value = "{userName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Todo> getAllTodo(@PathVariable final String userName) {
		return todoRepository.findAllByUserName(userName.toLowerCase());
	}

	@RequestMapping(value = "{userName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Todo createTodo(@PathVariable final String userName,
						   @RequestBody final Todo todo) {
		todo.setUserName( userName.toLowerCase() );
		return todoRepository.save(todo);
	}

	@RequestMapping(value = "{userName}/{requestId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Todo getTodo(@PathVariable final String userName, 
			            @PathVariable final String requestId ) throws NotFoundException {
		final Todo todo = todoRepository.findByUserName( userName.toLowerCase(), requestId);
		if (todo == null) {
			throw new NotFoundException("Todo with id:" + requestId + " was not found");
		}
		return todo;
	}

	@RequestMapping(value = "{userName}/{requestId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Todo updateTodo(@PathVariable final String userName, 
						   @PathVariable final String requestId, 
						   @RequestBody Todo todo) throws NotFoundException {
		
		final Todo oldTodo = todoRepository.findByUserName(userName.toLowerCase(), requestId);
		if (oldTodo == null) {
			throw new NotFoundException("Todo with id:" + requestId + " was not found");
		}
		BeanUtils.copyProperties(todo, oldTodo);
		oldTodo.setUserName( oldTodo.getUserName().toLowerCase());  //Make sure username is lower case
		return todoRepository.save(oldTodo);
	}

	@RequestMapping(value = "{userName}/{requestId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable final String userName, 
					   @PathVariable final String requestId) throws NotFoundException {
		Todo deleteTodo = todoRepository.findByUserName(userName.toLowerCase(), requestId);
		if( deleteTodo == null ) {
			throw new NotFoundException("Todo with userName: " + userName.toLowerCase() + " and Id: " + requestId + " was not found");
		}
		todoRepository.delete(deleteTodo.getId());
	}

}
