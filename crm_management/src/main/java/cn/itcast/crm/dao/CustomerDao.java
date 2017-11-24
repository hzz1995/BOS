package cn.itcast.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.crm.domain.Customer;

/**
 * 对客户的增删改查操作
 * @author 胡正再
 *
 */
public interface CustomerDao extends JpaRepository<Customer, Integer>{
	/**
	 * 查询出用户表中fixedAreaId不为空的Cusotmer对象
	 * @return List<Customer>
	 */
	List<Customer> findByFixedAreaIdIsNull();
	
	/**
	 * 查询出用户表中已经关联定区的Customer对象
	 * @param fixedAreaId
	 * @return List<Customer>
	 */
	List<Customer> findByFixedAreaId(String fixedAreaId);
	
	/**
	 * 修改Customer和定区的关联关系
	 * @param fixedAreaId    定区id
	 * @param id             客户表id
	 */
	@Query("update Customer set fixedAreaId = ? where id = ?")
	@Modifying
	void updateFixedAreaId(String fixedAreaId, Integer id);
	
	/**
	 * 取消所有与定区关联的客户
	 * @param fixedAreaId
	 */
	@Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
	@Modifying
	void clearFixedAreaId(String fixedAreaId);
}
