package org.starcoin.starswap.api.system;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.starcoin.starswap.api.vo.ResultUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class UrlFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(Filter.class);

    private final static String[] URI_NOT_FILTER = new String[]{"/v1/starswap", "swagger", "v3/api-docs"};

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = ((HttpServletRequest) request).getRequestURI();
        if (isFilter(uri)) {
            chain.doFilter(request, response);
        } else {
            logger.info("拦截URI：{}", uri);
            doResponse(response);
        }
    }

    @Override
    public void destroy() {
    }

    private boolean isFilter(String uri) {
        for (String str : URI_NOT_FILTER) {
            if (uri.indexOf(str) == 0) {
                return true;
            }
        }
        return false;
    }

    private void doResponse(ServletResponse response) {
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JSONObject.toJSONString(ResultUtils.failure()));
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
