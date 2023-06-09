/* EAIMD - eaimd.org - 2023 */
package org.eaimd.ai_md.utils;

import java.beans.Expression;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.Collator;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import lombok.SneakyThrows;

//@SuppressWarnings({ "java:S3776", "java:S1319", "java:S1066", "java:S5663", "java:S1612", "java:S135", "java:S125" })
public class CU {

	static char[] lowerCaseAlphabetWithDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	
	/* Strings */

	private static final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
	static {
		DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
		symbols.setGroupingSeparator('_');
		decimalFormat.setDecimalFormatSymbols(symbols);
	}

	public static final String SP = " "; 

	public static final String ES = ""; 

	public static final String E = System.lineSeparator(); 
	
	/* Streams */
    
	public static String loadBufferInString(byte[] buffer, boolean replaceBom) {
		
		if (buffer.length > 0 ) {
			String content = new String(buffer, StandardCharsets.UTF_8);
	        /* UTF8_BOM */
	        if (replaceBom && content.startsWith("\uFEFF")) {
	        	content = content.substring(1);
	        }
	        return content;
		}
		
		return "";
	}

	public static byte[] saveStringToBuffer(String source) {

		return source.getBytes(StandardCharsets.UTF_8);
	}
	
	public static byte[] loadInputStreamInBuffer(InputStream inputStream) {
		
		try (inputStream) {
			return inputStream.readAllBytes();
		}
		catch (IOException ioException) {
			//CL.e("loadInputStreamInString", ioException);
		}
		
		return new byte[0];
	}
	
	public static String loadInputStreamInString(InputStream inputStream, boolean replaceBom) {
		
		byte[] buffer = loadInputStreamInBuffer(inputStream);
		
		return loadBufferInString(buffer, replaceBom);
	}

	public static String loadInputStreamInString(InputStream inputStream) {
		
		return loadInputStreamInString(inputStream, true);
	}

	public static String trimStrip(String text) {
		
		if (text == null) {
			return null;
		}

		return text.trim().strip();
	}

	public static boolean isEmpty(String text) {
		
		return (text == null) || (trimStrip(text).length() == 0);
	}
	
	public static String capitalize(String text) {
		
		return text.substring(0, 1).toUpperCase() + text.substring(1);
	}
	
	/* Files */
	
	@SneakyThrows(IOException.class)
	public static byte[] loadFileInBuffer(String fileName) {

		return Files.readAllBytes(Paths.get(fileName));
	}

	@SneakyThrows(IOException.class)
	public static void saveBufferToFile(byte[] buffer, String fileName) {

		Files.write(Paths.get(fileName), buffer);
	}

	public static String loadFileInString(String fileName, boolean replaceBom) {

		byte[] buffer = loadFileInBuffer(fileName);
		
		return loadBufferInString(buffer, replaceBom);
	}

	public static String loadFileInString(String fileName) {
		
		return loadFileInString(fileName, true);
	}

	public static void saveStringToFile(String source, String fileName) {

		byte[] buffer = saveStringToBuffer(source);
		saveBufferToFile(buffer, fileName);
	}
	
	
	/* File operations */
	
	public static final String S = File.separator; 

	public static boolean findIfFileExists(String fileNameWithPath) {

		return Files.exists(Paths.get(fileNameWithPath));
	}

	public static boolean findIfIsValidPath(String fileOrFolderNameWithPath) {

		try {
			Paths.get(fileOrFolderNameWithPath);	
		} catch (InvalidPathException invalidPathException) {
			return false;
		}
		
		return true;
	}

	public static String findInvalidPathMessage(String fileOrFolderNameWithPath) {

		try {
			Paths.get(fileOrFolderNameWithPath);	
		} catch (InvalidPathException invalidPathException) {
			return invalidPathException.getMessage();
		}
		
		return "";
	}
	
	@SneakyThrows(IOException.class)
	public static void deleteFile(String fileNameWithPath) {

		Files.deleteIfExists(Paths.get(fileNameWithPath));
	}
	
	public static void deleteFolderContentsOnly(String folderNameWithPath) {

		deleteFolder(folderNameWithPath, true);
	}

	public static void deleteFolder(String folderNameWithPath) {

		deleteFolder(folderNameWithPath, false);
	}

	@SneakyThrows(IOException.class)
	private static void deleteFolder(String folderNameWithPath, boolean deleteContentsOnly) {

		Path rootPath = Paths.get(folderNameWithPath);
		if (Files.notExists(rootPath)) {
			return;
		}
		List<Path> pathsToDelete = Files.walk(rootPath).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
		if (deleteContentsOnly) {
			pathsToDelete.remove(rootPath);
		}
		for(Path path : pathsToDelete) {
		    Files.deleteIfExists(path);
		}
	}

	@SneakyThrows(IOException.class)
	public static void deleteFolderAndItsContents(String folderNameWithPath) {
	    
		Path rootPath = Paths.get(folderNameWithPath);
		if (Files.notExists(rootPath)) {
			return;
		}
		Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
		    @Override
		    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		        Files.delete(file);
		        return FileVisitResult.CONTINUE;
		    }
		    @Override
		    public FileVisitResult postVisitDirectory(Path folder, IOException ioException) throws IOException {
		        if (ioException != null) {
		            throw ioException;
		        }
		        Files.delete(folder);
		        return FileVisitResult.CONTINUE;
		    }
		});
	}
	
	@SneakyThrows(IOException.class)
	public static void createFoldersIfNotExists(String folderNameWithPath) {

		Path catalogBlocksFolderPath = Paths.get(folderNameWithPath); 
		if (Files.notExists(catalogBlocksFolderPath)) {
			Files.createDirectories(catalogBlocksFolderPath);
		}
	}

	@SneakyThrows(IOException.class)
	public static long findFileSizeInBytes(String fileNameWithPath) {

		if (findIfFileExists(fileNameWithPath)) {
			return Files.size(Paths.get(fileNameWithPath));
		}
		
		return -1;
	}

    private static class CopyFileVisitor extends SimpleFileVisitor<Path> {
		
		private final Path targetPath;
		private Path sourcePath = null;
		
		private final ArrayList<String> ignoredExtensions = new ArrayList<>();

		public CopyFileVisitor(Path targetPath, String ignoreExtensions) {
			this.targetPath = targetPath;
			
			for (String ignoredExtension : ignoreExtensions.split(";")) {
				ignoredExtensions.add(ignoredExtension.trim());
			}
		}

		@Override
		public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
			
			if (sourcePath == null) {
				sourcePath = dir;
			}
			else {
				Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
			
			String fileExtension = findExtension(file.toString());
			if (ignoredExtensions.contains(fileExtension)) {
				return FileVisitResult.CONTINUE;
			}
			Files.copy(file, targetPath.resolve(sourcePath.relativize(file)));
			return FileVisitResult.CONTINUE;
		}
	}

//	//	public static void copyFolders(Path sourcePath, Path targetPath) throws IOException {
//	
//		Files.walkFileTree(sourcePath, new CopyFileVisitor(targetPath, ""));
//	}

	@SneakyThrows(IOException.class)
	public static void copyFileOrFolderIntoFolder(String sourceFileOrFolder, String targetFolder) {
	
		Files.walkFileTree(Paths.get(sourceFileOrFolder), new CopyFileVisitor(Paths.get(targetFolder), ""));
	}
	
	@SneakyThrows(IOException.class)
	public static void copyFolders(String sourceFolder, String targetFolder, String ignoreForExtensions) {
		
		Files.walkFileTree(Paths.get(sourceFolder), new CopyFileVisitor(Paths.get(targetFolder), ignoreForExtensions));	
	}

	@SneakyThrows(IOException.class)
	public static void copyFile(String sourceFileNameWithPath, String targetFileNameWithPath) {
	
		Files.copy(Paths.get(sourceFileNameWithPath), Paths.get(targetFileNameWithPath), StandardCopyOption.REPLACE_EXISTING);
	}

	public static String findFileFolder(String fileName) {
		
		Path parentPath = Paths.get(fileName).getParent(); 
		return parentPath == null ? "" : parentPath.toString(); 
	}

	public static String findFileName(String fileName) {
		
		int fileFolderLength = findFileFolder(fileName).length();
		return fileFolderLength == 0 ? fileName : fileName.substring(fileFolderLength + S.length()); 
	}

	public static String findFileNameWithoutExtension(String fileName) {
		
		String fileNameWithExtension = findFileName(fileName);
		String fileExtension = findExtension(fileName);
		return fileNameWithExtension.substring(0, fileNameWithExtension.length() - (fileExtension.length() + 1)); 
	}

	public static String findExtension(String fileName) {

		char ch;
		int len;
		if ((fileName == null) || ((len = fileName.length()) == 0) || ((ch = fileName.charAt(len - 1)) == '/') || (ch == '\\') || (ch == '.')) {
			return "";
		}

		int dotInd = fileName.lastIndexOf('.');
		int sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
		if (dotInd <= sepInd) {
			return "";
		}
		else {
			return fileName.substring(dotInd + 1).toLowerCase();
		}
	}
	
	public static String formatSize(long size) {

		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		
		double b = size / (1024d * 1024d * 1024d);
		String suffix;
		if (b >= 1) {
			suffix = "GB";
		}
		else {
			b = size / (1024d * 1024d);
			if (b >= 1) {
				suffix = "MB";
			}
			else {
				b = size / 1024d;
				if (b >= 1) {
					suffix = "KB";
				}
				else {
					return size + " B";
				}
			}
		}
		
		return decimalFormat.format(b).replace(",", ".") + " " + suffix;
	}

	public static String formatTime(long milliseconds) {

		StringBuilder sb = new StringBuilder();
		
		long hours = milliseconds / (60 * 60 * 1000);
		if (hours > 0) {
			sb.append(hours + "h");
			milliseconds = milliseconds - (hours * 60 * 60 * 1000);
		}
		
		long minutes = milliseconds / (60 * 1000);
		if (minutes > 0) {
			sb.append(minutes + "m");
			milliseconds = milliseconds - (minutes *  60 * 1000);
		}
		else {
			if (hours > 0) {
				sb.append("0m");	
			}
		}

		long seconds = milliseconds / 1000;
		if (seconds > 0) {
			sb.append(seconds + "s");
		}
		else {
			if (minutes > 0) {
				sb.append("0s");	
			}
		}
		
		return sb.toString();
	}

	public static String formatSeconds(long milliseconds) {

		long seconds = milliseconds / 1000;
		milliseconds = milliseconds - (seconds * 1000);
		
		return "" + Double.parseDouble(seconds + "." + milliseconds);
	}

	public static String formatCount(long count) {

		return decimalFormat.format(count);
	}

	public static String formatTotalElements(int size) {

		return decimalFormat.format(size);
	}

	public static List<String> findFolderFiles(String folderName, String filter) {
		
		ArrayList<String> result = new ArrayList<>();
		
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(folderName), filter)) {
			
			stream.forEach(fileOrFolder -> {
				if (!Files.isDirectory(fileOrFolder, LinkOption.NOFOLLOW_LINKS)) {
					result.add(fileOrFolder.toString());
				}
			});
		} catch (IOException ioException) {
			//CL.e("getsFolderFiles; folderName: " + folderName + ", filter: " + filter, ioException);
		}
		
		return result;
	}

	/* Utilities */
	
	public static String deleteWhitespace(final String str) {
		
		if (isEmpty(str)) {
			return str;
		}
		final int sz = str.length();
		final char[] chs = new char[sz];
		int count = 0;
		for (int i = 0; i < sz; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				chs[count++] = str.charAt(i);
			}
		}
		if (count == sz) {
			return str;
		}
		
		return new String(chs, 0, count);
	}
	
	public static void sortStrings(List<String> unsorted) {

		Collections.sort(unsorted, Collator.getInstance(Locale.ENGLISH));
	}

	public static void sortStringsDotAfter(List<String> unsorted, List<String> names, List<String> hiddenNames) {

		/* Sort */
		for (String name : unsorted) {
			if (name.startsWith(".")) {
				hiddenNames.add(name);
			}
			else {
				names.add(name);
			}
		}
		sortStrings(names);
		sortStrings(hiddenNames);
	}

	public static String escapeQuotes(String source) {
		
		return "\"" + source.replace("\"", "\\\\\"") + "\"";
	}

	
	/* Arrays */
	
	public static String[] toArray(Stream<String> stream) {
		
		return stream.toArray(String[]::new);
	}

	public static List<String> toArrayList(Stream<String> stream) {
		
		return stream.collect(Collectors.toCollection(ArrayList<String>::new));
	}

	public static String[] toArray(List<String> arrayList) {
		
		return toArray(arrayList.stream());
	}

	public static List<String> toArrayList(String[] array) {
		
		return toArrayList(Arrays.stream(array));
	}
	
	public static void loadInputStreamInHashMap(InputStream inputStream, Map<String, String> map, String separator) {
		
		String lines = loadInputStreamInString(inputStream);
		lines.lines().forEachOrdered(line -> {
			String[] keyValue = line.split(separator);
			map.put(keyValue[0].toLowerCase(), keyValue[1]);
		});
	}

	/* JSON */
	private static final ObjectMapper jsonMapper = new ObjectMapper();
	
	@SneakyThrows({DatabindException.class, StreamReadException.class, IOException.class})
	public static <T> T jsonDeserializeFromFile(String sourceFileName, Class<T> valueType) {
		
		return jsonMapper.readValue(new File(sourceFileName), valueType);
	}

	@SneakyThrows({DatabindException.class, StreamReadException.class, IOException.class})
	public static <T> T jsonDeserialize(String content, Class<T> valueType) {
		
		return jsonMapper.readValue(content, valueType);
	}

	@SneakyThrows({DatabindException.class, StreamWriteException.class, IOException.class})
	public static void jsonSerializeToFile(String resultFileName, Object value) {

		jsonMapper.writerWithDefaultPrettyPrinter().writeValue(new File(resultFileName), value);
	}

	@SneakyThrows({DatabindException.class, StreamWriteException.class, IOException.class})
	public static String jsonSerialize(Object value) {

		return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
	}

	/* TOML */
	private static final ObjectMapper tomlMapper = new TomlMapper();
	
	@SneakyThrows({DatabindException.class, StreamReadException.class, IOException.class})
	public static <T> T tomlDeserializeFromFile(String sourceFileName, Class<T> valueType) {
		
		return tomlMapper.readValue(new File(sourceFileName), valueType);
	}

	@SneakyThrows({DatabindException.class, StreamReadException.class, IOException.class})
	public static <T> T tomlDeserialize(String content, Class<T> valueType) {
		
		return tomlMapper.readValue(content, valueType);
	}

	@SneakyThrows({DatabindException.class, StreamWriteException.class, IOException.class})
	public static void tomlSerializeToFile(String resultFileName, Object value) {

		tomlMapper.writerWithDefaultPrettyPrinter().writeValue(new File(resultFileName), value);
	}

	@SneakyThrows({DatabindException.class, StreamWriteException.class, IOException.class})
	public static String tomlSerialize(Object value) {

		return tomlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
	}

	/* YAML */
	private static final ObjectMapper yamlMapper = new YAMLMapper();
	
	@SneakyThrows({DatabindException.class, StreamReadException.class, IOException.class})
	public static <T> T yamlDeserializeFromFile(String sourceFileName, Class<T> valueType) {
		
		return yamlMapper.readValue(new File(sourceFileName), valueType);
	}

	@SneakyThrows({DatabindException.class, StreamReadException.class, IOException.class})
	public static <T> T yamlDeserialize(String content, Class<T> valueType) {
		
		return yamlMapper.readValue(content, valueType);
	}

	@SneakyThrows({DatabindException.class, StreamWriteException.class, IOException.class})
	public static void yamlSerializeToFile(String resultFileName, Object value) {

		yamlMapper.writerWithDefaultPrettyPrinter().writeValue(new File(resultFileName), value);
	}

	@SneakyThrows({DatabindException.class, StreamWriteException.class, IOException.class})
	public static String yamlSerialize(Object value) {
		
		return yamlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
	}

	public static void exec(Object target, String methodName, Object argument) {
		
		try {
			Object[] oneArgument = new Object[1];
			oneArgument[0] = argument;
			(new Expression(target, methodName, oneArgument)).execute();
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	}

	public static void exec(Object target, String methodName) {
		
		try {
			(new Expression(target, methodName, new Object[0])).execute();
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	}

	public static Object val(Object target, String methodName) {

		try {
			return (new Expression(target, methodName, new Object[0])).getValue();
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	}

	@SneakyThrows({InterruptedException.class, ExecutionException.class})
	public static void await(Future<?> future) {

		future.get();
	}
	
	public static String generateUniqueId() {
		
		return NanoIdUtils.randomNanoId(new Random(), lowerCaseAlphabetWithDigits, 8);
	}
}
