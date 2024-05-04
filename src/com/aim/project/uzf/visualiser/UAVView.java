package com.aim.project.uzf.visualiser;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.aim.project.uzf.UZFDomain;
import com.aim.project.uzf.instance.Location;
import com.aim.project.uzf.interfaces.UZFInstanceInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class UAVView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7066509516892991728L;

	private UAVPanel oPanel;

	private Color oEnclosureColor;

	private Color oRoutesColor;

	public UAVView(UZFInstanceInterface oInstance, UZFDomain oProblem, Color oCitiesColor, Color oRoutesColor) {

		this.oEnclosureColor = oCitiesColor;
		this.oRoutesColor = oRoutesColor;
		this.oPanel = new UAVPanel(oInstance, oProblem);
		JFrame frame = new JFrame();
		frame.setTitle("UAV Zoo Feeder Solution Visualiser");
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.add(this.oPanel);
		frame.setVisible(true);
	}

	class UAVPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1295525707913147839L;

		private UZFInstanceInterface oInstance;

		private UZFDomain oProblem;

		public UAVPanel(UZFInstanceInterface oInstance, UZFDomain oProblem) {

			this.oInstance = oInstance;
			this.oProblem = oProblem;
		}

		int map(double d, double min_x, double max_x, long out_min, long out_max) {
			return (int) ((d - min_x) * (out_max - out_min) / (max_x - min_x) + out_min);
		}

		public void updateSolution(UAVSolutionInterface[] current, UAVSolutionInterface[] candidate,
								   UAVSolutionInterface best) {

			this.repaint();
		}

		LinkedList<Color> oColorStack = new LinkedList<Color>();

		private void drawChef(Graphics g, int x, int y, int width, int height) {

			oColorStack.push(g.getColor());

			g.setColor(Color.WHITE);
			g.fillRect(x-width/2, y-height/2, width, height);

			g.setColor(Color.BLACK);

			g.drawLine(x-width/2, y-height/2,x+width/2, y+height/2);
			g.drawLine(x+width/2, y-height/2,x-width/2, y+height/2);

			g.setColor(oColorStack.pop());
		}

		public void drawUAV(UZFDomain oProblem, Graphics g) {

			int[] rep = oProblem.getBestSolutionRepresentation();
			if (rep != null) {

				Location oPrepLocation = oProblem.getLoadedInstance().getLocationOfFoodPreparationArea();

				int width = getWidth();
				int height = getHeight();

				double max_x = Integer.MIN_VALUE;
				double max_y = Integer.MIN_VALUE;
				double min_x = Integer.MAX_VALUE;
				double min_y = Integer.MAX_VALUE;

				// find min and max x and y coordinates
				max_x = Math.max(max_x, oPrepLocation.x());
				max_y = Math.max(max_y, oPrepLocation.y());
				min_x = Math.min(min_x, oPrepLocation.x());
				min_y = Math.min(min_y, oPrepLocation.y());

				for (int i : rep) {

					Location l = oInstance.getLocationForEnclosure(rep[i]);
					max_x = Math.max(max_x, l.x());
					max_y = Math.max(max_y, l.y());
					min_x = Math.min(min_x, l.x());
					min_y = Math.min(min_y, l.y());
				}

				// draw food prep to first enclosure
				int x1, x2, y1, y2;
				Location l1 = oPrepLocation, l2 = oInstance.getLocationForEnclosure(rep[0]);
				x1 = map(l1.x(), min_x, max_x, 10, width - 10);
				x2 = map(l2.x(), min_x, max_x, 10, width - 10);
				y1 = height - map(l1.y(), min_y, max_y, 10, height - 10);
				y2 = height - map(l2.y(), min_y, max_y, 10, height - 10);

				g.setColor(Color.YELLOW);
				g.drawLine(x1, y1, x2, y2);

				g.setColor(oEnclosureColor);
				g.fillOval(x1 - 2, y1 - 2, 4, 4);

				// draw enclosure routes
				for (int i = 0; i < rep.length - 1; i++) {

					l1 = oInstance.getLocationForEnclosure(rep[i]);
					l2 = oInstance.getLocationForEnclosure(rep[i + 1]);

					x1 = map(l1.x(), min_x, max_x, 10, width - 10);
					x2 = map(l2.x(), min_x, max_x, 10, width - 10);
					y1 = height - map(l1.y(), min_y, max_y, 10, height - 10);
					y2 = height - map(l2.y(), min_y, max_y, 10, height - 10);

					g.setColor(oRoutesColor);
					g.drawLine(x1, y1, x2, y2);

					g.setColor(oEnclosureColor);
					g.fillOval(x1 - 2, y1 - 2, 4, 4);
				}

				// draw route from last enclosure to food preparation area
				l1 = oInstance.getLocationForEnclosure(rep[rep.length - 1]);
				l2 = oPrepLocation;
				x1 = map(l1.x(), min_x, max_x, 10, width - 10);
				x2 = map(l2.x(), min_x, max_x, 10, width - 10);
				y1 = height - map(l1.y(), min_y, max_y, 10, height - 10);
				y2 = height - map(l2.y(), min_y, max_y, 10, height - 10);

				g.setColor(Color.YELLOW);
				g.drawLine(x1, y1, x2, y2);

				g.setColor(oEnclosureColor);
				g.setColor(oEnclosureColor);
				g.fillOval(x1 - 2, y1 - 2, 4, 4);

				drawChef(g, x2, y2, 12, 12);

			} else {
				g.setColor(Color.WHITE);
				System.out.println("Unsupported");
				g.drawString("Unsupported solution representation...", (int) (0), (int) (getHeight() / 2.0));
			}
		}

		public void paintComponent(Graphics g) {

			super.paintComponent(g);

			int width = getWidth();
			int height = getHeight();

			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);

			if (oProblem != null) {
				this.drawUAV(oProblem, g);
			}

			g.dispose();

		}
	}
}
