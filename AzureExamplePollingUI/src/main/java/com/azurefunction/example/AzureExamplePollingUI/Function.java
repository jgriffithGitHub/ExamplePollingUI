package com.azurefunction.example.AzureExamplePollingUI;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.Map.Entry;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function
{
	/**
	 * This function listens at endpoint "/api/HttpExample". Two ways to invoke it
	 * using "curl" command in bash: 1. curl -d "HTTP Body" {your
	 * host}/api/HttpExample 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
	 */
	@FunctionName("GetVotingUI")
	public HttpResponseMessage run(@HttpTrigger(name = "req", methods =
	{ HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
			final ExecutionContext context)
	{
		Logger log = context.getLogger();
		
		log.info("Java HTTP trigger processed a request.");

		log = context.getLogger();
		log.info("Java HTTP trigger processed a request.");
		log.info("Request Method: " + request.getHttpMethod());

		String principalName = "None provided";
		String principalId = "None provided";
		
		String retText = " --- Headers: ";
		Map<String,String> headers = request.getHeaders();
		
		if(headers == null || headers.isEmpty())
		{
			retText = "No headers";			
		}
		else
		{
			Set<Entry<String, String>> keySet = headers.entrySet();
			if(keySet == null || keySet.isEmpty())
			{
				retText = "No headers";			
			}
			else
			{	
				principalName = headers.get("x-ms-client-principal-name");
				principalId = headers.get("x-ms-client-principal-id");
			}
		}
		
		log.info(retText);			
		log.info("principalName: " + principalName);
		log.info("principalId: " + principalId);

		String uiTemplate = "";
		try
		{
			PageBuilder pb = new PageBuilder();			
			pb.loadTemplate();
			pb.setElectionId(log);			
			pb.setTitle(log);
			pb.setVotes(log);
			uiTemplate = pb.getPage();
	 	} 
		catch (Exception e)
		{
			uiTemplate = "<html><head></head><body>Exception:<br>" + e.getMessage() + "</body></html>";
		}
	
       return request.createResponseBuilder(HttpStatus.OK).header("Content-Type", "text/html").body(uiTemplate).build();
	}
}
