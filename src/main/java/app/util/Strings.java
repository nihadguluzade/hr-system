package app.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Utility class to manage internationalization.
 */
public abstract class Strings {

    private static ResourceBundle BUNDLE;
    private static final String BUNDLE_NAME = "strings";

    public static final String MAIN_APP_TITLE;

    static {
        final Locale locale = Locale.getDefault();

        BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

        MAIN_APP_TITLE = BUNDLE.getString("Main.AppTitle");
    }

    public static ResourceBundle GetBundle() {
        return BUNDLE;
    }

}
