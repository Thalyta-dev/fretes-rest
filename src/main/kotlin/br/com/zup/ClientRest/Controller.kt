package br.com.zup.ClientRest

import br.com.zup.CalculaFreteRequest
import br.com.zup.ErroDetails
import br.com.zup.FretesGrpcServiceGrpc
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpResponse.unauthorized
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
    fun calculaFrete(@QueryValue cep: String): HttpResponse<Any> {

        val request = CalculaFreteRequest.newBuilder()
            .setCep(cep).build()


        try {

            var response = grpcFrete.calculaFrete(request)

            return HttpResponse.ok(

                FreteResponse(
                    cep = response.cep,
                    valor = response.valor
                )
            )


        } catch (e: StatusRuntimeException) {

            val statusCode = e.status.code
            val description = e.status.description

            if (statusCode.equals(Status.Code.PERMISSION_DENIED)) {

                val statusProto = io.grpc.protobuf.StatusProto.fromThrowable(e)
                    ?: return HttpResponse.unauthorized<Any?>().body(description)

                val anyDetails = statusProto.detailsList[0]
                val details = anyDetails.unpack(ErroDetails::class.java)

                return HttpResponse.unauthorized<Any?>().body("${details.code} : ${details.erro}")

            }

            if (statusCode.equals(Status.Code.INVALID_ARGUMENT)) {
                return HttpResponse.badRequest<Any>(description)
            }
        }

        return HttpResponse.ok<Any>()
    }

    data class FreteResponse(val cep: String, val valor: Double) {}

}

