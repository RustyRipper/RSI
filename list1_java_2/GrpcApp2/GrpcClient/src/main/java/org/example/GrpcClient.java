package org.example;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.grpc.*;

import java.io.*;
import java.util.Scanner;

public class GrpcClient {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        String address = "localhost";
        int port = 50001;

        System.out.println("Running gRPC client...");
        ManagedChannel channel = ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();

        ServiceNameGrpc.ServiceNameStub nonbStub2;
        nonbStub2 = ServiceNameGrpc.newStub(channel);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Type command:");
            String line = scanner.nextLine();
            if (line.equals("x")) {
                channel.shutdown();
                break;
            }
            String[] array = line.split(" ");
            switch (array[0]) {
                case ("help") -> {
                    if (array.length == 1) {
                        System.out.println("add [name] [year] [color] [path]");
                        System.out.println("getL");
                        System.out.println("get [name]");
                        System.out.println("rm [name]");
                        System.out.println("down [name] [filename_client]");
                        System.out.println("up [filename_client] [filename_server]");
                    }
                    else {
                        System.out.println("ERROR COMMAND");
                    }
                }
                case ("add") -> {
                    if (array.length == 5) {
                        nonbStub2.addCarRecord(CarRecord.newBuilder().setName(array[1]).setYear(Integer.parseInt(array[2]))
                                .setColor(array[3]).setPath(array[4]).build(), new CarStrObs());
                    }
                    else {
                        System.out.println("ERROR COMMAND");
                    }
                }
                case ("rm") -> {
                    if (array.length == 2) {
                        nonbStub2.deleteCarRecord(CarName.newBuilder().setCarName(array[1]).build(), new CarStrObs());
                    }
                    else {
                        System.out.println("ERROR COMMAND");
                    }
                }
                case ("get") -> {
                    if (array.length == 2) {
                        nonbStub2.getCarRecord(CarName.newBuilder().setCarName(array[1]).build(), new CarStrObs());
                    }
                    else {
                        System.out.println("ERROR COMMAND");
                    }
                }
                case ("getL") -> {
                    if (array.length == 1) {
                        nonbStub2.getCarRecordsList(RecordEmpty.newBuilder().build(), new CarNameStrObs());
                    }
                    else {
                        System.out.println("ERROR COMMAND");
                    }
                }
                case ("down") -> {
                    if (array.length == 3) {
                        nonbStub2.streamToClientCar(CarName.newBuilder().setCarName(array[1]).build(), new StrObsServerToClient(array[2]));
                    }
                    else {
                        System.out.println("ERROR COMMAND");
                    }
                }
                case ("up") -> {
                    if (array.length == 3) {
                        String from = array[1];
                        String to = array[2];
                        sendToServer(nonbStub2, from, to);
                    }
                    else {
                        System.out.println("ERROR COMMAND");
                    }
                }
            }
            Thread.sleep(1000);
        }

    }

    public static void sendToServer(ServiceNameGrpc.ServiceNameStub nonbStub2, String from, String to) {
        StreamObserver<ByteRequest2> strReqObserver = nonbStub2.streamToSrv(new StrObsClientToServer());
        int BUFF_SIZE = 20000;
        byte[] buffer = new byte[BUFF_SIZE];
        String path = "C:\\git_projects\\RSI\\list1_java_2\\GrpcApp2\\clientFiles\\" + from;
        System.out.println(path);
        try (FileInputStream fileInputStream = new FileInputStream(new File(path))) {
            int buf;
            while ((buf = fileInputStream.read(buffer)) > 0) {
                ByteRequest2 byteRequest2 = ByteRequest2
                        .newBuilder()
                        .setChunk(ByteString.copyFrom(buffer))
                        .setNumOfBytes(buf)
                        .setFileName(to)
                        .build();
                strReqObserver.onNext(byteRequest2);
                Thread.sleep(500);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        strReqObserver.onCompleted();
    }


    private static class CarStrObs implements StreamObserver<CarRecord> {

        @Override
        public void onNext(CarRecord value) {
            System.out.println(value.getName() + " " + value.getYear() + " " + value.getColor() + " " + value.getPath());
        }

        @Override
        public void onError(Throwable t) {
            System.out.println(t.getMessage());
        }

        @Override
        public void onCompleted() {
            System.out.println("end CarRecord");
        }
    }

    private static class CarNameStrObs implements StreamObserver<CarName> {

        @Override
        public void onNext(CarName value) {
            System.out.println(value.getCarName());
        }

        @Override
        public void onError(Throwable t) {
            System.out.println(t.getMessage());
        }

        @Override
        public void onCompleted() {
            System.out.println("end CarName");
        }
    }

    private static class StrObsServerToClient implements StreamObserver<ByteResponse> {
        FileOutputStream fileOutputStream;

        public StrObsServerToClient(String name) throws FileNotFoundException {
            fileOutputStream =
                    new FileOutputStream(
                            new File("C:\\git_projects\\RSI\\list1_java_2\\GrpcApp2\\clientFiles\\" + name));
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

    private static class StrObsClientToServer implements StreamObserver<ByteResponse2> {

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
}