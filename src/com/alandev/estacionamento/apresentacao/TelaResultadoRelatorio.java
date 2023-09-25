package com.alandev.estacionamento.apresentacao;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.alandev.estacionamento.negocio.Movimentacao;
import com.alandev.estacionamento.utilitario.EstacionamentoUtil;

public class TelaResultadoRelatorio extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final JButton btnOk = new JButton("Ok");
	private JTable tblFaturamento;
	private JFrame parent;

	public TelaResultadoRelatorio(TelaInicialRelatorio telaInicialRelatorio, List<Movimentacao> movimentacoes,
			LocalDateTime data) {
		this.parent = telaInicialRelatorio;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(new Dimension(600, 300));
		setResizable(false);
		setTitle("Relatório de Faturamento");

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		btnOk.addActionListener(this);
		panel.add(btnOk);

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.SOUTH);

		String textoFaturamento = EstacionamentoUtil.gerarTextoFaturamento(data, movimentacoes);

		JLabel lblTextoFaturamento = new JLabel(textoFaturamento);
		lblTextoFaturamento.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_2.add(lblTextoFaturamento);

		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, BorderLayout.CENTER);

		Object[][] conteudoFaturamento = preencherTabela(movimentacoes);
		tblFaturamento = new JTable();
		tblFaturamento.setModel(
				new DefaultTableModel(conteudoFaturamento, new String[] { "Placa", "Entrada", "Sa\u00EDda", "Valor" }));
		scrollPane.setViewportView(tblFaturamento);

		setLocationRelativeTo(null);
	}

	private Object[][] preencherTabela(List<Movimentacao> movimentacoes) {
		Object[][] conteudo = new Object[movimentacoes.size()][4];

		for (int i = 0; i < movimentacoes.size(); i++) {
			conteudo[i][0] = movimentacoes.get(i).getVeiculo().getPlaca();
			conteudo[i][1] = EstacionamentoUtil.getDisplayData(movimentacoes.get(i).getDataHoraEntrada());
			conteudo[i][2] = EstacionamentoUtil.getDisplayData(movimentacoes.get(i).getDataHoraSaida());
			conteudo[i][3] = movimentacoes.get(i).getValor();

		}

		return conteudo;
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		parent.setVisible(true);
		dispose();

	}

}
