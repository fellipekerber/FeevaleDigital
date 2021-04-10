package projetoIntegrador;

import java.nio.channels.IllegalSelectorException;

public class ListaSimplesmenteEncadeada {

	private static class Nodo {

		Object dado;
		Nodo proximo;

		public Nodo( Object dado ) {
			this.dado = dado;
		}
	}

	private Nodo cabeca;
	private int tamanho = 0;

	public void adicionar( Object dado ) {

		Nodo novoNodo = new Nodo( dado );

		if( cabeca == null ) {
			cabeca = novoNodo;
			tamanho++;
			return;
		}

		Nodo atual = cabeca;
		while( atual.proximo != null ) {
			atual = atual.proximo;
		}
		atual.proximo = novoNodo;
		tamanho++;
	}

	public Object pegarPrimeiroValor() {

		if( cabeca == null ) {
			throw new IllegalSelectorException();
		}
		return cabeca.dado;
	}

	public Object pegarUltimoValor() {

		if( cabeca == null ) {
			throw new IllegalSelectorException();
		}

		Nodo atual = cabeca;

		while( atual.proximo != null ) {
			atual = atual.proximo;
		}
		return atual.dado;
	}

	public Object pegarValor( int posicao ) {
		if( cabeca == null ) {
			throw new IllegalStateException();
		}

		if( posicao > tamanho ) {
			throw new ArrayIndexOutOfBoundsException();
		}

		Nodo atual = cabeca;
		int posicaoAtual = 0;
		while( posicaoAtual < posicao ) {

			atual = atual.proximo;
			posicaoAtual++;
		}
		return atual.dado;
	}

	public int tamanhoLista() {
		return tamanho;
	}

	public void excluirLista() {
		cabeca = null;
		tamanho = 0;
	}

	public void excluirValorLista( int posicao ) {

		if( cabeca == null ) {
			return;
		}

		if( posicao > tamanho ) {
			throw new ArrayIndexOutOfBoundsException();
		}

		if( posicao == 0 ) {
			cabeca = cabeca.proximo;
			tamanho --;
			return;
		}

		Nodo atual = cabeca;
		int posicaoDelete = 1;
		while( posicaoDelete < posicao ) {

			atual = atual.proximo;
			posicaoDelete++;
		}
		atual.proximo = atual.proximo.proximo;
		tamanho--;
	}

}
