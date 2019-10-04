package com.company.demo.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.company.demo.WebCrawlingTest4Application;
import com.company.demo.crawler.CrawlingInfo;
import com.company.demo.crawler.InsightCrawler;
import com.company.demo.crawler.WikitreeCrawler;
import com.company.demo.dto.Article;
import com.company.demo.service.CrawlingService;

@Controller
public class CrawlingController {
	@Autowired
	CrawlingService crawlingService;
	@Autowired
	InsightCrawler insightCrawler;
	@Autowired
	WikitreeCrawler wikitreeCrawler;
	
	private static Logger logger = LoggerFactory.getLogger(WebCrawlingTest4Application.class);
	
	private int SOURCEID_insight_fashion = 1;
	private int SOURCEID_insight_food = 2;
	private int SOURCEID_wikitree_fashion = 3;
	private int SOURCEID_wikitree_food = 4;
	
	// DB에서 소스아이디에 해당하는 정보를 가져와서 여기서 크롤러에 넣는 식으로 진행
	
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		//DB 비울까 말까?
		crawlingService.resetDB();
		
		logger.info("Started initial web Crawling to insigt / thread : {}",Thread.currentThread().getName());
		executeInsightCrawling();
		
		logger.info("Started initial web Crawling to wikitree - thread : {}",Thread.currentThread().getName());
		executeWikitreeCrawling();
	}

	@Scheduled(fixedDelay = 172800000) // 2틀마다 실행 (수정 가능성 큰 주기)
	public void executeUpdateCrawlingEveryTwoDays() {
		
		logger.info("Start Scheduling Thread : {}", Thread.currentThread().getName());
		
		// 모든 사이트 수정 크롤러
		// 그냥 정해진 시작점부터 쭉 수정해나가는 크롤러 (경우의 수? 또는 모두 업데이트 해버리기?)
		// 끼어들기, 삭제, 수정

		logger.info("End Scheduling Thread");
	}
	
	@Scheduled(cron = "0 0 10 15W * ?")  // 한달에 15일에 가까운 평일에 한 번 실행 (수정 가능성 희박한 주기)
	public void executeUpdateCrawling1stDayOfEveryMonth() {
		
		logger.info("Start Scheduling Thread : {}", Thread.currentThread().getName());
		
		// 모든 사이트 수정 크롤러

		logger.info("End Scheduling Thread");
	}
	
	@Scheduled(cron = "0 40 * * * MON-FRI") // 평일 정각마다 돌아감
	public void executeInsightCrawlingEveryHour() {
		
		logger.info("Start Scheduling Thread : {}", Thread.currentThread().getName());
		
		executeInsightCrawling();

		logger.info("End Scheduling Thread");
	}
	
	@Scheduled(cron = "0 40 * * * MON-FRI") // 평일 정각마다 돌아감
	public void executeWikitreeCrawlingEveryHour() {

		logger.info("Start Scheduling Thread : {}", Thread.currentThread().getName());
		
		executeWikitreeCrawling();
		
		logger.info("crawling history : {}", wikitreeCrawler.getHistory().get(wikitreeCrawler.getHistory().size() - 1));
	}
	
	@RequestMapping("crawlInsight")
	@ResponseBody
	public String executeInsightCrawling() {
		// -------------인사이트-패션 크롤링-----------------
		
		// DB의 최신 데이터 날짜 셋팅해줌 (null: 일주일 전부터, lastDate: 이어서)
		Date lastDate = crawlingService.getLastDate(SOURCEID_insight_fashion);
		insightCrawler.setLimitDate(lastDate);
		
		insightCrawler.setSourceId(SOURCEID_insight_fashion);
		
		List<Article> articles = insightCrawler.crawling();
		
		if (articles.size() != 0 && articles != null) { // 수집한 기사 있음
			
			crawlingService.collectData(articles);
			
		} else if (articles != null){ // 수집한 기사 없음

			logger.info(insightCrawler.getErrorMsg());
			return insightCrawler.getErrorMsg();
			
		} else if (articles == null){ // 에러
			
			logger.error(insightCrawler.getErrorMsg());
			return insightCrawler.getErrorMsg();
		}
		
		// -------------인사이트-푸드 크롤링-----------------
		
		// DB의 최신 데이터 날짜 셋팅해줌 (null: 일주일 전부터, lastDate: 이어서)
		Date lastDate2 = crawlingService.getLastDate(SOURCEID_insight_food);
		insightCrawler.setLimitDate(lastDate2);
		
		insightCrawler.setSourceId(SOURCEID_insight_food);
		
		List<Article> articles2 = insightCrawler.crawling();
		
		if (articles.size() != 0 && articles != null) { // 수집한 기사 있음
			
			crawlingService.collectData(articles2);
			
		} else if (articles != null){ // 수집한 기사 없음

			logger.info(insightCrawler.getErrorMsg());
			return insightCrawler.getErrorMsg();
			
		} else if (articles == null){ // 에러
			
			logger.error(insightCrawler.getErrorMsg());
			return insightCrawler.getErrorMsg();
		}
	
		return "All Insight Crawling done successfully";
	}

	@RequestMapping("crawlWikitree")
	@ResponseBody
	public String executeWikitreeCrawling() {
		// -------------위키트리-패션 크롤링-----------------
		
		// DB의 최신 데이터 날짜 셋팅해줌 (null: 일주일 전부터, lastDate: 이어서)
		Date lastDate = crawlingService.getLastDate(SOURCEID_wikitree_fashion);
		wikitreeCrawler.setLimitDate(lastDate);
		
		wikitreeCrawler.setSourceId(SOURCEID_wikitree_fashion);
		
		List<Article> articles = wikitreeCrawler.crawling();

		if (articles.size() != 0 && articles != null) { // 수집한 기사 있음

			crawlingService.collectData(articles);
			
		} else if (articles != null){ // 수집한 기사 없음

			logger.info(wikitreeCrawler.getErrorMsg());
			return wikitreeCrawler.getErrorMsg();
			
		} else { // 에러
			
			logger.error(wikitreeCrawler.getErrorMsg());
			return wikitreeCrawler.getErrorMsg();
		}
		
		// -------------위키트리-푸드 크롤링-----------------
		
		// DB의 최신 데이터 날짜 셋팅해줌 (null: 일주일 전부터, lastDate: 이어서)
		Date lastDate2 = crawlingService.getLastDate(SOURCEID_wikitree_food);
		wikitreeCrawler.setLimitDate(lastDate2);
		
		wikitreeCrawler.setSourceId(SOURCEID_wikitree_food);
		
		List<Article> articles2 = wikitreeCrawler.crawling();
		
		if (articles.size() != 0 && articles != null) { // 수집한 기사 있음
			
			crawlingService.collectData(articles2);
			
		} else if (articles != null){ // 수집한 기사 없음

			logger.info(wikitreeCrawler.getErrorMsg());
			return wikitreeCrawler.getErrorMsg();
			
		} else if (articles == null){ // 에러
			
			logger.error(wikitreeCrawler.getErrorMsg());
			return wikitreeCrawler.getErrorMsg();
		}
	
		return "All Wikitree Crawling done successfully";
	}
	
	@RequestMapping("checkAll")
	@ResponseBody
	public Map<String,Object> showAllCrawlingProgress() {
		
//	작동하고 있는 스케줄링된 인사이트 크롤러들의 히스토리를 모두 체크
		
		Map<String,Object> allProgress = new HashMap<>();
		
		allProgress.put("insight-history",insightCrawler.getHistory());
		allProgress.put("wikitree-history",wikitreeCrawler.getHistory());
		
		return allProgress;
	}
	
	@RequestMapping("checkInsight")
	@ResponseBody
	public List<CrawlingInfo> showInsightCrawlingProgress() {
		
//		작동하고 있는 스케줄링된 인사이트 크롤러들의 히스토리를 모두 체크
		
		return insightCrawler.getHistory();
	}
	
	@RequestMapping("checkWikitree")
	@ResponseBody
	public List<CrawlingInfo> showWikitreeCrawlingProgress() {
		
//		작동하고 있는 스케줄링된 위키트리 크롤러들의 히스토리를 모두 체크
		System.out.println(wikitreeCrawler.getHistory());
		
		return wikitreeCrawler.getHistory();
	}
}