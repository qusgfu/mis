package cel.logweb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/cel/hello")
public class HelloController {
	@RequestMapping("/hello")
	public String begin(HttpServletRequest request, HttpServletResponse response) { 
		 return "hello world";  
	}
}
