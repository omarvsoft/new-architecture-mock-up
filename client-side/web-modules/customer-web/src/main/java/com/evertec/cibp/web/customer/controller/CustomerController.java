package com.evertec.cibp.web.customer.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.evertec.cibp.web.authentication.model.User;


@Controller
public class CustomerController {
	
	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	
	@RequestMapping("/user")
    public String user(Model model, HttpServletRequest request) {
		
		String token = getToken(request);
		
		System.out.println("Token in Cookie " + token);
		
		User userDeserialized = (User) getValue(token);
        
		request.getCookies();
		
        model.addAttribute("model", userDeserialized);
		
		return "user";
	}
	
	private String getToken(HttpServletRequest request) {
		
		String token = "";
		
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
		 for (Cookie cookie : cookies) {
		   if (cookie.getName().equals("TOKEN")) {
			   token = cookie.getValue();
		    }
		  }
		}
		
		return token;
	}
	
	public Object getValue( final String key ) {
	    return redisTemplate.opsForValue().get( key );
	}

	public void setValue( final String key, final Object value ) {
		redisTemplate.opsForValue().set( key, value );
	}

}
