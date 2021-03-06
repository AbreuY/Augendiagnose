package de.eisfeldj.augendiagnose;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import de.eisfeldj.augendiagnose.activities.MainActivity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Utility class to hold private constants.
 */
@SuppressFBWarnings(value = "NM_SAME_SIMPLE_NAME_AS_SUPERCLASS",
		justification = "Intentionally using same name as superclass")
public final class ApplicationSettings extends de.jeisfeld.augendiagnoselib.ApplicationSettings {
	/**
	 * An instance of this class.
	 */
	private static volatile ApplicationSettings mInstance;

	/**
	 * Keep constructor private.
	 */
	private ApplicationSettings() {
	}

	/**
	 * Get an instance of this class.
	 *
	 * @return An instance of this class.
	 */
	public static ApplicationSettings getInstance() {
		if (mInstance == null) {
			mInstance = new ApplicationSettings();
		}
		return mInstance;
	}

	@Override
	public void startApplication(@NonNull final Activity triggeringActivity) {
		Intent intent = new Intent(triggeringActivity, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		triggeringActivity.startActivity(intent);
	}
}
