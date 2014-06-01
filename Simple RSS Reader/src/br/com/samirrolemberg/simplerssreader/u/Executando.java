package br.com.samirrolemberg.simplerssreader.u;

import java.util.HashMap;
import java.util.Map;

public class Executando {

	/**
	 * Armazena um conteúdo de um feed que está sendo adicionado na Task de SalvarNovoFeedTask.java
	 * O link do rss é adicionado no onpreexecute e removido no onpostexecute da mesma tarefa
	 * verificar na activity se o link do feed recem adicionado ainda etsá nessa fila.
	 * a chave de comparação é feita com
	 * feedAux.getIdFeed()+feedAux.getRss()
	 * e o valor 1 é adicionado á chave como valor de execução.
	 */
	public final static Map<String, Integer> ADICIONAR_FEED = new HashMap<String, Integer>();
	
	private Executando(){
		super();
	}
}
