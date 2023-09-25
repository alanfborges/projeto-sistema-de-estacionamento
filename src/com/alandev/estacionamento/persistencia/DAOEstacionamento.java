package com.alandev.estacionamento.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import com.alandev.estacionamento.controle.EstacionamentoException;
import com.alandev.estacionamento.negocio.Movimentacao;
import com.alandev.estacionamento.negocio.Vaga;
import com.alandev.estacionamento.negocio.Veiculo;
import com.alandev.estacionamento.utilitario.EstacionamentoUtil;

public class DAOEstacionamento {

	/**
	 * Armazena os dados da movimentacao
	 * 
	 * @param movimentacao Instância de movimentação
	 * @throws EstacionamentoException se houver erro de registro
	 */
	public void criar(Movimentacao movimentacao) throws EstacionamentoException {
		String cmd1 = EstacionamentoUtil.get("insertMov");
		String cmd2 = EstacionamentoUtil.get("atualizaVaga");

		Connection conexao = null;
		try {
			conexao = getConection();
			conexao.setAutoCommit(false);
			PreparedStatement stmt = conexao.prepareStatement(cmd1);
			stmt.setString(1, movimentacao.getVeiculo().getPlaca());
			stmt.setString(2, movimentacao.getVeiculo().getMarca());
			stmt.setString(3, movimentacao.getVeiculo().getModelo());
			stmt.setString(4, movimentacao.getVeiculo().getCor());
			stmt.setString(5, EstacionamentoUtil.getDataAsString(movimentacao.getDataHoraEntrada()));

			stmt.execute();
			// segundo comando
			stmt = conexao.prepareStatement(cmd2);
			stmt.setInt(1, Vaga.ocupadas() + 1);
			stmt.execute();

			conexao.commit();
		} catch (SQLException e) {
			try {
				conexao.rollback();
				throw new EstacionamentoException("Erro ao registrar veículo!");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Atualiza os dados de data de saída e valor da movimentação.
	 * 
	 * @param movimentacao instância da movimentacao
	 * @throws EstacionamentoException Quando há erro na hora de registrar o veiculo
	 */
	public void atualizar(Movimentacao movimentacao) throws EstacionamentoException {
		String cmd1 = EstacionamentoUtil.get("updateMov");
		String cmd2 = EstacionamentoUtil.get("atualizaVaga");

		Connection conexao = null;
		try {
			conexao = getConection();
			conexao.setAutoCommit(false);
			PreparedStatement stmt = conexao.prepareStatement(cmd1);
			stmt.setDouble(1, movimentacao.getValor());
			stmt.setString(2, EstacionamentoUtil.getDataAsString(movimentacao.getDataHoraSaida()));
			stmt.setString(3, movimentacao.getVeiculo().getPlaca());

			stmt.execute();
			// segundo comando
			stmt = conexao.prepareStatement(cmd2);
			stmt.setInt(1, Vaga.ocupadas() - 1);
			stmt.execute();

			conexao.commit();
		} catch (SQLException e) {
			try {
				conexao.rollback();
				throw new EstacionamentoException("Erro ao registrar veículo!");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Busca a movimentação cujo veiculo tem a placa informad que aina esta
	 * estacionado
	 * 
	 * @param placa A placa do veiculo
	 * @return A movimentação encontrada ou null se não houver.
	 */
	public Movimentacao buscarMovimentacaoAberta(String placa) {
		String cmd = EstacionamentoUtil.get("getMovAberta");
		Connection conexao = null;
		Movimentacao movimentacao = null;
		try {
			conexao = getConection();
			PreparedStatement ps = conexao.prepareStatement(cmd);
			ps.setString(1, placa);

			ResultSet resultado = ps.executeQuery();

			if (resultado.next()) {
				String rplaca = resultado.getString("placa");
				String rdataEntrada = resultado.getString("data_entrada");
				Veiculo veiculo = new Veiculo(rplaca);
				movimentacao = new Movimentacao(veiculo, EstacionamentoUtil.getDate(rdataEntrada));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conexao);
		}
		return movimentacao;
	}

	/**
	 * Consulta todas as movimentacoes fechadas (pagas e com data de sai preenchida)
	 * no mês e ano da data informada
	 * 
	 * @param data Data da consulta
	 * @return Lista de movimentações do ano e mês informados
	 */
	public List<Movimentacao> consultarMovimentacoes(LocalDateTime data) {
		Connection conexao = null;
		String cmd = EstacionamentoUtil.get("selectMovRelatorio");
		List<Movimentacao> movimentacoes = new ArrayList<>();
		try {
			conexao = getConection();
			PreparedStatement ps = conexao.prepareStatement(cmd);
			ps.setString(1, data.toString());
			data = data.with(TemporalAdjusters.lastDayOfMonth());
			ps.setString(2, data.toString());

			ResultSet resultado = ps.executeQuery();

			while (resultado.next()) {
				String placa = resultado.getString("placa");
				LocalDateTime entrada = EstacionamentoUtil.getDate(resultado.getString("data_entrada"));
				LocalDateTime saida = EstacionamentoUtil.getDate(resultado.getString("data_saida"));
				double valor = resultado.getDouble("valor");

				Veiculo veiculo = new Veiculo(placa);
				Movimentacao movimentacao = new Movimentacao(veiculo, entrada);
				movimentacao.setDataHoraSaida(saida);
				movimentacao.setValor(valor);

				movimentacoes.add(movimentacao);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection(conexao);
		}
		return movimentacoes;
	}

	/**
	 * Realiza uma conexão com os dados obtidos externamente
	 * 
	 * @return Uma conexão
	 * @throws SQLException
	 */
	public static Connection getConection() throws SQLException {
		String url = EstacionamentoUtil.get("url");
		String usuario = EstacionamentoUtil.get("usuario");
		String senha = EstacionamentoUtil.get("senha");

		Connection conexao = DriverManager.getConnection(url, usuario, senha);
		return conexao;

	}

	/**
	 * Encerra uma conexão
	 * 
	 * @param conexao Conexão a ser encerrada
	 */
	public static void closeConnection(Connection conexao) {
		if (conexao != null) {
			try {
				conexao.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Busca da base de dados a quantidade de vagas ocupadas
	 * 
	 * @return a quantidade de vagas ocupadas
	 */
	public int getOcupadas() {
		int ocupadas = 0;
		Connection conexao = null;
		String cmd = EstacionamentoUtil.get("consultaOcupadas");
		try {
			conexao = getConection();
			PreparedStatement ps = conexao.prepareStatement(cmd);

			ResultSet resultado = ps.executeQuery();
			if (resultado.next()) {
				ocupadas = resultado.getInt("ocupadas");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection(conexao);
		}
		return ocupadas;
	}
}
