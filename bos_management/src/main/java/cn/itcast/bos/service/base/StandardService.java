package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Standard;
/**
 * 收派标准的增删改查
 * @author 胡正再
 *
 */
public interface StandardService {
	/**
	 * 保存
	 * @param standard
	 */
	void save(Standard standard);
}
