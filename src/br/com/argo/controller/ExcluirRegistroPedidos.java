package br.com.argo.controller;

import java.math.BigDecimal;
import java.sql.SQLException;

import br.com.argo.repository.ServicePrincipal;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.modelcore.MGEModelException;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class ExcluirRegistroPedidos implements AcaoRotinaJava{

	@Override
	public void doAction(ContextoAcao ctx) throws Exception {
		// TODO Auto-generated method stub
		Registro[] linhas = ctx.getLinhas();
		ServicePrincipal Servicos = new ServicePrincipal();
		  JdbcWrapper jdbc = JapeFactory.getEntityFacade().getJdbcWrapper();
		    SessionHandle hnd = JapeSession.open(); // Sempre feche depois
		    try {
		    	for (Registro registro : linhas) {
		    		BigDecimal codpedido = (BigDecimal) registro.getCampo("CODPED");
		    		BigDecimal nUnico = (BigDecimal) registro.getCampo("NROUNICO");
					BigDecimal nUnicoPedido = (BigDecimal) registro.getCampo("NROUNICOPED");
		            // Chamada para deletar o item
		            deletarItemNota(codpedido);

		            ctx.setMensagemRetorno(
		                    "Registro deletado com sucesso! Nro unico: " + nUnico + 
		                    " | Numero do Pedido Marcado: " + nUnicoPedido
		                );
		    	}
				
			} catch (Exception e) {
				// TODO: handle exception
				   ctx.mostraErro("Erro ao enviar o e-mail: " + e.getMessage());
			        e.printStackTrace();
			}
	}
	
	private void deletarItemNota(BigDecimal codpedido) throws SQLException, MGEModelException {
	    JdbcWrapper jdbc = JapeFactory.getEntityFacade().getJdbcWrapper();
	    NativeSql sql = new NativeSql(jdbc);

	    try {
	        sql.appendSql("DELETE FROM AD_ESTPALLETPEDIDO WHERE CODPED = :CODPED");
	        sql.setNamedParameter("CODPED", codpedido);
	        sql.executeUpdate();
	    } catch (Exception e) {
	        MGEModelException.throwMe(e);
	    } finally {
	        NativeSql.releaseResources(sql);
	    }
	}
}
