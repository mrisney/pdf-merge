package com.itis.shaca.mvar.pdf.dao;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import oracle.jdbc.OracleBlob;

@Component
public class MVARReportDAO {

	private static final Logger logger = LoggerFactory.getLogger(MVARReportDAO.class);

	private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String DB_CONNECTION = "jdbc:oracle:thin:@(description=(address=(host=dev.itis-db4.com)(protocol=tcp)(port=1521))(connect_data=(sid=ORCL)(server=SHARED)))";;
	private static final String DB_USER = "os_hi_reports";
	private static final String DB_PASSWORD = "os_hi_reports";

	public void insertBlobViaSelectForUpdate(final Connection conn, final String tableName, final int id,
			final byte value[]) throws SQLException, IOException {
		try (final PreparedStatement pstmt = conn
				.prepareStatement(String.format("INSERT INTO %s (ID, VALUE) VALUES (?, EMPTY_BLOB())", tableName))) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		}

		try (final PreparedStatement pstmt = conn
				.prepareStatement(String.format("SELECT VALUE FROM %s WHERE ID = ? FOR UPDATE", tableName))) {
			pstmt.setInt(1, id);
			try (final ResultSet rset = pstmt.executeQuery()) {
				while (rset.next()) {
					final Blob blob = rset.getBlob(1);
					try (final OutputStream out = new BufferedOutputStream(blob.setBinaryStream(1L))) {
						out.write(value);
					}
				}
			}
		}
	}

	public static void insertMVARReport(int crashId, String mimeType, byte[] pdfData, int pages) throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement updatePreparedStatement = null;

		String insertStatementSQL = "INSERT INTO mvar_reports"
				+ "(mvar_report_id,crash_id,report_mime,total_pages,last_modified,report_blob) VALUES"
				+ "(mvar_reports_seq.nextval,?,?,?,?,?)";

		try {

			dbConnection = getDBConnection();

			OracleBlob tempBlob = (OracleBlob) dbConnection.createBlob();

			preparedStatement = dbConnection.prepareStatement(insertStatementSQL);
			preparedStatement.setInt(1, crashId);
			preparedStatement.setString(2, mimeType);
			preparedStatement.setInt(3, pages);
			preparedStatement.setTimestamp(4, getCurrentTimeStamp());

			OutputStream os = tempBlob.setBinaryStream(1);
			os.write(pdfData);

			preparedStatement.setBlob(5, tempBlob);
			// execute insert SQL statement
			preparedStatement.execute();
			preparedStatement.close();

			tempBlob.free();

			logger.debug("Record is inserted into mvar_reports table!");

		} catch (SQLException e) {

			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}

	}

	private static Connection getDBConnection() {

		Connection dbConnection = null;

		try {

			Class.forName(DB_DRIVER);

		} catch (ClassNotFoundException e) {

			logger.error(e.getMessage());

		}

		try {

			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
			return dbConnection;

		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return dbConnection;
	}

	private static java.sql.Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	}
}
