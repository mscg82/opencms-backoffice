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

import org.j4me.ui.DeviceScreen;
import org.j4me.ui.Theme;
import org.j4me.ui.UIManager;

/**
 * This screen paints the video captured
 * from the device camera on the main area
 * of the screen, allowing to capture a
 * snapshot of the shown image.
 * 
 * @author Giuseppe Miscione
 *
 */
public class VideoScreen extends DeviceScreen {
	
	/**
	 * Classes implementing this interface will
	 * be notified when the user uses the right
	 * soft key to take a snapshot from the camera.
	 */
	public interface VideoSnapshotListener {
		
		/**
		 * This event is raised when the user presses
		 * the right soft key to take a snapshot from
		 * the provided {@link VideoScreen} object.
		 */
		public void takeSnapshot(VideoScreen videoScreen);
		
	}
	
	protected Player videoPlayer;
	protected VideoControl videoControl;
	
	protected int videoMargins[];
	protected DeviceScreen previous;

	protected VideoSnapshotListener snapshotListener;

	/**
	 * Creates a new instance of a {@link VideoScreen},
	 * setting the video margins to 0.
	 */
	public VideoScreen() {
		this(null, null, null);
	}
	
	/**
	 * Creates a new instance of a {@link VideoScreen},
	 * setting the video margins to 0.
	 * @param previous The previous {@link DeviceScreen}.
	 */
	public VideoScreen(DeviceScreen previous) {
		this(null, previous, null);
	}
	
	/**
	 * Creates a new instance of a {@link VideoScreen} with the provided
	 * preferred video margins. This constructor sets also the previous
	 * screen to which the application returns when the left soft key
	 * is pressed.
	 * @param previous The previous {@link DeviceScreen}.
	 * @param videoMargins The preferred margins of the renderer video.
	 */
	public VideoScreen(DeviceScreen previous, int videoMargins[]) {
		this(null, previous, videoMargins);
	}

	/**
	 * Creates a new instance of a {@link VideoScreen} with the provided
	 * preferred video margins.
	 * @param videoMargins The preferred margins of the renderer video.
	 */
	public VideoScreen(int videoMargins[]) {
		this(null, null, videoMargins);
	}

	/**
	 * Creates a new instance of a {@link VideoScreen},
	 * setting the video margins to 0.
	 * @param title The title of the screen.
	 */
	public VideoScreen(String title) {
		this(title, null, null);
	}
	
	/**
	 * Creates a new instance of a {@link VideoScreen} with the provided
	 * preferred video margins. This constructor sets also the previous
	 * screen to which the application returns when the left soft key
	 * is pressed.
	 * @param title The title of the screen.
	 * @param previous The previous {@link DeviceScreen}.
	 * @param videoMargins The preferred margins of the renderer video.
	 */
	public VideoScreen(String title, DeviceScreen previous, int videoMargins[]) {
		setTitle(title);
		setPrevious(previous);
		setVideoMargins(videoMargins);
		Theme theme = UIManager.getTheme();
		setMenuText(
			previous == null ? theme.getMenuTextForExit() : theme.getMenuTextForCancel(),
			"Snapshot");
	}
	
	protected void acceptNotify() {
		if(snapshotListener != null) {
			new Thread() {

				public void run() {
					snapshotListener.takeSnapshot(VideoScreen.this);					
				}
				
			}.start();
		}
	}

	/**
	 * Stops the video playback and release system resource.
	 */
	public void closeVideo() {
		if(videoPlayer != null && videoPlayer.getState() == Player.STARTED) {
			videoPlayer.deallocate();
			videoPlayer = null;
			videoControl = null;
		}
	}

	protected void declineNotify() {
		closeVideo();
		if(previous != null) {
			previous.show();
			super.declineNotify();
		}
		else {
			// exit midlet
			UIManager.getMidlet().notifyDestroyed();
		}
	}
	
	/**
	 * Returns the {@link DeviceScreen} object
	 * to which the execution returns when the
	 * left soft key is pressed.
	 * 
	 * @return The {@link DeviceScreen} object
	 * to which the execution returns when the
	 * left soft key is pressed.
	 */
	public DeviceScreen getPrevious() {
		return previous;
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
		return videoControl.getSnapshot(encoding == null ?
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
	 * Returns the actual snapshot listener object.
	 * @return The actual snapshot listener object.
	 */
	public VideoSnapshotListener getSnapShotListener() {
		return snapshotListener;
	}

	/**
	 * Returns the underlying video control object
	 * or <code>null</code> if the object wasn't initialized.
	 * 
	 * @return The underlying video control object.
	 */
	public VideoControl getVideoControl() {
		return videoControl;
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
	
	/**
	 * When this screen is hide, close the video and free
	 * system resources.
	 */
	public void hideNotify() {
		try {
			closeVideo();
		} catch(Exception e){}
	}
	
	/* (non-Javadoc)
	 * @see org.j4me.ui.DeviceScreen#paint(javax.microedition.lcdui.Graphics)
	 */
	protected void paint(Graphics g) {
		int margins[] = getVideoMargins();
		try {
			if(videoControl == null) {
				videoControl = (VideoControl)videoPlayer.getControl("VideoControl");
				videoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, getCanvas());
				int x = g.getTranslateX();
				int y = g.getTranslateY();
				videoControl.setDisplayLocation(x + margins[2], y + margins[0]);
				videoControl.setDisplaySize(
					g.getClipWidth() - margins[1] - margins[3],
					g.getClipHeight() - margins[0] - margins[2]);
				videoControl.setVisible(true);
			}
			if(videoPlayer.getState() != Player.STARTED) {
				videoPlayer.start();
			}
		} catch(Exception e){
			g.drawString("Unable to render video", margins[2], margins[0], Graphics.TOP|Graphics.LEFT);
		}
	}

	/**
	 * Sets the {@link DeviceScreen} object
	 * to which the execution returns when the
	 * left soft key is pressed. If set to
	 * <code>null</code>, the midlet will be closed.
	 * 
	 * @param previous The {@link DeviceScreen} object
	 * to set.
	 */
	public void setPrevious(DeviceScreen previous) {
		this.previous = previous;
	}
	
	/**
	 * Sets the snapshot listener that will be notified when
	 * the user uses the right soft key to take a snapshot from
	 * the camera.
	 * @param snapShotListener The snapshot listener.
	 */
	public void setSnapShotListener(VideoSnapshotListener snapShotListener) {
		this.snapshotListener = snapShotListener;
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
	
	/**
	 * When this screen is shown, allocate a camera player
	 * and realize it.
	 */
	public void showNotify() {
		if(videoPlayer != null) {
			closeVideo();
			videoPlayer = null;
		}
		
		try {
			try {
				videoPlayer = Manager.createPlayer("capture://image");
			} catch(Exception e) {
				videoPlayer = Manager.createPlayer("capture://video");
			}
			videoPlayer.realize();
		} catch(IOException e){
			e.printStackTrace();
		} catch(MediaException e){
			e.printStackTrace();
		}
	}

}
