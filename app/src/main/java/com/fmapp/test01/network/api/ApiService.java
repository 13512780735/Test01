package com.fmapp.test01.network.api;


import com.fmapp.test01.MyApplication;
import com.fmapp.test01.network.EmptyEntity;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.DownLoadModel;
import com.fmapp.test01.network.model.FilesListModel;
import com.fmapp.test01.network.model.ImageModel;
import com.fmapp.test01.network.model.LoginRegisterModel;
import com.fmapp.test01.network.model.LoginCodeModel;
import com.fmapp.test01.network.model.MemberModel;
import com.fmapp.test01.network.model.SvipDownModel;
import com.fmapp.test01.network.model.history.HistoryListModel;
import com.fmapp.test01.network.model.star.StarListModel;
import com.fmapp.test01.network.model.workStation.OnlineFilesModel;
import com.fmapp.test01.network.model.workStation.WorkStationListModel;

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
     * 删除文件
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
     * 推至操作台
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
     * 删除操作台文件
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
     * 下载过渡页
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
     * 转存至永久空间
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
     * 删除星标文件
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
     * 下载文件
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("file/svipdown")
    Observable<BaseResponse<SvipDownModel>> svipdown(@Header("token") String token,
                                                     @Field("id") int id

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
