package top.wcpe.wcpelib.common.utils.zip;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
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
            compress(sourcePath, zipOutputStream = new ZipOutputStream(Files.newOutputStream(outPath)), sourcePath.toFile().getName(), keepDirStructure);
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

    private static void compress(Path sourcePath, ZipOutputStream zos, String name, boolean keepDirStructure) throws IOException {
        if (Files.isDirectory(sourcePath)) {
            List<Path> subFolder = Files.walk(sourcePath, 1).skip(1)
                    .collect(Collectors.toList());
            if (subFolder.size() == 0 || subFolder.isEmpty()) {
                if (keepDirStructure) {
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                }
            } else {
                for (Path path : subFolder) {
                    if (keepDirStructure) {
                        compress(path, zos, name + "/" + path.getFileName().toString(), true);
                    } else {
                        compress(path, zos, path.getFileName().toString(), false);
                    }
                }
            }
            return;
        }
        zos.putNextEntry(new ZipEntry(name));
        byte[] bytes = Files.readAllBytes(sourcePath);
        zos.write(bytes, 0, bytes.length);
        zos.closeEntry();
    }

}
