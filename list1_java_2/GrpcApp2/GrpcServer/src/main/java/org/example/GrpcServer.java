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
        ArrayList<CarRecord> recordArrayList;

        public MyServiceImpl() {
            recordArrayList = new ArrayList<>();
            recordArrayList.add(CarRecord.newBuilder().setName("fiat").setColor("black").setYear(1999)
                    .setPath("C:\\git_projects\\RSI\\list1_java_2\\GrpcApp2\\serverFiles\\fiat.png").build());
            recordArrayList.add(CarRecord.newBuilder().setName("Audi").setYear(2001).setColor("White")
                    .setPath("C:\\git_projects\\RSI\\list1_java_2\\GrpcApp2\\serverFiles\\audi.png").build());
            recordArrayList.add(CarRecord.newBuilder().setName("BMW").setYear(2002).setColor("Red")
                    .setPath("C:\\git_projects\\RSI\\list1_java_2\\GrpcApp2\\serverFiles\\bmw.png").build());
        }

        public CarRecord getCarByName(String name) {
            if (recordArrayList.isEmpty()) {
                return null;
            }
            for (CarRecord carRecord : recordArrayList) {
                if (carRecord.getName().equals(name)) {
                    return carRecord;
                }
            }
            return null;
        }

        public void streamToClient(ByteRequest req,
                                   StreamObserver<ByteResponse> responseObserver) {
            int BUFF_SIZE = 20000;
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
            final boolean[] isRun = {false};

            final FileOutputStream[] fileOutputStream = new FileOutputStream[1];
            StreamObserver<ByteRequest2> srvObserver = new StreamObserver<ByteRequest2>() {
                @Override
                public void onNext(ByteRequest2 value) {

                    if (!isRun[0]) {
                        try {
                            fileOutputStream[0] = new FileOutputStream
                                    (new File("C:\\git_projects\\RSI\\list1_java_2\\GrpcApp2\\serverFiles\\" + value.getFileName()));
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        isRun[0] = true;
                    }

                    try {
                        System.out.println(value.getChunk() + " " + value.getNumOfBytes());
                        fileOutputStream[0].write(value.getChunk().toByteArray());
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
                        fileOutputStream[0].flush();
                        fileOutputStream[0].close();
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


        public void addCarRecord(CarRecord record,
                                 StreamObserver<CarRecord> responseObserver) {
            if(getCarByName(record.getName()) != null){
                responseObserver.onError(new Exception("Car exist"));
                return;
            }
            recordArrayList.add(record);
            responseObserver.onNext(record);
            responseObserver.onCompleted();

        }

        public void deleteCarRecord(CarName record,
                                    StreamObserver<CarRecord> responseObserver) {

            if (recordArrayList.isEmpty()) {
                return;
            }
            for (CarRecord myRecord : recordArrayList) {
                if (myRecord.getName().equals(record.getCarName())) {
                    System.out.println("DELETE " + record.getCarName());
                    responseObserver.onNext(myRecord);
                    recordArrayList.remove(myRecord);
                    responseObserver.onCompleted();
                    return;
                }
            }

        }

        public void getCarRecord(CarName record,
                                 StreamObserver<CarRecord> responseObserver) {
            if (recordArrayList.isEmpty()) {
                return;
            }
            for (CarRecord myRecord : recordArrayList) {
                if (myRecord.getName().equals(record.getCarName())) {
                    responseObserver.onNext(myRecord);
                    responseObserver.onCompleted();
                }
            }
        }

        public void getCarRecordsList(RecordEmpty recordEmpty, StreamObserver<CarName> responseObserver) {
            if (recordArrayList.isEmpty()) {
                return;
            }
            for (CarRecord myRecord : recordArrayList) {
                System.out.println(myRecord);
                responseObserver.onNext(CarName.newBuilder().setCarName(myRecord.getName()).build());
            }
            responseObserver.onCompleted();
        }

        public void streamToClientCar(CarName carName, StreamObserver<ByteResponse> responseObserver) {
            CarRecord carRecord = getCarByName(carName.getCarName());

            int BUFF_SIZE = 20000;
            byte[] buffer = new byte[BUFF_SIZE];
            String path = carRecord.getPath();
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
    }
}