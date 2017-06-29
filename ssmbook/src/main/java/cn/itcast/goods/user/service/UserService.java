package cn.itcast.goods.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.dao.UserMapper;
import cn.itcast.goods.user.domain.User;
import cn.itcast.goods.user.service.exception.UserException;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

/**
 * @author xiadc
 * @date:2016-7-20 上午9:56:02
 */
@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserMapper userMapper;

	/**注册校验
	 * @param loginname
	 * @return
	 */
	public boolean ajaxValidateLoginname(String loginname){
		try {
			User user = userMapper.ajaxValidateLoginname(loginname);
			if(user == null){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**email 校验
	 * @param email
	 * @return
	 */
	public boolean ajaxValidateEmail(String email){
		try {
			User user = userMapper.ajaxValidateEmail(email);
			if(user == null){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void regist(User user){
		
		//1.补全user
		user.setStatus(false);
		user.setActivationcode(CommonUtils.uuid()+CommonUtils.uuid());
		user.setUid(CommonUtils.uuid());
		
		//2.user加入数据库
		try {
			userMapper.add(user);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		//3.发邮件
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		String host = prop.getProperty("host");
		String username = prop.getProperty("username");
		String password = prop.getProperty("password");
		
		//创建email session
		Session session  = MailUtils.createSession(host, username, password);
		//创建mail对象
		String from = prop.getProperty("from");
		String to = user.getEmail();
		String subject = prop.getProperty("subject");
		String content = MessageFormat.format(prop.getProperty("content"), user.getActivationcode());
		
		Mail mail = new Mail(from,to,subject,content);
		//发邮件
		try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			e.printStackTrace();
			//throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**激活用户
	 * @param code
	 * @throws UserException
	 */
	/*public void activation(String code) throws UserException{
		try {
			User user = userMapper.findByCode(code);
			if(user ==null) throw new UserException("激活码不存在！");
			if(user.isStatus()) throw new UserException("您已经激活过了，请勿重复激活！");
			userMapper.updateState(user.getUid(), true);//激活
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}*/
	
	/**登陆功能
	 * @param user
	 * @return
	 */
	public User login(User user){
		try {
			return userMapper.findByLoginnameAndLoginpass(user.getLoginname(), user.getLoginpass());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**修改密码
	 * @param user
	 * @throws UserException 
	 */
/*	public void updatePassword(String uid,String oldPass,String newPass) throws UserException{
		
		try {
			//校验老密码
			boolean bool = userMapper.findByUidAndPassword(uid, oldPass);
			if(!bool) throw new UserException("老密码错误！");
			
			userMapper.updatePassword(uid, newPass);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}*/
	
}
