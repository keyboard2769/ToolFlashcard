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

import ppplib.VcConst;

public class McCard implements Comparable<McCard>{
  
  private final String cmEnglish;
  private String cmDefinition;
  private int cmEncounterCount;
  private int cmFlashCount;
  
  public McCard(String pxEnglish, String pxDefinition){
    cmEncounterCount=0;
    cmFlashCount=0;
    if(VcConst.ccIsValidString(pxEnglish)){
      String lpTrimmed =pxEnglish.trim();
      if(VcConst.ccIsAlphabetWord(lpTrimmed)){
        cmEnglish=lpTrimmed;
      }else{
        cmEnglish="@_";
      }//..?
    }else{
      cmEnglish="@_";
    }//..?
    if(VcConst.ccIsValidString(pxDefinition)){
      cmDefinition=pxDefinition.trim();
    }else{
      cmDefinition="yyy";
    }//..?
  }//++!
  
  //===
  
  public final void ccSetupCount(int pxEncounter, int pxFlash){
    cmEncounterCount=pxEncounter;
    cmFlashCount=pxFlash;
  }//+++
  
  public final void ccSetEncounterCount(int pxVal){
    cmEncounterCount = pxVal;
  }//+++
  
  public final void ccIncrementEncounterCount(){
    cmEncounterCount ++;
  }//+++
  
  public final void ccSetFlashCount(int pxVal){
    cmFlashCount = pxVal;
  }//+++
  
  public final void ccIncrementFlashCount(){
    cmFlashCount ++;
  }//+++
  
  public final void ccSetDefinition(String pxDefinition){
    if(VcConst.ccIsValidString(pxDefinition)){
      cmDefinition=pxDefinition.trim();
    }else{
      cmDefinition="yyy";
    }//..?
  }//+++
  
  //===
  
  public final String ccGetEnlish(){
    return cmEnglish;
  }//+++
  
  public final String ccGetDefinition(){
    return cmDefinition;
  }//+++
  
  public final int ccGetEncounterCount(){
    return cmEncounterCount;
  }//+++
  
  public final int ccGetFlashCount(){
    return cmFlashCount;
  }//+++
  
  public final int ccGetEvaluation(){
    return cmFlashCount+cmEncounterCount;
  }//+++
  
  //=== interface 

  @Override public int compareTo(McCard o) {
    if(o==null){return -1;}
    return o.ccGetEvaluation()-ccGetEvaluation();
    
    /* [tofigure]::what the hell was going on here?!
      if(o.ccGetEvaluation() == ccGetEvaluation()){
        if(o.ccGetEncounterCount() < ccGetEncounterCount()){
          return -1;
        }else{
          return 0;
        }
      }//..?
      if(o.ccGetEvaluation() < ccGetEvaluation()){
        return -1;
      }else{
        return 1;
      }//..?
    */
    
  }//+++

  @Override public String toString() {
    return String.format(
      "<w e=\"%s\" d=\"%s\" ec=\"%d\" fc=\"%d\"/>",
      cmEnglish, cmDefinition, cmEncounterCount, cmFlashCount
    );
  }//+++
  
}//***eof
