package com.fundoo.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection = "Note")
public class Note {

	@Id
	private String id;

	private String title;

	private String content;

	private String color;

	private boolean isArchived;

	private boolean inTrash;

	private Date reminder;

	private String userId;
}
