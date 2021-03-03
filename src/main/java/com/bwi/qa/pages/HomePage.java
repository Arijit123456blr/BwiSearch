package com.bwi.qa.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.bwi.qa.base.TestBase;

public class HomePage extends TestBase
{

	@FindBy(id = "btn-modify-stay-update")
	WebElement findMyHotelBtn;

	/**
	 * Use: This is a constructor
	 * @author Arijit Basu
	 * @param  none
	 * @return none
	 */
	public HomePage() {
		PageFactory.initElements(driver, this);
	}

	/**
	 * Use: To verify HomePage Title
	 * @author Arijit Basu
	 * @param  none
	 * @return String
	 */
	public String verifyHomePageTitle(){
		return driver.getTitle();
	}

	/**
	 * Use: To verify the availability of FindMyHotel Button
	 * @author Arijit Basu
	 * @param  none
	 * @return boolean
	 */
	public boolean verifyFindMyHotelButton(){
		return findMyHotelBtn.isDisplayed();
	}
}
