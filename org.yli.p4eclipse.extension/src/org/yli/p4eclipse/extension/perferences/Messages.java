package org.yli.p4eclipse.extension.perferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.yli.p4eclipse.extension.perferences.messages"; //$NON-NLS-1$
	public static String WorkbenchPerferencePage_ENABLE_DEBUG;
	public static String WorkbenchPerferencePage_POST_REVIEW_CMD_LINE;
	public static String WorkbenchPerferencePage_REVIEWBOARD_UTILITIES_FOLDER;
	public static String WorkbenchPerferencePage_TITLE;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
