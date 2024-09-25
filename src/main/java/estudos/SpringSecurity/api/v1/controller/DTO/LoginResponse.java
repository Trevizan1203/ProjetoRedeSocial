package estudos.SpringSecurity.api.v1.controller.DTO;

public record LoginResponse(String tokenAcesso, Long tempoUso) {
}
