package cn.itcast.goods.cart.controller;


import java.util.List;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.cart.domain.CartItem;
import cn.itcast.goods.cart.service.CartItemService;
import cn.itcast.goods.user.domain.User;

/** 
 * @author xiadc 
 * createtime：2017年4月17日 下午5:34:31 
 * 类说明 
 */

@Controller
public class CartItemController {

	@Autowired
	private CartItemService cartItemService;
	
	@RequestMapping("/loadCartItems")
	public ModelAndView loadCartItems(String cartItemIds){
		ModelAndView mv = new ModelAndView();
		List<CartItem> list = cartItemService.loadCartItems(cartItemIds);
		mv.addObject("cartItemList", list);
		mv.addObject("cartItemIds", cartItemIds);
		mv.setViewName("/jsps/cart/showitem.jsp");
		return mv;
	}
	
	@RequestMapping("/updateQuantity")
	public @ResponseBody CartItem updateQuantity(String cartItemId,int quantity){
		CartItem cartItem = cartItemService.updateQuantity(cartItemId, quantity);		
		return cartItem;				
	}
	
	
	@RequestMapping("/addCartItem")
	public ModelAndView addCartItem(Book book,@RequestParam(value="quantity",required=false) int quantity,HttpSession session){
		ModelAndView mv = new ModelAndView();
		
		User user = (User) session.getAttribute("sessionUser");
		if(user == null){
			mv.setViewName("/jsps/user/login.jsp");
			return mv;
		}
		CartItem cartItem = new CartItem();
		cartItem.setQuantity(quantity);
		cartItem.setUser(user);
		cartItem.setBook(book);		
		cartItemService.addCartItem(cartItem);						
		return myCart(session);
	}
	//显示购物车
	@RequestMapping("/myCart")
	public ModelAndView myCart(HttpSession session){
		ModelAndView mv = new ModelAndView();		
		User user = (User) session.getAttribute("sessionUser");
		if(user == null){
			mv.setViewName("/jsps/user/login.jsp");
			return mv;
		}
		List<CartItem> list =  cartItemService.findByUid(user.getUid());
		mv.addObject("list", list);
		mv.setViewName("/jsps/cart/list.jsp");
		return mv;
	}
	
	@RequestMapping("/batchDelete")
	public ModelAndView batchDelete(String cartItemIds,HttpSession session){
		//ModelAndView mv = new ModelAndView();
		cartItemService.batchDelete(cartItemIds);
		return myCart(session);
		
	}
	
	
}
