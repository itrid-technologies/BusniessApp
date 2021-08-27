package com.itridtechnologies.resturantapp.UiViews.Activities;

import static com.itridtechnologies.resturantapp.utils.AppManager.logout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.itridtechnologies.resturantapp.Adapters.AdapterRecieptOrders;
import com.itridtechnologies.resturantapp.Adapters.AdapterRecipetItems;
import com.itridtechnologies.resturantapp.Adapters.AdapterReciptOrderAddons;
import com.itridtechnologies.resturantapp.Adapters.AdapterTotal;
import com.itridtechnologies.resturantapp.R;
import com.itridtechnologies.resturantapp.model.TotalModel;
import com.itridtechnologies.resturantapp.models.RecieptOrder.AddonItemsItem;
import com.itridtechnologies.resturantapp.models.RecieptOrder.Data;
import com.itridtechnologies.resturantapp.models.RecieptOrder.OrderAddonsItem;
import com.itridtechnologies.resturantapp.models.RecieptOrder.OrderItemsItem;
import com.itridtechnologies.resturantapp.models.RecieptOrder.RecieptResponse;
import com.itridtechnologies.resturantapp.models.receiptOrder.MainOrdersDetails;
import com.itridtechnologies.resturantapp.models.receiptOrder.RecieptMainObject;
import com.itridtechnologies.resturantapp.network.RetrofitNetMan;
import com.itridtechnologies.resturantapp.utils.AppManager;
import com.itridtechnologies.resturantapp.utils.LogoutViaNotification;

import org.jetbrains.annotations.NotNull;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Reciept extends AppCompatActivity {

    ///Variables For UI
    private TextView mNameHeader;
    private TextView mOrderId;
    private TextView mTimeNDate;
    private TextView mService;
    private TextView mDeliveryNotes;
    private TextView mPaidUnpaid;
    private TextView mCustomerNameBtm;
    private TextView mCustomerAddress;
    private TextView mCustomerNumber;
    private TextView mBussinessName;
    private RecyclerView mRCVReciept;
    private FloatingActionButton fab;
    private ProgressBar mPBReciept;
    private LinearLayout mLLReciept;
    private Double mTotalAmount = 00.00;
    private RecyclerView mRCVTotals;
    //Adapter
    private AdapterTotal totalAdapter;

    //Trigger Value for test Receipt
    private String isTest = "1";

    //Token for header
    private final String token = AppManager.getBusinessDetails().getData().getToken();

    //Taking order if from previous activity
    private String orderId;

    ///Lists of Orders
    private List<OrderItemsItem> mOrders = new ArrayList<>();
    //Lists of Order Addons
    private List<OrderAddonsItem> mOrderAddons = new ArrayList<>();
    //List of order Addons Names
    private List<AddonItemsItem> mOrderAddonDetails = new ArrayList<>();

    //Media Player
    private MediaPlayer mediaPlayer;

    private MainOrdersDetails mCustomerDetails;
    private List<com.itridtechnologies.resturantapp.models.receiptOrder.OrderItemsItem> mOrderItemList;
//    private List<List<com.itridtechnologies.resturantapp.models.receiptOrder.OrderAddonsItem>> mOrderAddonList;
//    private List<List<com.itridtechnologies.resturantapp.models.receiptOrder.AddonItemsItem>> mOrderAddonItemsList;


    ///ScreenShot
    private static final int REQUEST_EXTERNAL_STORAGe = 1;
    private static String[] permissionstorage = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciept);

        //verifying permissions
        verifystoragepermissions(Reciept.this);

        //Make status bar transparent
        AppManager.hideStatusBar(Reciept.this);

        //Getting Value
        isTest = getIntent().getStringExtra("isTest");


        //Setting Variables
        setVariables();

        //Setting Totals
        totalFun();

        //Getting order Id
        orderId = String.valueOf(getIntent().getStringExtra("orderId"));


        //Floating action Button
        printNow();


        ///Sound on screenshot
        // adding beep sound
        mediaPlayer = MediaPlayer.create(this, R.raw.notification);

        LogoutViaNotification.logoutOnType();
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (isTest.equals("0"))
        {
            //In case not a test
            //Hit API
            getData(orderId);
        }
        else {
            ///Showing and printing Receipt
            //Changing Visibility
            mPBReciept.setVisibility(View.GONE);
            mLLReciept.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
        }

    }

    //Verify Permissions
    // verifying if storage permission is given or not
    public static void verifystoragepermissions(Activity activity) {

        int permissions = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // If storage permission is not given then request for External Storage Permission
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissionstorage, REQUEST_EXTERNAL_STORAGe);
            Log.e("TAG", "verifystoragepermissions:Permission not given");
        }
    }

    //Print function
    public void printNow() {
        fab.setOnClickListener(v -> {
            //Snackbar
            AppManager.SnackBar(Reciept.this, "Saved in Gallery");
            //Print Reciept
            doPhotoPrint(AppManager.screenshot(mLLReciept));
            mediaPlayer.start();
        });
    }

//    private void createPDFFile(String path) {
//        if (new File(path).exists())
//            new File(path).delete();
//
//        try {
//            Document document = new Document();
//            //Save Order
//            PdfWriter.getInstance(document, new FileOutputStream(path));
//            //Open to write
//            document.open();
//            //Page Setting
//            document.setPageSize(PageSize.A4);
//            document.addCreationDate();
//            document.addAuthor("Daniyal Qamar");
//            document.addCreator(AppManager.getBusinessDetails().getData().getResults().getBusinessName());
//
//            //Title of document
//            addNewItem(document,"Order Reciept", Element.ALIGN_CENTER);
//
//            printPDF();
//
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//    }

//    private void printPDF() {
//        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
//        try {
//            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(Reciept.this,Common.getAppPath(Reciept.this) + "testPdf.pdf");
//            printManager.print("Order",printDocumentAdapter,new PrintAttributes.Builder().build());
//        }catch (Exception ex)
//        {
//            Log.e("TAG", "printPDF: Error " +ex.getMessage());
//        }
//    }
//
//    private void addNewItem(Document document, String order_reciept, int alignCenter) throws DocumentException{
//        Chunk chunk = new Chunk(order_reciept);
//        Paragraph paragraph = new Paragraph(chunk);
//        paragraph.setAlignment(alignCenter);
//        document.add(paragraph);
//    }


//    //Take Screenshot
//    protected static File screenshot(View view, String filename) {
//        Date date = new Date();
//
//        // Here we are initialising the format of our image name
//        CharSequence format = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", date);
//        try {
//            // Initialising the directory of storage
//            String dirpath = Environment.getExternalStorageDirectory() + "";
//            File file = new File(dirpath);
//            if (!file.exists()) {
//                boolean mkdir = file.mkdir();
//            }
//
//            // File name
//            String path = dirpath + "/" + filename + "-" + format + ".jpeg";
//            Log.e("TAG", "screenshot: " + path );
//            view.setDrawingCacheEnabled(true);
//            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
//            view.setDrawingCacheEnabled(false);
//            File imageurl = new File(path);
//            FileOutputStream outputStream = new FileOutputStream(imageurl);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
//            outputStream.flush();
//            outputStream.close();
//            return imageurl;
//
//        } catch (IOException io) {
//            io.printStackTrace();
//        }
//        return null;
//    }

    private void setVariables() {
        mNameHeader = findViewById(R.id.headerNameCustomer);
        mOrderId = findViewById(R.id.orderIdHeader);
        mTimeNDate = findViewById(R.id.orderDatenTime);
        mService = findViewById(R.id.serviceType);
        mDeliveryNotes = findViewById(R.id.deliveryNotes);
        mPaidUnpaid = findViewById(R.id.paid_unpaid);
        mCustomerNameBtm = findViewById(R.id.customerNameBtm);
        mCustomerAddress = findViewById(R.id.customerAddress);
        mCustomerNumber = findViewById(R.id.customerMobile);
        mBussinessName = findViewById(R.id.businessName);
        mRCVReciept = findViewById(R.id.rcv_reciept);
        fab = findViewById(R.id.fab);
        mPBReciept = findViewById(R.id.pb_reciept);
        mLLReciept = findViewById(R.id.root);
        mRCVTotals = findViewById(R.id.rv_order_totals_reciept);
    }


    public void totalFun() {
        mRCVTotals.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        totalAdapter = new AdapterTotal(dummySubTotals(), Reciept.this);
        mRCVTotals.setAdapter(totalAdapter);
        totalAdapter.notifyDataSetChanged();
    }//end total function


    //////dummy data for recycler
    public List<TotalModel> dummySubTotals() {
        //Adding Dummy Data in list
        List<TotalModel> list = new ArrayList<>();
        list.add(new TotalModel("Subtotal", "200.00"));
        list.add(new TotalModel("Delivery Fee", "50.00"));
        list.add(new TotalModel("Service Fee", "256.00"));

        return list;
    }

    //calling API
    public void getData(String orderId) {

        Log.e("order id", "onCreate: " + orderId);
        Call<RecieptMainObject> call = RetrofitNetMan.getRestApiService().getCustomerReciept(token, orderId);
        call.enqueue(new Callback<RecieptMainObject>() {
            @Override
            public void onResponse(@NotNull Call<RecieptMainObject> call, @NotNull Response<RecieptMainObject> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        //Customer Data
                        mCustomerDetails = response.body().getData().getOrder().get(0);
                        //Order Main Items
                        mOrderItemList = new ArrayList<>(response.body().getData().getOrderItems());

                        //Ui update
                        UpdateUI(
                                mCustomerDetails.getFirstName() + " " +
                                        mCustomerDetails.getLastName(),
                                String.valueOf(mCustomerDetails.getId()),
                                mCustomerDetails.getPickuptime(),
                                String.valueOf(mCustomerDetails.getOrderType()),
                                mCustomerDetails.getBusinessNotes(),
                                String.valueOf(mCustomerDetails.getPaymentStatus()),
                                "Ferozepur Rd, In Front of Shama Metro Station Ichhra \n" +
                                        "Shershah Colony Lahore, Punjab 54610, Pakistan",
                                mCustomerDetails.getPhoneNumber(),
                                "Thank you for ordering from " + mCustomerDetails.getBusinessName(),
                                mTotalAmount
                        );

                        //Adapter calling and giving lists
                        UpdateOrders(mOrderItemList);
                    }
                }
                else if (response.code() == 401)
                {
                    logout();
                }

            }///onResponse

            @Override
            public void onFailure(@NotNull Call<RecieptMainObject> call, @NotNull Throwable t) {

            }
        });
    }

//    private void getOrderAddonsList(List<com.itridtechnologies.resturantapp.models.receiptOrder.OrderItemsItem> orderItems) {
//
//        mOrderAddonList = new ArrayList<>();
//
//        for (com.itridtechnologies.resturantapp.models.receiptOrder.OrderItemsItem o : orderItems) {
//            mOrderAddonList.add(o.getOrderAddons());
//        }
//    }

//    private void getOrderAddonsItemList(List<List<com.itridtechnologies.resturantapp.models.receiptOrder.OrderAddonsItem>> orderAddonItems) {
//
//        mOrderAddonItemsList = new ArrayList<>();
//
//        for (int i = 0; i < orderAddonItems.size(); i++) {
//
//            for (int j = 0; j < orderAddonItems.get(i).size(); j++) {
//                mOrderAddonItemsList.add(orderAddonItems.get(i).get(j).getAddonItems());
//            }
//
//        }//outer For
//    }


    //Updating UI
    public void UpdateUI(
            String name,
            String order,
            String timeAndDate,
            String service,
            String deliveryNotes,
            String paidUnpaid,
            String address,
            String mobile,
            String businessName,
            Double totalAmount
    ) {
        //Checking Order Service Type
        if (service.equals("0")) {
            mService.setText("Delivery");
        } else if (service.equals("1")) {
            mService.setText("Take Away");
        } else {
            mService.setText("Self-Service");
        }

        ///Checking Payment Status
        if (paidUnpaid.equals("0")) {
            mPaidUnpaid.setText("ORDER IS UNPAID");
        } else {
            mPaidUnpaid.setText("ORDER IS PAID");
        }

        mCustomerNameBtm.setText(name);
        mNameHeader.setText(name);
        mOrderId.setText("#" + order);
        mTimeNDate.setText("Placed at " + timeAndDate);
        mDeliveryNotes.setText(deliveryNotes);
        mCustomerAddress.setText(address);
        mCustomerNumber.setText(mobile);
        mBussinessName.setText(businessName);
    }


    public void UpdateOrders(
            List<com.itridtechnologies.resturantapp.models.receiptOrder.OrderItemsItem> orderItemList
    ) {
        //Setting Adapter
        AdapterRecieptOrders adapter;
        mRCVReciept.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new AdapterRecieptOrders(orderItemList, Reciept.this);
        mRCVReciept.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Changing Visibility
        mPBReciept.setVisibility(View.GONE);
        mLLReciept.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }

    private void doPhotoPrint(Bitmap bitmap) {
        PrintHelper photoPrinter = new PrintHelper(Reciept.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        String businessName = AppManager.getBusinessDetails().getData().getResults().getBusinessName();
        photoPrinter.printBitmap(businessName + " order # " + orderId, bitmap);
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogoutViaNotification.onResumeFun();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogoutViaNotification.onPauseFun();
    }

}


//    Data data = response.body().getData();
//////Getting Order Names
////List 1 (Orders Names)
//    mOrders=response.body().getData().getOrderItems().subList(0,response.body().getData().getOrderItems().size());
//            for(int i=0;i<response.body().getData().getOrderItems().size();i++)
//        {
//        mTotalAmount=mTotalAmount+Double.parseDouble(response.body().getData().getOrderItems().get(i).getItemPrice());
//        }
//
//        //Getting orderAddons Lists
//        if(!response.body().getData().getOrderItems().isEmpty()){
//        //List 2 (Order Addon Names)
//        mOrderAddons=response.body().getData().getOrderItems().ge
//
//
//        if(response.body().getData().getOrderItems().get(0).getOrderAddons().size()>=1){
//        //List 3 (Order Addon Items_
//        mOrderAddonDetails=response.body().getData().getOrderItems().get(0).getOrderAddons().get(0).getAddonItems();
//
//
//        }else{
//        AppManager.toast("No Addons Available");
//        }
//        }
//
//        //Passing lists to adapter
//        UpdateOrders(mOrders,mOrderAddons,mOrderAddonDetails);
//
//        UpdateUI(
//        data.getOrder().get(0).getFirstName()+" "+
//        data.getOrder().get(0).getLastName(),
//        String.valueOf(data.getOrder().get(0).getId()),
//        data.getOrder().get(0).getPickuptime(),
//        String.valueOf(data.getOrder().get(0).getOrderType()),
//        data.getOrder().get(0).getBusinessNotes(),
//        String.valueOf(data.getOrder().get(0).getPaymentStatus()),
//        "Ferozepur Rd, In Front of Shama Metro Station Ichhra \n"+
//        "Shershah Colony Lahore, Punjab 54610, Pakistan",
//        data.getOrder().get(0).getPhoneNumber(),
//        "Thank you for ordering from "+data.getOrder().get(0).getBusinessName(),
//        mTotalAmount
//        );
//
//
//        }