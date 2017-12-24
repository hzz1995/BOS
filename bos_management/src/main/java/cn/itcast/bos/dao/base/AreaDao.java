package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.base.Area;

public interface AreaDao extends JpaRepository<Area,String>,JpaSpecificationExecutor<Area> {
	/**
	 * 通过省市区找到区域id
	 * @param province
	 * @param city
	 * @param district
	 * @return
	 */
	Area findByProvinceAndCityAndDistrict(String province, String city, String district);
}
