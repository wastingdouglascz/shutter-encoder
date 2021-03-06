/*******************************************************************************************
* Copyright (C) 2020 PACIFICO PAUL
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License along
* with this program; if not, write to the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
* 
********************************************************************************************/

package application;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;

import library.DECKLINK;

public class BlackMagicInput {

	public static JFrame frame; 
	public static JButton btnRecord;
	public static JRadioButton caseStopAt;
	public static JLabel lblTimecode;
	public static JComboBox<String[]> comboInput;
	private JComboBox<String[]> comboOutput;
	protected static JTextField lblDestination;
	private JRadioButton caseDeinterlace;
	public static JTextField TC1;
	public static JTextField TC2;
	public static JTextField TC3;
	
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public BlackMagicInput() {		
		frame = new JFrame();
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		if (System.getProperty("os.name").contains("Windows"))
			frame.setSize(473, 214);
		else
			frame.setSize(453, 194);

		frame.setTitle(Shutter.language.getProperty("frameBlackMagicInput"));
		frame.setForeground(Color.WHITE);
		frame.getContentPane().setBackground(new Color(50,50,50));
		frame.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("contents/icon.png")).getImage());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
		frame.setLocation(Shutter.frame.getLocation().x - frame.getWidth() - 20, Shutter.frame.getLocation().y + Shutter.frame.getHeight() / 2 - 40);
				
		frame.addWindowListener(new WindowAdapter(){

			public void windowClosing(WindowEvent arg0) {
				if (DECKLINK.isRunning)
				{
					try {
						DECKLINK.writer.write('q');
						DECKLINK.writer.flush();
						DECKLINK.writer.close();
					} catch (IOException er) {}
				}
				
				if (DECKLINK.isRunning)
					DECKLINK.process.destroy();				

			}
			
		});
		
		load();
		
		frame.setVisible(true);
				
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void load(){
		
		frame.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent arg0) {
				if (DECKLINK.isRunning)
					DECKLINK.process.destroy();				
			}			
			
		});
		
		btnRecord = new JButton(Shutter.language.getProperty("btnRecord"));
		btnRecord.setFont(new Font("Montserrat", Font.PLAIN, 12));
		btnRecord.setBounds(12, 97, 423, 21);
		frame.getContentPane().add(btnRecord);
		
		lblTimecode = new JLabel("00:00:00");
		lblTimecode.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimecode.setFont(new Font("FreeSans", Font.PLAIN, 30));
		lblTimecode.setForeground(Color.RED);
		lblTimecode.setBounds(10, 133, 422, 24);
		frame.getContentPane().add(lblTimecode);
		
		Label lblInput = new Label(Shutter.language.getProperty("lblInput"));
		lblInput.setAlignment(Label.RIGHT);
		lblInput.setFont(new Font("FreeSans", Font.PLAIN, 12));
		lblInput.setForeground(Color.WHITE);
		lblInput.setBackground(new Color(50,50,50));
		lblInput.setBounds(10, 9, 46, 22);
		frame.getContentPane().add(lblInput);
		
		comboInput = new JComboBox<String[]>();
		comboInput.setBounds(62, 9, 374, 22);	
		comboInput.setModel(new DefaultComboBoxModel(DECKLINK.formatsList.toArray()));
		comboInput.setSelectedItem(null);
		comboInput.setFont(new Font("FreeSans", Font.PLAIN, 11));
		comboInput.setEditable(false);
		frame.getContentPane().add(comboInput);
		
		Label lblOutput = new Label(Shutter.language.getProperty("lblOutput"));
		lblOutput.setAlignment(Label.RIGHT);
		lblOutput.setFont(new Font("FreeSans", Font.PLAIN, 12));
		lblOutput.setForeground(Color.WHITE);
		lblOutput.setBackground(new Color(50, 50, 50));
		lblOutput.setBounds(10, 37, 46, 22);
		frame.getContentPane().add(lblOutput);
		
		comboOutput = new JComboBox<String[]>();
		comboOutput.setFont(new Font("FreeSans", Font.PLAIN, 11));
		comboOutput.setEditable(false);
		comboOutput.setBounds(62, 37, 130, 22);
		frame.getContentPane().add(comboOutput);
		
		final String codecs[] = {"DV PAL 4/3", "DV PAL 16/9", "DNxHD 120", "DNxHD 185", "Apple ProRes 422", "Apple ProRes 422 HQ"};
	
		comboOutput.setModel(new DefaultComboBoxModel(codecs));
		comboOutput.setSelectedIndex(2);
		
		comboOutput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (comboOutput.getSelectedItem().toString().contains("DV PAL") || comboInput.getSelectedItem().toString().contains("interlaced") == false)
				{
					caseDeinterlace.setSelected(false);
					caseDeinterlace.setEnabled(false);
				}
				else
					caseDeinterlace.setEnabled(true);
			}
			
		});
		
		lblDestination = new JTextField();
		lblDestination.setEditable(false);
	  	lblDestination.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		lblDestination.setForeground(new Color(71,163,236));
		lblDestination.setFont(new Font("SansSerif", Font.BOLD, 13));
		lblDestination.setBackground(new Color(50,50,50));
		if (System.getProperty("os.name").contains("Windows"))
			lblDestination.setText(System.getProperty("user.home") + "\\Desktop");
		else
			lblDestination.setText(System.getProperty("user.home") + "/Desktop");
			
		lblDestination.setBounds(198, 37, 238, 22);
		frame.getContentPane().add(lblDestination);
		
		//Drag & Drop
		lblDestination.setTransferHandler(new RecordDestinationFileTransferHandler());   	
				
		comboInput.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
	            DECKLINK.toFFPLAY("-f decklink -draw_bars 0 -i " + '"' + DECKLINK.getBlackMagic + "@" + (comboInput.getSelectedIndex() + 1) + '"' + " -c:a copy -c:v copy -f matroska pipe:play |");				
			}

        });
				
		btnRecord.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (btnRecord.getText().equals(Shutter.language.getProperty("btnRecord")))
				{
					record();
					comboInput.setEnabled(false);
					comboOutput.setEnabled(false);
					caseDeinterlace.setEnabled(false);
					caseStopAt.setEnabled(false);
					TC1.setEnabled(false);
					TC2.setEnabled(false);
					TC3.setEnabled(false);
					btnRecord.setText(Shutter.language.getProperty("btnStopRecording"));
				}
				else if (btnRecord.getText().equals(Shutter.language.getProperty("btnStopRecording")))
				{															
					try {
						DECKLINK.writer.write('q');
						DECKLINK.writer.flush();
						DECKLINK.writer.close();
					} catch (IOException er) {}
					
					do {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {}
					} while (DECKLINK.process.isAlive());
										
					comboInput.setEnabled(true);
					comboOutput.setEnabled(true);
					caseDeinterlace.setEnabled(true);
					caseStopAt.setEnabled(true);
					TC1.setEnabled(true);
					TC2.setEnabled(true);
					TC3.setEnabled(true);
					btnRecord.setText(Shutter.language.getProperty("btnRecord"));
					
					DECKLINK.toFFPLAY("-f decklink -draw_bars 0 -i " + '"' + DECKLINK.getBlackMagic + "@" + (comboInput.getSelectedIndex() + 1) + '"' + " -c:a copy -c:v copy -f matroska pipe:play |");
				}
			}
					
		});
			
		caseDeinterlace = new JRadioButton(Shutter.language.getProperty("caseDeinterlace"));
		caseDeinterlace.setFont(new Font("FreeSans", Font.PLAIN, 12));
		caseDeinterlace.setBounds(10, 65, 157, 23);
		frame.getContentPane().add(caseDeinterlace);
		
		caseStopAt = new JRadioButton(Shutter.language.getProperty("caseStopAt"));
		caseStopAt.setFont(new Font("FreeSans", Font.PLAIN, 12));
		caseStopAt.setBounds(169, 65, 169, 23);
		frame.getContentPane().add(caseStopAt);
		
		caseStopAt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (caseStopAt.isSelected())
				{
					TC1.setEnabled(true);
					TC2.setEnabled(true);
					TC3.setEnabled(true);
				}
				else
				{
					TC1.setEnabled(false);
					TC2.setEnabled(false);
					TC3.setEnabled(false);
				}
			}
			
		});
		
		TC1 = new JTextField("00");
		TC1.setBounds(336, 66, 32, 21);
		TC1.setHorizontalAlignment(SwingConstants.CENTER);
		TC1.setFont(new Font("FreeSans", Font.PLAIN, 14));
		TC1.setColumns(10);
		TC1.setEnabled(false);
		frame.getContentPane().add(TC1);
				
		TC2 = new JTextField("00");
		TC2.setBounds(370, 66, 32, 21);
		TC2.setHorizontalAlignment(SwingConstants.CENTER);
		TC2.setFont(new Font("FreeSans", Font.PLAIN, 14));
		TC2.setColumns(10);
		TC2.setEnabled(false);
		frame.getContentPane().add(TC2);
		
		TC3 = new JTextField("00");
		TC3.setBounds(404, 66, 32, 21);
		TC3.setHorizontalAlignment(SwingConstants.CENTER);
		TC3.setFont(new Font("FreeSans", Font.PLAIN, 14));
		TC3.setColumns(10);
		TC3.setEnabled(false);
		frame.getContentPane().add(TC3);
				
		TC1.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e) {		
				char caracter = e.getKeyChar();											
				if (String.valueOf(caracter).matches("[0-9]+") == false && caracter != '￿' || String.valueOf(caracter).matches("[éèçàù]"))
		            e.consume(); 
				else if (TC1.getText().length() >= 2)					
					TC1.setText("");
					
			}

			@Override
			public void keyPressed(KeyEvent e) {					 
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}	
		});

		TC2.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e) {		
				char caracter = e.getKeyChar();											
				if (String.valueOf(caracter).matches("[0-9]+") == false && caracter != '￿' || String.valueOf(caracter).matches("[éèçàù]"))
		            e.consume();
				else if (TC2.getText().length() >= 2)					
					TC2.setText("");
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}	
		});
		
		TC3.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e) {		
				char caracter = e.getKeyChar();											
				if (String.valueOf(caracter).matches("[0-9]+") == false && caracter != '￿' || String.valueOf(caracter).matches("[éèçàù]"))
		            e.consume(); 
				else if (TC3.getText().length() >= 2)					
					TC3.setText("");
			}

			@Override
			public void keyPressed(KeyEvent e) { 
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}	
		});

	}
	
	private void record() {
		
		String interlaced = "";
		if (comboInput.getSelectedItem().toString().contains("(interlaced, upper field first)") && caseDeinterlace.isSelected() == false)
		{
			if (comboOutput.getSelectedItem().toString().contains("Apple ProRes"))
				interlaced = " -field_order tt";
			else if (comboOutput.getSelectedItem().toString().contains("DNxHD"))
				interlaced = " -flags +ildct -top 1";
			else if (comboOutput.getSelectedItem().toString().contains("DV PAL"))
				interlaced = " -field_order tb";
		}
		else if (comboInput.getSelectedItem().toString().contains("(interlaced, lower field first)")  && caseDeinterlace.isSelected() == false)
		{
			if (comboOutput.getSelectedItem().toString().contains("Apple ProRes"))
				interlaced = " -field_order bt";
			else if (comboOutput.getSelectedItem().toString().contains("DNxHD"))
				interlaced = " -field_order bt -flags +ildct -top 1";
		}	
		
		String deinterlace = "";
		if (caseDeinterlace.isSelected())
		{			
			if (comboInput.getSelectedItem().toString().contains("(interlaced, upper field first)"))
				deinterlace = "yadif=0:1:0";
			else if (comboInput.getSelectedItem().toString().contains("(interlaced, lower field first)"))
				deinterlace = "yadif=0:0:0";
			
			if (comboOutput.getSelectedItem().toString().contains("DNxHD"))
				deinterlace = "," + deinterlace;
			else
				deinterlace = " -filter:v " + deinterlace;
		}
				
		boolean cancelled = false;
		
		//Si le fichier existe
		File fileOut = new File(lblDestination.getText() + "/Num_" + comboOutput.getSelectedItem().toString().replace(" 4/3", "").replace(" 16/9", "").replace(" ","_") + ".mov");
		if(fileOut.exists())
		{						
			fileOut = Utils.fileReplacement(lblDestination.getText(), "Num.mov", ".mov", "_" + comboOutput.getSelectedItem().toString().replace(" 4/3", "").replace(" 16/9", "").replace(" ","_") + "_", ".mov");
			if (fileOut == null)
			{
				cancelled = true;
				comboInput.setEnabled(true);
				comboOutput.setEnabled(true);
				caseDeinterlace.setEnabled(true);
				caseStopAt.setEnabled(true);
				TC1.setEnabled(true);
				TC2.setEnabled(true);
				TC3.setEnabled(true);
				btnRecord.setText(Shutter.language.getProperty("btnRecord"));
			}
		}
		
		if (cancelled == false)
		{
			String decklink = "-f decklink -draw_bars 0 -i " + '"' + DECKLINK.getBlackMagic + "@" + (comboInput.getSelectedIndex() + 1) + '"';
			String output = "-f tee " + '"' + fileOut.toString().replace("\\", "/") + "|[f=matroska]pipe:play" + '"';
			
			switch (comboOutput.getSelectedItem().toString())
			{
				case "DV PAL 4/3":
					DECKLINK.toFFMPEG(decklink + " -aspect 4:3 -s 720x576 -c:a copy -c:v dvvideo -b:v 25000 -r 25" + interlaced + " -map v? -map a? -y " + output);
					break;
				case "DV PAL 16/9":
					DECKLINK.toFFMPEG(decklink + " -aspect 16:9 -s 720x576 -c:a copy -c:v dvvideo -b:v 25000 -r 25" + interlaced + " -map v? -map a? -y " + output);
					break;
				case "DNxHD 120":
					DECKLINK.toFFMPEG(decklink + " -s 1920x1080 -filter:v scale=1920:1080:force_original_aspect_ratio=decrease,pad=" + '"' + "1920:1080:(ow-iw)/2:(oh-ih)/2" + '"' + deinterlace + " -c:a copy -c:v dnxhd -b:v 120M -pix_fmt yuv422p" + interlaced + " -map v? -map a? -y " + output);
					break;
				case "DNxHD 185":
					DECKLINK.toFFMPEG(decklink + " -s 1920x1080 -filter:v scale=1920:1080:force_original_aspect_ratio=decrease,pad=" + '"' + "1920:1080:(ow-iw)/2:(oh-ih)/2" + '"'  + deinterlace   + " -c:a copy -c:v dnxhd -b:v 185M -pix_fmt yuv422p" + interlaced + " -map v? -map a? -y " + output);
					break;
				case "Apple ProRes 422":
					DECKLINK.toFFMPEG(decklink + " -c:a copy -c:v prores -profile:v 2" + deinterlace + interlaced + " -pix_fmt yuv422p10 -map v? -map a? -y " + output);
					break;
				case "Apple ProRes 422 HQ":
					DECKLINK.toFFMPEG(decklink + " -c:a copy -c:v prores -profile:v 3" + deinterlace + interlaced + " -pix_fmt yuv422p10 -map v? -map a? -y " + output);
					break;	
			}
		}
	}
}

//Drag & Drop lblDestination
@SuppressWarnings("serial")
class RecordDestinationFileTransferHandler extends TransferHandler {
	
public boolean canImport(JComponent arg0, DataFlavor[] arg1) {
  for (int i = 0; i < arg1.length; i++) {
    DataFlavor flavor = arg1[i];
    if (flavor.equals(DataFlavor.javaFileListFlavor)) {
  	  	BlackMagicInput.lblDestination.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
      return true;
    }
  }
  return false;
}

public boolean importData(JComponent comp, Transferable t) {
  DataFlavor[] flavors = t.getTransferDataFlavors();
  for (int i = 0; i < flavors.length; i++) {
    DataFlavor flavor = flavors[i];
    try {
      if (flavor.equals(DataFlavor.javaFileListFlavor)) {
        List<?> l = (List<?>) t.getTransferData(DataFlavor.javaFileListFlavor);
        Iterator<?> iter = l.iterator();
        while (iter.hasNext()) {
          File file = (File) iter.next();
          
          if (file.getName().contains("."))
        	  BlackMagicInput.lblDestination.setText(file.getParent());
          else
          {
        	  BlackMagicInput.lblDestination.setText(file.getAbsolutePath());
          }      
        }
 		
 		//Border
        BlackMagicInput.lblDestination.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 0));
		
        return true;
      }        
    } catch (IOException | UnsupportedFlavorException ex) {}
  }
  return false;
}  
}
