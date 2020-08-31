package com.feemoo.fmapp.network.api;


import com.feemoo.fmapp.MyApplication;
import com.feemoo.fmapp.network.EmptyEntity;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.CouponModel;
import com.feemoo.fmapp.network.model.DownLoadModel;
import com.feemoo.fmapp.network.model.FSLModel;
import com.feemoo.fmapp.network.model.FilesListModel;
import com.feemoo.fmapp.network.model.ImageModel;
import com.feemoo.fmapp.network.model.JXLikeModel;
import com.feemoo.fmapp.network.model.JXNavModel;
import com.feemoo.fmapp.network.model.JiFenModel;
import com.feemoo.fmapp.network.model.JxDetailsModel;
import com.feemoo.fmapp.network.model.LoginRegisterModel;
import com.feemoo.fmapp.network.model.LoginCodeModel;
import com.feemoo.fmapp.network.model.MemberModel;
import com.feemoo.fmapp.network.model.MoveFoldModel;
import com.feemoo.fmapp.network.model.SvipDownModel;
import com.feemoo.fmapp.network.model.VipModel;
import com.feemoo.fmapp.network.model.history.HistoryListModel;
import com.feemoo.fmapp.network.model.select.JXHomeModel;
import com.feemoo.fmapp.network.model.share.ShareListModel;
import com.feemoo.fmapp.network.model.star.StarListModel;
import com.feemoo.fmapp.network.model.workStation.OnlineFilesModel;
import com.feemoo.fmapp.network.model.workStation.WorkStationListModel;

import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;


/**
 * Created by admin on 2018/5/11.
 */

public interface ApiService {
    String token = MyApplication.getToken(MyApplication.getmContext());

    /**
     * 用户登录接口
     *
     * @param username
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("user/accountlogin")
    Observable<BaseResponse<LoginRegisterModel>> UserLogin(@Field("username") String username,
                                                           @Field("password") String password
    );

    /**
     * 手机登录发送短信
     *
     * @param pcode
     * @param phone
     * @return
     */
    @FormUrlEncoded
    @POST("user/getlogincode")
    Observable<BaseResponse<LoginCodeModel>> getlogincode(@Field("pcode") String pcode,
                                                          @Field("phone") String phone
    );


    /**
     * 验证手机登录
     *
     * @param pcode
     * @param phone
     * @param ucode
     * @param msgid
     * @return
     */
    @FormUrlEncoded
    @POST("user/phonefastlogin")
    Observable<BaseResponse<LoginRegisterModel>> phonefastlogin(@Field("pcode") String pcode,
                                                                @Field("phone") String phone,
                                                                @Field("ucode") String ucode,
                                                                @Field("msgid") String msgid
    );

    /**
     * 手机注册发送短信
     *
     * @param pcode
     * @param phone
     * @return
     */
    @FormUrlEncoded
    @POST("user/getregcode")
    Observable<BaseResponse<LoginCodeModel>> getregcode(@Field("pcode") String pcode,
                                                        @Field("phone") String phone
    );

    /**
     * 验证手机注册用户输入的验证码
     *
     * @param pcode
     * @param phone
     * @param ucode
     * @param msgid
     * @return
     */
    @FormUrlEncoded
    @POST("user/checkregphone")
    Observable<BaseResponse<EmptyEntity>> checkregphone(@Field("pcode") String pcode,
                                                        @Field("phone") String phone,
                                                        @Field("ucode") String ucode,
                                                        @Field("msgid") String msgid
    );

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
     * @return
     */
    @FormUrlEncoded
    @POST("user/phonereg")
    Observable<BaseResponse<LoginRegisterModel>> phonereg(@Field("pcode") String pcode,
                                                          @Field("phone") String phone,
                                                          @Field("ucode") String ucode,
                                                          @Field("msgid") String msgid,
                                                          @Field("username") String username,
                                                          @Field("password") String password,
                                                          @Field("cpassword") String cpassword
    );

    /**
     * 手机注册第二步中[绑定已有账号]
     *
     * @param pcode
     * @param phone
     * @param ucode
     * @param msgid
     * @param username
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("user/phonebind")
    Observable<BaseResponse<LoginRegisterModel>> phonebind(@Field("pcode") String pcode,
                                                           @Field("phone") String phone,
                                                           @Field("ucode") String ucode,
                                                           @Field("msgid") String msgid,
                                                           @Field("username") String username,
                                                           @Field("password") String password
    );

    /**
     * 文件列表数据
     *
     * @param token
     * @param folderid
     * @param keywords
     * @param pg
     * @return
     */
    @FormUrlEncoded
    @POST("file/getfiles")
    Observable<BaseResponse<FilesListModel>> getfiles(@Header("token") String token,
                                                      @Field("folderid") int folderid,
                                                      @Field("keywords") String keywords,
                                                      @Field("pg") int pg
    );

    /**
     * 云空间获取文件的飞速链
     *
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("file/getfsl")
    Observable<BaseResponse<FSLModel>> getfsl(@Header("token") String token,
                                              @Field("id") int id
    );

    /**
     * 云空间修改文件名
     *
     * @param token
     * @param id
     * @param name
     * @return
     */
    @FormUrlEncoded
    @POST("file/filerename")
    Observable<BaseResponse<String>> filerename(@Header("token") String token,
                                                @Field("name") String name,
                                                @Field("id") int id
    );

    /**
     * 云空间删除文件
     *
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("file/delfile")
    Observable<BaseResponse<String>> delfile(@Header("token") String token,
                                             @Field("id") int id
    );

    /**
     * 云空间获取用户文件夹-用于移动文件
     *
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("file/getfolders")
    Observable<BaseResponse<MoveFoldModel>> getfolders(@Header("token") String token,
                                                       @Field("id") int id
    );

    /**
     * 云空间移动文件
     *
     * @param token
     * @param fileid
     * @param folderid
     * @return
     */
    @FormUrlEncoded
    @POST("file/movefile")
    Observable<BaseResponse<String>> movefile(@Header("token") String token,
                                              @Field("fileid") String fileid,
                                              @Field("folderid") String folderid
    );

    /**
     * 云空间推至操作台
     *
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("file/tospro")
    Observable<BaseResponse<String>> tospro(@Header("token") String token,
                                            @Field("id") int id
    );

    /**
     * 云空间公有文件/私有文件转换
     *
     * @param token
     * @param fid
     * @param st
     * @return
     */
    @FormUrlEncoded
    @POST("api/setflshare")
    Observable<BaseResponse<String>> setflshare(@Header("token") String token,
                                                @Field("fid") String fid,
                                                @Field("st") String st
    );

    /**
     * 操作台删除操作台文件
     *
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("file/delprofile")
    Observable<BaseResponse<String>> delprofile(@Header("token") String token,
                                                @Field("id") int id
    );

    /**
     * 操作台下载过渡页
     *
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("file/filedetail")
    Observable<BaseResponse<DownLoadModel>> filedetail(@Header("token") String token,
                                                       @Field("id") int id
    );

    /**
     * 操作台转存至永久空间
     *
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("file/toever")
    Observable<BaseResponse<String>> toever(@Header("token") String token,
                                            @Field("id") int id
    );

    /**
     * 在线解压
     *
     * @param token
     * @param isview
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("file/zip")
    Observable<BaseResponse<String>> zip(@Header("token") String token,
                                         @Field("id") int id,
                                         @Field("isview") int isview,
                                         @Field("password") String password
    );

    /**
     * 在线预览
     * 预览/在线解压-压缩包文件-步骤1
     *
     * @param token
     * @param id
     * @param isview
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("file/zip")
    Observable<BaseResponse<OnlineFilesModel>> getzip(@Header("token") String token,
                                                      @Field("id") int id,
                                                      @Field("isview") int isview,
                                                      @Field("password") String password
    );

    /**
     * 星标文件
     *
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("file/cltfile")
    Observable<BaseResponse<String>> cltfile(@Header("token") String token,
                                             @Field("id") int id
    );

    /**
     * 星标删除星标文件
     *
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("file/delcltfile")
    Observable<BaseResponse<String>> delcltfile(@Header("token") String token,
                                                @Field("id") int id
    );

    /**
     * 修改星标文件名
     *
     * @param token
     * @param name
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("file/cltrename")
    Observable<BaseResponse<String>> cltrename(@Header("token") String token,
                                               @Field("name") String name,
                                               @Field("id") int id
    );

    /**
     * 操作台文件列表数据（包括永久空间）
     *
     * @param token
     * @param isever
     * @param folderid
     * @param keywords
     * @param pg
     * @return
     */
    @FormUrlEncoded
    @POST("file/getprofiles")
    Observable<BaseResponse<WorkStationListModel>> getprofiles(@Header("token") String token,
                                                               @Field("isever") int isever,
                                                               @Field("folderid") int folderid,
                                                               @Field("keywords") String keywords,
                                                               @Field("pg") int pg
    );


    /**
     * 星标文件列表数据
     *
     * @param token
     * @param keywords
     * @param pg
     * @return
     */
    @FormUrlEncoded
    @POST("file/getcltfiles")
    Observable<BaseResponse<StarListModel>> getcltfiles(@Header("token") String token,
                                                        @Field("keywords") String keywords,
                                                        @Field("pg") int pg
    );

    /**
     * 下载历史列表数据
     *
     * @param token
     * @param pg
     * @return
     */
    @FormUrlEncoded
    @POST("file/gethisfiles")
    Observable<BaseResponse<HistoryListModel>> gethisfiles(@Header("token") String token,
                                                           @Field("pg") int pg
    );

    /**
     * 获取个人消息
     *
     * @return
     */
    @POST("center/base")
    Observable<BaseResponse<MemberModel>> getMemberInfo(@Header("token") String token


    );

    /**
     * 个人中心上传头像-保存头像
     *
     * @param token
     * @param imgurl
     * @return
     */
    @FormUrlEncoded
    @POST("center/setlogo")
    Observable<BaseResponse<String>> setlogo(@Header("token") String token,
                                             @Field("imgurl") String imgurl


    );

    /**
     * SVIP购买信息
     *
     * @param token
     * @return
     */
    @POST("api/vipinfo")
    Observable<BaseResponse<VipModel>> vipinfo(@Header("token") String token


    );

    /**
     * 通过手机找回密码
     *
     * @param token
     * @param pcode
     * @param phone
     * @param ucode
     * @param msgid
     * @return
     */
    @FormUrlEncoded
    @POST("user/phonemodipass")
    Observable<BaseResponse<String>> phonemodipass(
            @Header("token") String token,
            @Field("pcode") String pcode,
            @Field("phone") String phone,
            @Field("ucode") String ucode,
            @Field("msgid") String msgid
    );

    /**
     * 下载文件
     *
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("file/svipdown")
    Observable<BaseResponse<SvipDownModel>> svipdown(@Header("token") String token,
                                                     @Field("id") int id

    );

    /**
     * 操作台下载文件
     *
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("file/prodown")
    Observable<BaseResponse<SvipDownModel>> prodown(@Header("token") String token,
                                                    @Field("id") int id,
                                                    @Field("isview") int isview

    );

    /**
     * 我的云文件分享列表
     *
     * @param token
     * @param keywords
     * @param pg
     * @return
     */
    @FormUrlEncoded
    @POST("file/getsharefiles")
    Observable<BaseResponse<ShareListModel>> getsharefiles(@Header("token") String token,
                                                           @Field("keywords") String keywords,
                                                           @Field("pg") int pg

    );

    /**
     * 鲸选首页-顶部默认展示一级分类
     *
     * @param token
     * @return
     */
    @POST("choice/getjxnav")
    Observable<BaseResponse<JXNavModel>> getjxnav(@Header("token") String token

    );

    /**
     * 鲸选首页-进入首页后展示的鲸选数据
     *
     * @param token
     * @return
     */
    @POST("choice/getjxtopfile")
    Observable<BaseResponse<JXHomeModel>> getjxtopfile(@Header("token") String token

    );

    /**
     * 鲸选-详情页
     *
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("choice/jxdetail")
    Observable<BaseResponse<JxDetailsModel>> jxdetail(@Header("token") String token,
                                                      @Field("id") String id

    );

    /**
     * 鲸选-33技术教程的点击展示全文
     *
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("choice/gethfcon")
    Observable<BaseResponse<String>> gethfcon(@Header("token") String token,
                                              @Field("id") String id

    );

    /**
     * 鲸选-点赞/踩
     *
     * @param token
     * @param id
     * @param st
     * @return
     */
    @FormUrlEncoded
    @POST("choice/jxlike")
    Observable<BaseResponse<JXLikeModel>> jxlike(@Header("token") String token,
                                                 @Field("id") String id,
                                                 @Field("st") String st

    );

    /**
     * 鲸选-文件下载
     *
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("choice/jxdown")
    Observable<BaseResponse<SvipDownModel>> jxdown(@Header("token") String token,
                                                   @Field("id") String id

    );

    /**
     * 扫码二维码-登录
     *
     * @param token
     * @param code
     * @return
     */
    @FormUrlEncoded
    @POST("user/scanlogin")
    Observable<BaseResponse<String>> scanlogin(@Header("token") String token,
                                               @Field("code") String code

    );

    /**
     * 卡券包列表数据
     * @param token
     * @return
     */
    @POST("center/coupct")
    Observable<BaseResponse<CouponModel>> coupct(@Header("token") String token

    );

    /**
     * 签到前加载信息
     * @param token
     * @return
     */
    @POST("user/getsigndayv2")
    Observable<BaseResponse<JiFenModel>> getsigndayv2(@Header("token") String token

    );
    @Multipart
    @POST("https://ucgimg.fmapp.com/img_server/kind/php/img_up.php")
    Observable<ImageModel> uploadImage(@Part MultipartBody.Part filePart, @Header("token") String token);

//    /**
//     * 视频列表记录
//     * @param token
//     * @param type
//     * @param page
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("video/list ")
//    Observable<BaseResponse<VideoModel>> VideoList(@Field("token") String token,
//                                                   @Field("type") String type,
//                                                   @Field("page") String page
//    );
}
