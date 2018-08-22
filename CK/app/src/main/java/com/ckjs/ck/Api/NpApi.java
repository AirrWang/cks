package com.ckjs.ck.Api;

import com.ckjs.ck.Bean.AcceptBean;
import com.ckjs.ck.Bean.ActinfoBean;
import com.ckjs.ck.Bean.ActivityBaom;
import com.ckjs.ck.Bean.ActivitylistBean;
import com.ckjs.ck.Bean.AnalyseBean;
import com.ckjs.ck.Bean.BandBean;
import com.ckjs.ck.Bean.BandinfoBean;
import com.ckjs.ck.Bean.BandstatusBean;
import com.ckjs.ck.Bean.BodyanalyseBean;
import com.ckjs.ck.Bean.BodyanalysedBean;
import com.ckjs.ck.Bean.CaltoplistBean;
import com.ckjs.ck.Bean.CircleDongTaiBean;
import com.ckjs.ck.Bean.CitygymBean;
import com.ckjs.ck.Bean.CoachBean;
import com.ckjs.ck.Bean.CoachcommBean;
import com.ckjs.ck.Bean.CodeBean;
import com.ckjs.ck.Bean.CommentBean;
import com.ckjs.ck.Bean.ComnumBean;
import com.ckjs.ck.Bean.CrinfoBean;
import com.ckjs.ck.Bean.DelcircleBean;
import com.ckjs.ck.Bean.DelcommentBean;
import com.ckjs.ck.Bean.DirectlistBean;
import com.ckjs.ck.Bean.DirinfoBean;
import com.ckjs.ck.Bean.FamilyapplyinfoBean;
import com.ckjs.ck.Bean.FamilysearchBean;
import com.ckjs.ck.Bean.FansBean;
import com.ckjs.ck.Bean.FanstoplistBean;
import com.ckjs.ck.Bean.FavoriteBean;
import com.ckjs.ck.Bean.ForgetCodeBean;
import com.ckjs.ck.Bean.GetCircleBean;
import com.ckjs.ck.Bean.GetCircleMineBean;
import com.ckjs.ck.Bean.GetCircleTjBean;
import com.ckjs.ck.Bean.GetMyfocusBean;
import com.ckjs.ck.Bean.GetMyfocusCircleBean;
import com.ckjs.ck.Bean.GetcommentBean;
import com.ckjs.ck.Bean.GetdirectBaen;
import com.ckjs.ck.Bean.GetgymBean;
import com.ckjs.ck.Bean.GetnfcaccessBean;
import com.ckjs.ck.Bean.GetnfcsignBean;
import com.ckjs.ck.Bean.GetplaceBean;
import com.ckjs.ck.Bean.GyminfoBean;
import com.ckjs.ck.Bean.GymlistBean;
import com.ckjs.ck.Bean.HealthBean;
import com.ckjs.ck.Bean.IsfavoriteBean;
import com.ckjs.ck.Bean.LastbodyinfoBean;
import com.ckjs.ck.Bean.LaststepsBean;
import com.ckjs.ck.Bean.LessonBean;
import com.ckjs.ck.Bean.LetterstatusBean;
import com.ckjs.ck.Bean.MonthrunBean;
import com.ckjs.ck.Bean.MydirectBean;
import com.ckjs.ck.Bean.MyfamilyBean;
import com.ckjs.ck.Bean.OrderBean;
import com.ckjs.ck.Bean.OrderinfoBean;
import com.ckjs.ck.Bean.PrivilegeBean;
import com.ckjs.ck.Bean.ReadlistBean;
import com.ckjs.ck.Bean.RefundinfoBaom;
import com.ckjs.ck.Bean.RegistUserBean;
import com.ckjs.ck.Bean.RelnameBean;
import com.ckjs.ck.Bean.RuntrackBean;
import com.ckjs.ck.Bean.SartlessonBean;
import com.ckjs.ck.Bean.TolessonBean;
import com.ckjs.ck.Bean.UnbandBean;
import com.ckjs.ck.Bean.UpoxygenBean;
import com.ckjs.ck.Bean.UppressureBean;
import com.ckjs.ck.Bean.UsercommentBean;
import com.ckjs.ck.Bean.UupdapsdBean;
import com.ckjs.ck.Bean.VideolessonBean;
import com.ckjs.ck.Bean.VideostartBean;
import com.ckjs.ck.Bean.WoxygenBean;
import com.ckjs.ck.Bean.WpressureBean;
import com.ckjs.ck.Bean.WrateBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by NiPing
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public interface NpApi {
    @FormUrlEncoded
    @POST("circle/memberdel")
    Call<AcceptBean> memberdel(@Field("token") String token, @Field("user_id") int user_id, @Field("member_id") String member_id, @Field("type") String type);

    @FormUrlEncoded
    @POST("user/logup")
    Call<RegistUserBean> regist(@Field("tel") String tel,
                                @Field("password") String password, @Field("sms") String sms);

    @GET("user/sms")
    Call<CodeBean> getCode(@Query("tel") String tel);

    @FormUrlEncoded
    @POST("user/updapsd")
    Call<UupdapsdBean> updapsd(@Field("tel") String tel, @Field("password") String password, @Field("sms") String sms);

    @GET("user/updasms")
    Call<ForgetCodeBean> UpdatePwdBean(@Query("tel") String tel);

    @GET("info/actinfo")
    Call<ActinfoBean> actinfo(@Query("token") String token, @Query("user_id") int user_id, @Query("activity_id") int activity_id);

    @GET("info/activitybm")
    Call<ActivityBaom> activityBaom(@Query("token") String token, @Query("user_id") int user_id, @Query("activity_id") int activity_id);

    @GET("motions/refundinfo")
    Call<RefundinfoBaom> refundinfo(@Query("token") String token, @Query("user_id") int user_id);

    @FormUrlEncoded
    @POST("circle/publish")
    Call<CircleDongTaiBean> circlePublish(@Field("token") String token, @Field("user_id") int user_id, @Field("content") String content, @Field("place") String place,
                                          @Field("image[0]") String image0, @Field("image[1]") String image1, @Field("image[2]") String image2,
                                          @Field("image[3]") String image3, @Field("image[4]") String image4, @Field("image[5]") String image5, @Field("lat") String lat, @Field("lon") String lon);

    @GET("circle/getcircle")
    Call<GetCircleTjBean> getcircleTj(@Query("since_id") int since_id, @Query("max_id") int max_id, @Query("user_id") int user_id, @Query("token") String token);

    @GET("circle/fanscir")
    Call<GetCircleBean> fanscir(@Query("user_id") int user_id, @Query("token") String token, @Query("since_id") int since_id, @Query("max_id") int max_id);

    @GET("circle/mycircle")
    Call<GetCircleMineBean> mycircle(@Query("user_id") int user_id, @Query("token") String token, @Query("since_id") int since_id, @Query("max_id") int max_id);

    @GET("circle/cirinfo")
    Call<CrinfoBean> cirinfo(@Query("user_id") int user_id, @Query("circle_id") int circle_id);

    @GET("circle/getcomment")
    Call<GetcommentBean> getcomment(@Query("user_id") int user_id, @Query("token") String token, @Query("circle_id") int circle_id, @Query("page") int page);

    @GET("circle/myfamily")
    Call<MyfamilyBean> myfamily(@Query("user_id") int user_id, @Query("token") String token, @Query("type") String type);

    /**
     * token|token值|字符串
     * user_id|用户id|int
     * circle_id|朋友圈id|int
     * content|评论内容|字符串
     */
    @FormUrlEncoded
    @POST("circle/comment")
    Call<CommentBean> comment(@Field("token") String token, @Field("user_id") int user_id, @Field("circle_id") int circle_id, @Field("content") String content, @Field("add_id") String add_id);

    @FormUrlEncoded
    @POST("circle/focus")
    Call<FansBean> focus(@Field("token") String token, @Field("user_id") int user_id, @Field("focus_id") int focus_id);

    @GET("direct/getdirect")
    Call<GetdirectBaen> getdirect();

    @GET("direct/mydirect")
    Call<MydirectBean> mydirect(@Query("token") String token, @Query("user_id") int user_id);

    @GET("circle/delcircle")
    Call<DelcircleBean> delcircle(@Query("token") String token, @Query("user_id") int user_id, @Query("circle_id") int circle_id);


    @GET("info/readlist")
    Call<ReadlistBean> readlist(@Query("token") String token, @Query("user_id") int user_id, @Query("page") int page);

    @GET("info/activitylist")
    Call<ActivitylistBean> activitylist(@Query("token") String token, @Query("user_id") int user_id,
                                        @Query("gym_id") int gym_id, @Query("page") int page);

    @GET("info/favorite")
    Call<FavoriteBean> favorite(@Query("token") String token, @Query("user_id") int user_id, @Query("read_id") String read_id);

    @GET("info/isfavorite")
    Call<IsfavoriteBean> isfavorite(@Query("token") String token, @Query("user_id") int user_id, @Query("read_id") String read_id);

    @GET("circle/fanstoplist")
    Call<FanstoplistBean> fanstoplist();

    @GET("circle/caltoplist")
    Call<CaltoplistBean> caltoplist();

    @GET("direct/dirinfo")
    Call<DirinfoBean> dirinfo(@Query("direct_id") String direct_id);

    @GET("info/getgym")
    Call<GetgymBean> getgym(@Query("token") String token, @Query("user_id") int user_id, @Query("lat") double lat, @Query("lon") double lon, @Query("city") String city);

    @GET("info/gyminfo")
    Call<GyminfoBean> gyminfo(@Query("gym_id") String gym_id);

    @GET("info/gymlist")//0：默认；1：共享手环；
    Call<GymlistBean> gymlist(@Query("lat") String lat, @Query("lon") String lon, @Query("raidus") String raidus, @Query("type") String type);

    @GET("direct/directlist")
    Call<DirectlistBean> directlist(@Query("class_id") String class_id);

    @GET("circle/myfans")
    Call<GetMyfocusBean> myfans(@Query("token") String token, @Query("user_id") int user_id, @Query("page") int page);

    @GET("user/relsms")
    Call<CodeBean> relsms(@Query("token") String token, @Query("user_id") int user_id, @Query("tel") String tel);

    @GET("info/bodyanalyse")
    Call<BodyanalyseBean> bodyanalyse(@Query("token") String token, @Query("user_id") int user_id, @Query("lat") String lat, @Query("lon") String lon);

    @GET("info/bodyanalyse")
    Call<BodyanalysedBean> bodyanalysed(@Query("token") String token, @Query("user_id") int user_id, @Query("lat") String lat, @Query("lon") String lon);

    @GET("info/analyse")
    Call<AnalyseBean> analyse(@Query("token") String token, @Query("user_id") int user_id, @Query("gym_id") String gym_id);

    @FormUrlEncoded
    @POST("user/relname")
    Call<RelnameBean> relname(@Field("token") String token,
                              @Field("user_id") int user_id,
                              @Field("relname") String relname, @Field("tel") String tel, @Field("sms") String sms, @Field("password") String password);

    @GET("circle/thumb")
    Call<GetCircleBean> thumb(@Query("circle_id") int circle_id, @Query("user_id") int user_id, @Query("token") String token);

    @GET("circle/thumb")
    Call<GetCircleTjBean> thumbTj(@Query("circle_id") int circle_id, @Query("user_id") int user_id, @Query("token") String token);

    @GET("circle/thumb")
    Call<GetCircleMineBean> thumbMine(@Query("circle_id") int circle_id, @Query("user_id") int user_id, @Query("token") String token);

    @GET("circle/thumb")
    Call<GetMyfocusCircleBean> thumbMineFocus(@Query("circle_id") String circle_id, @Query("user_id") int user_id, @Query("token") String token);

    @GET("info/citygym")
    Call<CitygymBean> citygymFocus(@Query("lat") String lat, @Query("lon") String lon, @Query("city") String city, @Query("type") String type);

    @GET("info/coach")
    Call<CoachBean> coach(@Query("gym_id") String gym_id);

    @GET("info/gymcoach")
    Call<CoachBean> gymcoach(@Query("gym_id") String gym_id);

    @GET("info/privilege")
    Call<PrivilegeBean> privilege(@Query("gym_id") String gym_id);

    @GET("circle/delcomment")
    Call<DelcommentBean> delcomment(@Query("comment_id") String comment_id, @Query("token") String token, @Query("user_id") int user_id);

    @GET("circle/letterstatus")
    Call<LetterstatusBean> letterstatus(@Query("token") String token, @Query("user_id") int user_id);

    @GET("circle/comnum")
    Call<ComnumBean> comnum(@Query("token") String token, @Query("user_id") int user_id);

    @GET("circle/usercomment")
    Call<UsercommentBean> usercomment(@Query("token") String token, @Query("user_id") int user_id, @Query("page") int page);

    @GET("info/citycoach")
    Call<CoachBean> citycoach(@Query("city") String city);

    @GET("info/nearcoach")
    Call<CoachBean> nearcoach(@Query("lon") String lon, @Query("lat") String lat, @Query("raidus") String raidus);

    @GET("info/gymlist")
    Call<CitygymBean> gymlistJsf(@Query("lon") String lon, @Query("lat") String lat, @Query("raidus") String raidus);

    @GET("coach/order")
    Call<OrderBean> order(@Query("token") String token, @Query("user_id") int user_id, @Query("type") String type, @Query("page") int page);

    @GET("coach/accept")
    Call<AcceptBean> accept(@Query("token") String token, @Query("user_id") int user_id, @Query("id") String id);

    @GET("coach/refuse")
    Call<AcceptBean> refuse(@Query("token") String token, @Query("user_id") int user_id, @Query("id") String id, @Query("objection") String objection);

    @GET("coach/orderinfo")
    Call<OrderinfoBean> orderinfo(@Query("token") String token, @Query("user_id") int user_id, @Query("id") String id);

    @GET("shop/videolesson")
    Call<VideolessonBean> videolesson(@Query("token") String token, @Query("user_id") int user_id, @Query("id") String id, @Query("type") String type);

    @GET("shop/lesson")
    Call<LessonBean> lesson(@Query("token") String token, @Query("user_id") int user_id, @Query("id") String id, @Query("type") String type);

    @GET("shop/tolesson")
    Call<TolessonBean> tolesson(@Query("token") String token, @Query("user_id") int user_id, @Query("id") String id, @Query("type") String type);

    @GET("shop/startlesson")
    Call<SartlessonBean> startlesson(@Query("token") String token, @Query("user_id") int user_id, @Query("id") String id, @Query("type") String type);

    @GET("shop/stoplesson")
    Call<SartlessonBean> stoplesson(@Query("token") String token, @Query("user_id") int user_id, @Query("id") String id, @Query("type") String type);

    @GET("shop/coachcomm")
    Call<CoachcommBean> coachcomm(@Query("token") String token, @Query("user_id") int user_id, @Query("id") String id);

    @GET("shop/videostart")
    Call<VideostartBean> videostart(@Query("token") String token, @Query("user_id") int user_id, @Query("id") String id, @Query("type") String type);

    @GET("shop/videostop")
    Call<VideostartBean> videostop(@Query("token") String token, @Query("user_id") int user_id, @Query("id") String id, @Query("type") String type);

    @GET("motions/monthrun")
    Call<MonthrunBean> monthrun(@Query("token") String token, @Query("user_id") int user_id);

    @GET("motions/runtrack")
    Call<RuntrackBean> runtrack(@Query("token") String token, @Query("user_id") int user_id, @Query("run_id") String run_id);

    @GET("motions/wrate")
    Call<WrateBean> wrate(@Query("user_id") int user_id, @Query("token") String token);

    @GET("circle/familyapplyinfo")
    Call<FamilyapplyinfoBean> familyapplyinfo(@Query("user_id") int user_id, @Query("token") String token, @Query("bind_id") String bind_id);

    /**
     * 血氧一周详情获取
     *
     * @param token
     * @param user_id
     * @return
     */
    @GET("motions/woxygen")
    Call<WoxygenBean> woxygen(@Query("token") String token, @Query("user_id") int user_id);

    /**
     * 血压一周详情获取
     *
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @GET("motions/wpressure")
    Call<WpressureBean> wpressure(@Query("token") String token, @Query("user_id") int user_id);

    /**
     * 同步的健康数据的最后日期
     *
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @GET("motions/lastbodyinfo")
    Call<LastbodyinfoBean> lastbodyinfo(@Query("token") String token, @Query("user_id") int user_id);

    /**
     * 同步的步数的最后日期
     *
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @GET("motions/laststeps")
    Call<LaststepsBean> laststeps(@Query("token") String token, @Query("user_id") int user_id);

    @FormUrlEncoded
    @POST("shop/coachcomment")
    Call<AcceptBean> coachcomment(@Field("token") String token, @Field("user_id") int user_id, @Field("id") String id, @Field("content") String content, @Field("grade") String grade);

    @FormUrlEncoded
    @POST("coach/withdraw")
    Call<AcceptBean> withdraw(@Field("token") String token, @Field("user_id") int user_id, @Field("amount") String amount);

    /**
     * 参数|描述|值
     * user_id|用户id|
     * token|token值|
     * speed|速度|（例：12'22''/km
     * fat|卡路里消耗|（单位：千卡
     * mileage|里程|（单位：米
     * time|运动时长|（单位：秒
     * track|轨迹|
     *
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("motions/uprunrecord")
    Call<AcceptBean> uprunrecord(@Field("token") String token, @Field("user_id") int user_id,
                                 @Field("speed") String speed, @Field("fat") String fat,
                                 @Field("mileage") String mileage, @Field("time") String time,
                                 @Field("track") String track);

    /**
     * 查询手环绑定状态
     *
     * @param token
     * @param user_id
     * @param band
     * @return
     */
    @GET("motions/bandstatus")
    Call<BandstatusBean> bandstatus(@Query("token") String token, @Query("user_id") int user_id,
                                    @Query("band") String band);

    /**
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @GET("motions/health")
    Call<HealthBean> health(@Query("token") String token, @Query("user_id") int user_id);

    /**
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @GET("motions/bandinfo")
    Call<BandinfoBean> bandinfo(@Query("token") String token, @Query("user_id") int user_id, @Query("band") String band);

    /**
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @GET("motions/getnfcaccess")
    Call<GetnfcaccessBean> getnfcaccess(@Query("token") String token, @Query("user_id") int user_id);

    /**
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @GET("motions/getnfcsign")
    Call<GetnfcsignBean> getnfcsign(@Query("token") String token, @Query("user_id") int user_id);

    /**
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @GET("coach/getplace")
    Call<GetplaceBean> getplace(@Query("token") String token, @Query("user_id") int user_id);

    /**
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @GET("circle/familysearch")
    Call<FamilysearchBean> familysearch(@Query("token") String token, @Query("user_id") int user_id, @Query("content") String content);

    /**
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("circle/invite")
    Call<AcceptBean> invite(@Field("token") String token, @Field("user_id") int user_id, @Field("content") String content);

    /**
     * 手环绑定接口
     *
     * @param token
     * @param user_id
     * @param band
     * @return
     */
    @FormUrlEncoded
    @POST("motions/band")
    Call<BandBean> band(@Field("token") String token, @Field("user_id") int user_id, @Field("band") String band);

    /**
     * 手环解除绑定接口
     *
     * @param token
     * @param user_id
     * @param band
     * @return
     */
    @FormUrlEncoded
    @POST("motions/unband")
    Call<UnbandBean> unband(@Field("token") String token, @Field("user_id") int user_id, @Field("band") String band);

    /**
     * 血氧上传接口
     *
     * @param token
     * @param user_id
     * @param oxygen
     * @return
     */
    @FormUrlEncoded
    @POST("motions/upoxygen")
    Call<UpoxygenBean> upoxygen(@Field("token") String token, @Field("user_id") int user_id, @Field("oxygen") String oxygen);

    /**
     * 血压上传接口
     *
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("motions/uppressure")
    Call<UppressureBean> uppressure(@Field("token") String token, @Field("user_id") int user_id, @Field("spressure") String spressure, @Field("dpressure") String dpressure);

    /**
     * 健康监测数据实时上传接口
     *
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("motions/upbodyinfo")
    Call<AcceptBean> upbodyinfo(@Field("token") String token, @Field("user_id") int user_id, @Field("type") String type, @Field("bodyinfo") String bodyinfo, @Field("data") String data, @Field("hours") String hours);


    @FormUrlEncoded
    @POST("motions/upmovement")
    Call<AcceptBean> upmovement(@Field("token") String token, @Field("user_id") int user_id, @Field("rate") String rate, @Field("oxygen") String oxygen, @Field("spressure") String spressure, @Field("dpressure") String dpressure, @Field("prate") String prate);

    /**
     * 健康监测睡眠数据实时上传接口
     *
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("motions/upsleep")
    Call<AcceptBean> upsleep(@Field("token") String token, @Field("user_id") int user_id, @Field("sleep") String sleep);

    /**
     * 同步步数上传接口
     *
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("motions/upsteps")
    Call<AcceptBean> upsteps(@Field("token") String token, @Field("user_id") int user_id, @Field("stepsinfo") String sleep);

    /**
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("motions/unrent")
    Call<UnbandBean> unrent(@Field("token") String token, @Field("user_id") int user_id, @Field("band") String band, @Field("gym_id") String gym_id);

    /**
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("shop/lessonremind")
    Call<AcceptBean> lessonremind(@Field("token") String token, @Field("user_id") int user_id, @Field("type") String type, @Field("ease_id") String ease_id);

    /**
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("circle/upaliasname")
    Call<AcceptBean> upaliasname(@Field("token") String token, @Field("user_id") int user_id, @Field("aliasname") String aliasname, @Field("focus_id") String focus_id);

    /**
     * @param token
     * @param user_id
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("circle/familyaccept")
    Call<AcceptBean> familyaccept(@Field("token") String token, @Field("user_id") int user_id, @Field("bind_id") String bind_id, @Field("status") String status);


}
