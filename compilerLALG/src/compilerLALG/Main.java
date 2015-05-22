package compilerLALG;

import java.awt.EventQueue;

import compilerLALG.userInterface.MainFrame;

/**
 * Inicializa a aplicação
 */
public class Main {

	/**
	 * Classe principal do programa, responsável por invocar os elementos para a execução do mesmo
	 * 
	 * @param args Argumentos passados por linha de comando. Estes argumentos são ignorados
	 */
	public static void main(String[] args) {
								
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				
				try {
					
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					
				} catch (Exception e) { e.printStackTrace(); }
			}
			
		});

	}

}
