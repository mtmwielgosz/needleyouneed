package other;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

    public static final String getJSONResult(String query) throws MalformedURLException, IOException {

        HttpURLConnection connection = (HttpURLConnection) (new URL(query)).openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String result = "";
        String line;
        while ((line = br.readLine()) != null) {
            result += line + "\n";
        }
        br.close();

        return result;
    }

    public static final String addEmptyLines(String line) {

        if (!SimpleHelper.isEmpty(line)) {
            line = "\n" + line + "\n";
        }

        return line;
    }
}
