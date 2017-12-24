package cn.itcast.bos.job;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.service.base.PromotionService;

public class PromotionJob implements Job {

	@Autowired
	private PromotionService promotionService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("定时器执行");
		promotionService.updateStatus(new Date());
	}
	
	

}
