package cn.itcast.bos.service.base.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.PromotionDao;
import cn.itcast.bos.domain.base.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.base.PromotionService;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private PromotionDao promotionDao;
	@Override
	public void save(Promotion promotion) {
		promotionDao.save(promotion);
	}
	@Override
	public Page<Promotion> findAll(Pageable pageable) {
		Page<Promotion> all = promotionDao.findAll(pageable);
		return all;
	}
	@Override
	public PageBean<Promotion> findPageQuery(int page, int rows) {
		Pageable pageable = new PageRequest(page-1,rows);
		Page<Promotion> all = promotionDao.findAll(pageable);
		
		long totalSize = all.getTotalElements();
		List<Promotion> list = all.getContent();
		
		//封装数据
		PageBean<Promotion> pageBean = new PageBean<Promotion>();
		pageBean.setTotalCount(totalSize);
		pageBean.setListData(list);
		return pageBean;
	}
	@Override
	public Promotion findById(int id) {
		//如果用getOne(id),会报类型转换错误。
		Promotion promotion = promotionDao.findOne(id);
		return promotion;
	}
	
	/**
	 * 修改活动日期
	 * 		1：进行中
	 * 		2：已结束
	 */
	@Override
	public void updateStatus(Date date) {
		promotionDao.updateStatus(date);
		
	}

}
