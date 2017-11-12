package cn.itcast.bos.service.base;

import java.util.List;

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
}
