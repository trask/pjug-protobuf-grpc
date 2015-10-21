package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import com.example.Chat.ChatMessage;
import com.example.ChatServiceGrpc.ChatService;
import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;

public class ChatClient {

    public static void main(String[] args) throws Exception {
        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 8025)
                .negotiationType(NegotiationType.PLAINTEXT)
                .build();

        ChatService service = ChatServiceGrpc.newStub(channel);

        StreamObserver<ChatMessage> client = service.connect(
                new StreamObserver<ChatMessage>() {
                    @Override
                    public void onNext(ChatMessage value) {
                        System.out.println(value.getMessage());
                    }
                    @Override
                    public void onError(Throwable t) {}
                    @Override
                    public void onCompleted() {}
                });

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = in.readLine()) != null) {
            client.onNext(ChatMessage.newBuilder()
                    .setMessage(line)
                    .build());
        }

        channel.shutdown();
        channel.awaitTermination(1, TimeUnit.SECONDS);
    }
}
