package instagramBot.instagramHandler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RepostChecker {
	
	private BufferedImage imgDownloaded;
	private BufferedImage imgUploaded;

	public RepostChecker() {
	}
	
	public boolean isUploaded(File img_downloaded) {		
		int widthImgUploaded, heightImgUploaded;
		double percent;
		long diff = 0;
		long maxDiff = 0;
		
		File downloaded = img_downloaded;
		File[] uploaded = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\used\\").listFiles();
		
		try {
			imgDownloaded = ImageIO.read(downloaded);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int widthImgDownloaded = imgDownloaded.getWidth();
		int heightImgDownloaded = imgDownloaded.getHeight();
		
		if (uploaded.length == 0)
			return false;
		
		for (File file : uploaded) {
			try {
				imgUploaded = ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			widthImgUploaded = imgUploaded.getWidth();
			heightImgUploaded = imgUploaded.getHeight();
			
			if (widthImgDownloaded != widthImgUploaded || heightImgDownloaded != heightImgUploaded)
				continue;
			
			for (int y = 0; y < heightImgDownloaded; y++) {
				for (int x = 0; x < widthImgDownloaded; x++) {
					diff += pixelDiff(imgDownloaded.getRGB(x, y), imgUploaded.getRGB(x, y));
				}
			}
			
			maxDiff = 3L * 255 * widthImgDownloaded * heightImgDownloaded;
			percent = 100.0 * diff / maxDiff;
			
			if (percent > 10) 
				return false;
			return true;
		}
		return false;
	}
	
	private static int pixelDiff(int rgb1, int rgb2) {
		int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >>  8) & 0xff;
        int b1 =  rgb1        & 0xff;
        int r2 = (rgb2 >> 16) & 0xff;
        int g2 = (rgb2 >>  8) & 0xff;
        int b2 =  rgb2        & 0xff;
        
        return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
	}
	
}
