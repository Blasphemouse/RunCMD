package runcmd;

import java.util.HashMap;
import java.util.Scanner;


public class RegistryKeyParser 
{		
	/**
	 * Examines the output from the registry query to find keys previously added by RunCMD. 
	 */
	public static HashMap<String,String> parseKeys(String rawRegistryQueryOutput)
	{
		HashMap<String,String> keysAddedByRunCMD = new HashMap<String,String>();
		
		Scanner scanner = new Scanner(rawRegistryQueryOutput);
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			
			if(line.startsWith("HKEY"))
			{
				String shortcut = line.trim().substring(RunCmdConstants.KEY_LAUNCH.length());
				String path = null;
				
				while(line!=null && !line.equals(""))
				{
					line = scanner.nextLine();
					
					if(line==null || line.equals(""))		
					{	
						break;	
					}
					else
					{
						line = line.trim();
						String[] nameTypeValue = line.split(RunCmdConstants.REGISTRY_FIELD_DELIMITER);
						if(nameTypeValue[0].equals(RunCmdConstants.KEY_PATH_NAME))
						{
							path = nameTypeValue[2];
						}
						
						//Most entries have Name, Type, and Value, but there may be some with name & type, but missing values
						//The ones we care about will be a subset of the ones with all 3
						else if(nameTypeValue.length == 3)
						{	
							if(nameTypeValue[2].equals(RunCmdConstants.CREATED_BY_RUNCMD))
							{
								keysAddedByRunCMD.put(shortcut, path);
							}								
						}
					}
				}
			}		
		}
		scanner.close();
		
		return keysAddedByRunCMD;
	}
}
