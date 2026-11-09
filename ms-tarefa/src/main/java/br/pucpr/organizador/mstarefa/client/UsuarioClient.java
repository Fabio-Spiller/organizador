package br.pucpr.organizador.mstarefa.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class UsuarioClient {

    private final RestClient restClient;

    public UsuarioClient(RestClient.Builder builder,
                         @Value("${usuario.service.url}") String usuarioServiceUrl) {
        this.restClient = builder.baseUrl(usuarioServiceUrl).build();
    }

    public boolean existe(Long usuarioId) {
        try {
            restClient.get()
                    .uri("/usuarios/{id}", usuarioId)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }
}