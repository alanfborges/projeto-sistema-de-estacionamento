package com.alandev.estacionamento.negocio;

import java.time.LocalDateTime;

/**
 * Representa o fluxo do veículo dentro do estacionamento, ou seja ele contem as
 * informações de entrada e saída do veículo eo valor pago na estada.
 * 
 * @author alan
 */
public class Movimentacao {

	private Veiculo veiculo;
	private LocalDateTime dataHoraEntrada;
	private LocalDateTime dataHoraSaida;
	private double valor;

	public Movimentacao(Veiculo veiculo, LocalDateTime dataHoraEntrada) {
		this.veiculo = veiculo;
		this.dataHoraEntrada = dataHoraEntrada;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public LocalDateTime getDataHoraEntrada() {
		return dataHoraEntrada;
	}

	public void setDataHoraEntrada(LocalDateTime dataHoraEntrada) {
		this.dataHoraEntrada = dataHoraEntrada;
	}

	public LocalDateTime getDataHoraSaida() {
		return dataHoraSaida;
	}

	public void setDataHoraSaida(LocalDateTime dataHoraSaida) {
		this.dataHoraSaida = dataHoraSaida;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}
}
