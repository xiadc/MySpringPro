package cn.itcast.goods.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.order.domain.OrderItem;

/** 
 * @author xiadc 
 * createtime：2017年4月18日 上午11:41:43 
 * 类说明 
 */
public interface OrderMapper {

	public void addOrder(Order order) throws Exception;
	
	public void addOrderItemList(List<OrderItem> orderItemList) throws Exception;
	
	public Order findByOid(String oid) throws Exception;
	
	public List<Order> findByUser(@Param("uid")String uid,@Param("start")int start,@Param("ps")int ps) throws Exception;
	
	public int findCountByUser(String uid) throws Exception;
	
	public int findStatus(String oid) throws Exception;
	
	public void updateStatus(@Param("oid")String oid,@Param("status")int status) throws Exception;
	
}
