package cn.itcast.goods.cart.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.itcast.goods.cart.domain.CartItem;

/** 
 * @author xiadc 
 * createtime：2017年4月17日 下午5:33:00 
 * 类说明 
 */
public interface CartItemMapper {

	public CartItem findByBidAndUid(@Param("bid")String bid,@Param("uid")String uid) throws Exception;
	
	public void addCartItem(CartItem cartItem) throws Exception;
	
	public void updateQuantity(@Param("id")String id,@Param("quantity")int quantity) throws Exception;
	
	public List<CartItem> findByUser(String uid) throws Exception;
	
	public void batchDelete(String[] cartItemIds) throws Exception;
	
	public CartItem findByCartItemId(String cartItemId) throws Exception;
	
	public List<CartItem> loadCartItems(String[] cartItemIds) throws Exception;
}
