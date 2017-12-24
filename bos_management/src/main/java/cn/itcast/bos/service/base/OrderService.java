package cn.itcast.bos.service.base;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import cn.itcast.bos.domain.take_delivery.Order;


public interface OrderService {
	
	
	@Path("/order/saveOrder")
	@POST	
	@Consumes({"application/xml","application/json"})
	@Produces({"application/xml","application/json"})
	void saveOrder(Order order);
}
