package com.kotak.payzee.vendorapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.kotak.payzee.vendorapp.dummy.DummyContent;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.logging.Handler;
import android.widget.ProgressBar;

/**
 * A fragment representing a single Home detail screen.
 * This fragment is either contained in a {@link HomeListActivity}
 * in two-pane mode (on tablets) or a {@link HomeDetailActivity}
 * on handsets.
 */
public class HomeDetailFragment extends Fragment implements View.OnClickListener{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    String String_url = null;
    int Random_ID = 0;
    String BillAmount;
    StringBuilder content = new StringBuilder();
    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;
    private ProgressBar progress;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    public void NewBill(View view)
    {

        EditText qrInput = (EditText) view.findViewById(R.id.amount);
        String qrInputText = qrInput.getText().toString();
        //Log.v(LOG_TAG, qrInputText);

        //Find screen size
        WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3/4;

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrInputText,
                null,
                "PayceApp",
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        Log.v("Waheed","Dimension " + smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            ImageView myImage = (ImageView) view.findViewById(R.id.qrcode);
            myImage.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.home_detail)).setText(mItem.content);
        }

        progress = (ProgressBar) rootView.findViewById(R.id.progressBar1);

        Button scanButton;
        scanButton = (Button) rootView.findViewById(R.id.btnBill);
        scanButton.setOnClickListener(this);



        return rootView;
    }

    public void ChangeImage(View view)
    {
        ImageView myImage = (ImageView) getView().findViewById(R.id.qrcode);
        myImage.setImageResource(R.drawable.tick);
    }


    public void onClick(final View v) { //check for what button is pressed

        switch (v.getId()) {
            case R.id.btnBill: {
                Log.v("Waheed", "Button Scan Clicked");
                //NewBill(v);
                EditText qrInput = (EditText) getView().findViewById(R.id.amount);
                try {
                    Log.v("Waheed", qrInput.getText().toString());
                }
                catch(Exception e)
                {
                    Log.v("Waheed","QR CODE ERror");
                }

                String qrInputText = qrInput.getText().toString();

                String vendor_id = "1001";
                Random r = new Random();
                int Low = 10000000;
                int High = 999999999;
                Random_ID = r.nextInt(High-Low) + Low;

                String QRCodeText = vendor_id + "|" + qrInputText + "|" + Random_ID;

                BillAmount = qrInputText;


                WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerDimension = width < height ? width : height;
                smallerDimension = smallerDimension * 3/4;

                Log.v("Waheed","Dimension " + smallerDimension);

                //Encode with a QR Code image
                QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(QRCodeText,
                        null,
                        "PayceApp",
                        BarcodeFormat.QR_CODE.toString(),
                        smallerDimension);
                try {
                    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                    ImageView myImage = (ImageView) getView().findViewById(R.id.qrcode);
                    myImage.setBackgroundColor(Color.rgb(255, 179, 179));
                    myImage.setImageBitmap(bitmap);

                    // Vendor ID
                    // Amount
                    // Random ID
                    String_url = "http://www.puliyal.com/Kotak/NewBill.php?vendor_id=" + vendor_id + "&amount="+qrInputText+"&random_id="+Random_ID;

                    try {
                        new CallInternetPage().execute(String_url);

                        CharSequence toastText = " New Bill Created for the Amount " + qrInputText;
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), toastText, Toast.LENGTH_LONG);
                        toast.show();

                        toastText = "Now, Please ask your customer to scan and make payment ! ";
                        toast = Toast.makeText(getActivity().getApplicationContext(), toastText, Toast.LENGTH_LONG);
                        toast.show();

                        startProgress(v);



                    } catch (Exception e1) {
                        Log.v("Waheed open", e1.toString());
                    }


                } catch (WriterException e) {
                    e.printStackTrace();
                }

                break;
            }
        }
    }


    // Async Task Class
    class CallInternetPage extends AsyncTask<String, String, String> {

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url) {

            InputStream in = null;
            int resCode = -1;
            content = new StringBuilder();
            String line;
            try {

                Log.v("Waheed URL ", String_url);
                URL url = new URL(String_url);

                URLConnection conection = url.openConnection();
                conection.connect();

                int lenghtOfFile = conection.getContentLength();
                Log.v("Waheed Lengt File",lenghtOfFile + "");

                // wrap the urlconnection in a bufferedreader
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conection.getInputStream()));

                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null)
                {
                    content.append(line);
                }
                Log.v("Waheed","Content from the Resut =" + content.toString());
                bufferedReader.close();
                /*
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(),10*1024);
                // Output stream to write file in SD card
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/data.txt");
                output.flush();
                // Close streams
                output.close();
                input.close();
                */

                Log.v("Waheed",f_url.toString());
            }
            catch(Exception ee)
            {
                Log.v("Waheed open eee", ee.toString());

            }

            return content.toString();
        }

        // While Downloading Music File
        protected void onProgressUpdate(String... progress) {
            // Set progress percentage

        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            // Dismiss the dialog after the Music file was downloaded

        }
    }


    public void startProgress(final View view) {


        // do something long
        Runnable runnable = new Runnable() {

            int x = 0;
            public void run() {

                do{
                    x = doFakeWork();

                    if(x == 1)
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // This code will always run on the UI thread, therefore is safe to modify UI elements.
                                ImageView myImage = (ImageView) getView().findViewById(R.id.qrcode);
                                myImage.setImageResource(R.drawable.tick);

                                CharSequence toastText = "Bill amount "+ BillAmount + " paid by the customer";
                                Toast toast = Toast.makeText(getActivity().getApplicationContext(), toastText, Toast.LENGTH_LONG);
                                toast.show();

                            }
                        });

                    }
                    Log.v("Waheed","Running..." + x);

                }while( x <= 0 );
            }
        };
        new Thread(runnable).start();
    }

    // Simulating something timeconsuming
    private int doFakeWork() {
        int returnValue = -1;
        try {

            String_url = "http://www.puliyal.com/Kotak/checkPayment.php?random_id=" +Random_ID;

            new CallInternetPage().execute(String_url);
            Log.v("Waheed", "Calling Check Payment" + content.toString());

           try {
                returnValue = Integer.parseInt(content.toString());
            }
            catch(Exception e)
            {
                Log.v("Waheed", "ERRRORRRRRRRRRRRRRRR PARSING" +e.toString());
                returnValue = 0;
            }
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

}
