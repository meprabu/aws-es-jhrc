package com.elasticSearch.esjhrc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elasticSearch.esjhrc.entity.ProfileDocument;
import com.elasticSearch.esjhrc.service.ProfileService;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileController {
	
	private ProfileService service;
	
	@Autowired
	public ProfileController(ProfileService service){
		this.service = service;
	}

	@GetMapping(path="/test")
	public String testController(){
		return "Holla Mundo!";
	}
	
	@PostMapping
	public ResponseEntity createDocument(@RequestBody ProfileDocument document) throws Exception{
		return new ResponseEntity(service.cerateProfileDocument(document), HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
    public ProfileDocument findById(@PathVariable String id) throws Exception {
        return service.findById(id);
    }
	
	@GetMapping("/findAll")
	public List<ProfileDocument> findAllDocuments() throws Exception {
		return service.findAll();
	}
	
}
