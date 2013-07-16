package fart.dungeoncrawler.mapeditor;

public class MapToInfo
{
	private String mapToName;
	private boolean validName;
	private String mapToX;
	private boolean validX;
	private String mapToY;
	private boolean validY;
	
	public MapToInfo()
	{
		mapToName = "";
		mapToX = "";
		mapToY = "";
		
		validName = false;
		validX = false;
		validY = false;
	}
	
	public MapToInfo(String mapToName, String mapToX, String mapToY)
	{
		setMapToName(mapToName);
		setMapToX(mapToX);
		setMapToY(mapToY);
	}
	
	public void setMapToName(String mapToName)
	{
		this.mapToName = mapToName;
		
		if(this.mapToName.equals(""))
			validName = false;
		else
			validName = true;
	}
	
	public void setMapToX(String mapToX)
	{
		try
		{
			int coord = Integer.parseInt(mapToX);
			
			this.mapToX = mapToX;
			if((coord >= 0) && (coord <= 32))
				validX = true;
			else
				validX = false;
		}
		catch(NumberFormatException ex)
		{
			validX = false;
		}
	}
	
	public void setMapToY(String mapToY)
	{
		try
		{
			int coord = Integer.parseInt(mapToY);
			
			this.mapToY = mapToY;
			if((coord >= 0) && (coord <= 32))
				validY = true;
			else
				validY = false;
		}
		catch(NumberFormatException ex)
		{
			validY = false;
		}
	}
	
	public String getMapToName() { return mapToName; }
	public String getMapToX() { return mapToX; }
	public String getMapToY() { return mapToY; }
	
	public boolean isMapToNameValid() { return validName; }
	public boolean isMapToXValid() { return validX; }
	public boolean isMapToYValid() { return validY; }
	
	public boolean isInformationValid()
	{
		if(validName && validX && validY)
			return true;
		else
			return false;
	}
}
