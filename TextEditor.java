package TextEditor;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;

public class TextEditor extends JFrame implements ActionListener
{
	JTextArea textArea;   
	JScrollPane scrollPane;
	JLabel fontLabel;
	JSpinner fontSizeSpinner;
	JButton fontColorButton;
	JComboBox fontBox;
	
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenuItem openItem;
	JMenuItem saveItem;
	JMenuItem exitItem;
	
	TextEditor()
	{
		this.setTitle("TextEditor");           // Frame Title
		this.setSize(500, 550);
		this.setLocationRelativeTo(null);  // JFrame will show up in the Middle of the Window screen
		this.setLayout(new FlowLayout());
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);           // after one line is full automatically append to next line
		textArea.setWrapStyleWord(true);     // when the word takes more space but line is full, it will automatically send the full word to the next line to be written their completely 
		textArea.setFont(new Font("Arial",Font.PLAIN,20));
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(450,450));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		fontLabel = new JLabel("Font:");
		
		fontSizeSpinner = new JSpinner(); // spinner has a integer inc++ & dec-- button 
		fontSizeSpinner.setPreferredSize(new Dimension(50,25));
		fontSizeSpinner.setValue(20);   // default value
		fontSizeSpinner.addChangeListener(new ChangeListener()  // whenever the value is inc++ or dec-- on the FontButton the text written in the textArea will correspondingly respond to that
		{
			@Override
			public void stateChanged(ChangeEvent e) 
			{                             // getting the textArea font type          //  getting the value from spinner & converting it to integer to declare new size of font
				textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int)fontSizeSpinner.getValue()));
			}	
		});
		
		fontColorButton = new JButton("Color");
		fontColorButton.setFocusable(false);
		fontColorButton.addActionListener(this);
		
		
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(); // creating a string array and storing all available font types in it
		fontBox = new JComboBox(fonts);       // & then displaying those types in a Combo box,
		fontBox.addActionListener(this);     //  when clicked on an type in the box the whole textArea font style will change 
		fontBox.setSelectedItem("Arial");   // default font style 
		
		menuBar = new JMenuBar();         // creating a space for Menu's, it saves a space above the Buttons for various Menu uses
		fileMenu = new JMenu("File");    // in the MenuBar section creating a Menu which will have items, the items will perform various operations  
		openItem = new JMenuItem("Open");
		saveItem = new JMenuItem("Save");
		exitItem = new JMenuItem("Exit");	
		openItem.addActionListener(this);
		saveItem.addActionListener(this);
		exitItem.addActionListener(this);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(exitItem);
		menuBar.add(fileMenu);

		this.setJMenuBar(menuBar);
		this.add(fontLabel);
		this.add(fontSizeSpinner);
		this.add(fontColorButton);
		this.add(fontBox);
		this.add(scrollPane);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{	
		if(e.getSource()==fontColorButton)
		{
			JColorChooser colorChooser = new JColorChooser();
			Color color = colorChooser.showDialog(null,"Color Bar",Color.black); // this will open javax predefined Color Bar
			textArea.setForeground(color);                                      // when a color is selected in the Bar it will change the text area color to it
		}
		
		if(e.getSource()==fontBox)
		{
			textArea.setFont(new Font( (String)fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize() ));
		}
		
		if(e.getSource()==openItem)
		{
			JFileChooser fileChooser = new JFileChooser();    // we use File Chooser to choose the files saved in the device or cloud
			fileChooser.setCurrentDirectory(new File("."));  // when we start the "Open" option it will firstly show the current file directory where this code is saved
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files","txt");  // using this we can add multiple file extension of file type for ease of search
			fileChooser.setFileFilter(filter); // this will set the filter option with the All Files option in OPEN
			
			int response = fileChooser.showOpenDialog(null);  // when file selected what is the response of the file i.e 0,1, & -1
			
			if(response == JFileChooser.APPROVE_OPTION)     // APPROVE_OPTION means 1
			{
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
				Scanner fileIn = null;
				
				try 
				{
					fileIn = new Scanner(file); // we scan the file and print it in the text area line by line till it has next words 
					if(file.isFile())          // checking the file selected is a readable file not a directory or image
					{
						while(fileIn.hasNext())
						{
							String line = fileIn.nextLine()+"\n";
							textArea.append(line);
						}
					}
				} 
				catch (FileNotFoundException e1) 
				{
					e1.printStackTrace();
				}
				finally
				{
					fileIn.close();  // no matter what happens(success, error or something) in the end it will close the file permanently
				}
			}		
		}
		
		if(e.getSource()==saveItem)
		{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("."));
			
			int response = fileChooser.showSaveDialog(null);
			
			if(response == JFileChooser.APPROVE_OPTION)
			{
				File file;
				PrintWriter fileOut = null;  // PrintWriter will print the text in the text field(NotePad in the case of pc) of the respective device  
				
				file = new File(fileChooser.getSelectedFile().getAbsolutePath());  // we will save it at the chosen location
				try 
				{
					fileOut = new PrintWriter(file);
					fileOut.println(textArea.getText());
				} 
				catch (FileNotFoundException e1) 
				{
					e1.printStackTrace();
				}
				finally
				{
					fileOut.close();
				}
			}
		}
		
		if(e.getSource()==exitItem)
		{
			System.exit(0);
		}	
	}

}
