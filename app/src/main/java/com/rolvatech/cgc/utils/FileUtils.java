package com.rolvatech.cgc.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Base64;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class FileUtils {
    static int count = 0;

    public static boolean isFileExists(String path) {
        return (new File(path)).exists();
    }

    public static void writeFile(byte[] binary, String SdcardPath, String fileName) {
        BufferedOutputStream bufferedOutputStream = null;
        try {
            File themeFile = new File(SdcardPath);
            if (!themeFile.exists()) {
                new File(SdcardPath).mkdirs();
            }
            File file = new File(SdcardPath + fileName);
            if (file.exists()) {
                file.delete();
            }
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bufferedOutputStream.write(binary);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedOutputStream != null)
                    bufferedOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getFileNameFromPath(String filePath) {
        String fileName = null;
        try {
            fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static String SaveInputStreamAsFile(Context ctx, String SdcardPath, String source, String fileName) {
        try {
            File myFile = new File(SdcardPath, "Themes.xml");

            myFile.createNewFile();

            FileOutputStream fOut = new FileOutputStream(myFile);

            OutputStreamWriter myOutWriter =

                    new OutputStreamWriter(fOut);

            myOutWriter.append(source);

            myOutWriter.close();

            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    public static int ordinalIndexOf(String str, String s, int n) {
        int pos = str.indexOf(s, 0);
        while (n-- > 0 && pos != -1)
            pos = str.indexOf(s, pos + 1);
        return pos;
    }

    public static String getFileNameForProducts(String filePath) {
        int index = ordinalIndexOf(filePath, "/", 2);
        return ((filePath.substring(index + 1, filePath.length())).replace("/", "_"));
    }

    public static void inputStream2File(InputStream inputStream, String fileName, String SdcardPath) {
        try {
            File themeFile = new File(SdcardPath);
            if (!themeFile.exists()) {
                new File(SdcardPath).mkdirs();
            }
            File file = new File(SdcardPath + fileName);
            if (file.exists()) {
                file.delete();
            }

            BufferedInputStream bis = new BufferedInputStream(inputStream);
            FileOutputStream fos = new FileOutputStream(SdcardPath + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            byte byt[] = new byte[1024];
            int noBytes;
            while ((noBytes = bis.read(byt)) != -1)
                bos.write(byt, 0, noBytes);
            bos.flush();
            bos.close();
            fos.close();
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static InputStream getFileFromSDcard(String SDcardpath, String fileName) {
        InputStream is = null;
        try {
            File myFile = new File(SDcardpath, fileName);
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }
//				txtData.setText(aBuffer);
            is = new ByteArrayInputStream(aBuffer.getBytes());
            myReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }


    public static void copyDirectory(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    private static void acquireWifi(Context context, PowerManager.WakeLock mWifiLock) {
        mWifiLock.acquire();
        Log.e("acquire", "DONE");
    }


    public static File getOutputImageFile(String folder) {

        File captureImagesStorageDir = new File(Environment.getExternalStorageDirectory() + File.separator + folder);

        if (!captureImagesStorageDir.exists()) {
            if (!captureImagesStorageDir.mkdirs()) {
                return null;
            }
        }

        String timestamp = System.currentTimeMillis() + "";
        File imageFile = new File(captureImagesStorageDir.getPath() + File.separator
                + "CAPTURE_" + timestamp + ".jpg");
        return imageFile;
    }

    public static File getOutputAudioFile(String folder) {

        File captureImagesStorageDir = new File(Environment.getExternalStorageDirectory() + File.separator + folder);

        if (!captureImagesStorageDir.exists()) {
            if (!captureImagesStorageDir.mkdirs()) {
                return null;
            }
        }

        String timestamp = System.currentTimeMillis() + "";
        File imageFile = new File(captureImagesStorageDir.getPath() + File.separator
                + "CAPTURE_" + timestamp + ".mp3");


        return imageFile;
    }

    public static File getOutputVideoFile(String folder) {

        File captureImagesStorageDir = new File(Environment.getExternalStorageDirectory() + folder);

        if (!captureImagesStorageDir.exists()) {
            if (!captureImagesStorageDir.mkdirs()) {
                return null;
            }
        }

        String timestamp = System.currentTimeMillis() + "";
        File imageFile = new File(captureImagesStorageDir.getPath() + File.separator
                + "CAPTURE_" + timestamp + ".mp4");


        return imageFile;
    }

    public static File getApkFilePath(String folder) {

        File captureImagesStorageDir = new File(Environment.getExternalStorageDirectory() + folder);

        if (!captureImagesStorageDir.exists()) {
            if (!captureImagesStorageDir.mkdirs()) {
                return null;
            }
        }

        String timestamp = System.currentTimeMillis() + "";
        File imageFile = new File(captureImagesStorageDir.getPath() + File.separator
                + Constants.DEFAULT_FOLDER + "_" + timestamp + ".apk");


        return imageFile;
    }


    public static void deleteLogFile(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                long sizeInMB = file.length() / 1048576;
                if (sizeInMB >= 5)
                    file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeIntoLog(String str) {
        try {
            String flePath = Environment.getExternalStorageDirectory().toString() + "/" + "/RequestLog.txt";
            File connectionFolder = new File(Environment.getExternalStorageDirectory().toString() + "/" + Constants.DEFAULT_FOLDER);
            if (!connectionFolder.exists())
                connectionFolder.mkdir();
            deleteLogFile(flePath);
            FileOutputStream fos = new FileOutputStream(flePath, true);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(str.getBytes());

            bos.flush();
            bos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
