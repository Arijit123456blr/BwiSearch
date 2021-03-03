package com.bwi.qa.base;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.Event;
import org.openqa.selenium.devtools.v87.network.Network;
import org.openqa.selenium.devtools.v87.network.Network.GetResponseBodyResponse;
import org.openqa.selenium.devtools.v87.network.model.RequestId;
import org.openqa.selenium.devtools.v87.network.model.Response;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestBase {
	public static WebDriver driver;
	public static Properties prop;
	private static DevTools chromeDevTools;

	/**
	 * Use: This is a constructor
	 * 
	 * @param none
	 * @return none
	 */
	public TestBase() {
		try {
			prop = new Properties();
			FileInputStream config = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\main\\java\\com\\bwi\\qa\\config\\config.properties");
			prop.load(config);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Use: Browser/driver initilization
	 * 
	 * @param none
	 * @return none
	 * @throws InterruptedException
	 */
	public static void initialization() {
		String browserName = prop.getProperty("browser");
		if (browserName.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					System.getProperty("user.dir") + "\\src\\test\\resources\\drivers\\chromedriver.exe");
			driver = new ChromeDriver();
		}
		driver.manage().window().maximize();
		chromeDevTools = ((ChromeDriver) driver).getDevTools();
		chromeDevTools.createSession();
		driver.get("https://www.bestwestern.com");
	}

	public static void hotelResponse() {
		// syncWait(20000);
		chromeDevTools.send(Network.enable(of(100000000), empty(), empty()));
		final RequestId[] requestIds = new RequestId[1];
		Set<Response> resSet = new HashSet<>();
		chromeDevTools.addListener(Network.responseReceived(), entry -> {
			if (entry.getResponse().getUrl().contains("proxy?")) {
				requestIds[0] = entry.getRequestId();
				Response res = entry.getResponse();
				resSet.add(res);
			}
		});
		System.out.println("Request Id >>>>>>>>>>>>>> " + requestIds[0]);
		if (requestIds[0] != null) {
			GetResponseBodyResponse responseBody = chromeDevTools.send(Network.getResponseBody(requestIds[0]));
			syncWait(20000);
			System.out.println("RESPONSE::>>>>>>>>>>>>>>" + responseBody.getBody());

			String jsonString = responseBody.getBody();
			JSONObject JSONResponse = new JSONObject(jsonString);
			JSONObject verticeObj = JSONResponse.getJSONObject("resortSummary");
			for (String names : JSONObject.getNames(verticeObj)) {
				String HotelName = verticeObj.getString(names);
				System.out.println("Hotel Name::>>>>>>>>>>>>>>" + HotelName);
			}
		}
	}

	private static Event function() {
		// TODO Auto-generated method stub
		return null;
	}

	public void output_on_start(String... varargs) {
		System.out.println("STARTED " + varargs);
	}

	public void output_on_end(String... varargs) {
		System.out.println("FINISHED " + varargs);
	}

	/**
	 * Use: To verify the availibility of an webelement using locator webelement
	 * 
	 * @author Arijit Basu
	 * @param WebElement
	 * @return boolean
	 */
	public boolean verifyElementExist(WebElement element) {
		boolean blnStatus = false;
		WebDriverWait localWebDriverWait = new WebDriverWait(driver, 60);
		try {
			localWebDriverWait.until(ExpectedConditions.visibilityOf(element));
			System.out.println("Element is available:" + element + "Pass");

			blnStatus = true;

		} catch (RuntimeException localRuntimeException) {
			System.out.println("Error in finding Element:" + localRuntimeException.getMessage() + "Fail");
		}
		return blnStatus;
	}

	/**
	 * Use: To handle sync events
	 * 
	 * @author Arijit Basu
	 * @param none
	 * @return none
	 */
	public static void syncWait(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Use: To verify the availability of webelement using locator
	 * 
	 * @author Arijit Basu
	 * @param none
	 * @return none
	 */
	public boolean verifyElementExistByLocator(By loc) {
		boolean blnStatus = false;
		WebDriverWait localWebDriverWait = new WebDriverWait(driver, 60);
		try {
			localWebDriverWait.until(ExpectedConditions.presenceOfElementLocated(loc));
			System.out.println("Element is available:" + loc + "Pass");
			blnStatus = true;

		} catch (RuntimeException localRuntimeException) {
			System.out.println("Error in finding Element:" + localRuntimeException.getMessage() + "Fail");
		}
		return blnStatus;
	}

	/**
	 * Use: To convert the month
	 * 
	 * @author Arijit Basu
	 * @param String
	 * @return String
	 */
	public String convertMonth(String selectedmonth) {
		String month = selectedmonth;
		switch (month) {
		case "January":
			return "-Jan-2021";
		case "February":
			return "-Feb-2021";
		case "March":
			return "-Mar-2021";
		case "April":
			return "-Apr-2021";
		case "May":
			return "-May-2021";
		case "June":
			return "-jun-2021";
		case "July":
			return "-Jul-2021";
		case "August":
			return "-Aug-2021";
		case "September":
			return "-Sep-2021";
		case "October":
			return "-Oct-2021";
		case "November":
			return "-Nov-2021";
		case "December":
			return "-Dec-2021";

		default:
			return "invalid month";
		}
	}

	/**
	 * Use: To scroll down the page
	 * 
	 * @author Arijit Basu
	 * @param none
	 * @return none
	 */
	public void pressPageDownKey() {
		try {
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_PAGE_DOWN);
			r.keyRelease(KeyEvent.VK_PAGE_DOWN);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	

}
