package cn.itcast.goods.book.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.pager.PageBean;

/** 
 * @author xiadc 
 * createtime：2017年4月17日 下午4:07:02 
 * 类说明 
 */
@Controller
public class BookController {
	@Autowired
	private BookService bookService;
	
	@RequestMapping("/load")
	public ModelAndView load(String bid){
		ModelAndView mv = new ModelAndView();
		Book book = bookService.load(bid);
		mv.addObject("book", book);
		mv.setViewName("/jsps/book/desc.jsp");
		return mv;
	}
	
	@RequestMapping("/findByCategory")
	public ModelAndView  findByCategory(String cid,HttpServletRequest req){
		
		ModelAndView mv = new ModelAndView();
		//1.得到pc
		int pc =getPc(req);
		//2.得到url
		String url = getUrl(req);
		//3.获取查询条件，这里是cid
		//String cid = req.getParameter("cid");
		//4.调用service
		PageBean<Book> pb = bookService.findByCategory(cid,pc);
		//5.给pageBean设置url,保存pageBean并转发
		pb.setUrl(url);		
		mv.addObject("pb", pb);
		mv.setViewName("jsps/book/list.jsp");
		return mv;
		
	}
	
	/**获取当前页码pagecode
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req){
		int pc =1;
		String param = req.getParameter("pc");
		if(param!=null&&!param.trim().isEmpty()){
			try{
				pc = Integer.parseInt(param);
			}catch(RuntimeException e){}
		}
			return pc;
	}
	
	/**截取url，页面中的分页导航中需要使用它作为超链接目标
	 * @param req
	 * @return
	 */
	private String getUrl(HttpServletRequest req){
		// http://localhost:8080/goods/BookServlet?method=findByCategory&cid=xxx
		// /goods/BookServlet + ? +method=findByCategory&cid=xxx
		String url = req.getRequestURI()+ "?" + req.getQueryString();
		/*
		 * 如果url中存在pc参数，截取掉，不存在就不用截取
		 */
		int index =url.lastIndexOf("&pc=");
		if(index!=-1)
			url=url.substring(0,index);
		return url;
	}
}
