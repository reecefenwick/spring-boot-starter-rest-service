package au.com.reecefenwick.api.rest.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

@Configuration
public class HttpRequestResponseMetricFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestResponseMetricFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)  servletRequest;

        long startTime = Calendar.getInstance().getTimeInMillis();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        int reqContentLength = request.getContentLength();
        String remoteIP = request.getRemoteAddr();
        String remoteHost = request.getRemoteHost();

        try {
            chain.doFilter(servletRequest, servletResponse);
        } finally {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            long finishTime = Calendar.getInstance().getTimeInMillis();
            long elapsedTime = finishTime - startTime;
            int responseCode = response.getStatus();
            int responseSize = response.getBufferSize();

            log.info("Request made to server uri={} method={} reqContentLength={} finishTime={} elapsedTime={} " +
                    "responseCode={} remoteIP={} remoteHost={}", uri, method, reqContentLength, remoteIP, remoteHost,
                    finishTime, elapsedTime, responseCode, responseSize);
        }
    }
}
