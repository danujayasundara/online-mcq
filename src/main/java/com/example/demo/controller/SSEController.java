package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

import com.example.demo.dto.StudentStatusDto;
import com.example.demo.service.UserServiceImpl;

public class SSEController {
	
	@Autowired
    private UserServiceImpl userServiceImpl; 

	/*@GetMapping("/streamStudents")
    public Flux<ServerSentEvent<StudentStatusDto>> streamStudents() {
        Flux<StudentStatusDto> studentsFlux = userServiceImpl.getStudentsFlux(); 
        return studentsFlux.map(student -> ServerSentEvent.builder(student).build());
    }*/

}
