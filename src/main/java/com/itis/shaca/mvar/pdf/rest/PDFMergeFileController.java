package com.itis.shaca.mvar.pdf.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itis.shaca.mvar.pdf.service.PDFMergeService;

@RestController
public class PDFMergeFileController {

	@Autowired
	private PDFMergeService pdfMergeService;

	private static final Logger logger = LoggerFactory.getLogger(PDFMergeFileController.class);

	@RequestMapping(value = "/merge", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity merge() {
		try {
			pdfMergeService.merge();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void getDownload(HttpServletResponse response) throws IOException {


		ByteArrayOutputStream outputStream = pdfMergeService.merge();
		outputStream.writeTo(response.getOutputStream());

		// Set the content type and attachment header.
		response.addHeader("Content-disposition", "attachment;filename=merge.pdf");
		response.setContentType("application/pdf");
		response.setContentLength(outputStream.toByteArray().length);

	}

}
