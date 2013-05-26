package org.mixare.data.convert;


import com.geocube.ProjectConstants;
import org.json.JSONException;
import org.json.JSONObject;
import org.mixare.POIMarker;
import org.mixare.data.DataHandler;
import org.mixare.data.DataSource;
import org.mixare.lib.HtmlUnescape;
import org.mixare.lib.marker.Marker;
import ru.spb.osll.json.JsonLoadTagsRequest;
import ru.spb.osll.json.JsonLoadTagsResponse;
import ru.spb.osll.objects.Channel;
import ru.spb.osll.objects.Mark;

import java.util.ArrayList;
import java.util.List;

public class Geo2TagDataProcessor extends DataHandler implements DataProcessor {
    private static String authToken;
    private static double latitude;
    private static double longitude;
    private static double radius;

    public static void setAuthToken(String authToken) {
        Geo2TagDataProcessor.authToken = authToken;
    }

    public static void setLatitude(double latitude) {
        Geo2TagDataProcessor.latitude = latitude;
    }

    public static void setLongitude(double longitude) {
        Geo2TagDataProcessor.longitude = longitude;
    }

    public static void setRadius(double radius) {
        Geo2TagDataProcessor.radius = radius;
    }

    @Override
    public String[] getUrlMatch() {
        String[] str = {"geo2tag"};
        return str;
    }

    @Override
    public String[] getDataMatch() {
        String[] str = {"geo2tag"};
        return str;
    }

    @Override
    public boolean matchesRequiredType(String type) {
        if(type.equals(DataSource.TYPE.GEO2TAG.name())){
            return true;
        }
        return false;
    }

    @Override
    public List<Marker> load(String rawData, int taskId, int colour) throws JSONException {
        List<Marker> markers = new ArrayList<Marker>();
        ArrayList<Channel> channels = new ArrayList<Channel>();

        JsonLoadTagsRequest request = new JsonLoadTagsRequest(authToken, latitude, longitude, radius, ProjectConstants.SERVER_URL);
        JSONObject ob = request.doRequest();
        if (ob != null) {
            JsonLoadTagsResponse resp = new JsonLoadTagsResponse();
            resp.parseJson(ob);

            channels = (ArrayList<Channel>) resp.getChannels();
        }

        if (!channels.isEmpty()) {
            for (Channel ch : channels) {
                for (Mark m : ch.getMarks()) {
                    Marker ma = new POIMarker(
                            m.getTitle(),
                            HtmlUnescape.unescapeHTML(m.getDescription(), 0),
                            m.getLatitude(),
                            m.getLongitude(),
                            m.getAltitude(),
                            m.getLink(),
                            taskId, colour);
                    markers.add(ma);
                }
            }
        }

        return markers;
    }

}
