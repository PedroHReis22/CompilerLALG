package compilerLALG.userInterface;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import compilerLALG.lexical.Lexical;
import compilerLALG.lexical.Token;
import compilerLALG.syntactic.Syntactic;
import compilerLALG.syntactic.SyntaticError;
import compilerLALG.util.Clipboard;
import compilerLALG.util.ReadFile;
import compilerLALG.util.SaveFile;

/**
 * Frame principal da aplicação
 */
public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = -1016369432346974527L;
	
	private JMenuItem mntmOpen;
	private JMenuItem mntmSave;
	private JMenuItem mntmExit;
	private JMenuItem mntmCompile;
	
	private JTable lexemeTable;
	private JTable errorsTable;
	
	private JTextArea txSource;
	
	private JLabel lblCarretPosition;
	
	private FileChooser fileChooser;
	
	private JButton btnOpenFile;
	private JButton btnSaveFile;
	private JButton btnCompile;
	
	private Lexical lexical;
	
	private ArrayList<Boolean> errors;
	
	/**
	 * Cria o frame principal da aplicação
	 */
	public MainFrame() {
		
		fileChooser = new FileChooser();
		errors = new ArrayList<>();		
		
		initialize();
		setListeners();	
		
//		txSource.setText("program nome_programa; "
//				+ "var a, b : integer; "
//				+ "c : real;"
//				+ "begin "
//				+ "if +(");
//		compile();
	}

	/**
	 * Inicializa os elementos gráficos do frame
	 */
	private void initialize() {
		
		setBounds(100, 100, 750, 600);
		setMinimumSize(new Dimension(750, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Compilador LALG");
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("Arquivo");
		menuBar.add(mnFile);
		
		mntmOpen = new JMenuItem("Abrir");
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmOpen);
		
		mntmSave = new JMenuItem("Salvar");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		mntmExit = new JMenuItem("Sair");
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mnFile.add(mntmExit);
																																																												
		JMenu mnLexicalAnalyzer = new JMenu("Compilar");
		menuBar.add(mnLexicalAnalyzer);
		
		mntmCompile = new JMenuItem("Compilar");
		mntmCompile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mnLexicalAnalyzer.add(mntmCompile);
		
		JPanel contentPanel = new JPanel();
		getContentPane().add(contentPanel, BorderLayout.CENTER);
				
		txSource = new JTextArea();
		txSource.setLineWrap(true);
		txSource.setWrapStyleWord(true);
				
		JScrollPane textAreaPanel = new JScrollPane();
		textAreaPanel.setViewportView(txSource);
		
		TextLineNumber tln = new TextLineNumber(txSource);
		textAreaPanel.setRowHeaderView(tln);																																												
		
		lblCarretPosition = new JLabel("1 : 1");
		getContentPane().add(lblCarretPosition, BorderLayout.SOUTH);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		JScrollPane tablePanel = new JScrollPane();
		tabbedPane.addTab("Tabela de Lexemas", tablePanel);
		
		errorsTable = new JTable();
		
		errorsTable.setModel(new DefaultTableModel(
				new Object[][] {},	new String[] {"Localização", "Tipo de Erro", "Token", "Erro"}
		)
		{
			private static final long serialVersionUID = -904169401122777985L;
			
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		
		});
	
		errorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		errorsTable.setCellSelectionEnabled(true);
		errorsTable.getTableHeader().setReorderingAllowed(false);

		JScrollPane logPanel = new JScrollPane();
		logPanel.setViewportView(errorsTable);
		tabbedPane.addTab("Log de Erros", logPanel);
		
		GroupLayout layout = new GroupLayout(contentPanel);
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(textAreaPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
						.addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE))
					.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
					.addContainerGap()
					.addComponent(textAreaPanel, GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		lexemeTable = new JTable(){
			
			private static final long serialVersionUID = -3407327369465198202L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
				
				Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
				if(errors.get(rowIndex)) c.setBackground(new Color(255, 0, 0));
				else c.setBackground(new Color(255, 255, 255));
				return c;
				
			}
		};
		
		lexemeTable.setModel(new DefaultTableModel(
					new Object[][] {},	new String[] {"Lexema Lido", "Token", "Linha Inicial", "Linha Final", "Coluna Inicial", "Coluna Final"	}
		)
		{
			private static final long serialVersionUID = -904169401122777985L;
				
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
			
		});
		
		lexemeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lexemeTable.setCellSelectionEnabled(true);
		lexemeTable.getTableHeader().setReorderingAllowed(false);
		
		tablePanel.setViewportView(lexemeTable);
		contentPanel.setLayout(layout);
		
		JToolBar toolBar = new JToolBar();
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		btnOpenFile = new JButton(new ImageIcon(getClass().getResource("/compilerLALG/resources/open.png")));
		btnOpenFile.setToolTipText("Abrir arquivo");
		toolBar.add(btnOpenFile);
		
		btnSaveFile = new JButton(new ImageIcon(getClass().getResource("/compilerLALG/resources/save.png")));
		btnSaveFile.setToolTipText("Salvar em arquivo");
		toolBar.add(btnSaveFile);
		
		btnCompile = new JButton(new ImageIcon(getClass().getResource("/compilerLALG/resources/compile.png")));
		btnCompile.setToolTipText("Compilar");
		toolBar.add(btnCompile);
	}

	/**
	 * Define os listeners (ações) sobre a interface
	 */
	private void setListeners() {
						
		ActionListener menuListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(e.getSource().equals(mntmOpen)) openFile();
				else if(e.getSource().equals(mntmSave)) saveFile();
				else if(e.getSource().equals(mntmCompile)) compile();
				else if(e.getSource().equals(mntmExit)) exit();
				
			}
		};
		
		mntmOpen.addActionListener(menuListener);
		mntmSave.addActionListener(menuListener);
		mntmExit.addActionListener(menuListener);
		mntmCompile.addActionListener(menuListener);
		
		ActionListener btnListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(e.getSource().equals(btnOpenFile)) openFile();
				else if(e.getSource().equals(btnSaveFile)) saveFile();
				else if(e.getSource().equals(btnCompile)) compile();
				
			}
		};
		
		btnOpenFile.addActionListener(btnListener);
		btnSaveFile.addActionListener(btnListener);
		btnCompile.addActionListener(btnListener);
		
		txSource.addCaretListener(new CaretListener() {
			
            public void caretUpdate(CaretEvent e) {
            	
                JTextArea editArea = (JTextArea) e.getSource();

                int linenum = 1;
                int columnnum = 1;

                try {
                	
                    int caretpos = editArea.getCaretPosition();
                    linenum = editArea.getLineOfOffset(caretpos);
                    columnnum = caretpos - editArea.getLineStartOffset(linenum);
                    linenum += 1;
                    columnnum += 1;
                }
                
                catch(Exception ex) {
                	ex.printStackTrace();
                }
                
                lblCarretPosition.setText(linenum + " : " + columnnum);
            }
        });
		
		txSource.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.getKeyChar() == '\t') { //pressionado tab, é substituido por 8 espaços
										
					e.consume();
					
					try {
						
						Robot robot = new Robot();
						for(int i = 0; i < 8; ++i) {
							
							new Thread(new Runnable() {
								@Override public void run() { robot.keyPress(KeyEvent.VK_SPACE); }
							}).start();
							
						}
						
					} catch (AWTException ex) {
						ex.printStackTrace();
					}
										
				}
				else if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) { //control + v
					String clipboard = Clipboard.getClipboardContents();
					clipboard = clipboard.replaceAll("\\t", "        ");
					Clipboard.setClipboardContents(clipboard);
				}
			}
			
		});
		
	}
	
	/**
	 * Realiza a leitura de um arquivo de texto, exibindo seu conteúdo na área de texto.
	 */
	private void openFile() {
		
		fileChooser.showOpenDialog();
		
		if(fileChooser.hasSelectedFile()) {
			
			ReadFile readFile = new ReadFile(fileChooser.getSelectedFile());
			String contentFile = readFile.getContenFile();
			contentFile = contentFile.replaceAll("\\t", "        ");
			txSource.setText(contentFile);
			
			String title = "Compilador LALG";
			title = title + " [ " + fileChooser.getSelectedFile().getAbsolutePath() + " ]"; 
			setTitle(title);
		}
		
	}
	
	/**
	 * Salva o conteúdo do texto em um arquivo de texto
	 */
	private void saveFile() {
		
		fileChooser.showSaveDialog();
		
		if(fileChooser.hasSelectedFile()) {
			
			String contentFile = txSource.getText();
			new SaveFile(fileChooser.getSelectedFile(), contentFile);
			
			String title = "Compilador LALG";
			title = title + " [ " + fileChooser.getSelectedFile().getAbsolutePath() + " ]"; 
			setTitle(title);
			
		}
		
	}

	/**
	 * Executa a compilação do código fonte disponível
	 */
	private void compile() {
		
		removeAllTableRows(lexemeTable);
		removeAllTableRows(errorsTable);
		errors.removeAll(errors);
				
		if(!lexicalAnalyzer()) return;
		syntacticAnalyzer();
		
	}
	
	/**
	 * Realiza a análise léxica sobre a entrada no text area, exibindo os resultados em uma tabela
	 * 
	 * @return true se a análise não encontrou erros e fase se foram encontrados erros
	 */
	private boolean lexicalAnalyzer() {		
		
		if(txSource.getText() == null || txSource.getText().isEmpty()) return false;
		
		lexical = new Lexical();
		lexical.execute(txSource.getText());
		
		ArrayList<Token> tokens = lexical.getTokens();
		ArrayList<Token> lexicalErrors = lexical.getLexicalErrors();
		
		setLexemes(tokens);
		setLexicalErrors(lexicalErrors);
						
		for(Token token : tokens) errors.add(lexicalErrors.contains(token));
		
		return (!errors.contains(true));
		
	}
	
	/**
	 * Realiza a análise sintática sobre a entrada no text area, exibindo os erros encontrados em uma tabela.
	 * 
	 */
	private boolean syntacticAnalyzer() {
				
//		tokens.add(new Token("$", 0, 0));
//		
//		Syntactic syntactic = new Syntactic(); 
//		ArrayList<SyntaticError> syntaticErrors = syntactic.execute(tokens);
//		
//		for(int row = 0; row < syntaticErrors.size(); ++row) {
//			
//			SyntaticError syntaticError = syntaticErrors.get(row);
//			
//			addErrosTableRow();
//			
//			errorsTable.setValueAt(syntaticError.getToken().getStartLine() + ":" + syntaticError.getToken().getStartColumn(), row, 0);
//			errorsTable.setValueAt("Erro Sintático", row, 1);
//			errorsTable.setValueAt(syntaticError.getToken().getToken(), row, 3);
//			errorsTable.setValueAt(syntaticError.getError(), row, 3);
//			
//		}
		
		return true;
		
	}
	
	/**
	 * Define na tabela de erros, os erros encontrados na análise léxica
	 * 
	 * @param errors Os erros encontrados durante a análise léxica
	 */
	private void setLexicalErrors(ArrayList<Token> errors) {
		
		int row = 0;
		for(Token token : errors) {
			
			int tokenType = token.getType();
			
			DefaultTableModel model = (DefaultTableModel) errorsTable.getModel();
			model.addRow(new Object[]{null, null, null, null, null, null});
			
			errorsTable.setValueAt(token.getStartLine() + ":" + token.getStartColumn(), row, 0);
			errorsTable.setValueAt("Erro léxico", row, 1);
			errorsTable.setValueAt(token.getToken(), row, 2);
			
			if(tokenType == Token.UNKNOWN) errorsTable.setValueAt("Token inválido", row++, 3);
			else if(tokenType == Token.MALFORMED_IDENTIFIER) errorsTable.setValueAt("Identificador inválido", row++, 3);
			else if(tokenType == Token.MALFORMED_INTEGER_NUMBER) errorsTable.setValueAt("Número inteiro inválido", row++, 3);
			else if(tokenType == Token.MALFORMED_REAL_NUMBER) errorsTable.setValueAt("Número real inválido", row++, 3);
			
		}
	}
	
	/**
	 * Define os lexemas provenientes da análise léxica na tabela de lexemas
	 * 
	 * @param lexemes Os tokens obtidos na análise léxica
	 */
	private void setLexemes(ArrayList<Token> lexemes) {
		
		int row = 0;
		for(Token token : lexemes) {
			
			DefaultTableModel model = (DefaultTableModel) lexemeTable.getModel();
			model.addRow(new Object[]{null, null, null, null});
			
			lexemeTable.setValueAt(token.getToken(), row, 0);
			lexemeTable.setValueAt(token.getTokenType(), row, 1);
			lexemeTable.setValueAt(token.getStartLine(), row, 2);
			lexemeTable.setValueAt(token.getEndLine(), row, 3);
			lexemeTable.setValueAt(token.getStartColumn(), row, 4);
			lexemeTable.setValueAt(token.getEndColumn(), row, 5);
			
			++row;
		}
		
	}
		
	/**
	 * Remove todas as linhas da tabela
	 */
	private void removeAllTableRows(JTable table) {
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int rowCount = model.getRowCount();
		
		for(int i = 0; i < rowCount; ++i) 
			model.removeRow(0);
		
	}

	/**
	 * Encerra a execução do programa
	 */
	private void exit() {
		System.exit(0);
	}

}
