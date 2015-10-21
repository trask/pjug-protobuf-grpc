package com.example;

import java.util.concurrent.TimeUnit;

import com.example.PetClinic.Address;
import com.example.PetClinic.Animal;
import com.example.PetClinic.Pet;
import com.example.PetClinic.PetClinicReply;
import com.example.PetClinic.PetClinicRequest;
import com.example.PetClinicServiceGrpc.PetClinicServiceBlockingClient;
import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;

public class PetClinicClient {

    public static void main(String[] args) throws Exception {
        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 8025)
                .negotiationType(NegotiationType.PLAINTEXT)
                .build();

        PetClinicServiceBlockingClient client =
                PetClinicServiceGrpc.newBlockingStub(channel);

        PetClinicReply reply = client.findPetClinic(PetClinicRequest.newBuilder()
                .setOwnerName("Trask Stalnaker")
                .setAddress(Address.newBuilder()
                        .setStreet("3456 NE Fremont St")
                        .setCity("Portland")
                        .build())
                .addPet(Pet.newBuilder()
                        .setAnimal(Animal.CAT)
                        .setName("Fiver")
                        .build())
                .build());

        System.out.println(reply.getName());
        System.out.println(reply.getAddress().getStreet());
        System.out.println(reply.getAddress().getCity());

        channel.shutdown();
        channel.awaitTermination(1, TimeUnit.SECONDS);
    }
}
