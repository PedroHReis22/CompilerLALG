package compilerLALG.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Lê um arquivo texto, retornando o seu conteúdo
 */
public class ReadFile {
	
	private File file;
	private String contentFile;
	
	/**
	 * Cria o objeto para leitura de arquivo texto
	 * 
	 * @param file O arquivo a ser lido
	 */
	public ReadFile(File file) {
		this.file = file;
		setContentFile();	
	}
	
	/**
	 * Percorre o arquivo texto, armazenando o seu conteúdo
	 */
	private void setContentFile() {
		
		StringBuffer buffer = new StringBuffer();
		
		try {
			
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			String line;
			while((line = bufferedReader.readLine()) != null) {
				buffer.append(line + "\n");
			}
						
			bufferedReader.close();
			fileReader.close();
			
			contentFile = buffer.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	/**
	 * Retorna o conteúdo do arquivo lido
	 * 
	 * @return O conteúdo do arquivo lido
	 */
	public String getContenFile() {
		return contentFile;
	}	

}
