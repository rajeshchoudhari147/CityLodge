package model;

import java.util.Formatter;

import util.DateTime;

public class StandardRoom extends AbstractRoom
{
	public Formatter fmt;
	private static final double ONE_BED = 59.0;
	private static final double TWO_BED = 99.0;
	private static final double FOUR_BED = 199.0;
	private static final double LATE_FEE = 1.35;
	
	public StandardRoom(String roomID, int noOfBeds, String featureSummary, String roomType,
			RoomStatus roomStatus, DateTime lastMaintenanceDate)
	{
		super(roomID, noOfBeds, featureSummary, roomType, roomStatus, lastMaintenanceDate);
	}
	
	public void setFees(HiringRecord hiringRecord)
	{
		DateTime actualReturnDate = hiringRecord.getActualReturnDate();
		DateTime estimatedReturnDate = hiringRecord.getEstimatedreturnDate();
		DateTime rentDate = hiringRecord.getRentDate();
		int noOfBeds = this.getNoOfBeds();
		int rentDays = DateTime.diffDays(estimatedReturnDate, rentDate);
		int lateDays = DateTime.diffDays(actualReturnDate, estimatedReturnDate);
		if(lateDays <= 0)
		{
			hiringRecord.setLateFee(0.0);
			if (noOfBeds == 1)
			{
				hiringRecord.setRentalFee(ONE_BED * (rentDays + lateDays));
			}
			else if(noOfBeds == 2)
			{
				hiringRecord.setRentalFee(TWO_BED * (rentDays + lateDays));
			}
			else if(noOfBeds == 4)
			{
				hiringRecord.setRentalFee(FOUR_BED * (rentDays + lateDays));
			}
		}
		else
		{
			if (noOfBeds == 1)
			{
				hiringRecord.setLateFee(LATE_FEE * ONE_BED * lateDays);
				hiringRecord.setRentalFee(ONE_BED * rentDays);
			}
			else if(noOfBeds == 2)
			{
				hiringRecord.setLateFee(LATE_FEE * TWO_BED * lateDays);
				hiringRecord.setRentalFee(TWO_BED * rentDays);
			}
			else if(noOfBeds == 4)
			{
				hiringRecord.setLateFee(LATE_FEE * FOUR_BED * lateDays);
				hiringRecord.setRentalFee(FOUR_BED * rentDays);
			}
		}
	}
	
	public boolean rent(String customerID, DateTime rentDate, int NumberOfRentDay)
	{
		if(getRoomStatus()==RoomStatus.AVAILABLE)
		{
			String Day = rentDate.getNameOfDay();
			if(Day.equals("Saturday") || Day.equals("Sunday"))
			{
				if(NumberOfRentDay < 3 || NumberOfRentDay > 10)
				{
					System.out.println("Hiring period should be minimum of 3 days and maximum 10 days.");
					return false;
				}
			}
			else
			{
				if(NumberOfRentDay < 2 || NumberOfRentDay > 10)
				{
					System.out.println("Hiring period should be minimum of 2 days and maximum 10 days.");
					return false;
				}
			}
			addRecord(customerID, rentDate, NumberOfRentDay);
			setRoomStatus(RoomStatus.RENTED);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String toString()
	{
		String s =  getRoomID() + ":" + 
					getNoOfBeds() + ":" +
					getRoomType() + ":" +
					getRoomStatus() + ":" +
					getSummary();
		return s;
	}
	
	public String getDetails()
	{
		StringBuilder sb = new StringBuilder();
		fmt = new Formatter(sb);
		fmt.format("%-26s %-10s\n", "Room ID:", getRoomID());
		fmt.format("%-26s %-10s\n", "Number of Beds:", getNoOfBeds());
		fmt.format("%-26s %-10s\n", "Feature Summary:", getSummary());
		fmt.format("%-26s %-10s\n", "Status:", getRoomStatus());
		return sb.toString();
	}
}
