package top.wcpe.wcpelib.common.utils.zip;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩包管理工具
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-09-17 21:39
 */
public class ZipUtil {
    public static void compressToPath(final Path sourcePath, final Path outPath, final boolean keepDirStructure) {
        if (Files.notExists(sourcePath)) {
            return;
        }
        if (Files.notExists(outPath)) {
            Path parent = outPath.getParent();
            try {
                if (Files.notExists(parent)) {
                    Files.createDirectories(parent);
                }
                Files.createFile(outPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ZipOutputStream zipOutputStream = null;
        try {
            compress(sourcePath.toFile(), zipOutputStream = new ZipOutputStream(Files.newOutputStream(outPath)), sourcePath.toFile().getName(), keepDirStructure);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean keepDirStructure) throws IOException {
        byte[] buf = new byte[2048];
        if (sourceFile.isFile()) {
            zos.putNextEntry(new ZipEntry(name));
            FileInputStream in = new FileInputStream(sourceFile);
            int len;
            while ((len = in.read(buf)) != -1)
                zos.write(buf, 0, len);
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                if (keepDirStructure) {
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                }
            } else {
                byte b;
                int i;
                File[] arrayOfFile;
                for (i = (arrayOfFile = listFiles).length, b = 0; b < i; ) {
                    File file = arrayOfFile[b];
                    if (keepDirStructure) {
                        compress(file, zos, name + "/" + file.getName(), true);
                    } else {
                        compress(file, zos, file.getName(), false);
                    }
                    b++;
                }
            }
        }
    }

}
