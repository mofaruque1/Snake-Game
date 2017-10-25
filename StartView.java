package eecs2030_SnakeProject;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StartView {
	public static final int WIDTH = 905;
	public static final int HEIGHT = 790;

	private static JFrame obJ;


	public void createStartWindow() {
		obJ = new JFrame();
		obJ.setTitle("Snake Game");
		obJ.setIconImage(new ImageIcon("icon2.jpg").getImage());
		Gameplay gameplay = new Gameplay();
		obJ.setBounds(10, 10, 905, 790);
		obJ.setBackground(Color.gray);
		obJ.setResizable(false);
		obJ.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		obJ.add(gameplay);
		obJ.setVisible(true);

	}

}
