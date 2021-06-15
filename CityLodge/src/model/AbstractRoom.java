package model;

import java.util.ArrayList;

import util.DateTime;

public abstract class AbstractRoom implements Room
{
	private String roomID;
	private int noOfBeds;
	private String featureSummary;
	private String roomType;
	private RoomStatus roomStatus;
	private ArrayList<HiringRecord> hiringRecord;
	private DateTime lastMaintenanceDate;
	
	public AbstractRoom(String roomID, int noOfBeds, String featureSummary, String roomType, RoomStatus roomStatus, DateTime lastMaintenanceDate)
	{
		this.roomID = roomID;
		this.noOfBeds = noOfBeds;
		this.featureSummary = featureSummary;
		this.roomType = roomType;
		this.roomStatus = roomStatus;
		this.lastMaintenanceDate = lastMaintenanceDate;
		this.hiringRecord = new ArrayList<HiringRecord>();
	}
	
	@Override
	public String getRoomID()
	{
		return roomID;
	}
	
	@Override
	public int getNoOfBeds()
	{
		return noOfBeds;
	}
	
	@Override
	public String getSummary()
	{
		return featureSummary;
	}
	
	@Override
	public String getRoomType()
	{
		return roomType;
	}
	
	@Override
	public RoomStatus getRoomStatus()
	{
		return roomStatus;
	}
	
	public ArrayList<HiringRecord> getHiringRecord()
	{
		return hiringRecord;
	}
	
	public void setRoomStatus(RoomStatus rs)
	{
		roomStatus = rs;
	}
	
	public DateTime getLastMaintenanceDate()
	{
		return lastMaintenanceDate;
	}
	
	public void addRecord(String customerID, DateTime rentDate, int NumberOfRentDay)
	{
		HiringRecord hiringRec = new HiringRecord(getRoomID(),customerID,rentDate,NumberOfRentDay);
		hiringRecord.add(hiringRec);
	}
	
	public boolean returnRoom(DateTime returnDate)
	{
		if(getRoomStatus()==RoomStatus.RENTED)
		{
			HiringRecord record = hiringRecord.get(hiringRecord.size() - 1);
			DateTime rentDate = record.getRentDate();
			int diff = DateTime.diffDays(returnDate, rentDate);
			if(diff > 0)
			{
				setRoomStatus(RoomStatus.AVAILABLE);
				return true;
			}	
		}
		return false;
	}
	
	public boolean performMaintenance()
	{
		if(this.getRoomStatus() == (RoomStatus.RENTED))
		{
			return false;
		}
		else
		{
			this.setRoomStatus(RoomStatus.MAINTENANCE);
		}
		return true;
	}
	
	public boolean completeMaintenance(DateTime completionDate)
	{
		if(this.getRoomStatus() == RoomStatus.MAINTENANCE)
		{
			this.setRoomStatus(RoomStatus.AVAILABLE);
			lastMaintenanceDate = completionDate;
			return true;
		}
		return false;
	}
	
	public String getRecentRecord()
	{
		String s = "";
		int i = hiringRecord.size() - 1;
		int j = 0;
		while(i >= 0 && j <= 10)
		{
			s = s + hiringRecord.get(i).getDetails() + "---------------------------------------------\n";
			j++;
			i--;
		}
		return s;
	}
	
	public abstract void setFees(HiringRecord hiringR);
	public abstract boolean rent(String customerID, DateTime rentDate, int NumberOfRentDay);
	public abstract String toString();
	public abstract String getDetails();
}
