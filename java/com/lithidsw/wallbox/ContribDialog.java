package com.lithidsw.wallbox;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lithidsw.wallbox.utils.C;
import com.lithidsw.wallbox.utils.JsonHelper;

public class ContribDialog {

    private loadChange mLoad;
    Context mContext;
    TextView message;
    Status statFin = loadChange.Status.FINISHED;

    public ContribDialog(Context context) {
        mContext = context;
    }

    public void showContribAlert() {
        LayoutInflater li = LayoutInflater.from(mContext);
        View view = li.inflate(R.layout.contribute, null);
        message = (TextView) view.findViewById(R.id.changelog_body);
        mLoad = (loadChange) new loadChange().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR, C.URL_CONTRIB);
        new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.contributors))
                .setIcon(mContext.getResources().getDrawable(R.drawable.ic_launcher))
                .setView(view)
                .setCancelable(false)
                .setNegativeButton("Back",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                stopIt();
                            }
                        })
                .setPositiveButton("Github",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/lithid/WallBox"));
                                mContext.startActivity(browserIntent);
                                dialogInterface.dismiss();
                            }
                        }).show();
    }

    private void stopIt() {
        if (mLoad != null && mLoad.getStatus() != statFin) {
            mLoad.cancel(true);
            mLoad = null;
        }
    }

    class loadChange extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {
            return new JsonHelper().getJsonUrl(arg0[0]);
        }

        @Override
        protected void onPostExecute(final String string) {
            message.setText(Html.fromHtml(string));
            message.setMovementMethod(LinkMovementMethod.getInstance());
        }

    }

}
