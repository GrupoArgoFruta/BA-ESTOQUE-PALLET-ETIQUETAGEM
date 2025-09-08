# Estoque Pallet


![Logo da ArgoFruta](https://argofruta.com/wp-content/uploads/2021/05/Logo-text-white-1.png)
# 🔹 Objetivo

Essa rotina Java implementa a interface AcaoRotinaJava no Sankhya e é usada para gerar uma tabela com os pallets selecionados, atualizar os registros no banco e enviar um e-mail com os dados consolidados.

# 🔹 Fluxo da Ação

## Captura das linhas selecionadas

- O sistema pega todos os registros (ctx.getLinhas()) que o usuário marcou na tela.

## Configuração de serviços auxiliares

- ServiceCorpoEmailEtiquetagem → Responsável por montar e enviar o e-mail.

- ServicePrincipal → Responsável por atualizar dados relacionados ao envio do pallet.

- Montagem da Tabela HTML

# Cria um HTML em formato de tabela com as colunas:

- Data pallet

- Nro. Único

- Pallet

- Cliente

- Quantidade

- Calibre

- Variedade

- Código do Produto

- Descrição do Produto

- Local

- Loop pelos registros

- Para cada registro selecionado:

- Extrai os campos necessários (DTPALETIZACAO, NROUNICO, NROPALLET, CLIENTE, etc.).

## Adiciona uma linha na tabela HTML.

- Chama o método atualizarEnvio(...) em ServicePrincipal para salvar informações no banco.

- Envio do e-mail

- Após montar a tabela, pega as datas informadas no contexto (DTCONSILIDACAO, DTCARREGAMENTO, DTETIQUETAGEM).

- Chama ServiceEmail.CorpoEmailMarcacaoPallet(...) para enviar o e-mail com a tabela e as datas.

## Tratamento de Erros

- Se ocorrer alguma falha, a ação exibe mensagem de erro com ctx.mostraErro(...).

# 🔹 Estrutura da Tabela no E-mail
- Data pallet	N.Pallet	Pallet	Cliente	Qtd	Calibre	Variedade	Cod.Produto	Desc.Produto	Desc.Local

- Cada pallet selecionado gera uma linha nessa tabela.

# 🔹 Pontos de Atenção

- Fechamento da sessão (JapeSession): no código atual, a sessão hnd não é fechada. O ideal é fechar no finally (JapeSession.close(hnd)).

- Validação de nulos: alguns campos podem vir nulos e causar NullPointerException ao formatar datas.

- Envio de e-mail: depende do serviço ServiceCorpoEmailEtiquetagem já estar configurado para enviar corretamente.

# 🔹 Exemplo de Uso

- Usuário seleciona pallets na tela.

- Executa a rotina PrincipalEstoquePallet.

## O sistema:

- Atualiza o status/envio dos pallets no banco.

- Monta uma tabela HTML com os pallets.

- Envia um e-mail com a tabela e as datas de consolidação, carregamento e etiquetagem.

  ###  USUARIO
- USUARIO RESPONSAVEL: Mateus Oliveira dos Santos
 
- EMAIL : mateus.oliveira@argofruta.com
  
