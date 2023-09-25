package com.alandev.estacionamento.apresentacao;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import com.alandev.estacionamento.controle.EstacionamentoController;
import com.alandev.estacionamento.controle.EstacionamentoException;
import com.alandev.estacionamento.controle.VeiculoException;

public class TelaEntradaVeiculo extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JFrame parent;
	private JTextField txtMarca;
	private JTextField txtCor;
	private JTextField txtModelo;
	private JFormattedTextField txfPlaca;
	private JButton btnOk;
	private JButton btnCancel;

	public TelaEntradaVeiculo(JFrame parent) {
		setResizable(false);
		setSize(new Dimension(400, 300));
		setTitle("Entrada de Veículo");
		this.parent = parent;
		getContentPane().setLayout(null);

		JLabel lblPlaca = new JLabel("Placa:");
		lblPlaca.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPlaca.setBounds(114, 50, 46, 14);
		getContentPane().add(lblPlaca);

		JLabel lblMarca = new JLabel("Marca:");
		lblMarca.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMarca.setBounds(114, 90, 46, 14);
		getContentPane().add(lblMarca);

		JLabel lblModelo = new JLabel("Modelo:");
		lblModelo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblModelo.setBounds(114, 130, 46, 14);
		getContentPane().add(lblModelo);

		JLabel lblCor = new JLabel("Cor:");
		lblCor.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCor.setBounds(114, 170, 46, 14);
		getContentPane().add(lblCor);

		try {
			txfPlaca = new JFormattedTextField(new MaskFormatter("UUU-####"));
		} catch (ParseException e) {
			assert false : "Padrão de placa inválido!";
		}
		txfPlaca.setColumns(10);
		txfPlaca.setBounds(168, 47, 90, 20);
		getContentPane().add(txfPlaca);

		txtMarca = new JTextField();
		txtMarca.setBounds(168, 87, 90, 20);
		getContentPane().add(txtMarca);
		txtMarca.setColumns(10);

		txtModelo = new JTextField();
		txtModelo.setBounds(168, 127, 90, 20);
		getContentPane().add(txtModelo);
		txtModelo.setColumns(10);

		txtCor = new JTextField();
		txtCor.setBounds(168, 167, 90, 20);
		getContentPane().add(txtCor);
		txtCor.setColumns(10);

		btnOk = new JButton("Ok");
		btnOk.setBounds(100, 220, 89, 23);
		btnOk.addActionListener(this);
		btnOk.setActionCommand("ok");
		getContentPane().add(btnOk);

		btnCancel = new JButton("Cancelar");
		btnCancel.setBounds(200, 220, 89, 23);
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("cancel");
		getContentPane().add(btnCancel);

		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		if(evento.getActionCommand().equals("ok")) {
			EstacionamentoController controle = new EstacionamentoController();
			try {
				controle.processarEntrada(txfPlaca.getText(),
											txtMarca.getText(), 
											txtModelo.getText(),
											txtCor.getText());
				JOptionPane.showMessageDialog(null, "Veículo registrado com sucesso",
						"Entrada de Veículo",JOptionPane.INFORMATION_MESSAGE);
			} catch (EstacionamentoException | VeiculoException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(),
						"Falha na Entrada",JOptionPane.ERROR_MESSAGE);
			}
		}
		this.parent.setVisible(true);
		this.dispose();
	}
}
