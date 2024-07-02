package br.com.consultaCarros.Consulta_Carros.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosVeiculos(@JsonAlias("codigo") String codigoCarro,
                            @JsonAlias("nome") String nomeCarro) {

}
