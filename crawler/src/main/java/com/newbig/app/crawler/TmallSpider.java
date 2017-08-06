package com.newbig.app.crawler;

import org.joda.time.DateTime;
import us.codecraft.webmagic.Spider;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class TmallSpider {
    public static void main(String[] args) {
//        Spider.create(new TmallPageProcessor()).addUrl("https://chaoshi.tmall.com/").run();
//        for(int i=1;i<100;i++) {
//            Spider.create(new TmallCommentProcessor()).addUrl("https://rate.tmall.com/list_detail_rate.htm?itemId=531938869586&spuId=574538702&sellerId=358484020&order=3&currentPage=" + i + "&append=0&content=1&tagId=&posi=&picture=&ua=047UW5TcyMNYQwiAiwQRHhBfEF8QXtHcklnMWc%3D%7CUm5Ockt%2FRX9LdkxwRHBMeC4%3D%7CU2xMHDJ7G2AHYg8hAS8XIw0tA0UkQj5PYTdh%7CVGhXd1llXGhSaFxhW2dTZ1tvWGVHe0N6QHRIdE11SXdNcUh2THdZDw%3D%3D%7CVWldfS0SMgwzCCgUIQEvUyhOYgszBXFfCV8%3D%7CVmhIGCYfPwY9HSEdKRQ0DzsDOBgkEC8SMg4zBjsbJxIoEjIOMwswZjA%3D%7CV25OHjAePgQ8ASEdJRksDDkFPgNVAw%3D%3D%7CWGFBET8RMQsyDy8TKxMuDjsPNQ5YDg%3D%3D%7CWWBAED4QMAwwCDERLRElGjoGPwE0ClwK%7CWmNDEz0TMwY4BiYaIBgjAz8HOg8yZDI%3D%7CW2JCEjwSMgg3Di4RLxsgADwGOwU8ajw%3D%7CXGVFFTsVNQ8xBSUaIR0hAT0HPAQ%2FaT8%3D%7CXWVFFTsVNWVfZV9%2FQHtHelpgWG1PcUtzU2ldYUF%2FRWVbbzkZJAQqBCQYLREuEEYQ%7CXmdaZ0d6WmVFeUB8XGJaYEB5WWVYeExsWXlDY1h4QGBcYjQ%3D&isg=AtXVAMRaIFK77gRCIm1GBdOH5NhPeomMnsw6mVd6V8yWrvSgHyAGtBpsBoXi&needFold=0&_ksTS=1497790738214_1514").run();
//        }
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        DateTime dateTime = new DateTime();
//        Calendar c = Calendar.getInstance();
        c.setTime(new Date());//要把时间设置进去嘛
        System.out.println( c.get(Calendar.WEEK_OF_YEAR));
        System.out.println(dateTime.getWeekOfWeekyear());
    }
}
