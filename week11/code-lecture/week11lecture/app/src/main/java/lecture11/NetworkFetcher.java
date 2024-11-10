// Http connection to server at address given to getUrlBytes
// jst@itu.dk * 2024-06-30


/* Most of this class is copied from
// Android Programming: The Big Nerd Ranch Guide
//by Bill Phillips, Chris Stewart and Kristin Marsicano Chapter 25 */

package lecture11;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkFetcher {

  public byte[] getUrlBytes(String urlSpec) throws IOException {
    URL url= new URL(urlSpec);
    HttpURLConnection connection= (HttpURLConnection)url.openConnection();
    try {
      ByteArrayOutputStream out= new ByteArrayOutputStream();
      InputStream in= connection.getInputStream();
      if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
        throw new IOException(connection.getResponseMessage() +
            ": with " +  urlSpec);
      }
      int bytesRead= 0;
      byte[] buffer= new byte[1024];
      while ((bytesRead = in.read(buffer)) > 0) {
        out.write(buffer, 0, bytesRead);
      }
      out.close();
      return out.toByteArray();
    } finally {
      connection.disconnect();
    }
  }

  public String getUrlString(String url) {
    try {
      return new String(getUrlBytes(url));
    } catch (IOException ioe) { };
    return null;
  }
}

