/*
 * Copyright (C) 2020 Key Parker from K.I.C.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package pppmain;

import ppplib.VcConst;
import ppplib.ScConst;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import ppplib.ScTable;
import pppmodel.McCard;
import pppmodel.McVocabularyModel;

public class MainFrame {

  private MainFrame(){}//++!
  
  //=== component ** origin
  
  public static final BufferedImage O_ICON
   = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
  
  public static final JFrame O_FRAME
   = new JFrame("Flashcard app v0.1.0");
  
  public static final JTextArea O_AREA
   = new JTextArea(">>>"+VcConst.C_V_NEWLINE);
  
  public static final JTextField O_FIELD
   = new JTextField("");
  
  //=== component ** applicant
  
  public static final JTextField O_KEY_ENG
   = new JTextField();
  
  public static final JTextField O_VAL_JPN
   = new JTextField();
  
  public static final ScTable O_TABLE
   = new ScTable(pppmodel.McVocabularyModel.ccGetInstance(), -1, -1);
  
  public static final JTextField O_STATUS_BAR
   = new JTextField("::");
  
  //=== init
  
  public static final void ccSetupIcon(BufferedImage pxImage){
    if(pxImage==null){return;}
    if(pxImage.getWidth()!=32){return;}
    if(pxImage.getHeight()!=32){return;}
    for(int x=0;x<32;x++){for(int y=0;y<32;y++){
      pxImage.setRGB(x, y, 0xFF339933);
      if(x>y){pxImage.setRGB(x, y, 0xFFEEEEEE);}
      if(
        (x<=2 || x>=29)||
        (y<=2 || y>=29)
      ){pxImage.setRGB(x, y, 0xFF111111);}
    }}//..~
  }//+++
  
  private static final Runnable O_SWING_SETUP = new Runnable(){ 
    @Override public void run() {
      
      //-- menu ** item ** origin
      JMenuItem lpInfoItem = ScConst
        .ccCreateMenuItem("Info", "--action-info", KeyEvent.VK_I);
      JMenuItem lpQuitItem = ScConst
        .ccCreateMenuItem("Quit", "--action-quit", KeyEvent.VK_Q);
      
      //[head]::lpClearItem
      
      //-- menu ** item ** applicant
      JMenuItem lpImportItem = ScConst
        .ccCreateMenuItem("Import", "--action-import", KeyEvent.VK_O);
      JMenuItem lpExportItem = ScConst
        .ccCreateMenuItem("Export", "--action-export", KeyEvent.VK_T);
      JMenuItem lpClearItem = ScConst
        .ccCreateMenuItem("Clear", "--action-clear", KeyEvent.VK_C);
      JMenuItem lpRefreshItem = ScConst
        .ccCreateMenuItem("Refresh", "--action-refresh", KeyEvent.VK_H);
      JMenuItem lpDeleteItem = ScConst
        .ccCreateMenuItem("Delete", "--action-delete", KeyEvent.VK_L);
      JMenuItem lpDiscardItem = ScConst
        .ccCreateMenuItem("Discard", "--action-discard", KeyEvent.VK_D);
      
      //-- menu ** bar
      JMenu lpFileMenu=new JMenu("File");
      lpFileMenu.setMnemonic(KeyEvent.VK_F);
      lpFileMenu.add(lpImportItem);
      lpFileMenu.add(lpExportItem);
      lpFileMenu.add(new JSeparator());
      lpFileMenu.add(lpClearItem);
      lpFileMenu.add(new JSeparator());
      lpFileMenu.add(lpQuitItem);
      
      JMenu lpEditMenu = new JMenu("Edit");
      lpEditMenu.setMnemonic(KeyEvent.VK_E);
      lpEditMenu.add(lpRefreshItem);
      lpEditMenu.add(lpDeleteItem);
      lpEditMenu.add(lpDiscardItem);
      
      JMenu lpHelpMenu = new JMenu("Help");
      lpHelpMenu.setMnemonic(KeyEvent.VK_H);
      lpHelpMenu.add(lpInfoItem);
      
      JMenuBar lpMenuBar = new JMenuBar();
      lpMenuBar.add(lpFileMenu);
      lpMenuBar.add(lpEditMenu);
      lpMenuBar.add(lpHelpMenu);
      
      //-- shell ** ui ** origin
      O_AREA.setBackground(Color.LIGHT_GRAY);
      O_AREA.setDisabledTextColor(Color.DARK_GRAY);
      O_AREA.setEditable(false);
      O_AREA.setEnabled(false);
      JScrollPane lpAreaPane=new JScrollPane(O_AREA);
      O_FIELD.addKeyListener(MainActionManager.O_KEY_LISTENER);
      
      //-- shell ui ** applicant
      JButton lpEncounterButton = ScConst.ccCreateCommandButton(
        "Encounter", "--action-encounter", KeyEvent.VK_R
      );
      JButton lpFlashButton = ScConst.ccCreateCommandButton(
        "Flash", "--action-flash", KeyEvent.VK_A
      );
      JButton lpCashButton = ScConst.ccCreateCommandButton(
        "Cash", "--action-cash", KeyEvent.VK_C
      );
      JPanel lpOperatePane = new JPanel(new GridLayout(12, 1));
      lpOperatePane.add(new JLabel("Engish Vocabulary:"));
      lpOperatePane.add(O_KEY_ENG);
      lpOperatePane.add(new JLabel("Japanese Definition:"));
      lpOperatePane.add(O_VAL_JPN);
      lpOperatePane.add(lpEncounterButton);
      lpOperatePane.add(lpCashButton);
      lpOperatePane.add(new JSeparator(SwingConstants.HORIZONTAL));
      lpOperatePane.add(lpFlashButton);
      ScConst.ccSetupStatusBar(O_STATUS_BAR);
      
      //-- shell ui ** table
      O_TABLE.ccSetColumnWidth(0, 200);
      O_TABLE.ccSetColumnWidth(1, 200);
      O_TABLE.ccAddMouseListener(MainActionManager.O_TABLE_LISTENER);
      
      //-- shell ** console pane
      JPanel lpConsolePane = new JPanel(new BorderLayout());
      lpConsolePane.setBorder(BorderFactory.createEtchedBorder());
      lpConsolePane.add(lpAreaPane,BorderLayout.CENTER);
      lpConsolePane.add(O_FIELD,BorderLayout.PAGE_END);
      
      //-- shell ** apps pane
      JPanel lpApplicantPane = new JPanel(new BorderLayout());
      lpApplicantPane.add(lpOperatePane, BorderLayout.LINE_START);
      lpApplicantPane.add(O_TABLE, BorderLayout.CENTER);
      lpApplicantPane.add(O_STATUS_BAR, BorderLayout.PAGE_END);
      
      //-- shell ** tab
      JTabbedPane lpContentPane = new JTabbedPane();
      lpContentPane.add("Card",lpApplicantPane);
      lpContentPane.add("Console",lpConsolePane);
      
      //-- frame ** setup
      O_FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      O_FRAME.setJMenuBar(lpMenuBar);
      O_FRAME.setContentPane(lpContentPane);
      
      //-- frame ** icon
      ccSetupIcon(O_ICON);
      O_FRAME.setIconImage(O_ICON);
      
      //-- frame ** packup
      Point lpOrigin=ScConst.ccGetScreenInitPoint();
      Dimension lpScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension lpWindowSize = new Dimension(640, 480);
      O_FRAME.setLocation(
        lpOrigin.x+lpScreenSize.width/2-lpWindowSize.width/2,
        lpOrigin.y+lpScreenSize.height/2-lpWindowSize.height/2
      );
      O_FRAME.setPreferredSize(lpWindowSize);
      O_FRAME.setResizable(false);
      O_FRAME.pack();
      O_FRAME.setVisible(true);
      
      //-- bind ** clicked
      MainActionManager.ccRegisterAction
        (lpQuitItem, MainActionManager.O_QUITTING);
      MainActionManager.ccRegisterAction
        (lpInfoItem, MainActionManager.O_DISCLAIMING);
      MainActionManager.ccRegisterAction
        (lpEncounterButton, MainActionManager.O_ENCOUNTERING);
      MainActionManager.ccRegisterAction
        (lpCashButton, MainActionManager.O_REFOCUSING);
      MainActionManager.ccRegisterAction
        (lpFlashButton, MainActionManager.O_FLASHING);
      MainActionManager.ccRegisterAction
        (lpRefreshItem, MainActionManager.O_REFRESHING);
      MainActionManager.ccRegisterAction
        (lpDeleteItem, MainActionManager.O_DELETING);
      MainActionManager.ccRegisterAction
        (lpDiscardItem, MainActionManager.O_DISCARDING);
      MainActionManager.ccRegisterAction
        (lpImportItem, MainActionManager.O_IMPORTING);
      MainActionManager.ccRegisterAction
        (lpExportItem, MainActionManager.O_EXPORTING);
      MainActionManager.ccRegisterAction
        (lpClearItem, MainActionManager.O_CLEARING);
      
      //-- bind ** typed
      MainActionManager.ccRegisterAction
        ("quit", MainActionManager.O_QUITTING);
      
      //-- bind ** shortcut
      lpImportItem.setAccelerator
        (KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
      lpExportItem.setAccelerator
        (KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
      lpDeleteItem.setAccelerator
        (KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
      lpRefreshItem.setAccelerator(KeyStroke.getKeyStroke("F5"));
      
      
      //-- post
      McVocabularyModel.ccGetInstance().ccInit();
      
      ccWriteln("[sys]on", VcConst.C_V_OS);
      ccWriteln("[sys]from", VcConst.C_V_PWD);
      ccWriteln("[*** bon appetit ***]");

    }//+++
  };//***
  
  //=== util
  
  public static final void ccWriteln(String pxLine){
    ccWriteln(pxLine, null);
  }//+++
  
  public static final void ccWriteln(String pxTag, Object pxVal){
    if(pxTag==null){return;}
    if(pxVal==null){
      O_AREA.append(pxTag+VcConst.C_V_NEWLINE);
    }else{
      O_AREA.append(pxTag+":"+pxVal.toString()+VcConst.C_V_NEWLINE);
    }//..?
    int lpLength = O_AREA.getText().length();
    O_AREA.setSelectionStart(lpLength-1);
    O_AREA.setSelectionEnd(lpLength);
  }//+++
  
  public static final
  void ccErrorBox(String pxMessage){
    if(!ScConst.ccIsEDT()){return;}
    JOptionPane.showMessageDialog(
      O_FRAME,
      pxMessage,
      O_FRAME==null?"<!>":O_FRAME.getTitle(),
      JOptionPane.ERROR_MESSAGE
    );
  }//+++
  
  public static final void ccMessageBox(String pxMessage){
    if(SwingUtilities.isEventDispatchThread() && (O_FRAME!=null)){
      JOptionPane.showMessageDialog(O_FRAME,pxMessage);
    }else{System.err.println(".ccMessageBox()::"+pxMessage);}
  }//+++
  
  public static final boolean ccYesOrNoBox(String pxMessage){
    if(SwingUtilities.isEventDispatchThread() && (O_FRAME!=null)){
      int lpRes=JOptionPane.showConfirmDialog(
        O_FRAME,
        pxMessage,
        O_FRAME!=null?O_FRAME.getTitle():"<!>",
        JOptionPane.YES_NO_OPTION
      );
      return lpRes==0;
    }else{
      System.err.println(".ccMessageBox()::"+pxMessage);
      return false;
    }//..?
  }//+++
  
  public static final boolean ccFlashBox(McCard pxCard, List<String> pxList){
    
    //-- deref list
    boolean lpValid=false;
    if(pxList==null){
      lpValid=false;
    }else{
      lpValid=pxList.size()==4;
    }//..?
    Object[] lpDesAnswer = null;
    if(lpValid){
      lpDesAnswer = pxList.toArray();
    }else{
      lpDesAnswer = new Object[]{"?1", "?2", "?3", "?4"};
    }//..?
    
    //-- deref card
    String lpWord = "?word";
    String lpDef = "?Def";
    if(pxCard!=null){
      lpWord=pxCard.ccGetEnlish();
      lpDef=pxCard.ccGetDefinition();
    }//..?
    
    //-- go ask him
    String lpAnswer = (String)JOptionPane.showInputDialog(
      O_FRAME,lpWord,"FLASH!!",
      JOptionPane.PLAIN_MESSAGE,null,
      lpDesAnswer,"alas"
    );
    return lpDef.equals(lpAnswer);
    
  }//+++
  
  //=== entry
  
  public static final JFrame ccGetFrame(){return O_FRAME;}//+++

  public static void main(String[] args) {
    System.out.println("MainFrame.main()::enter");
    ScConst.ccApplyLookAndFeel(4, false);
    SwingUtilities.invokeLater(O_SWING_SETUP);
    System.out.println("MainFrame.main()::exit");
  }//!!!

}//***eof
