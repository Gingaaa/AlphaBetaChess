import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class UserInterface extends JPanel implements MouseListener, MouseMotionListener
{
	static int x=0,y=0;
	
	
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		this.setBackground(Color.YELLOW);
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);		
				
		Image chessPiecesImage; 
		chessPiecesImage = new ImageIcon("ChessPieces.png").getImage();
		g.drawImage(chessPiecesImage, x, y, x+64, y+64, 0, 0, 64, 64, this);
	}
	
	public void mouseMoved(MouseEvent e) 
	{
		x = e.getX();
		y = e.getY();
		repaint();
	}	
	public void mousePressed(MouseEvent e) 
	{}
	public void mouseReleased(MouseEvent e) 
	{}	
	public void mouseClicked(MouseEvent e) 
	{}	
	public void mouseDragged(MouseEvent e) 
	{}	
	public void mouseEntered(MouseEvent e) 
	{}	
	public void mouseExited(MouseEvent e) 
	{}
	
	
	
	
	
	
	
	
	
	
	
}
