package br.com.argo.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;

import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.MGEModelException;
import br.com.sankhya.modelcore.comercial.impostos.ImpostosHelpper;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class ServicePrincipal {

	public void atualizarEnvio(BigDecimal nUnico, String palett, Timestamp dataEnvio, String cliente, BigDecimal estoque, String calibre, String variedade, String codproduto, String descproduto, String desclocal, BigDecimal nUnicoPedido) throws MGEModelException {
		// TODO Auto-generated method stub
		
		JapeSession.SessionHandle hnd = null;
		JapeWrapper pedDAO = JapeFactory.dao("AD_ESTPALLETPEDIDO");
//		DynamicEntityNames
		try {
			
			hnd = JapeSession.open(); // Abertura da sessão do JapeSession
			DynamicVO pedidoVo = pedDAO.create()
				.set("NROUNICO",nUnico)	
				.set("NROPALLET",palett)
				.set("DTENVIO",dataEnvio)
				.set("CLIENTE",cliente)
				.set("ESTOQUE",estoque)
				.set("CALIBRE",calibre)
				.set("VARIEDADE",variedade)
				.set("COD_PRODUTO",codproduto)
				.set("PRODUTO",descproduto)
				.set("DESCRLOCAL",desclocal)
				.set("NROUNICOPED",nUnicoPedido)
				
				.save();  	
			
		} catch (Exception e) {
			MGEModelException.throwMe(e);
		} finally {
			JapeSession.close(hnd);
		}
		
	}
	public void AtualizaStatus(BigDecimal nroUnicoPedido, BigDecimal codPed, String status) throws Exception {
		SessionHandle hnd = JapeSession.open();
		hnd.setFindersMaxRows(-1);
		EntityFacade entity = EntityFacadeFactory.getDWFFacade();
		JdbcWrapper jdbc = entity.getJdbcWrapper();
		jdbc.openSession();
		if (nroUnicoPedido == null) {
			throw new IllegalArgumentException("O número único do pedido não pode ser nulo.");
		}
		try {
			NativeSql sql = new NativeSql(jdbc);
			sql.appendSql("UPDATE AD_ESTPALLETPEDIDO SET STATUS = :STATUS WHERE NROUNICOPED = :NROUNICOPED AND CODPED = :CODPED");
			sql.setNamedParameter("STATUS", status);
			sql.setNamedParameter("NROUNICOPED", nroUnicoPedido);
			sql.setNamedParameter("CODPED", codPed);

			sql.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erro ao atualizar status dos pedidos: " + e.getMessage());
		} finally {
			JdbcWrapper.closeSession(jdbc);
			JapeSession.close(hnd);
		}
	}

	

}
