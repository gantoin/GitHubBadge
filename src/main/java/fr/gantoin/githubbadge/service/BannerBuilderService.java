package fr.gantoin.githubbadge.service;

import static fr.gantoin.githubbadge.utils.CommandUtils.executeCommand;
import static fr.gantoin.githubbadge.utils.CommandUtils.executeOneLineCommand;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BannerBuilderService {

    public static final String HTTP_GITHUB_COM_GANTOIN = "https://github.com/gantoin";

    public synchronized void buildBanner(String firstName) {
        String pngPath = this.customGraphGrabber();
        pngPath = this.resizeImage(pngPath);
        String shadow = this.createShadowFile(pngPath);
        String templatePath = "src/main/resources/templates/template.png";
        String resultPath = this.mergeImages(shadow, templatePath);
        resultPath = this.mergeImages(pngPath, resultPath);
        if (resultPath != null) {
            this.addFirstName(firstName, resultPath);
        }
    }

    private synchronized String mergeImages(String pathToSvg, String templatePath) {
        String resultPath = "/tmp/result.png";
        // Integrate svg in template
        String command = "convert " + templatePath + " " + pathToSvg + " -gravity center -geometry +0+90 -composite -matte " + resultPath;
        executeOneLineCommand(command);
        return resultPath;
    }

    private synchronized String customGraphGrabber() {
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
        try {
            String pathname = "/tmp/particularElementScreenshot.PNG";
            FileUtils.copyFile(screenshotAs, new File(pathname));
            return pathname;
        } catch (IOException e) {
            throw new RuntimeException("Error while taking screenshot", e);
        } finally {
            driver.quit();
        }
    }

    public synchronized String addFirstName(String firstName, String resultPath) {
        // List of commands that we want to execute
        List<String> commands = new ArrayList<>();

        // Executable file
        commands.add("convert");

        // Executable file parameters
        commands.add(resultPath);
        commands.add("-font");
        commands.add("src/main/resources/fonts/GlacialIndifference-Bold.otf");
        commands.add("-pointsize");
        commands.add("75");
        commands.add("-fill");
        commands.add("white");
        commands.add("-draw");
        commands.add("text 235,115 '" + firstName + "!'");
        executeCommand(resultPath, commands);
        return resultPath;
    }

    private synchronized String createShadowFile(String pathname) {
        String shadowPath = "/tmp/shadow.png";
        List<String> commands = new ArrayList<>();
        commands.add("convert");
        commands.add(pathname);
        commands.add("+clone");
        commands.add("-background");
        commands.add("black");
        commands.add("-shadow");
        commands.add("60x3+0+0");
        commands.add("+swap");
        commands.add("-background");
        commands.add("none");
        commands.add("-layers");
        commands.add("merge");
        commands.add("+repage");
        executeCommand(shadowPath, commands);
        return shadowPath;
    }

    private synchronized String resizeImage(String imagePath) {
        String command = "convert " + imagePath + " -resize 130% " + imagePath;
        executeOneLineCommand(command);
        return imagePath;
    }
}
