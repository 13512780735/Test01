package com.fmapp.test01.activity.file.zip;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidev.download.DownloadInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fmapp.test01.R;
import com.fmapp.test01.activity.file.PictureActivity;
import com.fmapp.test01.activity.file.showOnlineDialog;
import com.fmapp.test01.activity.file.zip.adapter.FileListAdapter;
import com.fmapp.test01.activity.file.zip.widget.PasswordDialog;
import com.fmapp.test01.base.BaseActivity;
import com.fmapp.test01.download.FileModel;
import com.fmapp.test01.download.manager.ThreadManager;
import com.fmapp.test01.download.manager.UnZipManager;
import com.fmapp.test01.download.util.FileManager;
import com.fmapp.test01.download.util.FileUtils;
import com.fmapp.test01.utils.Utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZipRarFileActivity extends BaseActivity {
    /**
     * 消息相关
     **/
    private static final int HANDLER_SHOW_UN_ALL_FILE_LIST = 100;
    private static final int HANDLER_UN_ALL_FILE = 200;
    /**
     * 控件相关
     **/
    private ImageView ivBack;
    private TextView tvPath;
    private Button btnUnRar;
    private RecyclerView rvFilePath;
    private RecyclerView rvFileList;
    private ProgressBar progressBar;
    /**
     * 当前路径
     **/
    private String strCurrectPath;
    /**
     * zip源文件的路径
     **/
    private String zipRarFilePath;
    /**
     * 用来拆分层级
     **/
    private int splitIndex = 1;
    /**
     * 原始数据集合
     **/
    private List<FileModel> zipOrRarFileModelList = new ArrayList<>();
    /**
     * 用来显示的数据集合
     **/
    private List<FileModel> showZipRarFileList = new ArrayList<>();
    /**
     * 路径集合
     **/
    private List<String> filePathList = new ArrayList<>();
    /**
     * 文件列表适配器
     **/
    private FileListAdapter fileListAdapter;
    /**
     * 文件路径适配器
     **/
    // private FilePathAdapter filePathAdapter;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_SHOW_UN_ALL_FILE_LIST:
                    progressBar.setVisibility(View.GONE);
                    showZipRarFileList.clear();
                    showZipRarFileList.addAll((List<FileModel>) msg.obj);
                    sortFileModelsList();
                    if (showZipRarFileList != null && showZipRarFileList.size() > 0) {
                        btnUnRar.setVisibility(View.INVISIBLE);
                    } else {
                        btnUnRar.setVisibility(View.INVISIBLE);
                    }
                    break;
                case HANDLER_UN_ALL_FILE:
                    progressBar.setVisibility(View.GONE);
                    String unFilePath = (String) msg.obj;
                    if (!TextUtils.isEmpty(unFilePath)) {
                        Toast.makeText(ZipRarFileActivity.this, getResources().getString(R.string.un_file_success),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ZipRarFileActivity.this, getResources().getString(R.string.operation_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private String name;
    private FileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_rar_file);
        fileManager = new FileManager(this);
        zipRarFilePath = getIntent().getStringExtra("zipRarFilePath");
        name = getIntent().getStringExtra("zipRarFileName");
        initView();
        setAdapter();
        initListener();
        getZipOrRarFile();


    }

    private void setAdapter() {
        fileListAdapter = new FileListAdapter(R.layout.file_list_item, showZipRarFileList);
        rvFileList.setAdapter(fileListAdapter);
//        filePathAdapter = new FilePathAdapter(R.layout.file_path_item, filePathList);
//        rvFilePath.setAdapter(filePathAdapter);
    }

    /**
     * 获取zip/rar文件
     */
    private void getZipOrRarFile() {

        tvPath.setText(name);
        if (!TextUtils.isEmpty(zipRarFilePath)) {
            strCurrectPath = zipRarFilePath;
            tvPath.setText(name);
            //预览zip/rar文件内容列表
            readZipOrRarFileContentList(new File(zipRarFilePath));
        }
    }

    /**
     * 读取zip/rar文件内容列表
     *
     * @param zipRarFile
     */
    private void readZipOrRarFileContentList(final File zipRarFile) {
        progressBar.setVisibility(View.VISIBLE);
        ThreadManager.getInstance().start(new Runnable() {
            @Override
            public void run() {
                if (zipRarFile.getName().endsWith(".zip")) {
                    // zipOrRarFileModelList = UnRarManager.getInstance().getRarFileAllList(zipRarFile);
                    zipOrRarFileModelList = UnZipManager.getInstance().getZipFileAllList(zipRarFile);
                } else {
                    zipOrRarFileModelList = UnZipManager.getInstance().getZipFileAllList(zipRarFile);
                }
                //如果不是刷新的操作再去显示路径和递增，避免层级错乱
                splitIndex++;
                //根据路径预览,首次进入zip/rar文件内部路径为空
                layeredShowByPath(zipOrRarFileModelList, "");
            }
        });
    }

    /**
     * 根据路径打开的zip/rar文件预览
     *
     * @param zipRarFileList
     * @param zipRarFileInnerPath
     */
    private void layeredShowByPath(final List<FileModel> zipRarFileList, final String zipRarFileInnerPath) {
        //显示路径
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showRecyclerViewPath(zipRarFileInnerPath);
            }
        });
        List<FileModel> showRarFiles = new ArrayList<>();
        if (zipRarFileList != null && zipRarFileList.size() > 0) {
            for (FileModel fileModel : zipRarFileList) {
                //路径为空表示第一层级
                if (TextUtils.isEmpty(zipRarFileInnerPath) && !fileModel.getFilePath().contains("\\")) {
                    showRarFiles.add(fileModel);
                }
                //拆分层级
                if (!TextUtils.isEmpty(zipRarFileInnerPath) && fileModel.getFilePath().startsWith(zipRarFileInnerPath + "\\")
                        && fileModel.getFilePath().split("\\\\").length < splitIndex) {
                    String[] splitFileName = fileModel.getFileName().split("\\\\");
                    fileModel.setFileName(splitFileName[splitFileName.length - 1]);
                    showRarFiles.add(fileModel);
                }
            }
        }
        Message msg = mHandler.obtainMessage();
        msg.what = HANDLER_SHOW_UN_ALL_FILE_LIST;
        msg.obj = showRarFiles;
        mHandler.sendMessage(msg);
    }


    /**
     * 显示路径
     *
     * @param rarFileInnerPath rar文件内部路径
     */
    private void showRecyclerViewPath(String rarFileInnerPath) {
        if (TextUtils.isEmpty(rarFileInnerPath)) {
            tvPath.setVisibility(View.VISIBLE);
            rvFilePath.setVisibility(View.GONE);
        } else {
            tvPath.setVisibility(View.VISIBLE);
            rvFilePath.setVisibility(View.GONE);
            filePathList.clear();
            if (rarFileInnerPath.contains("\\")) {
                String[] splitPath = rarFileInnerPath.split("\\\\");
                filePathList.addAll(Arrays.asList(splitPath));
            } else {
                filePathList.add(rarFileInnerPath);
            }
        }
    }

    String rarOutFilePath = "";
    String names;
    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        fileListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (showZipRarFileList != null && showZipRarFileList.size() > 0) {
                    final FileModel fileModel = showZipRarFileList.get(position);
                    //如果预览是文件夹
                    if (fileModel.isDir()) {
                        splitIndex++;
                        strCurrectPath = fileModel.getFilePath();
                        layeredShowByPath(zipOrRarFileModelList, strCurrectPath);
                    }
                    //如果预览是文件
                    else {
                        //缓存路径
                        final String outPath = FileUtils.getAppTempPath();
                        Log.d("zipRarFilePath:", zipRarFilePath);


                        try {
                            String fileName = fileModel.getFilePath();
                            String fileName1 = null;
                            fileName1 = new String(fileModel.getFilePath().getBytes("GBK"), "UTF-8");
                            if (fileName1.length() > fileName.length()) {
                                names = fileName;
                            } else {
                                names = fileName1;
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        Log.d("zipRarFilePath222:",names);
                        Log.d("zipRarFilePath333:", outPath);
                        rarOutFilePath = UnZipManager.getInstance().unZipFileSingle(zipRarFilePath, outPath, names, "");
                        Log.d("路径：", rarOutFilePath);
//                        Log.d("路径5：", showZipRarFileList.get(position).getFilePath());
//                        Log.d("路径2：", FileUtils.getSDCardFilesPath());
//                        Log.d("路径3：", FileUtils.getFileNameNoExtension(outPath));
//                        Log.d("路径3：", FileUtils.getFileNameNoExtension(outPath));
                          FileUtils.openFileByApp(ZipRarFileActivity.this, rarOutFilePath);
//                        if (Utils.isFastClick()) {
//                            Log.d("点记录", "");
//                            String names = null;
//                            try {
//                                String fileName = showZipRarFileList.get(position).getFilePath();
//                                String fileName1 = null;
//                                fileName1 = new String(showZipRarFileList.get(position).getFilePath().getBytes("GBK"), "UTF-8");
//                                if (fileName1.length() > fileName.length()) {
//                                    names = fileName;
//                                } else {
//                                    names = fileName1;
//                                }
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
//
//                            String uri = FileUtils.getSDCardFilesPath() + "/Download/" + name;
//                            Intent intent = new Intent(mContext, PictureActivity.class);
//                            intent.putExtra("uri", uri);
//                            intent.putExtra("name", names);
//                            intent.putExtra("flag", "0");
//                            //intent .setType("image/*");
//                            mContext.startActivity(intent);
//                            // fileManager.openFile(mContext, FileUtils.getSDCardFilesPath()+showZipRarFileList.get(position).getFilePath(), showZipRarFileList.get(position).getFilePath());
//                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        goBack();
    }


    /**
     * 返回上一级
     */
    private void goBack() {
        splitIndex--;
        if (splitIndex > 2) {
            strCurrectPath = strCurrectPath.substring(0, strCurrectPath.lastIndexOf("\\"));
            layeredShowByPath(zipOrRarFileModelList, strCurrectPath);
        } else if (splitIndex == 2) {
            strCurrectPath = zipRarFilePath;
            tvPath.setText(name);
            layeredShowByPath(zipOrRarFileModelList, "");
        } else {
            finish();
        }
    }

    private void initView() {
        ivBack = findView(R.id.iv_back);
        tvPath = findView(R.id.tv_path);
        btnUnRar = findView(R.id.btn_unrar);
        btnUnRar.setVisibility(View.INVISIBLE);
        progressBar = findView(R.id.progress_bar);
        rvFileList = findView(R.id.rv_file_list);
        rvFileList.setLayoutManager(new LinearLayoutManager(this));
        rvFilePath = findView(R.id.rv_file_path);
        rvFilePath.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    /**
     * 排序
     */
    private void sortFileModelsList() {
        // 文件排序
        FileUtils.sortFileModelList(showZipRarFileList, "fileName", true);
        fileListAdapter.replaceData(showZipRarFileList);
        fileListAdapter.notifyDataSetChanged();
        //路径刷新
    }

    /**
     * 解压全部文件
     *
     * @param selectedPath
     * @param password
     */
    public void unZipRarAllFile(final String selectedPath, final String password) {
        progressBar.setVisibility(View.VISIBLE);
        ThreadManager.getInstance().start(new Runnable() {
            @Override
            public void run() {
                String unAllFilePath = null;
                if (zipRarFilePath.endsWith("rar")) {
//                    unAllFilePath = UnRarManager.getInstance().unRarAllFile(zipRarFilePath,
//                            selectedPath, password);
                } else {
                    try {
                        unAllFilePath = UnZipManager.getInstance().unZipAllFile(zipRarFilePath,
                                selectedPath, password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Message msg = mHandler.obtainMessage();
                msg.what = HANDLER_UN_ALL_FILE;
                msg.obj = unAllFilePath;
                mHandler.sendMessage(msg);
            }
        });
    }


    /**
     * 弹出Dialog 让用户输入密码
     *
     * @param outFolerPath
     * @param fileModelPath
     */
    private void getUnZipPassword(final String outFolerPath, final String fileModelPath) {
        new PasswordDialog(this, R.style.DialogTheme, zipRarFilePath, outFolerPath, fileModelPath).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String selectedPath = data.getStringExtra("selectedPath");
            selectedPath = selectedPath + "/" + FileUtils.getFileNameNoExtension(zipRarFilePath);
            //判断是否有密码
            boolean hasPassword = false;
            if (zipRarFilePath.endsWith(".zip")) {
                hasPassword = UnZipManager.getInstance().checkZipFileHasPassword(zipRarFilePath);
            } else {
                //   hasPassword = UnRarManager.getInstance().checkRarFileHasPassword(new File(zipRarFilePath));
            }
            if (hasPassword) {
                getUnZipPassword(selectedPath, "");
            } else {
                unZipRarAllFile(selectedPath, "");
            }
        }
    }
}
