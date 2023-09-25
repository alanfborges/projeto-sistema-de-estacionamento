package com.alandev.estacionamento.negocio;

import com.alandev.estacionamento.controle.EstacionamentoController;

/**
 * Representa as informações relativas a vagas do estacionamento ou status de
 * ocupação.
 * 
 * @author alan
 */
public class Vaga {

	public static int TOTAL_VAGAS = 100;
	private static int vagasOcupadas = inicilizarOcupadas();

	private Vaga() {
	}

	/**
	 * Verificar a existencia de alguma vaga livre no estacionamento.
	 * 
	 * @return true se houver alguma vaga e false se estiver lotado.
	 */
	public static boolean temVagaLivre() {
		return (vagasOcupadas < TOTAL_VAGAS);
	}

	/**
	 * Buscar o status atual das vagas do estacionamento
	 */
	public static int inicilizarOcupadas() {
		EstacionamentoController controle = new EstacionamentoController();
		return controle.inicializarOcupadas();
	}

	/**
	 * Retorna o número de vagas ocupadas
	 * 
	 * @return número total de vagas ocupadas num determinado instante
	 */
	public static int ocupadas() {
		return Vaga.vagasOcupadas;
	}

	/**
	 * Retornar o número de vagas livres
	 * 
	 * @return numero total de vagas livres num determinado instante
	 */
	public static int livres() {
		return TOTAL_VAGAS - Vaga.vagasOcupadas;
	}

	/**
	 * Atualiza o numero des vagas ocupadas apos a entrada do veículo
	 */
	public static void entrou() {
		Vaga.vagasOcupadas++;
	}

	/**
	 * Atualiza o numero des vagas ocupadas apos a saida do veículo
	 */
	public static void saiu() {
		Vaga.vagasOcupadas--;
	}
}
