/*
 * Copyright (C) 2019 Key Parker from K.I.C
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ppplib;

import java.util.Calendar;
import processing.core.PApplet;

/**
 * i struggled with the file name control on my windows computer.<br>
 * what saved me is the format(time) command from autohotkey.<br>
 */
public final class VcStampUtility {
  
  private VcStampUtility(){}//..!
  
  //===
  
  /**
   * @return aliasing Calender::get()
   */
  static public final int ccYear(){
   return Calendar.getInstance().get(Calendar.YEAR);
  }//+++
  
  /**
   * @return aliasing Calender::get()
   */
  static public final int ccMonth() {
    return Calendar.getInstance().get(Calendar.MONTH) + 1;
  }//+++
  
  /**
   * @return aliasing Calender::get()
   */
  static public final int ccDay() {
    return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
  }//+++
  
  /**
   * @return aliasing Calender::get()
   */
  static public int ccHour() {
    return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
  }//+++
  
  /**
   * @return aliasing Calender::get()
   */
  static public int ccMinute() {
    return Calendar.getInstance().get(Calendar.MINUTE);
  }//+++
  
  /**
   * @return aliasing Calender::get()
   */
  static public int ccSecond() {
    return Calendar.getInstance().get(Calendar.SECOND);
  }//+++
  
  //===

  /**
   * @param pxForm like:yy-MM-dd hh:mm'ss
   * @return formatted
   */
  synchronized public static final String ccTimeStamp(String pxForm){
    if(!VcConst.ccIsValidString(pxForm)){return pxForm;}
    String lpRes=pxForm.replaceFirst("yy",PApplet.nf(PApplet.year()%2000,2));
    lpRes=lpRes.replaceFirst("MM",PApplet.nf(PApplet.month(),2));
    lpRes=lpRes.replaceFirst("dd",PApplet.nf(PApplet.day(),2));
    lpRes=lpRes.replaceFirst("hh",PApplet.nf(PApplet.hour(),2));
    lpRes=lpRes.replaceFirst("mm",PApplet.nf(PApplet.minute(),2));
    lpRes=lpRes.replaceFirst("ss",PApplet.nf(PApplet.second(),2));
    return lpRes;
  }//+++
  
  /**
   * supposedly for document file.<br>
   * @return # _yyMMdd
   */
  synchronized static public final String ccFilenameTypeII(){
    StringBuilder lpRes=new StringBuilder("_");
    lpRes.append(PApplet.nf(PApplet.year()%2000,2));
    lpRes.append(PApplet.nf(PApplet.month(),2));
    lpRes.append(PApplet.nf(PApplet.day(),2));
    return lpRes.toString();
  }//+++
  
  /**
   * supposedly for archive file.<br>
   * @return # _yyMMddhhmm
   */
  synchronized static public final String ccFileNameTypeIII(){
    StringBuilder lpRes=new StringBuilder("_");
    lpRes.append(PApplet.nf(PApplet.year()%2000,2));
    lpRes.append(PApplet.nf(PApplet.month(),2));
    lpRes.append(PApplet.nf(PApplet.day(),2));
    lpRes.append(PApplet.nf(PApplet.hour(),2));
    lpRes.append(PApplet.nf(PApplet.minute(),2));
    return lpRes.toString();
  }//+++
  
  /**
   * supposedly for blueprint markup.<br>
   * @return # yy-MM-dd
   */
  synchronized static public final String ccFileNameTypeIV(){
    StringBuilder lpRes=new StringBuilder("");
    lpRes.append(PApplet.nf(PApplet.year()%2000,2));
    lpRes.append('-');
    lpRes.append(PApplet.nf(PApplet.month(),2));
    lpRes.append('-');
    lpRes.append(PApplet.nf(PApplet.day(),2));
    return lpRes.toString();
  }//+++
  
  /**
   * supposedly for auto generated test file.<br>
   * @param pxMark #
   * @return # _%mark%hhmmss 
   */
  synchronized static public final String ccFileNameTypeV(String pxMark){
    StringBuilder lpRes=new StringBuilder("_");
    lpRes.append(pxMark);
    lpRes.append(PApplet.nf(PApplet.hour(),2));
    lpRes.append(PApplet.nf(PApplet.minute(),2));
    lpRes.append(PApplet.nf(PApplet.second(),2));
    return lpRes.toString();
  }//+++
  
  /**
   * supposedly for archive file.<br>
   * @return # _yyMMddhhmmss
   */
  synchronized static public final String ccFileNameTypeVI(){
    StringBuilder lpRes=new StringBuilder("_");
    lpRes.append(PApplet.nf(PApplet.year()%2000,2));
    lpRes.append(PApplet.nf(PApplet.month(),2));
    lpRes.append(PApplet.nf(PApplet.day(),2));
    lpRes.append(PApplet.nf(PApplet.hour(),2));
    lpRes.append(PApplet.nf(PApplet.minute(),2));
    lpRes.append(PApplet.nf(PApplet.second(),2));
    return lpRes.toString();
  }//+++
  
  //===
  
  /**
   * supposedly for data logger.<br>
   * @return # hh:mm'ss 
   */
  synchronized static public final String ccDataLogTypeI(){
    StringBuilder lpRes=new StringBuilder("--");
    lpRes.append(PApplet.nf(PApplet.hour(),2));
    lpRes.append(':');
    lpRes.append(PApplet.nf(PApplet.minute(),2));
    lpRes.append('\'');
    lpRes.append(PApplet.nf(PApplet.second(),2));
    return lpRes.toString();
  }//+++
  
  //===
  
  /**
   * supposedly for error generation.<br>
   * @return # %hh:mm'ss%
   */
  synchronized static public final String ccErrStampTypeI(){
    StringBuilder lpRes=new StringBuilder("[ERR-");
    lpRes.append(PApplet.nf(PApplet.hour(),2));
    lpRes.append(':');
    lpRes.append(PApplet.nf(PApplet.minute(),2));
    lpRes.append('\'');
    lpRes.append(PApplet.nf(PApplet.second(),2));
    lpRes.append(']');
    return lpRes.toString();
  }//++++
  
 }//***eof
