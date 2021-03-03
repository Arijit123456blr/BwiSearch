package com.bwi.qa.testScripts;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import com.bwi.qa.pages.FindHotelPage;
import com.bwi.qa.pages.HomePage;
import com.bwi.qa.util.ExcelUtil;
import com.google.gson.Gson;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.filter.ValueNodes.JsonNode;

public class FindHotelTest {
	FindHotelPage findHotelPage;
	HomePage homePage = new HomePage();

	public FindHotelTest() {
		super();
	}
	
	/**
	 * Use: To search hotels on the application
	 * @author Arijit Basu
	 * @param  Destination
	 * @param  CheckInMonth
	 * @param  CheckInDate
	 * @param  CheckOutMonth
	 * @param  CheckOutDate
	 * @return none
	 */
	//As of now I have used System.out.println() for my verification,but instead we have to use logs
	@Test(enabled = true, description = "Find My Hotel. . .", priority = 1, dataProviderClass = ExcelUtil.class, dataProvider = "dp")
	public void findHotel(String Destination, String CheckInMonth, String CheckInDate, String CheckOutMonth,
			String CheckOutDate) throws Exception {
		findHotelPage = new FindHotelPage();
		findHotelPage.validate_HotelSearchFunctionality_Test(Destination, CheckInMonth, CheckInDate, CheckOutMonth, CheckOutDate);
	}
	
}
