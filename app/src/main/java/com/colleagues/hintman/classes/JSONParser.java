package com.colleagues.hintman.classes;
import com.colleagues.hintman.objects.*;
import java.util.*;
import org.json.*;
import android.util.*;

public class JSONParser
{
	
	public ArrayList<Hint>getHintsList(JSONObject jsonIbject){
	ArrayList<Hint> list = new ArrayList<Hint>();
		try
		{
			JSONObject group = jsonIbject.getJSONObject("group");
			JSONArray jsonArray = group.getJSONArray("active_hints");
			for(int i = 0; i < jsonArray.length(); i++){
				
				JSONObject object = jsonArray.getJSONObject(i);
				Hint hint = new Hint();
				hint.id = object.getLong("id");
				hint.content = object.getString("content");
				hint.userId =object.getLong("user_id");
				hint.date = object.getString("created_at");
				
				/*hint.userId = object.getLong("user_id");
				hint.groupId = object.getLong("group_id");
				hint.title = object.getString("title");
			
				hint.date = object.getString("created_at");
				*/
				list.add(hint);
			}
		}
		catch (JSONException e)
		{
			
		}

		
		
		return list;
	}
	public ArrayList<Group>getGroupList(JSONObject jsonIbject){
		ArrayList<Group> list = new ArrayList<Group>();
		try
		{
			JSONArray jsonArray = jsonIbject.getJSONArray("groups");
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject object = jsonArray.getJSONObject(i);
				Group hint = new Group();
				hint.id = object.getLong("id");
				hint.title = object.getString("name");
				list.add(hint);
			}
		}
		catch (JSONException e)
		{

		}



		return list;
	}
	
	public Hint getHint(JSONObject jsonIbject){
		Hint hint = new Hint();
		try
		{
			hint.date = jsonIbject.getString("created_at");
			hint.id = jsonIbject.getLong("id");
			hint.content = jsonIbject.getString("content");
			JSONObject group = jsonIbject.getJSONObject("group");
			hint.grup = group.getString("name");
			hint.groupId = jsonIbject.getLong("group_id");
			hint.userId = jsonIbject.getLong("user_id");
			hint.expired = jsonIbject.getBoolean("expired");
			hint.voted = jsonIbject.getBoolean("voted");
			hint.voteValue = jsonIbject.getString("vote_value");
		}
		catch (JSONException e)
		{

		}

		return hint;
	}
	
	public Hint getPushHint(JSONObject jsonIbject){
		Hint hint = new Hint();
		try
		{
			JSONObject data = jsonIbject.getJSONObject("data");
			hint.id = data.getLong("hint_id");
			hint.content = data.getString("alert");
			hint.grup = data.getString("name");
		}
		catch (JSONException e)
		{

		}

		return hint;
	}
}
