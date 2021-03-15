package com.azurefunction.example.AzureExamplePollingUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

public class PageBuilder
{
	private final String TITLE_MARKER = "\\{Title\\}";
	DatabaseConnection dbConn = null;
	private Connection connection = null;
	private int electionId = 0;
	
	public PageBuilder()
	{
		dbConn = new DatabaseConnection();
		connection = dbConn.getConnection();
	}

	public int setElectionId(Logger log)
	{
		int retVal = 0;
		try
		{
			PreparedStatement selectStatement = connection
					.prepareStatement("select * from electionDetails where startDate <= current_date() and endDate > current_date();");
	
			ResultSet rs = selectStatement.executeQuery();
			if(rs.next())
			{
				log.info("Found a row");
				electionId = rs.getInt("idElection");
				retVal = electionId;
			}
			else
			{
				log.info("No rows returned");
			}
		}
		catch(Exception e)
		{
			log.info("Exception: ");
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	public String setTitle(String page, Logger log)
	{
		String retVal = page;
		try
		{
			log.info("Setting title");
			
			PreparedStatement selectStatement = connection
					.prepareStatement("select * from electionDetails where idElection = " + electionId + ";");
	
			ResultSet rs = selectStatement.executeQuery();
			if(rs.next())
			{
				log.info("Found a row");
				String title = rs.getString("electionTitle");
				log.info("Title = " + title);
				retVal = retVal.replaceAll(TITLE_MARKER, title);
			}
			else
			{
				log.info("No rows returned");
			}
		}
		catch(Exception e)
		{
			log.info("Exception: ");
			e.printStackTrace();
		}
		
		return retVal;
	}

	public String setVotes(String page, Logger log)
	{
		String retVal = page;
		try
		{
			log.info("Setting votes");
			DatabaseConnection dbConn = new DatabaseConnection();
			Connection connection = dbConn.getConnection();
			
			PreparedStatement selectStatement = connection
					.prepareStatement("select * from voteTypes where electionId = " + electionId + ";");
	
			ResultSet rs = selectStatement.executeQuery();
			if(rs.next())
			{
				log.info("Found a row");
			}
			else
			{
				log.info("No rows returned");
			}
		}
		catch(Exception e)
		{
			log.info("Exception: ");
			e.printStackTrace();
		}
		
		return retVal;
	}
}
