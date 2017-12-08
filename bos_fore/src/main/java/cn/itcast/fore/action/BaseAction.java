package cn.itcast.fore.action;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(BaseAction.class);
	
	//因为要实例化它，所以必须在创建该对象的时候创建，而泛型又得不到这个值，所以必须通过反射得到这个对象。
	protected T model;
	protected int page;
	protected int rows;
	
	
	
	public void setPage(int page) {
		this.page = page;
	}


	public void setRows(int rows) {
		this.rows = rows;
	}


	@Override
	public T getModel() {
		return model;
	}
	
	
	public BaseAction() {
		
		Type superClass = this.getClass().getGenericSuperclass();
		ParameterizedType parameterizedType = (ParameterizedType)superClass;
		@SuppressWarnings("unchecked")
		Class<T> modelClass = (Class<T>)parameterizedType.getActualTypeArguments()[0];
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException e) {
			logger.error("实例化错误");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			logger.error("类型不匹配");
			e.printStackTrace();
		}
	}
	
	protected void pushPageDataToValueStack(Page<T> pageData) {
		Map<String,Object> map = new HashMap<>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		ActionContext.getContext().getValueStack().push(map);
	}

}
