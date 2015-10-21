package com.example;

import java.util.HashSet;
import java.util.Set;

import com.example.Chat.ChatMessage;
import com.example.ChatServiceGrpc.ChatService;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;

public class ChatServer {

    public static void main(String[] args) throws Exception {
        Server server = NettyServerBuilder.forPort(8025)
                .addService(ChatServiceGrpc.bindService(new ChatServiceImpl()))
                .build()
                .start();
        server.awaitTermination();
    }

    static class ChatServiceImpl implements ChatService {

        private Set<StreamObserver<ChatMessage>> clients = new HashSet<>();

        @Override
        public StreamObserver<ChatMessage> connect(
                final StreamObserver<ChatMessage> client) {
            clients.add(client);
            return new StreamObserver<ChatMessage>() {
                @Override
                public void onNext(ChatMessage chatMessage) {
                    for (StreamObserver<ChatMessage> cl : clients) {
                        if (cl != client) {
                            cl.onNext(chatMessage);
                        }
                    }
                }

                @Override
                public void onError(Throwable t) {}

                @Override
                public void onCompleted() {}
            };
        }
    }
}
