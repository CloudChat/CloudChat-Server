package fr.cloudchat.data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;

public class ConfigFile {
	public DatabaseConfig mysql;
	
	public static ConfigFile getConfig() {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get("config.json"));
			String content = new String(encoded, StandardCharsets.UTF_8);
			Gson gson = new Gson();
			return gson.fromJson(content, ConfigFile.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public DatabaseConfig getMysql() {
		return mysql;
	}

	public void setMysql(DatabaseConfig mysql) {
		this.mysql = mysql;
	}

	public class DatabaseConfig {
		private String username;
		private String password;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getDatabase() {
			return database;
		}
		public void setDatabase(String database) {
			this.database = database;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		private String database;
		private String url;
	}
}
