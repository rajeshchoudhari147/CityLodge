package model;

public interface Room 
{
	String getRoomID();
	
	RoomStatus getRoomStatus();

	String getSummary();

	int getNoOfBeds();

	String getRoomType();
}
