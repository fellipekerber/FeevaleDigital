package projectMultiPlataforma;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Cliente extends JFrame implements ActionListener {
	// parte que controla a recepção de mensagens do cliente
	private Socket conexao;

	Socket socket;

	JTextArea areaTexto;

	JTextField msg;

	JButton btn;

	JLabel rotulo;

	PrintStream saida;

	BufferedReader entrada;

	public Cliente() {
		super("Chat");

		Container tela = getContentPane();

		BorderLayout layout = new BorderLayout();
		tela.setLayout(layout);

		rotulo = new JLabel("");
		btn = new JButton("Enviar");
		btn.addActionListener(this);
		msg = new JTextField(20);

		areaTexto = new JTextArea(15, 30);
		JScrollPane painelRolagem = new JScrollPane(areaTexto);
		painelRolagem.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		painelRolagem.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		JPanel pSuperior = new JPanel();
		pSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));
		pSuperior.add(rotulo);

		JPanel pCentro = new JPanel();
		pCentro.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		pCentro.add(painelRolagem);

		JPanel pInferior = new JPanel();
		pInferior.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pInferior.add(msg);
		pInferior.add(btn);

		tela.add(BorderLayout.NORTH, pSuperior);
		tela.add(BorderLayout.CENTER, pCentro);
		tela.add(BorderLayout.SOUTH, pInferior);

		pack();
		setVisible(true);
	}

	void executa() {
		try {
			socket = new Socket("127.0.0.1", 8084);
			// Instancia do atributo saida, obtem os objetos que permitem controlar o fluxo
			// de comunicação
			saida = new PrintStream(socket.getOutputStream());
			String meuNome = JOptionPane.showInputDialog("Digite seu nome ");
			rotulo.setText("Olá, " + meuNome);
			// envia o nome digitado para o servidor
			saida.println(meuNome.toUpperCase());
			// instancia a thread para ip e porta conectados e depois inicia ela
			Recebedor r = new Recebedor(socket.getInputStream());
			new Thread(r).start();
		} catch (IOException e) {
			System.out.println("Falha na Conexao... .. ." + " IOException: " + e);
		}
	}

	public static void main(String[] args) {
		Cliente cliente = new Cliente();
		WindowListener ls = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		cliente.addWindowListener(ls);
		cliente.executa();
	}

	// execução da thread
	public void run() {
		try {
			// recebe mensagens de outro cliente através do servidor
			BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
			// cria variavel de mensagem
			String msg;
			while (true) {
				// pega o que o servidor enviou
				msg = entrada.readLine();
				// se a mensagem contiver dados, passa pelo if, caso contrario cai no break e
				// encerra a conexao
				if (msg == null) {
					System.out.println("Conexão encerrada!");
					System.exit(0);
				}
				areaTexto.append(msg + "\n");
			}
		} catch (IOException e) {
			// caso ocorra alguma exceção de E/S, mostra qual foi.
			System.out.println("Ocorreu uma Falha... .. ." + " IOException: " + e);
		}
	}

	class Recebedor implements Runnable {

		private InputStream servidor;

		public Recebedor(InputStream servidor) {
			this.servidor = servidor;
		}

		@Override
		public void run() {
			try {
				// recebe mensagens de outro cliente através do servidor
				entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				// cria variavel de mensagem
				String msg;
				while (true) {
					// pega o que o servidor enviou
					msg = entrada.readLine();
					// se a mensagem contiver dados, passa pelo if, caso contrario cai no break e
					// encerra a conexao
					if (msg == null) {
						System.out.println("Conexão encerrada!");
						System.exit(0);
					}
					areaTexto.append(msg + "\n");
				}
			} catch (IOException e) {
				// caso ocorra alguma exceção de E/S, mostra qual foi.
				System.out.println("Ocorreu uma Falha... .. ." + " IOException: " + e);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object fonte = event.getSource();

		if (fonte == btn) // Envia informacao pelo socket
		{
			String text = msg.getText();
			saida.println(text);
			msg.setText(new String("")); // recebe do servidor
		}
	}
}