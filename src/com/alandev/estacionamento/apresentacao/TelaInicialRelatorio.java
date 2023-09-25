package com.alandev.estacionamento.apresentacao;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.alandev.estacionamento.controle.EstacionamentoController;
import com.alandev.estacionamento.negocio.Movimentacao;

public class TelaInicialRelatorio extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	private JComboBox cboAno;
	
	@SuppressWarnings("rawtypes")
	private JComboBox cboMes;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TelaInicialRelatorio() {
		getContentPane().setFont(new Font("Dialog", Font.BOLD, 14));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(600, 150));
		setResizable(false);
		setTitle("Filtro do Relatório");
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 15, 40));

		JLabel lblAno = new JLabel("Ano:");
		lblAno.setFont(new Font("Dialog", Font.BOLD, 14));
		getContentPane().add(lblAno);

		cboAno = new JComboBox();
		cboAno.setModel(new DefaultComboBoxModel(new String[] { "2023", "2022", "2021" }));
		cboAno.setFont(new Font("Tahoma", Font.PLAIN, 14));
		getContentPane().add(cboAno);

		JLabel lblMes = new JLabel("Mês:");
		lblMes.setFont(new Font("Dialog", Font.BOLD, 14));
		getContentPane().add(lblMes);

		cboMes = new JComboBox();
		cboMes.setModel(new DefaultComboBoxModel(new String[] { "Janeiro", "Fevereiro", "Março", "Abril", "Maio",
				"Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" }));
		cboMes.setFont(new Font("Tahoma", Font.PLAIN, 14));
		getContentPane().add(cboMes);

		JButton btnGerar = new JButton("Gerar");
		btnGerar.addActionListener(this);
		btnGerar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		getContentPane().add(btnGerar);

		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		// recupera do combo o ano e mes escolhido
		int ano = Integer.parseInt((String) cboAno.getSelectedItem());
		int mes = (Integer) cboMes.getSelectedIndex() + 1;

		// byscar movimentaoes do mes e ano informados
		EstacionamentoController controle = new EstacionamentoController();
		LocalDateTime data = LocalDateTime.of(ano, mes, 1, 0, 0);
		List<Movimentacao> movimentacoes = controle.emitirRelatorio(data);

		// exibe a tela de conteudo e faturamento
		TelaResultadoRelatorio relatorio = new TelaResultadoRelatorio(this, movimentacoes, data);
		relatorio.setVisible(true);
		dispose();

	}

}
