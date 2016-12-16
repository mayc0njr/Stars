import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class Star{
	public int x;
	public int y;
	public int size;

	public Star(int x, int y){
		this.x = x;
		this.y = y;
		this.size = Math.max(1,(Math.abs(this.x-(Space.width/2)) + Math.abs(this.y-(Space.height/2)))/100);
	}
}
class StarPanel extends JPanel{
	Random rd = new Random();
	Star[] estrelas;
	Timer timer;
	ActionListener listener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			update();
		}
	};
	public StarPanel(int width, int height){
		this(width, height, ((Space.arg1 + Space.arg2)/4));
	}
	public StarPanel(int width, int height, int quant){
		super();
		this.setSize(width,height);
		estrelas = new Star[quant];
		initializeStars();
		this.setOpaque(true);
		repaint();
		timer = new Timer(Space.delay, listener);
		timer.setRepeats(true);
		timer.start();
	}
	public void initializeStars(){
		for(int x=0 ; x < estrelas.length ; x++){
			estrelas[x] = new Star(rd.nextInt(Space.width), rd.nextInt(Space.height));
		}
	}
	@Override
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.BLACK);
		g.fillRect(0,0,Space.width,Space.height);
		if(estrelas == null)
			return;
		g.setColor(Color.WHITE);
		for(Star s : estrelas){
			g.fillOval(s.x, s.y, s.size, s.size);
		}
	}
	public void update(){
		for(Star s : estrelas){
			if(s.x >= Space.width || s.x < 0 || s.y >= Space.height || s.y < 0){
				s.x = rd.nextInt(Space.width);
				s.y = rd.nextInt(Space.height);
			}else{
				if(s.x <= Space.width/2){
					s.x-= Math.max(1, Space.speed*(Space.width/2 - s.x)/100);
				}else{
					s.x+= Math.max(1, Space.speed*(s.x - Space.width/2)/100);
				}
				if(s.y <= Space.height/2){
					s.y-= Math.max(1, Space.speed*(Space.height/2 - s.y)/100);
				}else{
					s.y+= Math.max(1, Space.speed*(s.y - Space.height/2)/100);
				}
			}
			s.size = Math.max(2,(Math.abs(s.x-(Space.width/2)) + Math.abs(s.y-(Space.height/2)))/100);
			repaint();

		}
	}
	@Override
	public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    };
}

class StarFrame extends JFrame{
	JPanel painel;
	public StarFrame(String s, int width, int height){
		super(s);
		this.setSize(width, height);
		this.painel = new StarPanel(width,height);
		painel.setLayout(null);
		this.painel.setBounds(0,0,width,height);
		this.setContentPane(painel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public StarFrame(String s){
		this(s, 400, 400);
	}
	public StarFrame(){
		this("Untitled");
	}
}

public class Space{
	public static int arg1, arg2, speed = 3, delay = 50, width, height;
	public static void main(String args[]){
		arg1 = Integer.parseInt(args[1]);
		arg2 = Integer.parseInt(args[2]);
		width = arg1;
		height = arg2;
		new StarFrame(args[0], arg1,arg2).setVisible(true);
	}
}
