package com.venus.orm;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import com.venus.orm.annotation.Entity;
import com.venus.orm.exception.FieldsMapperException;

public class OrmUtils {
	private static final String PROJECT_RACINE= "src/main/java/";

	private static void allProjectPackages(String directoryName, Set<String> packs) {
		File directory = new File(directoryName);
		File[] filesList = directory.listFiles();
		for (File file : filesList) {
			if (file.isFile()) {
				String path = file.getPath();
				String packName = path.substring(path.indexOf("src") + 14, path.lastIndexOf('\\'));
				packs.add(packName.replace('\\', '.'));
			} else if (file.isDirectory()) {
				allProjectPackages(file.getAbsolutePath(), packs);
			}
		}
	}

	private static Set<Class<? extends Object>> getClassesInPackage(String package_Name) {
		Reflections reflections = new Reflections(package_Name, new SubTypesScanner(false));

		Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);

		return allClasses;

	}

	public static ArrayList<Class<?>> listOfClassesWithAnotationEntity() {
		ArrayList<Class<?>> listofEntity = new ArrayList<Class<?>>();

		Set<String> packagesNames = new HashSet<>();
		allProjectPackages(PROJECT_RACINE, packagesNames);

		for (String pn : packagesNames) {
			Set<Class<? extends Object>> allClasses = getClassesInPackage(pn);
			for (Class<? extends Object> cls : allClasses) {

				if (cls.isAnnotationPresent(Entity.class) && !listofEntity.contains(cls)) {
					listofEntity.add(cls);
				}

			}
		}
		return listofEntity;

	}

}
