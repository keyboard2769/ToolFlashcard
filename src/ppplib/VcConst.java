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

import java.util.Calendar;
import java.util.Random;

public final class VcConst {

  public static final String C_V_PATHSEP
    = System.getProperty("file.separator");

  public static final String C_V_NEWLINE
    = System.getProperty("line.separator");

  public static final String C_V_OS
    = System.getProperty("os.name");

  public static final String C_V_PWD
    = System.getProperty("user.dir");
  
  private static final Random O_RANOM = new Random(
      Calendar.getInstance().get(Calendar.MINUTE)
    * Calendar.getInstance().get(Calendar.SECOND)
  );
  
  //=== radom
  
  public static final float ccRandom(){
    return O_RANOM.nextFloat();
  }//+++
  
  public static final int ccRandom(int pxLowBound, int pxHightBound){
    return pxLowBound+(int)(ccRandom()*(float)(pxHightBound-pxLowBound));
  }//+++
  
  //=== string
  
  static public final boolean ccIsValidString(String pxLine){
    if(pxLine==null){return false;}
    return !pxLine.isEmpty();
  }//+++
  
  static public final boolean ccIsAlphabetWord(String pxLine){
    if(!ccIsValidString(pxLine)){return false;}
    return pxLine.matches("^[-a-zA-Z]{1,64}$");
  }//+++
  
  
}//***eof
