package org.yli.p4eclipse.extension.perferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.yli.p4eclipse.extension.Activator;

/**
 * Perference page.
 * 
 * @author yli
 *
 */
public class WorkbenchPerferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	    setDescription(Messages.WorkbenchPerferencePage_TITLE);
	}

	@Override
	protected void createFieldEditors() {
		addField(new DirectoryFieldEditor(PreferenceKeys.REVIEW_BOARD_UTIL_FOLDER,
				Messages.WorkbenchPerferencePage_REVIEWBOARD_UTILITIES_FOLDER,
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceKeys.POST_REVIEW_CMD_LINE, 
				Messages.WorkbenchPerferencePage_POST_REVIEW_CMD_LINE,
		        getFieldEditorParent()));
	}

}
