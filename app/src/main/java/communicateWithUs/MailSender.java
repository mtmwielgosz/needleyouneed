package communicateWithUs;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

class MailSender {

    private Activity activity;

    MailSender(Activity activity){
        this.activity = activity;
    }

    void send(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"seba11147@tlen.pl"});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
        try {
            activity.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity.getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
