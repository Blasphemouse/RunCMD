package runcmd.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import runcmd.RegistryEditor;

/**
 * Allows the user to add or modify a registry key.
 */
public class ManageKeyGUI 
{
	private static JFrame jf = null;
	private static JPanel mainPanel = null;
	private static JPanel topPanel = null;
	private static JPanel secondTopPanel = null;
	private static JPanel midPanel = null;
	private static JPanel botPanel = null;
	
	private static JLabel shortcut = null;
	private static JTextField shortcutTF = null;
	private static JLabel path = null;
	private static JTextField pathTF = null;
	
	private static String originalShortcutVal = null;
	private static String originalPathVal = null;
	private static boolean modificationRequested = false;
	
	public ManageKeyGUI()
	{
		loadGUI();
	}
	
	private void loadGUI()
	{
		jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		jf.setTitle("Add/Modify Registry Key");
		
		mainPanel = new JPanel();
		GridLayout gl = new GridLayout(4, 1);
		mainPanel.setLayout(gl);
		
		topPanel = new JPanel();

		shortcut = new JLabel("Shortcut");
		shortcutTF = new JTextField();
			shortcutTF.setColumns(50);
			shortcutTF.setEditable(true);
			shortcutTF.setMaximumSize(new Dimension(50,10));
			shortcutTF.setText(originalShortcutVal);
		
		secondTopPanel = new JPanel();
		
		path = new JLabel("Path");
		pathTF = new JTextField();
			pathTF.setColumns(50);
			pathTF.setEditable(true);
			pathTF.setMaximumSize(new Dimension(50,10));
			pathTF.setText(originalPathVal);
		
		topPanel.add(shortcut);
		topPanel.add(shortcutTF);
		secondTopPanel.add(path);
		secondTopPanel.add(pathTF);		
		
		midPanel = new JPanel();
		JButton jb = new JButton("Browse");
		jb.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser();
				midPanel.add(jfc);
				
				int answer = jfc.showOpenDialog(jf);
				if (answer == JFileChooser.APPROVE_OPTION)
				{
				    File file = jfc.getSelectedFile();
				    pathTF.setText(file.getAbsolutePath());
				}
			}
		});
		midPanel.add(jb);
		
		
		botPanel = new JPanel();
		GridLayout glBot = new GridLayout(1, 2);
		botPanel.setLayout(glBot);
		
		JButton saveBtn = new JButton("Save");
		JButton cancelBtn = new JButton("Cancel");
		
		saveBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String newShortcut = shortcutTF.getText();
				String newPath = pathTF.getText();
				
				String validationMessages = validateShortcut(newShortcut);
				if(validationMessages==null || validationMessages.equals(""))
				{
					if(modificationRequested)
					{
						RegistryEditor.deleteValue(originalShortcutVal);	
					}
					
					RegistryEditor.addValue(newShortcut, newPath);
					
					jf.setVisible(false);
				}
				else
				{
					JOptionPane.showMessageDialog(jf, validationMessages); 
				}
			}
		});
		
		cancelBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				jf.setVisible(false);
			}
		});	
		
		botPanel.add(saveBtn);
		botPanel.add(cancelBtn);
		
		mainPanel.add(topPanel);
		mainPanel.add(secondTopPanel);
		mainPanel.add(midPanel);
		mainPanel.add(botPanel);
		
		jf.add(mainPanel);
		
		jf.setLocation(400,250);
		jf.setVisible(false);
		jf.setSize(700,250);
	}
	
	private String validateShortcut(String newShortcut)
	{
		StringBuilder sb = new StringBuilder();
		if(!newShortcut.contains("."))
		{
			sb.append("The shortcut must contain an extension when being added. \nFor example: chromeButLonger.exe\nNot necessarily an \".exe\" but broadly executable/runnable.");
		}
		
		return sb.toString();
	}
	
	public void displayGUIforAddition()
	{
		originalShortcutVal = "";
		originalPathVal = "";
		shortcutTF.setText(originalShortcutVal);
		pathTF.setText(originalPathVal);
		
		modificationRequested = false;
		
		jf.setVisible(true);
	}
	
	public void displayGUIforModification(String shortcutVal, String pathVal)
	{
		originalShortcutVal = shortcutVal;
		originalPathVal = pathVal;
		shortcutTF.setText(originalShortcutVal);
		pathTF.setText(originalPathVal);
		
		modificationRequested = true;
		
		jf.setVisible(true);
	}
}