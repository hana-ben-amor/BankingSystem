package com.hana.notification_service.grpc;

import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class NotificationGrpcService extends NotificationServiceGrpc.NotificationServiceImplBase{
    @Override
    public void sendNotification(NotificationRequest request, StreamObserver<NotificationResponse> responseObserver) {
        String message=request.getMessage();
        System.out.println("RECEIVED gRPC notification: "+message);

        //construire la réponse
        NotificationResponse response=NotificationResponse
                .newBuilder()
                .setStatus("RECEIVED")
                .build();


        //envoyer la réponse
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
