package us.kulakov.lunch.utils;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import us.kulakov.lunch.R;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class ImageViewUtils {
    @Inject
    public ImageViewUtils() {
    }

    public void decorateImage(@NonNull String url, @NonNull ImageView imageView) {
        Picasso.with(imageView.getContext())
                .load(url)
                .error(R.drawable.no_image)
                .into(imageView);
    }
}
