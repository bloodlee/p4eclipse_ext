package org.yli.p4eclipse.extension.jobs;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.yli.p4eclipse.extension.Activator;
import org.yli.p4eclipse.extension.actions.Messages;
import org.yli.p4eclipse.extension.perferences.PreferenceKeys;
import org.yli.p4eclipse.extension.util.ReviewRequestPoster;

public class PostReviewRequestJob extends Job {

	private int changelistId;
	
	public PostReviewRequestJob(String name, int changelistId) {
		super(name);
		
		this.changelistId = changelistId;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		ReviewRequestPoster poster = new ReviewRequestPoster(); 
		
		if (!poster.postReviewRequest(changelistId)) {
			logInfo("Failed to post review request CL#" + changelistId); //$NON-NLS-1$
			
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					MessageDialog.openError(Display.getDefault().getActiveShell(), 
							Messages.PostReviewRequestAction_ERROR,
							MessageFormat.format(Messages.PostReviewRequestAction_FAILED_TO_POST_REVIEW_REQUEST,
									Integer.toString(changelistId)));
				}
				
			});
			
		} else {
			final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
			final Boolean showDialog = preferenceStore.getBoolean(PreferenceKeys.SHOW_POST_REVIEW_SUCCESS);
			
			if (showDialog) {
				
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						MessageDialogWithToggle
						.openInformation(
								Display.getDefault().getActiveShell(),
								Messages.PostReviewRequestAction_INFORMATION, 
								MessageFormat.format(Messages.PostReviewRequestAction_SUCCESSFULLY_POST_REVIEW_REQUEST, Integer.toString(changelistId)),
								Messages.PostReviewRequestAction_NEVER_SHOW_DIALOG_WHEN_SUCCESSFULLY_POSTED,
								showDialog, 
								preferenceStore,
								PreferenceKeys.SHOW_POST_REVIEW_SUCCESS);
					}
				});
				
			}
			
			logInfo("Succeed to post review request CL#" + changelistId); //$NON-NLS-1$
		}
		
		return Status.OK_STATUS;
	}

	private void logInfo(String message) {
		Activator.getDefault().getLog().log(new Status(IStatus.INFO, Activator.PLUGIN_ID, message));
	}
}
