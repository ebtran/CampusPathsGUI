package hw9;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class GUIView {
	RouteWrapper w;
	JPanel panel_right;
	
	RouteWrapper getRouteWrapper() {
		return w;
	}
	
	GUIView() {
		w = new RouteWrapper();
		JFrame frame = new JFrame("RPI Route Finder");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		JLabel label_b1 = new JLabel("Start Building"); // Start label
		
		final JComboBox<String> combo_b1 = combo_box(1); // Start combo box
		
		JLabel label_b2 = new JLabel("End Building"); // End label
		
		final JComboBox<String> combo_b2 = combo_box(2); // End combo box
		
		JButton button_reset = new JButton("Reset"); // Reset button
		button_reset.setActionCommand("reset");
		button_reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				combo_b1.setSelectedIndex(0);
				w.updatePoint(1,"Select a building");
				combo_b2.setSelectedIndex(0);
				w.updatePoint(2,"Select a building");
				panel_right.repaint();
			}
		});
		
		JPanel panel_left = new JPanel();
		panel_left.setLayout(new BoxLayout(panel_left, BoxLayout.X_AXIS));
		panel_left.add(label_b1);
		panel_left.add(Box.createRigidArea(new Dimension(5,0)));
		panel_left.add(combo_b1);
		panel_left.add(Box.createRigidArea(new Dimension(5,0)));
		panel_left.add(label_b2);
		panel_left.add(Box.createRigidArea(new Dimension(5,0)));
		panel_left.add(combo_b2);
		panel_left.add(Box.createRigidArea(new Dimension(5,0)));
		panel_left.add(button_reset);

		panel_right = new MapImage();
		panel_right.setPreferredSize(new Dimension(650,650));
		
		frame.add(panel_left, BorderLayout.NORTH);
		frame.add(panel_right, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

	JComboBox<String> combo_box(Integer order) {
		JComboBox<String> cb = new JComboBox<String>(w.getBuildingOptions());
		if (order.equals(1)) {
			cb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JComboBox cb = (JComboBox)e.getSource();
					w.updatePoint(1, (String) (cb.getSelectedItem()));
					panel_right.repaint();
				}
			});
		} else {
			cb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JComboBox cb = (JComboBox)e.getSource();
					w.updatePoint(2, (String) (cb.getSelectedItem()));
					panel_right.repaint();
				}
			});
		}
		return cb;
	}
}

class MapImage extends JPanel {
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("hw9/data/RPI_campus_map_2010_extra_nodes_edges.png"));
		} catch (IOException e) {
			System.err.println("File not found");
		}
		
		g2.drawImage(img,119,0,119+650,650,88,40,88+2000,40+2000,null);
		
		g2.setStroke(new BasicStroke(3));
		g2.setColor(Color.BLACK);
		if (RouteWrapper.x_coords != null && RouteWrapper.y_coords != null) {
			for (int i = 1; i < RouteWrapper.x_coords.size(); ++i) {
				g2.draw(new Line2D.Double(scaleX(RouteWrapper.x_coords.get(i-1)),scaleY(RouteWrapper.y_coords.get(i-1)),
						scaleX(RouteWrapper.x_coords.get(i)),scaleY(RouteWrapper.y_coords.get(i))));
			}
		}

		if (RouteWrapper.b1 != null) {
			g2.setColor(Color.BLACK);
			g2.fillOval(scaleX(RouteWrapper.b1.x())-7,scaleY(RouteWrapper.b1.y())-7,14,14);
			g2.setColor(Color.RED);
			g2.fillOval(scaleX(RouteWrapper.b1.x())-4,scaleY(RouteWrapper.b1.y())-4,8,8);
		}

		if (RouteWrapper.b2 != null) {
			g2.setColor(Color.BLACK);
			g2.fillOval(scaleX(RouteWrapper.b2.x())-7,scaleY(RouteWrapper.b2.y())-7,14,14);
			g2.setColor(Color.GREEN);
			g2.fillOval(scaleX(RouteWrapper.b2.x())-4,scaleY(RouteWrapper.b2.y())-4,8,8);
		}
	}
	
	static Integer scaleX(Integer x) {
		return (int)(650*((float)x-88))/2000+119;
	}
	
	static Integer scaleY(Integer y) {
		return (int)(650*((float)y-40))/2000;
	}
	
}