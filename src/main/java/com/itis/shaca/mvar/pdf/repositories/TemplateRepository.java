package com.itis.shaca.mvar.pdf.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.itis.shaca.mvar.pdf.entities.Template;

@Repository
public interface TemplateRepository extends CrudRepository<Template, Integer> {


}
