import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class Picture implements Serializable{
	private transient Image image;
	private String name;

	public Picture(String name, Image image){
		this.name = name;
		this.image = image;
	}
	
	public Image getImage(){
		return image;
	}
	
	public String getName(){
		return name;
	}
	
	public Picture(){
		name = "Picture: " + (int)(Math.random() * 10) + 1;
		image = new BufferedImage((int)(Math.random() * 9 + 100) * 10, (int)(Math.random() * 9 + 100) * 10, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < image.getWidth(null); i++){
			for (int j = 0; j < image.getHeight(null); j++){
				((BufferedImage)image).setRGB(i, j, new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256)).getRGB());
			}
		}
	}
	
	public void draw(int x, int y, int width, int height, Graphics g, int zoomScale, int startx, int starty){
		double zoom = (int)(Math.pow(2, zoomScale));
		g.drawImage(image, x, y, x + width, y + height, startx, starty, startx + (int)(width/zoom), starty + (int)(height/zoom), null);
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		int[] pixels = image != null ? new int[w * h] : null;

		if (image != null)

		{
			try {
				PixelGrabber pg = new PixelGrabber(image, 0, 0, w, h, pixels, 0, w);
				pg.grabPixels();
				if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
					throw new IOException("failed to load image contents");
				}
			} catch (InterruptedException e) {
				throw new IOException("image load interrupted");
			}
		}
		s.writeInt(w);
		s.writeInt(h);
		s.writeObject(pixels);

	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
		s.defaultReadObject();

		int w = s.readInt();
		int h = s.readInt();
		int[] pixels = (int[]) (s.readObject());

		if (pixels != null) {
			Toolkit tk = Toolkit.getDefaultToolkit();
			ColorModel cm = ColorModel.getRGBdefault();
			image = tk.createImage(new MemoryImageSource(w, h, cm, pixels, 0, w));
		}
	}
}
