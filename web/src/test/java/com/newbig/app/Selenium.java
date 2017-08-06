package com.newbig.app;

import org.junit.Ignore;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;


@Ignore
public class Selenium {

    public static void main(String[] args) {
        System.out.println("Seting browser");
        System.setProperty("webdriver.chrome.driver","/opt/soft/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(480, 800));
        driver.get("http://www.baidu.com");
        // 获取 网页的 title
        System.out.println("1 Page title is: " + driver.getTitle());
        // 通过 id 找到 input 的 DOM
        WebElement element = driver.findElement(By.id("kw"));
        // 输入关键字
        element.sendKeys("zTree");
        // 提交 input 所在的  form
        element.submit();
        // 通过判断 title 内容等待搜索页面加载完毕，Timeout 设置10秒
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("ztree");
            }
        });

        // 显示搜索结果页面的 title
        System.out.println("2 Page title is: " + driver.getTitle());
        String FirstUrl = "http://www.baidu.com";
        System.out.printf("now access %s \n ",FirstUrl);
        driver.get(FirstUrl);

        String SecondUrl = "http://news.baidu.com";
        System.out.printf("now access %s \n ",SecondUrl);
        driver.get(SecondUrl);

        System.out.printf("now back to %s \n",FirstUrl);
        driver.navigate().back();

        System.out.printf("forward to %s \n",SecondUrl);
        driver.navigate().forward();

        System.out.println("模拟浏览器刷新");
        driver.navigate().refresh();
        driver.get("http://www.baidu.com");

        //输入框输入内容
        WebElement input = driver.findElement(By.id("kw"));
        input.sendKeys("Seleniumm");

        System.out.println("删除多输入的一个m");
        input.sendKeys(Keys.BACK_SPACE);

        System.out.println("输入空格键+“课程”");
        input.sendKeys(Keys.SPACE);
        input.sendKeys("教程");

        System.out.println("ctrl+a全选输入框内容");
        input.sendKeys(Keys.CONTROL,"a");

        System.out.println("ctrl+x剪切输入框内容");
        input.sendKeys(Keys.CONTROL,"x");

        System.out.println("ctrl+v粘贴内容到输入框");
        input.sendKeys(Keys.CONTROL,"v");

        System.out.println("通过回车键盘来代替点击操作");
        input.sendKeys(Keys.ENTER);

        driver.quit();
    }
}
