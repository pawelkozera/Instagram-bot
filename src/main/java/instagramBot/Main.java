package instagramBot;

import instagramBot.driverCreator.CreateDriver;
import instagramBot.instagramHandler.Instagram;
import instagramBot.redditHandler.Reddit;

public class Main {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		
		while (true) {
			new Reddit(new CreateDriver("pc"), "login", "password").handler();
			new Instagram(new CreateDriver("mobile"), "login", "password").handler();
		}
	}

}
