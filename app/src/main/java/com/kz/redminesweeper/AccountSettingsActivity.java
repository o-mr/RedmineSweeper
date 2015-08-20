package com.kz.redminesweeper;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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

    BlankWall welcome;

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
        onChangeInputValue();
        mode = Mode.values()[modeInt];
        switch (mode) {
            case ADD_FIRST:
                welcome = BlankWall_.build(this);
                welcome.setTitle(R.string.label_start_redmine_sweeper);
                welcome.setSubTitle(R.string.label_start_redmine_sweeper_sub);
                welcome.setClickHide(true);
                welcome.show(baseLayout);
            case ADD :
            case SIGN_IN:
                signInButton.setVisibility(View.VISIBLE);
                editButtonGroup.setVisibility(View.GONE);
                break;
            case EDIT:
                signInButton.setVisibility(View.GONE);
                editButtonGroup.setVisibility(View.VISIBLE);
                deleteButton.setEnabled(!account.isEnable());
        }

        account = account.clone();
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
        signInButton.setEnabled(enabled);
        urlErrorLabel.setText("");
    }

    @Click({R.id.sign_in_button, R.id.update_button})
    void clickStart() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        progressDialog.setMessage(getString(R.string.dialog_msg_dialog_auth));
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
            account.setEnable(true);
            updateAccount();
        } catch (Exception e) {
            Log.e(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), e);
            authFailed();
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
                        finish();
                    }})
                .setNegativeButton(R.string.dialog_button_cancel, null)
                .create().show();
    }

    @UiThread
    void updateAccount() {
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
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (mode) {
                case ADD_FIRST:
                case SIGN_IN:
                    new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_title_rms_finish)
                    .setMessage(R.string.dialog_msg_rms_finish)
                    .setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {moveTaskToBack(true);}})
                    .setNegativeButton(R.string.dialog_button_cancel, null)
                    .create().show();
                case ADD:
                case EDIT:
                default:
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public enum Mode {
        ADD_FIRST,
        ADD,
        EDIT,
        SIGN_IN,
    }

}
