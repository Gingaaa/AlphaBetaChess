import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class UserInterface extends JPanel implements MouseListener, MouseMotionListener
{
	static int mouseX, mouseY, newMouseX, newMouseY;
	static int squareSize=32;
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		this.setBackground(Color.YELLOW);
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);		
				
		//create the checkered board
		for(int i=0;i<64;i+=2)
		{
			g.setColor(new Color(255,200,100));
			g.fillRect((i%8+(i/8)%2)*squareSize, (i/8)*squareSize, squareSize, squareSize);
			g.setColor(new Color(150,50,30));
			g.fillRect(((i+1)%8-((i+1)/8)%2)*squareSize, ((i+1)/8)*squareSize, squareSize, squareSize);
		}
		//draw the chess pieces initially
		Image chessPiecesImage;
		chessPiecesImage = new ImageIcon("ChessPieces.png").getImage();
		
		for(int i=0;i<64;i++)
		{
			int j=-1, k=-1;			
			switch(AlphaBetaChess.chessBoard[i/8][i%8]) //to eliminate a nested loop
			{
				case "P": j=5; k=0;
					break;
				case "p": j=5; k=1;
					break;
				case "R": j=2; k=0;
					break;
				case "r": j=2; k=1;
				break;
				case "K": j=4; k=0;
					break;
				case "k": j=4; k=1;
				break;
				case "B": j=3; k=0;
					break;
				case "b": j=3; k=1;
				break;
				case "Q": j=1; k=0;
					break;
				case "q": j=1; k=1;
				break;
				case "A": j=0; k=0;
					break;
				case "a": j=0; k=1;
				break;
			}
			if(j!=-1 && k!=-1)
			{
				g.drawImage(chessPiecesImage, (i%8)*squareSize, (i/8)*squareSize, (i%8+1)*squareSize, (i/8+1)*squareSize, j*64, k*64, (j+1)*64, (k+1)*64, this);
			}					
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) 
	{
	}
	@Override
	public void mousePressed(MouseEvent e) 
	{}
	@Override
	public void mouseReleased(MouseEvent e) 
	{}	
	@Override
	public void mouseClicked(MouseEvent e) 
	{}	
	@Override
	public void mouseDragged(MouseEvent e) 
	{}	
	@Override
	public void mouseEntered(MouseEvent e) 
	{}	
	@Override
	public void mouseExited(MouseEvent e) 
	{}
	
	
	
	
	
	
	
	
	
	
	
}
