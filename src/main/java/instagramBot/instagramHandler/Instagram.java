package instagramBot.instagramHandler;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import instagramBot.driverCreator.CreateDriver;

public class Instagram {
	
	private WebDriver driver;
	private WebDriverWait wait;
	private String username, password;
	
	public Instagram(CreateDriver newDriver, String username, String password) {
		this.driver = newDriver.getDriver();
		this.wait = newDriver.getWait();
		this.username = username;
		this.password = password;
	}
	
	public void handler() {
		loginPage();
		String path = "";
		while (path != "none") {
			path = getImage();
			if (path != "none") {
				uploadPost(path);
				try {
					TimeUnit.SECONDS.sleep(60 * 30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		driver.quit();
	}
	
	// Logging
	private void loginPage() {
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.get("https://www.instagram.com/accounts/login/?next=%2F&source=mobile_nav");
		wait.until(presenceOfElementLocated(By.name("username"))).sendKeys(username);
		driver.findElement(By.name("password")).sendKeys(password + Keys.ENTER);
		try {
			TimeUnit.SECONDS.sleep(5);
			driver.get("https://www.instagram.com/?hl=en");
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// Uploading post
	private void uploadPost(String path) {		
		wait.until(presenceOfElementLocated(By.xpath("//*[name()=\"svg\"][@aria-label=\"New Post\"]"))).click();
		// picture to upload
		wait.until(presenceOfElementLocated(By.xpath("//input[@accept = \"image/jpeg\"]"))).sendKeys(path);
		wait.until(presenceOfElementLocated(By.className("createSpriteExpand"))).click();
		wait.until(presenceOfElementLocated(By.xpath("//button[normalize-space()=\"Next\"]"))).click();
		// post captions
		wait.until(presenceOfElementLocated(By.xpath("//textarea[1]"))).sendKeys("#meme #memes #funny #funnymemes #funnymeme #stolenmemes #stolenmemesarethebest #goodmemes #underratedmemes #dailymemes #dailydose #memy #humor #comedy #memestagram");
		wait.until(presenceOfElementLocated(By.xpath("//button[normalize-space()=\"Share\"]"))).click();
		try {
			TimeUnit.SECONDS.sleep(5);
			driver.get("https://www.instagram.com/?hl=en");
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//Selecting image to post
	private String getImage() {
		File[] downloaded = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\downloaded\\").listFiles();
		RepostChecker repostChecker = new RepostChecker();
		
		for (File file : downloaded) {
			if (!repostChecker.isUploaded(file)) {
				File image = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\used\\" + file.getName());
				if (image.exists())
					image.delete();
				file.renameTo(image);
				file.delete();
				return System.getProperty("user.dir") + "\\src\\main\\resources\\used\\" + file.getName();
			}
			file.delete();
		}
		
		return "none";
	}
	
}
