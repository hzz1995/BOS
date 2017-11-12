package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierDao;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {

	@Autowired
	private CourierDao courierDao;
	
	//保存或更新相应的派送员对象
	@Override
	public void save(Courier courier) {
		courierDao.save(courier);
	}
	
	//通过分页返回当前页显示对象。
	@Override
	public Page<Courier> findByPage(Specification<Courier> specification,Pageable pageable) {
		Page<Courier> all = courierDao.findAll(specification,pageable);
		return all;
	}

	@Override
	public void updateBatch(String[] id) {
		for (String string : id) {
			Integer id_a = Integer.parseInt(string);
			courierDao.updateDel(id_a);
		}
	}
	
}
