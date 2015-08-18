package com.kz.redminesweeper;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kz.redminesweeper.bean.Account;
import com.kz.redminesweeper.bean.User;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_account_settings)
@OptionsMenu(R.menu.main)
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
    ImageButton startButton;

    @ViewById
    TextView startLabel;

    @ViewById
    TextView urlErrorLabel;

    @Extra
    Account account;

    @AfterViews
    void setUp() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        onChangeInputValue();
        urlText.setText(account.getRootUrl());
        loginIdText.setText(account.getLoginId());
        passwordText.setText(account.getPassword());
        savePasswordCheck.setChecked(account.isSavePassword());
    }

    @TextChange({R.id.url_text, R.id.login_id_text, R.id.password_text})
    void onChangeInputValue() {
        boolean enabled = isStart();
        int visibility = enabled ? View.VISIBLE : View.INVISIBLE;
        startButton.setVisibility(visibility);
        startButton.setEnabled(enabled);
        startLabel.setVisibility(visibility);
        startLabel.setEnabled(enabled);
        urlErrorLabel.setText("");
    }

    @Background
    @Click({R.id.start_button, R.id.start_label})
    void authenticate() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        account.setRootUrl(urlText.getText().toString());
        account.setLoginId(loginIdText.getText().toString());
        account.setPassword(passwordText.getText().toString());
        account.setSavePassword(savePasswordCheck.isChecked());
        if (app.getAccount() == null) account.setEnable(true);
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
        app.addAccount(account);
        saveAccount();
        finish();
    }

    @UiThread
    void authFailed() {
        urlErrorLabel.setText("error");
    }


    private void saveAccount() {
        String password = account.getPassword();
        if (!account.isSavePassword()) {
            account.setPassword("");
        }
        app.saveAccounts();
        account.setPassword(password);
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
