package socialMedia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mtmwi on 24.03.2017.
 */

final class JsonConverter {

    static final boolean isEmpty(String s) {

        return s == null || s.equals("");
    }

    static final String getJSONResult(String query) throws IOException {

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

    static final String addEmptyLines(String line) {

        if (!isEmpty(line)) {
            line = "\n" + line + "\n";
        }

        return line;
    }
}
