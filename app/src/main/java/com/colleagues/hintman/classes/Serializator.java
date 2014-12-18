package com.colleagues.hintman.classes;


import android.content.Context;
import android.os.Environment;

import java.io.*;
import java.util.ArrayList;
import android.util.*;

/*
    for saving and getting groups , users , albums ;
 */

public class Serializator<T extends Serializable>
{
    

    private Context context;
    private final String name;

    public Serializator(Context context,String name)
    {
        this.context = context;
        this.name = name;
    }

    private File getPathByType()
    {

        String dir =  String.format("/Android/data/%s/files/", context.getPackageName());

        File sd = context.getFilesDir();
		File filePath = null;
		if(sd == null){
			sd = Environment.getExternalStorageDirectory();
			filePath = new File(sd.getPath() + dir,name.toString() +".txt");
		}else{
			filePath = new File(sd.getPath(), name.toString() +".txt");
		}
								 

        File dumpdir = filePath.getParentFile();
        dumpdir.mkdirs();

        return filePath;
    }

    /*
	 throw exception

	 private Class returnedClass()
	 {
	 ParameterizedType parameterizedType = (ParameterizedType)getClass().getGenericSuperclass();
	 return (Class) parameterizedType.getActualTypeArguments()[0];
	 }
	 */

    public ArrayList<T> inSerialize(ArrayList<T> objects)
    {
        File file = getPathByType();

        OutputStream fileStream = null;
        ObjectOutputStream objectStream = null;


        if(file.exists())
            file.delete();

        try
        {

            fileStream = new FileOutputStream(file);
            objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(objects);


        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(fileStream != null)
                try {
                    fileStream.flush();
                    fileStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if(objectStream != null)
                try {
                    objectStream.flush();
                    objectStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
		return objects;
    }
    public ArrayList<T> getSerialization()
    {
        ArrayList<T> list = new ArrayList<T>();

        File distinctPath = getPathByType();

        if(!distinctPath.exists())
            return list;


        InputStream outFile = null;
        ObjectInputStream outObject = null;
        try
        {


            outFile = new FileInputStream(distinctPath);
            outObject = new ObjectInputStream(outFile);


            list  = (ArrayList <T>)outObject.readObject();

            return list;


        }
        catch(IOException e)
        {
            e.printStackTrace();
            return list;
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return list;
        }

        finally
        {
            if(outFile != null)
                try {
                    outFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if(outObject != null)
                try {
                    outObject.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }

}
