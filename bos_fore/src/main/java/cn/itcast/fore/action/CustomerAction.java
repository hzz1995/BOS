package cn.itcast.fore.action;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.crm.domain.Customer;
import cn.itcast.fore.utils.MailUtils;

@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
@Controller
@Actions
public class CustomerAction extends BaseAction<Customer>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//手机验证码
	private String checkcode;
	
	//邮箱激活码
	private String activecode;
	
	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}
	
	public void setActivecode(String activecode) {
		this.activecode = activecode;
	}

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Action(value="customer_sendSms")
	public String sendSms() {	
		String code = RandomStringUtils.randomNumeric(4);
		System.out.println("生成的手机信息验证码:" + code);
		//将随机生成的验证码保存到session中
		ActionContext.getContext().getSession().put("checkedcode", code);
		final String msg = "尊敬的用户您好,本次获得的验证码为："+code + ",服务电话：4006184000";
		jmsTemplate.send("bos_sms", new MessageCreator() {
		
			@Override
			public Message createMessage(Session session) throws JMSException {
					MapMessage mapMessage = session.createMapMessage();
					mapMessage.setString("telephone", model.getTelephone());
					mapMessage.setString("msg", msg);
				return mapMessage;
			}
			
		});
		return NONE;
	}
	
	@Action(value="customer_regist",results= {
			@Result(name="success",type="redirect",location="./signup-success.html"),
			@Result(name="input",type="redirect",location="./signup.html")})
	public String regist() {
		String code = (String)ActionContext.getContext().getSession().get("checkedcode");
		if(code == null || !code.equals(checkcode)) {
			//注册失败
			System.out.println("注册失败");
			return INPUT;
		} else {
			//注册成功
			WebClient.create
			("http://localhost:9998/crm_management/services"
					+ "/customerService/addCustomer").
			type(MediaType.APPLICATION_JSON).post(model);
			System.out.println("注册成功");
		}
		//发送激活邮件
		String activecode1 = RandomStringUtils.randomNumeric(32);
		
		//保存手机号码和验证码到redis中,24小时之后失效
		redisTemplate.opsForValue().set(model.getTelephone(), activecode1, 24, TimeUnit.HOURS);
		//发送邮件
		String content = "尊敬的客户您好，请于24小时内，进行邮箱账户的绑定，点击下面的地址完成绑定："
				+ "<br/><a href='" +MailUtils.activeUrl+ "?telephone="+model.getTelephone()
				+"&activecode="+activecode1+"'>速运快递邮箱绑定地址</a>";
		MailUtils.sendMail("速运快递激活邮件", content, model.getEmail());
		return SUCCESS;
	}
	
	@Action(value="customer_activeMail")
	public String activeMail() throws IOException {
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		//先判断激活码是否有效
		String activecode_From_Redis = redisTemplate.opsForValue().get(model.getTelephone());
		//如果无效则提示
		if(activecode_From_Redis == null || !activecode_From_Redis.equals(activecode)) {
			ServletActionContext.getResponse().getWriter().println("对不起您的激活码已经失效，请重新注册！");
		} else {
			//否则查看邮箱是否已经绑定，如果未绑定则绑定，否则不予绑定
			Customer customer = 
					WebClient.create("http://localhost:9998/crm_management/services/customerService/customer/telephone/"
			+model.getTelephone()).accept(MediaType.APPLICATION_JSON).get(Customer.class);
			
			if(customer.getType()== null || customer.getType() != 1) {
				//没有绑定过，可以进行绑定
				WebClient.create("http://loc	alhost:9998/crm_management/services/customerService/customer/updatetype/"
						+model.getTelephone()).get();
				ServletActionContext.getResponse().getWriter().println("注册成功");
			} else {
				ServletActionContext.getResponse().getWriter().println("对不起您已经绑定邮箱了，无需绑定");
			}
		}
		//最后删除激活码
		redisTemplate.delete(model.getTelephone());
		return NONE;
	}
}
