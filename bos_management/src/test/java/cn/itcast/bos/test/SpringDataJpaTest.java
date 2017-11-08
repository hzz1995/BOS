package cn.itcast.bos.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SpringDataJpaTest {
	@Autowired
	private StandardRepository standardRepository;
	@Test
	public void test() {
		System.out.println(standardRepository.findByName("40-60"));
	}
	
	@Test
	public void test1() {
		System.out.println(standardRepository.query("30-40"));
	}
	
	@Test
	public void test2() {
		System.out.println(standardRepository.query1("40-60"));
	}
	
	//修改
	@Test
	@Transactional
	@Rollback(false)
	public void test3() {
		standardRepository.updateMinLength(1,20);
	}
}
