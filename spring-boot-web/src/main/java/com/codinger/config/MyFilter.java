package com.codinger.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
/**
 * 过滤器：Filter是Servlet技术中最实用的技术，Web开发人员通过Filter技术，对web服务器管理的所有web资源：
 * 例如Jsp, Servlet, 静态图片文件或静态 html 文件等进行拦截，从而实现一些特殊的功能。
 * 例如实现URL级别的权限访问控制、过滤敏感词汇、压缩响应信息等一些高级功能。它主要用于对用户请求进行预处理，
 * 也可以对HttpServletResponse进行后处理。使用Filter的完整流程：Filter对用户请求进行预处理，
 * 接着将请求交给Servlet进行处理并生成响应，最后Filter再对服务器响应进行后处理。
 * @author Administrator
 *
 */
public class MyFilter implements Filter {
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		// 将请求转换成HttpServletRequest 请求 
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        System.out.println("this is MyFilter,url :"+req.getRequestURI());
        String path = req.getContextPath();
        String basePath = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+path;
        HttpSession session = req.getSession(true);
        String username = (String) session.getAttribute("username");
        if (req.getRequestURI().indexOf("index.html") == -1 &&(username == null || "".equals(username))) {
            resp.setHeader("Cache-Control", "no-store");
            resp.setDateHeader("Expires", 0);
            resp.setHeader("Prama", "no-cache");
            //此处设置了访问静态资源类
            resp.sendRedirect(basePath+"/index.html");
        } else {
            // Filter 只是链式处理，请求依然转发到目的地址。 
            filterChain.doFilter(req, resp);
        }
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}
}
