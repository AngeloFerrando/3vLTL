package unige.abstraction.beans;

import java.io.File;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonObject {
	
	private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	@Override
	public String toString() {
		return GSON.toJson(this);
	}
	
	public static <T> T load(File fileName, Class<T> clazz) throws Exception {
		return GSON.fromJson(new FileReader(fileName), clazz);
	}

	public static <T> T load(String json, Class<T> clazz) throws Exception {
		return GSON.fromJson(json, clazz);
	}

}
