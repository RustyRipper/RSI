package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import io.grpc.stub.StreamObserver;
import org.example.grpc.ServiceNameGrpc;
import org.example.grpc.TheRequest;
import org.example.grpc.TheResponse;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) {
        int port = 50001;
        System.out.println("Starting gRPC server...");
        Server server = ServerBuilder.forPort(port).addService(new MyServiceImpl()).build();
        try {
            server.start();
            System.out.println("...Server started");
            server.awaitTermination();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class MyServiceImpl extends ServiceNameGrpc.ServiceNameImplBase {

        @Override
        public void unaryProcedure(TheRequest req, StreamObserver<TheResponse> responseObserver) {
            String msg;
            System.out.println("...called UnaryProcedure - start");
            if (req.getAge() > 18) msg = "Mr/Ms " + req.getName();
            else msg = "Boy/Girl";
            TheResponse response = TheResponse.newBuilder().setMessage("Hello " + msg).build();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            System.out.println("...called UnaryProcedure - end");
        }
        public void streamProcedure(TheRequest req,
                                    StreamObserver<TheResponse> responseObserver) {
            for (int i = 0; i < 9; i++) {
                TheResponse response = TheResponse.newBuilder()
                        .setMessage("Stream chunk " + (i + 1)).build();
                // [enter here Thread.sleep to easier trace the operation]
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                responseObserver.onNext(response);
            }
            responseObserver.onCompleted();
        }
    }
}