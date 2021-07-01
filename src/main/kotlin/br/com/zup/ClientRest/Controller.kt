package br.com.zup.ClientRest

import br.com.zup.CalculaFreteRequest
import br.com.zup.FretesGrpcServiceGrpc
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import javax.inject.Inject

@Controller
class Controller(
    @Inject
    val grpcFrete: FretesGrpcServiceGrpc.FretesGrpcServiceBlockingStub
) {

    @Get("/api/fretes")
    fun calculaFrete(@QueryValue cep: String): FreteResponse {

        val  request = CalculaFreteRequest.newBuilder()
            .setCep(cep).build()

        var response = grpcFrete.calculaFrete(request)

        return FreteResponse(
            cep = response.cep,
            valor = response.valor
        )

    }

    data class FreteResponse(val cep: String, val valor: Double) {}

}

