package com.sk.util;

/** 
 * 
 *	payments
 *	FileUtil.java 
 *	@author huPengfei
 *	Aug 12, 2010 : 4:56:07 PM
 *
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author hpf
 * 
 */
public class FileUtil {

	protected static Log logger = LogFactory.getLog(FileUtil.class);

	public static void main(String[] args) {
		writeFile("d:/message-bi.html", "hello", "utf-8");
		readFile("d:/message-bi.html");
	}

	public static void writeFile(File f, String content, String encoding) {
		String s = new String();
		StringBuffer sb = new StringBuffer();
		try {
			if (f.exists()) {
				logger.info("文件" + f.getPath() + "存在,删除原来文件");
				f.delete();
			}
			logger.info("文件" + f.getPath() + "正在创建...");
			if (f.createNewFile()) {
				logger.info("文件" + f.getPath() + "创建成功！");
			} else {
				logger.info("文件" + f.getPath() + "创建失败！");
			}

			BufferedReader input = new BufferedReader(new FileReader(f));

			while ((s = input.readLine()) != null) {
				sb.append(s).append("\n");
			}
			System.out.println("文件" + f.getPath() + "内容：" + sb.toString());
			input.close();
			sb.append(content);
			logger.info("file=" + f.getPath() + "\t" + encoding);
			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), encoding));
			output.write(sb.toString());
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeFile(String filePath, String content, String encoding) {
		writeFile(new File(filePath), content, encoding);
	}

	/**
	 * 读取文件内容
	 * 
	 * @param filePath
	 * @return
	 */
	public static String readFile(String filePath) {
		String s = null;
		StringBuffer sb = new StringBuffer();
		File f = new File(filePath);
		if (f.exists()) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
				while ((s = br.readLine()) != null) {
					sb.append(s);
				}
				return sb.toString();
			} catch (Exception e) {
				System.out.println("读取文件" + filePath + "出错!");
				e.printStackTrace();
				return "";
			}
		} else {
			System.out.println("文件" + filePath + "不存在!");
			return "";
		}
	}

	public final static String WINDOWS = "Windows";

	public final static String LINUX = "Linux";

	/***************************************************************************
	 * 判断操作系统
	 * 
	 * @return
	 *************************************************************************/
	public static String getOS() {
		if (File.separator.equalsIgnoreCase("\\"))
			return WINDOWS;
		else
			return LINUX;
	}

	/**
	 * 返回指定文件夹中的指定文件
	 * 
	 * @param dir
	 * @param fileName
	 * @param create
	 * @return
	 */
	public static File getFile(File dir, String fileName, boolean create) {
		File file = new File(dir.getAbsolutePath() + File.separator + fileName);
		try {
			if (create && !file.exists()) {
				file.createNewFile();
			}
		} catch (IOException ioe) {
			logger.error("Could not create file {}." + file.getPath());
			return null;
		}
		return file;
	}

	/**
	 * TODO 没有做错误处理，例如指定路径是一个文件路径 返回指定路径下的目录
	 * 
	 * @param path
	 *            指定路径
	 * @param create
	 *            如果没有则创建
	 * @return
	 */
	public static File getDir(String path, boolean create) {
		File dir = new File(path);
		if (!dir.exists() && create) {
			System.out.println("dir==" + dir);
			dir.mkdirs();
		}
		return dir;
	}

	/***
	 * 创建文件夹下文件
	 * 
	 * @param dirPath
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static File getFileByDirAndFile(String dirPath, String fileName) {
		File file = null;
		try {
			File dirFile = new File(dirPath);
			if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
				boolean creadok = dirFile.mkdirs();
				if (creadok) {
					logger.info("ok:create dir success! ");
				} else {
					logger.error("err:create dir fail!");
				}
			}
			file = new File(dirPath.concat(File.separator).concat(fileName));
			if (!file.exists()) {
				boolean creafok = file.createNewFile();
				if (creafok) {
					logger.info(" ok:create file success! ");
				} else {
					logger.error(" err:create file fail!");
				}
			}
			logger.info(" ok: file exists! ");
		} catch (Exception e) {
			e.printStackTrace();
			return file;
		}
		return file;
	}

	public static BufferedReader getFileContent(File file, String encode) throws IOException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(file), encode);
		BufferedReader read = new BufferedReader(isr);
		return read;
	}

	public static BufferedReader getFileContent(File file) throws IOException {
		FileInputStream fin = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fin));
		return br;
	}

	public static String getBasePath() {
		String filePath = FileUtil.class.getResource("/").getPath();
		return filePath;
	}

}
