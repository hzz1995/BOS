package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

	/**
	 * 派送标准配送分页
	 * @param total
	 * @param rows
	 * @return
	 */
	Page<Standard> findByPage(int total, int rows);

	List<Standard> findAll();
}
