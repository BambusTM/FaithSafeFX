package dev.yorun.faithsafe.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ExportPasswords {
  public void exportPasswords(File userJsonFile, File passwordJsonFile, File destinationFile) throws IOException {
      try (FileOutputStream fos = new FileOutputStream(destinationFile);
           ZipOutputStream zipOut = new ZipOutputStream(fos)) {

        addToZipFile(passwordJsonFile, zipOut);
        addToZipFile(userJsonFile, zipOut);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }


  private void addToZipFile(File file, ZipOutputStream zipOut) throws IOException {
    try (FileInputStream fis = new FileInputStream(file)) {
      ZipEntry zipEntry = new ZipEntry(file.getName());
      zipOut.putNextEntry(zipEntry);

      byte[] bytes = new byte[1024];
      int length;
      while ((length = fis.read(bytes)) >= 0) {
        zipOut.write(bytes, 0, length);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
