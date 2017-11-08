package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardRepository;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

@Service
@Transactional
public class StandardServiceImpl implements StandardService {
	
	@Autowired
	private StandardRepository standardRepository;
	
	@Override
	public void save(Standard standard) {
		standardRepository.save(standard);
	}

	@Override
	public Page<Standard> findByPage(int total, int rows) {
		Pageable pageable = new PageRequest(total,rows);
		Page<Standard> page = standardRepository.findAll(pageable);
		return page;
	}

	//查询所有派送标准
	@Override
	public List<Standard> findAll() {
		List<Standard> list = standardRepository.findAll();
		return list;
	}

}
