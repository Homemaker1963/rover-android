package io.rover;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.internal.ParcelableGeofence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Created by ata_n on 2016-03-23.
 */
public class EventService extends IntentService implements JsonApiCompletionHandler {

    private static String TAG = "EventService";

    public EventService() {
        super("EventService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        String applicationToken = Rover.getApplicationToken();

        final Event event = intent.getParcelableExtra("event");



        try {
            NetworkTask networkTask = new NetworkTask("POST", new URL("http://sdfd.com"));

            JsonApiObjectSerializer serializer = new ObjectSerializer(event, getApplicationContext());
            JsonPayloadProvider payloadProvider = new JsonApiPayloadProvider(serializer);

            networkTask.setPayloadProvider(payloadProvider);

            JsonApiObjectMapper mapper = new ObjectMapper(); // TODO: could get this from Rover singleton?
            JsonApiResponseHandler responseHandler = new JsonApiResponseHandler(mapper);
            responseHandler.setCompletionHandler(this);

            networkTask.setResponseHandler(responseHandler);

        } catch (MalformedURLException e) {

        }
    }

    @Override
    public void onHandleCompletion(List includedObject) {

//        // Geofences
//
//        ArrayList<ParcelableGeofence> geofences = new ArrayList<ParcelableGeofence>();
//        for (Object object : includedObject) {
//            if (object instanceof ParcelableGeofence) { geofences.add((ParcelableGeofence)object); }
//        }
//
//        // start service that monitors for geofences
//        Intent intent = new Intent(getApplicationContext(), GoogleApiService.class);
//        intent.putParcelableArrayListExtra("geofences", geofences);
//
//        startService(intent);
//
//        // Messages
    }
}
