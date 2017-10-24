package com.ai.ssa.web.common.process;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

public class DateConvert implements Converter {
	

    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String DATETIME_PATTERN_NO_SECOND = "yyyy-MM-dd HH:mm";

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final String MONTH_PATTERN = "yyyy-MM";

	@Override
	public Object convert(Class type, Object value) {

		if (value == null) {
			return null;
		}
		if (value instanceof Date) {
			return value;
		}
		if (value instanceof Long) {
			Long longValue = (Long) value;
			return new Date(longValue.longValue());
		}
		if (value instanceof String) {
			String dateStr = (String) value;
			Date endTime = null;
			try {
				String regexp1 = "([0-9]{4})-([0-1][0-9])-([0-3][0-9])T([0-2][0-9]):([0-6][0-9]):([0-6][0-9])";
				String regexp2 = "([0-9]{4})-([0-1][0-9])-([0-3][0-9])(/t)([0-2][0-9]):([0-6][0-9]):([0-6][0-9])";
				String regexp3 = "([0-9]{4})-([0-1][0-9])-([0-3][0-9])";
				if (dateStr.matches(regexp1)) {
					dateStr = dateStr.split("T")[0] + " " + dateStr.split("T")[1];
					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					endTime = sdf.parse(dateStr);
					return endTime;
				} else if (dateStr.matches(regexp2)) {
					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					endTime = sdf.parse(dateStr);
					return endTime;
				} else if (dateStr.matches(regexp3)) {
					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					endTime = sdf.parse(dateStr);
					return endTime;
				} else {
					return dateStr;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
	
	 /**
     * Convert String to Date
     *
     * @param value
     * @return
     * @throws ParseException
     */
    private Date doConvertToDate(Object value) throws ParseException {
        Date result = null;

        if (value instanceof String) {
            result = DateUtils.parseDate((String) value, new String[]{DATE_PATTERN, DATETIME_PATTERN,
                    DATETIME_PATTERN_NO_SECOND, MONTH_PATTERN});

            // all patterns failed, try a milliseconds constructor
            if (result == null && !StringUtils.isEmpty((String) value)) {

                try {
                    result = new Date(new Long((String) value).longValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } else if (value instanceof Object[]) {
            // let's try to convert the first element only
            Object[] array = (Object[]) value;

            if (array.length >= 1) {
                value = array[0];
                result = doConvertToDate(value);
            }

        } else if (Date.class.isAssignableFrom(value.getClass())) {
            result = (Date) value;
        }
        return result;
    }

    /**
     * Convert Date to String
     *
     * @param value
     * @return
     */
    private String doConvertToString(Object value) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_PATTERN);
        String result = null;
        if (value instanceof Date) {
            result = simpleDateFormat.format(value);
        }
        return result;
    }


}
