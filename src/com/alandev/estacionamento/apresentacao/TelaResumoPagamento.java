package com.alandev.estacionamento.apresentacao;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.alandev.estacionamento.negocio.Movimentacao;
import com.alandev.estacionamento.utilitario.EstacionamentoUtil;

public class TelaResumoPagamento extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JFrame parent;

	public TelaResumoPagamento(Movimentacao movimentacao, JFrame parent) {
		this.parent = parent;
		getContentPane().setFont(new Font("Dialog", Font.BOLD, 12));
		setSize(new Dimension(430, 300));
		setResizable(false);
		setTitle(" Resumo de Pagamento");
		getContentPane().setLayout(null);

		JLabel lblPlaca = new JLabel("Placa:");
		lblPlaca.setFont(new Font("Dialog", Font.BOLD, 12));
		lblPlaca.setBounds(110, 40, 46, 14);
		getContentPane().add(lblPlaca);

		JLabel lblDataEntrada = new JLabel("Entrada:");
		lblDataEntrada.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDataEntrada.setBounds(110, 80, 59, 14);
		getContentPane().add(lblDataEntrada);

		JLabel lblDataSaida = new JLabel("Saída:");
		lblDataSaida.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDataSaida.setBounds(110, 120, 46, 14);
		getContentPane().add(lblDataSaida);

		JLabel lblValor = new JLabel("Valor:");
		lblValor.setFont(new Font("Dialog", Font.BOLD, 12));
		lblValor.setBounds(110, 160, 46, 14);
		getContentPane().add(lblValor);

		String sPlaca = movimentacao.getVeiculo().getPlaca();
		JLabel lblValPlaca = new JLabel(sPlaca);
		lblValPlaca.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblValPlaca.setBounds(210, 40, 133, 14);
		getContentPane().add(lblValPlaca);

		String sEntrada = EstacionamentoUtil.getDisplayData(movimentacao.getDataHoraEntrada());
		JLabel lblValDataEntrada = new JLabel(sEntrada);
		lblValDataEntrada.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblValDataEntrada.setBounds(210, 80, 133, 14);
		getContentPane().add(lblValDataEntrada);

		String sSaida = EstacionamentoUtil.getDisplayData(movimentacao.getDataHoraSaida());
		JLabel lblValDataSaida = new JLabel(sSaida);
		lblValDataSaida.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblValDataSaida.setBounds(210, 120, 133, 14);
		getContentPane().add(lblValDataSaida);

		String sValor = "R$ " + movimentacao.getValor();
		JLabel lblValValo = new JLabel(sValor);
		lblValValo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblValValo.setBounds(210, 160, 133, 14);
		getContentPane().add(lblValValo);

		JButton btnOk = new JButton("Ok");
		btnOk.setBounds(140, 210, 89, 23);
		btnOk.addActionListener(this);
		getContentPane().add(btnOk);

		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		parent.setVisible(true);
		dispose();

	}
}
