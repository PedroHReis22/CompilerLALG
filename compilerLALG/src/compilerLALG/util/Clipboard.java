package compilerLALG.util;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Manipulação do clipboard
 */
public class Clipboard {
	
	/**
	 * Retorna o conteúdo do clipboard do sistema
	 * 
	 * @return O conteúdo do clipboard do sistema
	 */
	public static String getClipboardContents() {
		
		String result = "";
		java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    Transferable contents = clipboard.getContents(null);
	    
	    boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
	    
	    if (hasTransferableText) {
	    	
	    	try { result = (String)contents.getTransferData(DataFlavor.stringFlavor); }
	    	catch (UnsupportedFlavorException | IOException e) {
	    		e.printStackTrace();
	    	}
	    }
	    
	    return result;
	}
	
	/**
	 * Define o conteúdo de uma string no clipboard do sistema
	 * 
	 * @param string A string a ser transferiada para o clipboard do sistema 
	 */
	public static void setClipboardContents(String string){
	    StringSelection stringSelection = new StringSelection(string);
	    java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents(stringSelection, null);
	  }
	
	
}
