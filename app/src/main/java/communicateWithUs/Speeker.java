package communicateWithUs;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;

class Speeker {

    private Activity activity;

    Speeker(Activity activity){
        this.activity = activity;
    }

    void promptSpeechInput( int req) {

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
