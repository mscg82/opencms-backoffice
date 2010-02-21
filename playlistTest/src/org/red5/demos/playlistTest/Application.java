package org.red5.demos.playlistTest;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;

public class Application extends MultiThreadedApplicationAdapter {

	private static Logger log = Red5LoggerFactory.getLogger(Application.class, "playlistTest");

	private IScope appScope;

	// private IServerStream serverStream;
	private IServerPlaylist playlist;

	/** {@inheritDoc} */
	@Override
	public boolean appConnect(IConnection conn, Object[] params) {

		playlist = getPlaylist(appScope);

		if (!playlist.getRunOnStart() && playlist != null) {
			playlist.start();
		}

		return super.appConnect(conn, params);
	}

	/** {@inheritDoc} */
	@Override
	public void appDisconnect(IConnection conn) {
		playlist = getPlaylist(appScope);
		if (!playlist.getRunOnStart() && playlist != null) {
			playlist.stop();
		}
		super.appDisconnect(conn);
	}

	/** {@inheritDoc} */
	@Override
	public boolean appStart(IScope app) {
		log.debug("Playlist application starting...");

		playlist = getPlaylist(app);
		playlist.init();
		appScope = app;

		log.debug("Playlist application started.");
		return true;
	}

	private IServerPlaylist getPlaylist(IScope scope) {
		ApplicationContext appCtx = scope.getContext().getApplicationContext();
		IServerPlaylist pl = (IServerPlaylist) appCtx.getBean("playlistController");
		pl.setScope(scope);
		return pl;
	}

	public void setPlaylist(IServerPlaylist playlist) {
		this.playlist = playlist;
	}
}
