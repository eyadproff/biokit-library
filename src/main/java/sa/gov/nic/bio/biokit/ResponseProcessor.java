package sa.gov.nic.bio.biokit;

public interface ResponseProcessor<T>
{
    void processResponse(T response);
}