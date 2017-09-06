import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ButtonModel;
import javax.swing.JButton;
public class Buttons extends JButton{

	private Color puvodniBarva = new Color (0,0,0);
	private Color stinBarva = new Color(0,0,0);
	private Color klikBarva = new Color(255,255,255);;
	private int zaobleniZvenku = 35;
	private int zaobleniZevnitr = 28;
	private GradientPaint GP;

	public Buttons(String text) {
		super();
		setText(text);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFont(new Font("MujFont", Font.BOLD, 12));
		setForeground(Color.RED);
		setFocusable(false);

	}
	
	public Buttons(Color puvodniBarva, Color konecnaBarva, Color klikBarva) {
		super();
		this.puvodniBarva = puvodniBarva;
		this.stinBarva = konecnaBarva;
		this.klikBarva = klikBarva;
		setForeground(Color.WHITE);
		setFocusable(false);
		setContentAreaFilled(false);
		setBorderPainted(false);

	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int h = getHeight();
		int w = getWidth();
		ButtonModel model = getModel();
		
		g2d.setPaint(GP);
		GradientPaint p1;
		GradientPaint p2;
		
		if (model.isPressed()) {
			GP = new GradientPaint(0, 0, klikBarva, 0, h, klikBarva, true);
			g2d.setPaint(GP);
			p1 = new GradientPaint(0, 0, new Color(0, 0, 0), 0, h - 1,
					new Color(100, 100, 100));
			p2 = new GradientPaint(0, 1, new Color(0, 0, 0, 50), 0, h - 3,
					new Color(255, 255, 255, 100));
		} else {
			p1 = new GradientPaint(0, 0, new Color(100, 100, 100), 0, h - 1,
					new Color(0, 0, 0));
			p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 100), 0,
					h - 3, new Color(0, 0, 0, 50));
			GP = new GradientPaint(0, 0, puvodniBarva, 0, h, stinBarva, true);
		}
		
		RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, w - 1,
				h - 1, zaobleniZvenku, zaobleniZvenku);
		Shape clip = g2d.getClip();
		g2d.clip(r2d);
		g2d.fillRect(0, 0, w, h);
		g2d.setClip(clip);
		g2d.setPaint(p1);
		g2d.drawRoundRect(0, 0, w - 1, h - 1, zaobleniZvenku,
				zaobleniZvenku);
		g2d.setPaint(p2);
		g2d.drawRoundRect(1, 1, w - 3, h - 3, zaobleniZevnitr,
				zaobleniZevnitr);
		g2d.dispose();

		super.paintComponent(g);
		repaint();
	}
    void vymenBarvu()
    {
    	Color temp = puvodniBarva;
    	puvodniBarva=klikBarva;
    	klikBarva=temp;
    	
    	repaint();
    }
	
}
