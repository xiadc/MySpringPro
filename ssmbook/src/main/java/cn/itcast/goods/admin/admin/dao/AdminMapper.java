package cn.itcast.goods.admin.admin.dao;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Param;

import cn.itcast.goods.admin.admin.domain.Admin;

/** 
 * @author xiadc 
 * createtime：2017年4月19日 下午5:06:35 
 * 类说明 
 */
public interface AdminMapper {

	public Admin find(@Param("loginname")String loignname,@Param("loginpass")String loginpass) throws SQLException;
	
}
