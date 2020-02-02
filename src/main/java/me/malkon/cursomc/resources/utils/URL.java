package me.malkon.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	// converte string com espaços, caracteres especiais para string c caracteres
	// básicos. A URL vem tipo tv%20%led. Pq na classe ProdutoServices é esperado
	// uma string como nome e uma lista de ids
	// da categoria
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";// caso de algum erro(excecao) retorne uma string vazia
		}
	}

	public static List<Integer> decodeIntList(String s) {
		String[] vet = s.split(",");
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < vet.length; i++) {
			list.add(Integer.parseInt(vet[i]));
		}
		return list;
		// Essa linha abaixo faz a mesma operacao que esta acima, metodo decodeIntList
		// return Arrays.asList(s.split(",")).stream().map(x ->
		// Integer.parseInt(x)).collect(Collectors.toList());
	}

}
