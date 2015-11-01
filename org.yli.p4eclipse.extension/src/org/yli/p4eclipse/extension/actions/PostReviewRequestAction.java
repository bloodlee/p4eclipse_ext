package org.yli.p4eclipse.extension.actions;

import java.text.MessageFormat;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.yli.p4eclipse.extension.Activator;
import org.yli.p4eclipse.extension.jobs.PostReviewRequestJob;
import org.yli.p4eclipse.extension.util.ReviewRequestPoster;

import com.perforce.team.core.p4java.IP4PendingChangelist;
import com.perforce.team.ui.views.PendingView;

public class PostReviewRequestAction implements IObjectActionDelegate {

	private static final String PENDING_LIST_VIEW_ID = "com.perforce.team.ui.PendingChangelistView"; //$NON-NLS-1$
	
	private Shell shell;
	
	private ILog logger = Activator.getDefault().getLog();
	
	public PostReviewRequestAction() {
		
	}

	@Override
	public void run(IAction action) {

		if (!ReviewRequestPoster.canPostReviewRequest()) {
			MessageDialog.openError(shell, Messages.PostReviewRequestAction_ERROR, 
					Messages.PostReviewRequestAction_PLEASE_SET_UP_COMMAND_LINE);
			return;
		}
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewPart view = (IViewPart) page.getActivePart();
		
		if (view != null) {
			logInfo("retrieving the pending changelist id"); //$NON-NLS-1$

			PendingView pendingView = (PendingView)view;
			
			TreeViewer treeViewer = pendingView.getPerforceViewControl().getViewer();
			if (treeViewer != null) {
				TreeItem[] items = treeViewer.getTree().getSelection();
				for (TreeItem item : items) {
					IP4PendingChangelist pendingCL = (IP4PendingChangelist)(item.getData());
					
					if (pendingCL != null) {
						int changelistId = pendingCL.getId();
						
						if (changelistId != 0) {
							logInfo("Going to post review request CL#" + changelistId); //$NON-NLS-1$
							
							PostReviewRequestJob job = new PostReviewRequestJob(
									MessageFormat.format("Posting review request CL#{0}", Integer.toString(changelistId)),
									changelistId);
							job.schedule();
						}
						
					}
				}
			}
		} else {
			logInfo("Couldn't find the pending list view");
		}
	}

	private void logInfo(String message) {
		logger.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, message));
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

}
