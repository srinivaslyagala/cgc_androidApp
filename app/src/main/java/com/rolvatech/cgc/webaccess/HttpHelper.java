package com.rolvatech.cgc.webaccess;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;


import com.rolvatech.cgc.utils.Methods;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Girish Velivela on 11-07-2016.
 */
public class HttpHelper {

    private final String LOG_TAG = "HttpHelper";

    private int TIMEOUT_CONNECT_MILLIS = (2 * 60 * 1000);
    private int TIMEOUT_READ_MILLIS = TIMEOUT_CONNECT_MILLIS - 5000;

    @SuppressLint({"SSLCertificateSocketFactoryGetInsecure", "AllowAllHostnameVerifier"})
    public InputStream sendRequest(String requestUrl, String method, Map<String, String> headers, String postData) {

        Log.e(LOG_TAG, "sendRequest - Started");
        Log.e(LOG_TAG, "requestUrl - " + requestUrl);
        URL url;
        HttpURLConnection connection;

        try {
            url = new URL(requestUrl);


            if (url.getProtocol().toLowerCase().equals("https")) {
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                HttpsTrustManager.allowAllSSL();
                //https.setSSLSocketFactory(createSSLSocketFactory());
//                try {
//                    TrustManager[] victimizedManager = new TrustManager[]{
//
//                            new X509TrustManager() {
//
//                                public X509Certificate[] getAcceptedIssuers() {
//
//                                    X509Certificate[] myTrustedAnchors = new X509Certificate[0];
//
//                                    return myTrustedAnchors;
//                                }
//
//                                @Override
//                                public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                                }
//
//                                @Override
//                                public void checkServerTrusted(X509Certificate[] certs, String authType) {
//                                }
//                            }
//                    };
//
//                    SSLContext sc = SSLContext.getInstance("SSL");
//                    sc.init(null, victimizedManager, new SecureRandom());
//                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//                    HostnameVerifier allHostsValid = new HostnameVerifier() {
//                        @SuppressLint("BadHostnameVerifier")
//                        public boolean verify(String hostname, SSLSession session) {
//                            return true;
//                        }
//                    };
//
////                    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
////                        @Override
////                        public boolean verify(String s, SSLSession sslSession) {
////                            return true;
////                        }
////                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                connection = https;
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }
//            if (connection instanceof HttpsURLConnection) {
//                HttpsURLConnection httpsConn = (HttpsURLConnection) connection;
//                httpsConn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
//                httpsConn.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//                httpsConn.setRequestMethod(method);
//                httpsConn.setConnectTimeout(TIMEOUT_CONNECT_MILLIS);
//                httpsConn.setDoInput(true);
//                if (headers != null && headers.size() > 0) {
//                    Set<String> keySet = headers.keySet();
//                    for (String key : keySet)
//                        httpsConn.setRequestProperty(key, headers.get(key));
//                }
//                if (!TextUtils.isEmpty(postData)) {
//                    httpsConn.setDoOutput(true);
//                    OutputStream outputStream = httpsConn.getOutputStream();
//                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//                    writer.write(postData);
//                    writer.flush();
//                    writer.close();
//                    outputStream.close();
//                }
//                int httpCode = httpsConn.getResponseCode();
//                String resMsg = httpsConn.getResponseMessage();
//                Log.e(LOG_TAG, "Response Code - " + httpCode);
//                Log.e(LOG_TAG, "Response Message - " + resMsg);
//                return httpsConn.getInputStream();
//            }
//            else
//            {
            connection.setRequestMethod(method);
            connection.setConnectTimeout(TIMEOUT_CONNECT_MILLIS);
            connection.setDoInput(true);
            if (headers != null && headers.size() > 0) {
                Set<String> keySet = headers.keySet();
                for (String key : keySet)
                    connection.setRequestProperty(key, headers.get(key));
            }
            if (!TextUtils.isEmpty(postData)) {
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(postData);
                writer.flush();
                writer.close();
                outputStream.close();
            }
            int httpCode = connection.getResponseCode();
            String resMsg = connection.getResponseMessage();
            Log.e(LOG_TAG, "Response Code - " + httpCode);
            Log.e(LOG_TAG, "Response Message - " + resMsg);
            return connection.getInputStream();
            //  }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Log.e(LOG_TAG, "sendRequest - Ended");
        }
        return null;
    }
//    @Override
//    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
//        SSLSocket sslSocket = (SSLSocket)sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
//        getHostnameVerifier().verify(host, sslSocket);
//        return sslSocket;
//    }

    public String uploadImage(String baseUrl, String file) {

        URL url;
        HttpURLConnection connection = null;

        try {

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            url = new URL(baseUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            File uploadFile = new File(file);

            FileInputStream fileInputStream = new FileInputStream(uploadFile);
            DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
            MimeTypeMap map = MimeTypeMap.getSingleton();

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer
                    .append(twoHyphens + boundary + lineEnd)
                    .append("Content-Disposition: form-data; name=\"photo\";filename=\"" + uploadFile.getName() + "\"")
                    .append(lineEnd)
                    .append("Content-Type: " + map.getMimeTypeFromExtension(Methods.getExtentionOfFile(file)))
                    .append(lineEnd + lineEnd);

            Log.e("upload", stringBuffer.toString());
            writer.writeBytes(stringBuffer.toString());
            byte[] buffer = new byte[1024];
            int length;

            while ((length = fileInputStream.read(buffer, 0, buffer.length)) > 0) {
                writer.write(buffer, 0, length);
            }
            writer.writeBytes(lineEnd + lineEnd);
            writer.writeBytes(twoHyphens + boundary + twoHyphens);
            writer.flush();
            writer.close();

            int httpCode = connection.getResponseCode();
            String resMsg = connection.getResponseMessage();
            Log.e(LOG_TAG, "Response Code - " + httpCode);
            Log.e(LOG_TAG, "Response Message - " + resMsg);
            if (httpCode == 200) {
               /* String responseStr = StringUtils.convertStreamToString(connection.getInputStream()).toString();
                Log.e("responseStr",responseStr);

                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                InputSource src = new InputSource();
                src.setCharacterStream(new StringReader(responseStr));

                Document doc = builder.parse(src);
                String message = doc.getElementsByTagName("Message").item(0).getTextContent();
                String Url = doc.getElementsByTagName("FileName").item(0).getTextContent();

                if(!StringUtils.isEmpty(message) && message.equalsIgnoreCase("successful"))
                {
                    return Url;
                }*/
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        File crtFile = new File("server.crt");
        Certificate certificate = null;

        try {
            certificate = CertificateFactory.getInstance("X.509").generateCertificate(new FileInputStream(crtFile));
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            assert keyStore != null;
            keyStore.load(null, null);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            keyStore.setCertificateEntry("server", certificate);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        TrustManagerFactory trustManagerFactory = null;
        try {
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            assert trustManagerFactory != null;
            trustManagerFactory.init(keyStore);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            assert sslContext != null;
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sslContext.getSocketFactory();


    }

    public void close(InputStream inputStream) {
        Log.e(LOG_TAG, "close - Started");
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Exception e) {
                Log.e(LOG_TAG, e.toString());
                e.printStackTrace();
            }
        }
        Log.e(LOG_TAG, "close - Ended");
    }

}
