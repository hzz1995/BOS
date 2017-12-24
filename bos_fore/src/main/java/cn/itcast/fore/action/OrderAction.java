package cn.itcast.fore.action;

import java.util.Date;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Contants;
import cn.itcast.bos.domain.take_delivery.Order;

@Namespace("/")
@ParentPackage("json-default")
@Service
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sendAreaInfo;
	private String recAreaInfo;	
	
	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}




	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}

	@Action(value="save_orders",results= {@Result(name="success",type="redirect",location= "/#/myhome")})
	public String saveOrder() {
		String[] sendAddresses = sendAreaInfo.split("/");
		String[] recvAddresses = recAreaInfo.split("/");
		//发件人地址
		Area sendArea = new Area();
		sendArea.setProvince(sendAddresses[0]);
		sendArea.setCity(sendAddresses[1]);
		sendArea.setDistrict(sendAddresses[2]);
		//通过后端得到区域id
		
		model.setSendArea(sendArea);
		
		//收件人地址
		Area recvArea = new Area();
		recvArea.setProvince(recvAddresses[0]);
		recvArea.setCity(recvAddresses[1]);
		recvArea.setDistrict(recvAddresses[2]);
		//通过后端得到区域id
		
		model.setRecArea(recvArea);
		
		//设置订单号
		model.setOrderNum(UUID.randomUUID().toString());
		
		//生成订单的时间
		model.setOrderTime(new Date());
		
		//设置订单类型
		model.setOrderType("1");
		
		//设置快递状态
		model.setStatus("待取件");
		
		//设置客户id
		//Customer customer = (Customer)ServletActionContext.getRequest().getSession().getAttribute("customer");
		/*
		 * 调试
		 */
		model.setCustomer_id(1);
		
		WebClient.create(Contants.BOS_MANAGER_URI + "/services/orderService/order/saveOrder")
				.type(MediaType.APPLICATION_JSON).post(model);
		return SUCCESS;
	}

}
