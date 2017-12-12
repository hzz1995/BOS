package cn.itcast.bos.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
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
import org.springframework.stereotype.Controller;

import cn.itcast.bos.action.base.BaseAction;
import cn.itcast.bos.domain.base.Promotion;
import cn.itcast.bos.service.base.PromotionService;

/**
 * 活动模块的kindEditor部分功能实现
 * 1.文件上传。
 * 2.查看服务器文件的所有文件
 * @author 胡正再
 *
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
@Actions
public class ActivityAction extends BaseAction<Promotion> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private File titleImgFile;
	private String titleImgFileFileName;
	private String titleImgFileContentType;
	
	public File getTitleImgFile() {
		return titleImgFile;
	}

	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}

	public String getTitleImgFileFileName() {
		return titleImgFileFileName;
	}
	
	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}
	
	public String getTitleImgFileContentType() {
		return titleImgFileContentType;
	}

	public void setTitleImgFileContentType(String titleImgFileContentType) {
		this.titleImgFileContentType = titleImgFileContentType;
	}
	@Autowired
	private PromotionService promotionService;

	
	//这里不跳转，一直没反应,求解决
	@Action(value="promotion_save",results= {
			@Result(name="success",type="redirect",location="www.baidu.com"),
			@Result(name="input",location="./pages/error.jsp")})
	public String save() {
		//根目录路径，可以指定绝对路径，比如 /var/www/attached/
		String rootPath = ServletActionContext.getServletContext().getRealPath("/") + "upload/";
		//根目录URL，可以指定绝对路径，比如 http://www.	yoursite.com/attached/
		String rootUrl  = ServletActionContext.getRequest().getContextPath() + "/upload/";
		
		System.out.println(titleImgFile);
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String suffix = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf('.'));
		String fileName = uuid + suffix;
		String newPath = rootPath + fileName;
		File newFile = new File(newPath);
		try {
			FileUtils.copyFile(titleImgFile, newFile);
		} catch (IOException e) {
			System.out.println(ActivityAction.class + "上传出错!");
			e.printStackTrace();
		}
		model.setTitleImg(rootUrl + fileName);
		promotionService.save(model);
		
		return SUCCESS;
	}
	
	@Action(value="promotion_findAll",results= {@Result(name="success",type="json")})
	public String findAll() {
		Pageable pageable= new PageRequest(page-1, rows);
		Page<Promotion> list = promotionService.findAll(pageable);
		pushPageDataToValueStack(list);
		return SUCCESS;
	}
	
}
