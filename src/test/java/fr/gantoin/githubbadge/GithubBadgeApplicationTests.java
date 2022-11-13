package fr.gantoin.githubbadge;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GithubBadgeApplicationTests {

    public static final String HTTP_GITHUB_COM_GANTOIN = "http://github.com/gantoin";

    @Test
    void testScreenShot() throws Exception {
        WebDriver driver;
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);
        driver = new ChromeDriver(chromeOptions);
        Dimension dimension = new Dimension(1920, 1080);
        driver.manage().window().setSize(dimension);
        driver.get(HTTP_GITHUB_COM_GANTOIN);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1500)", "");
        File screenshotAs = driver.findElement(By.xpath("//*[@id=\"user-profile-frame\"]")) //
                .findElement(By.className("js-calendar-graph-svg")) //
                .findElement(By.xpath("./..")) //
                .getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotAs, new File("/tmp/particularElementScreenshot.PNG"));
        driver.quit();
    }
}
