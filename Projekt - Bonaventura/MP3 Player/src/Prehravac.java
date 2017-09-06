import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.Painter;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Vector;

import javax.swing.JSlider;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;
import java.awt.CardLayout;
import java.awt.Toolkit;

class Prehravac extends JFrame {

	private Media media;
	private MediaPlayer mediaPlayer;
	private Pozadi vyplnObsah;
	private JButton plButton;
	private JButton buttonPlay;
	private JButton buttonStop;
	private File song;
	private JSlider slider;
	private JSlider prehravaciSlider;
	private Vector<File> seznam;
	private JList playList;
	private JScrollPane vypln;
	private DefaultListModel model;
	private boolean novySoubor;
	private boolean novaVolba;
	private boolean opakuj;
	private boolean songHraje;
	private JPanel panel;
	private BufferedImage obrazekSkladby;
	private JPanel panel1;
	private JLabel nadpis;
	private JLabel nadpis2;
	private URI uri;
	private JLabel zbyvajiciCas;
	private JLabel momentalniCas;
	private int indexSongu;
	private Slider controlPanel;
	private boolean smazany;
	private Vector<Integer> nahodnostIndex;
	private Buttons nahodne;
	private Buttons ztlum;
	private int predchoziHodnotaHlasitosti;
	private int hlasitost = 50;
	boolean jeNahodne;
	private JLabel sliderButton;
    public static void main(String args[]) {
		for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
			if ("Nimbus".equals(laf.getName())) {
				try {
					UIManager.setLookAndFeel(laf.getClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		new Prehravac();

	}

	Prehravac() {
		setTitle("Java Player");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Prehravac.class.getResource("freeicon.png")));
		setResizable(false);
		setBackground(Color.BLACK);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);

		player();
		pridej();
		playButton();
		playList();

		hlasitost();
		ProgAndSeek();
		tamZpet();
		stopButton();
		opakuj();
		playlistDalsi();
		nahodne();
		ztlumZvuk();
		vyplnObsah.repaint();

	}

	void player() {
		vyplnObsah = new Pozadi();
		setBounds(100, 100, 538, 354);

		this.setVisible(true);
		setContentPane(vyplnObsah);
		vyplnObsah.setLayout(null);
		vypln = new JScrollPane(playList);
		vypln.setVisible(false);

		vyplnObsah.setBackground(new Color(0, 0, 128));
		panel = new JPanel();
		panel.setBounds(0, 214, 511, 110);

		controlPanel = new Slider();
		controlPanel.setLocation(0, 217);
		controlPanel.setSize(535, 107);
		controlPanel.setLayout(new CardLayout(0, 0));
		controlPanel.add(panel, "name_37092408351471");

		vyplnObsah.add(controlPanel);
	}

	void pridej() {
		plButton = new Buttons("PlayList");
		plButton.setBounds(428, 45, 80, 23);
		plButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vypln.setVisible(!vypln.isVisible());
			}
		});
		panel.setLayout(null);
	
		panel.add(plButton);

	}

	void playList() {
		seznam = new Vector<File>();
		model = new DefaultListModel();
		playList = new JList(model);
		playList.setSize(71, 83);
		playList.setLocation(338, 25);
		playList.setDragEnabled(false);
		playList.setFocusable(true);

		playList.setTransferHandler(new Playlist(playList, model, seznam));

		vypln.setViewportView(playList);
		vypln.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		vypln.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		vypln.setOpaque(true);
		vypln.setSize(130, 217);
		vypln.setLocation(405, 0);

		DefaultListSelectionModel m = new DefaultListSelectionModel();
		m.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		playList.setSelectionModel(m);
		playList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				song = seznam.get(playList.getSelectedIndex());
				indexSongu = playList.getSelectedIndex();
				novaVolba = true;
				if (song != null) {
					smazany = false;
					hraj();
				} else {
					mediaPlayer.stop();
					buttonPlay.setText("►");
					songHraje = false;
				}
			}
		});

		vyplnObsah.add(vypln);

		panel1 = new JPanel();
		panel1.setBounds(0, 0, 535, 50);
		panel1.setBackground(new Color(0, 0, 0, 100));
		vyplnObsah.add(panel1);
		panel1.setLayout(null);
		{
			nadpis = new JLabel("MP3 Player");
			nadpis.setForeground(Color.WHITE);
			nadpis.setHorizontalAlignment(SwingConstants.LEFT);
			nadpis.setVerticalAlignment(SwingConstants.TOP);
			nadpis.setFont(new Font("Times New Roman", Font.BOLD, 20));
			nadpis.setBounds(10, 0, 480, 23);

			panel1.add(nadpis);
		}
		{
			nadpis2 = new JLabel("");
			nadpis2.setForeground(Color.WHITE);
			nadpis2.setVerticalAlignment(SwingConstants.TOP);
			nadpis2.setFont(new Font("Times New Roman", Font.ITALIC, 16));
			nadpis2.setBounds(10, 23, 480, 27);
			panel1.add(nadpis2);
		}
	}

	void playlistDalsi()

	{
		playList.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					if (playList.getSelectedIndex() == 0 && seznam.size() == 0)
						return;

					if (playList.getSelectedIndex() >= 0 && mediaPlayer != null) {
						if (seznam.size() > 1)
							hrajDalsi();
						else {
							vyplnObsah.setImage(null);
							smazany = true;
							mediaPlayer.stop();
							buttonPlay.setText("►");
							songHraje = false;

							nadpis.setText("MP3 Player");
							nadpis2.setText("");
							vyplnObsah.repaint();

						}
						seznam.removeElementAt(playList.getSelectedIndex());
						model.removeElementAt(playList.getSelectedIndex());
					}
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});
	}

	void playButton() {
		buttonPlay = new PlButton("►");
		buttonPlay.setFont(new Font("Dialog", Font.BOLD, 26));

		buttonPlay.setBounds(240, 30, 54, 54);
		buttonPlay.setPreferredSize(new Dimension(130, 28));

		buttonPlay.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				hraj();
			}
		});
		panel.add(buttonPlay);
	}

	void hraj() {
		
		if (smazany)
			return;
		if (songHraje && !novaVolba) {
			buttonPlay.setText("►");
			mediaPlayer.pause();
			songHraje = false;
			return;
		}
		if (novaVolba) {
			novySoubor = true;
			novaVolba = false;
	
			Mp3File toto = null;
			ID3v2 tag = null;
			ID3v1 tag2 = null;
			try {
				toto = new Mp3File(song.getAbsolutePath());
				if (toto.hasId3v2Tag()) {
					tag = toto.getId3v2Tag();
					// Image
					byte[] imageData = tag.getAlbumImage();
					if (imageData != null)
						obrazekSkladby = ImageIO.read(new ByteArrayInputStream(imageData));
					else
						obrazekSkladby = null;
					// Titulek a hudebnik
					nadpis.setText(tag.getTitle());
					nadpis2.setText(tag.getArtist());
			
				} else if (toto.hasId3v1Tag()) {
					tag2 = toto.getId3v2Tag();
					// bez obrazku
					obrazekSkladby = null;
					
					nadpis.setText(tag.getTitle());
					nadpis2.setText(tag.getArtist());
					
				}

			} catch (UnsupportedTagException | InvalidDataException | IOException e1) {
				e1.printStackTrace();
			}

		}

		if (mediaPlayer != null && !novySoubor) {
			mediaPlayer.play();
			buttonPlay.setText("ll");
			songHraje = true;
			return;
		}
		if (mediaPlayer != null)
			mediaPlayer.stop();
		if (seznam.size() == 1)
			song = seznam.get(0);
		new JFXPanel();
		uri = song.toURI();
		uri.toASCIIString();
		String path = uri.toString();
		media = new Media(path);

		vyplnObsah.setImage(obrazekSkladby);
		mediaPlayer = new MediaPlayer(media);
		vyplnObsah.repaint();

		mediaPlayer.setOnReady(new Runnable() {
			public void run() {

				buttonPlay.setText("ll");
				mediaPlayer.play();
				songHraje = true;
				novySoubor = false;
				prehravaciSlider.setMaximum((int) media.getDuration().toSeconds());
				prehravaciSlider.setValue(0);
				Duration t;

				while (!novySoubor && mediaPlayer.getCurrentTime().toSeconds() < media.getDuration().toSeconds()) {
					
					prehravaciSlider.setValue((int) mediaPlayer.getCurrentTime().toSeconds());
					
					t = mediaPlayer.getTotalDuration().subtract(mediaPlayer.getCurrentTime());
					zbyvajiciCas.setText(
							Integer.toString((int) t.toMinutes()) + ":" + Integer.toString((int) t.toSeconds() % 60));
					momentalniCas.setText(Integer.toString((int) mediaPlayer.getCurrentTime().toMinutes()) + ":"
							+ Integer.toString((int) mediaPlayer.getCurrentTime().toSeconds() % 60));
					

				}
				if (!novySoubor) {
					try {
						Thread.sleep(4000); 
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
					if (opakuj) {
						novaVolba = true;
						hraj();
					} else {
						indexSongu++;
						indexSongu = indexSongu % (seznam.size());
						if(!jeNahodne)
						song = seznam.get(indexSongu);
						else 
							song = seznam.get(nahodnostIndex.get(indexSongu));
						novaVolba = true;
						hraj();
					}
				} else {
					novySoubor = true;
					songHraje = false;
					buttonPlay.setText("Play");
				}

			}
		});
	
		zmenaHlasitosti();
	}

	void stopButton() {
		buttonStop = new Buttons("▊");
		buttonStop.setHorizontalAlignment(SwingConstants.RIGHT);
		buttonStop.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonStop.setBounds(304, 46, 80, 23);
		buttonStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mediaPlayer.stop();
				buttonPlay.setText("►");
				songHraje = false;
			}
		});
		panel.add(buttonStop);
	}

	void nahodne() {
		nahodne = new Buttons("🔀       ");
		nahodne.setFont(new Font("Segoe UI Symbol", Font.BOLD, 12));
		nahodne.setSize(75, 23);
		nahodne.setLocation(114, 46);
		jeNahodne=false;
		nahodne.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				if (!jeNahodne) {
					jeNahodne=true;
					nahodnostIndex= new Vector<Integer>();
					nahodnostIndex.setSize(seznam.size());
					Random rnd = new Random();
					int index, a;
					for (int i = 0; i < nahodnostIndex.size(); i++)
						nahodnostIndex.set(i, i);
					
					for (int i = seznam.size() - 1; i > 0; i--) {
						index = rnd.nextInt(i + 1);
						
						a = nahodnostIndex.elementAt(index);
						nahodnostIndex.set(index, nahodnostIndex.get(i));
						nahodnostIndex.set(i, a);
					}
					nahodne.vymenBarvu();
				}
				else {
					jeNahodne=false;
					nahodne.vymenBarvu();
					
				}
			}
		});
		panel.add(nahodne);

	}
	
	void hlasitost() {

		UIDefaults sliderDefaults = new UIDefaults();

		sliderDefaults.put("Slider.thumbWidth", 20);
		sliderDefaults.put("Slider.thumbHeight", 20);
		sliderDefaults.put("Slider:SliderThumb.backgroundPainter", new Painter<JComponent>() {
			public void paint(Graphics2D g, JComponent c, int w, int h) {
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setStroke(new BasicStroke(2f));
				g.setColor(Color.WHITE);
				g.fillOval(1, 1, w - 3, h - 3);
				g.setColor(Color.darkGray);
				g.drawOval(1, 1, w - 3, h - 3);
			}
		});
		sliderDefaults.put("Slider:SliderTrack.backgroundPainter", new Painter<JComponent>() {
			public void paint(Graphics2D g, JComponent c, int w, int h) {
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setStroke(new BasicStroke(2f));
				g.setColor(Color.GRAY);
				g.fillRoundRect(0, 6, w - 1, 8, 8, 8);
				g.setColor(Color.WHITE);
				g.drawRoundRect(0, 6, w - 1, 8, 8, 8);
			}
		});

		slider = new JSlider();
		slider.setBounds(428, 11, 80, 31);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		sliderButton = new JLabel("Hlasitost: " + slider.getValue() + "%");
		sliderButton.setBounds(428, 3, 80, 14);
		panel.add(sliderButton);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (mediaPlayer == null)
				{
					hlasitost= slider.getValue();
					slider.setValue(slider.getValue());
				}
				else
				{
					hlasitost= slider.getValue();
					mediaPlayer.setVolume((double) (slider.getValue() / 100.0));
				}
				sliderButton.setText("Volume: " + slider.getValue() + "%");
			}
		});
		panel.add(slider);

	}
	
	void zmenaHlasitosti() {
		slider.setValue(hlasitost);
		mediaPlayer.setVolume((double) (slider.getValue() / 100.0));
	}
	
	void ztlumZvuk() {
		
		ztlum = new Buttons("    🔊");
		ztlum.setFont(new Font("Segoe UI Symbol", Font.BOLD, 12));
		ztlum.setSize(64, 23);
		ztlum.setLocation(355, 46);
		ztlum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(slider.getValue()>0)
				{
					ztlum.setText("    🔇");
					predchoziHodnotaHlasitosti= slider.getValue();
					slider.setValue(0);
					sliderButton.setText("Volume: " + slider.getValue() + "%");
					mediaPlayer.setVolume(0);
					ztlum.vymenBarvu();
				}
				else
				{
					ztlum.setText("    🔊");				
					slider.setValue(predchoziHodnotaHlasitosti);
					sliderButton.setText("Volume: " + slider.getValue() + "%");
					mediaPlayer.setVolume((double) (slider.getValue() / 100.0));
					ztlum.vymenBarvu();
				}
			}
		});
		panel.add(ztlum);
	}


	void ProgAndSeek() {
		
		UIDefaults sliderDefaults = new UIDefaults();

		sliderDefaults.put("Slider.thumbWidth", 20);
		sliderDefaults.put("Slider.thumbHeight", 20);
		sliderDefaults.put("Slider:SliderThumb.backgroundPainter", new Painter<JComponent>() {
			public void paint(Graphics2D g, JComponent c, int w, int h) {
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setStroke(new BasicStroke(2f));
				// ========================================
				System.setProperty("myColor1", "0XFF0000");
				g.setColor(Color.getColor("myColor1"));
				// ========================================
				g.fillOval(1, 1, w - 3, h - 3);
				System.setProperty("myColor2", "0XCCCCCC");
				g.setColor(Color.getColor("myColor2"));
				g.drawOval(1, 1, w - 3, h - 3);
			}
		});
		sliderDefaults.put("Slider:SliderTrack.backgroundPainter", new Painter<JComponent>() {
			public void paint(Graphics2D g, JComponent c, int w, int h) {
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setStroke(new BasicStroke(2f));
				g.setColor(Color.darkGray);
				g.fillRoundRect(0, 6, w - 1, 8, 8, 8);
				g.setColor(Color.WHITE);
				g.drawRoundRect(0, 6, w - 1, 8, 8, 8);
			}
		});
		prehravaciSlider = new JSlider();
		prehravaciSlider.setBounds(58, 7, 300, 23);

		prehravaciSlider.putClientProperty("Nimbus.Overrides", sliderDefaults);
		prehravaciSlider.putClientProperty("Nimbus.Overrides.InheritDefaults", false);
	
		prehravaciSlider.setValue(0);

		prehravaciSlider.setVisible(true);
		
		prehravaciSlider.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int mouseX = e.getX();
				int value = (int) Math
						.round(((double) mouseX / (double) prehravaciSlider.getWidth()) * prehravaciSlider.getMaximum());
				prehravaciSlider.setValue(value);

				Duration t;
				t = mediaPlayer.getTotalDuration();
				mediaPlayer.seek(t.multiply(value / (double) prehravaciSlider.getMaximum()));
			}
		});
		
		panel.add(prehravaciSlider);
		counter();
	}

	void counter() {
		
	zbyvajiciCas = new JLabel("");
	zbyvajiciCas.setBounds(367, 13, 53, 14);
	panel.add(zbyvajiciCas);

	momentalniCas = new JLabel("");
	momentalniCas.setBounds(10, 13, 46, 14);
	panel.add(momentalniCas);}

	
	void hrajDalsi() {
		indexSongu++;
		indexSongu = indexSongu % (seznam.size());
		if(!jeNahodne)
			song = seznam.get(indexSongu);
			else 
				song = seznam.get(nahodnostIndex.get(indexSongu));
		novaVolba = true;
		hraj();

	}

	void hrajPredchozi() {
		indexSongu--;
		if (indexSongu < 0)
			indexSongu = seznam.size() - 1;
		if(!jeNahodne)
			song = seznam.get(indexSongu);
			else 
				song = seznam.get(nahodnostIndex.get(indexSongu));
		novaVolba = true;
		hraj();
	}

	void tamZpet() {
		JButton dopredu = new Buttons("      ►►");
		dopredu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				hrajDalsi();
			}
		});
		JButton dozadu = new Buttons("◄◄   ");
		dozadu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				hrajPredchozi();
			}
		});
		dopredu.setBounds(265, 46, 80, 23);
		dozadu.setBounds(187, 46, 75, 23);
		panel.add(dopredu);
		panel.add(dozadu);

	}

	void opakuj() {
		Buttons Repeat = new Buttons("🔁         ");
		Repeat.setFont(new Font("Segoe UI Symbol", Font.BOLD, 12));
		Repeat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (opakuj) {
					opakuj = false;
					Repeat.setFont(new Font("Segoe UI Symbol", Font.BOLD, 12));
					Repeat.setText("🔁         ");
					Repeat.vymenBarvu();
				} else {
					opakuj = true;
					Repeat.setFont(new Font("Segoe UI Symbol", Font.BOLD, 16));
					Repeat.setText("1        ");
					Repeat.vymenBarvu();
				}
			}
		});
		Repeat.setBounds(150, 46, 80, 23);
		panel.add(Repeat);
	}


	
}