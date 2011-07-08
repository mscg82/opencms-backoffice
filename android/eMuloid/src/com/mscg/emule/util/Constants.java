package com.mscg.emule.util;


public class Constants {

	public static final String LOGGED_CHECK_XPATH = "//a[contains(@href, 'w=transfer')]";

	public static class Intent {
		public static final String EXIT_PARAM = "com.mscg.emule.exit";
	}

	public static class Messages {
		public static final int START_DOWNLOAD = 0;
		public static final int ANALYZING_RESPONSE = 1;

		public static class Login {
			public static final int BAD_PASSWORD = 2;
			public static final int LOGGED_IN = 3;
		}

		public static class SpeedBox {
			public static final int UPDATE_SERVER = 2;
			public static final int UPDATE_KAD = 3;
			public static final int UPDATE_DOWNLOAD_SPEED = 4;
			public static final int UPDATE_UPLOAD_SPEED = 5;
		}

		public static class Transfers {
			public static final int UPDATE_CATEGORIES = 6;
			public static final int UPDATE_DOWNLOADS = 7;
		}

		public static final int UPDATE_TERMINATED = 1000;

		public static final int ERROR = -1;
		public static final int NOT_LOGGED = -2;

		public static final int ARG1_MESSAGE_CODE   = 0;
		public static final int ARG1_MESSAGE_STRING = 1;
	}

}
