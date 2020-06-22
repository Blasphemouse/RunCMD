package runcmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map.Entry;

import runcmd.gui.RunCmdGUI;

/**
 * Spins up processes to call the registry via command-line.
 */
public class RegistryEditor 
{		
	/**
	 * Validates the application has admin access by adding a dummy entry and cleaning it up.
	 */
	public static int validateRunAsAdministrator()
	{
		int returnCode = addValue("chromeButLonger.exe", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
		if(returnCode==RunCmdConstants.REGISTRY_SUCCESS_CODE)
		{
			deleteValue("chromeButLonger.exe");
		}
		
		return returnCode;
	}
	
	/** 
	 * Adds a new registry entry for the given shortcut & path.
	 * Returns success or error (if lacking admin access).
	 */
	public static int addValue(String shortcut, String path)
    {
		int returnCode = RunCmdConstants.REGISTRY_SUCCESS_CODE;
		
    	ProcessBuilder builderAddKey = new ProcessBuilder(new String[] {"cmd.exe", "/C", "reg add \"" + RunCmdConstants.KEY_LAUNCH + shortcut + "\" /ve /d \"" + path + "\" /f"});
			StringBuilder queryResultsSB = new StringBuilder();
			StringBuilder errorResultsSB = new StringBuilder();
		runProcess(builderAddKey, queryResultsSB, errorResultsSB);
		
		if(errorResultsSB.toString().contains(RunCmdConstants.REGISTRY_ERROR_TEXT_NOT_RUNNING_AS_ADMINISTRATOR))
		{
			returnCode = RunCmdConstants.REGISTRY_ERROR_CODE_NOT_RUNNING_AS_ADMINISTRATOR;
			return returnCode;
		}
		
		ProcessBuilder builderAddComment = new ProcessBuilder(new String[] {"cmd.exe", "/C", "reg add \"" + RunCmdConstants.KEY_LAUNCH + shortcut + "\" /v Note /d \"Created By RunCMD\" /f"});
			StringBuilder queryResultsCommentSB = new StringBuilder();
			StringBuilder errorResultsCommentSB = new StringBuilder();
		runProcess(builderAddComment, queryResultsCommentSB, errorResultsCommentSB);
		
		RunCmdGUI.addKey(shortcut, path);
		
        return returnCode;
    }
	
	/**
	 * Delete registry entry with the given shortcut.
	 */
	public static void deleteValue(String shortcut)
    {
		ProcessBuilder builderDeleteKey = new ProcessBuilder(new String[] {"cmd.exe", "/C", "reg delete \"" + RunCmdConstants.KEY_LAUNCH + shortcut + "\" /f"});
			StringBuilder queryResultsSB = new StringBuilder();
			StringBuilder errorResultsSB = new StringBuilder();
		runProcess(builderDeleteKey, queryResultsSB, errorResultsSB);
		
		RunCmdGUI.removeKey(shortcut);
    }
	
	/**
	 * Called upon launch of RunCMD to populate the registry keys which were created by RunCMD on previous launches.
	 */
	public static HashMap<String,String> addAllKeysCreatedByRunCMD()
	{
		ProcessBuilder builderGetExistingRunCMDKeys = new ProcessBuilder(new String[] {"cmd.exe", "/C", "reg query \"" + RunCmdConstants.KEY_LOOKUP + "\" /s"});
		StringBuilder queryResultsSB = new StringBuilder();
		StringBuilder errorResultsSB = new StringBuilder();
		runProcess(builderGetExistingRunCMDKeys, queryResultsSB, errorResultsSB);
		
		HashMap<String,String> keysCreatedByRunCMD = RegistryKeyParser.parseKeys(queryResultsSB.toString());
		for(Entry<String,String> e: keysCreatedByRunCMD.entrySet())
		{
			RunCmdGUI.addKey(e.getKey(), e.getValue());
		}
		
		return keysCreatedByRunCMD;
	}
	
	/**
	 * Executes the requested command; populates queryResults and errorResults
	 */
	private static void runProcess(ProcessBuilder pb, StringBuilder queryResultsSB, StringBuilder errorResultsSB)
	{
		try {
			Process p = pb.start();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			String line = reader.readLine();
			while(line != null)
			{
				queryResultsSB.append(line + "\n");
				line = reader.readLine();
			}
			String errorLine = errorReader.readLine();
			while(errorLine != null)
			{
				errorResultsSB.append(errorLine + "\n");
				errorLine = reader.readLine();
			}
			
			p.waitFor(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
