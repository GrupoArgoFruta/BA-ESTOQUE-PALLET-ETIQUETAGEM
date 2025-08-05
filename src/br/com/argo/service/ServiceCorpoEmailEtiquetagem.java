package br.com.argo.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.auth.AuthenticationInfo;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;
import br.com.sankhya.ws.ServiceContext;

public class ServiceCorpoEmailEtiquetagem {
	String msg;
	ServiceEmailTeste  EmailTest = new ServiceEmailTeste();

	public void CorpoEmailMarcacaoPallet(ContextoAcao ctx, String tabelaHtml,Date dataConsilidacao,Date dataCarregamento, Timestamp dtEtiquetagem) throws Exception {
		JdbcWrapper jdbc = JapeFactory.getEntityFacade().getJdbcWrapper();
		SessionHandle hnd = JapeSession.open();
		NativeSql nativeSql = new NativeSql(jdbc);
		EntityFacade dwfFacade = EntityFacadeFactory.getDWFFacade();
		String usuarioLogadoNome = ((AuthenticationInfo) ServiceContext.getCurrent().getAutentication()).getUsuVO().getNOMEUSU();
		BigDecimal usuarioLogadoID = ((AuthenticationInfo) ServiceContext.getCurrent().getAutentication()).getUserID();
		Timestamp dataAtual = new Timestamp(new Date().getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dataHoraAtualFormatada = sdf.format(dataAtual);
		
		String assunto = "Pedido etiquetagem";
		// Formatar a data do parâmetro
	    String dtConsilidacao = dataConsilidacao != null ? sdf.format(dataConsilidacao) : "Data não informada";
	    String dtCarregamento = dataCarregamento != null ? sdf.format(dataCarregamento) : "Data não informada";
	    String dtaEtiquetagem = dtEtiquetagem != null ? sdf.format(dtEtiquetagem) : "Data não informada";
		// Construindo o valor a ser inserido no campo AD_PROTOCOLO
        String infProtocolo = "Usuário: " + usuarioLogadoNome + "   |  ID: " + usuarioLogadoID + "   |   Data: " + dataHoraAtualFormatada;
        // Pega o valor do parâmetro digitado (como String)
       
		try {
			Registro[] linhas = ctx.getLinhas();
			// Pega o valor do parâmetro digitado (como String)
	        String nroUnicoStr = (String) ctx.getParam("NROUNICO");
	        String obs = (String) ctx.getParam("OBS");

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
			String mensagem = "<!DOCTYPE html>" +
					"<html>" +
					"<head>" +
					"    <meta charset='utf-8'/>" +
					"    <title>Email</title>" +
					"<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">"+
					 "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css\">"+
					"    <style>" +
				      "@import url(https://fonts.googleapis.com/css?family=Roboto:400,500,700,300,100);" +
					"        body { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }" +
					"        .success-box {" +
					"            background-color: #e6ffed;" +
					"            border-left: 5px solid #28a745;" +
					"            padding: 15px;" +
					"            margin-bottom: 20px;" +
					"            border-radius: 5px;" +
					"            color: #155724;" +
					"            display: flex;" +
					"            align-items: center;" +
					"            font-size: 14px;" +
					"        }" +
					"        .success-box img {" +
					"            width: 20px;" +
					"            height: 20px;" +
					"            margin-right: 10px;" +
					"        }" +
					"        table {" +
					"            border-collapse: collapse;" +
					"            width: 100%;" +
					"            background-color: #fff;" +
					"            box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);" +
					"        }" +
					"        th, td {" +
					"            border: 1px solid #ddd;" +
					"            padding: 8px;" +
					"            text-align: center;" +
					"        }" +
					"        th {" +
					"            background-color: #343a40;" +
					"            color: white;" +
					"        }" +
					"        tr:nth-child(even) { background-color: #f2f2f2; }" +
					"    </style>" +
					"</head>" +
					"<body>" +

					"    <table cellpadding='0' cellspacing='0' border='0'>" +
					"        <tr>" +
					"            <td align='center' style='background-color:#1e6533;'>" +
					"                <div style='padding: 10px;'>" +
					"                    <img border='0' style='width:17%;'" +
					"                         src='https://argofruta.com/wp-content/uploads/2021/05/Logo-text-green.png' alt='Logo'>" +
					"                </div>" +
					"            </td>" +
					"        </tr>" +
					"    </table>" +

					"    <div class='success-box'>" +
					"        <img src='https://img.icons8.com/color/48/checked--v1.png' alt='check' style='width:16px;height:16px;margin-right:6px;'>" +
					       "<strong>" + infProtocolo + "       &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp; Data de consolidação : " + dtConsilidacao + 
					  "   &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp; Data do carregamento : " + dtCarregamento +
					  "   &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp; Data do Etiquetagem : " + dtaEtiquetagem +
					       "</strong><br>"+
					"    </div>" +
					"<p style= margin-top: 4px; font-weight: bold;>Detalhes do Pedido: Serviço: "+descricao+"</p>"+
					"<p style='margin-top: 4px; font-weight: bold;'>Observação do Serviço: " + 
				    (obs != null && !obs.trim().isEmpty() ? obs : "Nenhuma observação informada.") + 
				    "</p>" +

					      tabelaHtml + // sua tabela HTML com os dados

					"</body>" +
					"</html>";


                
		
			EmailTest.enviarEmail(assunto, mensagem);
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
	        JapeSession.close(hnd);
	        JdbcWrapper.closeSession(jdbc);
	        NativeSql.releaseResources(nativeSql);
	    }

	}
}
