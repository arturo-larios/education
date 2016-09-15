package com.nvisium.moneyx.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;

import com.nvisium.moneyx.MoneyxLogger;
import com.nvisium.moneyx.model.CspReport;
import com.nvisium.moneyx.security.csp.CspReportRequest;
import com.nvisium.moneyx.service.impl.AdminServiceImpl;

@RequestMapping(value = "/admin")
@Controller
public class AdminController {

	@Autowired
	AdminServiceImpl adminService;

	@RequestMapping(value = "/submit-csp-report", method = RequestMethod.POST)
	@ResponseBody
	public String submitCspReport(@RequestBody CspReportRequest report) {
		adminService.insertCspReport(report);
		MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/admin/submit-csp-report");
		return "Violation reported!! Watch out!!";
	}

	@RequestMapping(value = "/get-csp-reports", method = RequestMethod.GET)
	@ResponseBody
	public List<CspReport> getCspReports() {
		MoneyxLogger.log("Session-"+ RequestContextHolder.getRequestAttributes().getSessionId() +":/admin/get-csp-report");
		return adminService.getCspReports();
	}
}

