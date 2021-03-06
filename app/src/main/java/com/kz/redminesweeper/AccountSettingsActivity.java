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
import com.kz.redminesweeper.rest.RedmineAccess;
import com.kz.redminesweeper.view.BlankWall;
import com.kz.redminesweeper.view.BlankWall_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_account_settings)
public class AccountSettingsActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1001;

    @App
    RmSApplication app;

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

    @Extra
    Account account;

    @Extra
    int modeInt;

    @Extra
    int msgId;

    Mode mode;

    private BlankWall title;

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
        onChangeInputValue();
        switch (mode) {
            case SIGN_IN: setupSignIn(); break;
            case SWITCH: setupSwitch(); break;
            case ADD: setupAdd(); break;
            case EDIT: setupEdit(); break;
            case FIRST: setUpFirst(); break;
            case ERROR: setUpError(); break;
        }
    }

    private void setupSignIn() {
        showTitle();
        signInButton.setVisibility(View.VISIBLE);
        editButtonGroup.setVisibility(View.GONE);
        if (account.getPassword().length() > 0) {
            onClickSignIn();
        } else {
            title.setTimer(1000);
        }
    }

    private void setupSwitch() {
        signInButton.setVisibility(View.VISIBLE);
        editButtonGroup.setVisibility(View.GONE);
        if (account.getPassword().length() > 0) {
            onClickSignIn();
        }
    }

    private void setupAdd() {
        signInButton.setVisibility(View.VISIBLE);
        editButtonGroup.setVisibility(View.GONE);
    }

    private void setupEdit() {
        account.setUser(null);
        signInButton.setVisibility(View.GONE);
        editButtonGroup.setVisibility(View.VISIBLE);
        deleteButton.setEnabled(!account.isEnable());
    }

    private void setUpFirst() {
        showWelcome();
        signInButton.setVisibility(View.VISIBLE);
        editButtonGroup.setVisibility(View.GONE);
    }

    private void setUpError() {
        showError();
        account.setUser(null);
        signInButton.setVisibility(View.VISIBLE);
        editButtonGroup.setVisibility(View.GONE);
    }

    @Click({R.id.sign_in_button, R.id.update_button})
    void onClickSignIn() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        clearError();
        progressDialog.show();
        account.setRootUrl(urlText.getText().toString());
        account.setLoginId(loginIdText.getText().toString());
        account.setPassword(passwordText.getText().toString());
        account.setSavePassword(savePasswordCheck.isChecked());

        app.getAccountManager().authenticate(account, new RedmineAccess.RestResultListener<Account>() {
            @Override
            public void onSuccessful(Account result) {
                authSuccessful();
            }

            @Override
            public void onFailed(int msgId, Throwable e) {
                authFailed(msgId);
            }
        });
    }

    @UiThread
    void authSuccessful() {
        progressDialog.dismiss();
        startMainActivity();
    }

    @UiThread
    void authFailed(int msgId) {
        String msg = getString(msgId);
        if (msgId == R.string.label_msg_error_network ||
                msgId == R.string.label_msg_error_rest_timeout ||
                msgId == R.string.label_msg_error_page_not_found ||
                msgId == R.string.label_msg_error_rest_failed) {
            urlText.setError(msg);
            urlText.requestFocus();
        } else if (msgId == R.string.label_msg_error_auth_failed) {
            loginIdText.setError(msg);
            passwordText.setError(msg);
            loginIdText.requestFocus();
        }
        progressDialog.dismiss();
        if (mode == Mode.SIGN_IN || mode == Mode.ERROR) {
            title.hide();
        }
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

    private void clearError() {
        urlText.setError(null);
        loginIdText.setError(null);
        passwordText.setError(null);
    }

    void startMainActivity() {
        if (mode == Mode.SIGN_IN || mode == Mode.FIRST || mode == Mode.ERROR) {
            Intent intent = new Intent(this, MainActivity_.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @TextChange({R.id.url_text, R.id.login_id_text, R.id.password_text})
    void onChangeInputValue() {
        boolean enabled = isInputCompletion();
        signInButton.setEnabled(enabled);
        clearError();
    }

    private boolean isInputCompletion() {
        String url = urlText.getText().toString();
        String loginId = loginIdText.getText().toString();
        String password = passwordText.getText().toString();
        return url.length() > 0 && loginId.length() > 0 && password.length() > 0;
    }

    private void createProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.dialog_msg_dialog_auth));
        progressDialog.setCancelable(false);
    }

    private void showWelcome() {
        title = BlankWall_.build(this);
        title.setTitle(R.string.label_start_redmine_sweeper);
        title.setSubTitle(R.string.label_start_redmine_sweeper_sub);
        title.setClickHide(true);
        title.setHideActionBar(true);
        title.show(baseLayout);
    }

    private void showTitle() {
        title = BlankWall_.build(this);
        title.setHideActionBar(true);
        title.show(baseLayout);
    }

    private void showError() {
        title = BlankWall_.build(this);
        title.setTitle(msgId, 20);
        title.setSubTitle(R.string.label_msg_error_retry);
        title.setHideActionBar(true);
        title.setWallColor(R.color.bg_error);
        title.setBlankWallCallBacks(new BlankWall.BlankWallCallBacks() {
            @Override
            public void onClick() {
                onClickSignIn();
            }
        });
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
            if (mode == Mode.SIGN_IN || mode == Mode.FIRST || mode == Mode.ERROR) {
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
        SIGN_IN, SWITCH, ADD, EDIT, FIRST, ERROR
    }

}
