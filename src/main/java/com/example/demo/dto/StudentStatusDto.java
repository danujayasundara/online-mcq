package com.example.demo.dto;

public class StudentStatusDto {
	
	private String name;
    private boolean status;
    
	public StudentStatusDto(String name, boolean status) {
		super();
		this.name = name;
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
}
