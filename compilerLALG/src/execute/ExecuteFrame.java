package execute;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class ExecuteFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	JTextArea txExecutation;

	private ArrayList<String> C;
	private Double[] D;
	private int s;

	public ExecuteFrame(ArrayList<String> C) {

		//initComponents();

		this.C = C;
		D = new Double[C.size()];
		
		interpretationByteCode();
		System.out.println("terminou");
	}

	private void initComponents() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 400);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JTextArea textArea = new JTextArea();
		textArea.setBackground(Color.BLACK);
		contentPane.add(textArea, BorderLayout.CENTER);

		textArea.setForeground(Color.WHITE);
		textArea.setFont(new Font("Consolas", Font.BOLD, 14));
	}

	public void interpretationByteCode() {

		for (int t = 0; t < D.length; t++) {
			D[t] = 0.0;
		}
		

		String aux = null;
		s = 0;
		double saidaPadrao = 21.0;
		double entradaPadrao = 5.0;

		for (int i = 0; i < C.size(); i++) {
			
			
			

			aux = C.get(i);
			String[] a = aux.split(" ");

			if (a[0].equals("INPP")) {
				s = s - 1;
			}
			if (a[0].equals("AMEM")) {
				int m = Integer.parseInt(a[1]);
				s = s + m;
			}

			if (a[0].equals("DMEM")) {
				int m = Integer.parseInt(a[1]);
				s = s - m;
			}

			if (a[0].equals("PARA")) {
				dispose();
			}

			if (a[0].equals("LEIT")) {
				s = s + 1;
				D[s] = new Scanner(System.in).nextDouble();
			}

			if (a[0].equals("LERL")) {
				s = s + 1;
				D[s] = new Scanner(System.in).nextDouble();
			}
			
			if (a[0].equals("LEBL")) {
				s = s + 1;
				D[s] = new Scanner(System.in).nextDouble();
			}

			if (a[0].equals("IMPR")) {
				System.out.println(D[s]);
				s = s - 1;
			}

			if (a[0].equals("IMPC")) {
				System.out.println(D[s]);
				s = s - 1;
			}

			if (a[0].equals("IMPE")) {
				System.out.println(D[s]);
				s = s - 1;
			}

			if (a[0].equals("INVR")) {
				D[s] = -D[s];
			}

			if (a[0].equals("CONJ")) {
				if (D[s - 1] == 1 && D[s] == 1) {
					D[s - 1] = 1.0;
				} else {
					D[s - 1] = 0.0;
				}
			}

			if (a[0].equals("DISJ")) {
				if (D[s - 1] == 1 || D[s] == 1) {
					D[s - 1] = 1.0;
				} else {
					D[s - 1] = 0.0;
				}
			}

			if (a[0].equals("CMME")) {
				if (D[s - 1] < D[s]) {
					D[s - 1] = 1.0;
				} else {
					D[s - 1] = 0.0;
				}
			}

			if (a[0].equals("CMMA")) {
				if (D[s - 1] > D[s]) {
					D[s - 1] = 1.0;
				} else {
					D[s - 1] = 0.0;
				}
			}

			if (a[0].equals("CMIG")) {
				if (D[s - 1].equals(D[s])) {
					D[s - 1] = 1.0;
				} else {
					D[s - 1] = 0.0;
				}
			}

			if (a[0].equals("CMDG")) {
				if (!D[s - 1].equals(D[s])) {
					D[s - 1] = 1.0;
				} else {
					D[s - 1] = 0.0;
				}
			}

			if (a[0].equals("CMAG")) {
				if (D[s - 1] >= D[s]) {
					D[s - 1] = 1.0;
				} else {
					D[s - 1] = 0.0;
				}
			}

			if (a[0].equals("CMEG")) {
				if (D[s - 1] <= D[s]) {
					D[s - 1] = 1.0;
				} else {
					D[s - 1] = 0.0;
				}
			}

			if (a[0].equals(("SOMA"))) {
				D[s - 1] = D[s - 1] + D[s];
				s = s - 1;
			}

			if (a[0].equals(("SUBT"))) {
				D[s - 1] = D[s - 1] - D[s];
				s = s - 1;
			}

			if (a[0].equals(("MULT"))) {
				D[s - 1] = D[s - 1] * D[s];
				s = s - 1;
			}

			if (a[0].equals(("DIVI"))) {
				D[s - 1] = D[s - 1] / D[s];
				s = s - 1;
			}

			if (a[0].equals(("MODI"))) {
				D[s - 1] = D[s - 1] % D[s];
				s = s - 1;
			}

			if (a[0].equals("NEGA")) {
				D[s] = 1 - D[s];
			}

			if (a[0].equals("ARMZ")) {
				int n = Integer.parseInt(a[1]);
				D[n] = D[s];
				s = s - 1;
			}

			if (a[0].equals("CRCT")) {
				s = s + 1;
				D[s] = Double.parseDouble(a[1]);
			}

			if (a[0].equals("CRVL")) {
				s = s + 1;
				int n = Integer.parseInt(a[1]);
				D[s] = D[n];
			}

			if (a[0].equals(("NADA"))) {
				continue;
			}

			if (a[0].equals(("DSVS"))) {
				int q = Integer.parseInt(a[1]);
                for (int m = 0; m < C.size(); m++) {
                    if (C.get(m).startsWith(Integer.toString(q))) {
                        i = m;
                    }

                }
			}

			if (a[0].equals(("DSVF"))) {
				
				int p = Integer.parseInt(a[1]);
                if (D[s - 1] == 0.0) {
                    for (int m = 0; m < C.size(); m++) {
                        if (C.get(m).startsWith(Integer.toString(p))) {
                            i = m;
                        }
                    }

                }

                s = s - 1;
			}
			
//			System.out.println("bytecode = " + C.get(i));
//			toString();
//			System.out.println("s = " + s);
//			System.out.println("----------------");
//			new Scanner(System.in).nextLine();
			
//			System.out.println(this);
//			System.out.println(s);
//			System.out.println("----------------");
//			new Scanner(System.in).nextLine();

		}
				
	}
	
	public String toString() {
				
		for(int i = 0; i <= s; ++i) System.out.println(i + " ---> " + D[i]);
		
		return "";
	}

}