package cn.itcast.goods.user.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;










import cn.itcast.goods.user.domain.User;
import cn.itcast.goods.user.service.UserService;

/** 
 * @author xiadc 
 * createtime：2017年4月15日 下午4:58:47 
 * 类说明 
 */
@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/login")
	public ModelAndView login(User formUser,HttpSession session,HttpServletResponse res) throws UnsupportedEncodingException{
		
		ModelAndView mv = new ModelAndView();
		Map<String,String> errorMap =validatelogin(formUser,session);
		if(!errorMap.isEmpty()){
			mv.addObject("form",formUser);
			mv.addObject("errors", errorMap);
			mv.setViewName("/jsps/user/login.jsp");			
			return mv;
		}
		
		User user = userService.login(formUser);
		
		if(user ==null){
			mv.addObject("user", formUser);
			mv.addObject("msg", "用户名或密码错误！");
			mv.setViewName("/jsps/user/login.jsp"); 
		}else if(!user.isStatus()){//激活码未激活
			mv.addObject("user", formUser);
			mv.addObject("msg", "账号未激活！");
			mv.setViewName("/jsps/user/login.jsp");
 		}else{
			session.setAttribute("sessionUser", user);
			String loginname = user.getLoginname();
			loginname = URLEncoder.encode(loginname,"utf-8");
			Cookie cookie = new Cookie("loginname", loginname);
			cookie.setMaxAge(3600*30);
			res.addCookie(cookie);
			mv.setViewName("/index.jsp");
		}
		
		return mv;
	}
	
	/**登陆校验
	 * @param formUser
	 * @param session
	 * @return
	 */
	private Map<String,String> validatelogin(User formUser,HttpSession session){
		Map<String,String> errorMap = new  HashMap<String, String>();
		//1.校验登录名
		String loginname = formUser.getLoginname();
		if(loginname==null||loginname.trim().isEmpty()){
			errorMap.put("loginname","用户名不能为空！");
		}else if(loginname.length()<3||loginname.length()>20){
			errorMap.put("loginname", "用户名长度必须介于3到20之间");
		}
		//2.校验登陆密码
		String loginpass = formUser.getLoginpass();
		if(loginpass==null||loginpass.trim().isEmpty()){
			errorMap.put("loginpass","密码不能为空！");
		}else if(loginpass.length()<3||loginpass.length()>20){
			errorMap.put("loginpass", "密码长度必须介于3到20之间");
		}
		//5.校验验证码
		String verifyCode = formUser.getVerifycode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode ==null||verifyCode.trim().isEmpty()){
			errorMap.put("verifycode", "验证码不能为空！");
		}else if(!verifyCode.equalsIgnoreCase(vcode)){
			errorMap.put("verifycode", "验证码错误！");
		}
		
		return errorMap;	
	}
	
	//登录验证码正确性验证
	@RequestMapping("/ajaxValidateVerifyCode")
	public void ajaxValidateVerifyCode(String verifycode,HttpSession session,HttpServletResponse res) throws IOException{
		String sessionVerifyCode = (String) session.getAttribute("vCode");
		if(sessionVerifyCode.equalsIgnoreCase(verifycode)){
			res.getWriter().print(true);
		}else{
			res.getWriter().print(false);
		}
		
	}
	
	
	@RequestMapping("/ajaxValidateLoginname")
	public void ajaxValidateLoginname(String loginname,HttpServletResponse res) throws IOException{
		boolean b= userService.ajaxValidateLoginname(loginname);
		res.getWriter().print(b);
	}
	
	@RequestMapping("/ajaxValidateEmail")
	public void ajaxValidateEmail(String email,HttpServletResponse res) throws IOException{
		boolean b= userService.ajaxValidateLoginname(email);
		res.getWriter().print(b);
	}
	
	@RequestMapping("/regist")
	public ModelAndView regist(User formUser,HttpSession session){
		//1.表单信息转成javabean
		//User formUser = CommonUtils.toBean(request.getParameterMap(), User.class);
		//2.验证表单信息,如果检验失败，
		Map<String,String> errorMap = this.validateRegist(formUser, session);
		
		ModelAndView mv  = new ModelAndView();
		
		if(!errorMap.isEmpty()){
			//request.setAttribute("form", formUser);
			//request.setAttribute("errors", errorMap);
			
			mv.addObject("form", formUser);
			mv.addObject("errors", errorMap);
			mv.setViewName("/jsps/user/regist1.jsp");
			
			return mv;
			//return "f:/jsps/user/regist1.jsp";
		}
		//3.存储user信息，转给service
		userService.regist(formUser);
		//4.获取注册结果，转跳成功或者失败页面
		//request.setAttribute("code", "success");
		////request.setAttribute("msg", "注册功能，请到邮箱激活");
		
		mv.addObject("code", "success");
		mv.addObject("msg", "注册功能，请到邮箱激活");
		mv.setViewName("/jsps/msg.jsp");
		
		return mv;
		//return "f:/jsps/msg.jsp";
	}
	
	/**注册校验
	 * @param formUser
	 * @param session
	 * @return
	 */
	private Map<String,String> validateRegist(User formUser,HttpSession session){
		Map<String,String> errorMap = new  HashMap<String, String>();
		//1.校验登录名
		String loginname = formUser.getLoginname();
		if(loginname==null||loginname.trim().isEmpty()){
			errorMap.put("loginname","用户名不能为空！");
		}else if(loginname.length()<3||loginname.length()>20){
			errorMap.put("loginname", "用户名长度必须介于3到20之间");
		}else if(!userService.ajaxValidateLoginname(loginname)){
			errorMap.put("loginname", "用户名已被注册！");
		}
		
		//2.校验登陆密码
		String loginpass = formUser.getLoginpass();
		if(loginpass==null||loginpass.trim().isEmpty()){
			errorMap.put("loginpass","密码不能为空！");
		}else if(loginpass.length()<3||loginpass.length()>20){
			errorMap.put("loginpass", "密码长度必须介于3到20之间");
		}
		
		//3.确认密码校验
		String reloginpass = formUser.getReloginpass();
		if(reloginpass==null||reloginpass.trim().isEmpty()){
			errorMap.put("reloginpass","密码不能为空！");
		}else if(!reloginpass.equals(loginpass)){
			errorMap.put("reloginpass", "两次输入密码不一致！");
		}
		
		//4.校验email
		String email = formUser.getEmail();
		if(email==null||email.trim().isEmpty()){
			errorMap.put("email","email不能为空！");
		}else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")){
			errorMap.put("email", "用户名长度必须介于3到20之间");
		}else if(!userService.ajaxValidateEmail(email)){
			errorMap.put("email", "email已被注册！");
		}
		
		//5.校验验证码
		String verifyCode = formUser.getVerifycode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode ==null||verifyCode.trim().isEmpty()){
			errorMap.put("verifycode", "验证码不能为空！");
		}else if(!verifyCode.equalsIgnoreCase(vcode)){
			errorMap.put("verifycode", "验证码错误！");
		}
		
		return errorMap;	
	}				
}
