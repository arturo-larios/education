package com.nvisium.moneyx.security.csp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nvisium.moneyx.model.CspReport;

public class CspReportRequest {
	@JsonProperty("csp-report")
	private CspReportBody cspReport;

	public CspReportBody getCspReport() {
		return cspReport;
	}

	public void setCspReport(CspReportBody cspReport) {
		this.cspReport = cspReport;
	}
	
	public CspReport asModel() {
		CspReport report = new CspReport();
		report.setDocumentUri(cspReport.getDocumentUri());
		report.setReferrer(cspReport.getReferrer());
		report.setBlockedUri(cspReport.getBlockedUri());
		report.setViolatedDirective(cspReport.getViolatedDirective());
		report.setOriginalPolicy(cspReport.getOriginalPolicy());
		report.setColumnNumber(cspReport.getColumnNumber());
		report.setLineNumber(cspReport.getLineNumber());
		report.setScriptSample(cspReport.getScriptSample());
		report.setSourceFile(cspReport.getSourceFile());
		report.setStatusCode(cspReport.getStatusCode());
		report.setEffectiveDirective(cspReport.getEffectiveDirective());
		return report;
	}

	public class CspReportBody {

		@JsonProperty("document-uri")
		private String documentUri;

		@JsonProperty("referrer")
		private String referrer;

		@JsonProperty("blocked-uri")
		private String blockedUri;

		@JsonProperty("violated-directive")
		private String violatedDirective;

		@JsonProperty("original-policy")
		private String originalPolicy;

		@JsonProperty("column-number")
		private String columnNumber;

		@JsonProperty("line-number")
		private String lineNumber;

		@JsonProperty("script-sample")
		private String scriptSample;
		
		@JsonProperty("source-file")
		private String sourceFile;
		
		@JsonProperty("status-code")
		private String statusCode;
		
		@JsonProperty("effective-directive")
		private String effectiveDirective;
		
		public String getDocumentUri() {
			return documentUri;
		}

		public void setDocumentUri(String documentUri) {
			this.documentUri = documentUri;
		}

		public String getReferrer() {
			return referrer;
		}

		public void setReferrer(String referrer) {
			this.referrer = referrer;
		}

		public String getBlockedUri() {
			return blockedUri;
		}

		public void setBlockedUri(String blockedUri) {
			this.blockedUri = blockedUri;
		}

		public String getViolatedDirective() {
			return violatedDirective;
		}

		public void setViolatedDirective(String violatedDirective) {
			this.violatedDirective = violatedDirective;
		}

		public String getOriginalPolicy() {
			return originalPolicy;
		}

		public void setOriginalPolicy(String originalPolicy) {
			this.originalPolicy = originalPolicy;
		}

		public String getColumnNumber() {
			return columnNumber;
		}

		public void setColumnNumber(String columnNumber) {
			this.columnNumber = columnNumber;
		}

		public String getLineNumber() {
			return lineNumber;
		}

		public void setLineNumber(String lineNumber) {
			this.lineNumber = lineNumber;
		}

		public String getScriptSample() {
			return scriptSample;
		}

		public void setScriptSample(String scriptSample) {
			this.scriptSample = scriptSample;
		}

		public String getSourceFile() {
			return sourceFile;
		}

		public void setSourceFile(String sourceFile) {
			this.sourceFile = sourceFile;
		}

		public String getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(String statusCode) {
			this.statusCode = statusCode;
		}

		public String getEffectiveDirective() {
			return effectiveDirective;
		}

		public void setEffectiveDirective(String effectiveDirective) {
			this.effectiveDirective = effectiveDirective;
		}
	}
}
