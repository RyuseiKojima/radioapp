package com.example.radioapp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.ui.Model;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 404 Not Found（存在しない番組など）
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleNotFound(IllegalArgumentException e, Model model) {
        logger.warn("リクエスト対象が見つかりません: {}", e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "error/404";
    }

    /**
     * Springのルーティングエラー（明示的にNoHandlerFoundExceptionが投げられた場合）
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNoHandlerFound(NoHandlerFoundException e, Model model) {
        logger.warn("不正なURLアクセス: {}", e.getRequestURL());
        model.addAttribute("errorMessage", "ページが見つかりませんでした");
        return "error/404";
    }

    /**
     * データベース操作に関する例外
     */
    @ExceptionHandler(DataAccessException.class)
    public String handleDatabaseError(DataAccessException e, Model model) {
        logger.error("データベースエラーが発生しました", e);
        model.addAttribute("errorMessage", "サーバーエラーが発生しました。時間をおいて再度お試しください。");
        return "error/500";
    }

    /**
     * その他の予期しない例外
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.error("予期せぬエラー", e);
        model.addAttribute("errorMessage", "予期せぬエラーが発生しました。");
        return "error/500";
    }
}
