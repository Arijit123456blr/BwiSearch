package com.bwi.qa.pages;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.poi.util.SystemOutLogger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v87.network.Network;
import org.openqa.selenium.devtools.v87.network.Network.GetResponseBodyResponse;
import org.openqa.selenium.devtools.v87.network.model.RequestId;
import org.openqa.selenium.devtools.v87.network.model.Response;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.bwi.qa.base.TestBase;
import com.bwi.qa.util.ConfigReader;
import com.gargoylesoftware.htmlunit.javascript.host.file.File;
import com.google.gson.Gson;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class FindHotelPage extends TestBase {
	HomePage homePage = new HomePage();
	public String checkInDate = null;
	public String checkOutDate = null;
	public boolean updateStatus = false;
	private static DevTools chromeDevTools;

	@FindBy(id = "destination-input")
	WebElement destinationTxtBox;

	@FindBy(id = "checkin")
	WebElement checkInDateSelect;

	@FindBy(id = "checkout")
	WebElement checkOutDateSelect;

	@FindBy(xpath = "//*[@id='btn-modify-stay-update']")
	WebElement findMyHotelButton;

	@FindBy(xpath = "//*[@id='btn-modify-stay']")
	WebElement changeSearchButton;

	@FindBy(id = "summary-destination")
	WebElement summaryDestination;

	@FindBy(id = "summary-checkin")
	WebElement summaryCheckIn;

	@FindBy(id = "summary-checkout")
	WebElement summaryCheckOut;
	
	public By cookieBanner= By.xpath("//button[@id='onetrust-accept-btn-handler']");
	public By destination= By.xpath("//input[@id='destination-input']");
	public By findhotel= By.xpath("//button[@id='btn-modify-stay-update']");
	public By changesearch=By.xpath("//*[@id='btn-modify-stay']");
	public By getHotelNames= By.xpath("//div[contains(@id,'hotel-name')]");
	
	/**
	 * Use: This is a constructor
	 * @author Arijit Basu
	 * @param  none
	 * @return none
	 */
	public FindHotelPage() {
		PageFactory.initElements(driver, this);
	}

	/**
	 * Use: To get previous destinaiton name
	 * @author Arijit Basu
	 * @param  none
	 * @return String
	 */
	public String getPreviousDestination() {
		syncWait(2000);
		return destinationTxtBox.getText();
	}

	/**
	 * Use: To select the destination in the application
	 * @author Arijit Basu
	 * @param  String : need to provide destination value
	 * @return none
	 */
	public void enterDestination(String destination) throws Exception {
		System.out.println("Destination:::" + destination);
		destinationTxtBox.clear();
		destinationTxtBox.sendKeys(destination);
		syncWait(2000);
		driver.findElement(By.xpath("//ul[@id='google-suggestions']//li[@data-place='" + destination + "']")).click();
	}

	/**
	 * Use: To select the checkindate in the application
	 * @author Arijit Basu
	 * @param1  String: need to provide month
	 * @param2  String: need to provide date
	 * @return none
	 */
	public void selectChechInDate(String month, String date) throws InterruptedException {
		checkInDate = month + " " + date;
		driver.findElement(By.xpath("//*[@id='checkin']")).click();
		System.out.println("CheckIn:::" + checkInDate);
		syncWait(2000);
		driver.findElement(By.xpath("//a[contains(@aria-label, '" + checkInDate + "')]")).click();
	}

	/**
	 * Use: To select the checkoutdate in the application
	 * @author Arijit Basu
	 * @param1  String: need to provide month
	 * @param2  String: need to provide date
	 * @return none
	 */
	public void selectCheckOutDate(String month, String date) throws InterruptedException {
		checkOutDate = month + " " + date;
		driver.findElement(By.xpath("//*[@id='checkout']")).click();
		System.out.println("CheckOut:::" + checkOutDate);
		boolean availibilityOfCheckOutDate = verifyElementExistByLocator(
				By.xpath("//a[contains(@aria-label, '" + checkOutDate + "')]"));
		System.out.println("CheckOutDate element availibility:::" + availibilityOfCheckOutDate);
		if (availibilityOfCheckOutDate) {
			driver.findElement(By.xpath("//a[contains(@aria-label, '" + checkOutDate + "')]")).click();
		} else {
			System.out.println("checkOutDate Element is not available");
		}
	}

	/**
	 * Use: To click on the FindMyHotel Button in the application
	 * @author Arijit Basu
	 * @param  none
	 * @return none
	 */
	public void clickOnFindMyHotelButton() {
		if (homePage.verifyFindMyHotelButton()) {
			String hotelText = findMyHotelButton.getText();
			System.out.println("Text on FIND MY HOTEL button is:::" + hotelText);
			findMyHotelButton.click();
			syncWait(20000);
		} else {
			System.out.println("Find My Hotel button is not avaiable");
		}
	}

	/**
	 * Use: To verify the entered Destination
	 * @author Arijit Basu
	 * @param  none
	 * @return String
	 */
	public String verifyDestination() {
		syncWait(10000);
		String destinationVerify = null;
		boolean status = verifyElementExist(summaryDestination);
		System.out.println("Visibility Status of summaryDestination is::::" + status);

		if (status) {
			destinationVerify = summaryDestination.getText();
			System.out.println("Verified Destination is:::" + destinationVerify);
		}
		return destinationVerify;
	}

	/**
	 * Use: To verify the entered checkindate
	 * @author Arijit Basu
	 * @param  none
	 * @return String
	 */
	public String verifyCheckInDate() {
		String checkinDateVerify = null;
		boolean status = verifyElementExist(summaryCheckIn);
		System.out.println("Visibility Status of summaryCheckIn is::::" + status);
		if (status) {
			checkinDateVerify = summaryCheckIn.getText();
			System.out.println("Verified summaryCheckIn is:::" + checkinDateVerify);
		}
		return checkinDateVerify;
	}

	/**
	 * Use: To verify the entered checkoutdate
	 * @author Arijit Basu
	 * @param  none
	 * @return String
	 */
	public String verifyCheckOutDate() {
		String checkOutDateVerify = null;
		boolean status = verifyElementExist(summaryCheckOut);
		System.out.println("Visibility Status of summaryCheckOut is::::" + status);
		if (status) {
			checkOutDateVerify = summaryCheckOut.getText();
			System.out.println("Verified summaryCheckOut is:::" + checkOutDateVerify);
		}
		return checkOutDateVerify;
	}


	/**
	 * Use: To click on changesearch button
	 * @author Arijit Basu
	 * @param  none
	 * @return none
	 */
	public void clickOnChangeSearch() {
		changeSearchButton.click();
	}

	public void validate_HotelSearchFunctionality_Test(String Destination, String CheckInMonth, String CheckInDate, String CheckOutMonth, String CheckOutDate) throws InterruptedException {
		String browserName = prop.getProperty("browser");
		if (browserName.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir") + "\\src\\test\\resources\\drivers\\chromedriver.exe");
			driver = new ChromeDriver();
		}
		driver.manage().window().maximize();
		chromeDevTools = ((ChromeDriver)driver).getDevTools();
		chromeDevTools.createSession();
		//driver.get("https://www.bestwestern.com");
		ConfigReader reader=new ConfigReader();
		driver.get(reader.getURL());
		syncWait(2000);
		String ActualURL=driver.getCurrentUrl();
		String ExpectedURL="https://www.bestwestern.com/en_US.html";
		Assert.assertEquals(ActualURL, ExpectedURL);
		System.out.println("BestWestern home page has been loaded succsfully");
		driver.findElement(cookieBanner).click();
		syncWait(20000);
		driver.findElement(destination).sendKeys(Destination);
		syncWait(10000);
		driver.findElement(By.xpath("//ul[@id='google-suggestions']//li[@data-place='"+Destination+"']")).click();
		syncWait(5000);
		selectChechInDate(CheckInMonth, CheckInDate);
		syncWait(5000);
		selectCheckOutDate(CheckOutMonth, CheckOutDate);
		syncWait(5000);		
		driver.findElement(findhotel).click();
		syncWait(20000);
		driver.findElement(changesearch).click();
		driver.findElement(destination).clear();
		driver.findElement(destination).sendKeys("New York, NY, USA");
		syncWait(10000);
		driver.findElement(By.xpath("//ul[@id='google-suggestions']//li[@data-place='New York, NY, USA']")).click();
		syncWait(5000);
		selectChechInDate(CheckInMonth, CheckInDate);
		syncWait(5000);
		selectCheckOutDate(CheckOutMonth, CheckOutDate);
		syncWait(5000);
		chromeDevTools.send(Network.enable(of(100000000), empty(), empty()));
		final RequestId[] requestIds = new RequestId[1];
		Set<Response> resSet = new HashSet<>();
		chromeDevTools.addListener(Network.responseReceived(),
		entry -> {	
			if(entry.getResponse().getUrl().contains("proxy?"))
			{
				requestIds[0] = entry.getRequestId();
				Response res = entry.getResponse();
				resSet.add(res);
			}
		});
		syncWait(5000);
		driver.findElement(findhotel).click();
		syncWait(20000);
		
		//Get Hotel names from DOM-- UI
		List<WebElement> hotelCardsList= driver.findElements(getHotelNames);
		int HotelSearchCountGUI =hotelCardsList.size();
		System.out.println("===============================:: GUI-HOTEL DETAILS=========================================");
		System.out.println("No of hotel cards available are:::" + HotelSearchCountGUI);
		for(int i=0;i<HotelSearchCountGUI;i++)
		{
			System.out.println("GUI::Hotel Name:: >>>>>>>>>>>>>::"+hotelCardsList.get(i).getText().trim());
		}
		System.out.println();
		System.out.println();
		
		
		//Get Hotel names from Network Tab
		GetResponseBodyResponse responseBody = chromeDevTools.send(Network.getResponseBody(requestIds[0]));
		syncWait(20000);
		System.out.println("RESPONSE::>>>>>>>>>>>>>>" + responseBody.getBody());
		System.out.println("===============================:: Network-HOTEL DETAILS====================================");
		Configuration conf = Configuration.defaultConfiguration();
		List<String> names = JsonPath.using(conf).parse(responseBody.getBody().toString()).read("$[*]['resortSummary']['name']");
		System.out.println("List of Hotel Names : " + names);
		System.out.println("Number of Hotels : " + names.size());
		int HotelSearchCountNetworkTab=names.size();
		Assert.assertEquals(HotelSearchCountGUI, HotelSearchCountNetworkTab);
		System.out.println("The Hotel Total count from DOM is equal to Total count from Network Tab");
		for(String str:names) {
			System.out.println("Network Tab:: Hotel Name:: >>>>>>>>>>>>>::" + str.replace("Best Western ","").trim());
		}
		driver.quit();
		
	}
}
