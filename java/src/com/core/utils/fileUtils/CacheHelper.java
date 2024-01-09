package com.core.utils.fileUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.text.TextUtils;

import com.android.inputmethod.latin.R;
import com.core.BaseApplication;
;
import com.core.utils.AppLogger;
import com.core.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import kotlin.io.FilesKt;


public class CacheHelper {

    public static final String DIR_AUDIO_DRAFT = "audio_draft";
    private static final String DIR_THUMBS = ".thumbs";
    public static final String DIR_MEDIA_FOLDER = "Media";
    public static final String DIR_GIFFY_FOLDER = "Animated Gifs/.Sent";
    public static final String DIR_APP_FOLDER = BaseApplication.instance.getString(R.string.app_name);
    public static final String DIR_ANDROID_FOLDER = "Android/media";
    public static final String PAINTED_IMAGE_EXTENSION = "jpg";
    public static String TAG = "CacheHelper";

    public static final String IMAGES = "Images";
    public static final String VIDEOS = "Videos";
    public static final String IMAGE_FOLDER_NAME = DIR_APP_FOLDER + " " + IMAGES;
    public static final String VIDEO_FOLDER_NAME = DIR_APP_FOLDER + " " + VIDEOS;
    public static final String AUDIO_FOLDER_NAME = DIR_APP_FOLDER + " Audios";
    private static final String DOCUMENT_FOLDER_NAME = DIR_APP_FOLDER + " Documents";


    public static String getScrambleRootFolder() {
        return Environment.getExternalStorageDirectory() + "/" + DIR_APP_FOLDER;
    }

    private static String getAndroidRootFolder() {
        return Environment.getExternalStorageDirectory() + "/" + DIR_ANDROID_FOLDER;
    }

    public static String getScrambleMediaFolder() {
        String directoryPath;
        if (Utils.INSTANCE.getAndroidQAndAbove()) {
            directoryPath = getAndroidRootFolder() + "/" + BaseApplication.instance.getPackageName() + "/" + DIR_MEDIA_FOLDER;
            createMediaDirectoryIfNotExists(directoryPath);
        } else {
            directoryPath = getScrambleRootFolder() + "/" + DIR_MEDIA_FOLDER;
        }
        return directoryPath;
    }

    public static String getAppPrivateDir() {
        return getAndroidRootFolder() + "/" + BaseApplication.instance.getPackageName() + "/" + DIR_MEDIA_FOLDER;
    }

    public static String getAudioDir(Boolean draftingAudi) {
        String audioFilePath;
        if (draftingAudi) {
            audioFilePath = getScrambleMediaFolder() + "/" + AUDIO_FOLDER_NAME + "/" + DIR_AUDIO_DRAFT;
        } else {
            audioFilePath = getScrambleMediaFolder() + "/" + AUDIO_FOLDER_NAME;
        }
        File audioDir = new File(audioFilePath);
        if (!audioDir.exists()) {
            audioDir.mkdirs();
        }
        return audioFilePath;
    }

    public static String getImagesDir() {
        File imagesDir = new File(getScrambleMediaFolder() + "/" + IMAGE_FOLDER_NAME);
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
        }
        return getScrambleMediaFolder() + "/" + IMAGE_FOLDER_NAME;
    }

    public static String getImageThumbsDir() {
        File imagesThumbDir = new File(getImagesDir() + "/" + DIR_THUMBS);
        if (!imagesThumbDir.exists()) {
            imagesThumbDir.mkdirs();
        }
        createNoMediaFile(imagesThumbDir);
        return getImagesDir() + "/" + DIR_THUMBS;
    }

    public static String getDocumentsDir() {
        return getScrambleMediaFolder() + "/" + DOCUMENT_FOLDER_NAME;
    }

    public static void createNoMediaFile(File directory) {
        File file = new File(directory.getAbsolutePath(), ".nomedia");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                AppLogger.INSTANCE.e(TAG, "Exception " + e.getMessage());
            }
        }
    }

    private static void createMediaDirectoryIfNotExists(final String mediaDirectoryPath) {
        File mediaDirectory = new File(mediaDirectoryPath);
        try {
            if (!mediaDirectory.exists()) {
                mediaDirectory.mkdirs();
                if (!mediaDirectory.exists()) {
                    File file = new File(mediaDirectory, "TempMedia");
                    if (!file.exists())
                        file.createNewFile();

                    FileOutputStream out = new FileOutputStream(file, false);
                    out.flush();
                    out.close();

                    file.deleteOnExit();
                }
                AppLogger.INSTANCE.e(TAG, "TempMedia  File Saved Successfully");
            }
        } catch (IOException e) {
            AppLogger.INSTANCE.e(TAG, "Failed to save TempMedia File");
        }
    }

    public static File saveThumbImage(Context context, Bitmap myBitmap, String filename) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        try {
            File f = generateNewThumbFile(filename);
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
            scanFile(context, f);
            return f;
        } catch (IOException e1) {
            return null;
        }
    }

    public static File generateNewThumbFile(String filename) {
        return new File(getImageThumbsDir(), filename);
    }

    public static void scanFile(Context context, File f) {
        MediaScannerConnection.scanFile(context,
                new String[]{f.getPath()},
                new String[]{"image/jpeg"}, null);
    }

    public static File getAudioFilePath(Context context, String filename, Boolean draftingAudio) {

        File audioFolder;
        if (context == null || filename == null) {
            return null;
        }
        audioFolder = new File(getAudioDir(draftingAudio));
        if (!audioFolder.exists())
            audioFolder.mkdirs();
        File file = new File(audioFolder, filename);
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                AppLogger.INSTANCE.e(TAG, "Exception " + e.getMessage());
            }
        AppLogger.INSTANCE.d(TAG, "file url ::: " + file.getAbsolutePath());
        return file;
    }


    public static String isAppPrivateDirectoryCreated() {
        File appPrivateDir = new File(getAndroidRootFolder());
        if (!appPrivateDir.exists()) {
            if (appPrivateDir.mkdirs())
                return appPrivateDir.getAbsolutePath();
            else
                return "";
        }
        return appPrivateDir.getAbsolutePath();
    }

    public static String getPackageDirectory() {
        String comeraPackageDir = isAppPrivateDirectoryCreated();
        if (!TextUtils.isEmpty(comeraPackageDir)) {
            File file = new File(comeraPackageDir + "/" + BaseApplication.instance.getPackageName());
            if (!file.exists())
                file.mkdirs();
            return file.getAbsolutePath();
        }
        return null;
    }

    /**
     * Check whether path contains sent folder or not
     *
     * @param path
     * @return
     */
    public static Boolean isSentFolder(String path) {
        return path.contains("/sent/");
    }

    public static String getGiffyFolder() {
        if (Utils.INSTANCE.getAndroidQAndAbove()) {
            return "/" + DIR_ANDROID_FOLDER + "/" + BaseApplication.instance.getPackageName() +
                    "/" + DIR_MEDIA_FOLDER + "/" + DIR_GIFFY_FOLDER;
        } else {
            return FileUtils.APP_GIF_PATH;
        }
    }

    public static String getBackgroundPath() {
        if (Utils.INSTANCE.getAndroidQAndAbove()) {
            return "/" + DIR_ANDROID_FOLDER + "/" + BaseApplication.instance.getPackageName() +
                    "/" + DIR_MEDIA_FOLDER + "/Chat Background/";
        } else {
            return FileUtils.APP_CHAT_BACKGROUND_PATH;
        }
    }

    /**
     * When app storage is cleared from settings then all the data is deleted but in some newer devices
     * i.e. Android 13 the app package folder is not deleted. When we try to create any file in it then
     * it fails. So, on launch of the app after that we check if that folder already exits then delete it.
     */
    public static void deletePackageFolderIfAlreadyExists() {
        if (Utils.INSTANCE.getAndroidQAndAbove()) {
            try {
                String directoryPath = getAndroidRootFolder() + "/" + BaseApplication.instance.getPackageName();
                File parentFile = new File(directoryPath);
                if (parentFile.exists()) {
                    boolean isDeleted = FilesKt.deleteRecursively(parentFile);
                    AppLogger.INSTANCE.d(TAG, "packageFolder: isDeleted= " + isDeleted);
                }
            } catch (Exception ignore) {
            }
        }
    }

}
