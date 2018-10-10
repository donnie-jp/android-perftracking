package com.rakuten.tech.mobile.perf.core.base;

import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.rakuten.tech.mobile.perf.core.Tracker;

public class WebViewClientBase extends WebViewClient {

  public int com_rakuten_tech_mobile_perf_page_trackingId;
  public String com_rakuten_tech_mobile_perf_requestMethod = "VIEW";

  public void onPageStarted(WebView view, String url, Bitmap favicon) {
    Tracker.prolongMetric();
    if (com_rakuten_tech_mobile_perf_page_trackingId == 0) {
      com_rakuten_tech_mobile_perf_page_trackingId = Tracker.startUrl(url, com_rakuten_tech_mobile_perf_requestMethod);
    }
    super.onPageStarted(view, url, favicon);
  }

  public void onPageFinished(WebView view, String url) {
    Tracker.prolongMetric();
    if (com_rakuten_tech_mobile_perf_page_trackingId != 0) {
      Tracker.endUrl(com_rakuten_tech_mobile_perf_page_trackingId, 200);
      com_rakuten_tech_mobile_perf_page_trackingId = 0;
    }
    super.onPageFinished(view, url);
  }

  public void onReceivedHttpError(WebView view, WebResourceRequest request,
      WebResourceResponse errorResponse) {
    Tracker.prolongMetric();
    if (com_rakuten_tech_mobile_perf_page_trackingId != 0) {
      Tracker.endUrl(com_rakuten_tech_mobile_perf_page_trackingId, errorResponse.getStatusCode());
      com_rakuten_tech_mobile_perf_page_trackingId = 0;
    }
    super.onReceivedHttpError(view, request, errorResponse);
  }

  public WebResourceResponse shouldInterceptRequest (WebView view, WebResourceRequest request) {
    com_rakuten_tech_mobile_perf_requestMethod = request.getMethod();
    return super.shouldInterceptRequest(view, request);
  }
}
