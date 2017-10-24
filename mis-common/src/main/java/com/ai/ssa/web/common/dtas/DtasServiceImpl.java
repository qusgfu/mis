package com.ai.ssa.web.common.dtas;

import java.io.File;
import java.io.InputStream;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

@Component
public class DtasServiceImpl implements DtasService {

	@Resource
	private SqlSessionTemplate sql;

	@Override
	public String test() throws Exception {
		String url = getUrl();
		Header[] headers = new Header[6];
		headers[0] = new BasicHeader(DtasConstant.HOST, url);
		headers[1] = new BasicHeader(DtasConstant.X_DTAS_PROTOCOLVERSION, "1.4");
		headers[2] = new BasicHeader(DtasConstant.X_DTAS_TIME, getSeconds());
		headers[3] = new BasicHeader(DtasConstant.X_DTAS_CHALLENGE, getUUID());
		headers[4] = new BasicHeader(DtasConstant.X_DTAS_CHECKSUMCALCULATINGORDER, DtasConstant.X_DTAS_PROTOCOLVERSION
				+ "," + DtasConstant.X_DTAS_TIME + "," + DtasConstant.X_DTAS_CHALLENGE);
		headers[5] = new BasicHeader(DtasConstant.X_DTAS_CHECKSUM, CalculateChecksum("", headers, ""));
		CloseableHttpClient httpClient = httpClientForSSL.getConnection();
		return httpClientForSSL.doGet(url + DtasConstant.TEST, headers, httpClient);
	}

	@Override
	public String register() throws Exception {
		String url = getUrl();
		Header[] headers = new Header[11];
		headers[0] = new BasicHeader(DtasConstant.HOST, url);
		headers[1] = new BasicHeader(DtasConstant.X_DTAS_PROTOCOLVERSION, "1.4");
		headers[2] = new BasicHeader(DtasConstant.X_DTAS_PRODUCTNAME, "");
		headers[3] = new BasicHeader(DtasConstant.X_DTAS_CLIENTHOSTNAME, "");
		headers[4] = new BasicHeader(DtasConstant.X_DTAS_CLIENTUUID, DtasConstant.CLIENTUUID);
		headers[5] = new BasicHeader(DtasConstant.X_DTAS_SOURCEID, "");
		headers[6] = new BasicHeader(DtasConstant.X_DTAS_SOURCENAME, "");
		headers[7] = new BasicHeader(DtasConstant.X_DTAS_TIME, getSeconds());
		headers[8] = new BasicHeader(DtasConstant.X_DTAS_CHALLENGE, getUUID());
		headers[9] = new BasicHeader(DtasConstant.X_DTAS_CHECKSUMCALCULATINGORDER,
				DtasConstant.X_DTAS_PROTOCOLVERSION + "," + DtasConstant.X_DTAS_PRODUCTNAME + ","
						+ DtasConstant.X_DTAS_CLIENTHOSTNAME + "," + DtasConstant.X_DTAS_CLIENTUUID + ","
						+ DtasConstant.X_DTAS_SOURCEID + "," + DtasConstant.X_DTAS_SOURCEID + ","
						+ DtasConstant.X_DTAS_TIME + "," + DtasConstant.X_DTAS_CHALLENGE);
		headers[10] = new BasicHeader(DtasConstant.X_DTAS_CHECKSUM, CalculateChecksum("", headers, ""));
		CloseableHttpClient httpClient = httpClientForSSL.getConnection();
		return httpClientForSSL.doGet(url + DtasConstant.REGISTER, headers, httpClient);
	}

	@Override
	public String unregister() throws Exception {
		String url = getUrl();
		Header[] headers = new Header[7];
		headers[0] = new BasicHeader(DtasConstant.HOST, url);
		headers[1] = new BasicHeader(DtasConstant.X_DTAS_PROTOCOLVERSION, "1.4");
		headers[2] = new BasicHeader(DtasConstant.X_DTAS_CLIENTUUID, DtasConstant.CLIENTUUID);
		headers[3] = new BasicHeader(DtasConstant.X_DTAS_TIME, getSeconds());
		headers[4] = new BasicHeader(DtasConstant.X_DTAS_CHALLENGE, getUUID());
		headers[5] = new BasicHeader(DtasConstant.X_DTAS_CHECKSUMCALCULATINGORDER,
				DtasConstant.X_DTAS_PROTOCOLVERSION + "," + DtasConstant.X_DTAS_CLIENTUUID + ","
						+ DtasConstant.X_DTAS_TIME + "," + DtasConstant.X_DTAS_CHALLENGE);
		headers[6] = new BasicHeader(DtasConstant.X_DTAS_CHECKSUM, CalculateChecksum("", headers, ""));
		CloseableHttpClient httpClient = httpClientForSSL.getConnection();
		return httpClientForSSL.doGet(url + DtasConstant.UNREGISTER, headers, httpClient);
	}

	@Override
	public String checkDuplicate(String fileSHA1) throws Exception {
		String url = getUrl();
		Header[] headers = new Header[9];
		headers[0] = new BasicHeader(DtasConstant.HOST, url);
		headers[1] = new BasicHeader(DtasConstant.CONTENT_TYPE, "text/plain");
		// headers[2] = new BasicHeader(DtasConstant.CONTENT_LENGTH, "");
		headers[3] = new BasicHeader(DtasConstant.X_DTAS_PROTOCOLVERSION, "1.4");
		headers[4] = new BasicHeader(DtasConstant.X_DTAS_CLIENTUUID, DtasConstant.CLIENTUUID);
		headers[5] = new BasicHeader(DtasConstant.X_DTAS_TIME, getSeconds());
		headers[6] = new BasicHeader(DtasConstant.X_DTAS_CHALLENGE, getUUID());
		headers[7] = new BasicHeader(DtasConstant.X_DTAS_CHECKSUMCALCULATINGORDER,
				DtasConstant.X_DTAS_PROTOCOLVERSION + "," + DtasConstant.X_DTAS_CLIENTUUID + ","
						+ DtasConstant.X_DTAS_TIME + "," + DtasConstant.X_DTAS_CHALLENGE);
		headers[8] = new BasicHeader(DtasConstant.X_DTAS_CHECKSUM, CalculateChecksum("", headers, ""));
		CloseableHttpClient httpClient = httpClientForSSL.getConnection();
		return httpClientForSSL.doPutV1(url + DtasConstant.CHECK_DUPLICATE_SAMPLE, headers, fileSHA1, httpClient);
	}

	@Override
	public String upload(String APIKey, InputStream inputStream, String fileSHA1, String fileType) throws Exception {
		String url = getUrl();
		Header[] headers = new Header[13];
		headers[0] = new BasicHeader(DtasConstant.HOST, url);
		headers[1] = new BasicHeader(DtasConstant.CONTENT_TYPE, "application/x-compressed");
		// headers[2] = new BasicHeader(DtasConstant.CONTENT_LENGTH, "");
		headers[3] = new BasicHeader(DtasConstant.X_DTAS_PROTOCOLVERSION, "1.4");
		headers[4] = new BasicHeader(DtasConstant.X_DTAS_CLIENTUUID, DtasConstant.CLIENTUUID);
		headers[5] = new BasicHeader(DtasConstant.X_DTAS_SOURCEID, "");
		headers[6] = new BasicHeader(DtasConstant.X_DTAS_SOURCENAME, "");
		headers[7] = new BasicHeader(DtasConstant.X_DTAS_ARCHIVE_SHA1, fileSHA1);
		headers[8] = new BasicHeader(DtasConstant.X_DTAS_ARCHIVE_FILENAME, getArchiveFilename(fileSHA1, fileType));
		headers[9] = new BasicHeader(DtasConstant.X_DTAS_TIME, getSeconds());
		headers[10] = new BasicHeader(DtasConstant.X_DTAS_CHALLENGE, getUUID());
		headers[11] = new BasicHeader(DtasConstant.X_DTAS_CHECKSUMCALCULATINGORDER,
				DtasConstant.X_DTAS_PROTOCOLVERSION + "," + DtasConstant.X_DTAS_CLIENTUUID + ","
						+ DtasConstant.X_DTAS_SOURCEID + "," + DtasConstant.X_DTAS_SOURCENAME + ","
						+ DtasConstant.X_DTAS_ARCHIVE_SHA1 + "," + DtasConstant.X_DTAS_ARCHIVE_FILENAME + ","
						+ DtasConstant.X_DTAS_TIME + "," + DtasConstant.X_DTAS_CHALLENGE);
		headers[12] = new BasicHeader(DtasConstant.X_DTAS_CHECKSUM, CalculateChecksum(APIKey, headers, ""));
		CloseableHttpClient httpClient = httpClientForSSL.getConnection();
		return httpClientForSSL.doPut(url + DtasConstant.UPLOAD_SAMPLE, headers, inputStream, httpClient);
	}

	@Override
	public String getReport() throws Exception {
		String url = getUrl();
		Header[] headers = new Header[9];
		headers[0] = new BasicHeader(DtasConstant.HOST, url);
		headers[1] = new BasicHeader(DtasConstant.X_DTAS_PROTOCOLVERSION, "1.4");
		headers[2] = new BasicHeader(DtasConstant.X_DTAS_CLIENTUUID, DtasConstant.CLIENTUUID);
		headers[3] = new BasicHeader(DtasConstant.X_DTAS_ARCHIVE_SHA1, "");
		headers[4] = new BasicHeader(DtasConstant.X_DTAS_ReportType, "");
		headers[5] = new BasicHeader(DtasConstant.X_DTAS_TIME, getSeconds());
		headers[6] = new BasicHeader(DtasConstant.X_DTAS_CHALLENGE, getUUID());
		headers[7] = new BasicHeader(DtasConstant.X_DTAS_CHECKSUMCALCULATINGORDER,
				DtasConstant.X_DTAS_PROTOCOLVERSION + "," + DtasConstant.X_DTAS_CLIENTUUID + ","
						+ DtasConstant.X_DTAS_ARCHIVE_SHA1 + "," + DtasConstant.X_DTAS_ReportType + ","
						+ DtasConstant.X_DTAS_TIME + "," + DtasConstant.X_DTAS_CHALLENGE);
		headers[8] = new BasicHeader(DtasConstant.X_DTAS_CHECKSUM, CalculateChecksum("", headers, ""));
		CloseableHttpClient httpClient = httpClientForSSL.getConnection();
		return httpClientForSSL.doGet(url + DtasConstant.GET_REPORT, headers, httpClient);
	}

	private String getUrl() {
		Map map = sql.selectOne("common.getSysCfg","DDAN_URL");
		String url = (String) map.get("value");
		return url;
	}

	private String getUUID() {
		UUID uid = UUID.randomUUID();
		return uid.toString();
	}

	private String getSeconds() {
		Long s = System.currentTimeMillis() / 1000;
		return s.toString();
	}

	private String CalculateChecksum(String APIKey, Header[] headers, String Body) throws Exception {
		StringBuffer sb = new StringBuffer(APIKey);
		for (int i = 0; i < headers.length; i++) {
			Header header = headers[i];
			String name = header.getName();
			if (name.contains("X-DTAS-") && !"X-DTAS-Checksum".equals(name)
					&& !"X-DTAS-ChecksumCalculatingOrder".equals(name)) {
				sb = sb.append(header.getValue());
			}
		}
		sb = sb.append(Body);
		return SHA1(sb.toString());
	}

	private String SHA1(String key) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(key.getBytes("UTF-8"));
		byte[] result = md.digest();

		StringBuffer sb = new StringBuffer();

		for (byte b : result) {
			int i = b & 0xff;
			if (i < 0xf) {
				sb.append(0);
			}
			sb.append(Integer.toHexString(i));
		}
		return sb.toString();
	}

	private String getArchiveFilename(String fileSHA1, String fileType) {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyMMdd-HHmmss");
		LocalDate date = LocalDate.now();
		String dateStr = date.format(f);
		return dateStr + "_" + fileSHA1 + fileType;
	}

}
