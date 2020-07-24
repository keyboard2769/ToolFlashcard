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

package pppmodel;

import processing.data.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import ppplib.ScConst;
import ppplib.VcConst;
import pppmain.MainFrame;
import processing.core.PApplet;

public final class McVocabularyModel implements TableModel{
  
  private static final McVocabularyModel SELF = new McVocabularyModel();
  public static final McVocabularyModel ccGetInstance(){return SELF;}//+++
  private McVocabularyModel(){}//++!
  
  private static int smDummyCounter = 0;
  
  private boolean cmFlashing = false;
  
  private final List<McCard> cmLesCard = new LinkedList<McCard>();
  
  private final HashMap<String, McCard> cmLesEntry
    = new HashMap<String, McCard>();
  
  private final List<String> cmLesJamming = new ArrayList<String>();
  
  public final void ccInit(){
    ccImportCard("xxx", "yyy", -1, -1);
  }//..?
  
  //===
  
  public final boolean ccImportCard(
    String pxEnglish, String pxDefinition,
    int pxEncounter, int pxFlash
  ){
    if(!VcConst.ccIsAlphabetWord(pxEnglish)){return false;}
    if(!VcConst.ccIsValidString(pxDefinition)){return false;}
    McCard lpCard = new McCard(pxEnglish, pxDefinition);
    lpCard.ccSetupCount(pxEncounter, pxFlash);
    if(!cmLesEntry.containsKey(pxEnglish)){
      cmLesEntry.put(pxEnglish, lpCard);
      cmLesCard.add(lpCard);
    }else{
      McCard lpExistCard = cmLesEntry.get(pxEnglish);
      String lpOldDef  = lpExistCard.ccGetDefinition();
      boolean lpAnswer = pxDefinition.equals(lpOldDef);
      if(!lpAnswer){
        lpAnswer=MainFrame.ccYesOrNoBox(String.format(
          "overwrite? %s new : %s %s old : %s",
          VcConst.C_V_NEWLINE,pxDefinition,
          VcConst.C_V_NEWLINE,lpOldDef
        ));
      }
      if(lpAnswer){
        lpExistCard.ccSetDefinition(pxDefinition);
        MainFrame.ccWriteln(
          ".ccImportCard $ user choosed to overwrite definition",
          lpExistCard.ccGetEnlish()
        );
      }//..?
      lpExistCard.ccIncrementEncounterCount();
    }//..?
    if(!cmLesJamming.contains(pxDefinition)){
      cmLesJamming.add(pxDefinition);
    }//..?
    return true;
  }//+++
  
  public final
  boolean ccRegisterNewCard(String pxEnglish, String pxDefinition){
    return ccImportCard(pxEnglish, pxDefinition, 0, 0);
  }//+++
  
  public final McCard ccGetRandomCard(){
    return cmLesCard.get(VcConst.ccRandom(0, cmLesCard.size()));
  }//+++
  
  public final String ccGetRandomDefinition(){
    return cmLesJamming.get(VcConst.ccRandom(0, cmLesJamming.size()-1));
  }//+++
  
  public final int ccGetDummyCount(){
    return smDummyCounter;
  }//+++
  
  public final String ccToStatusString(){
    return String.format("[total:%d][at:%d]",
      cmLesCard.size(), 
      MainFrame.O_TABLE.ccGetSelectedRowIndex()
    );
  }//+++
  
  public final void ccSetFlashing(boolean pxStatus){
    cmFlashing = pxStatus;
  }//+++
  
  public final void ccSort(){
    try {
      Collections.sort(cmLesCard);
    } catch (Exception e) {
      MainFrame.ccWriteln("[bad]sorting prolem occured while doing ??? .");
      MainFrame.ccErrorBox("bad sorting prolem!!");
    }//..?
  }//+++
  
  public final int ccGetSize(){
    return cmLesCard.size();
  }//+++
  
  public final void ccDelete(int pxIndex){
    if(pxIndex<1){return;}
    int lpIndex=0;
    if(pxIndex>=ccGetSize()){
      lpIndex=ccGetSize()-1;
    }else{
      lpIndex=pxIndex;
    }//..?
    if(cmLesCard.size()>4){
      cmLesCard.remove(lpIndex);
    }//..?
  }//+++
  
  public final void ccDiscard(int pxCount){
    if(pxCount<1){return;}
    int lpCount=0;
    if(pxCount>=ccGetSize()){
      lpCount=ccGetSize()-1;
    }else{
      lpCount=pxCount;
    }//..?
    for(int i=0;i<pxCount;i++){
      if(cmLesCard.size()>4){
        cmLesCard.remove(0);
      }//..?
    }//..~
  }//+++
  
  public final void ccImportFromFile(File pxFile){
    if(pxFile == null){
      MainFrame.ccErrorBox("abort");
      return;
    }//..?
    if(!(
         pxFile.canRead() && pxFile.isFile() 
      && (pxFile.length()<999999)
      && (pxFile.getName().endsWith(".json"))
    )){
      MainFrame.ccErrorBox("invalid file");
      return;
    }//..?
    JSONArray lpJSON=null;
    try {
       lpJSON = JSONArray.parse(PApplet.join(
        PApplet.loadStrings(pxFile), ""
      ));
    } catch (Exception e) {
      lpJSON=null;
      MainFrame.ccErrorBox("something seriousely wrong occurreed");
      System.err.println(
        "pppmodel.McVocabularyModel.ccImportFromFile() $ unknown exception"
      );
      System.exit(-1);
    }//..?
    if(lpJSON!=null){
      System.out.println("pppmodel.McVocabularyModel.ccImportFromFile() $ "+lpJSON.size());
      for(int i=0;i<lpJSON.size();i++){
        JSONArray lpRecord = lpJSON.getJSONArray(i);
        int lpSize = lpRecord.size();
        String lpE = lpSize>=1?lpRecord.getString(0):"";
        String lpD = lpSize>=2?lpRecord.getString(1):"";
        int lpEC = lpSize>=3?lpRecord.getInt(2):0;
        int lpFC = lpSize>=4?lpRecord.getInt(3):0;
        ccImportCard(lpE, lpD, lpEC, lpFC);
      }//..~
    }//..?
  }//+++
  
  public final void ccExportToFile(File pxFile){
    if(pxFile == null){return;}
    if(!pxFile.getName().endsWith("json")){
      MainFrame.ccErrorBox("bad externsion");
      return;
    }//..?
    JSONArray lpJSON = new JSONArray();
    for(McCard it : cmLesCard){
      JSONArray lpRecord = new JSONArray();
      lpRecord.setString(0, it.ccGetEnlish());
      lpRecord.setString(1, it.ccGetDefinition());
      lpRecord.setInt(2, it.ccGetEncounterCount());
      lpRecord.setInt(3, it.ccGetFlashCount());
      lpJSON.append(lpRecord);
    }//..~
    try {
      lpJSON.save(pxFile, "");
    } catch (Exception e) {
      MainFrame.ccErrorBox("some thing seriousely wrong occurred");
      System.err.println("pppmodel.McVocabularyModel.ccExportToFile() $ "
        +e.getMessage());
    }//..?
  }//+++
  
  public final void ccClear(){
    cmLesCard.clear();
    cmLesEntry.clear();
    cmLesJamming.clear();
    ccImportCard("xxx", "yyy", -1, -1);
  }//+++

  //=== interface 
  
  @Override public int getColumnCount() {
    return 4;
  }//+++

  @Override public String getColumnName(int columnIndex) {
    switch(columnIndex){
      case 0:return "Wrd";
      case 1:return "Def";
      case 2:return "Enc";
      case 3:return "Fla";
      default:return "<?>";
    }//..?
  }//+++

  @Override public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }//+++

  @Override public int getRowCount() {
    return cmFlashing?1:cmLesCard.size();
  }//+++
  
  @Override public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    System.out.println(String.format(
      "[DISCARD].setValueAt $ %s::[%d,%d]",
      aValue,rowIndex,columnIndex
    ));
  }//+++

  @Override public Object getValueAt(int rowIndex, int columnIndex) {
    if(rowIndex<0 || rowIndex>=cmLesCard.size()){return "<!>";}
    if(cmFlashing){return "m(_ _)m";}
    McCard lpCard = cmLesCard.get(rowIndex);
    if(lpCard==null){return "<?>";}
    switch(columnIndex){
      case 0:return lpCard.ccGetEnlish();
      case 1:return lpCard.ccGetDefinition();
      case 2:return lpCard.ccGetEncounterCount();
      case 3:return lpCard.ccGetFlashCount();
      default:return "<ny>";
    }//..?
  }//+++

  @Override public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;
  }//+++
  
  @Override public void addTableModelListener(TableModelListener l) {
    smDummyCounter++;
  }//+++

  @Override
  public void removeTableModelListener(TableModelListener l) {
    smDummyCounter--;
  }//+++
  
  //=== test
  
  @Deprecated private void tstAddDummyCard(){
    
    ccRegisterNewCard("one", "hi");
    ccRegisterNewCard("two", "fu");
    ccRegisterNewCard("three", "mi");
    ccRegisterNewCard("four", "yon");

    ccRegisterNewCard("five", "ii");
    ccRegisterNewCard("six", "mu");
    ccRegisterNewCard("seven", "na");
    ccRegisterNewCard("eight", "ya");

    ccRegisterNewCard("nine", "ko");
    ccRegisterNewCard("ten", "to");
    ccRegisterNewCard("eleven", "mo");
    ccRegisterNewCard("twelve", "ti");

    ccRegisterNewCard("thirteen", "wa");
    ccRegisterNewCard("fourteen", "ga");
    ccRegisterNewCard("fifteen", "yo");
    ccRegisterNewCard("sixteen", "da");
    
  }//+++
  
}//***EOF
