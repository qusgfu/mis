package com.ai.ssa.web.common.dtas;

public class DtasConstant {

	// Headers
	public final static String HOST = "Host";
	public final static String CONTENT_TYPE = "Content-Type";
	public final static String CONTENT_LENGTH = "Content-Length";
	public final static String X_DTAS_PROTOCOLVERSION = "X-DTAS-ProtocolVersion";
	public final static String X_DTAS_PRODUCTNAME = "X-DTAS-ProductName";
	public final static String X_DTAS_CLIENTHOSTNAME = "X-DTAS-ClientHostname";
	public final static String X_DTAS_CLIENTUUID = "X-DTAS-ClientUUID";
	public final static String X_DTAS_SOURCEID = "X-DTAS-SourceID";
	public final static String X_DTAS_SOURCENAME = "X-DTAS-SourceName";
	public final static String X_DTAS_ARCHIVE_SHA1 = "X-DTAS-Archive-SHA1";
	public final static String X_DTAS_ARCHIVE_FILENAME = "X-DTAS-Archive-Filename";
	public final static String X_DTAS_ReportType = "X-DTAS-ReportType";
	public final static String X_DTAS_TIME = "X-DTAS-Time";
	public final static String X_DTAS_CHALLENGE = "X-DTAS-Challenge";
	public final static String X_DTAS_CHECKSUMCALCULATINGORDER = "X-DTAS-ChecksumCalculatingOrder";
	public final static String X_DTAS_CHECKSUM = "X-DTAS-Checksum";

	// restful api to DTAS
	public final static String TEST = "/test";
	public final static String REGISTER = "/register";
	public final static String UNREGISTER = "/unregister";
	public final static String CHECK_DUPLICATE_SAMPLE = "/check_duplicate_sample";
	public final static String UPLOAD_SAMPLE = "/upload_sample";
	public final static String GET_REPORT = "/get_report";
	public final static String GET_BLACK_LISTS = "/get_black_lists";
	public final static String GET_SAMPLE_LIST = "/get_sample_list";

	// represent of this client
	public final static String CLIENTUUID = "7429668d-66d2-4ec4-83c7-a34c8537d14a";
}
