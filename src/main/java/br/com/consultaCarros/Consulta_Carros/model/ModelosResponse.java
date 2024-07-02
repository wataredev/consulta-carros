package br.com.consultaCarros.Consulta_Carros.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelosResponse {
    private List<ModeloVeiculos> modelos;

    public List<ModeloVeiculos> getModelos() {
        return modelos;
    }

    public void setModelos(List<ModeloVeiculos> modelos) {
        this.modelos = modelos;
    }
}
