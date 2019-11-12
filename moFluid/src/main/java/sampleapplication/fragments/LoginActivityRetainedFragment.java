package sampleapplication.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import sampleapplication.LoginActivity2;
import sampleapplication.receivers.AnetResultReceiver;
import sampleapplication.services.AnetIntentService;

/**
 * Starts the AnetIntentService to authenticate a user and listens to the results of the
 * authentication transaction (from the result receiver) to send back via a callback to the activity.
 */
public class LoginActivityRetainedFragment extends Fragment
        implements AnetResultReceiver.ReceiverCallback {

    private AnetResultReceiver resultReceiver;
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onReceiveAuthenticateUserResult(Bundle resultData);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        resultReceiver = new AnetResultReceiver(new Handler());
        resultReceiver.setReceiverCallback(this);
        Bundle credentials = getArguments();
        if (credentials!= null) {
            String loginId = credentials.getString(LoginActivity2.LOGIN_ID_TAG);
            String password = credentials.getString(LoginActivity2.PASSWORD_TAG);
            startServiceAuthenticateUser(loginId, password);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (resultReceiver != null)
            resultReceiver.clearReceiverCallback();
    }

    /**
     * Starts the service to authenticate a user
     * Pre: loginId and password are not empty
     * @param loginId username for the account entered in the EditText
     * @param password password for the account entered in the EditText
     * @return whether the service was successfully started
     */
    public boolean startServiceAuthenticateUser(String loginId, String password) {
        try {
            Intent intent = new Intent(getActivity(), AnetIntentService.class);
            intent.setAction(AnetIntentService.ACTION_AUTHENTICATE_USER);
            intent.putExtra(LoginActivity2.LOGIN_ID_TAG, loginId);
            intent.putExtra(LoginActivity2.PASSWORD_TAG, password);
            intent.putExtra(AnetResultReceiver.RESULT_RECEIVER_TAG, resultReceiver);
            getActivity().startService(intent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Uses a callback to propagate the result of the service request back to the activity
     * @param resultCode result code for the transaction defined in and delivered by the service
     * @param resultData the result of the transaction and the full array list of transactions
     */
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == AnetIntentService.AUTHENTICATE_USER_RESULT_CODE)
            mListener.onReceiveAuthenticateUserResult(resultData);
    }
}
