package com.bwi.qa.testScripts;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.bwi.qa.base.TestBase;
import com.bwi.qa.pages.HomePage;


public class HomePageTest extends TestBase 
{
	HomePage homePage;

	public HomePageTest() 
	{
		super();
	}

	@BeforeTest
	public void setUp()
	{
		initialization();
		homePage=new HomePage();
	}

	/**
	 * Use: To test is to check the title of the page
	 * @author Arijit Basu
	 * @param  none
	 * @return none
	 */
	@Test(priority=1)
	public void verifyHomePageTitle()
	{
		String homePageTitle = homePage.verifyHomePageTitle();
		System.out.println("Title is:::::"+ homePageTitle);
		Assert.assertEquals(homePageTitle, "Book Direct at Best Western Hotels & Resorts");
	}

	/**
	 * Use: To test is to verify FindMyHotel Button
	 * @author Arijit Basu
	 * @param  none
	 * @return none
	 */
	@Test(priority=2)
	public void checkFindMYHotelBtn(){
		Assert.assertTrue(homePage.verifyFindMyHotelButton());
	}
}
