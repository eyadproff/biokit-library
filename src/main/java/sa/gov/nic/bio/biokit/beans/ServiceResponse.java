package sa.gov.nic.bio.biokit.beans;

public class ServiceResponse<T>
{
    public interface TypeCaster<T1, T2>
    {
        T1 cast(T2 t);
    }
    
    private final boolean success;
    private final T result;
    private final String errorCode;
    private final Exception exception;

    private ServiceResponse(boolean success, T result, String errorCode, Exception exception)
    {
        this.success = success;
        this.result = result;
        this.errorCode = errorCode;
        this.exception = exception;
    }

    public final boolean isSuccess(){return success;}
    public final T getResult(){return result;}
    public final String getErrorCode(){return errorCode;}
    public final Exception getException(){return exception;}

    public static <T> ServiceResponse<T> successfulResponse(T result)
    {
        return new ServiceResponse<T>(true, result, null, null);
    }

    public static <T> ServiceResponse<T> failureResponse(String errorCode, Exception exception)
    {
        return new ServiceResponse<T>(false, null, errorCode, exception);
    }
    
    public static <T1, T2> ServiceResponse<T1> cast(ServiceResponse<T2> input, TypeCaster<T1, T2> typeCaster)
    {
        return new ServiceResponse<T1>(input.success, typeCaster != null ? typeCaster.cast(input.result) : null,
                                       input.errorCode, input.exception);
    }

    @Override
    public String toString()
    {
        return "ServiceResponse{" +
                "success=" + success +
                ", result=" + result +
                ", errorCode='" + errorCode + '\'' +
                ", exception=" + exception +
                '}';
    }
}