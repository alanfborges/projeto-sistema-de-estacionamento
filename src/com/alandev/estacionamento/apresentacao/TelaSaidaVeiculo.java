package com.alandev.estacionamento.apresentacao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

import com.alandev.estacionamento.controle.EstacionamentoController;
import com.alandev.estacionamento.controle.EstacionamentoException;
import com.alandev.estacionamento.controle.VeiculoException;
import com.alandev.estacionamento.negocio.Movimentacao;

public class TelaSaidaVeiculo extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JFrame parent;
	private JFormattedTextField txtPlaca;
	private JButton btnOk;
	private JButton btnCancel;

	public TelaSaidaVeiculo(JFrame parent) {
		setResizable(false);
		setTitle("Saída de Veículo");
		setSize(new Dimension(600, 200));
		this.parent = parent;

		JPanel panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setVgap(15);
		getContentPane().add(panel, BorderLayout.SOUTH);

		btnOk = new JButton("Ok");
		btnOk.addActionListener(this);
		btnOk.setActionCommand("ok");
		panel.add(btnOk);

		btnCancel = new JButton("Cancelar");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("cancelar");
		panel.add(btnCancel);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setVgap(35);
		getContentPane().add(panel_1, BorderLayout.CENTER);

		JLabel lblPlaca = new JLabel("Placa:");
		lblPlaca.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel_1.add(lblPlaca);

		try {
			txtPlaca = new JFormattedTextField(new MaskFormatter("UUU-####"));
		} catch (ParseException e) {
			assert false : "Padrão de placa inválido!";
		}
		txtPlaca.setForeground(new Color(0, 0, 255));
		txtPlaca.setFont(new Font("Tahoma", Font.BOLD, 16));
		txtPlaca.setColumns(10);
		panel_1.add(txtPlaca);

		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		// String cmd = evento.getActionCommand();

		if (evento.getActionCommand().equals("ok")) {
			EstacionamentoController controle = new EstacionamentoController();
			Movimentacao movimentacao = null;
			try {
				movimentacao = controle.processarSaida(txtPlaca.getText());
			} catch (VeiculoException | EstacionamentoException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage(), "Falha na Saída", JOptionPane.ERROR_MESSAGE);
			}
			TelaResumoPagamento telaResumo = new TelaResumoPagamento(movimentacao, parent);
			telaResumo.setVisible(true);
		} else {
			parent.setVisible(true);
		}
		dispose();
	}
}