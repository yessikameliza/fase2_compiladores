package gui;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class MainWindow extends JFrame implements ActionListener {

	private JScrollPane scrollCampoTexto, scrollConsola;
	private JTextArea campoCodigo, campoArbol;
	private static JTextArea  consola;
	private JLabel arbol, txtConsola;
	private static String temporal;
	private AnalizadorLexico analizadorLexico;
	private JMenu mnCompilar;
	private JMenuBar menuBar;
	private JMenu mnArchivo;
	private JMenuItem mntmAbrir;
	private JMenuItem mntmNuevo;
	private JMenuItem mntmGuardar;
	private JMenuItem mntmGuardarComo;
	private JTextArea campoLinea;
	private File fichero;
	private JMenuItem mntmRunAs;
	
	public MainWindow() {
		
		this.setSize(950,600);
		getContentPane().setLayout(null);
		this.setLocationRelativeTo(null);
		setTitle("Compilador Huq");
		
	
		
		arbol = new JLabel("Arbol");
		arbol.setFont(new Font("Tahoma", Font.BOLD, 14));
		arbol.setForeground(Color.BLACK);
		arbol.setBounds(500, 11, 290, 23);
		getContentPane().add(arbol);
		
		txtConsola = new JLabel("Consola");
		txtConsola.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtConsola.setForeground(Color.BLACK);
		txtConsola.setBounds(10, 330, 290, 23);
		getContentPane().add(txtConsola);
		
		scrollCampoTexto = new JScrollPane();
		scrollCampoTexto.setBounds(54, 58, 424, 251);
		getContentPane().add(scrollCampoTexto);
		
				campoCodigo = new JTextArea();
				campoCodigo.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent arg0) {
						if (campoCodigo.getText().equals("\n\tIngrese aquí las sentencias a compilar.")) {
							campoCodigo.setText("");
						}
					}
					@Override
					public void focusLost(FocusEvent arg0) {
						if (campoCodigo.getText().equals("\n\tIngrese aquí las sentencias a compilar.")) {
							campoCodigo.setText("");
						}
					}
				});
				campoCodigo.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent arg0) {
						contarLineas();
					}
				});
				
				campoCodigo.setText("\n\tIngrese aquí las sentencias a compilar.");
				scrollCampoTexto.setViewportView(campoCodigo);
				campoCodigo.setWrapStyleWord(true);
				campoCodigo.setLineWrap(true);
		
		campoArbol = new JTextArea();
		campoArbol.setWrapStyleWord(true);
		campoArbol.setBounds(500, 59, 424, 251);
		campoArbol.setLineWrap(true);
		getContentPane().add(campoArbol);
		
		consola = new JTextArea();
		consola.setWrapStyleWord(true);
		consola.setLineWrap(true);
		
		scrollConsola = new JScrollPane();
		scrollConsola.setBounds(10, 350, 900, 100);
		getContentPane().add(scrollConsola);
		scrollConsola.setViewportView(consola);
		
		menuBar = new JMenuBar();
		menuBar.setBounds(10, 11, 124, 21);
		getContentPane().add(menuBar);
		
		mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);
		
		mntmAbrir = new JMenuItem("Abrir");
		mntmAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
						fichero = fc.getSelectedFile();
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
		});
		mnArchivo.add(mntmAbrir);
		
		mntmNuevo = new JMenuItem("Nuevo");
		mntmNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				campoCodigo.setText("\n\tIngrese aquí las sentencias a compilar.");
				campoLinea.setText("1");
				fichero = null;
				
			}
		});
		mnArchivo.add(mntmNuevo);
		
		mntmGuardar = new JMenuItem("Guardar");
		mntmGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileWriter archivos;
				if (fichero != null) {
					try {
						archivos = new FileWriter(fichero);
						BufferedWriter buff = new BufferedWriter(archivos);
						buff.write(campoCodigo.getText());
						buff.close();
						JOptionPane.showMessageDialog(null, "Se ha guardado correctamente");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "No se ha realizado la operación");
					}
				} else {
					JOptionPane.showMessageDialog(null, "No se ha realizado la operación");
				}
			}
		});
		mnArchivo.add(mntmGuardar);
		
		mntmGuardarComo = new JMenuItem("Guardar como");
		mntmGuardarComo.addActionListener(this);
		mnArchivo.add(mntmGuardarComo);
		
		mnCompilar = new JMenu("Compilar");
		menuBar.add(mnCompilar);
		
		mntmRunAs = new JMenuItem("Run as");
		mntmRunAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				contarLineas();
				System.out.println("Hkakhakshka");
				if(!(campoCodigo.getText().trim().equals(""))) {
					
					try {
						InputStream stream = new ByteArrayInputStream(campoCodigo.getText().getBytes("UTF-8"));
						analizadorLexico = new AnalizadorLexico(stream);
						analizadorLexico.TokenList();
						
					} catch (ParseException v) {
						// TODO Auto-generated catch block
						v.printStackTrace();
					} catch (UnsupportedEncodingException v) {
						// TODO Auto-generated catch block
						v.printStackTrace();
					}
				
					
				}else {
					
					JOptionPane.showMessageDialog(null, "Ingrese codigo valido!", "Error", JOptionPane.ERROR_MESSAGE);	
					}
			}
		});
		mnCompilar.add(mntmRunAs);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 58, 25, 251);
		getContentPane().add(scrollPane);
		
		campoLinea = new JTextArea();
		scrollPane.setViewportView(campoLinea);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		temporal="";
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
	     
		if (event.getSource() == mntmGuardarComo) {
			try {
				
				/** llamamos el metodo que permite cargar la ventana */
				String ruta;
				JFileChooser file = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(".HUQ", "huq");
				file.setFileFilter(filter);
				if (file.showSaveDialog(null) == file.APPROVE_OPTION) {
					ruta = file.getSelectedFile().getAbsolutePath();
					if (new File(ruta).exists()) {
						if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, 
							"El fichero existe,deseas reemplazarlo?", "Titulo", JOptionPane.YES_NO_OPTION)) {
							FileWriter archivos = new FileWriter(new File(ruta));
							BufferedWriter buff = new BufferedWriter(archivos);
							buff.write(campoCodigo.getText());
							buff.close();
						}
					} else {
						FileWriter archivos = new FileWriter(fichero = new File(ruta + ".huq"));
						BufferedWriter buff = new BufferedWriter(archivos);
						buff.write(campoCodigo.getText());
						buff.close();
					}
				}
				JOptionPane.showMessageDialog(null, "Guardado");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		
		
		}
			
		
		
		
		
		
				
			
		
		
	}

	
	public static void escribirResultado(int n, String token, String lexema, int nL, int nC) {
		temporal += n + "- Token:" + token + " Lexema:" + lexema + " Linea:" + nL + " Columna:" + nC+"\n";
		consola.setText(temporal);
	}
	private void contarLineas() {

		String a = "1\n";
		for (int i = 2; i <= campoCodigo.getLineCount(); i++) {
			a += i + "\n";
		}
		campoLinea.setText(a);

	}
}
