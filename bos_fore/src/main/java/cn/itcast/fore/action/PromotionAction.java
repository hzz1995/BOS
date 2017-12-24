package cn.itcast.fore.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import freemarker.template.Configuration;
import freemarker.template.Template;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
@Actions
@SuppressWarnings("unchecked")
public class PromotionAction extends BaseAction<Promotion> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Action(value="query_promotion",results= {@Result(name="success",type="json")})
	public String queryPromotion() {
		PageBean<Promotion> pageBean = WebClient.create("http://localhost:9001/bos_management/services/promotionService/pageQuery"
				+ "?page=" + page + "&rows=" + rows).accept(MediaType.APPLICATION_JSON).get(PageBean.class);
		
		ActionContext.getContext().getValueStack().push(pageBean);
		return SUCCESS;
	}
	
	@Action(value="promotion_showDetail")
	public String showDetail() throws Exception {
		//先判断id.html是否存在，如果不存在，则创建，否则直接输出。
		String htmlRealPath = ServletActionContext.getServletContext()
				.getRealPath("/freemarker");
		File htmlFile = new File(htmlRealPath + "/" + model.getId() + ".html");
		
		if(!htmlFile.exists()) {
			Configuration config = new Configuration(
						Configuration.VERSION_2_3_22);
			String freemarkerRealPath = ServletActionContext.getServletContext()
			.getRealPath("/WEB-INF/templates");
			config.setDirectoryForTemplateLoading(new File(freemarkerRealPath));
			
			Template template = config.getTemplate("promotion_template.ftl");
			
			//封装对象
			Promotion promotion = WebClient.create("http://localhost:9001/bos_management/services/promotionService/findById?id="
					+ model.getId())
					.accept(MediaType.APPLICATION_JSON)
					.get(Promotion.class);
			
			Map<String,Object> map = new HashMap<>();
			map.put("promotion", promotion);
			
			template.process(map, new OutputStreamWriter(new FileOutputStream(htmlFile),"utf-8"));
		}
		
		//直接输出
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		
		FileUtils.copyFile(htmlFile, ServletActionContext.getResponse().getOutputStream() );
		return NONE;
	}
	
	

}
