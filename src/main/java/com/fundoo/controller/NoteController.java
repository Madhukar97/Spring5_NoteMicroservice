package com.fundoo.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fundoo.model.Note;
import com.fundoo.service.NoteService;
import com.fundoo.util.Response;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@CrossOrigin(origins="http://localhost:3000")
@RequestMapping("/notemicroservice")
public class NoteController {

	@Autowired
	private NoteService noteService;

	@PostMapping("/users/saveNote/")
	public Mono<Response> saveNote( @RequestHeader String token, @RequestBody Note note){
		Mono<Response> response = noteService.saveNote(note , token);
		return response;
	}

	@DeleteMapping("/users/{id}")
	public Mono<Response> deleteNote(@PathVariable String id){
		Mono<Response> response = noteService.deleteNote(id);
		return response;
	}

	@GetMapping("/users/")
	public Flux<Note> getAllNote(@RequestHeader String token){
		Flux<Note> notes = noteService.getAllNote(token);
		return notes;
	}

	@PutMapping("users/")
	public Mono<Response> updateNote( @RequestHeader String token, @RequestBody Note note){
		Mono<Response> response = noteService.updateNote(note , token);
		return response;
	}

	@GetMapping("/note/search/{query}")
	public ResponseEntity<List<Note>> searchNotes(@RequestHeader String token, @PathVariable String query) throws IllegalArgumentException, UnsupportedEncodingException{
		List<Note> notes = noteService.searchNote(query, token);
		return new ResponseEntity<>(notes, HttpStatus.OK);
	}
	
	@GetMapping("/note/search")
	public ResponseEntity<List<Note>> getAllNoteTitles(){
		List<Note> noteTitles = noteService.fetchAllNoteTitles();
		return new ResponseEntity<List<Note>>(noteTitles, HttpStatus.OK);
	}

}
