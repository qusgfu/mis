package com.ai.ssa.web.common.utils;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {
	private  static ObjectMapper objectMapper = new ObjectMapper(); 
	public static void main1(String[] args) {
		
	}
public static Object parse(String jsonStr) {
  Object obj = null;
	try {
	  obj = objectMapper.readValue(jsonStr, Object.class);
  } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  } 
	return obj;
}
public static <T> T parse(String jsonStr, Class<T> class1) throws JsonParseException, JsonMappingException, IOException{
  return objectMapper.readValue(jsonStr, class1); 
}
public static String toJSON(Object obj) throws JsonParseException, JsonMappingException, IOException{
  return objectMapper.writeValueAsString(obj); 
}

public static void main(String[] args) {
  String s = "{\"aa\":{}}";
  Map<String, Object> m = (Map)JSONUtil.parse(s);
  System.out.println(m);
  
}
}
