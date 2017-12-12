package cn.itcast.bos.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

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
public class UploadImageAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private File imgFile;
	private String imgFileFileName;
	private String imgFileContentType;
	
	public File getImgFile() {
		return imgFile;
	}

	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public String getImgFileFileName() {
		return imgFileFileName;
	}

	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	public String getImgFileContentType() {
		return imgFileContentType;
	}

	public void setImgFileContentType(String imgFileContentType) {
		this.imgFileContentType = imgFileContentType;
	}

	/**
	 * 上传文件
	 * @return
	 */
	@Action(value="image_upload",results= {@Result(name="success",type="json")})
	public String upload() {
		System.out.println("文件名:" + imgFileFileName);
		//保存图片的物理位置
		String realPath = ServletActionContext.getServletContext().getRealPath("") +
				"\\upload\\";
		String projectPath = ServletActionContext.getServletContext().getContextPath()+
				"/upload/";
		
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String suffix = imgFileFileName.substring(imgFileFileName.lastIndexOf('.'),imgFileFileName.length());
		
		//新文件名
		String imgNewName = uuid + suffix;
		
		File newFile = new File(realPath + imgNewName);
		System.out.println(newFile);
		try {
			//上传图片到物理硬盘
			FileUtils.copyFile(imgFile, newFile);
		} catch (IOException e) {
			System.out.println("上传失败");
			e.printStackTrace();
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("error", 0);
		map.put("url", projectPath +"/" +imgNewName);
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	@Action(value="image_manage",results= {@Result(name="success",type="json")})
	public String findImage() {
		//根目录路径，可以指定绝对路径，比如 /var/www/attached/
		String rootPath = ServletActionContext.getServletContext().getRealPath("/") + "upload/";
		//根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
		String rootUrl  = ServletActionContext.getRequest().getContextPath() + "/upload/";
		//图片扩展名
		String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};
		File currentPathFile = new File(rootPath);
		//遍历目录取的文件信息
		List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
		if(currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Hashtable<String, Object> hash = new Hashtable<String, Object>();
				String fileName = file.getName();
				if(file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if(file.isFile()){
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
				fileList.add(hash);
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("moveup_dir_path", "");
		result.put("current_dir_path", rootPath);
		result.put("current_url", rootUrl);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	
}
