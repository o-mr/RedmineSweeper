package com.kz.redminesweeper;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kz.redminesweeper.account.Account;
import com.kz.redminesweeper.bean.User;
import com.kz.redminesweeper.view.BlankWall;
import com.kz.redminesweeper.view.BlankWall_;

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

    BlankWall title;

    @ViewById
    LinearLayout baseLayout;

    @ViewById
    EditText urlText;

    @ViewById
    EditText loginIdText;

    @ViewById
    EditText passwordText;

    @ViewById
    CheckBox savePasswordCheck;

    @ViewById
    Button signInButton;

    @ViewById
    LinearLayout editButtonGroup;

    @ViewById
    Button deleteButton;

    @ViewById
    TextView urlErrorLabel;

    @Extra
    Account account;

    @Extra
    int modeInt;

    Mode mode;

    private ProgressDialog progressDialog;

    @AfterViews
    void setUp() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        mode = Mode.values()[modeInt];
        account = (account == null) ? app.getAccountManager().getEnableAccount() : account;
        if (account == null) {
            mode = Mode.FIRST;
            account = new Account();
        }
        bind(account.clone());
        createProgressDialog();
        switch (mode) {
            case SIGN_IN: setupSignIn(); break;
            case ADD: setupAdd(); break;
            case EDIT: setupEdit(); break;
            case FIRST: setUpFirst(); break;
        }
        onChangeInputValue();
    }

    private void setupSignIn() {
        showTitle();
        signInButton.setVisibility(View.VISIBLE);
        editButtonGroup.setVisibility(View.GONE);
        if (account.getPassword().length() == 0) {
            passwordText.requestFocus();
            title.setTimer(1000);
        } else {
            onClickSignIn();
        }
    }

    private void setupAdd() {
        signInButton.setVisibility(View.VISIBLE);
        editButtonGroup.setVisibility(View.GONE);
    }

    private void setupEdit() {
        signInButton.setVisibility(View.GONE);
        editButtonGroup.setVisibility(View.VISIBLE);
        deleteButton.setEnabled(!account.isEnable());
    }

    private void setUpFirst() {
        showWelcome();
        signInButton.setVisibility(View.VISIBLE);
        editButtonGroup.setVisibility(View.GONE);
    }

    @Click({R.id.sign_in_button, R.id.update_button})
    void onClickSignIn() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        progressDialog.show();
        account.setRootUrl(urlText.getText().toString());
        account.setLoginId(loginIdText.getText().toString());
        account.setPassword(passwordText.getText().toString());
        account.setSavePassword(savePasswordCheck.isChecked());
        authenticate();
    }

    @Click({R.id.delete_button})
    void deleteAccount() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        new AlertDialog.Builder(this)
        .setTitle(R.string.dialog_title_delete_account)
        .setMessage(R.string.dialog_msg_delete_account)
        .setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                app.getAccountManager().removeAccount(account);
                startMainActivity();
            }
        })
         .setNegativeButton(R.string.dialog_button_cancel, null)
         .create().show();
    }

    @Background
    void authenticate() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        try {
            app.setUpRedmineRestService(account);
            User user = app.redmine.getMyUserInfo().getUser();
            user.getName().length(); //NPE
            account.setUser(user);
            account.setEnable(true);
            authSuccessful();
        } catch (Exception e) {
            Log.e(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), e);
            authFailed();
        }
    }

    @UiThread
    void authSuccessful() {
        app.getAccountManager().changeEnableAccount(account);
        progressDialog.dismiss();
        startMainActivity();
    }

    @UiThread
    void authFailed() {
        urlErrorLabel.setText("error");
        progressDialog.dismiss();
        title.hide();
    }

    void startMainActivity() {
        Intent intent = new Intent(this, MainActivity_.class);
        startActivityForResult(intent, 1);
        finish();
    }

    @TextChange({R.id.url_text, R.id.login_id_text, R.id.password_text})
    void onChangeInputValue() {
        boolean enabled = isInputCompletion();
        signInButton.setEnabled(enabled);
        urlErrorLabel.setText("");
    }

    private boolean isInputCompletion() {
        String url = urlText.getText().toString();
        String loginId = loginIdText.getText().toString();
        String password = passwordText.getText().toString();
        return url.length()  > 0 && loginId.length()  > 0 && password.length() > 0;
    }

    private void createProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.dialog_msg_dialog_auth));
        progressDialog.setCancelable(true);
    }

    private void showWelcome() {
        getSupportActionBar().hide();
        title = BlankWall_.build(this);
        title.setTitle(R.string.label_start_redmine_sweeper);
        title.setSubTitle(R.string.label_start_redmine_sweeper_sub);
        title.setClickHide(true);
        title.show(baseLayout);
    }

    private void showTitle() {
        getSupportActionBar().hide();
        title = BlankWall_.build(this);
        title.show(baseLayout);
    }

    private void bind(Account account) {
        urlText.setText(account.getRootUrl());
        loginIdText.setText(account.getLoginId());
        passwordText.setText(account.getPassword());
        savePasswordCheck.setChecked(account.isSavePassword());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mode == Mode.SIGN_IN || mode == Mode.FIRST) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.dialog_title_rms_finish)
                .setMessage(R.string.dialog_msg_rms_finish)
                .setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);}})
                .setNegativeButton(R.string.dialog_button_cancel, null)
                .create().show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public enum Mode {
        SIGN_IN, ADD, EDIT, FIRST,
    }

}
