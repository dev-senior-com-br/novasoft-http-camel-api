package br.com.senior.novasoft.http.camel.entities.pedidos;

import br.com.senior.novasoft.http.camel.entities.RequestError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.camel.component.jackson.JacksonDataFormat;

/**
 * <h1>DTO da API de Pedidos da Novasoft</h1>
 *
 * <p>DTO retornado da API de Pedidos
 * da Novasoft.</p>
 *
 * @author lucas.nunes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection(serialization = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pedido extends RequestError {

    public static final JacksonDataFormat JACKSON_DATA_FORMAT = new JacksonDataFormat(Pedido.class);

    /**
     * Somente o
     * ano da data.
     */
    @JsonProperty("anoDoc")
    private String anoDoc;

    /**
     * Somente o
     * mes da data.
     */
    @JsonProperty("perDoc")
    private String perDoc;

    /**
     * Data completa
     */
    @JsonProperty("fecha")
    private String fecha;

    /**
     * Numero do pedido
     */
    @JsonProperty("numDoc")
    private String numDoc;

    /**
     * Registro do documento
     */
    @JsonProperty("regDoc")
    private String regDoc;

    /**
     * Responsável
     */
    @JsonProperty("vendedor")
    private String vendedor;

    /**
     * Conta
     */
    @JsonProperty("cliente")
    private String cliente;

    /**
     * Nome da oportunidade
     */
    @JsonProperty("obsOrc")
    private String obsOrc;

    /**
     * Cidade
     */
    @JsonProperty("ciuDoc")
    private String ciuDoc;

    /**
     * Estado
     */
    @JsonProperty("depDoc")
    private String depDoc;

    /**
     * Pais
     */
    @JsonProperty("paiDoc")
    private String paiDoc;

    /**
     * Produto
     */
    @JsonProperty("item")
    private String item;

    /**
     * Quantidade
     */
    @JsonProperty("cantidad")
    private String cantidad;

    /**
     * Valor da venda
     */
    @JsonProperty("preVta")
    private String preVta;

    /**
     * Percentual desconto
     */
    @JsonProperty("porDes")
    private String porDes;

    /**
     * Depósito
     */
    @JsonProperty("bodega")
    private String bodega;

    /**
     * Data completa
     */
    @JsonProperty("fecTas")
    private String fecTas;

    /**
     * Data completa
     */
    @JsonProperty("venLote")
    private String venLote;

    /**
     * Data completa
     */
    @JsonProperty("fecEnt")
    private String fecEnt;

    /**
     * Subtipo
     */
    @JsonProperty("subTip")
    private String subTip = "005";

    /**
     * Tipo documento
     */
    @JsonProperty("tipDoc")
    private String tipDoc = "005";

    /**
     * Filial no ERP
     */
    @JsonProperty("codSuc")
    private String codSuc;

    /**
     *
     */
    @JsonProperty("CodAlu")
    private String CodAlu = "0";

    /**
     * Centro de custo
     */
    @JsonProperty("codCco")
    private String codCco;

    /**
     * Clasificacion 1
     */
    @JsonProperty("codCl1")
    private String codCl1;

    /**
     * Clasificacion 2
     */
    @JsonProperty("codCl2")
    private String codCl2;

    /**
     * Clasificacion 3
     */
    @JsonProperty("codCl3")
    private String codCl3;

    /**
     * Provedor
     */
    @JsonProperty("provee")
    private String provee = "0";

    /**
     * Lista
     */
    @JsonProperty("lista")
    private String lista = "0";

    /**
     * Prazo de dias
     */
    @JsonProperty("diaPla")
    private String diaPla = "0";

    /**
     * Tipo de Moeda
     */
    @JsonProperty("indMp")
    private String indMp = "00";

    /**
     * Taxa
     */
    @JsonProperty("tasa")
    private String tasa = "0";

    /**
     * Fatura do fornecedor
     */
    @JsonProperty("facPro")
    private String facPro = "0";

    /**
     * Código de Caixa
     */
    @JsonProperty("codCaja")
    private String codCaja = "0";

    /**
     * Vinícola de destino
     */
    @JsonProperty("bodDes")
    private String bodDes = "0";

    /**
     * Quantidade de Unidade
     */
    @JsonProperty("cantUni")
    private String cantUni = "0";

    /**
     * Alterar
     */
    @JsonProperty("altero")
    private String altero = "0";

    /**
     * Fator de conversão
     */
    @JsonProperty("facCon")
    private String facCon = "0";

    /**
     * Fator de conversão
     */
    @JsonProperty("cosUni")
    private String cosUni = "0";

    /**
     * Porcentagem de IVA
     */
    @JsonProperty("porIva")
    private String porIva = "0";

    /**
     * Porcentagem de IVA
     * gravado
     */
    @JsonProperty("porIvaNg")
    private String porIvaNg = "0";

    /**
     * Porcentagem de Retenção
     */
    @JsonProperty("porRet")
    private String porRet = "0";

    /**
     * Porcentagem de Comissão
     */
    @JsonProperty("porCom")
    private String porCom = "0";

    /**
     * Custo unitário ajustado
     */
    @JsonProperty("cosUnAI")
    private String cosUnAI = "0";

    /**
     * Filial de Destino
     */
    @JsonProperty("sucDes")
    private String sucDes = "0";

    /**
     * Código do lote
     */
    @JsonProperty("codLote")
    private String codLote = "0";

    /**
     * Código do lote
     */
    @JsonProperty("indTra")
    private String indTra = "0";

    /**
     * Indicador de Transferência
     */
    @JsonProperty("asigNum")
    private Boolean asigNum = Boolean.FALSE;

    /**
     * Indicador Faturável
     */
    @JsonProperty("indRefac")
    private Boolean indRefac = Boolean.FALSE;

    /**
     * Código da Convenção
     */
    @JsonProperty("codConv")
    private String codConv = "0";

    /**
     * Código da Filial de
     * Acordo
     */
    @JsonProperty("convSuc")
    private String convSuc = "0";

    /**
     * Contrato de código de custo
     */
    @JsonProperty("convCco")
    private String convCco = "0";

    /**
     * Código classificador 1
     * Acordo
     */
    @JsonProperty("convCl1")
    private String convCl1 = "0";

    /**
     * Código classificador 2
     * Acordo
     */
    @JsonProperty("convCl2")
    private String convCl2 = "0";

    /**
     * Código classificador 3
     * Acordo
     */
    @JsonProperty("convCl3")
    private String convCl3 = "0";

    /**
     * Número da fatura
     * Serviços temporários
     */
    @JsonProperty("numFact")
    private String numFact = "0";

    /**
     * Número do pedido
     * Cobrança
     */
    @JsonProperty("ordFact")
    private String ordFact = "0";

    /**
     * Porcentagem de Gestão
     */
    @JsonProperty("porAdm")
    private String porAdm = "0";

    /**
     * Porcentagem de Imposto
     */
    @JsonProperty("porImp")
    private String porImp = "0";

    /**
     * Porcentagem de Utilitário
     */
    @JsonProperty("porUti")
    private String porUti = "0";

    /**
     * Administração de valor
     */
    @JsonProperty("monAdm")
    private String monAdm = "0";

    /**
     * Valor do imposto
     */
    @JsonProperty("monImp")
    private String monImp = "0";

    /**
     * Valor de utilidade
     */
    @JsonProperty("monUti")
    private String monUti = "0";

    /**
     * Número do documento
     * atribuído na
     * importação de
     * documento
     */
    @JsonProperty("asigNumero")
    private String asigNumero = "0";

    /**
     * Modelo de Distribuição
     */
    @JsonProperty("indAfe")
    private String indAfe = "0";

    /**
     * Código de terceiros
     */
    @JsonProperty("codTer")
    private String codTer = "0";

    /**
     * Retenção de IVA
     */
    @JsonProperty("tarRii")
    private String tarRii = "0";

    /**
     * Nº de retenção de IVA
     * Gravado
     */
    @JsonProperty("tarRiiNg")
    private String tarRiiNg = "0";

    /**
     * Porcentagem de imposto
     * Soma 1
     */
    @JsonProperty("porSum1")
    private String porSum1 = "0";

    /**
     * Porcentagem de imposto
     * Soma 2
     */
    @JsonProperty("porSum2")
    private String porSum2 = "0";

    /**
     * Porcentagem de imposto
     * Soma 3
     */
    @JsonProperty("porSum3")
    private String porSum3 = "0";

    /**
     * Porcentagem de imposto
     * Soma 4
     */
    @JsonProperty("porSum4")
    private String porSum4 = "0";

    /**
     * Porcentagem de imposto
     * Subtrair 1
     */
    @JsonProperty("porRes1")
    private String porRes1 = "0";

    /**
     * Porcentagem de imposto
     * Subtrair 2
     */
    @JsonProperty("porRes2")
    private String porRes2 = "0";

    /**
     * Porcentagem de imposto
     * Subtrair 3
     */
    @JsonProperty("porRes3")
    private String porRes3 = "0";

    /**
     * Porcentagem de imposto
     * Subtrair 4
     */
    @JsonProperty("porRes4")
    private String porRes4 = "0";

    /**
     * Base AIU
     */
    @JsonProperty("baseIvaAiu")
    private String baseIvaAiu = "0";

    /**
     * Código Retenção
     */
    @JsonProperty("codRet")
    private String codRet = "0";

    /**
     * Número de contagem física
     */
    @JsonProperty("conteo")
    private String conteo = "0";

    /**
     * Taxa de desconto
     * contado
     */
    @JsonProperty("type")
    private String porCtd = "0";

    /**
     * Valor de desconto
     * contado
     */
    @JsonProperty("valCtd")
    private String valCtd = "0";

    /**
     * Desconto de volume
     */
    @JsonProperty("desVol")
    private String desVol = "0";

    /**
     * Taxa de desconto
     * por volume
     */
    @JsonProperty("porVol")
    private String porVol = "0";

    /**
     * Valor total em dinheiro
     */
    @JsonProperty("valTcd")
    private String valTcd = "0";

    /**
     * Código classificador 4
     * Acordo
     */
    @JsonProperty("convCl4")
    private String convCl4 = "0";

    /**
     * Código classificador 5
     * Acordo
     */
    @JsonProperty("convCl5")
    private String convCl5 = "0";

    /**
     * Código classificador 6
     * Acordo
     */
    @JsonProperty("convCl6")
    private String convCl6 = "0";

    /**
     * Código classificador 7
     * Acordo
     */
    @JsonProperty("convCl7")
    private String convCl7 = "0";

    /**
     * Código classificador 8
     * Acordo
     */
    @JsonProperty("convCl8")
    private String convCl8 = "0";

    /**
     * Descrição do registro
     */
    @JsonProperty("descripCue")
    private String descripCue = "0";

    /**
     * Filial do cliente
     */
    @JsonProperty("sucCli")
    private String sucCli = "0";

    /**
     * Filial do Provedor
     */
    @JsonProperty("sucProv")
    private String sucProv = "0";

    /**
     * Código de ICA
     */
    @JsonProperty("codIca")
    private String codIca = "0";

    /**
     * Custo do valor do registro
     * Fiscal
     */
    @JsonProperty("valDefFis")
    private String valDefFis = "0";

    /**
     * Código de conceito para
     * saídas
     */
    @JsonProperty("codCon")
    private String codCon = "0";

    /**
     * Ano do pedido
     */
    @JsonProperty("anoPed")
    private String anoPed = "0";

    /**
     * Período do pedido
     */
    @JsonProperty("perPed")
    private String perPed = "0";

    /**
     * Subtipo do Pedido
     */
    @JsonProperty("subPed")
    private String subPed = "0";

    /**
     * Número do pedido
     */
    @JsonProperty("pedido")
    private String pedido = "0";

    /**
     * Pedido de registro
     */
    @JsonProperty("regPed")
    private String regPed = "0";

    /**
     * Ano despacho
     */
    @JsonProperty("anoDes")
    private String anoDes = "0";

    /**
     * Período Despacho
     */
    @JsonProperty("perDes")
    private String perDes = "0";

    /**
     * Subtipo despacho
     */
    @JsonProperty("subDes")
    private String subDes = "0";

    /**
     * Número de despacho
     */
    @JsonProperty("despa")
    private String despa = "0";

    /**
     * Registro de despacho
     */
    @JsonProperty("regDes")
    private String regDes = "0";

    /**
     * Resolução Nº.
     */
    @JsonProperty("nroResol")
    private String nroResol = "0";

    /**
     * Tipo de fatura
     */
    @JsonProperty("tipoFact")
    private String tipoFact = "0";

    /**
     * Tipo de Operação
     */
    @JsonProperty("tipoOper")
    private String tipoOper = "0";

    /**
     * Ordem de compra do cliente
     */
    @JsonProperty("oComCli")
    private String oComCli = "0";

    /**
     * Contrato de quadro principal
     */
    @JsonProperty("contMarcoCab")
    private String contMarcoCab = "0";

    /**
     * Valor da última compra
     */
    @JsonProperty("valUltCom")
    private String valUltCom = "0";

    /**
     * Contrato-Quadro do Corpo
     */
    @JsonProperty("contMarcoCue")
    private String contMarcoCue = "0";

    /**
     * Ano de referência
     */
    @JsonProperty("anoRef")
    private String anoRef = "0";

    /**
     * Período referência
     */
    @JsonProperty("perRef")
    private String perRef = "0";

    /**
     * Subtipo de referência
     */
    @JsonProperty("subRef")
    private String subRef = "0";

    /**
     * Número de referência
     */
    @JsonProperty("numRef")
    private String numRef = "0";

    /**
     * Referência de registro
     */
    @JsonProperty("regRef")
    private String regRef = "0";

    /**
     * Numero do Pedido
     */
    @JsonProperty("numRefPag")
    private String numRefPag;
}