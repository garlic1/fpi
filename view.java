package t1_fpi;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

public class view extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	public static void copyFiles(File source, File destination) throws IOException {
		Files.copy(source.toPath(), destination.toPath());
	}

	private static BufferedImage fileToBufferedImage(File source) {
		BufferedImage image;
		try {
			image = ImageIO.read(source);
//			BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
			return image;
		}catch(IOException e) {
			return null;
		}
	}
	
	private static File bufferedImageToFile(BufferedImage source, String filename) throws IOException {
		File outputFile = new File(System.getProperty("user.dir") + "\\" + filename);
		ImageIO.write(source, "jpg", outputFile);
		return outputFile;
	}
	
	private static BufferedImage readFile(String filename) throws IOException {
		File source = new File(System.getProperty("user.dir") + "\\" + filename);
		System.out.println(System.getProperty("user.dir") + "\\" + filename);
		BufferedImage image = ImageIO.read(source);
		return image;
	}
	
	public static void saveFile(BufferedImage source, String outputName) throws IOException {
		File outputFile = new File(System.getProperty("user.dir") + "\\" + outputName + ".jpg");
		ImageIO.write(source, "jpg", outputFile);
	}
	
	
	public static BufferedImage getGrayscaleImage(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int currentColor;
		
		for(int y = 0; y<height ; y++) {
			for (int x = 0; x<width; x++) {
				currentColor = image.getRGB(x, y);
				
				// mascaras necessarias para extrair r g e b
				int red = (currentColor & 0xff0000) >> 16;
				int green = (currentColor & 0xff00) >> 8;
				int blue = currentColor & 0xff;
				
		        // normalizacao e correcao de gama
		        float red_normal = (float) Math.pow(red / 255.0, 2.2);
		        float green_normal = (float) Math.pow(green / 255.0, 2.2);
		        float blue_normal = (float) Math.pow(blue / 255.0, 2.2);
				
				float luminance = (float) (0.299 * red_normal + 0.587 * green_normal + 0.114 * blue_normal);
				
		        int grayLevel = (int) (255* Math.pow(luminance, 1.0 / 2.2));
		        int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
				
				image.setRGB(x, y, gray);
			}
		}
		
		return image;
	}
	
	private static BufferedImage getHorizontallyFlippedImage(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int currentColor;
		int invertedPixel = 0;
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		for(int y = 0; y<height ; y++) {
			for (int x = width - 1; x >= 0; x--) {
				currentColor = image.getRGB(x, y);
				newImage.setRGB(invertedPixel, y, currentColor);
				invertedPixel++;
			}
			invertedPixel = 0;
		}

		return newImage;
	}
	
	private static BufferedImage getVerticallyFlippedImage(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int currentColor;
		int invertedPixel = 0;
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		for (int x = 0; x < width; x++) {
			for(int y = height -1; y >= 0 ; y--) {
				currentColor = image.getRGB(x, y);
				
				newImage.setRGB(x, invertedPixel, currentColor);
				invertedPixel++;
			}
			invertedPixel = 0;
		}

		return newImage;
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
		
		JButton btnNewButton = new JButton("Open file to duplicate");
		contentPane.add(btnNewButton);
		
		JButton btnHorizontal = new JButton("Horizontal flip image");
		contentPane.add(btnHorizontal);
		
		JButton btnVertical = new JButton("Vertical flip image");
		contentPane.add(btnVertical);
		
		JButton btnGrayscale = new JButton("Grayscale image");
		contentPane.add(btnGrayscale);
		
		JLabel labelImagem = new JLabel("");
		contentPane.add(labelImagem);
		
		JButton saveButton = new JButton("Duplicate file");
		JLabel lblNewLabel = new JLabel("New file name");
		JLabel labelError = new JLabel();
		
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
							contentPane.add(labelError);
							File newfile = new File(file.getParentFile().getAbsolutePath() + "\\" + textField.getText() + ".jpg");
							try {
								copyFiles(file, newfile);
								labelError.setText("Image saved at " + file.getParentFile().getAbsolutePath() + "\\" + textField.getText() + ".jpg");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								labelError.setText("erro ao salvar imagem");
							}
						}
					});
				}
			}
		});
		
		btnHorizontal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				labelError.setText("");
				contentPane.remove(lblNewLabel);
				contentPane.remove(textField);
				contentPane.remove(saveButton);
				JFileChooser filechooser = new JFileChooser();
				filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				BufferedImage image;
				
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image", "jpg","jpeg");
				
				filechooser.setFileFilter(filter);
				int retorno = filechooser.showOpenDialog(contentPane);
				
				if (retorno == JFileChooser.APPROVE_OPTION) {
					File file = filechooser.getSelectedFile();
					image = fileToBufferedImage(file);
					image = getHorizontallyFlippedImage(image);
					
					try {
						saveFile(image, "horizontal_flip_output");
						contentPane.add(labelError);
						labelError.setText("Image saved at " + System.getProperty("user.dir") + "\\" + "horizontal_flip_output" + ".jpg");
					} catch(IOException eh) {
						labelError.setText("Erro ao salvar horizontal flip");
					}
					
					labelImagem.setIcon(new ImageIcon(image));
				}
			}
		});
		
		btnVertical.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser filechooser = new JFileChooser();
				BufferedImage image;
				filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image", "jpg","jpeg");
				
				filechooser.setFileFilter(filter);
				int retorno = filechooser.showOpenDialog(contentPane);
				
				if (retorno == JFileChooser.APPROVE_OPTION) {
					File file = filechooser.getSelectedFile();
					image = fileToBufferedImage(file);
					image = getVerticallyFlippedImage(image);
					
					try {
						saveFile(image, "vertical_flip_output");
						contentPane.add(labelError);
						labelError.setText("Image saved at " + System.getProperty("user.dir") + "\\" + "vertical_flip_output" + ".jpg");
					}catch(IOException ev) {
						labelError.setText("Erro ao salvar vertical flip");
					}
					labelImagem.setIcon(new ImageIcon(image));
					
				}
			}
		});
		
		btnGrayscale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser filechooser = new JFileChooser();
				BufferedImage image;
				filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image", "jpg","jpeg");
				
				filechooser.setFileFilter(filter);
				int retorno = filechooser.showOpenDialog(contentPane);
				
				if (retorno == JFileChooser.APPROVE_OPTION) {
					File file = filechooser.getSelectedFile();
					image = fileToBufferedImage(file);
					image = getGrayscaleImage(image);
					
					try {
						saveFile(image, "grayscale");
						contentPane.add(labelError);
						labelError.setText("Image saved at " + System.getProperty("user.dir") + "\\" + "grayscale" + ".jpg");
					}catch(IOException ev) {
						labelError.setText("Erro ao salvar grayscale");
					}
					
					labelImagem.setIcon(new ImageIcon(image));
				}
			}
		});
	}
	
	
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
}
