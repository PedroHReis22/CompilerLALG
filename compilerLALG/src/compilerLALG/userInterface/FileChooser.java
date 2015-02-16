package compilerLALG.userInterface;
 
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
 
/**
 * Objeto para a seleção de arquivos para abrir e salvar
 */
public class FileChooser extends JFileChooser {
     
    private static final long serialVersionUID = 7890303360710039220L;
     
    private String pathFile;
    private File lastDir;
    private boolean selectedFile;
     
    /**
     * Cria a representação do filechooser
     */
    public FileChooser(){
        setMultiSelectionEnabled(false);
        lastDir = null;
    }
     
   /**
    * Exibe o frame para seleção de arquivo para ser salvo 
    */
    public void showSaveDialog() {
         
        setCurrentDirectory(lastDir);
         
        selectedFile = false;
        int result = showSaveDialog(JFrame.getFrames()[0]);
         
        if(result == JFileChooser.APPROVE_OPTION){
             
            selectedFile = true;
            lastDir = getSelectedFile().getAbsoluteFile();
            pathFile = getSelectedFile().getAbsolutePath();
            
            if(new File(pathFile).exists()){
                if(!overwriteFile()) selectedFile = false;
            }
        }
         
    }
     
    /**
     * Exibe o frame para seleção de arquivo a ser aberto
     */
    public void showOpenDialog() {
         
        setCurrentDirectory(lastDir);
         
        selectedFile = false;
        int result = showOpenDialog(JFrame.getFrames()[0]);
         
        if(result == JFileChooser.APPROVE_OPTION){
            selectedFile = true;                    
            lastDir = getSelectedFile().getAbsoluteFile();
            pathFile = getSelectedFile().getAbsolutePath();
        }
    }    
     
    /**
     * Verifica se o usuário deseja sobrescrever um arquivo existente
     * 
     * @return true caso deseje-se sobrescrever o arquivo e fase em caso contrário
     */
    public boolean overwriteFile(){
        String tokens[] = pathFile.split("\\" + File.separator);
        String fileName = tokens[tokens.length - 1];
        String message = "O Arquivo " + fileName + " já existe, deseja sobrescrevê-lo?";
        String title = "Sobrescrever o arquivo?";
        int result = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return (result == 0) ? true : false;
    }
         
    /**
     * Retorna o caminho do arquivo selecionado
     * 
     * @return O caminho do arquivo selecionado
     */
    public String getSelectedPathFile(){
        return (selectedFile) ? pathFile : null;
    }
     
    /**
     * Retorna se existe ou não arquivo selecionado
     * 
     * @return true se existe arquivo selecionado pelo filechooser e false se não existe
     */
    public boolean hasSelectedFile(){
        return selectedFile;
    }
 
}