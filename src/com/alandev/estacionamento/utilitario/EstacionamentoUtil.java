package com.alandev.estacionamento.utilitario;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alandev.estacionamento.negocio.Movimentacao;
import com.alandev.estacionamento.negocio.Tarifario;

/**
 * Reprensenta uma classe de apoio as demais do sistema.
 * 
 * @author alan
 */
public class EstacionamentoUtil {

	/**
	 * Valida placa com o padrao LLL-NNNN L = Letra N = Numero
	 * 
	 * @param placa Placa do veículo
	 * @return true se atender o padrão e false senão.
	 */
	public static boolean validarPadraoPlaca(String placa) {
		String padrao = "[A-Z][A-Z][A-Z]-\\d\\d\\d\\d";
		Pattern p = Pattern.compile(padrao);
		Matcher m = p.matcher(placa);
		return m.matches();
	}

	/**
	 * O calculo do valor da estada do veículo baseado no tarifario e na hora de
	 * entrada e saída do veículo
	 * 
	 * Altera a própria a instância passada como parâmetro.
	 * 
	 * @param movimentacao Instância da movimentação
	 */
	public void calcularPagamento(Movimentacao movimentacao) {
		// TODO implementar
	}

	/**
	 * Recupera uma propriedade di arquivo de configuração da aplicação
	 * configuration.txt
	 * 
	 * @param propriedade a ser lida
	 * @return valor associado a propriedade
	 */
	public static String get(String propriedade) {
		Properties prop = new Properties();
		String valor = null;
		try {
			prop.load(EstacionamentoUtil.class.getResourceAsStream("/recursos/configuration.txt"));
			valor = prop.getProperty(propriedade);
		} catch (IOException e) {
			assert false : "Configuração não carregada";
		}
		return valor;
	}

	/**
	 * Converte um tipo data hora em string
	 * 
	 * @param dataHoraEntrada data de entrada do veiculo
	 * @return String convertida
	 */
	public static String getDataAsString(LocalDateTime dataHoraEntrada) {
		return dataHoraEntrada.toString();
	}

	/**
	 * Calcula o valor a ser pago baseado na data de entrada e de saida
	 * 
	 * @param movimentacao Instancia da movimentação
	 */
	public static void calcularValorPago(Movimentacao movimentacao) {
		LocalDateTime inicio = movimentacao.getDataHoraEntrada();
		LocalDateTime fim = movimentacao.getDataHoraSaida();
		double valor = 0.0;

		// calcula a diferença de horas entre duas datas
		long diffHoras = inicio.until(fim, ChronoUnit.HOURS);

		if (diffHoras > 0) {
			valor += Tarifario.VALOR_HORA;
			fim = fim.minus(1, ChronoUnit.HOURS);
		}
		// calcula a diferença de minutos entre duas datas
		long diffMinutos = inicio.until(fim, ChronoUnit.MINUTES);

		valor += (diffMinutos / Tarifario.INCREMENTO_MINUTOS) * Tarifario.VALOR_INCREMENTAL;

		movimentacao.setValor(valor);
	}

	/**
	 * Converte uma string em um objeto LocalDateTime
	 * 
	 * @param rdataEntrada Data de entrada do veiculo
	 * @return Data convertida
	 */
	public static LocalDateTime getDate(String rdataEntrada) {
		return LocalDateTime.parse(rdataEntrada, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * Gera um texto com as informações das movimentações para ser exibido
	 * 
	 * @param movimentacoes Um lista de movimentções
	 * @return um texto contendo os dados das movimenações
	 */
	public static String gerarTextoFaturamento(LocalDateTime data, List<Movimentacao> movimentacoes) {
		double totalFaturado = 0.0;
		String texto = "";

		for (Movimentacao movimentacao : movimentacoes) {
			totalFaturado += movimentacao.getValor();
		}

		String sAno = "" + data.getYear();
		String sMes = "" + data.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
		texto = "Faturamento do mês de " + sMes;
		texto += " de " + sAno + " foi de R$ " + totalFaturado;
		return texto;
	}
	
	/**
	 * Converte a data do padrão americano para o padrão nacional
	 * 
	 * @param dataHoraEntrada Data de entrada do veiculo
	 * @return A data formata no padrão nacional
	 */
	public static String getDisplayData(LocalDateTime data) {
		return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
	}
}
