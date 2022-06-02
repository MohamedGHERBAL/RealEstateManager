package com.openclassrooms.realestatemanager.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.databinding.ActivityLoanSimulatorBinding;

import java.text.DecimalFormat;

public class LoanSimulatorActivity extends AppCompatActivity implements View.OnClickListener {

    // For Log.d .i .e
    private static final String TAG = LoanSimulatorActivity.class.getSimpleName();

    // For ViewBinding
    private ActivityLoanSimulatorBinding loanSimulatorBinding;
    private View mView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        // Binding View
        loanSimulatorBinding = ActivityLoanSimulatorBinding.inflate(getLayoutInflater());
        mView = loanSimulatorBinding.getRoot();
        setContentView(mView);

        // Init


        // Config
        configureActionBar();

        // For Calculate Button
        loanSimulatorBinding.calculateBtn.setOnClickListener(this::onClick);
    }

    public void showLoanResult() {
        Log.i(TAG, "showLoanResult");

        double loanAmount = Integer.parseInt(loanSimulatorBinding.amountCalculatorEdit.getText().toString());
        double interestRate = Double.parseDouble(String.valueOf(loanSimulatorBinding.rateCalculatorEdit.getText()));
        double loanDuration = Integer.parseInt(loanSimulatorBinding.durationEdit.getText().toString());

        double monthlyPayment = Utils.calculateMonthlyPayment(interestRate, loanDuration, loanAmount);
        double totalPayment = Utils.calculateTotalPayment(monthlyPayment, loanDuration);

        loanSimulatorBinding.amontPaymentCalculatorTv.setText(R.string.amont_monthly_payment + (new DecimalFormat("##.##").format(monthlyPayment)) + " €");
        loanSimulatorBinding.costCreditTv.setText(R.string.cost_credit + (new DecimalFormat("##.##").format(totalPayment)) + " €");
    }

    public void onClick(View view) {
        Log.d(TAG, "onClick");

        showLoanResult();
    }

    // For ActionBar
    private void configureActionBar() {
        Log.d(TAG, "configureActionBar");

        // Calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // Showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // This event will enable the back function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}