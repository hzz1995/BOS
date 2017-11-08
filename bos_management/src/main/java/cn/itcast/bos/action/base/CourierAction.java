package cn.itcast.bos.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
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
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@ParentPackage("json-default")
@Namespace(value="/")
@Controller
@Scope("prototype")
@Actions
public class CourierAction extends ActionSupport implements ModelDriven<Courier>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Courier courier = new Courier();
	private int page;
	private int rows;
	
	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Override
	public Courier getModel() {
		return courier;
	}
	
	@Autowired
	private CourierService courierService;
	
	@Action(value="courier_save",results= {@Result(name="success",
			type="redirect",location="./pages/base/courier.html")})
	public String save() {
		courierService.save(courier);
		return SUCCESS;
	}
	
	
	@Action(value="courier_Pagequery",results= {@Result(name="success",
			type="json")})
	public String Pagequery() {
		//条件查询
		Specification<Courier> specification = new Specification<Courier>() {

			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				//多条件查询
				List<Predicate> list = new ArrayList<>();
				//单表查询
				if(StringUtils.isNotBlank(courier.getCourierNum())) {
					//通过派送员工号去查找  类似与  where id = ?;
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class),courier.getCourierNum());
					list.add(p1);
				}
				if(courier.getStandard()!=null && StringUtils.isBlank(courier.getStandard().getName())) {
					//多表联合查询。 from standard and courier where standard.name like '%?%';
					Join<Object,Object> StandardRoot = root.join("standard",JoinType.INNER);
					Predicate p2 = cb.like(StandardRoot.get("name").as(String.class),"%" + 
							courier.getStandard().getName() + "%");
					list.add(p2);
				}
				if(StringUtils.isNotBlank(courier.getCompany())) {
					//通过公司名称模糊查询 类似于 like '%?%';
					Predicate p3 = cb.like(root.get("company").as(String.class),
							       "%" + courier.getCompany() + "%");
					list.add(p3);
				}
				if(StringUtils.isNotBlank(courier.getType())) {
					//通过类型等值查询 类似于 where type = ?;
					Predicate p4 = cb.equal(root.get("type").as(String.class),
								   courier.getType());
					list.add(p4);
				}
				
				Predicate predicate = cb.and(list.toArray(new Predicate[0]));
				return predicate;
				
			}
		
			
		};
		
		Pageable pageable = new PageRequest(page-1,rows);
		Page<Courier> page1 = courierService.findByPage(specification,pageable);
		Map<String,Object> result = new HashMap<>();
		result.put("total", page1.getNumberOfElements());
		result.put("rows", page1.getContent());
		
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	
}
