package com.ai.ssa.web.common.utils;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	private static final Log log = LogFactory.getLog(ExcelUtils.class);
	private static final String RESOURCE_HEAD_FIELD = "IP地址,姓名,联系方式,email,所属机构,登录名,用户角色,名单类型,内容";
	private static final HashMap headAndReg = new HashMap();

	public static Map readExcel(InputStream excel) throws Exception {
		Map resultMap = new HashMap();
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		List<Map<String, String>> wrong = new ArrayList();
		try {
			Workbook workbook = null;

			try {
				workbook = new XSSFWorkbook(excel);
			} catch (Exception e) {
				workbook = new HSSFWorkbook(excel);
			}
			for (int j = 0; j < 1; j++) {
				Sheet sheet = workbook.getSheetAt(j);

				Row row;
				Map<String, String> rows;
				if (sheet != null && sheet.getLastRowNum() > 0) {
					// 先获取sheet行数
					int iRowCount = sheet.getPhysicalNumberOfRows();

					Map<Integer, String> m = new HashMap<Integer, String>();
					// 获取数据
					for (int i = 0; i < iRowCount; i++) {
						row = sheet.getRow(i);
						int cellNum = row.getLastCellNum();
						if (i == 0) {
							for (int iCol = 0; iCol < cellNum; iCol++) {
								if (row != null) {
									String cv = getCellValue(row.getCell(iCol));
									if (!"-".equals(cv) && !Tools.isNull(cv)) {
										String h = contain(cv.replace(" ", ""));
										if (!Tools.isNull(h)) {
											m.put(iCol, h);
										}
									}
								}
							}
						} else {
							rows = new HashMap<String, String>(cellNum);
							StringBuilder sb = new StringBuilder("第" + i + "行：");
							boolean flag = false;
							for (int iCol = 0; iCol < cellNum; iCol++) {
								if (m.containsKey(iCol) && row != null) {
									String cellValue = getCellValue(row.getCell(iCol)).replace(" ", "");
									Pattern p = Pattern.compile((String) headAndReg.get(m.get(iCol)));
									Matcher matcher = p.matcher(cellValue);
									if (matcher.matches()) {
										rows.put(m.get(iCol), cellValue);
									} else {
										sb.append(cellValue).append(",");
										flag = true;
										break;
									}

								}
							}
							if (flag) {
								Map map = new HashMap();
								map.put("error", sb.append("格式错误！").toString());
								wrong.add(map);

							} else {
								listMap.add(rows);
							}
						}

					}
					// }
				} else {
					resultMap.put("error", "empty001");
					resultMap.put("errmsg", "表格为空！");
				}

			}
		} catch (Exception e) {
			// log.error(e);
			throw e;
		}
		if (wrong.size() > 0) {
			resultMap.put("error", "format");
			resultMap.put("wrongList", wrong);
			return resultMap;
		} else if (wrong.size() <= 0 && listMap.size() <= 0) {
			resultMap.put("error", "empty");
			resultMap.put("errmsg", "表格为空！");
		} else {
			resultMap.put("error", "success");
			resultMap.put("list", listMap);
		}
		System.out.println(resultMap);
		return resultMap;
	}

	private static String getCellValue(Cell cell) {
		if (cell == null) {
			return "";
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
			return Tools.isNull(cell.getStringCellValue()) ? "-" : cell.getStringCellValue();
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			DecimalFormat df = new DecimalFormat("0");
			return String.valueOf(df.format(cell.getNumericCellValue()));
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
			return "";
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
			String sValue = "";

			try {
				sValue = String.valueOf(cell.getNumericCellValue());
			} catch (Exception e) {
				sValue = cell.getStringCellValue();
			}

			return sValue;
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_ERROR) {
			return String.valueOf(cell.getErrorCellValue());
		} else {
			return "";
		}
	}

	/**
	 * 匹配表头信息
	 * 
	 * @param cv
	 * @param headMap
	 * @return
	 */
	private static String contain(String cv) {
		String head = "";
		if (headAndReg.containsKey(cv)) {
			head = (String) headAndReg.get(cv);
		}
		return head;
	}

	static {
		headAndReg.put("IP地址", "org_ip");
		headAndReg.put("姓名", "name");
		headAndReg.put("联系方式", "phone");
		headAndReg.put("电子邮件", "email");
		headAndReg.put("所属机构", "org_name");
		headAndReg.put("登录名", "login_name");
		headAndReg.put("用户角色", "role_name");
		headAndReg.put("名单类型", "list_type_name");
		headAndReg.put("内容", "list_value");
		headAndReg.put("org_ip",
				"^((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))).){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))$");
		headAndReg.put("name", "^([\u4E00-\u9FA5]{2,8})$");
		headAndReg.put("phone", "^([1][3,4,5,7,8][0-9]{9})|([0][1-9]{2,3}-[0-9]{5,10})|([1-9]{1}[0-9]{5,8})$");
		headAndReg.put("email", "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$");
		headAndReg.put("org_name", "^([\u4E00-\u9FA5]{1,10})$");
		headAndReg.put("login_name", "^[a-zA-Z0-9_]{3,16}$");
		headAndReg.put("role_name", "^[a-zA-Z0-9\u4e00-\u9fa5]+$");
		headAndReg.put("list_type_name", "^[\\s\\S]*$");
		headAndReg.put("list_value", "^[\\s\\S]*$");
	}

}
