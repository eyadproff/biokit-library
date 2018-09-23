package sa.gov.nic.bio.biokit.utils;

public interface TypeCaster<T1, T2>
{
	T1 cast(T2 t);
}