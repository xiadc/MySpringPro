package cn.itcast.goods.admin.admin.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.itcast.goods.admin.admin.domain.Admin;
import cn.itcast.goods.admin.admin.service.AdminService;

/** 
 * @author xiadc 
 * createtime：2017年4月19日 下午5:00:05 
 * 类说明 
 */

@Controller
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@RequestMapping("/loginAdmin")
	public ModelAndView loginAdmin(Admin formAdmin,HttpSession session){
		ModelAndView mv = new ModelAndView();
		
		Admin admin = adminService.login(formAdmin);
		
		if(admin == null){
			mv.addObject("msg", "用户名或密码错误！");
			mv.setViewName("/adminjsps/login.jsp");			
		}else{
			session.setAttribute("sessionAdmin", admin);
			mv.setViewName("/adminjsps/admin/index.jsp");			
		}
		return mv;
		
	}
}
