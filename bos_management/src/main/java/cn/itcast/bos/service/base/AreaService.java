package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Area;

/**
 * 对区域进行增删改查
 * @author 胡正再
 *
 */
public interface AreaService {
	/**
	 * 将区域信息批量导入数据库
	 * @param list
	 */
	void save(List<Area> list);
	
	/**
	 * 根据分页查询
	 * @param specification 
	 * @param pageable
	 * @return
	 */
	Page<Area> findAll(Specification<Area> specification, Pageable pageable);
}
