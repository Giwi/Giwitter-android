package giwi.org.giwitter.helpers;

import java.io.IOException;

/**
 * Created by xavier on 12/12/16.
 */

class CustomException extends IOException {
    CustomException(String statusText) {
        super(statusText);
    }
}
