package br.com.argo.controller;

import java.math.BigDecimal;

import br.com.argo.repository.ServicePrincipal;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.wrapper.JapeFactory;

public class AtualizarStatus implements AcaoRotinaJava{

	@Override
	public void doAction(ContextoAcao ctx) throws Exception {
		// TODO Auto-generated method stub
		Registro[] linhas = ctx.getLinhas();
	    JdbcWrapper jdbc = JapeFactory.getEntityFacade().getJdbcWrapper();
	    SessionHandle hnd = JapeSession.open(); // Sempre feche depois
	    ServicePrincipal servicos = new ServicePrincipal();
	    try {
			String statusParam = (String) ctx.getParam("OPCAO");

			if (!"P".equals(statusParam) && !"F".equals(statusParam)) {
				ctx.setMensagemRetorno("Status inválido. Use 'P' ou 'F'.");
				return;
			}

			for (Registro registro : linhas) {
				BigDecimal nroUnicoPedido = (BigDecimal) registro.getCampo("NROUNICOPED");
				BigDecimal codPed = (BigDecimal) registro.getCampo("CODPED");

				// Chama o método passando o status escolhido
				servicos.AtualizaStatus(nroUnicoPedido, codPed, statusParam);
			}
	    	
		} catch (Exception e) {
			// TODO: handle exception
			 ctx.mostraErro("Erro ao atualizar status: " + e.getMessage());
		        e.printStackTrace();
		}
	}

}
