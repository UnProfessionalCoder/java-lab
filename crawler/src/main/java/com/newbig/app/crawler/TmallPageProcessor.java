package com.newbig.app.crawler;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

public class TmallPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(1).setSleepTime(100);

    public void process(Page page) {
//        page.putField("title", page.getHtml().xpath("//div[@class='tb-detail-hd']/h1/a/text()"));
//        page.putField("title2", page.getHtml().xpath("//div[@class='tb-detail-hd']/p/text()"));
//        page.putField("renqi", page.getHtml().xpath("//span[@id='J_CollectCount']/text()"));
//        page.putField("shop", page.getHtml().xpath("//div[@id='shopExtra']/div[@class='slogo']/a[@class='slogo-shopname']/strong"));
//        page.putField("shopUrl", page.getHtml().xpath("//div[@id='shopExtra']/div[@class='slogo']/a[@class='slogo-shopname']"));
//        page.putField("shopId", page.getHtml().xpath("//div[@id='LineZing']"));
        String rawText = page.getRawText();
        Page p = new Page();
        p.setHtml(new Html(rawText));
        List<String> urls = p.getHtml().links().all();
        if (urls.size() > 0) {
            site.setDisableCookieManagement(Boolean.FALSE);
            site.getCookies().clear();
            site.getHeaders().clear();

            for (String url : urls) {
                System.out.println(url);
//                page.addTargetRequests(Lists.newArrayList(url));
            }
        }
    }

    public Site getSite() {

        return site;
    }
}
