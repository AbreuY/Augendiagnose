package de.eisfeldj.augendiagnose.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import de.eisfeldj.augendiagnose.Application;
import de.eisfeldj.augendiagnose.R;
import de.eisfeldj.augendiagnose.util.DialogUtil;
import de.eisfeldj.augendiagnose.util.ImageUtil;
import de.eisfeldj.augendiagnose.util.MediaStoreUtil;

/**
 * Main activity of the application
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SettingsActivity.setDefaultSharedPreferences(this);

		Application.setSharedPreferenceBoolean(R.string.key_internal_organized_new_photo, false);

		Intent intent = getIntent();
		if (Intent.ACTION_SEND_MULTIPLE.equals(intent.getAction()) && intent.getType() != null) {
			// Application was started from other application by passing a list of images - open
			// OrganizeNewPhotosActivity.
			ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
			if (imageUris != null) {
				ArrayList<String> fileNames = new ArrayList<String>();
				for (int i = 0; i < imageUris.size(); i++) {
					if (ImageUtil.getMimeType(imageUris.get(i)).startsWith("image/")) {
						String fileName = MediaStoreUtil.getRealPathFromURI(imageUris.get(i));
						if (fileName == null) {
							fileName = imageUris.get(i).getPath();
						}
						fileNames.add(fileName);
					}
				}
				boolean rightEyeLast = Application.getSharedPreferenceBoolean(R.string.key_eye_sequence_choice);
				OrganizeNewPhotosActivity.startActivity(this, fileNames.toArray(new String[0]),
						Application.getSharedPreferenceString(R.string.key_folder_photos), rightEyeLast);
			}
		}

		if (Intent.ACTION_MAIN.equals(intent.getAction()) && savedInstanceState == null) {
			boolean firstStart = false;
			// When starting from launcher, check if started the first time in this version. If yes, display release
			// notes.
			String storedVersionString = Application.getSharedPreferenceString(R.string.key_internal_stored_version);
			if (storedVersionString == null || storedVersionString.length() == 0) {
				storedVersionString = "12";
				firstStart = true;
			}
			int storedVersion = Integer.parseInt(storedVersionString);
			int currentVersion = Application.getVersion();

			if (storedVersion < currentVersion) {
				DialogUtil.displayReleaseNotes(this, firstStart, storedVersion + 1, currentVersion);
			}
		}

		if (!Application.isEyeFiInstalled()) {
			Button buttonEyeFi = (Button) findViewById(R.id.mainButtonOpenEyeFiApp);
			buttonEyeFi.setVisibility(View.GONE);
			Button buttonOrganize = (Button) findViewById(R.id.mainButtonOrganizeNewPhotos);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) buttonOrganize.getLayoutParams();
			params.weight = 2;
		}
	}

	/**
	 * Inflate options menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	/**
	 * Handle menu actions
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			SettingsActivity.startActivity(this);
			break;
		case R.id.action_help:
			DisplayHtmlActivity.startActivity(this, R.string.html_overview);
			break;
		}
		return true;
	}

	/**
	 * onClick action for Button to open the Eye-Fi app
	 * 
	 * @param view
	 */
	public void openEyeFiApp(View view) {
		if (Application.isEyeFiInstalled()) {
			startActivity(getPackageManager().getLaunchIntentForPackage("fi.eye.android"));
		}
		else {
			Intent googlePlayIntent = new Intent(Intent.ACTION_VIEW);
			googlePlayIntent.setData(Uri.parse("market://details?id=fi.eye.android"));
			try {
				startActivity(googlePlayIntent);
			}
			catch (Exception e) {
				DialogUtil.displayError(this, R.string.message_dialog_eyefi_not_installed, false);
			}
		}
	}

	/**
	 * onClick action for Button to display eye photos
	 * 
	 * @param view
	 */
	public void listFoldersForDisplayActivity(View view) {
		ListFoldersForDisplayActivity.startActivity(this,
				Application.getSharedPreferenceString(R.string.key_folder_photos));
	}

	/**
	 * onClick action for Button to organize new eye photos
	 * 
	 * @param view
	 */
	public void organizeNewFoldersActivity(View view) {
		boolean rightEyeLast = Application.getSharedPreferenceBoolean(R.string.key_eye_sequence_choice);
		OrganizeNewPhotosActivity.startActivity(this, Application.getSharedPreferenceString(R.string.key_folder_input),
				Application.getSharedPreferenceString(R.string.key_folder_photos), rightEyeLast);
	}

}
