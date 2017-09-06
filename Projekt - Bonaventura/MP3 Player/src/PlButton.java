import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ButtonModel;
import javax.swing.JButton;

public class PlButton extends JButton {
	
	private Color puvodniBarva = new Color(255,0,0);
	private Color stinBarva = new Color(255, 0, 51);
	private Color klikBarva = new Color(0, 0, 0);
	private GradientPaint GP;

	public PlButton(String text) {
		super();
		setText(text);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFont(new Font("MujFont", Font.BOLD, 12));
		setForeground(Color.WHITE);
		setFocusable(false);

	}

	public PlButton() {
		setForeground(Color.WHITE);
		setFocusable(false);
		setContentAreaFilled(false);
		setBorderPainted(false);

	}

	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int h = getHeight();
		ButtonModel model = getModel();

		g2d.setPaint(GP);
		if (model.isPressed()) {
			GP = new GradientPaint(0, 0, klikBarva, 0, h, klikBarva, true);
			g2d.setPaint(GP);
		} else {
			GP = new GradientPaint(0, 0, puvodniBarva, 0, h, stinBarva, true);
		}
		RenderingHints hints = new RenderingHints(null);
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(hints);
		g2d.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
		
		super.paintComponent(g2d);
		repaint();
	}

}