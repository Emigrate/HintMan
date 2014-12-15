package com.colleagues.hintman.objects;
import java.io.*;

public class Hint implements Serializable
{
	public String title;
	public String content;
	public String date;
	public String grup;
	public boolean expired;
	public boolean voted;
	public String voteValue;
	public long groupId;
	public long userId;
	public long id;
}
