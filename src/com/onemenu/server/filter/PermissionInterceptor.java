package com.onemenu.server.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.onemenu.server.controller.AbstractController;


/**
 * @author linhang
 * 
 *         <p>
 *         权限过滤器
 *         </p>
 * 
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter implements AbstractController {
    // @Override
    // public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
    // Object handler) throws Exception {
    // String url = request.getRequestURL().toString();
    // boolean result = true;
    //
    // HttpSession session = request.getSession();
    // // 非登录页面才需要进行是否登录的过滤
    // if (!url.contains("main/loginController")) {
    // Account account = (Account) session.getAttribute(SessionKey.LoginBean.toString());
    //
    // if (account == null) {
    // // 没有登录
    // result = false;
    // String newUrl =
    // request.getScheme() + "://" + request.getServerName() + ":"
    // + request.getServerPort() + "/" + request.getContextPath();
    // response.sendRedirect(newUrl);
    // }
    // }
    // return result;
    // }

    private Logger log = Logger.getLogger(PermissionInterceptor.class);

    public PermissionInterceptor() {
        // TODO Auto-generated constructor stub
    }

    private String mappingURL;// 利用正则映射到需要拦截的路径

    public void setMappingURL(String mappingURL) {
        this.mappingURL = mappingURL;
    }

    /**
     * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
     * 
     * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链, 从最后一个拦截器往回执行所有的postHandle()
     * 接着再从最后一个拦截器往回执行所有的afterCompletion()
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
    	if(log.isDebugEnabled())
    		log.debug("==============Seqences: 1.preHandle================");
    	
    	request.setAttribute("userTime", System.currentTimeMillis());
    	
        String url = request.getRequestURL().toString();
        // if (mappingURL == null || url.matches(mappingURL)) {
        // request.getRequestDispatcher("/msg.jsp").forward(request, response);
        // return false;
        // }
        HttpSession session = request.getSession();
        // 非登录页面才需要进行是否登录的过滤
//        if (!url.contains("main/loginController") && !url.contains("main/pageController")) {
//
//            Account account = (Account) session.getAttribute(SessionKey.LoginBean.toString());
//            if (account == null) {
//                // 没有登录
//                String newUrl =
//                        request.getScheme() + "://" + request.getServerName() + ":"
//                                + request.getServerPort() + "/" + request.getContextPath();
//                log.info(newUrl);
//                response.sendRedirect(newUrl);
//            }
//        }

        return true;
    }

    // 在业务处理器处理请求执行完成后,生成视图之前执行的动作
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView) throws Exception {
    	if(log.isDebugEnabled())
    		log.debug("==============Seqences: 2.postHandle================");
    	
    	Object userTime = request.getAttribute("userTime");
    	
    	if(userTime!=null && userTime instanceof Long){
    		long _userTime = (Long) userTime;
    		long curTime = System.currentTimeMillis()  ;
    		if((curTime -_userTime)>3000){
    			String url = request.getRequestURL().toString();
    			log.error("timeout----url:"+url+ ",times:"+(curTime-_userTime));
    		}
    	}
    }

    /**
     * 在DispatcherServlet完全处理完请求后被调用
     * 
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
    	if(log.isDebugEnabled())
    		log.debug("==============Seqences: 3.afterCompletion================");
    }

}
