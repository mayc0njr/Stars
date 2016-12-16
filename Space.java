import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/* Classe que armazena os dados da estrela.
 * A estrela é desenhada como uma circunferência.
 * Pode ser desenhada a partir de uma posição X,Y, e o tamanho da circuferência. *
 */
class Star{
	public int x; //Coordenada X
	public int y; //Coordenada Y
	public int size; //Tamanho da estrela.

	public Star(int x, int y){
		this.x = x;
		this.y = y;
		//Fórmula que calcula o tamanho da estrela, quanto mais longe do centro, maior é.
		this.size = Math.max(1,(Math.abs(this.x-(Space.width/2)) + Math.abs(this.y-(Space.height/2)))/100);
	}
}
/* Painel onde são desenhadas as estrelas, para representar o espaço.
 * Herda de um JPanel para manter a herança. Porém existem sobreescritas de funções.
 */
class StarPanel extends JPanel{
	Random rd = new Random(); //Random utilizado para reposicionar as estrelas que saem do espaço referente ao panel, evitando re-instanciar objetos.
	Star[] estrelas; //Array de estrelas, todas as estrelas exibidas ficarão neste array.
	Timer timer; //Cronômetro utilizado para "atualizar" a exibição dos frames na tela.
	ActionListener listener = new ActionListener() { //Action Listener utilizado junto ao Timer, define qual função será chamada quando o cronômetro zerar.
		public void actionPerformed(ActionEvent event) {
			update();
		}
	};
	public StarPanel(int width, int height){
		this(width, height, ((Space.arg1 + Space.arg2)/4));
	}
	public StarPanel(int width, int height, int quant){
		super();
		this.setSize(width,height); //Define o tamanho do panel.
		estrelas = new Star[quant];//Define a quantidade de estrelas.
		initializeStars(); //Instancia todas as estrelas do array.
		this.setOpaque(true);//Define que o painel não é transparente.
		repaint();//Repinta-o, de acordo com as estrelas geradas.
		timer = new Timer(Space.delay, listener); //Instancia o cronômetro.
		timer.setRepeats(true); //Marca que o cronômetro repetirá cada vez que terminar a contagem.
		timer.start(); //Inicializa a contagem do cronômetro.
	}
	public void initializeStars(){
		for(int x=0 ; x < estrelas.length ; x++){
			estrelas[x] = new Star(rd.nextInt(Space.width), rd.nextInt(Space.height)); //Inicializa uma estrela a partir de valores aleatórios
		}
	}

	/* A função paint foi sobreescrita de forma a realizar os procedimentos de
 	 * desenho do espaço e das estrelas. Para garantir o bom funcionamento,
 	 * a função chama em seu inicio o paint do JPanel, superclasse de StarPanel.
 	 * o espaço é desenhado de forma que o fundo é desenhado como um retângulo preto
 	 * preenchido, e as estrelas círculos brancos de acordo com suas coordenadas e tamanho.
	*/
	@Override
	public void paint(Graphics g){
		super.paint(g); //Chamando o paint da superclasse
		g.setColor(Color.BLACK); //setando a cor para preto
		g.fillRect(0,0,Space.width,Space.height); //Desenha um retângulo que cobre toda a tela.
		if(estrelas == null) //Caso o array de estrelas não tenha sido instânciado ainda, finaliza a função.
			return;
		g.setColor(Color.WHITE); //Seta a cor para branco.
		for(Star s : estrelas){ //Desenha todas as estrelas do array na tela.
			g.fillOval(s.x, s.y, s.size, s.size);
		}
	}

	/* Função chamada pelo cronômetro para atualizar o estado atual da interface.
	 * A função "empurra" todas as estrelas um pouco para fora da tela, e então,
	 * redimensiona as estrelas de acordo com sua posição. Estrelas mais longe
	 * do centro, são maiores e se movem ainda mais rápido para fora da tela.
	 * Por fim, as estrelas que saem da tela, são "resetadas", recebendo uma posição
	 * aleatória, e redesenhadas na tela.
	*/
	public void update(){
		for(Star s : estrelas){ //Para todas estrelas do array
			if(s.x >= Space.width || s.x < 0 || s.y >= Space.height || s.y < 0){ //Reseta as estrelas que saíram da tela.
				s.x = rd.nextInt(Space.width);
				s.y = rd.nextInt(Space.height);
			}else{ //Caso as estrelas estejam dentro da tela, afasta-as do centro de acordo com o quadrante em que se encontram.
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
			s.size = Math.max(2,(Math.abs(s.x-(Space.width/2)) + Math.abs(s.y-(Space.height/2)))/100); //Recalcula o tamanho da estrela de acordo com sua nova posição.
			repaint(); //Repinta o Painel.

		}
	}
	@Override
	public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    };
}

/* Classe que representa a Janela da aplicação
 * Herda de JFrame e acrescenta conteúdos próprios,
 * seu conteúdo e apenas (e todo) o conteúdo do StarPanel.
 */
class StarFrame extends JFrame{
	JPanel painel;
	public StarFrame(String s, int width, int height){
		super(s);
		this.setSize(width, height);
		this.painel = new StarPanel(width,height);
		painel.setLayout(null);
		this.painel.setBounds(0,0,width,height); //Redimensiona o painel.
		this.setContentPane(painel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public StarFrame(String s){
		this(s, 400, 400);
	}
	public StarFrame(int width, int height){
		this("Untitled", width, height);
	}
}

public class Space{
	public static int arg1, arg2, speed = 3, delay = 50, width, height;
	public static void main(String args[]){
		int nArgs = args.length;
		if(nArgs > 3)
			nArgs = 3;
		arg1 = Integer.parseInt(args[nArgs-2]);
		arg2 = Integer.parseInt(args[nArgs-1]);
		width = arg1;
		height = arg2;
		if(nArgs < 3)
			new StarFrame(arg1,arg2).setVisible(true);
		else
			new StarFrame(args[0], arg1,arg2).setVisible(true);
	}
}
