package com.company.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

	//@Scheduled(cron = "* * * * * ?")
	public void cronJobSch() {
		System.out.println("Current Thread : " + Thread.currentThread().getName() );
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start = new Date();
		String startDate = sdf.format(start);
		System.out.println("Java cron schedule start:: " + startDate);
//		try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
		
		Date end = new Date();
		String endDate = sdf.format(end);
		System.out.println("Java cron schedule stop:: " + endDate);
	}
	
	//@Scheduled(fixedDelay = 5000) 
	public void scheduleFixedRateTask() {
		System.out.println("Current Thread : " + Thread.currentThread().getName() );
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		String strDate = sdf.format(now);
		System.out.println("Fixed Rate Task:: " + strDate);
	}
}
