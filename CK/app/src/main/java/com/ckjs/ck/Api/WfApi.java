package com.ckjs.ck.Api;

import com.ckjs.ck.Bean.AlipayBean;
import com.ckjs.ck.Bean.BindInfoBean;
import com.ckjs.ck.Bean.BindacceptBean;
import com.ckjs.ck.Bean.CaltopInfoBean;
import com.ckjs.ck.Bean.CanAcceptBean;
import com.ckjs.ck.Bean.CircleReportBean;
import com.ckjs.ck.Bean.CitySearchBean;
import com.ckjs.ck.Bean.CoachCoachInfoBean;
import com.ckjs.ck.Bean.CoachCoachMyInfoBean;
import com.ckjs.ck.Bean.CoachInfoBean;
import com.ckjs.ck.Bean.CoachOrderBean;
import com.ckjs.ck.Bean.CoachRecordBean;
import com.ckjs.ck.Bean.FeedBackBean;
import com.ckjs.ck.Bean.FocusarchivesBean;
import com.ckjs.ck.Bean.GetBodyInfoBean;
import com.ckjs.ck.Bean.GetGymInfoBean;
import com.ckjs.ck.Bean.GetInfoBean;
import com.ckjs.ck.Bean.GetMyActivityBean;
import com.ckjs.ck.Bean.GetMyGoalsBean;
import com.ckjs.ck.Bean.GetMyOrderBean;
import com.ckjs.ck.Bean.GetMyfavoriteBean;
import com.ckjs.ck.Bean.GetMyfocusBean;
import com.ckjs.ck.Bean.GetMyfocusCircleBean;
import com.ckjs.ck.Bean.GetMyfocusDetailBean;
import com.ckjs.ck.Bean.GetNFCInfoBean;
import com.ckjs.ck.Bean.GetNewVersionBean;
import com.ckjs.ck.Bean.GetShopAdressBean;
import com.ckjs.ck.Bean.GetShopInfoBean;
import com.ckjs.ck.Bean.GetSignBean;
import com.ckjs.ck.Bean.GetWrecordBean;
import com.ckjs.ck.Bean.HealWarningsBean;
import com.ckjs.ck.Bean.IntegralBean;
import com.ckjs.ck.Bean.LetterBean;
import com.ckjs.ck.Bean.LetterListBean;
import com.ckjs.ck.Bean.LoginBean;
import com.ckjs.ck.Bean.MSleepInfoBean;
import com.ckjs.ck.Bean.MedicalFileBean;
import com.ckjs.ck.Bean.MemberApplyBean;
import com.ckjs.ck.Bean.MessageBean;
import com.ckjs.ck.Bean.MessageListBean;
import com.ckjs.ck.Bean.MyCoachOrderBean;
import com.ckjs.ck.Bean.OrderInfoShopBean;
import com.ckjs.ck.Bean.OrderQueryBean;
import com.ckjs.ck.Bean.OxyWarningBean;
import com.ckjs.ck.Bean.PreWarningBean;
import com.ckjs.ck.Bean.PriCoachInfoBean;
import com.ckjs.ck.Bean.PrivilegeInfoBean;
import com.ckjs.ck.Bean.PushLetterBean;
import com.ckjs.ck.Bean.RateWarningBean;
import com.ckjs.ck.Bean.ReceiptBean;
import com.ckjs.ck.Bean.RecordBean;
import com.ckjs.ck.Bean.RentInfoBean;
import com.ckjs.ck.Bean.RunlistBean;
import com.ckjs.ck.Bean.RunmonthBean;
import com.ckjs.ck.Bean.SignInBean;
import com.ckjs.ck.Bean.SleepInfoBean;
import com.ckjs.ck.Bean.SubmitOrderBean;
import com.ckjs.ck.Bean.ThirdLoginBean;
import com.ckjs.ck.Bean.UnFocusBean;
import com.ckjs.ck.Bean.UnRentInfoBean;
import com.ckjs.ck.Bean.UnRentStatusBean;
import com.ckjs.ck.Bean.UpdateAdressBean;
import com.ckjs.ck.Bean.UpdateGoalsBean;
import com.ckjs.ck.Bean.UpmedicalfileBean;
import com.ckjs.ck.Bean.UprateBean;
import com.ckjs.ck.Bean.UserInfoBean;
import com.ckjs.ck.Bean.UsereportBean;
import com.ckjs.ck.Bean.UupurlBean;
import com.ckjs.ck.Bean.WSleepInfoBean;
import com.ckjs.ck.Bean.WeiPayBean;
import com.ckjs.ck.Bean.WithdrawalBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by  Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public interface WfApi {
    @FormUrlEncoded
    @POST("user/login")
    Call<LoginBean> login(@Field("tel") String tel, @Field("password") String password);

    @FormUrlEncoded
    @POST("info/updainfo")
    Call<UserInfoBean> userinfo(@Field("token") String token,
                                @Field("user_id") int user_id,
                                @Field("username") String username,
                                @Field("picurl") String picurl,
                                @Field("height") int height,
                                @Field("weight") float weight,
                                @Field("sex") int sex,
                                @Field("birthday") String birthday);

    @FormUrlEncoded
    @POST("info/updainfo")
    Call<UserInfoBean> mineuserinfo(@Field("token") String token,
                                    @Field("user_id") int user_id,
                                    @Field("username") String username,
                                    @Field("picurl") String picurl,
                                    @Field("height") int height,
                                    @Field("weight") float weight,
                                    @Field("sex") int sex,
                                    @Field("birthday") String birthday,
                                    @Field("motto") String motto);

    @FormUrlEncoded
    @POST("info/upload")
    Call<UupurlBean> uppic(@Field("token") String token, @Field("user_id") int user_id, @Field("image") String image);

    @GET("info/getinfo")
    Call<GetInfoBean> getinfo(@Query("token") String token, @Query("user_id") int user_id);

    @GET("info/signin")
    Call<SignInBean> signin(@Query("token") String token, @Query("user_id") int user_id, @Query("gym_id") int gym_id);

    @GET("info/getsign")
    Call<GetSignBean> getsign(@Query("token") String token, @Query("user_id") int user_id);

    @GET("circle/myfocus")
    Call<GetMyfocusBean> myfocus(@Query("token") String token, @Query("user_id") int user_id, @Query("page") int page);

    @GET("circle/focusinfo")
    Call<GetMyfocusDetailBean> myfocusinfo(@Query("token") String token, @Query("user_id") int user_id, @Query("focus_id") int focus_id);

    @FormUrlEncoded
    @POST("circle/unfocus")
    Call<UnFocusBean> unfocus(@Field("token") String token, @Field("user_id") int user_id, @Field("focus_id") int focus_id);

    @GET("circle/focuscircle")
    Call<GetMyfocusCircleBean> focuscircle(@Query("focus_id") int focus_id, @Query("page") int page, @Query("user_id") int user_id, @Query("token") String token);

    @FormUrlEncoded
    @POST("info/updainfo")
    Call<UserInfoBean> userinfoweight(@Field("token") String token,
                                      @Field("user_id") int user_id,
                                      @Field("weight") float weight);

    @GET("info/myfavorite")
    Call<GetMyfavoriteBean> myfavorite(@Query("token") String token, @Query("user_id") int user_id, @Query("page") int page);

    @GET("info/update")
    Call<GetNewVersionBean> update(@Query("os") String os);

    @FormUrlEncoded
    @POST("info/feedb")
    Call<FeedBackBean> feedback(@Field("token") String token,
                                @Field("user_id") int user_id,
                                @Field("content") String content);

    @GET("info/myactivity")
    Call<GetMyActivityBean> myactivity(@Query("token") String token, @Query("user_id") int user_id, @Query("page") int page);

    @GET("info/mygoals")
    Call<GetMyGoalsBean> mygoals(@Query("user_id") int user_id, @Query("token") String token);

    @FormUrlEncoded
    @POST("info/updgoals")
    Call<UpdateGoalsBean> upmygoals(@Field("user_id") int user_id, @Field("token") String token, @Field("fat") String fat,
                                    @Field("step") String step, @Field("sleep") String sleep);

    @GET("shop/shopinfo")
    Call<GetShopInfoBean> shopinfo(@Query("shop_id") String shop_id);

    @FormUrlEncoded
    @POST("shop/updadress")
    Call<UpdateAdressBean> updadress(@Field("user_id") int user_id, @Field("token") String token, @Field("adress") String adress,
                                     @Field("tel") String tel, @Field("name") String name);

    @GET("shop/getadress")
    Call<GetShopAdressBean> getadress(@Query("user_id") String user_id, @Query("token") String token);

    @FormUrlEncoded
    @POST("shop/order")
    Call<SubmitOrderBean> submitorder(@Field("user_id") int user_id, @Field("token") String token, @Field("shop_id") String shop_id, @Field("num") String num,
                                      @Field("shop_type") String shop_type, @Field("adress") String adress, @Field("name") String name, @Field("tel") String tel);

    @GET("shop/myorder")
    Call<GetMyOrderBean> myorder(@Query("user_id") int user_id, @Query("token") String token, @Query("page") int page);

    @FormUrlEncoded
    @POST("user/thirdlogin")
    Call<ThirdLoginBean> thirdlogin(@Field("openid") String openid, @Field("access_token") String access_token, @Field("type") String type, @Field("unionid") String unionid);

    @FormUrlEncoded
    @POST("info/updainfo")
    Call<UserInfoBean> thirduserinfo(@Field("token") String token,
                                     @Field("user_id") int user_id,
                                     @Field("height") int height,
                                     @Field("weight") float weight,
                                     @Field("sex") int sex,
                                     @Field("birthday") String birthday);

    @GET("info/wrecord")
    Call<GetWrecordBean> wrecord(@Query("user_id") int user_id, @Query("token") String token, @Query("type") String type);

    @GET("info/mrecord")
    Call<GetWrecordBean> mrecord(@Query("user_id") int user_id, @Query("token") String token, @Query("type") String type);

    @GET("info/yrecord")
    Call<GetWrecordBean> yrecord(@Query("user_id") int user_id, @Query("token") String token, @Query("type") String type);

    @FormUrlEncoded
    @POST("shop/weipay")
    Call<WeiPayBean> weipay(@Field("token") String token, @Field("user_id") int user_id, @Field("order_id") String order_id, @Field("type") String type);

    @FormUrlEncoded
    @POST("shop/orderquery")
    Call<OrderQueryBean> orderquery(@Field("token") String token, @Field("user_id") int user_id, @Field("order_id") String order_id, @Field("type") String type);

    @GET("info/mygym")
    Call<GetGymInfoBean> mygym(@Query("user_id") int user_id, @Query("token") String token);

    @FormUrlEncoded
    @POST("shop/alipay")
    Call<AlipayBean> alipay(@Field("token") String token, @Field("user_id") int user_id, @Field("order_id") String order_id, @Field("type") String type);

    @FormUrlEncoded
    @POST("shop/aliquery")
    Call<OrderQueryBean> aliquery(@Field("token") String token, @Field("user_id") int user_id, @Field("order_id") String order_id, @Field("type") String type);

    @FormUrlEncoded
    @POST("shop/receipt")
    Call<ReceiptBean> receipt(@Field("user_id") int user_id, @Field("token") String token, @Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("shop/delorder")
    Call<ReceiptBean> delorder(@Field("user_id") int user_id, @Field("token") String token, @Field("order_id") String order_id);

    @GET("info/bodyinfo")
    Call<GetBodyInfoBean> bodyinfo(@Query("user_id") int user_id, @Query("token") String token);

    @GET("circle/report")
    Call<CircleReportBean> report(@Query("user_id") int user_id, @Query("token") String token, @Query("circle_id") int circle_id);

    @GET("circle/letterlist")
    Call<LetterListBean> letterlist(@Query("user_id") int user_id, @Query("token") String token, @Query("page") int page);

    @GET("circle/letter")
    Call<LetterBean> circleletter(@Query("user_id") int user_id, @Query("add_id") String add_id, @Query("token") String token, @Query("page") int page);

    @FormUrlEncoded
    @POST("circle/pushletter")
    Call<PushLetterBean> pushletter(@Field("user_id") int user_id, @Field("token") String token, @Field("add_id") String add_id, @Field("content") String content);

    @GET("info/privilegeinfo")
    Call<PrivilegeInfoBean> privilegeinfo(@Query("privilege_id") int privilege_id);

    @GET("info/coachinfo")
    Call<CoachInfoBean> coachinfo(@Query("coach_id") int coach_id);

    @FormUrlEncoded
    @POST("shop/order")
    Call<SubmitOrderBean> submitorderjsf(@Field("user_id") int user_id, @Field("token") String token, @Field("shop_id") String shop_id, @Field("num") String num,
                                         @Field("shop_type") String shop_type);

    @GET("circle/usereport")
    Call<UsereportBean> usereport(@Query("user_id") int user_id, @Query("add_id") String add_id, @Query("token") String token, @Query("content") String content);

    @FormUrlEncoded
    @POST("circle/delletter")
    Call<ReceiptBean> delletter(@Field("user_id") int user_id, @Field("token") String token, @Field("letter_id") String letter_id);

    @FormUrlEncoded
    @POST("info/record")
    Call<RecordBean> record(@Field("user_id") int user_id, @Field("token") String token, @Field("steps") int steps,
                            @Field("fat") String fat, @Field("mileage") String mileage);

    @GET("info/pricoachinfo")
    Call<PriCoachInfoBean> pricoachinfo(@Query("id") String id);

    @FormUrlEncoded
    @POST("shop/coachorder")
    Call<CoachOrderBean> coachorder(@Field("user_id") String user_id, @Field("token") String token, @Field("id") String id,
                                    @Field("type") String type, @Field("num") String num, @Field("adress") String adress,
                                    @Field("handler") String handler);

    @GET("shop/mycoachorder")
    Call<MyCoachOrderBean> mycoachorder(@Query("user_id") String user_id, @Query("token") String token, @Query("page") String page);

    @GET("coach/coachinfo")
    Call<CoachCoachInfoBean> coachinfo(@Query("user_id") String user_id, @Query("token") String token);

    @GET("coach/myinfo")
    Call<CoachCoachMyInfoBean> coachmyinfo(@Query("user_id") String user_id, @Query("token") String token);

    @GET("coach/canaccept")
    Call<CanAcceptBean> canaccept(@Query("user_id") String user_id, @Query("token") String token);

    @FormUrlEncoded
    @POST("coach/upintro")
    Call<CoachOrderBean> upintro(@Field("user_id") String user_id, @Field("token") String token, @Field("intro") String intro);

    @GET("coach/coachrecord")
    Call<CoachRecordBean> coachrecord(@Query("user_id") String user_id, @Query("token") String token, @Query("page") String page);

    @GET("coach/withdrawal")
    Call<WithdrawalBean> withdrawal(@Query("user_id") String user_id, @Query("token") String token);

    @GET("info/integral")
    Call<IntegralBean> integral(@Query("user_id") String user_id, @Query("token") String token);

    @GET("motions/runmonth")
    Call<RunmonthBean> runmonth(@Query("user_id") String user_id, @Query("token") String token);

    @GET("motions/runlist")
    Call<RunlistBean> runlist(@Query("user_id") String user_id, @Query("token") String token, @Query("month") String month, @Query("page") int page);

    @GET("motions/healwarnings")
    Call<HealWarningsBean> healwarnings(@Query("user_id") String user_id, @Query("token") String token, @Query("page") int page, @Query("type") String type);

    @GET("motions/ratewarning")
    Call<RateWarningBean> ratewarning(@Query("user_id") String user_id, @Query("token") String token, @Query("warning_id") String warning_id);

    @GET("motions/oxywarning")
    Call<OxyWarningBean> oxywarning(@Query("user_id") String user_id, @Query("token") String token, @Query("warning_id") String warning_id);

    @GET("motions/prewarning")
    Call<PreWarningBean> prewarning(@Query("user_id") String user_id, @Query("token") String token, @Query("warning_id") String warning_id);

    @FormUrlEncoded
    @POST("motions/uprate")
    Call<UprateBean> uprate(@Field("user_id") String user_id, @Field("token") String token, @Field("rate") String rate, @Field("maxrate") String maxrate, @Field("minrate") String minrate);

    @GET("info/medicalfile")
    Call<MedicalFileBean> medicalfile(@Query("token") String token, @Query("user_id") String user_id);

    @FormUrlEncoded
    @POST("info/upmedicalfile")
    Call<UpmedicalfileBean> upmedicalfile(@Field("token") String token, @Field("user_id") String user_id, @Field("mcondition") String mcondition, @Field("notes") String notes, @Field("allergy") String allergy,
                                          @Field("bloodtype") String bloodtype, @Field("druguse") String druguse, @Field("contacttel") String contacttel, @Field("contactname") String contactname, @Field("picurl") String picurl);

    /**
     * type0：默认；1：共享手环；
     */
    @GET("info/citygymsearch")
    Call<CitySearchBean> citygymsearch(@Query("city") String city, @Query("keys") String keys, @Query("type") String type);

    @FormUrlEncoded
    @POST("info/bindgym")
    Call<UpmedicalfileBean> bindgym(@Field("user_id") String user_id, @Field("token") String token, @Field("gym_id") String gym_id, @Field("card") String card);

    @FormUrlEncoded
    @POST("info/unbindgym")
    Call<UpmedicalfileBean> unbindgym(@Field("user_id") String user_id, @Field("token") String token);

    @GET("circle/messagelist")
    Call<MessageListBean> messagelist(@Query("user_id") String user_id, @Query("token") String token, @Query("page") int page);

    @GET("circle/message")
    Call<MessageBean> message(@Query("user_id") String user_id, @Query("token") String token, @Query("message_id") String message_id, @Query("page") int page, @Query("add_id") String add_id);

    @GET("circle/bindinfo")
    Call<BindInfoBean> bindinfo(@Query("user_id") String user_id, @Query("token") String token, @Query("bind_id") String bind_id);

    @FormUrlEncoded
    @POST("circle/bindaccept")
    Call<BindacceptBean> bindaccept(@Field("user_id") String user_id, @Field("token") String token, @Field("bind_id") String bind_id, @Field("status") String status);

    @FormUrlEncoded
    @POST("circle/delmessage")
    Call<BindacceptBean> delmessage(@Field("user_id") String user_id, @Field("token") String token, @Field("message_id") String message_id);

    @GET("motions/sleepinfo")
    Call<SleepInfoBean> sleepinfo(@Query("user_id") String user_id, @Query("token") String token);

    @GET("motions/wsleepinfo")
    Call<WSleepInfoBean> wsleepinfo(@Query("user_id") String user_id, @Query("token") String token);

    @GET("motions/msleepinfo")
    Call<MSleepInfoBean> msleepinfo(@Query("user_id") String user_id, @Query("token") String token);

    @GET("motions/rentinfo")
    Call<RentInfoBean> rentinfo(@Query("user_id") String user_id, @Query("token") String token, @Query("ordernum") String ordernum);

    @GET("motions/unrentinfo")
    Call<UnRentInfoBean> unrentinfo(@Query("user_id") String user_id, @Query("token") String token, @Query("band") String band);

    @GET("motions/unrentstatus")
    Call<UnRentStatusBean> unrentstatus(@Query("user_id") String user_id, @Query("token") String token);

    @FormUrlEncoded
    @POST("info/invite")
    Call<UpmedicalfileBean> invite(@Field("user_id") String user_id, @Field("token") String token);

    @GET("motions/getnfcinfo")
    Call<GetNFCInfoBean> getnfcinfo(@Query("token") String token, @Query("user_id") String user_id);

    @GET("circle/caltopinfo")
    Call<CaltopInfoBean> caltopinfo(@Query("user_id") String user_id, @Query("token") String token);

    @GET("circle/yesdaycaltop")
    Call<CaltopInfoBean> yesdaycaltop(@Query("user_id") String user_id, @Query("token") String token);

    @FormUrlEncoded
    @POST("coach/updateplace")
    Call<BindacceptBean> updateplace(@Field("user_id") String user_id, @Field("token") String token, @Field("lat") String lat, @Field("lon") String lon, @Field("place") String place);

    @FormUrlEncoded
    @POST("motions/refund")
    Call<BindacceptBean> refund(@Field("user_id") String user_id, @Field("token") String token);

    @GET("shop/orderinfo")
    Call<OrderInfoShopBean> orderinfo(@Query("user_id") String user_id, @Query("token") String token, @Query("order_id") String order_id);

    @GET("circle/focusarchives")
    Call<FocusarchivesBean> focusarchives(@Query("token") String token, @Query("user_id") String user_id, @Query("focus_id") String focus_id);

    @GET("circle/memberapplyinfo")
    Call<MemberApplyBean> memberapplyinfo(@Query("user_id") String user_id, @Query("token") String token, @Query("member_id") String member_id);

    @FormUrlEncoded
    @POST("circle/memberapply")
    Call<BindacceptBean> memberapply(@Field("user_id") String user_id, @Field("token") String token, @Field("member_id") String member_id,
                                     @Field("ownership") String ownership, @Field("type") String type);

}
