package com.example.paymenttypes.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract to communicate with {@link AppContentProvider}
 */
public class AppContract {
  /** The authority for app contents. */
  public static final String CONTENT_AUTHORITY = "com.example.paymenttypes.provider";
  /** Base URI to access provider's content. */
  protected static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
  /** Base content type. */
  protected static final String BASE_CONTENT_TYPE = "vnd.paymenttypes.app.dir/vnd.paymenttypes.";
  /** Base item Content type. */
  protected static final String BASE_CONTENT_ITEM_TYPE = "vnd.paymenttypes.app.item/vnd.paymenttypes.";


  /** Payment columns. */
  interface PaymentColumns {
    /** Payment id. */
    String ID = "id";
    /** Payment name. */
    String NAME = "name";
    /** Payment type id. */
    String PAYMENT_TYPE_ID = "payment_type_id";
    /** Payment thumbnail. */
    String THUMBNAIL = "thumbnail";
  }

  /** Payments contract. */
  public static class Payments implements PaymentColumns, BaseColumns {

    /** Uri Path. */
    static final String PATH = "payments";

    /** Content Uri. */
    public static final Uri CONTENT_URI =
        BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();

    /** Content type. */
    public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + PATH;

    /** Item Content type. */
    public static final String CONTENT_ITEM_TYPE = BASE_CONTENT_ITEM_TYPE + PATH;

    /** Default projection. */
    public static final String[] DEFAULT_PROJECTION = new String[]{
        _ID, ID, NAME, PAYMENT_TYPE_ID, THUMBNAIL };

    /** Default "ORDER BY" clause. */
    public static final String DEFAULT_SORT = _ID + " ASC";

    /** Credit card payment type. */
    public static final String CREDIT_CARD_PAYMENT_TYPE = "credit_card";

    /** Build {@link android.net.Uri} for request all entities. */
    public static Uri buildUri() {
      return CONTENT_URI.buildUpon().build();
    }

    /** Build {@link android.net.Uri} for requested entity. */
    public static Uri buildUri(String id) {
      return CONTENT_URI.buildUpon().appendPath(id).build();
    }

    /** Extract the id from given {@link android.net.Uri} */
    public static final String getId(Uri uri) {
      return uri.getPathSegments().get(1);
    }
  }
}
