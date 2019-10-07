package com.company.demo.crawler;

import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.company.demo.dto.Article;

@Component
public class WikitreeCrawler extends Crawler {
	
	WikitreeCrawler() {
		
		// 최초에 한번 갱신
		history = new ArrayList<>();
		
		// 초기 셋팅 (셋팅안하면 크롤링 못함)
		sourceId = -1; 
		limitDate = null; 
		
		// 크롤링 셋팅
		needToCollectDataOnNextPage = true;
		crawlingPage = 1;
		errorMsg = "새로운 기사 없음";  // 크롤링 끝날 때까지 다른 에러메시지 리턴안되면 크롤링 할 기사 null인 것
		
		// 크롤러에 lastDate, sourceId 셋팅해줘야 크롤링 가능
	}
	
	@Override
	public List<Article> getArticlesFromOnePage(int crawlingPage) {

		List<Article> collectedArticles = new ArrayList<>();
		
		String nc_id = listUrl.split("=")[1];
		String page =  Integer.toString(crawlingPage);
		String cpage = listUrl.split("\\?")[0];
		
        Document listPage = null;
		JSONObject obj = null;
		
		try {
			
			// https://www.wikitree.co.kr/main/list.php?nc_id=81~2
			listPage = Jsoup.connect("https://www.wikitree.co.kr/main/list_process.php")
					.data("page",page)
					.data("nc_id",nc_id)
					.data("list_sum","30")
					.data("notCtg","5, 14 ,12 ,4")
					.data("cpage","/main/list.php")
					.ignoreContentType(true)
					.post();
			
			
			JSONParser parser = new JSONParser(); 
			obj = (JSONObject) parser.parse(listPage.text());
			
			JSONArray list = (JSONArray) obj.get("list"); 
			JSONObject article; 

			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			
			String id = "";
			String webPath = "";
			Date regDate = null;
			String writer = "";
			String title = "";
			String body = "";
			
			for(int i = 0 ; i < list.size(); i++) { 

				// <Article>
		    	// 1.id | 2.regDate | 3.sourceId | 4.webPath (detailUrl) | 5.writer | 6.title | 7.body
				Article collectedArticle = new Article();
				
				article = (JSONObject) list.get(i); 
				
				
				webPath = siteUrl + (String)article.get("link"); //siteUrl 하위 링크 (/부터 id까지)
				id = webPath.split("=")[1].trim();
				regDate = formatter.parse((String)article.get("ar_writedate"));
				title = (String)article.get("ar_dptitle");
				writer = (String)article.get("ar_name"); //이메일은 본문 파야함
				body = (String)article.get("ar_content"); // 태그 그대로임
				
				if (regDate.compareTo(limitDate) < 1) {
					needToCollectDataOnNextPage = false;
					break;
				}
				
				collectedArticle.setId(id);
				collectedArticle.setRegDate(regDate);
				collectedArticle.setSourceId(sourceId); // 1: 인사이트-패션
		    	collectedArticle.setWebPath(webPath); 
		    	collectedArticle.setTitle(title);
		    	collectedArticle.setWriter(writer);
		    	collectedArticle.setBody(body); 
		    	
		    	collectedArticles.add(collectedArticle);
			}
	    	
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return collectedArticles;
	}
}