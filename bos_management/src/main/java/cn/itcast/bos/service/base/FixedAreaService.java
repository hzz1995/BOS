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
}
