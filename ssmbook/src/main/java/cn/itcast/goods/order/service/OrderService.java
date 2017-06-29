package cn.itcast.goods.order.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.goods.order.dao.OrderDao;
import cn.itcast.goods.order.dao.OrderMapper;
import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.pager.PageConstants;
import cn.itcast.jdbc.JdbcUtils;

@Service
@Transactional
public class OrderService {

	//private OrderDao orderDao = new OrderDao();
	@Autowired
	private OrderMapper orderMapper;
	
	/**按id查询状态
	 * @param oid
	 * @return
	 */
	public int findStatus(String oid){
		try {
			return orderMapper.findStatus(oid);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**更新状态
	 * @param oid
	 * @param status
	 */
	public void updateStatus(String oid,int status){
		try {
			 orderMapper.updateStatus(oid, status);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**我的订单
	 * @param uid
	 * @param pc
	 * @return
	 */
	public PageBean<Order> myOrders(String uid,int pc){
		try {
			PageBean<Order> pb = new PageBean<Order>();
			//1.得到ps
			int ps = PageConstants.BOOK_PAGE_SIZE;
			//总记录数
			int count = orderMapper.findCountByUser(uid);
			int start = (pc-1)*ps;		
			
			List<Order> list = orderMapper.findByUser(uid,start,ps);
			pb.setBeanList(list);
			pb.setPc(pc);
			pb.setTr(count);
			pb.setPs(ps);
			
			return pb;
		} catch (Exception e) {
			
			throw new RuntimeException(e);
		}
	}
	
	/**添加订单
	 * @param order
	 */
	public void addOrder(Order order){
		try {			
			 orderMapper.addOrder(order);
			 orderMapper.addOrderItemList(order.getOrderItemList());
		} catch (Exception e) {			
			throw new RuntimeException(e);
		}
	}
	
	/**按id查询订单
	 * @param order
	 */
	public Order findByOid(String oid){
		try {		
			return orderMapper.findByOid(oid);						
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**查询所有订单
	 * @param pc
	 * @return
	 */
	public PageBean<Order> findAll(int pc){
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb= orderDao.findAll(pc);
			JdbcUtils.commitTransaction();	
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	
	/**按状态查询订单
	 * @param status
	 * @param pc
	 * @return
	 */
	public PageBean<Order> findByStatus(int status,int pc){
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb= orderDao.findByStatus(status, pc);
			JdbcUtils.commitTransaction();	
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
}
