package br.com.argo.util;

import java.math.BigDecimal;
import java.sql.Timestamp;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;

public class UtilTestes implements AcaoRotinaJava{

	@Override
	public void doAction(ContextoAcao ctx) throws Exception {
	    SessionHandle hnd = JapeSession.open();

	    try {
	        // Pega o valor do parâmetro digitado (como String)
	        String nroUnicoStr = (String) ctx.getParam("NROUNICO");

	        if (nroUnicoStr == null || nroUnicoStr.trim().isEmpty()) {
	            ctx.setMensagemRetorno("Nenhum valor foi informado para NROUNICO.");
	            return;
	        }

	        // Converte para BigDecimal
	        BigDecimal nroUnico = new BigDecimal(nroUnicoStr);

	        // Busca o registro na entidade
	        JapeWrapper servicoDAO = JapeFactory.dao("AD_SRVPALLET");
	        DynamicVO servicoVO = servicoDAO.findByPK(nroUnico);

	        String descricao = servicoVO.asString("DESCRICAO");

	        ctx.setMensagemRetorno("Código pesquisado: " + nroUnico + "\nDescrição encontrada: " + descricao);

	    } catch (Exception e) {
	        ctx.setMensagemRetorno("Erro ao buscar o serviço: " + e.getMessage());
	    } finally {
	        JapeSession.close(hnd);
	    }
	}


	

}
