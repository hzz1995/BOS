package cn.itcast.bos.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;

@ParentPackage("struts-default")
@Namespace("/")
@Controller
@Scope("prototype")
@Actions
public class AreaAction extends ActionSupport implements ModelDriven<Area>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Area area;
	private File file;
	
	@Autowired
	private AreaService areaService;
	
	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public Area getModel() {
		return area;
	}
	
	@Action(value="area_importBatch")
	public String importBatch() throws Exception {
		System.out.println(file);
		List<Area> list = new ArrayList<>();
		//创建一个excel对象
		@SuppressWarnings("resource")
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
		//得到一个工作簿
		HSSFSheet createSheet = hssfWorkbook.getSheetAt(0);
		
		
		for (Row row : createSheet) {
			Area area = new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			
			
			list.add(area);
		}
		areaService.save(list);
		
		//
		return NONE;
	}
}
