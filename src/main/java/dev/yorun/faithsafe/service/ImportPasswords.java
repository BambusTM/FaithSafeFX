package dev.yorun.faithsafe.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ImportPasswords {

  public void importPasswords(File zipFile, File destinationDir) throws IOException {
    try (FileInputStream fis = new FileInputStream(zipFile);
         ZipInputStream zipIn = new ZipInputStream(fis)) {

      ZipEntry entry;
      while ((entry = zipIn.getNextEntry()) != null) {
        File file = new File(destinationDir, entry.getName());
        extractFile(zipIn, file);
        zipIn.closeEntry();
      }
    }
  }

  private void extractFile(ZipInputStream zipIn, File file) throws IOException {
    try (FileOutputStream fos = new FileOutputStream(file)) {
      byte[] bytes = new byte[1024];
      int length;
      while ((length = zipIn.read(bytes)) >= 0) {
        fos.write(bytes, 0, length);
      }
    }
  }
}
