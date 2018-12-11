package com.partyboxAPI;

import android.annotation.SuppressLint;

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
        String serverIP = "10.0.0.121";
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
        } catch (PartyBoxException e) {
            e.printStackTrace();
        }

    }
}
