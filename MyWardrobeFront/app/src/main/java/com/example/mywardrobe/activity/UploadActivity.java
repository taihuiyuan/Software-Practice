package com.example.mywardrobe.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mywardrobe.R;
import com.example.mywardrobe.base.BaseActivity;
import com.example.mywardrobe.entity.Category;
import com.example.mywardrobe.entity.Location;
import com.example.mywardrobe.presenter.UploadPresenter;
import com.example.mywardrobe.utils.FileImageUpload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UploadActivity extends BaseActivity<UploadPresenter> {

    private static final int CHOOSE_PHOTO = 2;

    Button btnBack;

    private ImageView picture;
    private String imagePath;
    private Spinner spinnerCategoryName;
    private Spinner spinnerColor;
    private Spinner spinnerSeason;
    private Spinner spinnerLocation;

    private ArrayList<Category> categories = new ArrayList<>();
    private String[] seasons = {"春天","夏天","秋天","冬天"};
    private String[] colors = {"黑色系","白色系","灰色系","裸色系","红色系","橙色系","黄色系","绿色系","蓝色系","紫色系","其它"};
    private ArrayList<Location> locations = new ArrayList<>();

    Button chooseFromAlbum;
    String etCategoryName;
    String etColor;
    String etSeason;
    EditText etPrice;
    String etLocation;
    EditText etNote;
    Button btUpload;

    TextView addCategory;
    TextView addLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        initView();

        btnBack = findViewById(R.id.btnBack);

        spinnerCategoryName = findViewById(R.id.spinnerCategoryName);
        spinnerColor = findViewById(R.id.spinnerColor);
        spinnerSeason = findViewById(R.id.spinnerSeason);
        spinnerLocation = findViewById(R.id.spinnerLocation);

        addCategory = findViewById(R.id.addCategory);
        addLocation = findViewById(R.id.addLocation);

        etPrice = (EditText) findViewById(R.id.uploadPrice);
        etNote = (EditText) findViewById(R.id.uploadNote);
        etPrice.clearFocus();
        etNote.clearFocus();

        btUpload = (Button) findViewById(R.id.btUpload);

//        Bundle bundle = this.getIntent().getExtras();
//        String username = bundle.getString("username");
        SharedPreferences mSharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
        mPresenter.username = mSharedPreferences.getString("username", "");

        initSpinnerCategoryName();
        initSpinnerColorAndSeason();
        initSpinnerLocation();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);
        picture = findViewById(R.id.picture);
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(UploadActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UploadActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                else {
                    openAlbum();
                }
            }
        });

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(UploadActivity.this);
                final EditText editText = new EditText(UploadActivity.this);
                builder1.setView(editText);
                builder1.setTitle("新增分类");
                editText.setHint("请输入新增的分类名");
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText().length() > 15){
                            Toast.makeText(UploadActivity.this,"分类名不能超过15字",Toast.LENGTH_SHORT).show();
                        }else {
                            addCategory(editText.getText().toString());
                        }
                    }
                });
                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder1.show();
            }
        });

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(UploadActivity.this);
                final EditText editText = new EditText(UploadActivity.this);
                builder1.setView(editText);
                builder1.setTitle("新增位置");
                editText.setHint("请输入新增的位置名");
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText().length() > 10){
                            Toast.makeText(UploadActivity.this,"位置名不能超过10字",Toast.LENGTH_SHORT).show();
                        }else {
                            addLocation(editText.getText().toString());
                        }
                    }
                });
                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder1.show();
            }
        });

        spinnerCategoryName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                etCategoryName = categories.get(pos).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UploadActivity.this, "请选择分类", Toast.LENGTH_LONG).show();
            }
        });

        spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                etColor = colors[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UploadActivity.this, "请选择颜色", Toast.LENGTH_LONG).show();
            }
        });

        spinnerSeason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                etSeason = seasons[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UploadActivity.this, "请选择季节", Toast.LENGTH_LONG).show();
            }
        });

        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                etLocation = locations.get(pos).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UploadActivity.this, "请选择位置", Toast.LENGTH_LONG).show();
            }
        });


        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String price = etPrice.getText().toString();
                String note = etNote.getText().toString();
                if (price.equals("")){
                    price = "0";
                }
                mPresenter.categoryName = etCategoryName;
                mPresenter.color = etColor;
                mPresenter.season = etSeason;
                mPresenter.price = price;
                mPresenter.location = etLocation;
                mPresenter.note = note;
                doUpload();
            }
        });
    }

    private void initSpinnerColorAndSeason() {
        ArrayList<String> listForSpinner = new ArrayList<>(Arrays.asList(colors));
        ArrayAdapter<String> adapterForSpinner;
        adapterForSpinner = new ArrayAdapter<>(UploadActivity.this, R.layout.support_simple_spinner_dropdown_item, listForSpinner);
        spinnerColor.setAdapter(adapterForSpinner);

        ArrayList<String> listForSpinner2 = new ArrayList<>(Arrays.asList(seasons));
        ArrayAdapter<String> adapterForSpinner2;
        adapterForSpinner2 = new ArrayAdapter<>(UploadActivity.this, R.layout.support_simple_spinner_dropdown_item, listForSpinner2);
        spinnerSeason.setAdapter(adapterForSpinner2);
    }

    private void initSpinnerLocation() {
        mPresenter.listLocation(new UploadPresenter.ListLocationCallback() {
            @Override
            public void onListLocationSuccess(List<Location> dataList) {
                ArrayList<String> listForSpinner = new ArrayList<>();
                ArrayAdapter<String> adapterForSpinner;
                locations.clear();
                locations.addAll(dataList);
                for (Location c: locations){
                    if (c.getName().length()>15){
                        c.setName(c.getName().substring(0,15)+"...");
                    }
                    listForSpinner.add(c.getName());
                }
                adapterForSpinner = new ArrayAdapter<>(UploadActivity.this, R.layout.support_simple_spinner_dropdown_item, listForSpinner);
                spinnerLocation.setAdapter(adapterForSpinner);
            }

            @Override
            public void onListLocationFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initSpinnerCategoryName() {
        mPresenter.listCategory(new UploadPresenter.ListCategoryCallback() {
            @Override
            public void onListCategorySuccess(List<Category> dataList) {
                ArrayList<String> listForSpinner = new ArrayList<>();
                ArrayAdapter<String> adapterForSpinner;
                categories.clear();
                categories.addAll(dataList);
                for (Category c: categories){
                    listForSpinner.add(c.getName());
                }
                adapterForSpinner = new ArrayAdapter<>(UploadActivity.this, R.layout.support_simple_spinner_dropdown_item, listForSpinner);
                spinnerCategoryName.setAdapter(adapterForSpinner);
            }

            @Override
            public void onListCategoryFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload;
    }

    @Override
    public void initView() {
        mPresenter = new UploadPresenter();
        mPresenter.attachView(this);

    }


    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                }
                else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                handleImageOnKitKat(data);
                break;
            default:
                break;
        }
    }

    //4.4以上系统使用这个方法处理图片
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath  = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }
            else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri,则用普通方式处理
            imagePath = getImagePath(uri, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        //根据图片路径显示图片
        displayImage(imagePath);
    }

    //获取图片的路径
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和Selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
            chooseFromAlbum.setBackground(null);
        }
        else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }


    //向后端发送数据的方法
    private void doUpload() {
        //先上传图片，获得图片的url
        Log.e("uploadFile", "imagePath"+imagePath);
        if (imagePath == null){
            Toast.makeText(getApplicationContext(), "请选择图片", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] temp = imagePath.split("/");
        String fileName = temp[temp.length - 1];
        FileImageUpload fiu = new FileImageUpload();
        String msg = fiu.uploadFile(imagePath, fileName);
        Log.e("uploadFile", "imageUrl"+fiu.getImageUrl());
        if (fiu.getImageUrl().equals("")) {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
        else {
            mPresenter.imageUrl = fiu.getImageUrl();
            mPresenter.upload(new UploadPresenter.UploadCallback() {
                @Override
                public void onUploadSuccess(String message, int status) {
                    if (status == 200) {
                        Toast.makeText(getApplicationContext(), "上传成功！", Toast.LENGTH_SHORT).show();
                        toHome();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "上传失败"+message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onUploadFail(String msg) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    private void toHome() {
        Intent intent = new Intent(UploadActivity.this, NavbarActivity.class);
        startActivity(intent);
    }

    private void addCategory(String categoryName) {
        mPresenter.newCategoryName = categoryName;
        mPresenter.addCategory(new UploadPresenter.AddCategoryCallback() {
            @Override
            public void onAddCategorySuccess(String message, int status) {
                if (status == 200) {
                    Toast.makeText(getApplicationContext(), "添加成功！", Toast.LENGTH_SHORT).show();
                    initSpinnerCategoryName();
                }
                else {
                    Toast.makeText(getApplicationContext(), "添加失败："+message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAddCategoryFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addLocation(String location) {
        mPresenter.newLocation = location;
        mPresenter.addLocation(new UploadPresenter.AddLocationCallback() {
            @Override
            public void onAddLocationSuccess(String message, int status) {
                if (status == 200) {
                    Toast.makeText(getApplicationContext(), "添加成功！", Toast.LENGTH_SHORT).show();
                    initSpinnerLocation();
                }
                else {
                    Toast.makeText(getApplicationContext(), "添加失败："+message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAddLocationFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void back(){
        UploadActivity.this.finish();
    }
}
