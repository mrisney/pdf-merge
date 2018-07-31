package com.itis.shaca.mvar.pdf.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itis.shaca.mvar.pdf.entities.Template;
import com.itis.shaca.mvar.pdf.repositories.TemplateRepository;

@Service
public class PDFMergeService {

	private static final Logger logger = LoggerFactory.getLogger(PDFMergeService.class);

	@Autowired
	private TemplateRepository repository;

	public ByteArrayOutputStream merge() throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			PDFMergerUtility mergePdf = new PDFMergerUtility();
			mergePdf.setDestinationFileName("/Users/marcrisney/Projects/itis/pdf-merge/merged.pdf");
			mergePdf.setDestinationStream(baos);

			Optional<Template> templateA = repository.findById(1);
			if (templateA.isPresent()) {
				mergePdf.addSource(templateA.get().getBlobFile().getBinaryStream());
			}

			Optional<Template> templateB = repository.findById(2);
			if (templateB.isPresent()) {
				mergePdf.addSource(templateB.get().getBlobFile().getBinaryStream());
			}

			mergePdf.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());

		
			
			logger.debug("Documents merged");
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return baos;
	}
}