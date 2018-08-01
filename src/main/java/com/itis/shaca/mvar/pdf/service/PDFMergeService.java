package com.itis.shaca.mvar.pdf.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itis.shaca.mvar.pdf.dao.MVARReportDAO;
import com.itis.shaca.mvar.pdf.entities.MVARReportInput;
import com.itis.shaca.mvar.pdf.entities.MVARReportOutput;
import com.itis.shaca.mvar.pdf.repositories.MVARReportInputRepository;
import com.itis.shaca.mvar.pdf.repositories.MVARReportOutputRepository;

@Service
public class PDFMergeService {

	private static final Logger logger = LoggerFactory.getLogger(PDFMergeService.class);

	@Autowired
	private MVARReportInputRepository inputRepository;

	@Autowired
	private MVARReportOutputRepository outputRepository;

	@Autowired
	private MVARReportDAO reportDAO;

	@PersistenceContext
	protected EntityManager entityManager;

	@Transactional
	public void merge(int crashId) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			PDFMergerUtility mergePdf = new PDFMergerUtility();

			mergePdf.setDestinationFileName("/Users/marcrisney/Projects/itis/pdf-merge/merged.pdf");
			mergePdf.setDestinationStream(baos);
			List<MVARReportInput> mvarReports = inputRepository.findByCrashId(crashId);

			for (MVARReportInput report : mvarReports) {
				logger.debug("adding report : " + report.getReportName());
				mergePdf.addSource(report.getReportBlob().getBinaryStream());
			}

			mergePdf.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
			MVARReportOutput outputReport = new MVARReportOutput();
			outputReport.setCrashId(crashId);
			outputReport.setReportMime("application/pdf;charset=UTF-8");
			outputReport.setTotalPages(mvarReports.size());

			byte[] pdfBytes = baos.toByteArray();
			outputReport.setReportBlob(pdfBytes);
			MVARReportDAO.insertMVARReport(outputReport.getCrashId(), outputReport.getReportMime(), pdfBytes,
					mvarReports.size());

			logger.debug("Documents merged, file size = " + pdfBytes.length);
		} catch (Exception e) {
			logger.error(e.toString());
		}

	}

	public ByteArrayOutputStream download(int crashId) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			PDFMergerUtility mergePdf = new PDFMergerUtility();
			mergePdf.setDestinationFileName("/Users/marcrisney/Projects/itis/pdf-merge/merged.pdf");
			mergePdf.setDestinationStream(baos);

			List<MVARReportInput> mvarReports = inputRepository.findByCrashId(crashId);

			for (MVARReportInput report : mvarReports) {
				logger.debug("adding report : " + report.getReportName());
				mergePdf.addSource(report.getReportBlob().getBinaryStream());
			}

			mergePdf.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
			baos.flush();
			byte[] pdfBytes = baos.toByteArray();
			baos.close();
			logger.debug("Documents merged, file size = " + pdfBytes.length);

			logger.debug("Documents merged");
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return baos;
	}

	public ByteArrayOutputStream getReport(int crashId) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Optional<MVARReportInput> report = inputRepository.findById(crashId);

		if (report.isPresent()) {

			InputStream in = report.get().getReportBlob().getBinaryStream();

			int length = (int) report.get().getReportBlob().length();
			int bufferSize = 1024;

			byte[] buffer = new byte[bufferSize];

			while ((length = in.read(buffer)) != -1) {
				logger.debug("writing " + length + " bytes");
				baos.write(buffer, 0, length);
			}

			in.close();
		}

		return baos;
	}
}