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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.AbstractButton;
import ppplib.ScConst;
import ppplib.VcConst;
import ppplib.VcStampUtility;
import pppmodel.McCard;
import pppmodel.McVocabularyModel;

public final class MainActionManager {
  
  private static final MainActionManager SELF = new MainActionManager();
  public static final MainActionManager ccGetInstance(){return SELF;}//+++
  private MainActionManager(){}//++!
  
  //===
  
  public static final HashMap<Object,Runnable> O_MAP_OF_ACTION
    = new HashMap<Object,Runnable>();
  
  private static final ActionListener O_ACT_LISTENER = new ActionListener() {
    @Override public void actionPerformed(ActionEvent ae) {
      Object lpSource = ae.getSource();
      if(lpSource==null){return;}
      if(!O_MAP_OF_ACTION.containsKey(lpSource)){return;}
      O_MAP_OF_ACTION.get(lpSource).run();
    }//+++
  };//***
  
  public static final KeyListener O_KEY_LISTENER = new KeyListener() {
    @Override public void keyTyped(KeyEvent ke){}//+++
    @Override public void keyPressed(KeyEvent ke){}//+++
    @Override public void keyReleased(KeyEvent ke){
      int lpCharCode=(int)ke.getKeyChar();
      switch(lpCharCode){
        case 0x0A:
          MainFrame.ccWriteln(ccExecute(MainFrame.O_FIELD.getText()));
          MainFrame.O_FIELD.setText("");
        break;
        default:break;
      }//..?
    }//+++
  };//***
  
  public static final MouseAdapter O_TABLE_LISTENER = new MouseAdapter() {
    @Override public void mouseReleased(MouseEvent e) {
      MainFrame.O_STATUS_BAR
        .setText(McVocabularyModel.ccGetInstance().ccToStatusString());
    }//+++
  };//***
  
  //===
  
  public static final Runnable O_QUITTING = new Runnable(){
    @Override public void run() {
      if(!MainFrame.ccYesOrNoBox("sure?")){return;}
      int lpCode=0;
      System.out.println("MainFrame::end_with:"+Integer.toString(lpCode));
      System.exit(lpCode);
    }//+++
  };//***
  
  public static final Runnable O_DISCLAIMING = new Runnable(){
    @Override public void run() {
      MainFrame.ccMessageBox(
        "CAST IN THE NAME OF TEST"
        +VcConst.C_V_NEWLINE+
        "YA NOT GUILTY"
      );
    }//+++
  };//***
  
  public static final Runnable O_REFOCUSING = new Runnable(){
    @Override public void run() {
      MainFrame.O_KEY_ENG.setText("");
      MainFrame.O_VAL_JPN.setText("");
      MainFrame.O_KEY_ENG.requestFocus();
    }//+++
  };//***
  
  public static final Runnable O_ENCOUNTERING = new Runnable(){
    @Override public void run() {
      String lpEnglish = MainFrame.O_KEY_ENG.getText();
      String lpDefinition = MainFrame.O_VAL_JPN.getText();
      if(!(
          VcConst.ccIsValidString(lpEnglish)
        &&VcConst.ccIsValidString(lpDefinition)
      )){return;}
      boolean lpAnswer = McVocabularyModel.ccGetInstance()
        .ccRegisterNewCard(lpEnglish, lpDefinition);
      if(!lpAnswer){
        MainFrame.ccErrorBox("invalid word input");
        MainFrame.ccWriteln(String.format(
          "O_ENCOUNTERING.run() $ bad::[%s,%s]", lpEnglish,lpDefinition
        ));
      }else{
        MainFrame.O_TABLE.ccRefresh();
        ScConst.ccScrollToLast(MainFrame.O_TABLE);
      }//..?
      McVocabularyModel.ccGetInstance().ccSort();
      MainFrame.O_TABLE.ccRefresh();
      MainFrame.O_STATUS_BAR
        .setText(McVocabularyModel.ccGetInstance().ccToStatusString());
      MainFrame.O_KEY_ENG.setText("");
      MainFrame.O_VAL_JPN.setText("");
      MainFrame.O_KEY_ENG.requestFocus();
    }//+++
  };//***
  
  public static final Runnable O_FLASHING = new Runnable() {
    @Override public void run() {
      if(McVocabularyModel.ccGetInstance().ccGetSize()<=12){
        MainFrame.ccErrorBox("you do know more than this, dont you?");
        return;
      }//..?
      McVocabularyModel.ccGetInstance().ccSetFlashing(true);
      MainFrame.O_TABLE.ccRefresh();
      for(int i=0;i<10;i++){
        McCard lpCard = McVocabularyModel.ccGetInstance().ccGetRandomCard();
        int lpAnswerIndex = VcConst.ccRandom(0, 4)&0x03;
        ArrayList<String> lpLesAnswer = new ArrayList<String>(4);
        lpLesAnswer.add("");
        lpLesAnswer.add("");
        lpLesAnswer.add("");
        lpLesAnswer.add("");
        lpLesAnswer.set(lpAnswerIndex, lpCard.ccGetDefinition());
        for(int j=0;j<4;j++){
          if(lpLesAnswer.get(j).isEmpty()){
            lpLesAnswer.set(
              j, McVocabularyModel.ccGetInstance().ccGetRandomDefinition()
            );
          }//..?
        }//..~
        boolean lpCheck = MainFrame.ccFlashBox(lpCard, lpLesAnswer);
        if(lpCheck){
          lpCard.ccIncrementFlashCount();
        }else{
          MainFrame.ccMessageBox("x:"+lpCard.ccGetDefinition());
        }//..?
      }//..~
      McVocabularyModel.ccGetInstance().ccSetFlashing(false);
      McVocabularyModel.ccGetInstance().ccSort();
      MainFrame.O_TABLE.ccRefresh();
    }//+++
  };//***
  
  public static final Runnable O_REFRESHING = new Runnable() {
    @Override public void run() {
      McVocabularyModel.ccGetInstance().ccSort();
      MainFrame.O_TABLE.ccRefresh();
    }//+++
  };//***
  
  public static final Runnable O_DELETING = new Runnable() {
    @Override public void run() {
      int lpIndex = MainFrame.O_TABLE.ccGetSelectedRowIndex();
      if(lpIndex<0){return;}
      if(McVocabularyModel.ccGetInstance().ccGetSize() <= 4){
        MainFrame.ccMessageBox("i guess we dont need to");
        return;
      }//..?
      boolean lpAnswer = MainFrame
        .ccYesOrNoBox("you surely got it?");
      if(lpAnswer){
        McVocabularyModel.ccGetInstance().ccDelete(lpIndex);
        MainFrame.O_TABLE.ccRefresh();
        MainFrame.O_STATUS_BAR
          .setText(McVocabularyModel.ccGetInstance().ccToStatusString());
      }//..?
    }//+++
  };//***
  
  public static final Runnable O_DISCARDING = new Runnable() {
    @Override public void run() {
      int lpSize = McVocabularyModel.ccGetInstance().ccGetSize();
      if(lpSize <= 64){
        MainFrame.ccMessageBox("i guess we dont need to");
      }else{
        boolean lpAnswer = MainFrame
          .ccYesOrNoBox("so will those ranked TOP4 get dicharded ?");
        if(lpAnswer){
          McVocabularyModel.ccGetInstance().ccSort();
          McVocabularyModel.ccGetInstance().ccDiscard(4);
          MainFrame.O_TABLE.ccRefresh();
          MainFrame.O_STATUS_BAR
            .setText(McVocabularyModel.ccGetInstance().ccToStatusString());
        }//..?
      }//..?
    }//+++
  };//***
  
  public static final Runnable O_IMPORTING = new Runnable() {
    @Override public void run() {
      ScConst.ccSetFileChooserType('i');
      File lpFile = ScConst.ccGetFileByFileChooser('f');
      if(lpFile==null){return;}//..?
      McVocabularyModel.ccGetInstance().ccImportFromFile(lpFile);
      //McVocabularyModel.ccGetInstance().ccSort();
      MainFrame.O_TABLE.ccRefresh();
      MainFrame.O_STATUS_BAR
        .setText(McVocabularyModel.ccGetInstance().ccToStatusString());
    }//+++
  };//***
  
  public static final Runnable O_EXPORTING = new Runnable() {
    @Override public void run() {
      ScConst.ccSetFileChooserType('e');
      ScConst.ccSetFileChooserSelectedFile(
        VcConst.C_V_PWD+VcConst.C_V_PATHSEP
          +"vlist"+VcStampUtility.ccFileNameTypeIII()+".json"
      );
      File lpFile = ScConst.ccGetFileByFileChooser('f');
      if(lpFile==null){return;}
      if(lpFile.exists()){
        if(!lpFile.canWrite()){
          MainFrame.ccErrorBox("choosen file can not get overwritten");
          return;
        }//..?
      }//..?
      MainFrame.ccWriteln(".O_IMPORTING $ ", lpFile.getAbsolutePath());
      McVocabularyModel.ccGetInstance().ccExportToFile(lpFile);
    }//+++
  };//***
  
  public static final Runnable O_CLEARING = new Runnable() {
    @Override public void run() {
      if(!MainFrame.ccYesOrNoBox("sure?")){return;}
      McVocabularyModel.ccGetInstance().ccClear();
      MainFrame.O_TABLE.ccRefresh();
      MainFrame.O_STATUS_BAR
        .setText(McVocabularyModel.ccGetInstance().ccToStatusString());
    }//+++
  };//***
  
  //===
  
  public static final
  void ccRegisterAction(AbstractButton pxButton, Runnable pxAction){
    if(pxButton==null){return;}
    if(pxAction==null){return;}
    if(O_MAP_OF_ACTION.containsKey(pxButton)){return;}
    pxButton.addActionListener(O_ACT_LISTENER);
    O_MAP_OF_ACTION.put(pxButton, pxAction);
  }//+++
  
  public static final
  void ccRegisterAction(String pxCommand, Runnable pxAction){
    if(!VcConst.ccIsValidString(pxCommand)){return;}
    if(pxAction==null){return;}
    if(O_MAP_OF_ACTION.containsKey(pxCommand)){return;}
    O_MAP_OF_ACTION.put(pxCommand, pxAction);
  }//+++
  
  public static final
  String ccExecute(String pxCommand){
    if(!VcConst.ccIsValidString(pxCommand)){return ">>>";}
    if(O_MAP_OF_ACTION.containsKey(pxCommand)){
      O_MAP_OF_ACTION.get(pxCommand).run();
      return "[accepted]"+pxCommand;
    }else{
      return "[unhandled]"+pxCommand;
    }//..?
  }//+++
  
}//***eof
