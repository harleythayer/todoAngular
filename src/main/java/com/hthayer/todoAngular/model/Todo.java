package com.hthayer.todoAngular.model;

import org.springframework.data.annotation.Id;

public class Todo {

    @Id
    private String id;

    private String userName;
    private String whatToDo;
    private boolean completed;
    
    public Todo( ) { }
    
    public Todo( String id, String userName, String whatToDo, boolean completed ) {
    	this.id = id;
    	this.userName = userName;
    	this.whatToDo = whatToDo;
    	this.completed = completed;
    }
    
    public Todo( String userName, String whatToDo, boolean completed ) {
    	this.userName = userName;
    	this.whatToDo = whatToDo;
    	this.completed = completed;
    }

    public String getId( ) {
    	return id;
    }
    
    public void setId( String id ) {
    	this.id = id;
    }
    
	/**
	 * @return the name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param name the name to set
	 */
	public void setUserName(String name) {
		this.userName = name;
	}

	/**
	 * @return the whatToDo
	 */
	public String getWhatToDo() {
		return whatToDo;
	}

	/**
	 * @param whatToDo the whatToDo to set
	 */
	public void setWhatToDo(String whatToDo) {
		this.whatToDo = whatToDo;
	}

	/**
	 * @return the completed
	 */
	public boolean getCompleted() {
		return completed;
	}

	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

}