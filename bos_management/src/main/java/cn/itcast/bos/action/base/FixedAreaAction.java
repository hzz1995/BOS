package cn.itcast.bos.action.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.crm.domain.Customer;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
@Actions
public class FixedAreaAction extends BaseAction<FixedArea> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private FixedAreaService fixedAreaService;
	
	private String[] customerIds;
	
	//快递员id
	private Integer courierId;
	//派送时间
	private String takeTimeId;
	
	
	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}

	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}

	public void setTakeTimeId(String takeTimeId) {
		this.takeTimeId = takeTimeId;
	}



	/**
	 * 保存定区
	 * @return
	 */
	@Action(value="fixedArea_save",results= {
			@Result(name="success",type="redirect",location="./pages/base/fixed_area.html")})
	public String save() {
		fixedAreaService.save(model);
		return SUCCESS;
	}
	
	/**
	 * 通过条件和分页得到页面数据
	 * @return
	 */
	@Action(value="fixedAarea_pageQuery",results= {@Result(name="success",type="json")})
	public String pageQuery() {
		Specification<FixedArea> specification = new Specification<FixedArea>() {

			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if(StringUtils.isNotBlank(model.getId())) {
					Predicate p1 = cb.equal(root.get("id").as(String.class), model.getId());
					list.add(p1);
				} else if(StringUtils.isNotBlank(model.getCompany())){
					Predicate p2 = cb.like(root.get("company").as(String.class), "%" + model.getCompany() + "%");
					list.add(p2);
				}
				Predicate p3 = cb.and(list.toArray(new Predicate[0]));
 				return p3;
			}
			
		};
		
		Pageable pageable = new PageRequest(page-1,rows);
		Page<FixedArea> list = fixedAreaService.findAllByPage(specification,pageable);
		pushPageDataToValueStack(list);
		return SUCCESS;
	}
	
	
	/**
	 * 查找没有关联定区的客户集合
	 * @return
	 */
	@Action(value="fixedArea_findNoAssociationCustomers",
			results= {@Result(name="success",type="json")})
	public String findNoAssociationCustomers() {
		Collection<? extends Customer> collection = WebClient.create("http://localhost:9998/crm_management/services/customerService/noassociationcustomers")
				.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}
	
	//查找已经关联的客户信息
	@Action(value="fixedArea_findHasAssociationFixedAreaCustomers",
			results= {@Result(name="success",type="json")})
	public String findHasAssociationFixedAreaCustomers() {
		Collection<? extends Customer> collection = WebClient
				.create("http://localhost:9998/crm_management/services/customerService/associationfixedareacustomers/"+
				model.getId()).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}
    
	@Action(value="fixedArea_associationCustomersToFixedArea",results= {
					@Result(name="success",type="redirect",location="./pages/base/fixed_area.html")})
	public String associationCustomersToFixedArea() {
		//把数组拼接成字符串
		String customerStr = StringUtils.join(customerIds, ",");
		WebClient.create("http://localhost:9998/crm_management/services/customerService/associationcustomerstofixedarea?customerIds="
				+customerStr + "&fixedAreaId=" + model.getId()).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).put(null);
		return SUCCESS;
	}
	
	//关联快递员
	@Action(value="fixedArea_associationCourierToFixedArea",results= {
			@Result(name="success",type="redirect",location="./pages/base/fixed_area.html")})
	public String associationCourierToFixedArea() {
		//关联快递员和定区、关联快递员和派送时间
		fixedAreaService.associationCourierToFixedArea(model,courierId,takeTimeId);
		
		
		
		return SUCCESS;
	}
}
