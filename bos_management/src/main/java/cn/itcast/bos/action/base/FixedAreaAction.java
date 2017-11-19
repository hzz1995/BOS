package cn.itcast.bos.action.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;

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
	
	
	@Action(value="fixedArea_save",results= {
			@Result(name="success",location="../../pages/base/fixed_area.html")})
	public String save() {
		fixedAreaService.save(model);
		return SUCCESS;
	}
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

}
