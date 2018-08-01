package com.itis.shaca.mvar.pdf.entities;

import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "mvar_reports_in")
@Data
public class MVARReportInput {
	
	@Id
	@Column(name = "mvar_report_in_id")
	private Integer reportId;
	
	@Column(name = "crash_id")
	private Integer crashId;

	@Column(name = "report_name")
	private String reportName;
	
	@Column(name = "page_no")
	private Integer pageNumber;
	
	@Column(name = "report_mime")
	private String reportMime;
	
	@Column(name = "report_blob")
	private Blob reportBlob;
	
	@Column(name = "last_modified")
	private Date lastModified;
	
	@Column(name = "total_pages")
	private Integer totalPages;
	
	
	
	
}
