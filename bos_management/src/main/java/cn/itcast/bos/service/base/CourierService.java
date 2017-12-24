package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Courier;

/**
 * 派送员的增删改查
 * @author 胡正再
 *
 */
public interface CourierService {
	/**
	 * 通过得到一个对象并去 保存到数据库
	 * @param courier
	 */
	void save(Courier courier);
	/**
	 * 通过发送过来的当前页数page,和当前页面显示的总记录数得到当前页的内容。
	 * @param specification 
	 * @param page
	 * @param rows
	 * @return
	 */
	Page<Courier> findByPage(Specification<Courier> specification, Pageable pageable);
	
	/**
	 * 批量作废数据
	 * @param id
	 */
	void updateDelBatch(String[] id);
	/**
	 * 查询所有未关联定区的快递员
	 * @return
	 */
	List<Courier> findnoassociation();
	
	/**
	 * 批量复原数据
	 * @param id
	 */
	void updateRestoryBatch(String[] id);
	
}
