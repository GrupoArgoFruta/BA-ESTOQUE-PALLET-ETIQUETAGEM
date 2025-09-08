package br.com.argo.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.argo.repository.ServicePrincipal;
import br.com.argo.service.ServiceCorpoEmailEtiquetagem;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.modelcore.comercial.impostos.ImpostosHelpper;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class PrincipalEstoquePallet implements AcaoRotinaJava{

	@Override
	public void doAction(ContextoAcao ctx) throws Exception {
		// TODO Auto-generated method stub
		Registro[] linhas = ctx.getLinhas();
	    JdbcWrapper jdbc = JapeFactory.getEntityFacade().getJdbcWrapper();
	    SessionHandle hnd = JapeSession.open(); // Sempre feche depois
	    ServiceCorpoEmailEtiquetagem ServiceEmail = new ServiceCorpoEmailEtiquetagem();
	    ServicePrincipal Servicos = new ServicePrincipal();
	   try {
		// Construir mensagem de sucesso
		   // Construir a tabela HTML com os dados das notas
	        StringBuilder tabelaHtml = new StringBuilder();
	        SimpleDateFormat dataFormatado = new SimpleDateFormat("dd/MM/yyyy");
	        tabelaHtml.append("<table>")
            .append("<tr>")
            .append("<th>Data pallet</th>")
            .append("<th>N.Pallet</th>")
            .append("<th>Pallet</th>")
            .append("<th>Cliente</th>")
            .append("<th>Qtd</th>")
            .append("<th>Calibre</th>")
            .append("<th>Variedade</th>")
            .append("<th>Cod.Produto</th>")
            .append("<th>Desc.Produto</th>")
            .append("<th>Desc.Local</th>")
            .append("</tr>");
			for (Registro registro : linhas) {
				Timestamp dataEnvio = new Timestamp(new Date().getTime());
				Timestamp DtPalletizacao = (Timestamp) registro.getCampo("DTPALETIZACAO");
				BigDecimal nUnico = (BigDecimal) registro.getCampo("NROUNICO");
				BigDecimal nUnicoPedido = (BigDecimal) registro.getCampo("NROUNICOPED");
				String palett = (String) registro.getCampo("NROPALLET");
				String produtor = (String) registro.getCampo("PRODUTOR");
				BigDecimal estoque = (BigDecimal) registro.getCampo("QTDEPLT");
				String calibre = (String) registro.getCampo("CALIBRE");
				String variedade = (String) registro.getCampo("VARIEDADE");
				String codproduto = (String) registro.getCampo("COD_PRODUTO");
				String descproduto = (String) registro.getCampo("PRODUTO");
				String desclocal = (String) registro.getCampo("DESCRLOCAL");
				String cliente = (String) registro.getCampo("CLIENTE");
				
				tabelaHtml.append("<tr>")
                .append("<td>").append(dataFormatado.format(DtPalletizacao)).append("</td>")
                .append("<td>").append(nUnico).append("</td>")
                .append("<td>").append(palett).append("</td>")
                .append("<td>").append(cliente).append("</td>")
                .append("<td>").append(estoque).append("</td>")
                .append("<td>").append(calibre).append("</td>")
                .append("<td>").append(variedade).append("</td>")
                .append("<td>").append(codproduto).append("</td>")
                .append("<td>").append(descproduto).append("</td>")
                .append("<td>").append(desclocal).append("</td>")
                .append("</tr>");
				
				 Servicos.atualizarEnvio(nUnico,palett,dataEnvio,cliente,estoque,calibre,variedade,codproduto,descproduto,desclocal,nUnicoPedido); 

	        }
			 tabelaHtml.append("</table>");
			 Timestamp dtConsilidacao = (Timestamp) ctx.getParam("DTCONSILIDACAO");
			 Timestamp dtCarregamento = (Timestamp) ctx.getParam("DTCARREGAMENTO");
			 Timestamp dtEtiquetagem = (Timestamp) ctx.getParam("DTETIQUETAGEM");
			 ServiceEmail.CorpoEmailMarcacaoPallet(ctx, tabelaHtml.toString(),dtConsilidacao,dtCarregamento,dtEtiquetagem);
			
	} catch (Exception e) {
		// TODO: handle exception
		   ctx.mostraErro("Erro ao enviar o e-mail: " + e.getMessage());
	        e.printStackTrace();
	}
	}
	

}
