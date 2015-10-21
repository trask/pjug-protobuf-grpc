package com.example;

import com.example.PetClinic.Address;
import com.example.PetClinic.PetClinicReply;
import com.example.PetClinic.PetClinicRequest;
import com.example.PetClinicServiceGrpc.PetClinicService;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;

public class PetClinicServer {

    public static void main(String[] args) throws Exception {
        Server server = NettyServerBuilder.forPort(8025)
                .addService(PetClinicServiceGrpc.bindService(
                        new PetClinicServiceImpl()))
                .build()
                .start();
        server.awaitTermination();
    }

    private static class PetClinicServiceImpl implements PetClinicService {
        @Override
        public void findPetClinic(PetClinicRequest request,
                StreamObserver<PetClinicReply> responseObserver) {

            responseObserver.onNext(PetClinicReply.newBuilder()
                    .setName("Fremont Veterinary Clinic")
                    .setAddress(Address.newBuilder()
                            .setStreet("5055 NE Fremont St")
                            .setCity("Portland")
                            .build())
                    .build());
            responseObserver.onCompleted();
        }
    }
}
