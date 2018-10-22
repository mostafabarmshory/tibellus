package com.webpich.tibellus;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {

	public static void main(String[] args) throws IOException {
		String file = "page.html";
		String path = "http://webpich.com";

		WebDriver driver = loadWebDriver();
		try {
			driver.get(path);
			loadDriverLimits(driver);
		} catch (Exception e) {
		}

		// Take a screenshot of the current page
		FileUtils.write(new File(file), driver.getPageSource(), Charset.forName("UTF-8"));
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshot, new File("screenshot.png"));
		driver.quit();
	}

	/**
	 * Load a webdriver
	 * 
	 * @return
	 */
	private static WebDriver loadWebDriver() {
		String chromeDriverPath = "/home/maso/Downloads/Tools/chrom/chromedriver";
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		// load option
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--disable-gpu");
		options.addArguments("--window-size=1920,1200");
		options.addArguments("--ignore-certificate-errors");
		// Do not download images
		options.addArguments("--blink-settings=imagesEnabled=false");
		
		// TODO: maso, 2018: enable appcatch
		// TODO: maso, 2018: enable catch
		// TODO: maso, 2018: do not download css
		
		// crate driver
		ChromeDriver driver = new ChromeDriver(options);
		return driver;
	}

	public static boolean loadDriverLimits(WebDriver driver) {

		WebDriverWait wait = new WebDriverWait(driver, 60, 2000);

		// wait for jQuery to load
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				try {
					JavascriptExecutor js = (JavascriptExecutor) driver;
					return (Boolean) js.executeScript("return jQuery.active == 0");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
		};

		// wait for jQuery to load
		ExpectedCondition<Boolean> appLoad = new ExpectedCondition<Boolean>() {
			int i = 0;

			public Boolean apply(WebDriver driver) {
				try {
					JavascriptExecutor js = (JavascriptExecutor) driver;
					File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(screenshot, new File("screenshot-" + i++ + ".png"));
					return (Boolean) js
							.executeScript("return angular.element(document.body).scope().app.status == 'ready'");
				} catch (Exception e) {
					return false;
				}
			}
		};

		// wait for Javascript to load
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};

		return wait.until(jQueryLoad) && wait.until(jsLoad) && wait.until(appLoad);
	}
}