package com.itis.shaca.mvar.pdf.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.itis.shaca.mvar.pdf.entities.MVARReportInput;

@Repository
public interface MVARReportInputRepository  extends CrudRepository<MVARReportInput, Integer>{

	@Query("SELECT m FROM MVARReportInput m WHERE m.crashId =:crashId ORDER BY m.pageNumber ASC")
	List<MVARReportInput> findByCrashId(@Param("crashId") int crashId);
	
}
 