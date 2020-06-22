package runcmd;

public class RunCmdConstants 
{
	public static String KEY_LAUNCH = "HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\";
	public static String KEY_LOOKUP = "HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths";
	
	public static final String REGISTRY_FIELD_DELIMITER = "    ";
	public static String KEY_PATH_NAME = "(Default)";
	public static String CREATED_BY_RUNCMD = "Created By RunCMD";
	
	public static String REGISTRY_SUCCESS_TEXT_KEY_CHANGE_COMPLETE = "The operation completed successfully.";
	public static int REGISTRY_SUCCESS_CODE = 0;
	public static String REGISTRY_ERROR_TEXT_NOT_RUNNING_AS_ADMINISTRATOR = "ERROR: Access is denied.";
	public static int REGISTRY_ERROR_CODE_NOT_RUNNING_AS_ADMINISTRATOR = -1;
	
	public static String KEY_SHORTCUT_PATH_UI_DELIMITER = "   ---   ";
}