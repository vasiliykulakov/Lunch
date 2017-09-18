package us.kulakov.lunch.results;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import us.kulakov.lunch.R;
import us.kulakov.lunch.api.places.models.PlaceSearchItemModel;
import us.kulakov.lunch.results.di.ResultsScope;
import us.kulakov.lunch.utils.ImageViewUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Provides bridge between list items and their model
 */
@ResultsScope
public class ResultViewHolder extends ResultsAdapter.ViewHolder<PlaceSearchItemModel> {

    private final ImageViewUtils mImageViewUtils;

    @BindView(R.id.text_item_title)
    protected TextView mTitleTextView;

    @BindView(R.id.text_item_rating)
    protected TextView mRatingTextView;

    @BindView(R.id.image_item)
    protected ImageView mItemImage;

    @Nullable
    private PlaceSearchItemModel mCurrentItem;

    public ResultViewHolder(View itemView, ImageViewUtils imageViewUtils) {
        super(itemView);
        mImageViewUtils = imageViewUtils;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setCurrentItem(@Nullable PlaceSearchItemModel placeSearchItemModel) {
        mCurrentItem = placeSearchItemModel;
        if(mCurrentItem == null) {
            return;
        }

        mImageViewUtils.decorateImage(mCurrentItem.getImageUrl(), mItemImage);
        mTitleTextView.setText(mCurrentItem.getTitle());
        mRatingTextView.setText(String.format(Locale.getDefault(), "%.2f", mCurrentItem.getRating()));
    }

    @Override
    public PlaceSearchItemModel getCurrentItem() {
        return mCurrentItem;
    }
}
