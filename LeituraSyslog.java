package projetoIntegrador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LeituraSyslog {

	public static void main( String[] args ) throws IOException {

		ListaSimplesmenteEncadeada lista = new ListaSimplesmenteEncadeada();
		File syslog = new File( "/var/log/syslog" );
		boolean flag = true;

		try (Scanner inputUsuario = new Scanner( System.in )) {

			while( flag ) {

				System.out.println( "+------ MENU -----------------------+" );
				System.out.println( "+   1 - PROCESSAR EVENTOS           +" );
				System.out.println( "+   2 - LISTAR                      +" );  
				System.out.println( "+   3 - LIMPAR                      +" );
				System.out.println( "+   4 - EXCLUIR                     +" );
				System.out.println( "+   5 - CONTADOR                    +" );
				System.out.println( "+   6 - VALIDAR STATUS DE CONEXÃO   +" );
				System.out.println( "+   7 - EXIT                        +" );
				System.out.println( "+-----------------------------------+" );
				
				System.out.println("OPÇÃO DESEJADA:");
				int opcao = inputUsuario.nextInt();

				if( opcao == 1 ) {

					if( lista.tamanhoLista() > 0 ) {
						System.out.println( "AVISO: LISTA JÁ CARREGADA!!!" );
					} else {
						lista = LeituraSyslog.carregaEventos( syslog, lista );
						System.out.println( "A lista possui " + ( lista.tamanhoLista() == 1 ? " evento!!" : " eventos!!" ) );
					}
				} else if( opcao == 2 ) {

					System.out.println( "EVENTOS DE CONEXÃO: \n" );
					for( int i = 0; i < lista.tamanhoLista(); i++ ) {
						System.out.println( i + 1 + " - " + lista.pegarValor( i ) );
					}
				} else if( opcao == 3 ) {

					if( lista.tamanhoLista() > 0 ) {
						lista.excluirLista();
						System.out.println( "AVISO: Todos os eventos foram excluídos!!" );
					} else {
						System.out.println( "AVISO: Todos os eventos já foram excluídos anteriormente!!" );
					}
				} else if( opcao == 4 ) {

					if( lista.tamanhoLista() > 0 ) {
						System.out.println( "Por favor, indique a linha a ser excluída: " );
						opcao = inputUsuario.nextInt();

						if( opcao > 0 && opcao <= lista.tamanhoLista() ) {
							lista.excluirValorLista( opcao - 1 );
							System.out.println( "AVISO: Linha " + opcao + "excluída!" );
						} else {
							System.out.println( "ERRO: Linha desejada não consta no arquivo!!" );
						}
					} else {
						System.out.println( "ERRO: Não existem linhas para excluir." );
					}
				} else if( opcao == 5 ) {

					if( lista.tamanhoLista() == 0 ) {
						System.out.println( "ERRO: Lista atualmente vazia." );
					} else {
						System.out.println( "A lista possui " + ( lista.tamanhoLista() == 1 ? " elemento!!" : " elementos!!" ) );
					}
				} else if( opcao == 6 ) {

					System.out.println( LeituraSyslog.verificaEstadoConexao( lista ) );
				} else if( opcao == 7 ) {

					System.out.println( "Solicitando cancelamento da navegação..." );
					System.out.println( "...NAVEGAÇÃO CANCELADA!" );
					flag = false;
				} else {

					System.out.println( "ERRO: Comando não encontrado." );
				}
				System.out.println( "\n\n" );

			}

		} catch( InputMismatchException e ) {
			System.out.println( "ERRO: Por favor, navegue o menu entre os indicadores de 1 a 7." );
		}
	}

	public static ListaSimplesmenteEncadeada carregaEventos( File syslog, ListaSimplesmenteEncadeada lista ) throws FileNotFoundException {

		try (Scanner leitor = new Scanner( syslog )) {
			while( leitor.hasNextLine() ) {
				String line = leitor.nextLine();
				String[] vectorLine = line.split( " " );

				if( vectorLine[ 5 ].contains( "NetworkManager" ) ) {
					lista.adicionar( line );
				}
			}
		}

		return lista;
	}

	public static String verificaEstadoConexao( ListaSimplesmenteEncadeada lista ) {

		while( lista.tamanhoLista() > 0 ) {

			String ultimaLinha = (String) lista.pegarUltimoValor();
			if( ultimaLinha.contains( "CONNECTED_GLOBAL" ) ) {
				return "Conexão ativa!";
			} else if( ultimaLinha.contains( "DISCONNECTED" ) ) {
				return "Conexão£o inativa.";
			} else {
				lista.excluirValorLista( lista.tamanhoLista() - 1 );
			}
		}
		return "ERRO: Impossível validar o status por falta de indicadores.";
	}
}
