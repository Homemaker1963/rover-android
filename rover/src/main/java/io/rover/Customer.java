package io.rover;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by ata_n on 2016-03-24.
 */
public class Customer {

    private String mIdentifier;
    private String mName;
    private String mEmail;
    private String mPhoneNumber;
    private String[] mTags;
    private Map<String, Object> mTraits;

    private static Customer mSharedCustomer;
    private static String SHARED_CUSTOMER = "ROVER_SHARED_CUSTOMER";

    public static Customer getInstance(Context context) {

        if (mSharedCustomer != null) {
            return mSharedCustomer;
        }

        mSharedCustomer = pullSharedCustomerFromSharedPrefs(context);
        return mSharedCustomer;
    }

    private Customer() {
        mTraits = new HashMap<>();
    }

    public String getIdentifier() { return mIdentifier; }
    public String getName() { return mName; }
    public String getEmail() { return mEmail; }
    public String getPhoneNumber() { return mPhoneNumber; }
    public String[] getTags() { return mTags; }
    public Map<String, Object> getTraits() { return mTraits; }

    public void setIdentifier(String identifier) { mIdentifier = identifier; }
    public void setName(String name) { mName = name; }
    public void setEmail(String email) { mEmail = email; }
    public void setPhoneNumber(String phoneNumber) { mPhoneNumber = phoneNumber; }
    public void setTags(String[] tags) { mTags = tags; }

    public void save(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_CUSTOMER, 0).edit();
        editor.clear();

        editor.putString("identifier", getIdentifier());
        editor.putString("name", getName());
        editor.putString("email", getEmail());
        editor.putString("phoneNumber", getPhoneNumber());
        if (mTags != null) {
            editor.putStringSet("tags", new HashSet<String>(Arrays.asList(mTags)));
        }
        if (mTraits != null) {
            JSONObject jsonObject = new JSONObject();

            Iterator<String> keys = mTraits.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();

                try {
                    Object value = mTraits.get(key);
                    if (value instanceof Double) {
                        jsonObject.put(key, ((Double) value).doubleValue());
                    } else if (value instanceof String) {
                        jsonObject.put(key, value);
                    }
                } catch (JSONException e) {
                    Log.e("Customer", "Error storing traits");
                }
            }

            editor.putString("traits", jsonObject.toString());
        }
        editor.commit();
    }

    private static Customer pullSharedCustomerFromSharedPrefs(Context context) {
        Customer customer = new Customer();

        SharedPreferences sharedData = context.getSharedPreferences(SHARED_CUSTOMER, 0);
        customer.mIdentifier = sharedData.getString("identifier", null);
        customer.mName = sharedData.getString("name", null);
        customer.mEmail = sharedData.getString("email", null);
        customer.mPhoneNumber = sharedData.getString("phoneNumber", null);

        Set<String> tagsSet = sharedData.getStringSet("tags", null);
        if (tagsSet != null) {
            customer.mTags = tagsSet.toArray(new String[tagsSet.size()]);
        }

        String traitsString = sharedData.getString("traits", null);
        if (traitsString != null) {

            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(traitsString);
            } catch (JSONException e) {
                Log.e("Customer", "Invalid traits");
            }

            if (jsonObject != null) {
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();

                    try {
                        double number = jsonObject.getDouble(key);

                        customer.mTraits.put(key, new Double(number));
                    } catch (JSONException e) {
                        String string = jsonObject.optString(key, "");

                        customer.mTraits.put(key, string);
                    }
                }
            }
        }

        return customer;
    }
}
