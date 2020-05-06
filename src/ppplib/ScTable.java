/*
 * Copyright (C) 2018 Key Parker from K.I.C.
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 * it actually pissed me off at first i create every pane for a table. <br>
 * know i see separate them is not a bad idea at all. <br>
 */
public final class ScTable extends JScrollPane {
  
  private final JTable cmTable;
  
  //===
  
  /**
   * selection mode will be set single mode.<br>
   * @param pxModel we have a adaptor in this package
   * @param pxW width
   * @param pxH height
   */
  public ScTable(TableModel pxModel, int pxW, int pxH) {
    super();
    if(pxModel==null){
      cmTable = new JTable();
    }else{
      cmTable = new JTable(pxModel);
    }//..?
    cmTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    cmTable.getTableHeader().setReorderingAllowed(false);
    setViewportView(cmTable);
    if (pxW > 0 && pxH > 0) {
      setPreferredSize(new Dimension(pxW, pxH));
    }//..?
  }//+++
  
  //===
  
  private int ccFixIndex(int pxIndex) {
    int lpMasked = pxIndex & 0xFFFF;
    int lpSize = cmTable.getColumnModel().getColumnCount();
    return lpMasked > lpSize ? lpSize - 1 : lpMasked;
  }//+++
  
  //===
  
  /**
   * <pre>
   * wrapper for JTable::addMouseListener(). 
   * if you just add a listener to ScTable which is
   *   a scraoll pane actually, you can never get any mouse clicked
   *   event fired by that listener.
   * </pre>
   * @param pxListener #
   */
  public final void ccAddMouseListener(MouseListener pxListener) {
    if(pxListener==null){return;}
    cmTable.addMouseListener(pxListener);
  }//+++
  
  /**
   * no null check.<br>
   * @param pxFore text
   * @param pxBack background
   * @param pxGrid grid lines
   */
  public final
  void ccSetColor(Color pxFore, Color pxBack, Color pxGrid){
    cmTable.setForeground(pxFore);
    cmTable.setBackground(pxBack);
    cmTable.setGridColor(pxGrid);
  }//+++
  
  /**
   * ##
   * @param pxState #
   */
  public final void ccSetEnabled(boolean pxState){
    cmTable.setEnabled(pxState);
  }//+++
  
  /**
   * @param pxIndex from zero
   * @param pxWidth pix
   */
  public final void ccSetColumnWidth(int pxIndex, int pxWidth){
    TableColumn lpColumn
      = cmTable.getColumnModel().getColumn(ccFixIndex(pxIndex));
    lpColumn.setMinWidth(0x04);
    lpColumn.setMaxWidth(0xFF);
    lpColumn.setPreferredWidth(pxWidth&0xFF);
  }//+++
  
  /**
   * @param pxIndex from zero
   */
  public final void ccHideColumn(int pxIndex){
    TableColumn lpColumn
      = cmTable.getColumnModel().getColumn(ccFixIndex(pxIndex));
    lpColumn.setMinWidth(0);
    lpColumn.setMaxWidth(0);
    lpColumn.setPreferredWidth(0);
  }//+++
  
  /**
   * ##
   * @param pxModel do not pass null
   */
  public final void ccSetModel(TableModel pxModel) {
    if(pxModel == null){return;}
    cmTable.setModel(pxModel);
  }//+++
  
  //===
  
  /**
   * should not get called outside EDT but this does NOT check.<br>
   * @return the table component
   */
  public final JTable ccGetTable(){
    return cmTable;
  }//+++
  
  /**
   * alias to JTable::setRowSelectionInterval.<br>
   * thus JTable::setRowSelectionInterval supports multi-selection
   * the head and the tail will all get set to the passed value.<br>
   * should not get called outside EDT but this does NOT check.<br>
   * @param pxIndex count from zero
   */
  public final void ccSetSelectedRowIndex(int pxIndex){
    cmTable.setRowSelectionInterval(pxIndex, pxIndex);
  }//+++
  
  /**
   * alias to JTable::getSelectedRow.<br>
   * should not get called outside EDT but this does NOT check.<br>
   * @return getSelectedRow() of JTable.
   */
  public final int ccGetSelectedRowIndex(){
    return cmTable.getSelectedRow();
  }//+++
  
  //===
  
  /**
   * calls updateUI() than repaint().<br>
   */
  public final void ccRefresh(){
    if(!ScConst.ccIsEDT()){return;}
    cmTable.updateUI();
    cmTable.repaint();
  }//+++
  
  //===
  
  /**
   * wrapper for JTable::print(). <br>
   * for advanced use you should create a print job outside.
   */
  public final void ccViewPortPrint(){
    try{
      cmTable.print();
    }catch(PrinterException ex){
      System.out.println("kosui.pppswingui.ScConsole.ccPrint()::"
        + "PrinterException:"+ex.getLocalizedMessage());
    }//..%
  }//+++
  
}//***eof
