package cn.itcast.fore.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestRedis {

	@Autowired
	RedisTemplate<String, String> redisTemplate;
	@Test
	public void test() {
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		opsForValue.set("123", "实现理想");
		
		System.out.println(redisTemplate.opsForValue().get("123"));
		//redisTemplate.delete("胡正再");
		System.out.println(redisTemplate.opsForValue().get("123"));
		//redisTemplate.opsForValue().set("hzz", "ni", 30, TimeUnit.MINUTES)
	}
	
	
	@Test
	public void quartzTest() throws SchedulerException {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("hzz1","trigger2").build();
		
		
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1","trigger2")
				.startNow().withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(5).repeatForever()).build();
		
		scheduler.scheduleJob(job, trigger);
		scheduler.start();
		
	}

}


class HelloJob implements Job {
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("梁迪龙");
		
	}
	
}
