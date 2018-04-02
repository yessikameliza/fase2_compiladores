package gui;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import logic.AnalizadorLexico;
import logic.ParseException;
import logic.TokenMgrError;

public class MainWindow extends JFrame implements ActionListener {

	private JScrollPane scrollCampoTexto, scrollConsola;
	private JTextArea campoCodigo, campoArbol;
	private static JTextArea  consola;
	private JLabel arbol, txtConsola;
	private JButton btnArchivo, compilar;
	private static String temporal;
	private AnalizadorLexico analizadorLexico;
	
	public MainWindow() {
		
		this.setSize(950,600);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		setTitle("Compilador Huq");
		
		btnArchivo = new JButton("Archivo");
		btnArchivo.addActionListener(this);
		btnArchivo.setBounds(10, 5, 90, 23);
		add(btnArchivo);
		
		compilar = new JButton("Compilar");
		compilar.addActionListener(this);
		compilar.setBounds(110, 5, 90, 23);
		add(compilar);
		
	
		
		arbol = new JLabel("Arbol");
		arbol.setFont(new Font("Tahoma", Font.BOLD, 14));
		arbol.setForeground(Color.BLACK);
		arbol.setBounds(500, 11, 290, 23);
		add(arbol);
		
		txtConsola = new JLabel("Consola");
		txtConsola.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtConsola.setForeground(Color.BLACK);
		txtConsola.setBounds(10, 330, 290, 23);
		add(txtConsola);
		
		scrollCampoTexto = new JScrollPane();
		scrollCampoTexto.setBounds(10, 36, 424, 251);
		add(scrollCampoTexto);

		campoCodigo = new JTextArea();
		campoCodigo.setWrapStyleWord(true);
		campoCodigo.setLineWrap(true);
		scrollCampoTexto.setViewportView(campoCodigo);
		
		campoArbol = new JTextArea();
		campoArbol.setWrapStyleWord(true);
		campoArbol.setBounds(500, 36, 424, 251);
		campoArbol.setLineWrap(true);
		add(campoArbol);
		
		consola = new JTextArea();
		consola.setWrapStyleWord(true);
		consola.setLineWrap(true);
		
		scrollConsola = new JScrollPane();
		scrollConsola.setBounds(10, 350, 900, 100);
		add(scrollConsola);
		scrollConsola.setViewportView(consola);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		temporal="";
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if (event.getSource()==btnArchivo) {
			
			   try {
					System.out.println("Seleccione el codigo a analizar");
					// ---------------------------------Seleccion JFile Chooser-------------------
					// Creamos el objeto JFileChooser
					JFileChooser fc = new JFileChooser();

					// Creamos filtro
					FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.HUQ", "huq");

					// Le indicamos el filtro
					fc.setFileFilter(filtro);

					// Abrimos la ventana, guardamos la opcion seleccionada por el usuario
					int seleccion = fc.showOpenDialog(null);

					// Si el usuario, pincha en aceptar
					if (seleccion == JFileChooser.APPROVE_OPTION) {

						// Seleccionamos el fichero
						File fichero = fc.getSelectedFile();
						InputStream in = new FileInputStream(fichero);
						analizadorLexico = new AnalizadorLexico(in);
						analizadorLexico.TokenList();

					}

					// ---------------------------------Termina JFile Chooser-------------------

					System.out.println("Analisis terminado:");
					System.out.println("no se han hallado errores lexicos");

				} catch (TokenMgrError te) {
					System.out.println("Se han encontrado errores lexicos.");
					System.out.println(te.getMessage());

				} catch (ParseException e) {
					System.out.println("Analizador: Se han encontrado errores en el analisis.");
					System.out.println(e.getMessage());
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			
		}
		
		if(event.getSource() == compilar) {
			
			if(!(campoCodigo.getText().trim().equals(""))) {
				
				try {
					InputStream stream = new ByteArrayInputStream(campoCodigo.getText().getBytes("UTF-8"));
					analizadorLexico = new AnalizadorLexico(stream);
					analizadorLexico.TokenList();
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
			}else {
				
				JOptionPane.showMessageDialog(null, "Ingrese codigo valido!", "Error", JOptionPane.ERROR_MESSAGE);	
				}
		}
		
	}

	
	public static void escribirResultado(int n, String token, String lexema, int nL, int nC) {
		temporal += n + "- Token:" + token + " Lexema:" + lexema + " Linea:" + nL + " Columna:" + nC+"\n";
		consola.setText(temporal);
	}
	
}
