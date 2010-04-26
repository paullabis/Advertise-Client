package com.advertiseclient.activity;

import org.restlet.resource.ClientResource;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.advertiseclient.utility.CustomOnClickListener;
import com.advertiseserver.domain.Ad;
import com.advertiseserver.service.AdService;

public class AdvertiseActivity extends Activity {
	
	private AdService adService;

	/** The progress dialog used during the interactions with the server. */
	private ProgressDialog progressDialog;
	
	private Ad ad;
	
	/** Global handler used to refresh the interface. */
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			switch (msg.what) {
			case 0:
				// Update the interface using the retrieved contact
				if (ad != null) {
					// Update the remote contact
					TextView title = (TextView) findViewById(R.id.field_title);
					TextView description = (TextView) findViewById(R.id.field_description);

					title.setText(ad.getTitle().toString());
					description.setText(ad.getDescription().toString());

					findViewById(R.id.update_button).setVisibility(View.VISIBLE);
				}
				break;
			case 1:
				// Update the interface once the contact has been updated
				break;
			case 2:
				// Error.
				alertDialog.setMessage(msg.getData().getString("msg"));
				alertDialog.show();
				break;

			default:
				super.handleMessage(msg);
				break;
			}
		}
	};
	
	private AlertDialog alertDialog;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertise);
        
        ClientResource clientResource = new ClientResource("http://buzzbunk.appspot.com/ad/123");
        adService = clientResource.wrap(AdService.class);
        
		Button getButton = (Button) findViewById(R.id.get_button);
		Button updateButton = (Button) findViewById(R.id.update_button);
		updateButton.setVisibility(View.INVISIBLE);
        
		getButton.setOnClickListener(new CustomOnClickListener() {
			@Override
			public void onClick(View v) {
				progressDialog = ProgressDialog.show(v.getContext(),
						getString(R.string.process_dialog_title),
						getString(R.string.process_dialog_get), true, false);
				Thread thread = new Thread(this);
				thread.start();
				
			}
			
			@Override
			public void run() {
				try {
					// Get the remote contact
					ad = adService.retrieve();
					// The task is over, let the parent conclude.
					handler.sendEmptyMessage(0);
				} catch (Exception e) {
					Message msg = Message.obtain(handler, 2);
					Bundle data = new Bundle();
					data.putString("msg", "Cannot get the ad due to: "	+ e.getMessage());
					msg.setData(data);
					handler.sendMessage(msg);
				}
			}
		});
		
		updateButton.setOnClickListener(new CustomOnClickListener() {
			
			@Override
			public void onClick(View v) {
				progressDialog = ProgressDialog.show(v.getContext(),
						getString(R.string.process_dialog_title),
						getString(R.string.process_dialog_update), true, false);
				Thread thread = new Thread(this);
				thread.start();
			}
			
			@Override
			public void run() {
				TextView title = (TextView) findViewById(R.id.field_title);
				TextView description = (TextView) findViewById(R.id.field_description);
				
				ad.setTitle(title.getText().toString());
				ad.setDescription(description.getText().toString());
				
				try {
					adService.store(ad);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					Message msg = Message.obtain(handler, 2);
					Bundle data = new Bundle();
					data.putString("msg", "Cannot update the ad due to: " + e.getMessage());
					msg.setData(data);
					handler.sendMessage(msg);
				
				}
				
			}
		});
		
		// Initializes the alert dialog.
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(getString(R.string.error_dialog_title));
		alertDialog.setButton(getString(R.string.error_dialog_button),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
    }
}