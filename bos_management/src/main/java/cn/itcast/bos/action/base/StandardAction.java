package cn.itcast.bos.action.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;


@ParentPackage("json-default")
@Namespace(value="/")
@Controller
@Scope("prototype")
@Actions
public class StandardAction extends ActionSupport implements ModelDriven<Standard>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Standard standard = new Standard();
	//注入service对象
	@Autowired
	private StandardService standardService;
	
	private int page;
	private int rows;
	
	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Override
	public Standard getModel() {
		return standard;
	}
	
	//派送标准的添加
	@Action(value="standard_save",results={@Result(name="success",
			type="redirect",
				location="./pages/base/standard.html")})
	public String save() {
		System.out.println("添加收派标准");
		standardService.save(standard);
		return SUCCESS;
	}
	
	//派送标准的分页查询
	@Action(value="standard_pageFind",results= {@Result(name="success",
			type="json")})
	public String pageFind() {
		
		//根据业务层的方法进行分页查询
		Page<Standard> pages = standardService.findByPage(page-1,rows);
		Map<String,Object> result = new HashMap<>();
		result.put("total", pages.getNumberOfElements());
		result.put("rows",pages.getContent());
		
		//在把结果放到堆栈中
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	
	
	@Action(value="standard_findAll",results= {@Result(name="success",type="json")})
	public String findAll() {
		List<Standard> standards = standardService.findAll();
		ActionContext.getContext().getValueStack().push(standards);
		return SUCCESS;
	}
}
