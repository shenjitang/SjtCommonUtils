package org.shenjitang.common.util;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {
    public static final String INVALID_PARAMETER = "传递非法参数";
	private FileUtils() {

	}

	static {
		DEFAULT_CHARSET = SystemUtils.systemCharset();
	}

	public static String getPath(String path) {
		return path.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
	}

	public static final String FILE_SEPARATOR = System
			.getProperty("file.separator");

	public static void write(String content, String file) {
		write(content, file, false);
	}

	public static void write(String content, String file, boolean append) {
		BufferedWriter writer = null;
		PrintWriter out = null;
		try {
			if (append) {
				writer = new BufferedWriter(
						new FileWriter(new File(file), true));
			} else {
				createNewFile(file);
				writer = new BufferedWriter(new FileWriter(new File(file),
						false));
			}
			out = new PrintWriter(writer);
			if (append) {
				out.append(content);
			} else {
				out.write(content);
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (out != null) {
					out.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static final String DEFAULT_CHARSET;

	public static void append(String content, String file) {
		FileChannel channel = null;
		try {
			channel = new RandomAccessFile(file, "rw").getChannel();
			channel.position(channel.size());
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			channel.write(ByteBuffer.wrap(content.getBytes()));
			buffer.clear();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void writeByUTF8(String content, String file) {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			createNewFile(file);
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos, "UTF-8");
			osw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (osw != null) {
					osw.close();
				}
				if (fos != null) {
					fos.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public static void createNewDir(String dir) {
		if (StringUtils.isEmpty(dir)) {
			throw new IllegalArgumentException(INVALID_PARAMETER);
		}
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}

	}

	public static void createNewFile(String file) {
		if (StringUtils.isEmpty(file)) {
			throw new IllegalArgumentException(INVALID_PARAMETER);
		}
		try {
			String arg = file.substring(0, file.lastIndexOf("/"));
			createNewDir(arg);
			File f = new File(file);
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String read(String file) {
		if (StringUtils.isEmpty(file)) {
			throw new IllegalArgumentException(INVALID_PARAMETER);
		}
		StringBuffer sb = new StringBuffer("");
		try {
			FileChannel fc = new FileInputStream(file).getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			byte[] bytes;
			while ((fc.read(buffer)) != -1) {
				buffer.flip();
				bytes = new byte[buffer.remaining()];
				buffer.get(bytes);
				sb.append(new String(bytes, DEFAULT_CHARSET));
				buffer.clear();
			}
			fc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static void copy(String source, String target) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			ByteBuffer buffer = ByteBuffer.allocate(2048);
			fis = new FileInputStream(source);
			in = fis.getChannel();
			fos = new FileOutputStream(target);
			out = fos.getChannel();
			int len = -1;
			while ((len = in.read(buffer)) != -1) {
				buffer.flip();
				out.write(buffer);
				buffer.clear();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
				if (fos != null) {
					fos.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean delete(String file) {
		return delete(new File(file));
	}

	public static boolean delete(File file) {
		if (file == null) {
			throw new IllegalArgumentException(INVALID_PARAMETER);
		}
		if (!file.exists()) {
			return false;
		}
		if (file.isFile()) {
			file.delete();
		} else if (file.isDirectory() && StringUtils.isNotEmpty(file.getName())) {
			File[] files = file.listFiles();
			for (File f : files) {
				if (f.isFile()) {
					f.delete();
				} else if (f.isDirectory()) {
					delete(f);
				}
			}
			file.delete();
		}
		return true;
	}

	public static String getClassPath() {
		String path = Thread.currentThread().getContextClassLoader()
				.getResource("").getPath();
		path = path.replace("%20", " ");
		return SystemUtils.isWindowsSystem() ? path.substring(1) : path;
	}

	public static String getProjectPath() {
		String path = new File("").getAbsolutePath();
		return path;
	}

	public static List<String> list(String dir) {
		List<String> result = new ArrayList<String>();
		LinkedList<File> list = new LinkedList<File>();
		File source = new File(dir);
		File[] files = source.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				list.add(file);
			} else {
				result.add(file.getAbsolutePath());
			}
		}
		File f;
		while (!list.isEmpty()) {
			f = list.removeFirst();
			if (f.isDirectory()) {
				files = f.listFiles();
				if (files == null) {
					continue;
				}
				for (File file : files) {
					if (file.isDirectory()) {
						list.add(file);
					} else {
						result.add(file.getAbsolutePath());
					}
				}
			} else {
				result.add(f.getAbsolutePath());
			}
		}
		return result;
	}

	public static void main(String[] args) {
	}
}
