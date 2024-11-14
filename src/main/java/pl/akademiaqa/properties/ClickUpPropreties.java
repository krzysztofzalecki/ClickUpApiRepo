package pl.akademiaqa.properties;

import java.util.ResourceBundle;

public class ClickUpPropreties {

    private static final String TOKEN = "clickup.token";
    private static final String TEAM_ID = "clikup.team.id";

    private static String getProperty(String key) {
        return ResourceBundle.getBundle("clickup").getString(key);
    }

    public static String getToken() {
        return getProperty(TOKEN);
    }

    public static String getTeamId() {
        return getProperty(TEAM_ID);
    }

}
