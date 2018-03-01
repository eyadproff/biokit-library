package sa.gov.nic.bio.biokit;

import sa.gov.nic.bio.biokit.beans.ServiceResponse;
import sa.gov.nic.bio.biokit.exceptions.TimeoutException;
import sa.gov.nic.bio.biokit.websocket.beans.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class AsyncConsumer
{
    private static final Logger LOGGER = Logger.getLogger(AsyncConsumer.class.getName());
    
    public static class Response
    {
        private boolean success;
        private Message message;
        private String errorCode;
        private Exception exception;

        boolean isSuccess(){return success;}
        void setSuccess(boolean success){this.success = success;}

        public Message getMessage(){return message;}
        public void setMessage(Message message){this.message = message;}

        public String getErrorCode(){return errorCode;}
        void setErrorCode(String errorCode){this.errorCode = errorCode;}

        public Exception getException(){return exception;}
        public void setException(Exception exception){this.exception = exception;}
    
        @Override
        public boolean equals(Object o)
        {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
    
            Response response = (Response) o;
    
            return success == response.success && (message != null ? message.equals(response.message) :
                    response.message == null) && (errorCode != null ? errorCode.equals(response.errorCode) :
                    response.errorCode == null) && (exception != null ? exception.equals(response.exception) :
                    response.exception == null);
        }
    
        @Override
        public int hashCode()
        {
            int result = (success ? 1 : 0);
            result = 31 * result + (message != null ? message.hashCode() : 0);
            result = 31 * result + (errorCode != null ? errorCode.hashCode() : 0);
            result = 31 * result + (exception != null ? exception.hashCode() : 0);
            return result;
        }
    
        @Override
        public String toString() {
            return "Response{" +
                    "success=" + success +
                    ", message=" + message +
                    ", errorCode='" + errorCode + '\'' +
                    ", exception=" + exception +
                    '}';
        }
    }

    private String transactionId;
    private BlockingQueue<Response> queue = new LinkedBlockingQueue<Response>();

    public ServiceResponse<Message> processResponses(ResponseProcessor<Message> responseProcessor, long timeout, TimeUnit unit) throws InterruptedException, TimeoutException
    {
        while(true)
        {
            Response response = queue.poll(timeout, unit);

            if(response == null) throw new TimeoutException();

            if(response.isSuccess())
            {
                Message message = response.getMessage();

                if(message.isEnd()) return ServiceResponse.successfulResponse(message);
                else if(responseProcessor != null) responseProcessor.processResponse(message);
                else LOGGER.warning("It is not a final response and the response processor is null!");
            }
            else return ServiceResponse.failureResponse(response.errorCode, response.exception);
        }
    }

    public void consume(Message message)
    {
        Response response = new Response();
        response.setSuccess(true);
        response.setMessage(message);

        queue.add(response);
    }

    public void reportError(String errorCode, Exception exception)
    {
        Response response = new Response();
        response.setSuccess(false);
        response.setErrorCode(errorCode);
        response.setException(exception);

        queue.add(response);
    }

    public void setTransactionId(String transactionId){this.transactionId = transactionId;}
    public String getTransactionId(){return transactionId;}
}