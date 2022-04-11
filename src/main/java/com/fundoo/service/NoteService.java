package com.fundoo.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fundoo.model.Note;
import com.fundoo.repository.NoteRepo;
import com.fundoo.util.Response;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NoteService {
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private NoteRepo noteRepo;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private Response response;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ElasticSearchService elasticSearchService;


	public Mono<Response> saveNote(Note note, String token) {
		String userId = restTemplate.getForObject("http://localhost:8081/usermicroservice/getuserid/" + token, String.class);
		note.setUserId(userId);
		return noteRepo.save(note).map(entity -> {
			elasticSearchService.createNote(entity);
			return new Response(200, "Note is saved successfully...!", entity);
		});
	}

	public Mono<Response> deleteNote(String id) {
		return noteRepo.findById(id).flatMap(validNote -> {
			elasticSearchService.deleteNote(validNote);
			return noteRepo.deleteById(id).map(res -> {
				return new Response(200, "Note is deleted successfully..!", res);
			});
		});
	}

	public Flux<Note> getAllNote(String token) {
		String userId = restTemplate.getForObject("http://localhost:8081/usermicroservice/getuserid/" + token, String.class);
		return noteRepo.findAllByUserId(userId);
	}

	public Mono<Response> updateNote(Note note, String token) {
		String userId = restTemplate.getForObject("http://localhost:8081/usermicroservice/getuserid/" + token, String.class);
		note.setUserId(userId);
		return noteRepo.save(note).map(updatedEntity -> {
			elasticSearchService.updateNote(updatedEntity);
			return new Response(200, "Note is updated successfully..!", updatedEntity);
		});
	}

	public List<Note> searchNote(String query, String token) throws IllegalArgumentException, UnsupportedEncodingException{
		return elasticSearchService.searchNotes(query, token);
		//return elasticSearchService.searchData();
	}

	public void emptyTrash() {
		Flux<Note> notesList = noteRepo.findAll();
		notesList.filter(p -> p.isInTrash()).doOnNext(note -> {
			System.out.println("Note deleted with id: " + note.getId());
			noteRepo.delete(note);});
	}

	public void sendNoteReminderMail() {
		Flux<Note> notesList = noteRepo.findAll();
		Date currentDate = new Date();
		notesList.doOnNext(note -> {
			if( note.getReminder() != null && currentDate.getTime() >= note.getReminder().getTime()) {
				System.out.println("Note reminder mail sent to user email");
				try {
					emailService.sendNoteRemainder(note);
					note.setReminder(null);
					noteRepo.save(note);
				} catch (UnsupportedEncodingException | MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public List<Note> fetchAllNoteTitles() {
		return elasticSearchService.getAllTitles();
	}

}
