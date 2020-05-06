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
package ppplib;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.io.File;
import javax.swing.BoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import pppmain.MainFrame;

public final class ScConst {
  
  private static final JFileChooser O_FILE_CHOOSER
    = new JFileChooser(VcConst.C_V_PWD);
  
  private static String cmFileChooserButtonText = "OK";
  
  //===
  
  public static final JMenuItem ccCreateMenuItem(
    String pxText, String pxCommand, int  pxVirtualKey
  ){
    JMenuItem lpRes = new JMenuItem(pxText==null?"<?>":pxText);
    lpRes.setActionCommand(pxCommand==null?"--action-undefined":pxCommand);
    lpRes.setMnemonic(pxVirtualKey);
    return lpRes;
  }//+++
  
  public static final JButton ccCreateCommandButton(
    String pxText ,String pxCommand, int pxVirtualKey
  ){
    JButton lpRes = new JButton(pxText==null?"<?>":pxText);
    lpRes.setActionCommand(pxCommand==null?"--action-undefined":pxCommand);
    lpRes.setMnemonic(pxVirtualKey);
    return lpRes;
  }//+++
  
  public static final void ccSetupStatusBar(JTextField pxTarget){
    if(pxTarget==null){return;}  
    pxTarget.setEditable(false);
    pxTarget.setEnabled(false);
    pxTarget.setDisabledTextColor(Color.DARK_GRAY);
  }//+++
  
  //===
  
  public static final
  void ccSetFileChooserCurrentDirectoryLocation(String pxLocation){
    if(!VcConst.ccIsValidString(pxLocation)){return;}
    File lpFile = new File(pxLocation);
    if(!lpFile.isDirectory()){return;}
    O_FILE_CHOOSER.setCurrentDirectory(lpFile);
  }//++<

  public static final
  void ccSetFileChooserSelectedFile(String pxLocation){
    if(!VcConst.ccIsValidString(pxLocation)){return;}
    File lpFile = new File(pxLocation);
    if(!lpFile.isAbsolute()){return;}
    O_FILE_CHOOSER.setSelectedFile(lpFile);
  }//++<
  
  public static final
  void ccSetFileChooserType(char pxType){
    switch(pxType){
      
      case 'i':
        O_FILE_CHOOSER.setDialogType(JFileChooser.OPEN_DIALOG);
        cmFileChooserButtonText="Import";
      break;
      
      case 'e':
        O_FILE_CHOOSER.setDialogType(JFileChooser.SAVE_DIALOG);
        cmFileChooserButtonText="Export";
      break;
      
      default:break;
      
    }//..?
  }//+++
  
  public static final File ccGetFileByFileChooser(char pxMode){
    
    //-- pre
    if(!ccIsEDT()){return null;}
    
    //-- apply
    int lpMode=JFileChooser.FILES_AND_DIRECTORIES;
    switch(pxMode){
      case 'f':lpMode=JFileChooser.FILES_ONLY;break;
      case 'd':lpMode=JFileChooser.DIRECTORIES_ONLY;break;
      default:break;
    }//..?
    
    //-- show
    O_FILE_CHOOSER.updateUI();
    O_FILE_CHOOSER.setFileSelectionMode(lpMode);
    int lpFlag=O_FILE_CHOOSER
      .showDialog(MainFrame.O_FRAME, cmFileChooserButtonText);
    if(lpFlag==JFileChooser.APPROVE_OPTION){
      File lpFile=O_FILE_CHOOSER.getSelectedFile();
      return lpFile;
    }else{return null;}
    
  }//+++
  
  public static final boolean ccIsEDT(){
    if(SwingUtilities.isEventDispatchThread()){return true;}
    System.err.println("kosui.pppswing.ScConst.ccIsEDT()::BLOCK!!");
    return false;
  }//+++
  
  public static final void ccScrollToLast(JScrollPane pxTarget){
    if(!ccIsEDT()){return;}
    if(pxTarget==null){return;}
    pxTarget.validate();
    BoundedRangeModel lpModel = pxTarget.getVerticalScrollBar().getModel();
    if(lpModel==null){return;}
    lpModel.setValue(lpModel.getMaximum()-lpModel.getExtent());
  }//+++
  
  public static final Point ccGetScreenInitPoint() {
    Point lpDummyPoint = null;
    Point lpInitPoint = null;
    for (GraphicsDevice lpDevice
      : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
      if (lpDummyPoint == null) {
        lpDummyPoint
          = lpDevice.getDefaultConfiguration().getBounds().getLocation();
      } else if (lpInitPoint == null) {
        lpInitPoint
          = lpDevice.getDefaultConfiguration().getBounds().getLocation();
      }//..?
    }//..~
    if (lpInitPoint == null) {
      lpInitPoint = lpDummyPoint;
    }
    if (lpInitPoint == null) {
      lpInitPoint = new Point(0, 0);
    }
    return lpInitPoint;
  }//+++

  public static void ccApplyLookAndFeel(int pxIndex, boolean pxRead) {

    //-- pre
    String lpTarget = UIManager.getCrossPlatformLookAndFeelClassName();

    //-- getting
    if (pxIndex >= 0) {
      UIManager.LookAndFeelInfo[] lpInfos
        = UIManager.getInstalledLookAndFeels();
      if (pxRead) {
        System.out.println(".ssApplyLookAndFeel::installed lookNfeel: 0->");
        int cnt = 0;
        for (UIManager.LookAndFeelInfo it : lpInfos) {
          System.out.print("[" + Integer.toString(cnt) + "] ");
          System.out.println(it.getClassName());
          cnt++;
        }//..~
      }//..?
      int lpIndex = pxIndex > (lpInfos.length - 1) ? lpInfos.length - 1 : pxIndex;
      lpTarget = lpInfos[lpIndex].getClassName();
    }//..?

    //-- applying
    try {
      UIManager.setLookAndFeel(lpTarget);
    } catch (Exception e) {
      System.err.println(".ccApplyLookAndFeel()::" + e.getMessage());
    }//..?

  }//+++

}//***
