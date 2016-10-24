package within30.com.webservices;


public class Response 
{
	public ServiceMethods method;
	public boolean isError;
	public Object data;
	public String jsonResponse;
	public int messageCode;
	
	public String  payload;
	public Response(ServiceMethods method, boolean isError, Object data,int messageCode)
	{
		this.method = method;
		this.isError = isError;
		this.data = data;
		this.messageCode = messageCode;
	}
}
