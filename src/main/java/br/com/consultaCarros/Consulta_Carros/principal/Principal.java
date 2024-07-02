package br.com.consultaCarros.Consulta_Carros.principal;

import br.com.consultaCarros.Consulta_Carros.model.DadosVeiculos;
import br.com.consultaCarros.Consulta_Carros.model.ModeloVeiculos;
import br.com.consultaCarros.Consulta_Carros.model.ModelosResponse;
import br.com.consultaCarros.Consulta_Carros.model.Veiculo;
import br.com.consultaCarros.Consulta_Carros.service.ConsumoAPI;
import br.com.consultaCarros.Consulta_Carros.service.ConverteDados;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class Principal {

    private final Scanner scanner = new Scanner(System.in);
    private final ConsumoAPI consumo = new ConsumoAPI();
    private final ConverteDados conversor = new ConverteDados();
    private static final String ENDERECO = "https://parallelum.com.br/fipe/api/v1/";
    private static final ObjectMapper mapper = new ObjectMapper();

    private final Map<Integer, Runnable> operacoes = Map.of(
            1, this::consultarCarros,
            2, this::consultarMotos,
            3, this::consultarCaminhoes
    );

    public void iniciarConsulta() {
        exibirMenu();
    }

    private void exibirMenu() {
        System.out.println("""
                ********** OPÇÕES **********
                1 - CARROS
                2 - MOTOS
                3 - CAMINHÕES
                ****************************
                """);
        int escolha = scanner.nextInt();
        operacoes.getOrDefault(escolha, () -> System.out.println("Opção inválida. Escolha uma opção de 1 a 3.")).run();
    }


    private void consultarCarros() {
        try {
            String jsonMarcas = consumo.obterDadosHttp(ENDERECO + "carros/marcas");
            List<DadosVeiculos> marcasCarros = conversor.obterDados(jsonMarcas, new TypeReference<>() {});

            marcasCarros.stream()
                    .sorted(Comparator.comparingInt(m -> Integer.parseInt(m.codigoCarro())))
                    .forEach(m -> System.out.println("Código: " + m.codigoCarro() + " Nome: " + m.nomeCarro()));

            System.out.println("Informe o código da marca para consulta:");
            int codigoMarca = scanner.nextInt();

            String jsonModelos = consumo.obterDadosHttp(ENDERECO + "carros/marcas/" + codigoMarca + "/modelos");
            ModelosResponse response = conversor.obterDados(jsonModelos, new TypeReference<>() {});

            List<ModeloVeiculos> modelosCarros = response.getModelos();

            modelosCarros.stream()
                    .sorted(Comparator.comparingInt(m -> Integer.parseInt(m.getCodigo())))
                    .forEach(m -> System.out.println("Cód = " + m.getCodigo() + " / " + "Modelo = " + m.getNome()));

            System.out.println("Digite por favor o código do modelo para buscar os valores");
            int codigoModelo = scanner.nextInt();

            String jsonAnos = consumo.obterDadosHttp(ENDERECO + "carros/marcas/" + codigoMarca + "/modelos/" + codigoModelo + "/anos");
            List<DadosVeiculos> anos = conversor.obterDados(jsonAnos, new TypeReference<List<DadosVeiculos>>() {});

            List<Veiculo> veiculos = new ArrayList<>();

            for (DadosVeiculos ano : anos) {
                String jsonVeiculo = consumo.obterDadosHttp(ENDERECO + "carros/marcas/" + codigoMarca + "/modelos/" + codigoModelo + "/anos/" + ano.codigoCarro());
                
                try {
                    Veiculo veiculo = mapper.readValue(jsonVeiculo, Veiculo.class);
                    veiculos.add(veiculo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Imprimir os veículos
            for (Veiculo veiculo : veiculos) {
                System.out.println("Nome: " + veiculo.getModelo() + "\nValor: " + veiculo.getValor() + "\nMarca: " + veiculo.getMarca() + "\nAno: " + veiculo.getAno() + "\nCombustivel: " + veiculo.getTipo() + "\n**********");
            }


        } catch (Exception e) {
            System.out.println("Erro ao consultar carros: " + e.getMessage());
        }
    }

    private void consultarMotos() {
        try {
            String jsonMarcas = consumo.obterDadosHttp(ENDERECO + "motos/marcas");
            List<DadosVeiculos> marcasMotos = conversor.obterDados(jsonMarcas, new TypeReference<>() {});

            marcasMotos.stream()
                    .sorted(Comparator.comparingInt(m -> Integer.parseInt(m.codigoCarro())))
                    .forEach(m -> System.out.println("Código: " + m.codigoCarro() + " Nome: " + m.nomeCarro()));

            System.out.println("Informe o código da marca para consulta:");
            int codigoMarca = scanner.nextInt();

            String jsonModelos = consumo.obterDadosHttp(ENDERECO + "motos/marcas/" + codigoMarca + "/modelos");
            ModelosResponse response = conversor.obterDados(jsonModelos, new TypeReference<>() {});

            List<ModeloVeiculos> modelosCarros = response.getModelos();

            modelosCarros.stream()
                    .sorted(Comparator.comparingInt(m -> Integer.parseInt(m.getCodigo())))
                    .forEach(m -> System.out.println("Cód = " + m.getCodigo() + " / " + "Modelo = " + m.getNome()));


            System.out.println("Digite por favor o código do modelo para buscar os valores");
            int codigoModelo = scanner.nextInt();

            String jsonAnos = consumo.obterDadosHttp(ENDERECO + "motos/marcas/" + codigoMarca + "/modelos/" + codigoModelo + "/anos");
            List<DadosVeiculos> anos = conversor.obterDados(jsonAnos, new TypeReference<List<DadosVeiculos>>() {});

            List<Veiculo> veiculos = new ArrayList<>();

            for (DadosVeiculos ano : anos) {
                String jsonVeiculo = consumo.obterDadosHttp(ENDERECO + "motos/marcas/" + codigoMarca + "/modelos/" + codigoModelo + "/anos/" + ano.codigoCarro());

                try {
                    Veiculo veiculo = mapper.readValue(jsonVeiculo, Veiculo.class);
                    veiculos.add(veiculo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (Veiculo veiculo : veiculos) {
                System.out.println("Nome: " + veiculo.getModelo() + "\nValor: " + veiculo.getValor() + "\nMarca: " + veiculo.getMarca() + "\nAno: " + veiculo.getAno() + "\nCombustivel: " + veiculo.getTipo() + "\n**********");
            }


        } catch (Exception e) {
            System.out.println("Erro ao consultar carros: " + e.getMessage());
        }
    }

    private void consultarCaminhoes() {
        try {
            String jsonMarcas = consumo.obterDadosHttp(ENDERECO + "caminhoes/marcas");
            List<DadosVeiculos> marcasCaminhoes = conversor.obterDados(jsonMarcas, new TypeReference<>() {});

            marcasCaminhoes.stream()
                    .sorted(Comparator.comparingInt(m -> Integer.parseInt(m.codigoCarro())))
                    .forEach(m -> System.out.println("Código: " + m.codigoCarro() + " Nome: " + m.nomeCarro()));

            System.out.println("Informe o código da marca para consulta:");
            int codigoMarca = scanner.nextInt();

            String jsonModelos = consumo.obterDadosHttp(ENDERECO + "caminhoes/marcas/" + codigoMarca + "/modelos");
            ModelosResponse response = conversor.obterDados(jsonModelos, new TypeReference<>() {});

            List<ModeloVeiculos> modelosCarros = response.getModelos();

            modelosCarros.stream()
                    .sorted(Comparator.comparingInt(m -> Integer.parseInt(m.getCodigo())))
                    .forEach(m -> System.out.println("Cód = " + m.getCodigo() + " / " + "Modelo = " + m.getNome()));

            System.out.println("Digite por favor o código do modelo para buscar os valores");
            int codigoModelo = scanner.nextInt();

            String jsonAnos = consumo.obterDadosHttp(ENDERECO + "caminhoes/marcas/" + codigoMarca + "/modelos/" + codigoModelo + "/anos");
            List<DadosVeiculos> anos = conversor.obterDados(jsonAnos, new TypeReference<List<DadosVeiculos>>() {});

            List<Veiculo> veiculos = new ArrayList<>();

            for (DadosVeiculos ano : anos) {
                String jsonVeiculo = consumo.obterDadosHttp(ENDERECO + "caminhoes/marcas/" + codigoMarca + "/modelos/" + codigoModelo + "/anos/" + ano.codigoCarro());

                try {
                    Veiculo veiculo = mapper.readValue(jsonVeiculo, Veiculo.class);
                    veiculos.add(veiculo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (Veiculo veiculo : veiculos) {
                System.out.println("Nome: " + veiculo.getModelo() + "\nValor: " + veiculo.getValor() + "\nMarca: " + veiculo.getMarca() + "\nAno: " + veiculo.getAno() + "\nCombustivel: " + veiculo.getTipo() + "\n**********");
            }


        } catch (Exception e) {
            System.out.println("Erro ao consultar caminhões: " + e.getMessage());
        }
    }
}
