package cn.itcast.bos.service.base.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaDao;
import cn.itcast.bos.dao.base.FixedAreaDao;
import cn.itcast.bos.dao.base.OrderDao;
import cn.itcast.bos.dao.base.WorkBillDao;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Contants;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.base.OrderService;

@Service()
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private FixedAreaDao fixedAreaDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private WorkBillDao workBillDao;
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;
	
	
	@Override
	public void saveOrder(Order order) {

		//发件人的区域关联,如果这步不操作，那么此时得到的区域对象因为没有oid,会报错
		Area sendArea = order.getSendArea();
		Area sendResistence = areaDao.
				findByProvinceAndCityAndDistrict
				(sendArea.getProvince(),sendArea.getCity(),sendArea.getDistrict());
		order.setSendArea(sendResistence);
		
		//收件人关联区域
		Area recArea = order.getSendArea();
		Area recPesistence = areaDao.
				findByProvinceAndCityAndDistrict
				(recArea.getProvince(),recArea.getCity(),recArea.getDistrict());
		order.setRecArea(recPesistence);
		
		//得到定区id,通过定区id,得到该定区的快递员，绑定到快递员(完全匹配)
		String fixedAreaId = WebClient.create(Contants.CRM_MANAGEMENT_URL + 
				"/services/customerService/findCustomer/"
				+ "findFixedAreaIdByAddress?address=" + order.getSendAddress())
				.accept(MediaType.APPLICATION_JSON).get(String.class);
		
		if(fixedAreaId!=null) {
			//通过定区找到看快递员
			FixedArea fixedArea = fixedAreaDao.findOne(fixedAreaId);
			Iterator<Courier> iterator= fixedArea.getCouriers().iterator();
			if(iterator.hasNext()) {
				Courier courier = iterator.next();
				if(courier !=null) {
					System.out.println("关联快递员");
					order.setCourier(courier);
					orderDao.save(order);
				
					//生成工单
					createWorkBill(order);
					return;
				} else {
					// to be do 
					System.out.println("该区域没有快递员");
					return;
				}
			}
		} 
		
		//分区实现自动分单 匹配关键字
		Set<SubArea> subAreas = sendResistence.getSubareas();
		
		//判断详细地址中是否包含输入的关键字
		for (SubArea subArea : subAreas) {
			if(order.getSendAddress().contains(subArea.getKeyWords())) {
				linkCourier(subArea, order);
			}
		}
		
		//判断详细地址中是否包含辅助关键字
	    for (SubArea subArea : subAreas) {
			if(order.getSendAddress().contains(subArea.getAssistKeyWords())) {
				linkCourier(subArea, order);
			}
		}
	    
	    //人工分单
	    order.setOrderType("2");
	    orderDao.save(order);
	}
	
	

	
	//公司内部生成的工单
	private void createWorkBill(final Order order) {
		WorkBill workBill = new WorkBill();
		
		workBill.setBuildtime(new Date());
		workBill.setRemark(order.getRemark());
		workBill.setPickstate("新单");
		workBill.setType("新");
		
		final String sms = RandomStringUtils.randomNumeric(4);
		workBill.setSmsNumber(sms);
		workBill.setOrder(order);
		workBill.setCourier(order.getCourier());
		workBillDao.save(workBill);
		
		//将待发送的消息发送到消息队列中
		jmsTemplate.send("bos_sms",new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", order.getCourier().getTelephone());
				mapMessage.setString("msg", "短信序号：" + sms + ",取件地址:" + order.getSendAddress() + 
						",联系人:" + order.getSendName() + "手机:" + order.getSendMobile() + 
						",给您捎的话:" + order.getRemark());
				return mapMessage;
			}
			
		});
		workBill.setPickstate("已通知");
	}
	
	/**
	 * 自动下订单，关联快递员
	 * @param subArea
	 * @param order
	 */
	private void linkCourier(SubArea subArea,Order order) {
		Iterator<Courier> iterator= subArea.getFixedArea().getCouriers().iterator();
		if(iterator.hasNext()) {
			Courier courier = iterator.next();
			if(courier !=null) {
				System.out.println("关联快递员");
				order.setCourier(courier);
				orderDao.save(order);
				
				//生成工单
				createWorkBill(order);
				
				
				return;
			} 
		}
	}
}
