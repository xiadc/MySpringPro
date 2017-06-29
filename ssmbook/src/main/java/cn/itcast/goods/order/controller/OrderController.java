package cn.itcast.goods.order.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.cart.domain.CartItem;
import cn.itcast.goods.cart.service.CartItemService;
import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.order.domain.OrderItem;
import cn.itcast.goods.order.service.OrderService;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.user.domain.User;

/** 
 * @author xiadc 
 * createtime：2017年4月18日 上午11:39:56 
 * 类说明 
 */

@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private CartItemService cartItemService;
	
	
	@RequestMapping("/addOrder")
	public ModelAndView addOrder(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		String cartItemIds = req.getParameter("cartItemIds");
		List<CartItem> cartItemList =  cartItemService.loadCartItems(cartItemIds);
		//添加order对象
		Order order = new Order();
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(String.format("%tF %<tT", new Date()));
		order.setAddress(req.getParameter("address"));
		order.setStatus(1);
		
		BigDecimal total = new BigDecimal("0");
		for(CartItem cartItem : cartItemList){
			total = total.add(new BigDecimal(cartItem.getSumTotal()+""));
		}
		order.setTotal(total.doubleValue());
		order.setUser((User)req.getSession().getAttribute("sessionUser"));
		
		//得到orderItemList
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for(CartItem cartItem : cartItemList){
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setBook(cartItem.getBook());
			orderItem.setOrderItemId(CommonUtils.uuid());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setSubtotal(cartItem.getSumTotal());
			orderItemList.add(orderItem);
		}
		order.setOrderItemList(orderItemList);
		orderService.addOrder(order);
		cartItemService.batchDelete(cartItemIds);
		
		mv.addObject("order", order);
		mv.setViewName("/jsps/order/ordersucc.jsp");
		return mv;
	}
	
	@RequestMapping("/payPage")
	public ModelAndView payPage(String oid){
		ModelAndView mv = new ModelAndView();
		Order order= orderService.findByOid(oid);
		mv.addObject("order", order);
		mv.setViewName("/jsps/order/pay.jsp");
		return mv;
	}
	
	@RequestMapping("/myOrders")
	public ModelAndView myOrders(HttpServletRequest req,HttpSession session){
		ModelAndView mv = new ModelAndView();
		
		//1.得到pc
		int pc =getPc(req);
		//2.得到url
		String url = getUrl(req);
		
		User user= (User) session.getAttribute("sessionUser");
		if(user == null){
			mv.setViewName("/jsps/user/login.jsp");
			return mv;
		}
		PageBean<Order> pb = orderService.myOrders(user.getUid(),pc);	
		pb.setUrl(url);
		
		mv.addObject("pb", pb);
		mv.setViewName("/jsps/order/list.jsp");
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
	
	@RequestMapping("/queryOrderByOid")
	public ModelAndView queryOrderByOid(String oid,String status){
		ModelAndView mv = new ModelAndView();
		
		Order order = orderService.findByOid(oid);
		mv.addObject("order", order);
		mv.addObject("status", status);
		mv.setViewName("/jsps/order/desc.jsp");
		
		return mv;
	}
	
	@RequestMapping("/cancelOrder")
	public ModelAndView cancelOrder(String oid){
		ModelAndView mv = new ModelAndView();	
		
		int status = orderService.findStatus(oid);
		if(status !=1){
			mv.addObject("code", "error");
			mv.addObject("msg", "订单当前状态不能取消！");
			mv.setViewName("/jsps/msg.jsp");
			return mv;
		}
		orderService.updateStatus(oid, 5);//5代表取消订单		
		mv.addObject("code", "success");
		mv.addObject("msg", "订单已取消！");
		mv.setViewName("/jsps/msg.jsp");
		
		return mv;				
	}
	
	@RequestMapping("/confirmOrder")
	public ModelAndView confirmOrder(String oid){
		ModelAndView mv = new ModelAndView();	
		
		int status = orderService.findStatus(oid);
		if(status !=3){
			mv.addObject("code", "error");
			mv.addObject("msg", "订单当前状态不能确认收货！");
			mv.setViewName("/jsps/msg.jsp");
			return mv;
		}
		orderService.updateStatus(oid, 4);//5代表订单完成		
		mv.addObject("code", "success");
		mv.addObject("msg", "订单已取消！");
		mv.setViewName("/jsps/msg.jsp");
		
		return mv;
		
	}
	//假装付款成功
	@RequestMapping("/payment")
	public ModelAndView payment(String oid){
		ModelAndView mv = new ModelAndView();	
		
		int status = orderService.findStatus(oid);
		if(status !=1){
			mv.addObject("code", "error");
			mv.addObject("msg", "订单当前状态不能支付！");
			mv.setViewName("/jsps/msg.jsp");
			return mv;
		}
		orderService.updateStatus(oid, 3);//5代表订单完成		
		mv.addObject("code", "success");
		mv.addObject("msg", "订单已支付！");
		mv.setViewName("/jsps/msg.jsp");
		
		return mv;
	}
	
	
}
