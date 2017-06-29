package com.xdc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.xdc.po.User;
import com.xdc.service.UserService;

/** 
 * @author xiadc 
 * createtime：2017年4月10日 下午4:15:42 
 * 类说明 
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("getUser")
	public ModelAndView getUser(){
		
		List<User> list =  userService.findUser();
		ModelAndView mv = new ModelAndView();
		for(User user :list){
			System.out.println(user);
		}
		//System.out.println(list);
		mv.addObject("list", list);
		mv.setViewName("showUser");
		return mv;
		
	}
}
