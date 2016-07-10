package com.at.solcoal.activity;

public class SigninActivity2
//extends AppCompatActivity implements
//        GoogleApiClient.OnConnectionFailedListener,
//        View.OnClickListener
{
//    private static final String TAG = "SignInActivity";
//    private static final int RC_SIGN_IN = 9001;
//
//    private GoogleApiClient mGoogleApiClient;
//    private TextView mStatusTextView;
//    private ProgressDialog mProgressDialog;
//    private String afterLoginAction;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signin);
//        afterLoginAction = getIntent().getStringExtra(AppConstant.AFTER_LOGIN_ACTION);
//
//
//
//        // [START configure_signin]
//        // Configure sign-in to request the user's ID, email address, and basic
//        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        // [END configure_signin]
//
//        // [START build_client]
//        // Build a GoogleApiClient with access to the Google Sign-In API and the
//        // options specified by gso.
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this  /*FragmentActivity*/ , this  /*OnConnectionFailedListener*/ )
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//        // [END build_client]
//
//        // [START customize_button]
//        // Customize sign-in button. The sign-in button can be displayed in
//        // multiple sizes and color schemes. It can also be contextually
//        // rendered based on the requested scopes. For example. a red button may
//        // be displayed when Google+ scopes are requested, but a white button
//        // may be displayed when only basic profile is requested. Try adding the
//        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
//        // difference.
//        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
//        signInButton.setSize(SignInButton.SIZE_STANDARD);
//        signInButton.setScopes(gso.getScopeArray());
//        // [END customize_button]
//
//        // Button listeners
//        findViewById(R.id.sign_in_button).setOnClickListener(this);
//
//
//
//
//    }
//
//
//    /*@Override
//    protected void onResume() {
//        super.onResume();
//        //Call the MainActivity here 
//        Intent mainactivityintent = new Intent(SigninActivity.this, MainActivity.class);
//        startActivity(mainactivityintent);
//    }*/
//        // [START onActivityResult]
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//
//        }
//        //else
//        //   callbackManager.onActivityResult(requestCode, resultCode, data);
//    }
//    // [END onActivityResult]
//
//    // [START handleSignInResult]
//    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
//        if (result.isSuccess()) {
//            // Signed in successfully, show authenticated UI.
//            GoogleSignInAccount acct = result.getSignInAccount();
//            Intent intent = new Intent(SigninActivity2.this, UserLoginInfo.class);
//            UserInfo userInfo = new UserInfo();
//            userInfo.setExtId(acct.getId());
//            userInfo.setName(acct.getDisplayName());
//            userInfo.setEmail(acct.getEmail());
//            userInfo.setExtSource("google");
//            //userInfo.setImageUrl(acct.getPhotoUrl());
//            intent.putExtra("userInfo", userInfo);
//            if(afterLoginAction != null) {
//                intent.putExtra(AppConstant.AFTER_LOGIN_ACTION, afterLoginAction);
//            }
//            startActivity(intent);
//
//        } else {
//           // Signed out, show unauthenticated UI.
//           // updateUI(false);
//        }
//    }
//    // [END handleSignInResult]
//
//    private void showProgressDialog() {
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(this);
//            mProgressDialog.setMessage(getString(R.string.loading));
//            mProgressDialog.setIndeterminate(true);
//        }
//
//        mProgressDialog.show();
//    }
//
//    private void hideProgressDialog() {
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            mProgressDialog.hide();
//        }
//    }
//
//
//    // [START signIn]
//    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//    // [END signIn]
//
//    // [START signOut]
//    private void signOut() {
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        // [START_EXCLUDE]
//                        // updateUI(false);
//                        // [END_EXCLUDE]
//                    }
//                });
//    }
//    // [END signOut]
//
//    // [START revokeAccess]
//    private void revokeAccess() {
//        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        // [START_EXCLUDE]
//                       // updateUI(false);
//                        // [END_EXCLUDE]
//                    }
//                });
//    }
//    // [END revokeAccess]
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
//        // be available.
//        Log.d(TAG, "onConnectionFailed:" + connectionResult);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.sign_in_button:
//                signIn();
//                break;
//            //case R.id.sign_out_button:
//            //    signOut();
//            //    break;
//            //case R.id.disconnect_button:
//            //    revokeAccess();
//            //    break;
//            //case R.id.get_location:
//            //    get_location();
//            //    break;
//            //case R.id.loc:
//            //    get_location();
//            //    break;
//        }
//    }

}
