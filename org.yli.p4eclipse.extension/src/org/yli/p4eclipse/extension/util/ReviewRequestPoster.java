package org.yli.p4eclipse.extension.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.yli.p4eclipse.extension.Activator;
import org.yli.p4eclipse.extension.perferences.PreferenceKeys;

/**
 * Utility to post review request
 * 
 * @author yli
 *
 */
public class ReviewRequestPoster {

	/**
	 * Check if review request can be posted.
	 * @return true or false.
	 */
	public static boolean canPostReviewRequest() {
		String cmdLine = getPostReviewCmdLine();
		String directory = getReviewboardUtilityFolder();
		return cmdLine != null && !cmdLine.isEmpty() 
			&& directory != null && !directory.isEmpty();
	}

	private static String getReviewboardUtilityFolder() {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		return preferenceStore.getString(PreferenceKeys.REVIEW_BOARD_UTIL_FOLDER);
	}

	private static String getPostReviewCmdLine() {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		return preferenceStore.getString(PreferenceKeys.POST_REVIEW_CMD_LINE);
	}
	
	private static boolean isDebugEnabled() {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		return preferenceStore.getBoolean(PreferenceKeys.SHOW_DEBUG_INFO);
	}
	
	public boolean postReviewRequest(int changelistId) {
		StringBuilder sb = new StringBuilder();
		
		if (!canPostReviewRequest() || changelistId <= 0) {
			return false;
		}
		
		String cmdLine = getPostReviewCmdLine();
		String utilDirectory = getReviewboardUtilityFolder();
		boolean debugMode = isDebugEnabled();
		
		MessageConsole console = new MessageConsole("post request to review board", null);
		console.activate();
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] {console});
		
		MessageConsoleStream newMessageStream = console.newMessageStream();
		
		
		Process process = null;
		
		try {
			String[] cmdArrays = cmdLine.split("\\s");
			String[] finalCmdArrays = new String[cmdArrays.length + 2];
			
			System.arraycopy(cmdArrays, 0, finalCmdArrays, 0, cmdArrays.length);
			finalCmdArrays[cmdArrays.length] = Integer.toString(changelistId);
			
			if (debugMode) {
				finalCmdArrays[cmdArrays.length + 1] = "-d";
			} else {
				finalCmdArrays[cmdArrays.length + 1] = "";
			}
			
			ProcessBuilder pb = new ProcessBuilder(finalCmdArrays);
			pb.redirectErrorStream(true);
			pb.directory(new File(utilDirectory).getAbsoluteFile());
			
			process = pb.start();
		
			InputStream fis = process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        
			String line;
	        while ((line = br.readLine()) != null) {
	            sb.append(line).append("\n");
	            newMessageStream.println(line);
	        }
			
	        int returnCode = process.waitFor();
	        if (returnCode != 0) {
	        	Activator.getDefault().getLog().log(
	        			new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Review request posting failed."));
	        	Activator.getDefault().getLog().log(
	        			new Status(IStatus.ERROR, Activator.PLUGIN_ID, sb.toString()));
	        	return false;
	        }
	        
	        return true;
	        	
		} catch (InterruptedException e) {
			Activator.getDefault().getLog().log(
        			new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage()));
		} catch (IOException e) {
			Activator.getDefault().getLog().log(
        			new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage()));
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
		return false;
	}
}
