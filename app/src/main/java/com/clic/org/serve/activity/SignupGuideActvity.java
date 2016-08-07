package com.clic.org.serve.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clic.org.R;
import com.clic.org.serve.Utils.ClicUtils;
import com.clic.org.serve.Utils.JsonUtils;
import com.clic.org.serve.constants.ClicConstants;
import com.clic.org.serve.data.Address;
import com.clic.org.serve.data.Customer;
import com.clic.org.serve.data.OTP;
import com.clic.org.serve.data.OtpValidation;
import com.clic.org.serve.data.UserItem;
import com.clic.org.serve.data.UserItemsResponse;
import com.clic.org.serve.listener.ServiceListener;
import com.clic.org.serve.services.ServiceConstants;
import com.clic.org.serve.services.ServiceUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignupGuideActvity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout,layoutOR,layoutSignup;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip,btnGuest,btnRegister,btnLogin;
    private View register_layout;
    private EditText inputName, inputEmail, inputPassword,inputMobile;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword,inputLayoutMobile;
    private Button btnAddress;
    private String SERVICE_TYPE;
    private boolean mobileValidity = false;
    TextView txt_signUp,txt_signup_des,toollBarTitle;
    Customer mCustomer ;
    OTP mOtp;
    Address mAddress = new Address();
    private ImageView imagLogo;
    Toolbar toolbar;
    boolean singinCheck = true;
    private UserItem mUserItem;
    String type="";
    LoginButton facebookLoginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.clic.org.serve",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("debug", "keyhash"+Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/
        setContentView(R.layout.activity_signup);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toollBarTitle = (TextView)toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        callbackManager = CallbackManager.Factory.create();

        mCustomer = new Customer();
        if(ClicUtils.readPreference(this, R.string.clic_ClientID) != null)
        {
            finish();
            Intent knowMore = new Intent(SignupGuideActvity.this,ClicServeHome.class);
            knowMore.putExtra(getString(R.string.activity_type), getString(R.string.activity_know_more));
            startActivity(knowMore);
        }

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnGuest = (Button) findViewById(R.id.btn_guest);
        register_layout =(View)findViewById(R.id.layout_register);
        register_layout.setVisibility(View.GONE);
        imagLogo = (ImageView)findViewById(R.id.clic_logo);
        btnLogin = (Button) findViewById(R.id.btn_login);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.help_slide_one,
                R.layout.help_slide_two,
                R.layout.help_slide_three};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        //changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        inputLayoutName = (TextInputLayout) register_layout.findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) register_layout.findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) register_layout.findViewById(R.id.input_layout_password);
        inputLayoutMobile = (TextInputLayout) register_layout.findViewById(R.id.input_layout_mobile);
        layoutOR = (LinearLayout) register_layout.findViewById(R.id.layout_OR);

        inputName = (EditText) register_layout.findViewById(R.id.input_name);
        inputEmail = (EditText) register_layout.findViewById(R.id.input_email);
        inputPassword = (EditText) register_layout.findViewById(R.id.input_password);
        inputMobile = (EditText) register_layout.findViewById(R.id.input_mobile);
        btnRegister = (Button) register_layout.findViewById(R.id.btn_register);
        btnAddress = (Button) register_layout.findViewById(R.id.btn_address);
        txt_signUp = (TextView) register_layout.findViewById(R.id.txt_singIN);
        txt_signup_des = (TextView) register_layout.findViewById(R.id.txt_singup_des);
        layoutSignup = (LinearLayout) register_layout.findViewById(R.id.layout_signup);

        facebookLoginButton = (LoginButton)register_layout.findViewById(R.id.login_button);
        btnRegister.setTag(getString(R.string.txt_signin));
        txt_signUp.setTag(getString(R.string.txt_signup));
        txt_signup_des.setText(getString(R.string.txt_signup_des));
        txt_signUp.setText(getString(R.string.txt_signup));

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        if(getIntent().getExtras() != null)
        {
            type = ClicConstants.GUEST_USER;
            mUserItem = getIntent().getExtras().getParcelable(getString(R.string.user_item));
            toolbar.setVisibility(View.VISIBLE);
            dotsLayout.setVisibility(View.INVISIBLE);
            viewPager.setVisibility(View.GONE);
            register_layout.setVisibility(View.VISIBLE);
            inputLayoutName.setVisibility(View.GONE);
            inputLayoutEmail.setVisibility(View.GONE);
            //btnAddress.setVisibility(View.GONE);
            btnSkip.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);
            btnGuest.setVisibility(View.INVISIBLE);
            layoutElementsSingUp();
            singinCheck = false;

        }
        //register_layout.setVisibility(View.GONE);
        inputMobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if(!singinCheck) {
                        SERVICE_TYPE = ClicConstants.MOIBILE_CHECK_SERVICE;
                        ServiceUtils.makeJSONObjectReq(SignupGuideActvity.this, ServiceConstants.MOBILE_VALIDATION + inputMobile.getText().toString(), myServiceListner, null);
                    }
                }
                return false;
            }
        });
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code

                                try {
                                    inputName.setText(object.getString("name"));
                                    finish();
                                    Intent knowMore = new Intent(SignupGuideActvity.this, ClicServeHome.class);
                                    knowMore.putExtra(getString(R.string.activity_type), getString(R.string.activity_know_more));
                                    startActivity(knowMore);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        MenuItem menuIcon = menu.findItem(R.id.action_search);
        menuIcon.setVisible(false);


        /*searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home)
        {
            if(txt_signUp.getTag().toString().equalsIgnoreCase(getString(R.string.txt_signup))) {
                layoutElementsSingUp();

            }
            else
            {
               layoutElementsSignIn();

            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    public void onButtonClic(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_login:
                toolbar.setVisibility(View.VISIBLE);
                dotsLayout.setVisibility(View.INVISIBLE);
                viewPager.setVisibility(View.GONE);
                register_layout.setVisibility(View.VISIBLE);
                inputLayoutName.setVisibility(View.GONE);
                inputLayoutEmail.setVisibility(View.GONE);
               //btnAddress.setVisibility(View.GONE);
                layoutOR.setVisibility(View.VISIBLE);
                btnSkip.setVisibility(View.INVISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);
                toollBarTitle.setText(getString(R.string.txt_signin));
                break;
            case R.id.btn_guest:
                ClicUtils.createPreferences(getApplicationContext(), getString(R.string.clic_guest), R.string.clic_usertype);
                //startActivity(new Intent(SignupGuideActvity.this, MainActivityClic.class));
                Intent knowMore = new Intent(SignupGuideActvity.this,ClicServeHome.class);
                knowMore.putExtra(getString(R.string.activity_type), getString(R.string.activity_know_more));
                startActivity(knowMore);
                break;
            case R.id.btn_register:
                if(btnRegister.getTag().toString().equalsIgnoreCase(getString(R.string.txt_signup))) {
                    submitForm();
                }else
                {
                    userLogin();
                }
                break;
            case R.id.btn_skip:
                viewPager.setVisibility(View.GONE);
                register_layout.setVisibility(View.VISIBLE);
                inputLayoutName.setVisibility(View.GONE);
                inputLayoutEmail.setVisibility(View.GONE);
               // btnAddress.setVisibility(View.GONE);
                btnSkip.setVisibility(View.INVISIBLE);
                break;
            case R.id.txt_singIN:
               // hideLayout();

                if(txt_signUp.getTag().toString().equalsIgnoreCase(getString(R.string.txt_signup))) {
                   layoutElementsSingUp();
                    singinCheck = false;
                }
                else
                {
                    singinCheck  = true;
                   layoutElementsSignIn();
                }
                break;
            case R.id.btn_address:
                ClicUtils.createAddressDialog(this,R.layout.clic_address_layout,mAddress);
                break;

        }
    }
    public void layoutElementsSingUp()
    {
        inputLayoutMobile.setVisibility(View.VISIBLE);
        inputLayoutPassword.setVisibility(View.VISIBLE);
        inputLayoutName.setVisibility(View.VISIBLE);
        inputLayoutEmail.setVisibility(View.VISIBLE);
       // btnAddress.setVisibility(View.VISIBLE);
        layoutOR.setVisibility(View.VISIBLE);
        facebookLoginButton.setVisibility(View.VISIBLE);
        btnRegister.setTag(getString(R.string.txt_signup));
        txt_signUp.setTag(getString(R.string.txt_signin));
        txt_signup_des.setText(getString(R.string.txt_signin_des));
        txt_signUp.setText(getString(R.string.txt_signin));
        imagLogo.setVisibility(View.GONE);
        toollBarTitle.setText(getString(R.string.txt_signup));
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void layoutElementsSignIn()
    {
        imagLogo.setVisibility(View.VISIBLE);
        inputLayoutMobile.setVisibility(View.VISIBLE);
        inputLayoutPassword.setVisibility(View.VISIBLE);
        inputLayoutName.setVisibility(View.GONE);
        inputLayoutEmail.setVisibility(View.GONE);
       // btnAddress.setVisibility(View.GONE);
        layoutOR.setVisibility(View.VISIBLE);
        facebookLoginButton.setVisibility(View.VISIBLE);
        btnRegister.setTag(getString(R.string.txt_signin));
        txt_signUp.setTag(getString(R.string.txt_signup));
        txt_signup_des.setText(getString(R.string.txt_signup_des));
        txt_signUp.setText(getString(R.string.txt_signup));
        toollBarTitle.setText(getString(R.string.txt_signin));
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
            } else {
                // still pages are left
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    /**
     * Validating form
     */
    private void userLogin()
    {
        if (!validateMobileNumber()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        mCustomer.setPassword(inputPassword.getText().toString());
        mCustomer.setPhoneNumber(inputMobile.getText().toString());

        SERVICE_TYPE = ClicConstants.CUSTOMER_LOGIN;

        ServiceUtils.postJsonObjectRequest(SignupGuideActvity.this,
                ServiceConstants.CUSTOMER_LOGIN,myServiceListner,JsonUtils.getJsonString(mCustomer));

    }
    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        if(!mobileValidity)
        {
            inputLayoutMobile.setError(getString(R.string.err_msg_mobile));
            return;
        }

        mCustomer.setCustomerName(inputName.getText().toString());
        mCustomer.setPassword(inputPassword.getText().toString());
        mCustomer.setEmailID(inputEmail.getText().toString());
        mCustomer.setPhoneNumber(inputMobile.getText().toString());
        SERVICE_TYPE = ClicConstants.CUSTOMER_SERVICE;

        ServiceUtils.postJsonObjectRequest(SignupGuideActvity.this,
                ServiceConstants.CREATE_CUSTOMER,myServiceListner,JsonUtils.getJsonString(mCustomer));

    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateMobileNumber() {
        if (inputMobile.getText().toString().length() <10) {
            inputLayoutMobile.setError(getString(R.string.err_msg_mobile_limit));
            requestFocus(inputMobile);
            return false;
        } else {
            inputLayoutMobile.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }






    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
                case R.id.input_mobile:
                    SERVICE_TYPE = ClicConstants.MOIBILE_CHECK_SERVICE;
                    ServiceUtils.makeJSONObjectReq(SignupGuideActvity.this, ServiceConstants.MOBILE_VALIDATION+inputMobile.getText().toString(), myServiceListner, inputMobile.getText().toString());
                    break;

            }
        }
    }

    ServiceListener myServiceListner = new ServiceListener() {

        @Override
        public void onServiceResponse(String response) {

            if(response.equalsIgnoreCase("OTP"))
            {
                return;
            }
            if(!SERVICE_TYPE.equalsIgnoreCase(ClicConstants.MOIBILE_CHECK_SERVICE))
            {
                if(response.contains("errorCode"))
                {
                    if(SERVICE_TYPE.equalsIgnoreCase(ClicConstants.CUSTOMER_LOGIN))
                    {
                        ClicUtils.displayToast(SignupGuideActvity.this,"Invalid Credentials..");
                        return;
                    }
                    ClicUtils.displayToast(SignupGuideActvity.this, "Error Response From server");
                    return;
                }
            }

            mOtp  = new Gson().fromJson(response,OTP.class);
            if(SERVICE_TYPE.equalsIgnoreCase(ClicConstants.OTP_SERVICE))
            {

                ClicUtils.createPreferences(getApplicationContext(), inputMobile.getText().toString(), R.string.clic_username);
                ClicUtils.createPreferences(getApplicationContext(),inputPassword.getText().toString(), R.string.clic_password);
                ClicUtils.createPreferences(getApplicationContext(), mOtp.getCustomerID(), R.string.clic_ClientID);
                ClicUtils.createPreferences(getApplicationContext(), getString(R.string.clic_guest), R.string.clic_usertype);


                if(type.equalsIgnoreCase(ClicConstants.GUEST_USER))
                {
                    SERVICE_TYPE = ClicConstants.ADD_PRODUCT_SERVICE;
                    mUserItem.setCustomerID(mOtp.getCustomerID());
                    ServiceUtils.postJsonObjectRequest(SignupGuideActvity.this, ServiceConstants.ADD_CUSTOMER_ITEM, myServiceListner, JsonUtils.getJsonString(mUserItem));


                }
                else {
                    finish();
                    Intent knowMore = new Intent(SignupGuideActvity.this, ClicServeHome.class);
                    knowMore.putExtra(getString(R.string.activity_type), getString(R.string.activity_know_more));
                    startActivity(knowMore);

                }

            }
            else if(SERVICE_TYPE.equalsIgnoreCase(ClicConstants.MOIBILE_CHECK_SERVICE) )
            {
                if(response.contains("errorCode"))
                {
                    mobileValidity = false;
                    inputLayoutMobile.setError(getString(R.string.err_msg_mobile_notexist));
                }else {
                    mobileValidity = true;
                    inputLayoutMobile.setErrorEnabled(false);
                }

            }
            else if(SERVICE_TYPE.equalsIgnoreCase(ClicConstants.CUSTOMER_SERVICE))
            {
                OtpValidation otpValidation = new OtpValidation();
                otpValidation.setCustomerOTP(mOtp.getOtpNum());
                otpValidation.setCustomerID(mOtp.getCustomerID());
                SERVICE_TYPE = ClicConstants.OTP_SERVICE;
                ClicUtils.createPreferences(getApplicationContext(),mOtp.getCustomerID(),R.string.clic_ClientID);
                ClicUtils.createPreferences(getApplicationContext(), mOtp.getCustomerID(), R.string.clic_usertype);
                ClicUtils.createOTPValidationDialog(SignupGuideActvity.this,
                        R.layout.otp_validation,
                        otpValidation,
                        myServiceListner,
                        ClicConstants.DIALOG_TYPE_OTP);
            }
            else if(SERVICE_TYPE.equalsIgnoreCase(ClicConstants.CUSTOMER_LOGIN))
            {
                JSONObject lobj = null;
                try {
                    lobj = new JSONObject(response);
                    ClicUtils.createPreferences(getApplicationContext(),lobj.getString("customerID"),R.string.clic_ClientID);
                    ClicUtils.createPreferences(getApplicationContext(), lobj.getString("customerID"), R.string.clic_usertype);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                finish();
                Intent knowMore = new Intent(SignupGuideActvity.this,ClicServeHome.class);
                knowMore.putExtra(getString(R.string.activity_type), getString(R.string.activity_know_more));
                startActivity(knowMore);

            }
            else if(SERVICE_TYPE.equalsIgnoreCase(ClicConstants.ADD_PRODUCT_SERVICE))
            {
                finish();
                mUserItem.setCustomerID(mOtp.getCustomerID());
                Intent intent = new Intent(SignupGuideActvity.this, ClicServeHome.class);
                intent.putExtra(getString(R.string.user_item), mUserItem);
                intent.putExtra(getString(R.string.activity_type), getString(R.string.product_added_success_status));
                startActivity(intent);
                ClicUtils.createPreferences(SignupGuideActvity.this, "true", R.string.is_product_exit);

            }
        }

        @Override
        public void onServiceError(String response) {

            if(SERVICE_TYPE.equalsIgnoreCase(ClicConstants.OTP_SERVICE))
            {

            }
            else if(SERVICE_TYPE.equalsIgnoreCase(ClicConstants.MOIBILE_CHECK_SERVICE))
            {
                inputLayoutMobile.setError(getString(R.string.err_msg_mobile_notexist));
                requestFocus(inputMobile);
            }
            else if(SERVICE_TYPE.equalsIgnoreCase(ClicConstants.CUSTOMER_SERVICE))
            {

            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
