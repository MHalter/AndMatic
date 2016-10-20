package de.halters.andmatic;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * Created by Halter on 17.10.2016.
 */

public class AppPreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        Preference.OnPreferenceChangeListener changeListener = new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString());
                return true;
            }
        };

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            Preference pref;
            int counter = 0;
            do {
                counter++;
                pref = this.getPreferenceManager().findPreference("btn" + counter + "Name");
                if (pref != null) {
                    pref.setOnPreferenceChangeListener(changeListener);
                    pref.setSummary(((EditTextPreference) pref).getText());

                    pref = this.getPreferenceManager().findPreference("btn" + counter + "Adress");
                    pref.setOnPreferenceChangeListener(changeListener);
                    pref.setSummary(((EditTextPreference) pref).getText());

                    pref = this.getPreferenceManager().findPreference("btn" + counter + "PressType");
                    pref.setOnPreferenceChangeListener(changeListener);
                    pref.setSummary(((EditTextPreference) pref).getText());
                }
            } while (pref != null);

            pref = this.getPreferenceManager().findPreference("ccuAddress");
            pref.setOnPreferenceChangeListener(changeListener);
            pref.setSummary(((EditTextPreference) pref).getText());

        }
    }


}
