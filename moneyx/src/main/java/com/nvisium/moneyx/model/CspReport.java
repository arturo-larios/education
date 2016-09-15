package com.nvisium.moneyx.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "cspReports")
public class CspReport {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Version
	private Long version;

	@Column(name = "role_name")
	private String documentUri;

	@Column(name = "referrer")
	private String referrer;

	@Column(name = "blocked_uri")
	private String blockedUri;

	@Column(name = "violated_directive")
	private String violatedDirective;

	@Column(name = "original_policy")
	private String originalPolicy;

	@Column(name = "column_number")
	private String columnNumber;

	@Column(name = "line_number")
	private String lineNumber;

	@Column(name = "script_sample")
	private String scriptSample;

	@Column(name = "source_file")
	private String sourceFile;

	@Column(name = "status_code")
	private String statusCode;

	@Column(name = "effective_directive")
	private String effectiveDirective;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

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
