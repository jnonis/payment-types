package com.example.paymenttypes.ui.payments;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.paymenttypes.R;
import com.example.paymenttypes.provider.AppContract;
import com.example.paymenttypes.ui.widget.LoadingImageView;
import com.example.paymenttypes.utils.ImageLoaderUtils;

/**
 * Adapter for payment methods.
 */
public class PaymentsAdapter extends CursorAdapter {

  /** Constructor. */
  public PaymentsAdapter(Context context) {
    super(context, null, 0);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.payment_item, parent, false);
    ViewHolder holder = new ViewHolder();
    holder.thumbnail = (LoadingImageView) view.findViewById(
        R.id.payment_item_thumbnail);
    holder.name = (TextView) view.findViewById(R.id.payment_item_name);
    view.setTag(holder);
    return view;
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    String title = cursor.getString(cursor.getColumnIndex(
        AppContract.Payments.NAME));
    String thumbnail = cursor.getString(cursor.getColumnIndex(
        AppContract.Payments.THUMBNAIL));

    // Get holder.
    ViewHolder holder = (ViewHolder) view.getTag();

    // Set name.
    holder.name.setText(title);

    // Set thumbnail.
    if (TextUtils.isEmpty(thumbnail)) {
      holder.thumbnail.getImageView().setImageBitmap(null);
    } else {
      ImageLoaderUtils.displayImage(thumbnail, holder.thumbnail);
    }
  }

  /** View holder. */
  private static class ViewHolder {
    LoadingImageView thumbnail;
    TextView name;
  }
}
