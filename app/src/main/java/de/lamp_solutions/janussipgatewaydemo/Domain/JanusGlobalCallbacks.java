package de.lamp_solutions.janussipgatewaydemo.Domain;



import android.util.Log;

import org.webrtc.PeerConnection;

import java.util.ArrayList;
import java.util.List;

import computician.janusclientapi.IJanusGatewayCallbacks;
import de.lamp_solutions.janussipgatewaydemo.MainActivity;

/**
 * handle janus-Servers callbacks
 */
public class JanusGlobalCallbacks implements IJanusGatewayCallbacks {

    private static final String TAG = "JanusGlobalCallbacks";


    /**
     * url to janus server
     */
    public String janus_uri = null;


    /**
     * context initializing this callback
     */
    public SipGateway sipgateway;

    public MainActivity callActivity;

    public JanusPluginCallbacks pluginCallbacks;


    @Override
    public void onSuccess() {
        pluginCallbacks.setCallActivity(callActivity);
        sipgateway.janusServer.Attach(pluginCallbacks);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public String getServerUri() {
        Log.v(TAG,"uri:" + janus_uri);
        return janus_uri;
    }

    @Override
    public List<PeerConnection.IceServer> getIceServers() {
        return new ArrayList<PeerConnection.IceServer>();
    }

    @Override
    public Boolean getIpv6Support() {
        return Boolean.FALSE;
    }

    @Override
    public Integer getMaxPollEvents() {
        return 0;
    }

    @Override
    public void onCallbackError(String error) {

    }


}
