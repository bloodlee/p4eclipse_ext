package org.yli.p4eclipse.extension.perferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.yli.p4eclipse.extension.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	    store.setDefault(PreferenceKeys.POST_REVIEW_CMD_LINE, "");
	    store.setDefault(PreferenceKeys.SHOW_POST_REVIEW_SUCCESS, true);
	    store.setDefault(PreferenceKeys.SHOW_DEBUG_INFO, false);
	}

}
