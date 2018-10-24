package com.webpich.tibellus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webpich.tibellus.seen.Collection;
import com.webpich.tibellus.seen.CollectionQuery;
import com.webpich.tibellus.seen.seo.Content;
import com.webpich.tibellus.seen.seo.ISeoService;
import com.webpich.tibellus.seen.user.Account;
import com.webpich.tibellus.seen.user.IUserService;

import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class Main {
	static IUserService userService;
	static ISeoService seoService;
	static Map<Long, Content> db;
	private static ObjectMapper objectMapper;
	private static OkHttpClient httpClient;

	public static void main(String[] args) throws IOException {
		String path = "http://www.webpich.com";
		String login = "admin";
		String password = "admin";

		db = new HashMap<Long, Content>();

		// serialization
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		// HTTP client
		CookieStore cookieStore = null;
		CookieHandler cookieHandler = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
		httpClient = new OkHttpClient.Builder()//
				.cookieJar(new JavaNetCookieJar(cookieHandler))//
				.build();

		Retrofit retrofit = new Retrofit.Builder()//
				.baseUrl(path + "/api/v2/")//
				.client(httpClient)//
				.addConverterFactory(JacksonConverterFactory.create(objectMapper))//
				.build();
		userService = retrofit.create(IUserService.class);
		seoService = retrofit.create(ISeoService.class);

		updateSeenPagesList(login, password);
//		updatePrerenderdPages();
		syncSeenPages(login, password);
	}

	private static void syncSeenPages(String login, String password) throws IOException {
		// login
		Response<Account> response = userService//
				.login(login, password)//
				.execute();
		if (!response.isSuccessful()) {
			System.err.println("Authentication fail (user or password is incorrect)");
		}

		response = userService//
				.getCurrentAccount()//
				.execute();
		if (!response.isSuccessful()) {
			System.err.println("You are not logined");
		}

		java.util.Collection<Content> pages = getPrerenderdPages();
		for (Content content : pages) {
			try {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = objectMapper.convertValue(content, Map.class);
				Response<Content> res = seoService//
						.updateContent(content.getId(), map)//
						.execute();
				if (!res.isSuccessful()) {
					System.err.println("Fail to update :" + content.getId());
				}

				File file = new File("storage/pages", content.getId().toString());

				ByteArrayOutputStream arr = new ByteArrayOutputStream();
//				OutputStream zipper = new GZIPOutputStream(arr);

				FileInputStream pageStream = new FileInputStream(file);
				IOUtils.copy(pageStream, arr);
//				IOUtils.copy(pageStream, zipper);

				RequestBody requestBody = RequestBody//
						.create(null, arr.toByteArray());

				Response<Content> uploadRes = seoService//
						.uploadContent(content.getId(), "gzip", requestBody)//
						.execute();
				if (!uploadRes.isSuccessful()) {
					System.err.println("Fail to upload :" + content.getId());
					System.out.println(uploadRes);
				} else {
					System.out.println("Seo content is uploaded : " + content.getId());
				}

				// sleep
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}
	}

	private static void updatePrerenderdPages() throws IOException {
		WebDriver driver = loadWebDriver();
		java.util.Collection<Content> pages = getPrerenderdPages();
		for (Content content : pages) {
			String path = content.getUrl();
			try {
				driver.get(path);
				loadDriverLimits(driver);
			} catch (Exception e) {
			}

			// update content
			content.setTitle(driver.getTitle());

			// Take a screenshot of the current page
			FileUtils.write(new File("storage/pages", content.getId().toString()), driver.getPageSource(),
					Charset.forName("UTF-8"));
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshot, new File("storage/screenshots", content.getId() + ".png"));
		}
		driver.quit();
	}

	private static java.util.Collection<Content> getPrerenderdPages() {
		return db.values();
	}

	private static void updateSeenPagesList(String login, String password) throws IOException {
		// login
		Response<Account> response = userService//
				.login(login, password)//
				.execute();
		if (!response.isSuccessful()) {
			System.err.println("Authentication fail (user or password is incorrect)");
		}

		response = userService//
				.getCurrentAccount()//
				.execute();
		if (!response.isSuccessful()) {
			System.err.println("You are not logined");
		}

		// get list of SEO contents

		CollectionQuery query = new CollectionQuery();
		Response<Collection<Content>> contentListResponse = seoService//
				.getContents(query)//
				.execute();
		if (!response.isSuccessful()) {
			System.err.println("Fail to get list of contents");
		}

		Collection<Content> list = contentListResponse.body();
		for (Content content : list.getItems()) {
			updatePageMeta(content);
		}
	}

	private static void updatePageMeta(Content content) {
		db.put(content.getId(), content);
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
//		options.addArguments("--headless");
//		options.addArguments("--disable-gpu");
		options.addArguments("--window-size=1920,1200");
		options.addArguments("--ignore-certificate-errors");
		// Do not download images
//		options.addArguments("--blink-settings=imagesEnabled=false");

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

			public Boolean apply(WebDriver driver) {
				try {
					JavascriptExecutor js = (JavascriptExecutor) driver;
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