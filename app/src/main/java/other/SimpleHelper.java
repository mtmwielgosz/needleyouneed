package other;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;

/**
 * Created by mtmwi on 24.03.2017.
 */

public final class SimpleHelper {

    public static final boolean isEmpty(String s) {

        return s == null || s.equals("");
    }

    public static final void promptSpeechInput(Activity activity, int req) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        try {
            activity.startActivityForResult(intent, req);
        } catch (ActivityNotFoundException a) {
            a.printStackTrace();
        }
    }
}
