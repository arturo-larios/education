package com.nvisium.moneyx.service;

import java.util.List;

import com.nvisium.moneyx.model.CspReport;
import com.nvisium.moneyx.security.csp.CspReportRequest;

public interface AdminService {

	public void insertCspReport(CspReportRequest report);
	
	public List<CspReport> getCspReports();
}
