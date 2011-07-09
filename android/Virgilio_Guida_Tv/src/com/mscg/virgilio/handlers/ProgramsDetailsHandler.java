package com.mscg.virgilio.handlers;

import java.text.SimpleDateFormat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mscg.virgilio.R;
import com.mscg.virgilio.VirgilioGuidaTvPrograms;
import com.mscg.virgilio.listener.DetailsButtonsListener;
import com.mscg.virgilio.programs.ProgramDetails;
import com.mscg.virgilio.programs.TVProgram;
import com.mscg.virgilio.util.ListViewScrollerThread;

public class ProgramsDetailsHandler extends Handler {

    private VirgilioGuidaTvPrograms context;
    private TVProgram program;
    private ProgressDialog progressDialog;

    private String contactingServer;
    private String analyzingData;
    private String errorTitle;
    private String errorText;
    // private String detailsTitle;
    private String closeText;
    private SimpleDateFormat hourFormatter;

    public ProgramsDetailsHandler(VirgilioGuidaTvPrograms context) {
        super();
        this.context = context;

        contactingServer = context.getString(R.string.contacting_server);
        analyzingData = context.getString(R.string.analyzing_data);
        errorTitle = context.getString(R.string.error_title);
        errorText = context.getString(R.string.error_text);
        // detailsTitle = context.getString(R.string.details_title);
        closeText = context.getString(R.string.close);
        hourFormatter = new SimpleDateFormat(context.getString(R.string.hours_format));
    }

    @Override
    public void handleMessage(Message msg) {
        AlertDialog.Builder ad = null;
        Dialog d = null;
        Bundle b = msg.getData();
        int caseValue = b.getInt(DownloadProgressHandler.TYPE);

        switch (caseValue) {
        case DownloadProgressHandler.START_DOWNLOAD:
            if (progressDialog != null)
                progressDialog.dismiss();
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(contactingServer);
            progressDialog.show();
            break;
        case DownloadProgressHandler.UPDATE_PROGRESS:
            if (progressDialog == null || progressDialog.isIndeterminate()) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                progressDialog = new ProgressDialog(context);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setTitle(analyzingData);
                progressDialog.setMax(10000);
                progressDialog.setMax(10000);
                progressDialog.setProgress(0);
            }
            int position = b.getInt("progress");
            progressDialog.setProgress(position);
            break;
        case DownloadProgressHandler.END_DOWNLOAD:
            break;
        case DownloadProgressHandler.END_PARSE:
            if (progressDialog != null)
                progressDialog.dismiss();

            d = new Dialog(context);
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.details_layout);
            // d.setTitle(detailsTitle);

            ProgramDetails details = program.getProgramDetails();

            ((TextView) d.findViewById(R.id.detailTitle)).setText(details.getTitle());
            ((TextView) d.findViewById(R.id.detailText)).setText(details.getDescription());

            ((TextView) d.findViewById(R.id.detailStart)).setText(hourFormatter.format(program.getStartTime()));
            ((TextView) d.findViewById(R.id.detailEnd)).setText(hourFormatter.format(program.getEndTime()));

            Button button = (Button) d.findViewById(R.id.detailsCancel);
            button.setText(closeText);
            button.setOnClickListener(new DetailsButtonsListener(d));

            ImageView image = (ImageView) d.findViewById(R.id.detailIcon);
            switch (details.getLevel()) {
            case ProgramDetails.GREEN:
                image.setImageResource(R.drawable.green);
                break;
            case ProgramDetails.YELLOW:
                image.setImageResource(R.drawable.yellow);
                break;
            case ProgramDetails.RED:
                image.setImageResource(R.drawable.red);
                break;
            }

            d.setCancelable(true);
            d.show();

            // ad = new AlertDialog.Builder(context);
            // ad.setTitle(programDetails.getTitle());
            // ad.setMessage(programDetails.getDescription());
            // ad.setNegativeButton(closeText, new OnClickListener() {
            // @Override
            // public void onClick(DialogInterface dialog, int which) {
            // dialog.dismiss();
            // }
            // });
            // ad.setCancelable(true);
            // ad.show();
            break;
        case DownloadProgressHandler.ERROR:
            if (progressDialog != null)
                progressDialog.dismiss();
            ad = new AlertDialog.Builder(context);
            ad.setTitle(errorTitle);
            ad.setMessage(errorText + " " + b.getString(DownloadProgressHandler.MESSAGE));
            ad.setCancelable(true);
            ad.show();
            break;
        case DownloadProgressHandler.SCROLL_LIST:
            int scrollPosition = b.getInt(ListViewScrollerThread.SCROLL_POSITION);
            context.getProgramsListView().setSelection(scrollPosition);
            break;
        }
    }

    public synchronized TVProgram getProgram() {
        return program;
    }

    public synchronized void setProgram(TVProgram program) {
        this.program = program;
    }

}
