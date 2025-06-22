package com.hana.account_service.grpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

@Service
public class GrpcNotificationClient {

    private final NotificationServiceGrpc.NotificationServiceBlockingStub stub;

    public GrpcNotificationClient() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9090) // Port du serveur gRPC du microservice Notification
                .usePlaintext()
                .build();
        stub = com.hana.account_service.grpc.NotificationServiceGrpc.newBlockingStub(channel);
    }

    public void sendNotification(String message) {
        NotificationRequest request = NotificationRequest.newBuilder()
                .setMessage(message)
                .build();
      NotificationResponse response = stub.sendNotification(request);
        System.out.println("Notification response: " + response.getStatus());
    }
}
