package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaDao;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {
	
	@Autowired
	private AreaDao areaDao;
	@Override
	public void save(List<Area> list) {
		areaDao.save(list);
	}
	@Override
	public Page<Area> findAll(Specification<Area> specification, Pageable pageable) {
		Page<Area> list = areaDao.findAll(specification, pageable);
		return list;
	}

}
