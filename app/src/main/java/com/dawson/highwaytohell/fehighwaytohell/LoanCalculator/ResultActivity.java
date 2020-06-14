package com.dawson.highwaytohell.fehighwaytohell.LoanCalculator;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dawson.highwaytohell.fehighwaytohell.R;


/**
 * This window will show all the results of the calculations for the loans and will allow you to send it as an email
 */
public class ResultActivity extends AppCompatActivity{

    //instantiate all variables
    double minMonthValue;
    double minMonthBValue;
    double monthlyValue;
    double balanceValue;
    double additionalValue;
    double fixedValue;
    double rateValue;

    TextView r2c2;
    TextView r2c3;
    TextView r2c4;

    TextView r3c2;
    TextView r3c3;
    TextView r3c4;

    TextView r4c2;
    TextView r4c3;
    TextView r4c4;

    TextView r5c2;
    TextView r5c3;
    TextView r5c4;

    TextView r6c2;
    TextView r6c3;
    TextView r6c4;

    TextView r7c2;
    TextView r7c3;
    TextView r7c4;

    /**
     * Override of oncreate to get the values from the intent
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_answer);
        Intent intent = getIntent();
        balanceValue = intent.getDoubleExtra("balance", 0.0);
        minMonthBValue = intent.getDoubleExtra("minMonthB", 0.0);
        minMonthValue = intent.getDoubleExtra("minMonth", 0.0);
        monthlyValue = intent.getDoubleExtra("monthly", 0.0);
        additionalValue = intent.getDoubleExtra("additional", 0.0);
        fixedValue = intent.getDoubleExtra("fixed", 0.0);
        rateValue = intent.getDoubleExtra("rate", 0.0);
        setupColumns();
        if(verifyValidParameters()) {
            double[] scenarios = orderArray();
            setUpResults(scenarios);
        }
    }

    /**
     * Instantiate all the variables with the columns from the table
     */
    private void setupColumns(){
        r2c2 = findViewById(R.id.Row2Col2);
        r2c3 = findViewById(R.id.Row2Col3);
        r2c4 = findViewById(R.id.Row2Col4);
        r3c2 = findViewById(R.id.Row3Col2);
        r3c3 = findViewById(R.id.Row3Col3);
        r3c4 = findViewById(R.id.Row3Col4);
        r4c2 = findViewById(R.id.Row4Col2);
        r4c3 = findViewById(R.id.Row4Col3);
        r4c4 = findViewById(R.id.Row4Col4);
        r5c2 = findViewById(R.id.Row5Col2);
        r5c3 = findViewById(R.id.Row5Col3);
        r5c4 = findViewById(R.id.Row5Col4);
        r6c2 = findViewById(R.id.Row6Col2);
        r6c3 = findViewById(R.id.Row6Col3);
        r6c4 = findViewById(R.id.Row6Col4);
        r7c2 = findViewById(R.id.Row7Col2);
        r7c3 = findViewById(R.id.Row7Col3);
        r7c4 = findViewById(R.id.Row7Col4);
    }


    /**
     * Fill in all the columns with the results from the calculations
     * @param columns array containing the values from the calculations
     */
    private void setUpResults(double[] columns){
        Log.wtf("Setup", "Setting up results");
        double lowest = getLowestNumber(columns);

        LoanCalculator loan = new LoanCalculator(balanceValue, rateValue);

        addResultsToCol(r2c2, r3c2, r4c2, r5c2, r6c2, r7c2, loan, columns[0], lowest);
        addResultsToCol(r2c3, r3c3, r4c3, r5c3, r6c3, r7c3, loan, columns[1], lowest);
        addResultsToCol(r2c4, r3c4, r4c4, r5c4, r6c4, r7c4, loan, columns[2], lowest);

    }

    /**
     * Adds a result from the calculation to the table
     * @param row2 time to pay off the loan
     * @param row3 balance of the calculation
     * @param row4 interest paid of the calculation
     * @param row5 total paid of the calculation
     * @param row6 amount saved of the calculation
     * @param row7 time saved of the calculation
     * @param loan loan calculator object to do calculations
     * @param payedOff the monthly pay towards the card
     * @param worstPayedOff the lowest monthly pay within the 3 options
     */
    private void addResultsToCol(TextView row2,TextView row3,TextView row4,TextView row5,TextView row6,TextView row7, LoanCalculator loan, double payedOff, double worstPayedOff){
        try {
            row2.setText(loan.yearsAndMonthToPayOff(payedOff));
        }catch(IllegalArgumentException e) {
            row2.setText("-");
        }
        try {
            row3.setText(Double.toString(loan.getBalance()));
        }catch(IllegalArgumentException e) {
            row3.setText("-");
        }
        try {
            row4.setText(Double.toString(loan.getTotalInterest(payedOff)));
        }catch(IllegalArgumentException e) {
            row4.setText("-");
        }
        try {
            row5.setText(Double.toString(loan.getTotalCostOfCredit(payedOff)));
        }catch(IllegalArgumentException e) {
            row5.setText("-");
        }
        try {
            row6.setText(loan.amountSaved(payedOff, worstPayedOff));
        }catch(IllegalArgumentException e) {
            row6.setText("-");
        }
        try {
            row7.setText(loan.timeSaved(payedOff, worstPayedOff));
        }catch(IllegalArgumentException e) {
            row7.setText("-");
        }
    }

    /**
     * Set up the array with all 3 monthly payment
     * @return the array filled with the monthly payments
     */
    private double[] orderArray(){
        double [] returnArray = new double[3];
        double optionA;


        if(minMonthValue > ((1+(monthlyValue/100))*balanceValue - balanceValue)){
            optionA = minMonthValue;
        }else{
            optionA = ((1+(monthlyValue/100))*balanceValue - balanceValue);
        }
        double optionB = minMonthBValue + additionalValue;
        double optionC = fixedValue;
        returnArray[0] = optionA;
        returnArray[1] = optionB;
        returnArray[2] = optionC;
        return returnArray;
    }

    /**
     * Find the lowest value inside of the array
     * @param array the array looked inside of
     * @return the lowest value
     */
    private double getLowestNumber(double[] array){
        double lowest = array[0];
        for(int i =0; i < array.length;i++){
            if(array[i] < lowest){
                lowest = array[i];
            }
        }
        return lowest;
    }

    /**
     * Verify all parameters of the xml have been filledi n
     * @return boolean if yes or no
     */
    private boolean verifyValidParameters() {
        if (balanceValue > 0 &&
                rateValue > 0 &&
                minMonthValue > 0 &&
                monthlyValue > 0 &&
                minMonthBValue > 0 &&
                additionalValue > 0 &&
                fixedValue > 0) {
            Log.d("verify", "true");
            return true;
        } else
            Log.d("verify", "false");
        return false;

    }

    /**
     * Sends an email with all the calculation information.
     * Information taken from
     * https://stackoverflow.com/questions/28546703/how-to-code-using-android-studio-to-send-an-email
     * https://stackoverflow.com/questions/10903754/input-text-dialog-android
     * @param view the view
     */
    public void sendEmail(final View view) {
        Log.i("Email", "Sending email");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.input));

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doLaunchContactPicker(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * Send an email with an email address
     * @param emailAddress the email address
     */
    private void sendEmail(String emailAddress){
        final String message = getResources().getString(R.string.emailHeader) + "\n" +
                "\n" + getResources().getString(R.string.wOptionA) + "\n" + r2c2.getText() +
                "\n" + getResources().getString(R.string.wBalance) + r3c2.getText() +
                "\n" + getResources().getString(R.string.wTotal) + r3c2.getText() +
                "\n" + getResources().getString(R.string.wAmount) + r3c2.getText() +
                "\n" + getResources().getString(R.string.wTime) + r3c2.getText() +
                "\n" + getResources().getString(R.string.wOptionB) + "\n" + r2c3.getText() +
                "\n" + getResources().getString(R.string.wBalance) + r3c3.getText() +
                "\n" + getResources().getString(R.string.wTotal) + r3c3.getText() +
                "\n" + getResources().getString(R.string.wAmount) + r3c3.getText() +
                "\n" + getResources().getString(R.string.wTime) + r3c3.getText() +
                "\n" + getResources().getString(R.string.wOptionC) + "\n" + r2c4.getText() +
                "\n" + getResources().getString(R.string.wBalance) + r3c4.getText() +
                "\n" + getResources().getString(R.string.wTotal) + r3c4.getText() +
                "\n" + getResources().getString(R.string.wAmount) + r3c4.getText() +
                "\n" + getResources().getString(R.string.wTime) + r3c4.getText();

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My Locan Calculator Result");
        emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{emailAddress});
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
            finish();
            Log.i("Email", "Finished sending email...");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ResultActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * https://stackoverflow.com/questions/10117049/get-only-email-address-from-contact-list-android
     * Intent to get result from contacts
     * @param name name of the contact
     */
    public void doLaunchContactPicker(String name) {
        String email = "";
        Cursor cur = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME.toLowerCase() + " like '" + name.toLowerCase() + "'", null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur1 = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (cur1.moveToNext()) {
                    email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    Log.e("Email", email);
                }
            }
            cur.close();
            sendEmail(email);
        }
    }

    /**
     * Show a toast with the full message of option A once clicked on
     * @param view
     */

    public void showOptionAToast(View view) {
        Toast toast = Toast.makeText(this, getResources().getString(R.string.optionA), Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Show a toast with the full message of option B once clicked on
     * @param view
     */

    public void showOptionBToast(View view) {
        Toast toast = Toast.makeText(this, getResources().getString(R.string.optionB), Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Show a toast with the full message of option C once clicked on
     * @param view
     */

    public void showOptionCToast(View view) {
        Toast toast = Toast.makeText(this, getResources().getString(R.string.optionC), Toast.LENGTH_SHORT);
        toast.show();
    }
}
