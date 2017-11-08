package cn.itcast.bos.dao.base;

import java.util.List;

//spring-data-jpa
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Standard;

public interface StandardRepository extends JpaRepository<Standard, Integer> {
	
	//第一种，按照命名规范查询
	public List<Standard> findByName(String name);
	
	//第二种，不按照命名规约，按照如下配置
	@Query(value="from Standard where name = ?1",nativeQuery=false)
	public List<Standard> query(String name);
	
	//第三种，不按照命名规约，在实体类中写入查询语句
	@Query
	public List<Standard> query1(String name);
	
	//第四种，不按照命名规约，按照如下配置
	@Query(value="update Standard set minLength=?2 where id = ?1")
	@Modifying
	public void updateMinLength(Integer id , Integer minLength);
	
}
