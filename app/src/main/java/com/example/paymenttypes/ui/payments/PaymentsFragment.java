package com.example.paymenttypes.ui.payments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.paymenttypes.R;
import com.example.paymenttypes.provider.AppContract;
import com.example.paymenttypes.service.ApiIntentService;
import com.example.paymenttypes.ui.widget.ErrorDialogFragment;


/**
 * This fragment shows a list of payment methods.
 */
public class PaymentsFragment extends Fragment implements
    LoaderManager.LoaderCallbacks<Cursor> {
  /** Error dialog tag. */
  private static final String ERROR_DIALOG_FRAGMENT = "ERROR_DIALOG_FRAGMENT";
  /** List view of payment methods. */
  private ListView mPaymentsView;
  /** List adapter. */
  private PaymentsAdapter mAdapter;
  /** Service result receiver. */
  private BroadcastReceiver mResultReceiver;

  /** Create a instance of the fragment, */
  public static PaymentsFragment newInstance() {
    return new PaymentsFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Keep the fragment alive on configuration changed.
    setRetainInstance(true);

    mResultReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        int result = intent.getExtras().getInt(ApiIntentService.EXTRA_RESULT);
        handleServiceResult(result);
      }
    };
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_payments, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mPaymentsView = (ListView) view.findViewById(android.R.id.list);
    View emptyView = view.findViewById(android.R.id.empty);
    mPaymentsView.setEmptyView(emptyView);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    // Setup adapter.
    if (mAdapter == null) {
      // Use application context to avoid leaks.
      mAdapter = new PaymentsAdapter(getActivity().getApplicationContext());
    }
    mPaymentsView.setAdapter(mAdapter);

    // Initialize loader.
    LoaderManager loaderManager = getActivity().getSupportLoaderManager();
    loaderManager.initLoader(0, null, this);

    // Call service.
    if (savedInstanceState == null) {
      startRequest();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    getActivity().registerReceiver(mResultReceiver,
        new IntentFilter(ApiIntentService.ACTION_SERVICE_FINISHED));
  }

  @Override
  public void onPause() {
    super.onPause();
    getActivity().unregisterReceiver(mResultReceiver);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mPaymentsView.setAdapter(null);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(getActivity(), AppContract.Payments.buildUri(),
        AppContract.Payments.DEFAULT_PROJECTION,
        AppContract.Payments.PAYMENT_TYPE_ID + "=?",
        new String[] { AppContract.Payments.CREDIT_CARD_PAYMENT_TYPE },
        AppContract.Payments.DEFAULT_SORT);
  }

  @Override
  public void onLoadFinished(Loader loader, Cursor data) {
    mAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader loader) {
    mAdapter.swapCursor(null);
  }

  /** Send an intent to request fetch data from service. */
  private void startRequest() {
    Intent intent = new Intent(getActivity(), ApiIntentService.class);
    getActivity().startService(intent);
  }

  /** Handles service results. */
  private void handleServiceResult(int result) {
    switch (result) {
      case ApiIntentService.RESULT_OK:
        break;
      case ApiIntentService.RESULT_NETWORK_FAIL:
        showErrorDialog(R.string.error_connection);
        break;
      case ApiIntentService.RESULT_APP_FAIL:
        showErrorDialog(R.string.error_app);
        break;
      case ApiIntentService.RESULT_SERVICE_FAIL:
        showErrorDialog(R.string.error_service);
        break;
    }
  }

  /**
   * Shows an error dialog.
   * It will remove any other previous error dialog.
   * @param resErrorMessage the resource of error messages.
   */
  private void showErrorDialog(int resErrorMessage) {
    ErrorDialogFragment errorDialog = ErrorDialogFragment.newInstance(
        resErrorMessage);
    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();

    // Remove old fragment.
    Fragment dialog = fragmentManager.findFragmentByTag(
        ERROR_DIALOG_FRAGMENT);
    if (dialog != null) {
      transaction.remove(dialog);
    }

    // Add new fragment.
    transaction.add(errorDialog, ERROR_DIALOG_FRAGMENT);
    transaction.commit();
  }
}
