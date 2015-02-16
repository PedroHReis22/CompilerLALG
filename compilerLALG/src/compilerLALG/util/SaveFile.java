package compilerLALG.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Salva um conteúdo de texto em um arquivo
 */
public class SaveFile {
	
	private File file;
	private String contentFile;
	
	/**
	 * Cria o objeto para salvar o conteúdo em um arquivo texto
	 * 
	 * @param file O arquivo em que será armazenado o conteúdo
	 * @param contentFile O conteúdo a ser salvo no arquivo
	 */
	public SaveFile(File file, String contentFile) {
		this.file = file;
		this.contentFile = contentFile;
		createFile();
	}
	
	/**
	 * Cria o arquivo texto a partir do conteúdo
	 */
	private void createFile() {
		
		try {
			
			FileWriter fileWriter = new FileWriter(file);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			
			bufferedWriter.write(contentFile);
			
			bufferedWriter.close();
			fileWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}

}
