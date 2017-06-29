package cn.itcast.goods.user.dao;

import org.apache.ibatis.annotations.Param;

import cn.itcast.goods.user.domain.User;

/** 
 * @author xiadc 
 * createtime：2017年4月15日 下午5:36:56 
 * 类说明 
 */
public interface UserMapper {

	
	public User findByLoginnameAndLoginpass(@Param("loginname") String loginname,@Param("loginpass") String loginpass) throws Exception;
	 
	//注册校验	
	public User ajaxValidateLoginname(String loginname) throws Exception;
	//email校验
	public User ajaxValidateEmail(String email) throws Exception;	
	//添加用户
	public void add(User add) throws Exception;
	
	
}
