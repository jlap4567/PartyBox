package com.partyboxAPI;

import android.annotation.SuppressLint;
import android.util.Log;

import com.partyboxAPI.exceptions.PartyBoxException;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MessageThread implements Runnable {

    @SuppressLint("NewApi")
    @Override
    public void run() {

        Party party = PartyFactory.getNewOrCurrentParty();
        Socket socket;
        OutputStreamWriter writer;
        JSONObject object;
        String serverIP = "73.10.137.124";
        int port = 9000;

        try {
            object = party.getOrderInfo().toJSON().getObject();
            socket = new Socket(serverIP, port);
            writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
            writer.write(object.toString());
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("xxxxx", e.getMessage());
        } catch (PartyBoxException e) {
            e.printStackTrace();
            Log.e("xxxxx", e.getMessage());
        }

    }
}
