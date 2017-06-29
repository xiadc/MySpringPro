package cn.itcast.goods.book.service;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.goods.book.dao.BookDao;
import cn.itcast.goods.book.dao.BookMapper;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.pager.Expression;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.pager.PageConstants;

@Service
@Transactional
public class BookService {

	//private BookDao bookDao = new BookDao();
	@Autowired
	private BookMapper bookMapper;
	
	/**加载图书
	 * @param bid
	 * @return
	 */
	public Book load(String bid){
		try {
			return bookMapper.findByBid(bid);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**按分类查
	 * @param cid
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCategory(String cid,int pc){
		try {
			//1.得到ps
			int ps = PageConstants.BOOK_PAGE_SIZE;
			//总记录数
			int count = bookMapper.findCountByCategory(cid);
			int start = (pc-1)*ps;					
			List<Book> bookList =  bookMapper.findByCategory(cid, start,ps);
			PageBean<Book> pb = new PageBean<Book>();
			pb.setBeanList(bookList);
			pb.setPc(pc);
			pb.setPs(ps);
			pb.setTr(count);
			
			return pb;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 通用查询方法
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	private PageBean<Book> findByCriteria(List<Expression> exprList, int pc) throws SQLException{
		//1.得到ps
		int ps = PageConstants.BOOK_PAGE_SIZE;
		
		StringBuilder whereSql = new StringBuilder("where 1=1");
		List<Object> params = new ArrayList<Object>();//对应问号的值
		for(Expression expr : exprList){
			whereSql.append(" and ").append(expr.getName()).append(" ")
			.append(expr.getOperation()).append(" ");
			if(!expr.getOperation().equals("is null")){
				whereSql.append("?");
				params.add(expr.getValue());
			}
		}
		
		//2.总记录数
		String sql = "select count(*) from t_book "+ whereSql;
		Number number =  (Number)qr.query(sql, new ScalarHandler(), params.toArray());
		int tr = number.intValue();
		
		//3.得到beanList，即当前页记录数
		 sql = "select * from t_book " + whereSql +" order by orderBy limit ?,?";
		 params.add((pc-1)*ps);//当前页首行记录下标
		 params.add(ps);//每页记录数
		 List<Book> bookList =  qr.query(sql, new BeanListHandler<Book>(Book.class), params.toArray());
		 
		 //4.创建PageBean，设置参数
		 PageBean<Book> pb = new PageBean<Book>();
		 pb.setBeanList(bookList);
		 pb.setPc(pc);
		 pb.setPs(ps);
		 pb.setTr(tr);
		 
		 return pb;
	}
	
	/**按作者查
	 * @param author
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByAuthor(String author,int pc){
		try {
			return bookDao.findByAuthor(author, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**按出版社
	 * @param press
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByPress(String press,int pc){
		try {
			return bookDao.findByPress(press, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**按书名查
	 * @param bname
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByBname(String bname,int pc){
		try {
			return bookDao.findByBname(bname, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**多条件组合查询
	 * @param criteria
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCombination(Book criteria,int pc){
		try {
			return bookDao.findByCombination(criteria, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**添加图书
	 * @param book
	 */
	public void add(Book book){
		try {
			bookDao.add(book);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**添加图书
	 * @param book
	 */
	public void edit(Book book){
		try {
			bookDao.updateBook(book);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	/**删除图书
	 * @param book
	 */
	public void delete(Book book){
		try {
			//删除图书
			bookDao.delete(book.getBid());
			
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
}
