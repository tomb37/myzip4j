package com.jelly.rar;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MyUnZipUtils {

    public static boolean isPasswordCorrect(ZipFile zipFile, String password) {
//        System.out.println("password:"+password);
        zipFile.setPassword(password.toCharArray());
        try {
            List<FileHeader> fileHeaders = zipFile.getFileHeaders();
            FileHeader fileHeader = fileHeaders.get(0);
            InputStream is = zipFile.getInputStream(fileHeader);
            is.read();
            is.close();
        } catch (ZipException e) {
            if (e.getType() == ZipException.Type.WRONG_PASSWORD) {
//                        System.out.println("Wrong password");
            } else {
                //Corrupt file
                e.printStackTrace();
            }
            return false;
        } catch (IOException e) {
//                    System.out.println("Most probably wrong password.");
            e.printStackTrace();
            return false;
        }
//            }
        return true;
    }
}
