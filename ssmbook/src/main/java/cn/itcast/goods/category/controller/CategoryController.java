package cn.itcast.goods.category.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.category.service.CategoryService;

/** 
 * @author xiadc 
 * createtime：2017年4月17日 上午11:56:29 
 * 类说明 
 */
@Controller
public class CategoryController {

	@Autowired 
	private CategoryService categoryService;
	
	@RequestMapping("/findAll")
	public ModelAndView findAll(){
		ModelAndView mv = new ModelAndView();
		List<Category> parents = categoryService.findAll();
		mv.addObject("parents", parents);
		mv.setViewName("/jsps/left.jsp");
		return mv;
	}	
	
	
}
