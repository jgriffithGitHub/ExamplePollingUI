package com.azurefunction.example.AzureExamplePollingUI;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
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
		Path uiFilePath = Path.of("src/main/resources/baseUi.html");
		log.info(uiFilePath.toAbsolutePath().toString());
		try
		{
			uiTemplate = Files.readString(uiFilePath);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		PageBuilder pb = new PageBuilder();
		pb.setElectionId(log);
		uiTemplate = pb.setTitle(uiTemplate, log);
		uiTemplate = pb.setVotes(uiTemplate, log);
		
		log.info(uiTemplate);
		
		return request.createResponseBuilder(HttpStatus.OK).header("Content-Type", "test/html").body(uiTemplate).build();
	}
}
