package cn.itcast.crm.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.crm.dao.CustomerDao;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    
	@Autowired
	private CustomerDao customerDao;
	
	@Override
	public List<Customer> findNoAssociationCustomers() {
		List<Customer> list = customerDao.findByFixedAreaIdIsNull();
		return list;
	}

	@Override
	public List<Customer> findHasAssociationFixedAreaCustomers(String fixedAreaId) {
		List<Customer> list = customerDao.findByFixedAreaId(fixedAreaId);
		return list;
	}

	@Override
	public void associationCustomersToFixedArea(String customerIdStr, String fixedAreaId) {
		customerDao.clearFixedAreaId(fixedAreaId);
		
		//如果没有关联的用户，那么得到一个"null"值
		if("null".equals(customerIdStr) || StringUtils.isBlank(customerIdStr)) {
			return;
		}
		
		
		//传进来客户id的格式{1,2,3}
		String[] ids = StringUtils.split(customerIdStr,",");
		for (String idStr : ids) {
			Integer id = Integer.parseInt(idStr);
			customerDao.updateFixedAreaId(fixedAreaId, id);
		}
	}
	
	@Override
	public Customer customerLogin(String telephone, String password) {
		return customerDao.findByTelephoneAndPassword(telephone, password);
	}

	@Override
	public String findFixedAreaIdByAddress(String address) {
		
		return customerDao.findFixedAreaIdByAddress(address);
	}

}
