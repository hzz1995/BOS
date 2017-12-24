package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierDao;
import cn.itcast.bos.dao.base.FixedAreaDao;
import cn.itcast.bos.dao.base.TakeTimeDao;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.FixedAreaService;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {

	@Autowired
	private FixedAreaDao fixedAreaDao;
	@Autowired
	private CourierDao courierDao;
	@Autowired
	private TakeTimeDao takeTimeDao;
	@Override
	public void save(FixedArea fixedArea) {
		fixedAreaDao.save(fixedArea);
	}

	@Override
	public Page<FixedArea> findAllByPage(Specification<FixedArea> specification,Pageable pageable) {
		Page<FixedArea> list = fixedAreaDao.findAll(specification,pageable);
		return list;
	}

	@Override
	public void associationCourierToFixedArea(FixedArea model, Integer courierId, String takeTimeId) {
		FixedArea fixedArea = fixedAreaDao.findOne(model.getId());
		Courier courier = courierDao.findOne(courierId);
		TakeTime takeTime = takeTimeDao.findOne(Integer.parseInt(takeTimeId));
		
		//关联
		fixedArea.getCouriers().add(courier);
		
		courier.setTakeTime(takeTime);
		
	}

}
