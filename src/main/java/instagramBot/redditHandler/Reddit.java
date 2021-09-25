package instagramBot.redditHandler;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import instagramBot.driverCreator.CreateDriver;

public class Reddit {

	private WebDriver driver;
	private WebDriverWait wait;
	private String username, password;
	
	public Reddit(CreateDriver newDriver, String username, String password) {
		this.driver = newDriver.getDriver();
		this.wait = newDriver.getWait();
		this.username = username;
		this.password = password;
	}
	
	public void handler() {
		loginPage();
		downloadMeme();
		driver.quit();
	}
	
	private void loginPage() {
		driver.get("https://www.reddit.com/login/");
		wait.until(presenceOfElementLocated(By.id("loginUsername"))).sendKeys(username);
		driver.findElement(By.id("loginPassword")).sendKeys(password + Keys.ENTER);
		try {
			TimeUnit.SECONDS.sleep(5);
			driver.get("https://www.reddit.com/r/memes/");
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// downloading post image
	private void downloadMeme() {
		String url;
		int index;
		List<WebElement> allImages = driver.findElements(By.xpath("//img[@alt = \"Post image\" and starts-with(@src, \"https://preview.redd.it\")]"));
		List<String> allImagesUrl = new ArrayList<String>();
		
		// getting images urls and changing them to https://i.redd.it/* to get full resolution
		for (WebElement image : allImages) {
			url = image.getAttribute("src");
			index = url.indexOf("?width");
			
			if (index == -1)
				index = url.indexOf("?blur");
			
			if (index != -1)
				allImagesUrl.add("https://i.redd.it/" + url.substring(24, index));
		}
		
		//saving images
		int i = 1;
		String path;
		for (String imageUrl : allImagesUrl) {
			path = System.getProperty("user.dir") + "\\src\\main\\resources\\downloaded\\meme" + i + ".jpg";
			savingImages(imageUrl, path);
			resizeImage(path);
			i++;
		}
	}
	
	private void savingImages(String imageUrl, String path) {
		try {
			ReadableByteChannel rbc = Channels.newChannel(new URL(imageUrl).openStream());
			FileOutputStream fos = new FileOutputStream(path);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	//changing to resolution that will fit instagram 1080x1080
	private void resizeImage(String path) {
		try {
			BufferedImage orginalImg;
			orginalImg = ImageIO.read(new File(path));
			if (orginalImg.getHeight() > 1080 || orginalImg.getWidth() > 1080) {
				BufferedImage bufferedImg;
				int newWidth, newHeight;
				
				newWidth = orginalImg.getScaledInstance(-1, 1080, Image.SCALE_SMOOTH).getWidth(null);
				newHeight = 1080;
				if (newWidth > 1080) {
					newHeight = orginalImg.getScaledInstance(1080, -1, Image.SCALE_SMOOTH).getHeight(null);
					newWidth = 1080;
				}
				
				bufferedImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = bufferedImg.createGraphics();
				g.drawImage(orginalImg.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
				ImageIO.write(bufferedImg, "jpg", new File(path));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
