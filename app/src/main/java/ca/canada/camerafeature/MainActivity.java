package ca.canada.camerafeature;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

// From https://developer.android.com/training/camera/photobasics
public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button btnPhoto;
    private Bitmap imageBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        btnPhoto = findViewById(R.id.btnPhoto);

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View iv) {
//                float angle = iv.getRotation() + 90;
//                float sx = imageView.getDrawable().getBounds().width();
//                float sy = imageView.getDrawable().getBounds().height();
//                float px = imageView.getWidth()/2;
//                float py = imageView.getHeight()/2;
//                float ix = imageView.getWidth();
//                float iy = imageView.getHeight();
//                float sfx;
//                float sfy;
//
//                Matrix matrix = new Matrix();
//                if (ix >= sx) {
//                    sfx = ix/sx;
//                } else {
//                    sfx = sx/ix;
//                }
//
//
//                if (iy >= sy) {
//                    sfy = iy/sy;
//                } else {
//                    sfy = sy/iy;
//                }
//
////                float sfx = Math.min(ix/sx, sx/ix);
////                float sfy = Math.min(iy/sy, sy/iy);
////                float my = iy/sy;
//                float sf = Math.min(ix/sx, iy/sy);
//                System.out.print(
//                        "\npx: " + px + "\npy: " + py +
//                        "\nsx: " + sx + "\nsy: " + sy +
//                        "\nix: " + ix + "\niy: " + iy +
//                        "\nsfx: " + sfx + "\nsfy: " + sfy +
//                        "\nmx: " + mx + "\nmy: " + my);
//
//
//                imageView.setScaleType(ImageView.ScaleType.MATRIX);
////                matrix.setRotate(90f, imageView.getDrawable().getBounds().width()/2, imageView.getDrawable().getBounds().height()/2);
//                matrix.postRotate( 90f, px, py);
//                matrix.postScale(sf, sf);
//
//                imageView.setImageMatrix(matrix);
//            }
//        });
    }



    static final int REQUEST_TAKE_PHOTO = 1;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                // ...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "ca.canada.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
////            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            Bitmap imageBitmap = (Bitmap) extras.get(MediaStore.EXTRA_OUTPUT);
//            imageView.setImageBitmap(imageBitmap);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic();
        }
    }



    String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    // From https://stackoverflow.com/questions/8981845/android-rotate-image-in-imageview-by-an-angle
    public void rotate(View v) {
//        float angle = 90;
//        float px = v.getWidth();
//        float py = v.getHeight();
//
//        Matrix matrix = new Matrix();
//        v.setScaleType(ImageView.ScaleType.MATRIX);
//        matrix.postRotate(angle, px, py);
//        v.setImageMatrix(matrix);

//        float dsjf = v.getDrawa
        v.setPivotX(v.getWidth()/2);
        v.setPivotY(v.getHeight()/2);
        v.setRotation(v.getRotation() + 90);
//        v.setScaleX(v.getParent().);

//        int targetW = imageView.getWidth();
//        int targetH = imageView.getHeight();
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
    }
}
