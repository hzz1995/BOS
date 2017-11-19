package cn.itcast.bos.action.base;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;


@ParentPackage("json-default")
@Namespace(value="/")
@Controller
@Scope("prototype")
@Actions
public class StandardAction extends BaseAction<Standard>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//注入service对象
	@Autowired
	private StandardService standardService;
	
	
	//派送标准的添加
	@Action(value="standard_save",results={@Result(name="success",
			type="redirect",
				location="./pages/base/standard.html")})
	public String save() {
		System.out.println("添加收派标准");
		standardService.save(model);
		return SUCCESS;
	}
	
	//派送标准的分页查询
	@Action(value="standard_pageFind",results= {@Result(name="success",
			type="json")})
	public String pageFind() {
		
		//根据业务层的方法进行分页查询
		Page<Standard> pages = standardService.findByPage(page-1,rows);
		pushPageDataToValueStack(pages);
		return SUCCESS;
	}
	
	
	//查询出所有的记录
	@Action(value="standard_findAll",results= {@Result(name="success",type="json")})
	public String findAll() {
		List<Standard> standards = standardService.findAll();
		ActionContext.getContext().getValueStack().push(standards);
		return SUCCESS;
	}
	
}
