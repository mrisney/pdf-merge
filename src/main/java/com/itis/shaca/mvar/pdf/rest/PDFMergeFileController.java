package com.itis.shaca.mvar.pdf.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itis.shaca.mvar.pdf.service.PDFMergeService;

@RestController
public class PDFMergeFileController {

	@Autowired
	private PDFMergeService pdfMergeService;

	private static final Logger logger = LoggerFactory.getLogger(PDFMergeFileController.class);

	@RequestMapping(value = "/merge/{crashId}", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity merge(@PathVariable("crashId") final Integer crashId) {
		try {
			pdfMergeService.merge(crashId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/download/{crashId}", method = RequestMethod.GET, produces = "application/pdf")
	public void getDownload(@PathVariable("crashId") final Integer crashId, HttpServletResponse response)
			throws IOException {

		ByteArrayOutputStream outputStream = pdfMergeService.download(crashId);
		outputStream.writeTo(response.getOutputStream());

		String fileName = crashId+"-mvar.report.pdf";
		// Set the content type and attachment header.
		response.addHeader("Content-disposition", "attachment;filename="+fileName);
		response.setContentType("application/pdf");
		response.setContentLength(outputStream.toByteArray().length);

	}
}
