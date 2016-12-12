package giwi.org.giwitter.helpers;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * The interface My prefs.
 */
@SharedPref
public interface MyPrefs {
    /**
     * Username string.
     *
     * @return the string
     */
    @DefaultString("")
    String username();

}
