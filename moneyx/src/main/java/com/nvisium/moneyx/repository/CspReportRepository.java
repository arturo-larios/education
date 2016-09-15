package com.nvisium.moneyx.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nvisium.moneyx.model.CspReport;

@Repository
@Qualifier(value = "cspReportRepository")
public interface CspReportRepository extends
		CrudRepository<CspReport, Long> {
	
	List<CspReport> findAll();

}
