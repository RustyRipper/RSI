package org.example;

import com.google.protobuf.ByteString;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import io.grpc.stub.StreamObserver;
import org.example.grpc.*;

import java.io.*;
import java.util.ArrayList;

public class GrpcServer {
    public static void main(String[] args) {
        int port = 50001;
        System.out.println("Starting gRPC server...");
        Server server = ServerBuilder.forPort(port).addService(new MyServiceImpl()).build();
        try {
            server.start();
            System.out.println("...Server started");
            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
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
                Thread.sleep(5000);
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

        public void streamProcedureFib(TheRequestFib req,
                                       StreamObserver<TheResponseFib> responseObserver) {
            int ile = req.getIle();
            int co = req.getCo();
            int temp = 1;
            int x1 = 0;
            int x2 = 1;
            int result = 0;
            for (int i = 0; i < ile; i++) {

                if (co == 2) {
                    result = x2 * x2;
                } else if (co == 3) {
                    result = x2 * x2 * x2;
                }
                // [enter here Thread.sleep to easier trace the operation]
                temp = x1 + x2;

                TheResponseFib response = TheResponseFib.newBuilder()
                        .setWynik(x2).setWynik23(result).build();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                x1 = x2;
                x2 = temp;
                responseObserver.onNext(response);

            }

            responseObserver.onCompleted();
        }


        public void streamToClient(ByteRequest req,
                                   StreamObserver<ByteResponse> responseObserver) {
            int BUFF_SIZE = 1000;
            byte[] buffer = new byte[BUFF_SIZE];
            String path = req.getPath();
            System.out.println(path);
            try (FileInputStream fileInputStream = new FileInputStream(new File(path))) {
                int buf;
                while ((buf = fileInputStream.read(buffer)) > 0) {
                    ByteResponse byteResponse = ByteResponse
                            .newBuilder()
                            .setChunk(ByteString.copyFrom(buffer))
                            .setNumOfBytes(buf)
                            .build();

                    responseObserver.onNext(byteResponse);
                    Thread.sleep(500);
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            responseObserver.onCompleted();
        }

        public StreamObserver<ByteRequest2> streamToSrv(StreamObserver<ByteResponse2> responseObserver) {

            FileOutputStream fileOutputStream =
                    null;
            try {
                fileOutputStream = new FileOutputStream(
                        new File("C:\\git_projects\\RSI\\list1_java_2\\GrpcApp2\\serverFiles\\fromClient.png"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            FileOutputStream finalFileOutputStream = fileOutputStream;
            StreamObserver<ByteRequest2> srvObserver = new StreamObserver<ByteRequest2>() {
                @Override
                public void onNext(ByteRequest2 value) {
                    try {
                        System.out.println(value.getChunk() + " " + value.getNumOfBytes());
                        finalFileOutputStream.write(value.getChunk().toByteArray());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    System.out.println("-->byte onError");
                }

                @Override
                public void onCompleted() {
                    try {
                        finalFileOutputStream.flush();
                        finalFileOutputStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    ByteResponse2 response = ByteResponse2.newBuilder().setStatus("OK").build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
            };
            return srvObserver;
        }

    }
}