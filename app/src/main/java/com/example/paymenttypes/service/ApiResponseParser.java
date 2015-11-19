package com.example.paymenttypes.service;

import android.content.ContentProviderOperation;

import com.example.paymenttypes.provider.AppContract;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Parse the json content from a input stream and create the {@link
 * android.content.ContentProviderOperation} for the results.
 */
public class ApiResponseParser {
  /** Id json attribute. */
  private static final String ID = "id";
  /** Name json attribute. */
  private static final String NAME = "name";
  /** Payment type id json attribute. */
  private static final String PAYMENT_TYPE_ID = "payment_type_id";
  /** Thumbnail json attribute. */
  private static final String THUMBNAIL = "thumbnail";

  /**
   * Parse the json content from input stream and returns the content provider
   * operations for parsed payments.
   * @param in the input stream.
   * @param operations operation list.
   * @throws IOException in case of connection error.
   */
  public void parse(InputStream in,
      ArrayList<ContentProviderOperation> operations)
      throws IOException {
    JsonReader reader = new JsonReader(new InputStreamReader(in));
    reader.beginArray();
    while (reader.hasNext()) {
      // Create a insert.
      ContentProviderOperation.Builder builder = ContentProviderOperation
          .newInsert(AppContract.Payments.buildUri());

      reader.beginObject();
      while (reader.hasNext()) {
        String name = reader.nextName();
        switch (name) {
          case ID:
            builder.withValue(AppContract.Payments.ID, nextStringSafe(reader));
            break;
          case NAME:
            builder.withValue(AppContract.Payments.NAME,
                nextStringSafe(reader));
            break;
          case PAYMENT_TYPE_ID:
            builder.withValue(AppContract.Payments.PAYMENT_TYPE_ID,
                nextStringSafe(reader));
            break;
          case THUMBNAIL:
            builder.withValue(AppContract.Payments.THUMBNAIL,
                nextStringSafe(reader));
            break;
          default:
            reader.skipValue();
            break;
        }
      }
      reader.endObject();

      // Add the insert.
      operations.add(builder.build());
    }
    reader.endArray();
  }

  /**
   * Utility to read an string safely in case of null content.
   * @param reader reader with the content to onParseResponse.
   * @return a string or null case of null content.
   * @throws java.io.IOException in case of error reading from stream.
   */
  private String nextStringSafe(final JsonReader reader) throws
      IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }
    return reader.nextString();
  }
}
