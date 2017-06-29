package cn.itcast.goods.book.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.itcast.goods.book.domain.Book;

/** 
 * @author xiadc 
 * createtime：2017年4月17日 下午4:05:17 
 * 类说明 
 */
public interface BookMapper {

	public int findCountByCategory(String cid) throws Exception;
	
	public List<Book> findByCategory(@Param("cid")String cid, @Param("start")int start,@Param("ps") int ps) throws Exception;
	
	public Book findByBid(String id) throws Exception;
}
