import java.util.*;
import javax.swing.*;

public class AlphaBetaChess 
{
    static String chessBoard[][]=
    	{
        {"r","k","b","q","a","b","k","r"}, //lowercase for black
        {"p","p","p","p","p","p","p","p"},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "}, //high end chess programs use a bit board instead of arrays
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {"P","P","P","P","P","P","P","P"}, //uppercase for white
        {"R","K","B","Q","A","B","K","R"}
        };
    
    public static String alphaBeta(int depth, int beta, int alpha, String move, int player)
    {
    	//String list="1"; For debugging
    	String list = possibleMoves();
    	
    	if (depth==0 || list.length()==0) {return move+(rating()*(player*2-1));} //makes the rating negative if it is the opposing player's move or positive if it is the engines
    	/* For debugging
    	 * list="";
    	 * System.out.print("How many moves are there: ");
    	 * Scanner sc = new Scanner(System.in);
    	 * int temp = sc.nextInt();
    	 * for(int i=0;i<temp;i++)
    	 * {
    	 * 	list+="1111b";
    	 * }   	
    	*/
    	//sort later
    	player=1-player; //either 1 or 0
    	for(int i=0; i<list.length(); i+=5) //skips ahead one move
    	{
    		makeMove(list.substring(i,i+5));
    		flipBoard(); //change later maybe because this isn't very efficient
    		String returnString = alphaBeta(depth-1, beta, alpha, list.substring(i,i+5), player);
    		int value = Integer.valueOf(returnString.substring(5));
    		flipBoard();
    		undoMove(list.substring(i,i+5));
    		
    		if(player==0)
    		{
    			if(value<=beta) {beta=value; if(depth==globalDepth) {move = returnString.substring(0,5);}}
    		}else{
    			if(value>alpha) {alpha=value; if(depth==globalDepth) {move = returnString.substring(0,5);}}    			
    		}
    		if(alpha>=beta)
    		{
    			if(player==0) {return move+beta;} else {return move+alpha;}
    		}
    	}
    	//return 1234b#####... where ###=variable length score
    	if(player==0) {return move+beta;} else {return move+alpha;}
    }
    public static void flipBoard()
    {	//swap index i with index j then i+1 with j-1 and so on. Also need to change to swap the case so the move methods function correctly. Only go through half or will end up in original position
    	String temp;
    	for(int i=0; i>32; i++)
    	{
    		int r=i/8, c=i%8; //row/column
    		
    		if(Character.isUpperCase(chessBoard[r][c].charAt(0)))
    		{
    			temp = chessBoard[r][c].toLowerCase();
    		}else{
    			temp = chessBoard[r][c].toUpperCase();
    		}
    		if(Character.isUpperCase(chessBoard[7-r][7-c].charAt(0)))
    		{
    			chessBoard[r][c] = chessBoard[7-r][7-c].toLowerCase();
    		}else{
    			chessBoard[r][c] = chessBoard[7-r][7-c].toUpperCase(); 
    		}
			chessBoard[7-r][7-c] = temp;
    	}
    	int kingTemp=kingPositionC; 			
    	kingPositionC = 63-kingPositionL;		
    	kingPositionL = 63-kingTemp;			
    }
    
    public static void makeMove(String move)
    {
    	if(move.charAt(4)!='P')
    	{ //x1,y1,x2,y2,captured piece
    		chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
    		chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=" ";
    		
    		if("A".equals(chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]))
    		{ //updating the kingPosition after it has been moved (if it has been moved)
    			kingPositionC=8*Character.getNumericValue(move.charAt(2)) + Character.getNumericValue(move.charAt(3));
    		}
    	}else
    	{ //if pawn promotion | col1,col2,capturedPiece,newPiece,P	
    		chessBoard[1][Character.getNumericValue(move.charAt(0))]=" "; //it must go from row 1 to row 0
    		chessBoard[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(3));
    	}
    }
    
    public static void undoMove(String move)
    {
    	if(move.charAt(4)!='P')
    	{ //x1,y1,x2,y2,captured piece
    		chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
    		chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=String.valueOf(move.charAt(4));
    		
    		if("A".equals(chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]))
    		{ //undoing the kingPosition after it has been moved (if it has been moved)
    			kingPositionC=8*Character.getNumericValue(move.charAt(0)) + Character.getNumericValue(move.charAt(1));
    		}
    		
    	}else
    	{ //if pawn promotion | col1,col2,capturedPiece,newPiece,P	
    		chessBoard[1][Character.getNumericValue(move.charAt(0))]="P"; //it must go from row 1 to row 0
    		chessBoard[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(2));
    	}
    	
    }
    
    public static String possibleMoves()
	{
		String list = "";
		for (int i=0; i<64; i++)
		{
			switch(chessBoard[i/8][i%8]) //to eliminate a nested loop
			{
				case "P": list+=possibleP(i);
					break;
				case "R": list+=possibleR(i);
					break;
				case "K": list+=possibleK(i);
					break;
				case "B": list+=possibleB(i);
					break;
				case "Q": list+=possibleQ(i);
					break;
				case "A": list+=possibleA(i);
					break;
			}
		}
		return list; //x1,y1,x2,y2,captured piece
	}
	
	public static String possibleP(int i)
	{
		String list = "", oldPiece;
		int r = i/8, c=i%8; //row/column 
		
		for(int j=-1; j<=1; j+=2)
		{
			try //capture
				{
					if(Character.isLowerCase(chessBoard[r-1][c+j].charAt(0)) && i>=16) //always go up but can go either way
					{
						oldPiece = chessBoard[r-1][c+j];
						chessBoard[r][c] = " ";
						chessBoard[r-1][c+j] = "P";
						if(kingSafe())
							list = list+r+c+(r-1)+(c+j)+oldPiece;
						chessBoard[r][c]="P";
						chessBoard[r-1][c+j] = oldPiece;
					}
				}catch(Exception e){}
				try //capture and promotion
				{
					if(Character.isLowerCase(chessBoard[r-1][c+j].charAt(0)) && i<16) //always go up but can go either way
					{
						String[] temp = {"Q", "R", "B", "K"};
						for(int k=0; k<4; k++)
						{
							oldPiece = chessBoard[r-1][c+j];
							chessBoard[r][c] = " ";
							chessBoard[r-1][c+j] = temp[k];
							if(kingSafe())
							{	
								//column1,column2,capturedPiece,newPiece,P
								list = list+c+(c+j)+oldPiece+temp[k]+"P";
							}
							chessBoard[r][c]="P";
							chessBoard[r-1][c+j] = oldPiece;
						}
					}
				}catch(Exception e){}
		}
		try //move 1 up
		{
			if(" ".equals(chessBoard[r-1][c]) && i>=16) //always go up but can go either way
			{
				oldPiece = chessBoard[r-1][c];
				chessBoard[r][c] = " ";
				chessBoard[r-1][c] = "P";
				if(kingSafe())
					list = list+r+c+(r-1)+c+oldPiece;
				chessBoard[r][c]="P";
				chessBoard[r-1][c] = oldPiece;
			}
		}catch(Exception e){}
		try //promotion no capture
		{
			if(" ".equals(chessBoard[r-1][c]) && i<16) //always go up but can go either way
			{
				String[] temp = {"Q", "R", "B", "K"};
				for(int k=0; k<4; k++)
				{
					oldPiece = chessBoard[r-1][c];
					chessBoard[r][c] = " ";
					chessBoard[r-1][c] = temp[k];
					if(kingSafe())
					{	
						//column1,column2,capturedPiece,newPiece,P
						list = list+c+c+oldPiece+temp[k]+"P";
					}
					chessBoard[r][c]="P";
					chessBoard[r-1][c] = oldPiece;;
				}
			}
		}catch(Exception e){}
		try //move 2 up
		{
			if(" ".equals(chessBoard[r-1][c]) && " ".equals(chessBoard[r-2][c]) && i>=48) //always go up but can go either way
			{
				oldPiece = chessBoard[r-2][c];
				chessBoard[r][c] = " ";
				chessBoard[r-2][c] = "P";
				if(kingSafe())
					list = list+r+c+(r-2)+c+oldPiece;
				chessBoard[r][c]="P";
				chessBoard[r-2][c] = oldPiece;
			}
		}catch(Exception e){}
		
		
		return list;
	}
		
	public static String possibleR(int i)
	{
		String list = "", oldPiece;
		int r = i/8, c=i%8; //row/column 
		
		int temp = 1;
		for(int j=-1; j<=1; j++)
		{
			for(int k=-1; k<=1; k++)
			{	/*     j  
				 *   0 1 0
				 * k 1 0 1
				 *   0 1 0
				 */
				if(j==0 || k==0)
				{
					try
					{
						while(" ".equals(chessBoard[r+temp*j][c+temp*k]))
						{
							oldPiece = chessBoard[r+temp*j][c+temp*k];
							chessBoard[r][c] = " ";
							chessBoard[r+temp*j][c+temp*k] = "R";
							if(kingSafe())
								list = list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
							chessBoard[r][c]="R";
							chessBoard[r+temp*j][c+temp*k] = oldPiece;
							
							temp++;
						}
						
						if(Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0)))
						{
							oldPiece = chessBoard[r+temp*j][c+temp*k];
							chessBoard[r][c] = " ";
							chessBoard[r+temp*j][c+temp*k] = "R";
							if(kingSafe())
								list = list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
							chessBoard[r][c]="R";
							chessBoard[r+temp*j][c+temp*k] = oldPiece;						
						}
					} catch(Exception e){}
					temp=1;
				}
			}
		}		
		return list;
	}
	
	public static String possibleK(int i)
	{
		String list = "", oldPiece;
		
		int r = i/8, c=i%8; //row,column 
		
		for(int j=-2; j<=2; j++)
		{
			for(int k=-2; k<=2; k++)
			{	/*       j
				 *   0 1 0 1 0
				 *   1 0 0 0 1
				 * k 0 0 x 0 0
				 *   1 0 0 0 1
				 *   0 1 0 1 0
				 */
				if(((j==-2 || j==2) && (k==-1 || k==1)) || ((j==-1 || j==1) && (k==-2 || k==2))) // j must be equal to a 2 only if k is equal to a 1 and vice versa
				{
					try
					{						
						if(Character.isLowerCase(chessBoard[r+j][c+k].charAt(0)) || " ".equals(chessBoard[r+j][c+k]))
						{
							oldPiece = chessBoard[r+j][c+k];
							chessBoard[r][c] = " ";
							chessBoard[r+j][c+k] = "K";
							if(kingSafe())
								list = list+r+c+(r+j)+(c+k)+oldPiece;
							chessBoard[r][c]="K";
							chessBoard[r+j][c+k] = oldPiece;						
						}
					} catch(Exception e){}
				}
			}
		}	
		return list;	
	}	
	
	public static String possibleB(int i)
	{
		String list = "", oldPiece;
		int r = i/8, c=i%8; //row,column 
		
		int temp = 1;
		for(int j=-1; j<=1; j++)
		{
			for(int k=-1; k<=1; k++)
			{	/*     j
				 *   1 0 1
				 * k 0 0 0
				 *   1 0 1
				 */
				if((j==1 || j==-1) && (k==1 || k==-1))
				{
					try
					{
						while(" ".equals(chessBoard[r+temp*j][c+temp*k]))
						{
							oldPiece = chessBoard[r+temp*j][c+temp*k];
							chessBoard[r][c] = " ";
							chessBoard[r+temp*j][c+temp*k] = "B";
							if(kingSafe())
								list = list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
							chessBoard[r][c]="B";
							chessBoard[r+temp*j][c+temp*k] = oldPiece;
							
							temp++;
						}
						
						if(Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0)))
						{
							oldPiece = chessBoard[r+temp*j][c+temp*k];
							chessBoard[r][c] = " ";
							chessBoard[r+temp*j][c+temp*k] = "B";
							if(kingSafe())
								list = list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
							chessBoard[r][c]="B";
							chessBoard[r+temp*j][c+temp*k] = oldPiece;						
						}
					} catch(Exception e){}
					temp=1;
				}
			}
		}		
		return list;
	}
	
	public static String possibleQ(int i)
	{
		String list = "", oldPiece;
		int r = i/8, c=i%8; //row/column 
		
		int temp = 1;
		for(int j=-1; j<=1; j++)
		{
			for(int k=-1; k<=1; k++)
			{	/*     j
				 *   1 1 1
				 * k 1 0 1
				 *   1 1 1
				 */
				if(j!=0 || k!=0)
				{
					try
					{
						while(" ".equals(chessBoard[r+temp*j][c+temp*k]))
						{
							oldPiece = chessBoard[r+temp*j][c+temp*k];
							chessBoard[r][c] = " ";
							chessBoard[r+temp*j][c+temp*k] = "Q";
							if(kingSafe())
								list = list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
							chessBoard[r][c]="Q";
							chessBoard[r+temp*j][c+temp*k] = oldPiece;
							
							temp++;
						}
						
						if(Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0)))
						{
							oldPiece = chessBoard[r+temp*j][c+temp*k];
							chessBoard[r][c] = " ";
							chessBoard[r+temp*j][c+temp*k] = "Q";
							if(kingSafe())
								list = list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
							chessBoard[r][c]="Q";
							chessBoard[r+temp*j][c+temp*k] = oldPiece;						
						}
					} catch(Exception e){}
					temp=1;
				}
			}
		}		
		return list;
	}
	
	public static String possibleA(int i)
	{
		String list = "", oldPiece;
		int r = i/8, c=i%8; //row/column 
		
		for(int j=0; j<9; j++) //a king has 9 places he can move including where he already is
		{
			if(j != 4) //4 represents where the king already is 0,1,2 3,4,5 6,7,8
			{
				try
				{
					if(Character.isLowerCase(chessBoard[r-1+j/3][c-1+j%3].charAt(0)) || " ".equals(chessBoard[r-1+j/3][c-1+j%3])) //if moving into square with opposition piece or no piece
					{
						oldPiece=chessBoard[r-1+j/3][c-1+j%3]; 	//storing what is in the location where the king is going to move so you can put it back later if needed
						chessBoard[r][c] = " "; 				//setting the old position of the King to blank because it has moved from that space
						chessBoard[r-1+j/3][c-1+j%3] = "A";		//moving the King into the new position
						
						int kingTemp = kingPositionC;
						kingPositionC = i+(j/3)*8 + j%3-9; 		//updates the new king position of White/Capitals - math checks out
						if(kingSafe())
							list = list+r+c+(r-1+j/3)+(c-1+j%3)+oldPiece;    //r+c gives initial position | (r-1+j/3)+(c-1+j%3) gives new position | oldPiece gives what was in the square before the King moved
						chessBoard[r][c] = "A"; 				//Only testing which moves are possible so now put everything back
						chessBoard[r-1+j/3][c-1+j%3] = oldPiece;
						kingPositionC = kingTemp;					
					}	
				} 
				catch (Exception e){} 
			}
		}
		//need to add castling later
		return list;
	}
	
	public static int rating()
	{
		/* For debugging
		 * System.out.print("What is the score: ");
		 * Scanner sc = new Scanner(System.in);
		 * return sc.nextInt();
		*/
		return 0;
	}
	
	public static boolean kingSafe() //is the position the king will move into a safe position?
	{
		//bishop/queen
		int temp = 1;
		for(int i=-1; i<=1; i++)
		{
			for(int j=-1; j<=1; j++)
			{	/*     j
				 *   1 0 1
				 * k 0 0 0
				 *   1 0 1
				 */
				if((i==1 || i==-1) && (j==1 || j==-1))
				{
					try
					{
						while(" ".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8+temp*j])) {temp++;} //moves through empty tiles until it hits something
						if("b".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8+temp*j]) ||
								"q".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8+temp*j]))
						{
							return false;	//if a bishop or queen is in line with the king then the king is not safe						
						}
					} catch(Exception e){}
					temp=1;
				}
			}
		}
		//rook/queen
		//temp = 1; temp is already 1
		for(int i=-1; i<=1; i+=2)
		{
			try
			{
				while(" ".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8])) {temp++;} //moves through vertical
				if("r".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8]) ||
						"q".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8]))
				{
					return false;	//if a rook or queen is in line with the king then the king is not safe						
				}
			} catch(Exception e){}
			temp=1;
			try
			{
				while(" ".equals(chessBoard[kingPositionC/8][kingPositionC%8+temp*i])) {temp++;} //moves through horizontal
				if("r".equals(chessBoard[kingPositionC/8][kingPositionC%8+temp*i]) ||
						"q".equals(chessBoard[kingPositionC/8][kingPositionC%8+temp*i]))
				{
					return false;	//if a rook or queen is in line with the king then the king is not safe						
				}
			} catch(Exception e){}
			temp=1;
		}
		//knight
		for(int i=-1; i<=1; i+=2)
		{
			for(int j=-1; j<=1; j+=2)
			{	
				try
					{
						if("k".equals(chessBoard[kingPositionC/8+i][kingPositionC%8+j*2]))
						{
							return false;						
						}
					} catch(Exception e){}
					try
					{
						if("k".equals(chessBoard[kingPositionC/8+i*2][kingPositionC%8+j]))
						{
							return false;						
						}
					} catch(Exception e){}
			}
		}
		//pawn
		if(kingPositionC>=16)
		{
			try
			{
				if("p".equals(chessBoard[kingPositionC/8-1][kingPositionC%8-1]))
				{
					return false;						
				}
			} catch(Exception e){}
			try
			{
				if("p".equals(chessBoard[kingPositionC/8-1][kingPositionC%8+1]))
				{
					return false;						
				}
			} catch(Exception e){}
		}
		//king		
		for(int i=-1; i<=1; i++)
		{
			for(int j=-1; j<=1; j++)
			{	
				if(i!=0 || j!=0)
				try
					{
						if("a".equals(chessBoard[kingPositionC/8+i][kingPositionC%8+j]))
						{
							return false;						
						}
					} catch(Exception e){}
			}
		}
		
		
		
		return true;
	}
	
	static int kingPositionC,kingPositionL; //capital, lowercase
	static int globalDepth = 4;
	
	public static void main(String[] args) 
	{
		while(!"A".equals(chessBoard[kingPositionC/8][kingPositionC%8])) {kingPositionC++;}//get King's location
		while(!"a".equals(chessBoard[kingPositionL/8][kingPositionL%8])) {kingPositionL++;}//get King's location
		
		JFrame f = new JFrame("Callum's Chess Program");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UserInterface ui = new UserInterface();
		f.add(ui);
		f.setSize(500, 500);
		f.setVisible(true);
		
		System.out.println(possibleMoves());
		makeMove(alphaBeta(globalDepth, 1000000, -1000000, "", 0));
			
		//makeMove("7657 ");
		//undoMove("7657 ");		
		for(int i=0; i<8; i++)
			System.out.println(Arrays.toString(chessBoard[i]));
		
		
	}

}
