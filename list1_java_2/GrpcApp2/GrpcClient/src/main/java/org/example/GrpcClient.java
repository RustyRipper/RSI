package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.grpc.ServiceNameGrpc;
import org.example.grpc.TheRequest;
import org.example.grpc.TheResponse;

import java.util.Iterator;
import java.util.Scanner;

public class GrpcClient {
    public static void main(String[] args) {
        String address = "localhost";
        int port = 50001;

        System.out.println("Running gRPC client...");
        ManagedChannel channel = ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
        TheRequest request = TheRequest.newBuilder().setName("Mariusz").setAge(33).build();

        TheResponse response = syncCalls(channel, request);
        System.out.println("--> Response: " + response);


        Iterator<TheResponse> respIterator;
        System.out.println("...calling streamProcedure");
        ServiceNameGrpc.ServiceNameBlockingStub bStub2;
        bStub2 = ServiceNameGrpc.newBlockingStub(channel);
        respIterator = bStub2.streamProcedure(request);
        System.out.println("...after calling streamProcedure");
        TheResponse strResponse;
        while(respIterator.hasNext()) {
            strResponse = respIterator.next();
            System.out.println("-->" + strResponse.getMessage());
        }
        System.out.println("...async calling streamProcedure");
        ServiceNameGrpc.ServiceNameStub nonbStub;
        nonbStub = ServiceNameGrpc.newStub(channel);
        nonbStub.streamProcedure(request, new UnaryObs());
        System.out.println("...after async calling streamProcedure");



        asyncCalls(channel, request);
        Scanner scanner = new Scanner(System.in);
        while(true){
            String x = scanner.nextLine();
            if(x.equals("x")){
                channel.shutdown();
                break;
            }
        }


    }
    private static class UnaryObs implements StreamObserver<TheResponse> {
        public void onNext(TheResponse theResponse) {
            System.out.println("-->async unary onNext: " +
                    theResponse.getMessage());
        }
        public void onError(Throwable throwable) {
            System.out.println("-->async unary onError");
        }
        public void onCompleted() {
            System.out.println("-->async unary onCompleted");
        }
    }
    static TheResponse syncCalls(ManagedChannel channel, TheRequest request){
        ServiceNameGrpc.ServiceNameBlockingStub bStub;
        bStub = ServiceNameGrpc.newBlockingStub(channel);
        System.out.println("...calling unaryProcedure");
        TheResponse response = bStub.unaryProcedure(request);
        System.out.println("...after calling unaryProcedure");
        return response;

    }
    static void asyncCalls(ManagedChannel channel, TheRequest request){
        ServiceNameGrpc.ServiceNameStub nonbStub;
        nonbStub = ServiceNameGrpc.newStub(channel);
        System.out.println("...async calling unaryProcedure");
        nonbStub.unaryProcedure(request, new UnaryObs());
        System.out.println("...after async calling unaryProcedure");
    }
}