import java.io.IOException;
import java.io.Reader;

public class UserLevelTest {

    public static void main(String[] args) {
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = readJsonFromUrl("https://tipo.live/api/v2/payments?token=CdKLsY6SL8vq31jF17aN&status=completed&paid_at_min_timestamp=2020-05-20T11:45:54.000000Z");
//            //2020-05-20T11:45:54.000000Z
//            //2020-05-20T11:45:54.000000Z
//            System.out.println(jsonObject.toString());
//            System.out.println("=============================================");
//            JSONArray array = jsonObject.getJSONArray("data");
//            System.out.println("=============================================");
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
//            GregorianCalendar gc = new GregorianCalendar();
//            for (int i = 0; i < array.length(); i++) {
//                gc.setTime(sdf.parse(array.getJSONObject(i).getString("paid_at")));
//                ; // 2009-07-16T19:20:30-05:00
//                System.out.println(sdf.format(gc.getTime()));
//                System.out.println(i + " " + array.getJSONObject(i).getString("name") + " " + ChatUtil.getNumber((double) array.getJSONObject(i).getInt("amount") / 100) + " wPLN");
//            }
//            System.out.println(array.toString());
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

//    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
//        InputStream is = new URL(url).openStream();
//        try {
//            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
//            String jsonText = readAll(rd);
//            return new JSONObject(jsonText);
//        } finally {
//            is.close();
//        }
//    }

}
