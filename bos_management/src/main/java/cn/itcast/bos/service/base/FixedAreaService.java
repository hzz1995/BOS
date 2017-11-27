package cn.itcast.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.FixedArea;

/**
 * 定区的增删改查
 * @author 胡正再
 *
 */
public interface FixedAreaService {
    void save(FixedArea model);

	Page<FixedArea> findAllByPage(Specification<FixedArea> specification, Pageable pageable);
	/**
	 * 实现了两个功能，定区关联快递员，快递员关联派送时间
	 * @param model
	 * @param courierId
	 * @param takeTimeId
	 */
	void associationCourierToFixedArea(FixedArea model, Integer courierId, String takeTimeId);
}
