package com.app.sanyou.view.viewpager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.sanyou.R;
import com.app.sanyou.common.CallListener;
import com.app.sanyou.common.JsonResult;
import com.app.sanyou.constants.Request;
import com.app.sanyou.utils.HttpUtil;
import com.app.sanyou.utils.StringUtil;
import com.app.sanyou.utils.UserUtil;
import com.app.sanyou.view.login.LoginActivity;
import com.app.sanyou.zxing.android.CaptureActivity;

public class ScanFragment extends Fragment {

    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;

    private TextView search_btn;
    private EditText query_text;
    private ImageView ivDelete;

    private Button btn_scan;
    private TextView tv_scanResult;

    private Context context;
    private TabViewPagerActivity tabViewPagerActivity;

    private String scanCode;

    private Handler handler = new Handler();

    private CallListener checkExistListener = new CallListener() {
        @Override
        public void success(JsonResult result) {
            Intent intent = new Intent(context, ScanResultActivity.class);
            intent.putExtra("scanCode",scanCode);
            intent.putExtra("tag","1");
            startActivity(intent);
        }

        @Override
        public void failure(JsonResult result) {
            getActivity().runOnUiThread(()->Toast.makeText(context,"??????????????????!",Toast.LENGTH_SHORT).show());
        }
    };

    public static ScanFragment getInstance(Context context,TabViewPagerActivity tabViewPagerActivity) {
        ScanFragment scanFragment = new ScanFragment();
        scanFragment.context = context;
        scanFragment.tabViewPagerActivity = tabViewPagerActivity;
        return scanFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scan_fragment,null);

        //?????????view
        initView(view);
        //?????????????????????
        initClickListener();

        return view;
    }

    /**
     * ?????????view
     */
    private void initView(View view){
//        tv_scanResult = view.findViewById(R.id.tv_scanResult);
//        btn_scan = view.findViewById(R.id.btn_scan);

        search_btn = view.findViewById(R.id.search_btn);
        query_text = view.findViewById(R.id.query_text);
        ivDelete = view.findViewById(R.id.ivDelete);
    }

    /**
     * ?????????????????????
     */
    private void initClickListener(){
        //????????????????????????
//        btn_scan.setOnClickListener(v -> {
//            //??????????????????
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(tabViewPagerActivity, new String[]{Manifest.permission.CAMERA}, 1);
//            } else {
//                goScan();
//            }
//        });

        //?????????????????????????????????
        ivDelete.setOnClickListener(v->{
            query_text.setText("");
            ivDelete.setVisibility(View.GONE);
        });

        //EditText????????????
        query_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0)
                    ivDelete.setVisibility(View.GONE);
                else{
                    ivDelete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //???????????????????????????
        search_btn.setOnClickListener(v->{
            scanCode = query_text.getText().toString();
            if(StringUtil.isNull(scanCode)){
                Toast.makeText(context,"???????????????????????????!",Toast.LENGTH_SHORT).show();
            }else{
                String userId = UserUtil.getUserId(context);
                if(StringUtil.isNull(userId)){
                    UserUtil.loginOut(context);
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                }

                //???????????????????????????
                HttpUtil.get(Request.URL + "/app/product/checkExist?code=" + scanCode,checkExistListener);
            }
        });
    }

    /**
     * ???????????????????????????
     */
    private void goScan(){
        Intent intent = new Intent(context, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goScan();
                } else {
                    Toast.makeText(context, "???????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //???????????????/????????????
        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                //?????????????????????
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                //?????????BitMap??????
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);

                tv_scanResult.setText("???????????????????????????" + content);
            }
        }
    }

}
