package com.fundoo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.fundoo.model.Note;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NoteRepo extends ReactiveMongoRepository<Note, String>{

	Mono<Note> findById(String id);

	Flux<Note> findAllByUserId(String userId);


}
