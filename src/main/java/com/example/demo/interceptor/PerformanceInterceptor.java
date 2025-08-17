package com.example.demo.interceptor;

import com.example.demo.service.PerformanceMonitoringService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class PerformanceInterceptor implements HandlerInterceptor {
    
    @Autowired
    private PerformanceMonitoringService performanceService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Long startTime = (Long) request.getAttribute("startTime");
        if (startTime != null) {
            long responseTime = System.currentTimeMillis() - startTime;
            performanceService.recordRequest(request.getRequestURI(), responseTime);
        }
    }
}