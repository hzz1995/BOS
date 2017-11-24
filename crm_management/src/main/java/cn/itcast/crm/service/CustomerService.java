package cn.itcast.crm.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cn.itcast.crm.domain.Customer;

/**
 * 提供给客户端对用户进行关联与取消关联的服务
 * 
 * @author 胡正再
 *
 */
public interface CustomerService {

	

	/**
	 * 查询未关联客户列表
	 * 
	 * @return List<Customer>
	 */
	@Path("/noassociationcustomers")
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<Customer> findNoAssociationCustomers();

	/**
	 * 查询已经关联客户列表
	 * 
	 * @return
	 */
	@Path("/associationfixedareacustomers/{fixedareaid}")
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<Customer> findHasAssociationFixedAreaCustomers(
			@PathParam("fixedareaid") String fixedAreaId);
	/**
	 *  将客户关联到指定定区中
	 * @param customerIdStr
	 * @param fixedAreaId
	 */
	@Path("/associationcustomerstofixedarea")
	@PUT
	public void associationCustomersToFixedArea(
			@QueryParam("customerIds") String customerIdStr,
			@QueryParam("fixedAreaId") String fixedAreaId);
}