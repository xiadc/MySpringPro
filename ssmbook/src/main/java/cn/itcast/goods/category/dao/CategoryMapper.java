package cn.itcast.goods.category.dao;

import java.util.List;

import cn.itcast.goods.category.domain.Category;

/** 
 * @author xiadc 
 * createtime：2017年4月17日 上午11:56:45 
 * 类说明 
 */
public interface CategoryMapper {

	public List<Category> findAll() throws Exception;
}
