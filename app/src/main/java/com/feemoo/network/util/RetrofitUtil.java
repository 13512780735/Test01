package com.feemoo.network.util;


import com.feemoo.BuildConfig;
import com.feemoo.network.Appconst.AppConst;
import com.feemoo.network.EmptyEntity;
import com.feemoo.network.api.ApiService;
import com.feemoo.network.model.BaseResponse;
import com.feemoo.network.model.CouponModel;
import com.feemoo.network.model.DownLoadModel;
import com.feemoo.network.model.FSLModel;
import com.feemoo.network.model.FilesListModel;
import com.feemoo.network.model.JXLikeModel;
import com.feemoo.network.model.JXNavModel;
import com.feemoo.network.model.JiFenModel;
import com.feemoo.network.model.JxDetailsModel;
import com.feemoo.network.model.LoginRegisterModel;
import com.feemoo.network.model.LoginCodeModel;
import com.feemoo.network.model.MemberModel;
import com.feemoo.network.model.MoveFoldModel;
import com.feemoo.network.model.SvipDownModel;
import com.feemoo.network.model.VipModel;
import com.feemoo.network.model.history.HistoryListModel;
import com.feemoo.network.model.select.JXHomeModel;
import com.feemoo.network.model.share.ShareListModel;
import com.feemoo.network.model.star.StarListModel;
import com.feemoo.network.model.workStation.OnlineFilesModel;
import com.feemoo.network.model.workStation.WorkStationListModel;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author nanchen
 * @fileName RetrofitRxDemoo
 * @packageName com.nanchen.retrofitrxdemoo.util
 * @date 2016/12/12  10:38
 */

public class RetrofitUtil {

    public static final int DEFAULT_TIMEOUT = 5;

    private Retrofit mRetrofit;
    private ApiService mApiService;

    private static RetrofitUtil mInstance;


    /**
     * 私有构造方法
     */
    private RetrofitUtil() {

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            // 打印网络请求日志
            LoggingInterceptor httpLoggingInterceptor = new LoggingInterceptor.Builder()
                    .loggable(BuildConfig.DEBUG)
                    .setLevel(Level.BASIC)
                    .log(Platform.INFO)
                    .request("Request")
                    .response("Response")
                    .build();
            httpClientBuilder.addInterceptor(httpLoggingInterceptor);

        }


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(MyGsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClientBuilder.build())
                .baseUrl(AppConst.BASE_URL)
                .build();
        mApiService = mRetrofit.create(ApiService.class);
    }

    public static RetrofitUtil getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitUtil.class) {
                mInstance = new RetrofitUtil();
            }
        }
        return mInstance;
    }

    /**
     * 密码账号用户登录
     *
     * @param username
     * @param password
     * @param subscriber
     */

    public void getUserLogin(String username, String password, Subscriber<BaseResponse<LoginRegisterModel>> subscriber) {
        mApiService.UserLogin(username, password)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 手机登录发送短信
     *
     * @param pcode
     * @param phone
     * @param subscriber
     */
    public void getlogincode(String pcode, String phone, Subscriber<BaseResponse<LoginCodeModel>> subscriber) {
        mApiService.getlogincode(pcode, phone)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 验证手机登录
     *
     * @param pcode
     * @param phone
     * @param ucode
     * @param msgid
     * @param subscriber
     */
    public void phonefastlogin(String pcode, String phone, String ucode, String msgid, Subscriber<BaseResponse<LoginRegisterModel>> subscriber) {
        mApiService.phonefastlogin(pcode, phone, ucode, msgid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 手机注册发送短信
     *
     * @param pcode
     * @param phone
     * @param subscriber
     */
    public void getregcode(String pcode, String phone, Subscriber<BaseResponse<LoginCodeModel>> subscriber) {
        mApiService.getregcode(pcode, phone)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 验证手机注册用户输入的验证码
     *
     * @param pcode
     * @param phone
     * @param ucode
     * @param msgid
     * @param subscriber
     */
    public void checkregphone(String pcode, String phone, String ucode, String msgid, Subscriber<BaseResponse<EmptyEntity>> subscriber) {
        mApiService.checkregphone(pcode, phone, ucode, msgid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 手机注册第二步中[新注册]
     *
     * @param pcode
     * @param phone
     * @param ucode
     * @param msgid
     * @param username
     * @param password
     * @param cpassword
     * @param subscriber
     */
    public void phonereg(String pcode, String phone, String ucode, String msgid, String username, String password, String cpassword, Subscriber<BaseResponse<LoginRegisterModel>> subscriber) {
        mApiService.phonereg(pcode, phone, ucode, msgid, username, password, cpassword)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 手机注册第二步中[绑定已有账号]
     *
     * @param pcode
     * @param phone
     * @param ucode
     * @param msgid
     * @param username
     * @param password
     * @param subscriber
     */
    public void phonebind(String pcode, String phone, String ucode, String msgid, String username, String password, Subscriber<BaseResponse<LoginRegisterModel>> subscriber) {
        mApiService.phonebind(pcode, phone, ucode, msgid, username, password)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 文件列表数据
     *
     * @param token
     * @param folderid
     * @param keywords
     * @param pg
     * @param subscriber
     */
    public void getfiles(String token, int folderid, String keywords, int pg, Subscriber<BaseResponse<FilesListModel>> subscriber) {
        mApiService.getfiles(token, folderid, keywords, pg)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 预览/在线解压-压缩包文件-步骤1
     * 在线预览
     *
     * @param token
     * @param id
     * @param isview
     * @param password
     * @param subscriber
     */
    public void getzip(String token, int id, int isview, String password, Subscriber<BaseResponse<OnlineFilesModel>> subscriber) {
        mApiService.getzip(token, id, isview, password)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void zip(String token, int id, int isview, String password, Subscriber<BaseResponse<String>> subscriber) {
        mApiService.zip(token, id, isview, password)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取文件的飞速链
     *
     * @param token
     * @param id
     * @param subscriber
     */
    public void getfsl(String token, int id, Subscriber<BaseResponse<FSLModel>> subscriber) {
        mApiService.getfsl(token, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 云空间修改文件名
     *
     * @param token
     * @param id
     * @param name
     * @param subscriber
     */
    public void filerename(String token, String name, int id, Subscriber<BaseResponse<String>> subscriber) {
        mApiService.filerename(token, name, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除文件
     *
     * @param token
     * @param id
     * @param subscriber
     */
    public void delfile(String token, int id, Subscriber<BaseResponse<String>> subscriber) {
        mApiService.delfile(token, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 云空间获取用户文件夹-用于移动文件
     *
     * @param token
     * @param id
     * @param subscriber
     */
    public void getfolders(String token, int id, Subscriber<BaseResponse<MoveFoldModel>> subscriber) {
        mApiService.getfolders(token, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 云空间公有文件/私有文件转换
     *
     * @param token
     * @param fid
     * @param st
     * @param subscriber
     */
    public void setflshare(String token, String fid, String st, Subscriber<BaseResponse<String>> subscriber) {
        mApiService.setflshare(token, fid, st)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 云空间移动文件
     *
     * @param token
     * @param fileid
     * @param folderid
     * @param subscriber
     */
    public void movefile(String token, String fileid, String folderid, Subscriber<BaseResponse<String>> subscriber) {
        mApiService.movefile(token, fileid, folderid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 推至操作台
     *
     * @param token
     * @param id
     * @param subscriber
     */
    public void tospro(String token, int id, Subscriber<BaseResponse<String>> subscriber) {
        mApiService.tospro(token, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除操作台文件
     *
     * @param token
     * @param id
     * @param subscriber
     */
    public void delprofile(String token, int id, Subscriber<BaseResponse<String>> subscriber) {
        mApiService.delprofile(token, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 下载过渡页
     *
     * @param token
     * @param id
     * @param subscriber
     */
    public void filedetail(String token, int id, Subscriber<BaseResponse<DownLoadModel>> subscriber) {
        mApiService.filedetail(token, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 转存至永久空间
     *
     * @param token
     * @param id
     * @param subscriber
     */
    public void toever(String token, int id, Subscriber<BaseResponse<String>> subscriber) {
        mApiService.toever(token, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 星标文件
     *
     * @param token
     * @param id
     * @param subscriber
     */
    public void cltfile(String token, int id, Subscriber<BaseResponse<String>> subscriber) {
        mApiService.cltfile(token, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 星标文件列表数据
     *
     * @param token
     * @param keywords
     * @param pg
     * @param subscriber
     */
    public void getcltfiles(String token, String keywords, int pg, Subscriber<BaseResponse<StarListModel>> subscriber) {
        mApiService.getcltfiles(token, keywords, pg)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除星标文件
     *
     * @param token
     * @param id
     * @param subscriber
     */
    public void delcltfile(String token, int id, Subscriber<BaseResponse<String>> subscriber) {
        mApiService.delcltfile(token, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 修改星标文件名
     *
     * @param token
     * @param name
     * @param id
     * @param subscriber
     */
    public void cltrename(String token, String name, int id, Subscriber<BaseResponse<String>> subscriber) {
        mApiService.cltrename(token, name, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 操作台文件列表数据（包括永久空间）
     *
     * @param token
     * @param isever
     * @param folderid
     * @param keywords
     * @param pg
     * @param subscriber
     */
    public void getprofiles(String token, int isever, int folderid, String keywords, int pg, Subscriber<BaseResponse<WorkStationListModel>> subscriber) {
        mApiService.getprofiles(token, isever, folderid, keywords, pg)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 下载历史列表数据
     *
     * @param token
     * @param pg
     * @param subscriber
     */

    public void gethisfiles(String token, int pg, Subscriber<BaseResponse<HistoryListModel>> subscriber) {
        mApiService.gethisfiles(token, pg)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 下载文件
     *
     * @param token
     * @param id
     * @param subscriber
     */
    public void svipdown(String token, int id, Subscriber<BaseResponse<SvipDownModel>> subscriber) {
        mApiService.svipdown(token, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 操作台下载
     *
     * @param token
     * @param id
     * @param isview
     * @param subscriber
     */
    public void prodown(String token, int id, int isview, Subscriber<BaseResponse<SvipDownModel>> subscriber) {
        mApiService.prodown(token, id, isview)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 我的云文件分享列表
     *
     * @param token
     * @param keywords
     * @param pg
     * @param subscriber
     */
    public void getsharefiles(String token, String keywords, int pg, Subscriber<BaseResponse<ShareListModel>> subscriber) {
        mApiService.getsharefiles(token, keywords, pg)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 鲸选首页-顶部默认展示一级分类
     *
     * @param token
     * @return
     */
    public void getjxnav(String token, Subscriber<BaseResponse<JXNavModel>> subscriber) {
        mApiService.getjxnav(token)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 鲸选首页-进入首页后展示的鲸选数据
     *
     * @param token
     * @param subscriber
     */

    public void getjxtopfile(String token, Subscriber<BaseResponse<JXHomeModel>> subscriber) {
        mApiService.getjxtopfile(token)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 鲸选-详情页
     *
     * @param token
     * @param id
     * @param subscriber
     */
    public void jxdetail(String token, String id, Subscriber<BaseResponse<JxDetailsModel>> subscriber) {
        mApiService.jxdetail(token, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 鲸选-33技术教程的点击展示全文
     *
     * @param token
     * @param id
     * @param subscriber
     */
    public void gethfcon(String token, String id, Subscriber<BaseResponse<String>> subscriber) {
        mApiService.gethfcon(token, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 鲸选-点赞/踩
     *
     * @param token
     * @param id
     * @param st
     * @return
     */
    public void jxlike(String token, String id, String st, Subscriber<BaseResponse<JXLikeModel>> subscriber) {
        mApiService.jxlike(token, id, st)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 鲸选-文件下载
     *
     * @param token
     * @param id
     * @param subscriber
     */
    public void jxdown(String token, String id, Subscriber<BaseResponse<SvipDownModel>> subscriber) {
        mApiService.jxdown(token, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取个人消息
     *
     * @param token
     * @param subscriber
     */
    public void getMemberInfo(String token, Subscriber<BaseResponse<MemberModel>> subscriber) {
        mApiService.getMemberInfo(token)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 个人中心上传头像-保存头像
     *
     * @param token
     * @param imgurl
     * @param subscriber
     */
    public void setlogo(String token, String imgurl, Subscriber<BaseResponse<String>> subscriber) {
        mApiService.setlogo(token, imgurl)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * SVIP购买信息
     *
     * @param token
     * @param subscriber
     */
    public void vipinfo(String token, Subscriber<BaseResponse<VipModel>> subscriber) {
        mApiService.vipinfo(token)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 扫码二维码-登录
     *
     * @param token
     * @param code
     * @param subscriber
     */
    public void scanlogin(String token, String code, Subscriber<BaseResponse<String>> subscriber) {
        mApiService.scanlogin(token, code)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 卡券包列表数据
     *
     * @param token
     * @param subscriber
     */
    public void coupct(String token, Subscriber<BaseResponse<CouponModel>> subscriber) {
        mApiService.coupct(token)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 签到前加载信息
     *
     * @param token
     * @param subscriber
     */
    public void getsigndayv2(String token, Subscriber<BaseResponse<JiFenModel>> subscriber) {
        mApiService.getsigndayv2(token)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 通过手机找回密码
     *
     * @param token
     * @param pcode
     * @param phone
     * @param ucode
     * @param msgid
     * @param subscriber
     */
    public void phonemodipass(String token, String pcode, String phone, String ucode, String msgid, Subscriber<BaseResponse<String>> subscriber) {
        mApiService.phonemodipass(token, pcode, phone, ucode, msgid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
//
//    /**
//     * 退出登录
//     *
//     * @param token
//     * @param subscriber
//     */
//    public void UserLogout(String token, Subscriber<BaseResponse<EmptyEntity>> subscriber) {
//        mApiService.UserLogout(token)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//    }


//    /**
//     * 获取订单列表
//     *
//     * @param token
//     * @param status
//     * @param page
//     * @param subscriber
//     */
//    public void GetOrderList(String token, String status, String page,
//                             Subscriber<BaseResponse<OrderListModel>> subscriber) {
//        mApiService.GetOrderList(token, status, page)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//    }
//
//
//    /**
//     * 视频记录
//     * @param token
//     * @param page
//     * @param type
//     * @param subscriber
//     */
//    public void VideoList(String token, String page, String type,
//                          Subscriber<BaseResponse<VideoModel>> subscriber) {
//        mApiService.VideoList(token, page, type)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//    }

    private <T> void toSubscribe(Observable<T> observable, Subscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
