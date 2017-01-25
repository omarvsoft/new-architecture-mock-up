package com.evertec.cibp.web.authentication.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.evertec.cibp.web.authentication.model.User;

@Controller
public class LoginController {
	
	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	
	private static final String token = "500cd831-b431-4165-829e-4cd8dab1e753";
	
	@RequestMapping("/validate")
    public String greeting(@RequestParam(value="userName") String userName, @RequestParam(value="age") Integer age, 
    		Model model, HttpServletResponse response) {
		
		Cookie cToken = new Cookie("TOKEN", token);
		cToken.setMaxAge(-1);
        
		User user = new User();
		
		user.setUserName(userName);
		user.setAge(age);
        
        this.setValue(token, user);
        
        User userDeserialized = (User) getValue(token);
        
        response.addCookie(cToken);
        model.addAttribute("name", userDeserialized.getUserName());
        
        return "login";
    }
	
	public Object getValue( final String key ) {
	    return redisTemplate.opsForValue().get( key );
	}

	public void setValue( final String key, final Object value ) {
		redisTemplate.opsForValue().set( key, value );
	}

}
