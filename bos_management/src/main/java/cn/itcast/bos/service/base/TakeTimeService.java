package cn.itcast.bos.service.base;

import java.util.List;

import cn.itcast.bos.domain.base.TakeTime;
/**
 * 标准配送时间
 * @author 胡正再
 *
 */
public interface TakeTimeService {
	List<TakeTime> findAll();
}
