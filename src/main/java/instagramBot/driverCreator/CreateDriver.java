package instagramBot.driverCreator;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	
	public CreateDriver(String browserType) {
		if (browserType == "mobile")
			mobile();
		else
			pc();
	}
	
	private void mobile() {
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", "Nexus 5");
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
		chromeOptions.addArguments("--headless");
		this.driver = new ChromeDriver(chromeOptions);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	
	private void pc() {
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--headless");
		this.driver = new ChromeDriver(chromeOptions);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	public WebDriver getDriver() {
		return driver;
	}

	public WebDriverWait getWait() {
		return wait;
	}
	
}
