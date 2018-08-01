package com.itis.shaca.mvar.pdf.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.itis.shaca.mvar.pdf.entities.MVARReportOutput;

@Repository
public interface MVARReportOutputRepository  extends CrudRepository<MVARReportOutput, Integer>{

}

