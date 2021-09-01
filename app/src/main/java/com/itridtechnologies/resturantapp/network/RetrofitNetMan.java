package com.itridtechnologies.resturantapp.network;


import com.google.gson.JsonObject;
import com.itridtechnologies.resturantapp.models.ActionOrder.ActionResponse;
import com.itridtechnologies.resturantapp.models.BussinessHours.BusinessHours;
import com.itridtechnologies.resturantapp.models.CancelOrder.CancelResponse;
import com.itridtechnologies.resturantapp.models.DelayOrder.DelayResponse;
import com.itridtechnologies.resturantapp.models.Feedback.FeedbackResponse;
import com.itridtechnologies.resturantapp.models.FeedbackReviewGiven.FeedbackReviewGivenResponse;
import com.itridtechnologies.resturantapp.models.HistoryOrderDetails.HistOrderDetailResponse;
import com.itridtechnologies.resturantapp.models.MenuAddOns.MenuAddOnResponse;
import com.itridtechnologies.resturantapp.models.MenuCategories.MenuCategoriesResponse;
import com.itridtechnologies.resturantapp.models.MenuCats.MenuCatsResponse;
import com.itridtechnologies.resturantapp.models.MenuItemAvailable.MenuItemAvailableResponse;
import com.itridtechnologies.resturantapp.models.OrderDelivered.SetToDelivered;
import com.itridtechnologies.resturantapp.models.OrderSubItems.SubItems;
import com.itridtechnologies.resturantapp.models.Pagination.PaginationResponse;
import com.itridtechnologies.resturantapp.models.SetToReady.SetToReadyResponse;
import com.itridtechnologies.resturantapp.models.Setting.SettingResponse;
import com.itridtechnologies.resturantapp.models.UpdateSetting.UpdateSettingResponse;
import com.itridtechnologies.resturantapp.models.feedbacktags.FeedbackTagsResponse;
import com.itridtechnologies.resturantapp.models.historyNew.NewHistoryWithTotals;
import com.itridtechnologies.resturantapp.models.login.LoginResponse;
import com.itridtechnologies.resturantapp.models.newOrder.NewOrderResponse;
import com.itridtechnologies.resturantapp.models.orderFeesSetting.FeeUpdateResponse;
import com.itridtechnologies.resturantapp.models.receiptOrder.RecieptMainObject;
import com.itridtechnologies.resturantapp.utils.Constants;
import com.itridtechnologies.resturantapp.utils.StringConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public class RetrofitNetMan {


    private static RestApi sRestApi = null;//function to get one time service

    public static RestApi getRestApiService() {
        if (sRestApi == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.readTimeout(15, TimeUnit.SECONDS);
            httpClient.connectTimeout(15, TimeUnit.SECONDS);
            httpClient.addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY));

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(StringConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            sRestApi = retrofit.create(RestApi.class);
        }
        return sRestApi;
    }//end get

    public interface RestApi {

        //Api of login
        @POST("business/login")
        Call<LoginResponse> login(
                @Body JsonObject object
        );

        //Api to get Customer Details
        @GET("business/order/new/{id}")
        Call<NewOrderResponse> getOrders(
                @Header("x-access-token") String token,
                @Path("id") String id
        );

        ////Api to get Menu Categories
        @GET("business/menu")
        Call<MenuCatsResponse> getCats(@Header("x-access-token") String token);


        ////Api to get Menu Addons
        @GET("business/menu/addon/{id}")
        Call<MenuAddOnResponse> getAddons(
                @Header("x-access-token") String token,
                @Path("id") String id
        );

        //Api to get History Details
        @GET("business/order/history?")
        Call<NewHistoryWithTotals> getFullHistory(
                @Header("x-access-token") String token,
                @Query("page") int page
        );

        //
        //Api to get History Details
        //history?page=1&start=2021-03-14&end=2021-05-14
        @GET("business/order/history?")
        Call<NewHistoryWithTotals> getHistory(
                @Header("x-access-token") String token,
                @Query("start") String Start,
                @Query("end") String End,
                @Query("page") int page
        );


        //Api to get Order Details
        //by id
        @GET("business/order/items/{id}")
        Call<SubItems> getOrderDetail(
                @Header("x-access-token") String token,
                @Path("id") String id
        );

        ////Post Api to Action order
        @POST("business/order/action")
        Call<ActionResponse> actionOfOrder
        (
                @Header("x-access-token") String token,
                @Body JsonObject body
        );

        ////Api to cancel order
        @Headers({"Content-Type: application/json"})
        @POST("business/order/status/cancel")
        Call<CancelResponse> cancelOrder
        (
                @Header("x-access-token") String token,
                @Body JsonObject body
        );

        ////Api to cancel order
        @Headers({"Content-Type: application/json"})
        @PUT("business/order/delay/{id}")
        Call<DelayResponse> delayOrder
        (
                @Header("x-access-token") String token,
                @Body JsonObject body,
                @Path("id") String id
        );


        ////Api of Setting
        @GET("business/preferences")
        Call<SettingResponse> getSettingDetails(@Header("x-access-token") String token);

        //Put API for Setting Switches
        @PUT("business/status-update")
        Call<UpdateSettingResponse> setSetting(
                @Header("x-access-token") String token,
                @Body JsonObject body
        );

        //Put API for Setting Delivery Fees and orders
        @PUT("business/delivery-settings")
        Call<FeeUpdateResponse> setFeeAndOrders(
                @Header("x-access-token") String token,
                @Body JsonObject body
        );


        ///Api of Menu Catrgories
        @GET("business/menu")
        Call<MenuCategoriesResponse> getMenuCateogries(@Header("x-access-token") String token);

        ///Api of Menu Add On Availiblity
        @PUT("business/menu/availability/{id}")
        Call<MenuItemAvailableResponse> itemAvailable
        (
                @Header("x-access-token") String token,
                @Path("id") String id,
                @Body JsonObject body
        );


        ////History Order Details
        @GET("business/order/items/{id}")
        Call<HistOrderDetailResponse> getHistOrderDetails(
                @Header("x-access-token") String token,
                @Path("id") String id
        );


        ////New Order with state pending
        @GET("business/orders?state=pending&page=1")
        Call<PaginationResponse> getPaginationPendingOrders(
                @Header("x-access-token") String token
        );


        ////New Order with state ready
        @GET("business/orders?state=Preparing&")
        Call<PaginationResponse> getPaginationReadyOrders(
                @Header("x-access-token") String token,
                @Query("page") int page
        );

        ////New Order with state preparing
        @GET("business/orders?state=ready&")
        Call<PaginationResponse> getPaginationPreparingOrders(
                @Header("x-access-token") String token,
                @Query("page") int page
        );

        ///Api of reciept
        @GET("business/order/reciept/{id}")
        Call<RecieptMainObject> getCustomerReciept(
                @Header("x-access-token") String token,
                @Path("id") String id);

        //Set order to ready
        @POST("business/order/state/{orderId}")
        Call<SetToReadyResponse> setToReady(
                @Header("x-access-token") String token,
                @Path("orderId") String orderId);

        ///Set order to history
        @PUT("business/order/complete/{orderId}")
        Call<SetToDelivered> setToDelivered(
                @Header("x-access-token") String token,
                @Path("orderId") String orderId
        );

        ///Bussiness Hours
        @GET("business/hours")
        Call<BusinessHours> getHours(
                @Header("x-access-token") String token
        );

        //Feedback
        @POST("business/review")
        Call<FeedbackResponse> postFeedback(
                @Header("x-access-token") String token,
                @Body JsonObject body
        );

        //Feedback Tags
        @GET("ratingtagsvalues?for_type=1&tag_type=0&by_type=0")
        Call<FeedbackTagsResponse> getTags();

        //Feedback given or not willbe check on history detailed screen
        //If true review not shown else shown
        @GET("business/check-review/{id}")
        Call<FeedbackReviewGivenResponse> getFeedbackReviewGiven(
                @Header("x-access-token") String token,
                @Path("id") String orderId
        );

    }//interface
}