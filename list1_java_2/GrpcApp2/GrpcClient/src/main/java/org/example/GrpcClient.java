package org.example;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.grpc.*;

import java.io.*;
import java.util.Iterator;
import java.util.Scanner;

public class GrpcClient {
    public static void main(String[] args) throws FileNotFoundException {
        String address = "localhost";
        int port = 50001;

        System.out.println("Running gRPC client...");
        ManagedChannel channel = ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
//        TheRequest request = TheRequest.newBuilder().setName("Maciej").setAge(22).build();
//
//        TheResponse response = syncCalls(channel, request);
//        System.out.println("--> Response: " + response);
//
//
//        Iterator<TheResponse> respIterator;
////        System.out.println("...calling streamProcedure");
////        ServiceNameGrpc.ServiceNameBlockingStub bStub2;
////        bStub2 = ServiceNameGrpc.newBlockingStub(channel);
//        respIterator = bStub2.streamProcedure(request);
////        System.out.println("...after calling streamProcedure");
//        TheResponse strResponse;
//        while(respIterator.hasNext()) {
//            strResponse = respIterator.next();
//            System.out.println("-->" + strResponse.getMessage());
//        }
//        System.out.println("...async calling streamProcedure");
//        ServiceNameGrpc.ServiceNameStub nonbStub;
//        nonbStub = ServiceNameGrpc.newStub(channel);
//        nonbStub.streamProcedure(request, new UnaryObs());
//        System.out.println("...after async calling streamProcedure");


//        asyncCalls(channel, request);


//        ServiceNameGrpc.ServiceNameBlockingStub bStub;
//        bStub = ServiceNameGrpc.newBlockingStub(channel);
//        TheRequestFib requestFib = TheRequestFib.newBuilder().setIle(10).setCo(2).build();
//        System.out.println("...calling unaryProcedure");
//        TheResponseFib responseFib = bStub.unaryProcedureFib(requestFib).next();
//        System.out.println("...after calling unaryProcedure");
//        System.out.println("--> Response: " + responseFib.getWynik() +"  -> "+ responseFib.getWynik23());

//        ServiceNameGrpc.ServiceNameStub nonbStub2;
//        nonbStub2 = ServiceNameGrpc.newStub(channel);
//        TheRequestFib requestFib2 = TheRequestFib.newBuilder().setIle(10).setCo(3).build();
//        System.out.println("...calling streamProcedure");
//        nonbStub2.streamProcedureFib(requestFib2, new FibObs());
//        System.out.println("...after calling streamProcedure");

        ServiceNameGrpc.ServiceNameStub nonbStub2;
        nonbStub2 = ServiceNameGrpc.newStub(channel);
        ByteRequest byteRequest = ByteRequest.newBuilder()
                .setPath("C:\\git_projects\\RSI\\list1_java_2\\GrpcApp2\\serverFiles\\server.png").build();
        nonbStub2.streamToClient(byteRequest, new StrObs2());
        //StreamObserver<ByteRequest> strReqObserver = nonbStub2.streamToSrv( new StrObs2());
        //MyRecord myRecord = MyRecord.newBuilder().setName("Maciej").setAge(18).setAddress("Wroclaw").build();
        //System.out.println(bStub.addRecord(myRecord));
        //bStub.getRecord(RecordName.newBuilder().setName("Maciej").build());
        //ByteRequest rrr = ByteRequest.newBuilder().set... .build();
        //strReqObserver.onNext(rrr);
        //strReqObserver.onCompleted();

        StreamObserver<ByteRequest2> strReqObserver = nonbStub2.streamToSrv(new StrObs3());
        int BUFF_SIZE = 1000;
        byte[] buffer = new byte[BUFF_SIZE];
        String path = "C:\\git_projects\\RSI\\list1_java_2\\GrpcApp2\\clientFiles\\client.png";
        System.out.println(path);
        try (FileInputStream fileInputStream = new FileInputStream(new File(path))) {
            int buf;
            while ((buf = fileInputStream.read(buffer)) > 0) {
                ByteRequest2 byteRequest2 = ByteRequest2
                        .newBuilder()
                        .setChunk(ByteString.copyFrom(buffer))
                        .setNumOfBytes(buf)
                        .build();
                strReqObserver.onNext(byteRequest2);
                Thread.sleep(500);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        strReqObserver.onCompleted();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String x = scanner.nextLine();
            if (x.equals("x")) {
                channel.shutdown();
                break;
            }
        }
    }

    private static class StrObs2 implements StreamObserver<ByteResponse> {
        FileOutputStream fileOutputStream;

        public StrObs2() throws FileNotFoundException {
            fileOutputStream =
                    new FileOutputStream(
                            new File("C:\\git_projects\\RSI\\list1_java_2\\GrpcApp2\\clientFiles\\fromServer.png"));
        }

        @Override
        public void onNext(ByteResponse value) {
            try {
                System.out.println(value.getChunk() + " " + value.getNumOfBytes());
                fileOutputStream.write(value.getChunk().toByteArray());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void onError(Throwable throwable) {
            System.out.println("-->byte onError");
        }

        public void onCompleted() {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private static class StrObs3 implements StreamObserver<ByteResponse2> {

        @Override
        public void onNext(ByteResponse2 value) {
            System.out.println(value.getStatus());
        }

        public void onError(Throwable throwable) {
            System.out.println("-->byte onError");
        }

        public void onCompleted() {
        }
    }

    private static class RecordObs implements StreamObserver<MyRecord> {

        @Override
        public void onNext(MyRecord value) {
            System.out.println("Record: " + value.getName() + " " + value.getAge() + " " + value.getAddress());
        }

        public void onError(Throwable throwable) {
            System.out.println("-->byte onError");
        }

        public void onCompleted() {

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

    private static class FibObs implements StreamObserver<TheResponseFib> {
        public void onNext(TheResponseFib theResponse) {
            System.out.println("-->async Fib onNext: " +
                    theResponse.getWynik() + " ::: " + theResponse.getWynik23());
        }

        public void onError(Throwable throwable) {
            System.out.println("-->async Fib onError");
        }

        public void onCompleted() {
            System.out.println("-->async Fib onCompleted");
        }
    }


    static TheResponse syncCalls(ManagedChannel channel, TheRequest request) {
        ServiceNameGrpc.ServiceNameBlockingStub bStub;
        bStub = ServiceNameGrpc.newBlockingStub(channel);
        System.out.println("...calling unaryProcedure");
        TheResponse response = bStub.unaryProcedure(request);
        System.out.println("...after calling unaryProcedure");
        return response;

    }

    static void asyncCalls(ManagedChannel channel, TheRequest request) {
        ServiceNameGrpc.ServiceNameStub nonbStub;
        nonbStub = ServiceNameGrpc.newStub(channel);
        System.out.println("...async calling unaryProcedure");
        nonbStub.unaryProcedure(request, new UnaryObs());
        System.out.println("...after async calling unaryProcedure");
    }
}