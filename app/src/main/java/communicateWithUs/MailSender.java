package communicateWithUs;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.widget.Toast;

class MailSender {

    private Activity activity;

    MailSender(Activity activity){
        this.activity = activity;
    }

    void send(String tittle, String textBody){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"seba11147@tlen.pl"});
        i.putExtra(Intent.EXTRA_SUBJECT, tittle);
        i.putExtra(Intent.EXTRA_TEXT   , textBody);
        try {
            activity.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity.getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
