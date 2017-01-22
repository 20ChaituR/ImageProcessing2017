import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Mat;

/**
 *
 */

/**
 * @author joelmanning
 *
 */
public class ImageViewer extends JPanel {
	private BufferedImage image;

	/**
	 * @param image
	 * @throws HeadlessException
	 */
	public ImageViewer(BufferedImage image) throws HeadlessException {
		this();
		this.image = image;
	}

	/**
	 * @throws HeadlessException
	 */
	public ImageViewer() throws HeadlessException {
		super();
		JFrame frame = new JFrame("Professor Manning's Wonderful Copy+Paste Program");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(Main.COLS, Main.ROWS);
		frame.add(this);
	}



	/* (non-Javadoc)
	 * @see java.awt.Container#paintComponents(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

	/**
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Mat mat) {
		image = matToBufferedImage(mat, image);
	}

	/**
	 * Converts/writes a Mat into a BufferedImage.
	 *
	 * @param matrix Mat of type CV_8UC3 or CV_8UC1
	 * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY
	 */
	public static BufferedImage matToBufferedImage(Mat matrix, BufferedImage bimg)
	{
	    if ( matrix != null ) {
	        int cols = matrix.cols();
	        int rows = matrix.rows();
	        int elemSize = (int)matrix.elemSize();
	        byte[] data = new byte[cols * rows * elemSize];
	        int type;
	        matrix.get(0, 0, data);
	        switch (matrix.channels()) {
	        case 1:
	            type = BufferedImage.TYPE_BYTE_GRAY;
	            break;
	        case 3:
	            type = BufferedImage.TYPE_3BYTE_BGR;
	            // bgr to rgb
	            byte b;
	            for(int i=0; i<data.length; i=i+3) {
	                b = data[i];
	                data[i] = data[i+2];
	                data[i+2] = b;
	            }
	            break;
	        default:
	        	System.out.println("Wrong number of channels");
	            return null;
	        }

	        // Reuse existing BufferedImage if possible
	        if (bimg == null || bimg.getWidth() != cols || bimg.getHeight() != rows || bimg.getType() != type) {
	            bimg = new BufferedImage(cols, rows, type);
	        }
	        bimg.getRaster().setDataElements(0, 0, cols, rows, data);
	    } else { // mat was null
	        bimg = null;
	    }
	    return bimg;
	}

	public BufferedImage toBufferedImage(Mat m){
	      int type = BufferedImage.TYPE_BYTE_GRAY;
	      if ( m.channels() > 1 ) {
	          type = BufferedImage.TYPE_3BYTE_BGR;
	      }
	      int bufferSize = m.channels()*m.cols()*m.rows();
	      byte [] b = new byte[bufferSize];
	      m.get(0,0,b); // get all the pixels
	      BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
	      final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	      System.arraycopy(b, 0, targetPixels, 0, b.length);
	      return image;

	  }
}
