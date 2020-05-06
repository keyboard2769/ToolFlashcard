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

package ppptest;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import ppplib.VcConst;
import pppmodel.McCard;

public class TestDelegate {
  
  
  private static void ccTestRangedRandomInteger(int pxL, int pxH){
    System.out.println(".ccTestRangedRandomInteger() $ begin");
    for(int i=0;i<10;i++){
      System.out.println(
        ".ccRandom() $ "+Integer.toString(VcConst.ccRandom(pxL, pxH))
      );
    }//..~
    System.out.println(".ccTestRangedRandomInteger() $ end");
  }//+++
  
  private static void ccTestSortList(){
    
    System.out.println(".ccTestSortList() $ begin");
    
    List<McCard> lpDesNumber = new LinkedList<McCard>();
    lpDesNumber.add(new McCard("one", "hi"));
    lpDesNumber.add(new McCard("two", "hu"));
    lpDesNumber.add(new McCard("three", "mi"));
    lpDesNumber.add(new McCard("for", "yo"));
    lpDesNumber.add(new McCard("five", "ii"));
    
    for(int i=0;i<lpDesNumber.size();i++){
      lpDesNumber.get(i).ccSetupCount(
        (int)(VcConst.ccRandom()*16f),
        (int)(VcConst.ccRandom()*16f)
      );
    }//..?
    
    
    System.out.println(".ccTestSortList() $ before");
    
    System.out.println(
      Arrays.toString(lpDesNumber.toArray()).replace(",", ",\r\n")
    );
    
    Collections.sort(lpDesNumber);
    
    System.out.println(".ccTestSortList() $ after");
    
    System.out.println(
      Arrays.toString(lpDesNumber.toArray()).replace(",", ",\r\n")
    );
    
    System.out.println(".ccTestSortList() $ end");
    
  }//+++
  
  private static void ccTestEnglishWordRegex(){
    String[] lpDesSample = new String[]{
      "time","time square","12time","time12","time12square",
      "time-square","timeSquare","@timeSquare","time#$square"
    };
    for(String it : lpDesSample){
      System.out.println(String.format(
        ".ccIsAlphabetWord $ %s : %s",
        it,VcConst.ccIsAlphabetWord(it)?"o":"x"
      ));
    }//..~
  }//+++
  
  //===
  
  public static void main(String[] args) {
    System.out.println("ppptest.TestDelegate.main() $ begin");
    
    //-- test
    ccTestRangedRandomInteger(0, 4);
    
    //-- post
    System.out.println("ppptest.TestDelegate.main() $ end");
    
  }//!!!
  
}//***eof
