package cn.itcast.bos.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
@Actions
public class AreaAction extends BaseAction<Area>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private File file;
	
	@Autowired
	private AreaService areaService;
	
	public void setFile(File file) {
		this.file = file;
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
		
	    //遍历每一行
		int i = 0;
		for (Row row : createSheet) {
			i++;
			// 一行数据对应 一个区域对象
			if (row.getRowNum() == 0) {
				// 第一行 跳过
				continue;
			}
			// 跳过空行
			if (row.getCell(0) == null
					|| StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
				continue;
			}
			Area area = new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			
			//得到省，市，区
			String province = area.getProvince();
			String city = area.getCity();
			String district  = area.getDistrict();
			//滤掉‘最后的量词’
			province = province.substring(0, province.length() - 1);
			city = city.substring(0, city.length() - 1);
			district = district.substring(0, district.length() - 1);
			
			//得到简码
			String[]city_code  = PinYin4jUtils.getHeadByString(province+city+district);
			StringBuffer buffer = new StringBuffer();
			for (String string : city_code) {
				buffer.append(string);
			}
			String shortcut = buffer.toString();
			area.setShortcode(shortcut);
			
			//得到城市编码
			String citycode = PinYin4jUtils.hanziToPinyin(city,"");
			area.setCitycode(citycode);
			list.add(area);
			
		}
		System.out.println("执行次数：" + i);
		areaService.save(list);
		
		
		return NONE;
	}
	
	@Action(value="area_pageQuery",results= {@Result(name="success",type="json")})
	public String Pagequery() {
		//获得条件
		Specification<Area> specification = new Specification<Area>() {

			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if(StringUtils.isNotBlank(model.getProvince())) {
					Predicate p1 = cb.equal(root.get("province").as(String.class), model.getProvince());
					list.add(p1);
				} else if(StringUtils.isNotBlank(model.getCity())) {
					Predicate p2 = cb.equal(root.get("city"), model.getCity());
					list.add(p2);
				} else if(StringUtils.isNotBlank(model.getDistrict())) {
					Predicate p3 = cb.equal(root.get("district"), model.getDistrict());
					list.add(p3);
				}
				Predicate p4 = cb.and(list.toArray(new Predicate[0]));
				return p4;
			}
		};
		
		Pageable pageable = new PageRequest(page-1,rows);
		Page<Area> list = areaService.findAll(specification,pageable);
		//将结果封装成json格式
		pushPageDataToValueStack(list);
		return SUCCESS;
	}
}

