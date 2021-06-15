package model;

import java.util.Formatter;

import util.DateTime;

public class Suite extends AbstractRoom
{
	private static final double SIX_BED = 999.0;
	private static final double LATE_FEE = 1099.00;
	private static final int MAINTENANCE_INTERVAL = 10;
	public Formatter fmt;
	
	public Suite(String roomID, int noOfBeds, String featureSummary,
			RoomStatus roomStatus, String roomType, DateTime lastMaintenanceDate)
	{
		super(roomID, noOfBeds, featureSummary, roomType, roomStatus, lastMaintenanceDate);
	}
	
	public void setFees(HiringRecord hiringRecord)
	{
		DateTime actualReturnDate = hiringRecord.getActualReturnDate();
		DateTime estimatedReturnDate = hiringRecord.getEstimatedreturnDate();
		DateTime rentDate = hiringRecord.getRentDate();
		int rentDays = DateTime.diffDays(estimatedReturnDate,rentDate);
		int lateDays = DateTime.diffDays(actualReturnDate, estimatedReturnDate);
		if (lateDays <= 0)
		{
			hiringRecord.setLateFee(0.0);
			hiringRecord.setRentalFee(SIX_BED * (rentDays+lateDays));
		}
		else
		{
			hiringRecord.setLateFee(SIX_BED * LATE_FEE * lateDays);
			hiringRecord.setRentalFee(SIX_BED * rentDays);
		}
	}
	
	public boolean rent(String customerID, DateTime rentDate, int NumberOfRentDay)
	{
		if (getRoomStatus() == RoomStatus.AVAILABLE)
		{
			if(NumberOfRentDay < 1)
			{
				return false;
			}
			DateTime nextMaintenanceDate = new DateTime(getLastMaintenanceDate(), MAINTENANCE_INTERVAL);
			int diff = DateTime.diffDays(nextMaintenanceDate, rentDate);
			if (NumberOfRentDay > diff)
				return false;
			addRecord(customerID, rentDate, NumberOfRentDay);
			setRoomStatus(RoomStatus.RENTED);
			return true;
		}
		else
			return false;
	}

	public String toString()
	{
		String s =  getRoomID() + ":" + 
					getNoOfBeds() + ":" +
					getRoomType() + ":" +
					getRoomStatus() + ":" +
					getLastMaintenanceDate() + ":" +
					getSummary();
		return s;
	}
	
	public String getDetails()
	{
		StringBuilder sbuf = new StringBuilder();
		fmt = new Formatter(sbuf);
		fmt.format("%-26s %-10s\n", "Room ID:", getRoomID());
		fmt.format("%-26s %-10s\n", "Number of Beds:", getNoOfBeds());
		fmt.format("%-26s %-10s\n", "Feature Summary:", getSummary());
		fmt.format("%-26s %-10s\n", "Status:", getRoomStatus());
		fmt.format("%-26s %-10s\n", "Last Maintenance Date:", getLastMaintenanceDate());
		return sbuf.toString();
	}
}
