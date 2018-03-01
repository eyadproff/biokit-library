package sa.gov.nic.bio.biokit.utils;

import sa.gov.nic.bio.biokit.exceptions.JsonMappingException;

public interface JsonMapper<T>
{
	T fromJson(String json) throws JsonMappingException;
	String toJson(T object) throws JsonMappingException;
}