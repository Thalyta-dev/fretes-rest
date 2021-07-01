package br.com.zup.ClientRest
import br.com.zup.FretesGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton


@Factory
class GrpcClientFactory {

    @Singleton
    fun fretesClientsStub(@GrpcChannel("fretes") channel: ManagedChannel): FretesGrpcServiceGrpc.FretesGrpcServiceBlockingStub {
        return FretesGrpcServiceGrpc.newBlockingStub(channel)
    }
}