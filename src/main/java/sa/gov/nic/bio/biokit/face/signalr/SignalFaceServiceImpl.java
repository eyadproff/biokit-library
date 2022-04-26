/*
package sa.gov.nic.bio.biokit.face.signalr;

import sa.gov.nic.bio.biokit.ResponseProcessor;
import sa.gov.nic.bio.biokit.beans.InitializeResponse;
import sa.gov.nic.bio.biokit.beans.LivePreviewingResponse;
import sa.gov.nic.bio.biokit.face.FaceService;
import sa.gov.nic.bio.biokit.face.FaceStopPreviewResponse;
import sa.gov.nic.bio.biokit.face.WebsocketFaceErrorCodes;
import sa.gov.nic.bio.biokit.face.beans.CaptureFaceResponse;
import sa.gov.nic.bio.biokit.face.beans.FaceStartPreviewResponse;
import sa.gov.nic.bio.biokit.signalr.SignalRClient;
import sa.gov.nic.bio.commons.TaskResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class SignalFaceServiceImpl implements FaceService {
    private ExecutorService executorService = Executors.newCachedThreadPool();
    public void getCapturedImage( CapturedImage capturedImage ) {

        BufferedImage image;
            try {

                    InputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(capturedImage.getBase64String()));
                     image = null;
                    try {
                        image = ImageIO.read(in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                 toByteArray(image, "PNG");

            } catch (Exception ex) {
                ex.printStackTrace();
            }


    }
    @Override
    public Future<TaskResponse<InitializeResponse>> initialize() {
        Callable<TaskResponse<InitializeResponse>> callable = new Callable<TaskResponse<InitializeResponse>>() {

            */
/**
             * Computes a result, or throws an exception if unable to do so.
             *
             * @return computed result
             *//*

            @Override
            public TaskResponse<InitializeResponse> call() {
                try {
                    SignalRClient.init();

                } catch (Exception e) {
                    String errorCode = WebsocketFaceErrorCodes.L0002_00002.getCode();
                    return TaskResponse.failure(errorCode, e);
                }
                InitializeResponse initializeResponse = new InitializeResponse();
                initializeResponse.setReturnCode(InitializeResponse.SuccessCodes.SUCCESS);
                initializeResponse.setCurrentDeviceName("Cannon");
                initializeResponse.setReturnMessage("SUCCESS");

                return new TaskResponse<InitializeResponse>(true, null, null, null, initializeResponse);

            }
        };

        FutureTask<TaskResponse<InitializeResponse>> futureTask = new FutureTask<TaskResponse<InitializeResponse>>(callable);
        executorService.submit(futureTask);
        return futureTask;
    }

    @Override
    public Future<TaskResponse<InitializeResponse>> deinitialize(String currentDeviceName) {
        return null;
    }

    @Override
    public Future<TaskResponse<FaceStartPreviewResponse>> startPreview(String currentDeviceName, ResponseProcessor<LivePreviewingResponse> responseProcessor) {
        Callable<TaskResponse<FaceStartPreviewResponse>> callable = new Callable<TaskResponse<FaceStartPreviewResponse>>() {
            */
/**
             * Computes a result, or throws an exception if unable to do so.
             *
             * @return computed result
             *//*

            @Override
            public TaskResponse<FaceStartPreviewResponse> call() {
                try
                {
                    SignalRClient.registerLiveViewEventListener(new Consumer<CapturedImage>() {
                        @Override
                        public void accept(CapturedImage capturedImage) {
                            LivePreviewingResponse livePreviewingResponse = new LivePreviewingResponse();
                            livePreviewingResponse.setPreviewImage(capturedImage.getBase64String());
                            responseProcessor.processResponse(livePreviewingResponse);
                        }
                    });
                } catch(Exception e)
                {
                    String errorCode = WebsocketFaceErrorCodes.L0002_00002.getCode();
                    return TaskResponse.failure(errorCode, e);
                }

                FaceStartPreviewResponse faceStartPreviewResponse = new FaceStartPreviewResponse();
                faceStartPreviewResponse.setReturnCode(FaceStartPreviewResponse.SuccessCodes.SUCCESS);

                return new TaskResponse<FaceStartPreviewResponse>(true, null, null, null, faceStartPreviewResponse);
            }
        };





        FutureTask<TaskResponse<FaceStartPreviewResponse>> futureTask = new FutureTask<FaceStartPreviewResponse>(callable);
        executorService.submit(futureTask);
        return futureTask;
    }

    @Override
    public Future<TaskResponse<FaceStopPreviewResponse>> stopPreview(String currentDeviceName) {
        return null;
    }

    @Override
    public Future<TaskResponse<CaptureFaceResponse>> captureFace(String currentDeviceName, boolean applyIcao) {
        return null;
    }

    public static byte[] toByteArray(BufferedImage bi, String format)
            throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;

    }

}
*/
