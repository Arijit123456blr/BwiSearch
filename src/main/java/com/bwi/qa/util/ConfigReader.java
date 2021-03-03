package com.bwi.qa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

	Properties pro;

	public ConfigReader() {
		try {
			File src = new File("C:\\Users\\abasu\\eclipse-workspace\\BestWestern_BwiSearch-master\\src\\main\\java\\com\\bwi\\qa\\config\\config.properties");
			FileInputStream fis = new FileInputStream(src);
			pro = new Properties();
			pro.load(fis);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public String getURL() {
		String URL = pro.getProperty("url");
		return URL;

	}

}
