/**
 * 
 */
package org.j4me.ext;

import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

import org.j4me.ui.Theme;
import org.j4me.ui.components.Component;

/**
 * This class draws the image taken by the phone camera 
 * 
 * @author Giuseppe Miscione
 *
 */
public class VideoComponent extends Component {
	
	protected Player videoPlayer;
	protected VideoControl control;
	
	protected int videoMargins[];

	/**
	 * Creates a new <code>VideoComponent</code>,
	 * initializing the phone camera and settings the default 0 margins for
	 * the rendered video.
	 * @throws MediaException If the device doesn't support picture
	 * capture.
	 * @throws IOException If there was a problem connecting with the camera.
	 */
	public VideoComponent() throws IOException, MediaException {
		this(null);
	}
	
	/**
	 * Creates a new <code>VideoComponent</code>,
	 * initializing the phone camera.
	 * @param videoMargins The preferred margins of the renderer video.
	 * @throws MediaException If the device doesn't support picture
	 * capture.
	 * @throws IOException If there was a problem connecting with the camera.
	 */
	public VideoComponent(int videoMargins[]) throws IOException, MediaException {
		setVideoMargins(videoMargins);
		try {
			videoPlayer = Manager.createPlayer("capture://image");
		} catch(Exception e) {
			videoPlayer = Manager.createPlayer("capture://video");
		}
		videoPlayer.realize();
	}

	/**
	 * Stops the video playback and release system resource.
	 */
	public void closeVideo() {
		if(videoPlayer != null && videoPlayer.getState() == Player.STARTED) {
			videoPlayer.deallocate();
			videoPlayer = null;
			control = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.j4me.ui.components.Component#getPreferredComponentSize(org.j4me.ui.Theme, int, int)
	 */
	protected int[] getPreferredComponentSize(Theme theme, int viewportWidth, int viewportHeight) {
		int margins[] = getVideoMargins();
		return new int[]{
			viewportWidth - margins[1] - margins[3],
			viewportHeight - margins[0] - margins[2]};
	}

	/**
	 * Returns a byte array with the data of the captured image.
	 * The device default parameters will be used for encoding and size.
	 * Remember to call this method from a new thread to avoid
	 * deadlocks.
	 * 
	 * @return A byte array with the data of the captured image.
	 * @throws MediaException If an error occurs while getting the snapshot.
	 */
	public byte[] getRawSnapshot() throws MediaException {
		return getRawSnapshot(null, 0, 0);
	}

	/**
	 * Returns a byte array with the data of the captured image.
	 * Remember to call this method from a new thread to avoid
	 * deadlocks.
	 * 
	 * @param encoding The encoding of the snapshot.
	 * @param width The width of the snapshot.
	 * @param height The height of the snapshot.
	 * @return A byte array with the data of the captured image.
	 * @throws MediaException If an error occurs while getting the snapshot.
	 */
	public byte[] getRawSnapshot(String encoding, int width, int height) throws MediaException {
		return control.getSnapshot(encoding == null ?
			null :
			"encoding=" + encoding + "&width=" + width + "&height=" + height);
	}

	/**
	 * Returns an {@link Image} with the captured image.
	 * The device default parameters will be used for encoding and size.
	 * Remember to call this method from a new thread to avoid
	 * deadlocks.
	 * 
	 * @return An {@link Image} with the captured image.
	 * @throws MediaException If an error occurs while getting the snapshot.
	 */
	public Image getSnapshot() throws MediaException {
		return getSnapshot(null, 0, 0);
	}
	
	/**
	 * Returns an {@link Image} with the captured image.
	 * Remember to call this method from a new thread to avoid
	 * deadlocks.
	 * 
	 * @param encoding The encoding of the snapshot.
	 * @param width The width of the snapshot.
	 * @param height The height of the snapshot.
	 * @return An {@link Image} with the captured image.
	 * @throws MediaException If an error occurs while getting the snapshot.
	 */
	public Image getSnapshot(String encoding, int width, int height) throws MediaException {
		byte[] raw = getRawSnapshot(encoding, width, height);
		return Image.createImage(raw, 0, raw.length);
	}
	
	/**
	 * Returns the margins set for the video.
	 * @return The margins set for the video.
	 */
	public int[] getVideoMargins() {
		if(videoMargins == null || videoMargins.length < 4)
			return new int[]{0, 0, 0, 0};
		else
			return videoMargins;
	}
	
	/**
	 * Returns the underlying video player.
	 * 
	 * @return The underlying video player.
	 */
	public Player getVideoPlayer() {
		return videoPlayer;
	}
	
	/* (non-Javadoc)
	 * @see org.j4me.ui.components.Component#paintComponent(javax.microedition.lcdui.Graphics, org.j4me.ui.Theme, int, int, boolean)
	 */
	protected void paintComponent(Graphics g, Theme theme, int width, int height, boolean selected) {
		int margins[] = getVideoMargins();
		try {
			if(control == null) {
				control = (VideoControl)videoPlayer.getControl("VideoControl");
				control.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, getScreen().getCanvas());
				int x = g.getTranslateX();
				int y = g.getTranslateY();
				control.setDisplayLocation(x + margins[2], y + margins[0]);
				control.setDisplaySize(width - margins[1] - margins[3], height - margins[0] - margins[2]);
				control.setVisible(true);
			}
			if(videoPlayer.getState() != Player.STARTED) {
				videoPlayer.start();				
			}
		} catch(Exception e){
			g.drawString("Unable to render video", margins[2], margins[0], Graphics.TOP|Graphics.LEFT);
		}
	}
	
	/**
	 * Sets the preferred margins for the rendered video.
	 * If set to <code>null</code>, the margins will be 0.
	 * 
	 * @param videoMargins The preferred margins for the video.
	 */
	public void setVideoMargins(int[] videoMargins) {
		this.videoMargins = videoMargins;
	}

}
