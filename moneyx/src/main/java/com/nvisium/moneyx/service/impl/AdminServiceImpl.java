package com.nvisium.moneyx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.nvisium.moneyx.model.CspReport;
import com.nvisium.moneyx.repository.CspReportRepository;
import com.nvisium.moneyx.security.SecurityUtils;
import com.nvisium.moneyx.security.csp.CspReportRequest;
import com.nvisium.moneyx.service.AdminService;

@Service
@Qualifier("adminService")
public class AdminServiceImpl implements AdminService {

	@Autowired
	SecurityUtils security;

	// @Autowired
	// AdminRepository adminRepository;

	@Autowired
	CspReportRepository cspReportRepository;

	@Override
	public void insertCspReport(CspReportRequest report) {
		cspReportRepository.save(report.asModel());
	}

	@Override
	public List<CspReport> getCspReports() {
		return cspReportRepository.findAll();
	}

}
