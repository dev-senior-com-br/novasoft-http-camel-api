package br.com.senior.novasoft.http.camel.entities.pedidos.collections;

import br.com.senior.novasoft.http.camel.entities.pedidos.Pedido;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection(serialization = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PedidoCollection {

    /**
     * Lista de pedidos
     */
    private List<Pedido> pedidos = new ArrayList<>();
}