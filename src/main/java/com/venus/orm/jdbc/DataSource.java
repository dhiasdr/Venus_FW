package com.venus.orm.jdbc;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DataSource {
	private String driverClassName;
	private String url;
	private String username;
	private String password;
	private final static String PROPERTIES_PATH="src/main/resources/application.properties";

	private Map<String, String> properties = new HashMap<String, String>();

	public DataSource() {
		String line = null;
		Scanner sc = null;
		try {
			sc = new Scanner(new File(PROPERTIES_PATH));
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				String[] parts = line.split("=", 2);
				if (parts.length >= 2) {
					String key = parts[0];
					String value = parts[1];
					properties.put(key, value);
				} else {
					System.out.println("ignoring line: " + line);
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		setProperties();
	}

	private void setProperties() {
		driverClassName = properties.get("myorm.datasource.driver-class-name");
		url = properties.get("myorm.datasource.url");
		username = properties.get("myorm.datasource.username");
		password = properties.get("myorm.datasource.password");
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataSource other = (DataSource) obj;
		if (driverClassName == null) {
			if (other.driverClassName != null)
				return false;
		} else if (!driverClassName.equals(other.driverClassName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	
}
