package cn.itcast.bos.service.base;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;

public interface PromotionService {
	/**
	 * 保存一个活动对象
	 * @param promotion
	 */
	void save(Promotion promotion);

	/**
	 * 分页查询所有数据
	 * @param pageable
	 * @return
	 */
	Page<Promotion> findAll(Pageable pageable);
	
	/**
	 * 设置远程连接，得到分页数据
	 */
	
	@Path("/pageQuery")
	@GET	
	@Produces({"application/xml","application/json"})
	PageBean<Promotion> findPageQuery(
			    @QueryParam("page") int page,
			    @QueryParam("rows") int rows
			);
	
	@Path("/findById")
	@GET	
	@Produces({"application/xml","application/json"})
	Promotion findById(@QueryParam("id") int id);

	void updateStatus(Date date);
	
}
