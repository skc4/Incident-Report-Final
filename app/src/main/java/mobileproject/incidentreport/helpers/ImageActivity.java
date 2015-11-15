package mobileproject.incidentreport.helpers;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import mobileproject.incidentreport.R;

/**
 * Created by Bpitt on 11/15/15.
 */
public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        Bitmap bitmap = getIntent().getParcelableExtra("image");

        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);
    }

    public void goBack(View view){
    finish();
    }

}
