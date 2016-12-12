package giwi.org.giwitter.helpers;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref
public interface MyPrefs {
    @DefaultString("")
    String username();

}
