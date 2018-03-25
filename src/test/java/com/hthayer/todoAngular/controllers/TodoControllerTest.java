package com.hthayer.todoAngular.controllers;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.hthayer.todoAngular.model.Todo;
import com.hthayer.todoAngular.repository.TodoRepository;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles( "test" )
public class TodoControllerTest {

	@Rule
	public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("todo-testDb");

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private MockMvc mvc;
	
	private final ObjectMapper mapper = new ObjectMapper();

	@Test
	@UsingDataSet(locations = "/testData/todoList.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
	public void testCountAllTodos() throws Exception {
		mvc.perform(get("/todos/harley")
					   	.accept(MediaType.APPLICATION_JSON)
					)
					.andExpect(jsonPath("$", hasSize(2)));
	}
	
	@Test
	@UsingDataSet(locations = "/testData/todoList.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
	public void testGetOneTodo() throws Exception {
		mvc.perform(get("/todos/harley/2")
						.accept(MediaType.APPLICATION_JSON)
					)
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.userName", is("harley")))
					.andExpect(jsonPath("$.whatToDo", is("Test a second todo item")))
					.andExpect(jsonPath("$.completed", is(false)));
	}
	
	@Test
	@UsingDataSet(loadStrategy = LoadStrategyEnum.DELETE_ALL)
	public void testGetOneTodoFail() throws Exception {
		mvc.perform(get("/todos/harley/3")
						.accept(MediaType.APPLICATION_JSON)
					)
					.andExpect(status().isNotFound());
	}

	@Test
	@UsingDataSet(locations = "/testData/todoList.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
	public void testdeleteATodo() throws Exception {
		final String id = "1";
		mvc.perform(delete("/todos/harley/" + id)
						.accept(MediaType.APPLICATION_JSON)
					)
				   	.andExpect(status().isOk());
		
		//Test controller to make sure it is throwing a 404 exception
		mvc.perform(get("/todos/harley/" + id)
						.accept(MediaType.APPLICATION_JSON)
					)
					.andExpect(status().isNotFound());

		mvc.perform(get("/todos/harley")
						.accept(MediaType.APPLICATION_JSON)
					)
					.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	@UsingDataSet(locations = "/testData/todoList.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
	public void testUpdateOneTodo( ) throws Exception {
		final Todo updateTodo = new Todo( "1", "harley", "We updated the todo item", true );
		
		
		
		mvc.perform(put("/todos/harley/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content( mapper.writeValueAsString(updateTodo))
						.accept(MediaType.APPLICATION_JSON)
					)
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.userName", is(updateTodo.getUserName())))
					.andExpect(jsonPath("$.whatToDo", is(updateTodo.getWhatToDo())))
					.andExpect(jsonPath("$.completed", is(updateTodo.getCompleted())));
		
		final Todo updatedTodo = todoRepository.findOne(updateTodo.getId());
		assertEquals( updateTodo.getWhatToDo(), updatedTodo.getWhatToDo()	);
		assertEquals( updateTodo.getUserName(), updatedTodo.getUserName( ) );
		assertEquals( updateTodo.getCompleted(), updatedTodo.getCompleted() );
		
		mvc.perform(get("/todos/harley/" + updateTodo.getId())
						.accept(MediaType.APPLICATION_JSON)
					)
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.userName", is(updateTodo.getUserName())))
					.andExpect(jsonPath("$.whatToDo", is(updateTodo.getWhatToDo())))
					.andExpect(jsonPath("$.completed", is(updateTodo.getCompleted())));

	}
	
	@Test
	@UsingDataSet(loadStrategy = LoadStrategyEnum.DELETE_ALL)
	public void testUpdateOneTodoFail( ) throws Exception {
		final Todo updateTodo = new Todo( "6", "harley", "We updated the todo item", true );
				
		mvc.perform(put("/todos/harley/" + updateTodo.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content( mapper.writeValueAsString(updateTodo))
						.accept(MediaType.APPLICATION_JSON)
					)
					.andExpect(status().is4xxClientError());
	}
	
	@Test
	@UsingDataSet(loadStrategy = LoadStrategyEnum.DELETE_ALL)
	public void testCreateNewTodo( ) throws Exception {
		final Todo newTodo = new Todo( "5", "harley", "This is a new Todo we just created", true);
				
		mvc.perform(post("/todos/harley/")
						.contentType(MediaType.APPLICATION_JSON)
						.content( mapper.writeValueAsString(newTodo))
						.accept(MediaType.APPLICATION_JSON)
					)
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.userName", is(newTodo.getUserName())))
					.andExpect(jsonPath("$.whatToDo", is(newTodo.getWhatToDo())))
					.andExpect(jsonPath("$.completed", is(newTodo.getCompleted())));
		
		mvc.perform(get("/todos/harley/" + newTodo.getId())
						.accept(MediaType.APPLICATION_JSON)
					)
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.userName", is(newTodo.getUserName())))
					.andExpect(jsonPath("$.whatToDo", is(newTodo.getWhatToDo())))
					.andExpect(jsonPath("$.completed", is(newTodo.getCompleted())));

	}
}