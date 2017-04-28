package communicateWithUs;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mtmwi.needleyouneed.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    @BindView(R.id.record_button)
    Button recordButton;
    @BindView(R.id.send_button)
    Button sendButton;
    @BindView(R.id.title_spinner)
    Spinner titleSpinner;
    @BindView(R.id.message_text)
    EditText messageText;
    private int MESSAGE_REQ = 111;

    private Speeker speeker;
    private MailSender mailSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);
        speeker = new Speeker(this);
        mailSender = new MailSender(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO
        String[] themas = new String[]{"inne", "zainteresowanie kocykiem", "nawiązanie współpracy"};

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, themas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        titleSpinner.setAdapter(dataAdapter);
    }

    @OnClick({R.id.record_button, R.id.send_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.record_button:
                speeker.promptSpeechInput(MESSAGE_REQ);
                break;
            case R.id.send_button:
                mailSender.send(messageText.getText().toString(),titleSpinner.getSelectedItem().toString());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MESSAGE_REQ && resultCode == RESULT_OK && null != data) {

            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null) {

                String accText = messageText.getText().toString();
                messageText.setText(accText + " " + result.get(0));
            }
        }
    }

    @Override
    public void onInit(int i) {
    }
}
