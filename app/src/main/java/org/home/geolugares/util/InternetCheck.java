package org.home.geolugares.util;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Kelvin on 8/12/2017.
 */

public class InternetCheck extends AsyncTask<Void, Void, Boolean> {

    private ConexionCallback mConsumer;

    public interface ConexionCallback {
        void isOnline(Boolean internet);
    }

    public InternetCheck(ConexionCallback consumer) {
        mConsumer = consumer;
        execute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean internet) {
        mConsumer.isOnline(internet);
    }
}
