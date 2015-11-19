package com.example.paymenttypes.ui.payments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.paymenttypes.R;

/**
 * This activity shows a list of payment methods.
 */
public class PaymentsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_container);

    if (savedInstanceState == null) {
      FragmentTransaction transaction = getSupportFragmentManager()
          .beginTransaction();
      transaction.replace(R.id.container, PaymentsFragment.newInstance());
      transaction.commit();
    }
  }
}
