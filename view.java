package t1_fpi;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;

public class view extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					view frame = new view();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void copyFiles(File source, File destination) throws IOException {
		Files.copy(source.toPath(), destination.toPath());
	}

	/**
	 * Create the frame.
	 */
	public view() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 702, 535);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		textField = new JTextField();

		
		JButton btnNewButton = new JButton("Open file");
		contentPane.add(btnNewButton);
		
		JLabel labelImagem = new JLabel("");
		contentPane.add(labelImagem);
		
		JButton saveButton = new JButton("Save file with new name");
		JLabel lblNewLabel = new JLabel("New file name");
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser filechooser = new JFileChooser();
				filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image", "jpg","jpeg");
				
				filechooser.setFileFilter(filter);
				int retorno = filechooser.showOpenDialog(contentPane);
				
				if (retorno == JFileChooser.APPROVE_OPTION) {
					File file = filechooser.getSelectedFile();
					labelImagem.setIcon(new ImageIcon(file.getPath()));
					contentPane.add(lblNewLabel);
					contentPane.add(textField);
					contentPane.add(saveButton);
					textField.setColumns(10);
					
					
					saveButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JLabel labelError = new JLabel("");
							contentPane.add(labelError);
							File newfile = new File(file.getParentFile().getAbsolutePath() + "\\" + textField.getText() + ".jpg");
							labelError.setText("Image saved at " + file.getParentFile().getAbsolutePath() + "\\" + textField.getText() + ".jpg");
							try {
								copyFiles(file, newfile);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								labelError.setText("erro ao salvar imagem");
							}
						}
					});
				}
			}
		});
		
		
	}
}
