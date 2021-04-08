package com.app.fijirentalcars.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.exifinterface.media.ExifInterface;

import com.app.fijirentalcars.PhotoActivity;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.listners.UploadListner;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadService extends Service {

    private NotificationManager mNM;
    private int NOTIFICATION = 100;
    private String CHANNEL_ID = "102";
    APIService apiService;
    NotificationCompat.Builder builder;
    UploadListner listner;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();

    public void toastMsg() {
        Toast.makeText(this, "Yes calling method", Toast.LENGTH_SHORT).show();
        builder.setContentTitle("Select image");
        builder.setContentText("uploading image");

        mNM.notify(NOTIFICATION, builder.build());
    }

    public void setListner(UploadListner listner) {
        this.listner = listner;
    }

    public class LocalBinder extends Binder {
        public UploadService getService() {
            return UploadService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mNM.cancel(NOTIFICATION);
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        apiService = Config.getClient().create(APIService.class);
        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    private void showNotification() {

        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            mNM.createNotificationChannel(channel);
        }


        Intent intent = new Intent(this, PhotoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_action_ac)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

    }


    public void upLoadImage(File file, String itemId, String id, boolean isGallary) {

        FijiRentalUtils.Logger("TAGS", "fiels " + file.length() + " path " + file.getAbsolutePath());


//        try {
//            Bitmap bitmap = BitmapFactory.decodeFile (file.getAbsolutePath());
//            if(isGallary){
//                File newFile=new File(getExternalCacheDir(),
//                        file.getName());
//                file=newFile;
//            }
//            bitmap.compress (Bitmap.CompressFormat.JPEG, 5, new FileOutputStream(file));
//        }
//        catch (Throwable t) {
//            FijiRentalUtils.Logger("ERROR", "Error compressing file." + t.toString ());
//            t.printStackTrace ();
//        }

        if (isGallary) {
            File newFile = new File(getExternalCacheDir(),
                    file.getName());
            file = new File(compressImage(file.getAbsolutePath(), newFile));
        } else {
            file = new File(compressImage(file.getAbsolutePath(), file));
        }


        FijiRentalUtils.Logger("TAGS", "fiels 2 " + file.getAbsolutePath() + " size " + (file.length() / 1024) + " par " + ("item_image_" + id));

        if ((file.length() / 1024) > 50) {
            Toast.makeText(this, FijiRentalUtils.largeImagefile, Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody body;
        MultipartBody.Builder reqBuilder = new MultipartBody.Builder();
        reqBuilder.setType(MultipartBody.FORM);
        reqBuilder.addFormDataPart("item_id", itemId);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), new File(file.getAbsolutePath()));
        reqBuilder.addFormDataPart("item_image[" + id + "]", file.getName(), requestFile);

        body = reqBuilder
                .build();

        builder.setContentTitle("Car Photo");
        builder.setContentText("Image Uploading");
        mNM.notify(NOTIFICATION, builder.build());

        apiService.uploadImage(FijiRentalUtils.getAccessToken(this), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;

                    jsonobject = new JSONObject(jstr);
                    if (jsonobject.optString("code").matches("200")) {

                        builder.setContentTitle("Car Photo");
                        builder.setContentText("Image Uploading Successful");
                        mNM.notify(NOTIFICATION, builder.build());
                    }


                } catch (Exception e) {

                    e.printStackTrace();
                    builder.setContentTitle("Car Photo");
                    builder.setContentText("Image Uploading failed");
                    mNM.notify(NOTIFICATION, builder.build());
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, UploadService.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listner != null) {
                    listner.onUploadFailed(id);
                }
                FijiRentalUtils.Logger("TAGS", "body te" + t.getMessage());
                builder.setContentTitle("Car Photo");
                builder.setContentText("Image Uploading Failed");
                mNM.notify(NOTIFICATION, builder.build());
            }
        });

    }

    public String compressImage(String imageUri, File outputFile) {

        String filePath = imageUri;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
//        float maxHeight = 1280.0f;
//        float maxWidth = 720.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = outputFile.getAbsolutePath();
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


}
