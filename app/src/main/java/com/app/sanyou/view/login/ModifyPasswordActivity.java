package com.app.sanyou.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.sanyou.R;
import com.app.sanyou.common.CallListener;
import com.app.sanyou.common.JsonResult;
import com.app.sanyou.constants.Request;
import com.app.sanyou.utils.HttpUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ModifyPasswordActivity extends AppCompatActivity {

    private ImageView back_img;
    private TextView back_text;
    private TextView title_text;

    private EditText usernameText;
    private EditText passwordText;
    private EditText confirmPasswordText;
    private Button modifyPasswordBtn;

    private String username;
    private String password;
    private String confirmPassword;

    private ImageView ivDeleteName;
    private ImageView ivDeletePwd;
    private ImageView ivDeleteConfirmPwd;

    private CallListener listener = new CallListener() {
        @Override
        public void success(JsonResult result) {
            ModifyPasswordActivity.this.runOnUiThread(()->Toast.makeText(getApplicationContext(),"密码修改成功!",Toast.LENGTH_SHORT).show());
            Intent intent = new Intent(ModifyPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        @Override
        public void failure(JsonResult result) {
            ModifyPasswordActivity.this.runOnUiThread(()->Toast.makeText(getApplicationContext(),"密码修改失败,请联系管理员!",Toast.LENGTH_SHORT).show());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modify_password);

        initView();
        //初始化点击事件
        initClickListener();
    }

    private void initView(){
        //初始化顶部栏
        back_img = findViewById(R.id.back_img);
        back_text = findViewById(R.id.back_text);
        title_text = findViewById(R.id.title_text);
        title_text.setText("修改密码");

        usernameText = findViewById(R.id.username_text);
        passwordText = findViewById(R.id.password_text);
        confirmPasswordText = findViewById(R.id.confirm_password_text);
        modifyPasswordBtn = findViewById(R.id.modify_password_btn);

        ivDeleteName = findViewById(R.id.ivDeleteName);
        ivDeletePwd = findViewById(R.id.ivDeletePwd);
        ivDeleteConfirmPwd = findViewById(R.id.ivDeleteConfirmPwd);
    }

    private void initClickListener(){
        //返回
        back_img.setOnClickListener(v->{
            finish();
        });
        back_text.setOnClickListener(v->{
            finish();
        });

        usernameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0)
                    ivDeleteName.setVisibility(View.GONE);
                else
                    ivDeleteName.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ivDeleteName.setOnClickListener(v->{
            usernameText.setText("");
            ivDeleteName.setVisibility(View.GONE);
        });

        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0)
                    ivDeletePwd.setVisibility(View.GONE);
                else
                    ivDeletePwd.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ivDeletePwd.setOnClickListener(v->{
            passwordText.setText("");
            ivDeletePwd.setVisibility(View.GONE);
        });


        confirmPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0)
                    ivDeleteConfirmPwd.setVisibility(View.GONE);
                else
                    ivDeleteConfirmPwd.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ivDeleteConfirmPwd.setOnClickListener(v->{
            confirmPasswordText.setText("");
            ivDeleteConfirmPwd.setVisibility(View.GONE);
        });


        modifyPasswordBtn.setOnClickListener(view -> {
            username = usernameText.getText().toString();
            password = passwordText.getText().toString();
            confirmPassword = confirmPasswordText.getText().toString();

            if(TextUtils.isEmpty(username)){
                Toast.makeText(getApplicationContext(),"用户名不能为空!",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)){
                Toast.makeText(getApplicationContext(),"密码不能为空!",Toast.LENGTH_SHORT).show();
            }else if(!password.equals(confirmPassword)){
                Toast.makeText(getApplicationContext(),"两次输入的密码不同!",Toast.LENGTH_SHORT).show();
            }else{
                JsonResult result = new JsonResult();
                Map<String,String> postData = new HashMap<>();
                postData.put("username",username);
                postData.put("password",password);
                Gson gson = new Gson();
                String json = gson.toJson(postData);
                HttpUtil.post(Request.URL + "/user/updatePwd",json,listener);
            }
        });
    }
}