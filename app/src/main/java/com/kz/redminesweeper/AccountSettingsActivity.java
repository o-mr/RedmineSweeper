package com.kz.redminesweeper;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.kz.redminesweeper.account.Account;
import com.kz.redminesweeper.bean.User;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_account_settings)
public class AccountSettingsActivity extends AppCompatActivity {

    @App
    RmSApplication app;

    @ViewById
    EditText urlText;

    @ViewById
    EditText loginIdText;

    @ViewById
    EditText passwordText;

    @ViewById
    CheckBox savePasswordCheck;

    @ViewById
    Button startButton;

    @ViewById
    TextView urlErrorLabel;

    @Extra
    Account account;

    private ProgressDialog progressDialog;

    @AfterViews
    void setUp() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        onChangeInputValue();
        urlText.setText(account.getRootUrl());
        loginIdText.setText(account.getLoginId());
        passwordText.setText(account.getPassword());
        savePasswordCheck.setChecked(account.isSavePassword());
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    @TextChange({R.id.url_text, R.id.login_id_text, R.id.password_text})
    void onChangeInputValue() {
        boolean enabled = isStart();
        startButton.setEnabled(enabled);
        urlErrorLabel.setText("");
    }

    @Click({R.id.start_button})
    void clickStart() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        account.setEnable(true);
        progressDialog.setMessage(getString(R.string.progress_dialog_auth_msg));
        progressDialog.setCancelable(true);
        progressDialog.show();
        account.setRootUrl(urlText.getText().toString());
        account.setLoginId(loginIdText.getText().toString());
        account.setPassword(passwordText.getText().toString());
        account.setSavePassword(savePasswordCheck.isChecked());
        authenticate();
    }

    @Background
    void authenticate() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        try {
            app.setUpRedmineRestService(account);
            User user = app.redmine.getMyUserInfo().getUser();
            user.getName().length(); //NPE
            account.setUser(user);
            authSuccessful();
        } catch (Exception e) {
            Log.e(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), e);
            authFailed();
        }
    }

    @UiThread
    void authSuccessful() {
        app.getAccountManager().putAccount(account);
        progressDialog.dismiss();
        finish();
    }

    @UiThread
    void authFailed() {
        urlErrorLabel.setText("error");
        progressDialog.dismiss();
    }

    private boolean isStart() {
        String url = urlText.getText().toString();
        String loginId = loginIdText.getText().toString();
        String password = passwordText.getText().toString();
        return url.length()  > 0 && loginId.length()  > 0 && password.length() > 0;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode != KeyEvent.KEYCODE_BACK && super.onKeyDown(keyCode, event);
    }
}
