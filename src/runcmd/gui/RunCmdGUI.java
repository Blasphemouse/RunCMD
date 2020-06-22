package runcmd.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import runcmd.RegistryEditor;
import runcmd.RunCmdConstants;

public class RunCmdGUI 
{	
	private static DefaultListModel<String> listModel = new DefaultListModel<String>();
	private static ManageKeyGUI manageKeyGUI;

	public static void main(String args[])
	{
		if(isRunningAsAdministrator())
		{
			buildAndShowGUI();
		}
	}
	
	private static void buildAndShowGUI()
	{
		JFrame jf = new JFrame();
		jf.setTitle("RunCMD: Custom Run Command Editor");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		runcmd.RegistryEditor.addAllKeysCreatedByRunCMD();
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		final JList<String> existingCommands = new JList<String>(listModel);
			existingCommands.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			existingCommands.setLayoutOrientation(JList.VERTICAL);
			existingCommands.setVisibleRowCount(5);
		
		JScrollPane scrollPane = new JScrollPane(existingCommands);
		
		JPanel buttonPanel = new JPanel(new GridLayout(1,3));
		
		JButton addButton = new JButton("Add");
		JButton modifyButton = new JButton("Modify Selected");
		JButton deleteButton = new JButton("Delete Selected");
		buttonPanel.add(addButton);
		buttonPanel.add(modifyButton);
		buttonPanel.add(deleteButton);
		
		addButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				manageKeyGUI.displayGUIforAddition();
			}
		});
		
		modifyButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int index = existingCommands.getSelectedIndex();
				
				if(index!=-1)
				{
					String keyShortcutDelimiterPath = (String) listModel.get(index);
					String shortcut = keyShortcutDelimiterPath.split(RunCmdConstants.KEY_SHORTCUT_PATH_UI_DELIMITER)[0];
					String path = keyShortcutDelimiterPath.split(RunCmdConstants.KEY_SHORTCUT_PATH_UI_DELIMITER)[1];
					
					manageKeyGUI.displayGUIforModification(shortcut, path);
				}
				else
				{
					if(existingCommands.getModel().getSize() == 0)
					{
						JOptionPane.showMessageDialog(jf, "There are no existing entries to modify yet.");
					}
					else
					{
						JOptionPane.showMessageDialog(jf, "Please select an existing entry to modify.");
					}
				}
			}
		});
		
		deleteButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int index = existingCommands.getSelectedIndex();
				String keyShortcutDelimiterPath = (String) listModel.get(index);
				String shortcut = keyShortcutDelimiterPath.split(RunCmdConstants.KEY_SHORTCUT_PATH_UI_DELIMITER)[0];
				RegistryEditor.deleteValue(shortcut);
			}
		});
		
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		jf.add(mainPanel);
		jf.setSize(600,250);
		jf.setLocation(100,100);
		jf.setVisible(true);
		
		manageKeyGUI = new ManageKeyGUI();
	}
	
	public static void addKey(String shortcut, String path)
	{
		String keyShortcutDelimiterPath = shortcut + RunCmdConstants.KEY_SHORTCUT_PATH_UI_DELIMITER + path;
		listModel.addElement(keyShortcutDelimiterPath);
	}
	
	public static void removeKey(String shortcut)
	{
		for(int i=0; i<listModel.size(); i++)
		{
			String listModelKeyShortcutDelimiterPath = listModel.getElementAt(i);
			String listModelShortcut = listModelKeyShortcutDelimiterPath.split(RunCmdConstants.KEY_SHORTCUT_PATH_UI_DELIMITER)[0];
			
			if(listModelShortcut.equals(shortcut))
			{
				listModel.remove(i);
			}
		}
	}
	
	private static boolean isRunningAsAdministrator()
	{
		int errorCode = RegistryEditor.validateRunAsAdministrator();
		if(errorCode!=0)
		{
			JOptionPane.showMessageDialog(null, "RunCMD is not currently running as administrator and will not be able to function properly.");
			return false;
		}
		return true;
	}
}