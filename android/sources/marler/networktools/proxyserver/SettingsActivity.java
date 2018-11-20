package marler.networktools.proxyserver;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(C0040R.menu.preferences);
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt("httpProxyListenPort", 8080);
    }
}
