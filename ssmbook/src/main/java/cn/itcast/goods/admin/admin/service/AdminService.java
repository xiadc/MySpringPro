package cn.itcast.goods.admin.admin.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.goods.admin.admin.dao.AdminDao;
import cn.itcast.goods.admin.admin.dao.AdminMapper;
import cn.itcast.goods.admin.admin.domain.Admin;

@Service
@Transactional
public class AdminService {
	//private AdminDao adminDao = new AdminDao();
	@Autowired
	private AdminMapper adminMapper;
	/**denglu gong ne
	 * @param admin
	 * @return
	 */
	public Admin login(Admin admin){
		try {
			return adminMapper.find(admin.getAdminname(), admin.getAdminpwd());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
