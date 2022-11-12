package fr.gantoin.githubbadge;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GithubBadgeApplicationTests {

    public static void takeSnapShot(WebDriver webdriver, String fileWithPath) throws Exception {

        //Convert web driver object to TakeScreenshot

        TakesScreenshot scrShot = ((TakesScreenshot) webdriver);

        //Call getScreenshotAs method to create image file

        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

        //Move image file to new destination

        File DestFile = new File(fileWithPath);

        //Copy file at destination

        FileUtils.copyFile(SrcFile, DestFile);

    }

    @Test
    void contextLoads() {
    }

    @Test

    public void testGuru99TakeScreenShot() throws Exception {

        WebDriver driver;
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        driver = new ChromeDriver();

        //goto url
        driver.get("http://github.com/gantoin");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1500)", "");

        // Locate the element on the web page
        WebElement frame = driver.findElement(By.xpath("//*[@id=\"user-profile-frame\"]"));
        WebElement table = frame.findElement(By.className("js-calendar-graph-svg"));

        // Get screenshot of the visible part of the web page
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Convert the screenshot into BufferedImage
        BufferedImage fullScreen = ImageIO.read(screenshot);

        //Find location of the webelement logo on the page
        Point location = table.getLocation();

        //Find width and height of the located element logo
        int width = table.getSize().getWidth();
        int height = table.getSize().getHeight();

        //cropping the full image to get only the logo screenshot
        System.out.println("location.getX(): " + location.getX());
        System.out.println("location.getY(): " + location.getY());
        System.out.println("width: " + width);
        System.out.println("height: " + height);
        System.out.println("fullScreen.getWidth(): " + fullScreen.getWidth());
        System.out.println("fullScreen.getHeight(): " + fullScreen.getHeight());

        BufferedImage logoImage = fullScreen.getSubimage(700, 1180,
                1365, 245);
        ImageIO.write(logoImage, "png", screenshot);

        //Save cropped Image at destination location physically.
        FileUtils.copyFile(screenshot, new File("/tmp/particularElementScreenshot.PNG"));

        driver.quit();

    }
}
