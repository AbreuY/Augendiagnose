package de.eisfeldj.augendiagnose.util;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import de.eisfeldj.augendiagnose.activities.SelectTwoPicturesActivity;
import de.eisfeldj.augendiagnose.components.EyeImageView;
import de.eisfeldj.augendiagnose.util.imagefile.EyePhoto;

/**
 * A class handling the selection two pictures, returning the pictures.
 */
public final class TwoImageSelectionHandler extends BaseImageSelectionHandler {
	/**
	 * The activity for selection.
	 */
	private SelectTwoPicturesActivity activity = null;

	/**
	 * A holder of the TwoImageSelectionHandler as singleton.
	 */
	private static volatile TwoImageSelectionHandler singleton;

	/**
	 * Get an instance of the handler - it is handled as singleton.
	 *
	 * @return an instance of the handler.
	 */
	public static TwoImageSelectionHandler getInstance() {
		if (singleton == null) {
			singleton = new TwoImageSelectionHandler();
		}
		return singleton;
	}

	/**
	 * Hide default constructor, to ensure singleton use.
	 */
	private TwoImageSelectionHandler() {
		// default constructor
	}

	/**
	 * Set the activity for first selection.
	 *
	 * @param activity
	 *            The activity.
	 */
	public void setActivity(final SelectTwoPicturesActivity activity) {
		this.activity = activity;
	}

	/**
	 * Clean all references.
	 */
	public static void clean() {
		singleton = null;
	}

	/**
	 * Highlight an EyeImageView if it is selected.
	 *
	 * @param view
	 *            the EyeImageView.
	 */
	public void highlightIfSelected(final EyeImageView view) {
		// TODO: fix issue on selecting first image - somehow this method is called then again with different view,
		// which leads to wrong highlighting on deselecting.
		if (hasSelectedView() && getSelectedImage().equals(view.getEyePhoto())) {
			selectView(view);
		}
	}

	/**
	 * Prepare a GridView for selection of the pictures.
	 *
	 * @param view
	 *            The GridView to be prepared.
	 * @param hasContextMenu
	 *            Flag indicating if a context menu should be enabled.
	 */
	public void prepareViewForSelection(final EyeImageView view, final boolean hasContextMenu) {
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (!hasSelectedView()) {
					selectView(view);
				}
				else if (getSelectedImage().equals(view.getEyePhoto())) {
					cleanSelectedViews();
				}
				else {
					createResponse(getSelectedImage(), view.getEyePhoto());
					cleanSelectedViews();
				}
			}
		});

		if (hasContextMenu) {
			view.setOnCreateContextMenuListener(getActivity());
		}
	}

	/**
	 * Return the paths of the two selected files to the parent activity.
	 *
	 * @param image1
	 *            the first selected image.
	 * @param image2
	 *            the second selected image.
	 */
	private void createResponse(final EyePhoto image1, final EyePhoto image2) {
		activity.returnResult(image1.getAbsolutePath(), image2.getAbsolutePath());
	}

	@Override
	protected Activity getActivity() {
		return activity;
	}
}
