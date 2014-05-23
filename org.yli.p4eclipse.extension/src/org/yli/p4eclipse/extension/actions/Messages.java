package org.yli.p4eclipse.extension.actions;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.yli.p4eclipse.extension.actions.messages"; //$NON-NLS-1$
	public static String PostReviewRequestAction_ERROR;
	public static String PostReviewRequestAction_FAILED_TO_POST_REVIEW_REQUEST;
	public static String PostReviewRequestAction_INFORMATION;
	public static String PostReviewRequestAction_NEVER_SHOW_DIALOG_WHEN_SUCCESSFULLY_POSTED;
	public static String PostReviewRequestAction_PLEASE_SET_UP_COMMAND_LINE;
	public static String PostReviewRequestAction_SUCCESSFULLY_POST_REVIEW_REQUEST;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
