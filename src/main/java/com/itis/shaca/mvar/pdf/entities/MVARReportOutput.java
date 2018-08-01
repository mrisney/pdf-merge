package com.itis.shaca.mvar.pdf.entities;

import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "mvar_reports")
@Data
public class MVARReportOutput {

	

	@Id
	@Column(name = "MVAR_REPORT_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MVAR_REPORTS_SEQ")
    @SequenceGenerator(sequenceName = "MVAR_REPORTS_SEQ", allocationSize = 1, name = "MVAR_REPORTS_SEQ")
	private Integer reportId;
	
	@Column(name = "crash_id")
	private Integer crashId;
	
	@Column(name = "report_mime")
	private String reportMime;
	
	@Column(name = "report_blob")
	private byte[] reportBlob;
	
	@Column(name = "last_modified")
	private Date lastModified;
	
	@Column(name = "total_pages")
	private Integer totalPages;
	
	
}
