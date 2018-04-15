package com.sincerly.fightcontentview.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2018/3/21 0021.
 */

public class FileUtil {
	/**
	 * 文件拷贝
	 *
	 * @param p1 源文件路径
	 * @param p2 目标文件路径
	 * @return
	 */
	public static void copyFileToPath(String p1, String p2) {
		File file2 = new File(p2);
		if (!file2.getParentFile().exists()) {
			file2.getParentFile().mkdirs();
			file2.getParentFile().mkdirs();
		}

		try {
			InputStream inputStream = new FileInputStream(p1);
			OutputStream outputStream = new FileOutputStream(p2);
			byte[] b = new byte[1024];
			int length = 0;
			try {
				while ((length = inputStream.read(b)) > 0) {
					outputStream.write(b, 0, length);
				}
				inputStream.close();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
