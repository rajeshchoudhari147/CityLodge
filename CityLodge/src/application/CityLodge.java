package application;

import java.util.ArrayList;
import java.util.Scanner;

import model.AbstractRoom;
import model.HiringRecord;
import model.RoomStatus;
import model.StandardRoom;
import model.Suite;
import util.DateTime;

public class CityLodge 
{
	private ArrayList<AbstractRoom> rooms = new ArrayList<>(50);
	Scanner sc = new Scanner(System.in);
	
	public void menu()
	{
		System.out.println("*****CITY LODGE MAIN MENU*****");
		System.out.printf("%-26s %-10s\n", "Add Room:", "1");
	    System.out.printf("%-26s %-10s\n", "Rent Room:", "2");
	    System.out.printf("%-26s %-10s\n", "Return Room:", "3");
	    System.out.printf("%-26s %-10s\n", "Room Maintenance:", "4");
	    System.out.printf("%-26s %-10s\n", "Complete Maintenance:", "5");
	    System.out.printf("%-26s %-10s\n", "Display All Rooms:", "6");
	    System.out.printf("%-26s %-10s\n", "Exit Program:", "7");
	    System.out.printf("%-26s\n" , "Enter your choice:");
	}
	
	public DateTime formatDate(String date)
	{
		String[] format = date.split("/");
		int dd = Integer.parseInt(format[0]);
		int mm = Integer.parseInt(format[1]);
		int yy = Integer.parseInt(format[2]);
		
		DateTime dateTime = new DateTime(dd,mm,yy);
		return dateTime;
	}
	
	public int getObject(String roomID)
	{
		int len = rooms.size();
		int i, obj = -1;
		for(i = 0; i < len; i++)
			if(rooms.get(i).getRoomID().equals(roomID))
				obj=i;
		return obj;
	}
		
	public void start()
	{
		menu();
		int c = sc.nextInt();
		String roomID;
		int obj;
		char type;
		switch(c)
		{
			//Add Room 
			case 1:	AbstractRoom room;
					System.out.print("Enter Room Type (Room or Suite): ");
					String roomType = sc.next();
					type = roomType.charAt(0);
					System.out.print("Enter Room ID: ");
					roomID = sc.next();
					obj = getObject(roomID);
					if(obj == -1)
					{
						if (roomID.charAt(0) != type)
						{
							System.out.println("\nPlease enter ROOM ID in the format: R_000 for Room & S_000 for Suite.\n");
							break;
						}
						sc.nextLine();
						System.out.print("Enter Room Feature Summary: ");
						String summary = sc.nextLine();
						int noOfBeds = 6;
						if (type == 'R')
						{
							do
							{
								System.out.print("Enter number of Beds (Only Choose 1, 2 or 4): ");
								noOfBeds = sc.nextInt();
							}while((noOfBeds != 1) && (noOfBeds != 2) && (noOfBeds != 4));
						}
						DateTime lastMaintenanceDate = new DateTime();
						if (type == 'S')
						{
							System.out.print("Enter Last Maintenance Date (dd/mm/yyyy): ");
							String date = sc.next();
							lastMaintenanceDate = formatDate(date);
						}
						RoomStatus roomStatus = RoomStatus.AVAILABLE;
					
						if (type == 'S')
						{
							room = new Suite(roomID, noOfBeds, summary, roomStatus, roomType, lastMaintenanceDate);
						}
						else
						{
							room = new StandardRoom(roomID, noOfBeds, summary, roomType, roomStatus, lastMaintenanceDate);
						}
						rooms.add(room);
						System.out.println("\nRoom " + roomID + " has been added to the system.\n");
					
						break;
					}
					else
					{
						System.out.println("\nRoom " + roomID + " already exists.\n");
						break;
					}
					
			//Rent Room
			case 2: System.out.print("Enter Room ID: ");
					roomID = sc.next();
					obj = getObject(roomID);
					if(obj != -1)
					{
						System.out.print("Enter Customer ID: ");
						String customerID = sc.next();
						System.out.print("Enter Room Rent Date (dd/mm/yyyy): ");
						String date = sc.next();
						DateTime rentDate = formatDate(date);
						System.out.print("Enter number of Rent days: ");
						int noOfRentDay = sc.nextInt();
						if (rooms.get(obj).rent(customerID,	rentDate, noOfRentDay))
							System.out.println("\nRoom " + roomID + " is now rented " + " by Customer " + customerID  + ".\n");
						else
							System.out.println("\nRoom " + roomID + " could not be rented.\n");
					}
					else
						System.out.println("\nRoom " + roomID + " does not exist.\n");
					break;
			
			//Return Room
			case 3: System.out.print("Enter Room ID: ");
					roomID = sc.next();
					obj = getObject(roomID);
					if(obj != -1)
					{
						System.out.print("Enter Room Return Date (dd/mm/yyyy): ");
						String date = sc.next();
						DateTime returnDate = formatDate(date);
						if (rooms.get(obj).returnRoom(returnDate))
						{
							ArrayList<HiringRecord> hiringRecord = rooms.get(obj).getHiringRecord();
							HiringRecord record = hiringRecord.get(hiringRecord.size()-1);
							record.setActualReturnDate(returnDate);
							rooms.get(obj).setFees(record);
							System.out.println("\nRoom Returned Successfully.\n");
							String details_room = rooms.get(obj).getDetails();
							String details_record = record.getDetails();
							System.out.println(details_room + "\n" + details_record);
						}
						else
							System.out.println("\nRoom " + roomID + " cannot be returned.\n");
					}
					else 
						System.out.println("\nRoom " + roomID + " does not exist.\n");
	
					break;
			
			//Room Maintenance
			case 4: System.out.print("Enter Room ID: ");
					roomID = sc.next();
					obj = getObject(roomID);
					if(obj != -1)
					{
						if(rooms.get(obj).performMaintenance())
							System.out.println("\nRoom " + roomID + " is now under maintenance.\n");
						else
							System.out.println("\nRoom " + roomID + " is currently rented.\n");
					}
					else
						System.out.println("\nRoom " + roomID + " does not exist.\n");
					break;
					
			//Complete Maintenance	
			case 5: System.out.print("Enter Room ID: ");
					roomID = sc.next();
					obj = getObject(roomID);
					if(obj != -1)
					{
						System.out.print("Enter Maintenance Completion date (dd/mm/yyyy): ");
						String rawDate = sc.next();
						DateTime lastDate = formatDate(rawDate);
						if(rooms.get(obj).completeMaintenance(lastDate))
							System.out.println("\nRoom "+ roomID +" has all maintenance completed and is ready for rent.\n");
						else
							System.out.println("\nRoom " + roomID + " maintenance could not be completed.\n");
					}
					else
						System.out.println("\nRoom " + roomID + " does not exist.\n");
					break;
	
			//Display all Rooms
			case 6:	int i;
				int len = rooms.size();
				for(i=0;i<len;i++)
					System.out.println(rooms.get(i).getDetails() + "\n*******HIRING RECORD*******\n" + ((rooms.get(i).getRecentRecord() == "")? "None": rooms.get(i).getRecentRecord()) + "\n");
				break;
	
			//Exit
			case 7:	System.out.println("\nQuiting Application...");
					return;

			default: System.out.println("\nInvalid choice");	
		}
		start();
	}
}
