package com.itis.shaca.mvar.pdf.entities;

import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "templates")
@Data
public class Template {

	@Id
	private Integer id;

	@Lob
	@Column(name = "blob_file")
	private Blob blobFile;

	@Column(name = "filename")
	private String fileName;
		
	@Column(name = "description")
	private String description;

	@Column(name = "created")
	private Date created;
	
}
