package com.alandev.estacionamento.controle;

import java.time.LocalDateTime;
import java.util.List;

import com.alandev.estacionamento.negocio.Movimentacao;
import com.alandev.estacionamento.negocio.Vaga;
import com.alandev.estacionamento.negocio.Veiculo;
import com.alandev.estacionamento.persistencia.DAOEstacionamento;
import com.alandev.estacionamento.utilitario.EstacionamentoUtil;

/**
 * Reponsável pela inteligência da aplicação, irá intermedia a camada de
 * persistencia e a camada de apresentação.
 * 
 * @author alan
 */
public class EstacionamentoController {

	/**
	 * A partir dos dados do veiculo informados pelo operador realiza o fluxo de
	 * entrada do veiculo no estacinamento registrando a movimentação gerada.
	 * 
	 * @param placa  Placa do veiculo
	 * @param marca  Marca do veiculo
	 * @param modelo Modelo do veiculo
	 * @param cor    Cor do veiculo
	 * @throws EstacionamentoException Quando estacionamento estiver lotado
	 * @throws VeiculoException        Quando o padrão da placa for invalido
	 */
	public void processarEntrada(String placa, String marca, String modelo, String cor)
			throws EstacionamentoException, VeiculoException {
		// verificar se o estacionamento está lotado
		if (!Vaga.temVagaLivre()) {
			throw new EstacionamentoException("Estacionamento lotado!");
		}

		// verificar o padrao de string da placa
		if (!EstacionamentoUtil.validarPadraoPlaca(placa)) {
			throw new VeiculoException("Placa informada inválida!");
		}

		// criar uma instancia do veiculo
		Veiculo veiculo = new Veiculo(placa, marca, modelo, cor);

		// criar a movimentacao vinculando o veiculo e com data de entrada corrente
		Movimentacao movimentacao = new Movimentacao(veiculo, LocalDateTime.now());

		// registrar na base de dados a informacao
		DAOEstacionamento dao = new DAOEstacionamento();
		dao.criar(movimentacao);

		// atualizar o numero de vagas ocupadas
		Vaga.entrou();
	}

	/**
	 * A partir de uma placa de veiculo informada, realiza todo o fluxo de saída de
	 * veiculo do estacionamento.
	 * 
	 * @param placa Placa do veiculo que estiver saindo
	 * @return Uma instância de movimentação com dados atualizados de valor a pagar
	 * @throws VeiculoException        quando a placa for inválida
	 * @throws EstacionamentoException Quando veículo com a placa informada não é
	 *                                 localizado
	 */
	public Movimentacao processarSaida(String placa) throws VeiculoException, EstacionamentoException {
		// validar a placa
		if (!EstacionamentoUtil.validarPadraoPlaca(placa)) {
			throw new VeiculoException("Placa Inválida!");
		}

		// buscar a movimentacao aberta baseada na placa
		DAOEstacionamento dao = new DAOEstacionamento();
		Movimentacao movimentacao = dao.buscarMovimentacaoAberta(placa);

		if (movimentacao == null) {
			throw new EstacionamentoException("Veículo não encontrado!");
		}

		// fazer o calculo do valor a ser pago
		movimentacao.setDataHoraSaida(LocalDateTime.now());
		EstacionamentoUtil.calcularValorPago(movimentacao);

		// atualizar os dados da movimentacao
		dao.atualizar(movimentacao);

		// atualizar o status da vaga
		Vaga.saiu();

		return movimentacao;
	}

	/**
	 * Realiza o fluxo de emissão de relatorio de faturamento baseado num m~es e ano
	 * informados
	 * 
	 * @param data Data (mês e ano) de emissão desejado
	 * @return Lista de movimentação que atendem ao filtro
	 */
	public List<Movimentacao> emitirRelatorio(LocalDateTime data) {
		DAOEstacionamento dao = new DAOEstacionamento();
		return dao.consultarMovimentacoes(data);
	}

	/**
	 * Busca na camada de persistencia informações sobre a vaga
	 * 
	 * @return quantidade de vagas ocupadas
	 */
	public int inicializarOcupadas() {
		DAOEstacionamento dao = new DAOEstacionamento();
		return dao.getOcupadas();
	}
}
