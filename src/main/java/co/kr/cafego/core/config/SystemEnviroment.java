package co.kr.cafego.core.config;

public class SystemEnviroment {

	public final static String PROFILE_LOCAL = "local";

	public final static String PROFILE_SERVER = "server";

	private static String activeProfile;

	public final static String[] CHIEF_PROFILE_CANDIDATES = new String[] { PROFILE_SERVER,
			PROFILE_LOCAL};

	public static String getActiveProfile() {
        return activeProfile;
    }
	
	public static String setActiveProfile(String profile) {
        return activeProfile = profile;
    }
}
